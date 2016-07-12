/*******************************************************************************
 * Copyright (c) 2013 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 *     Emiliano Marmonti(emarmonti@gmail.com) - Coordinación del componente III
 * 
 * Este software fue desarrollado en el marco de la consultoría "Desarrollo e implementación de las soluciones - Prueba piloto del Componente III -Desarrollador para las herramientas de back-end" del proyecto “Estrategia Regional y Marco de Interoperabilidad y Gestión para una Red Federada Latinoamericana de Repositorios Institucionales de Documentación Científica” financiado por Banco Interamericano de Desarrollo (BID) y ejecutado por la Cooperación Latino Americana de Redes Avanzadas, CLARA.
 ******************************************************************************/
package org.lareferencia.backend.util;

import static org.springframework.orm.jpa.EntityManagerFactoryUtils.ENTITY_MANAGER_SYNCHRONIZATION_ORDER;
import static org.springframework.util.ReflectionUtils.invokeMethod;

import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.transaction.spi.TransactionContext;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.support.ResourceHolderSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Hibernate's {@link StatelessSession} factory which will be bound to the
 * current transaction. This factory returns a Proxy which delegates method
 * calls to the underlying {@link StatelessSession} bound to transaction. At the
 * end of the transaction the session is automatically closed. This class
 * borrows idea's from {@link DataSourceUtils},
 * {@link EntityManagerFactoryUtils}, {@link ResourceHolderSynchronization} and
 * {@link LocalEntityManagerFactoryBean}.
 */
public class StatelessSessionFactoryBean implements FactoryBean<StatelessSession> {

	private final HibernateEntityManagerFactory entityManagerFactory;
	private SessionFactory sessionFactory;

	@Autowired
	public StatelessSessionFactoryBean(HibernateEntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.sessionFactory = entityManagerFactory.getSessionFactory();
	}

	/**
	 * Use this to override the {@link SessionFactory} obtained from the
	 * {@link EntityManagerFactory}. Please note that the connection will still
	 * be used from the {@link EntityManager}.
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public StatelessSession getObject() throws Exception {
		StatelessSessionInterceptor statelessSessionInterceptor = new StatelessSessionInterceptor(entityManagerFactory, sessionFactory);
		return ProxyFactory.getProxy(StatelessSession.class, statelessSessionInterceptor);
	}

	@Override
	public Class<?> getObjectType() {
		return StatelessSession.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	private static class StatelessSessionInterceptor implements MethodInterceptor {

		private final EntityManagerFactory entityManagerFactory;
		private final SessionFactory sessionFactory;

		public StatelessSessionInterceptor(EntityManagerFactory entityManagerFactory, SessionFactory sessionFactory) {
			this.entityManagerFactory = entityManagerFactory;
			this.sessionFactory = sessionFactory;
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			StatelessSession statelessSession = getCurrentSession();
			return invokeMethod(invocation.getMethod(), statelessSession, invocation.getArguments());
		}

		private StatelessSession getCurrentSession() {
			if (!TransactionSynchronizationManager.isActualTransactionActive()) {
				throw new IllegalStateException("There should be an active transaction for the current thread.");
			}
			StatelessSession statelessSession = (StatelessSession) TransactionSynchronizationManager.getResource(sessionFactory);
			if (statelessSession == null) {
				statelessSession = openNewStatelessSession();
				bindWithTransaction(statelessSession);
			}
			return statelessSession;
		}

		private StatelessSession openNewStatelessSession() {
			Connection connection = obtainPhysicalConnection();
			return sessionFactory.openStatelessSession(connection);
		}

		/**
		 * It is important we obtain the physical (real) connection otherwise it
		 * will be double proxied and there will be problems releasing the
		 * connection.
		 */
		private Connection obtainPhysicalConnection() {
			EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
			SessionImplementor sessionImplementor = (SessionImplementor) entityManager.getDelegate();
			return sessionImplementor.getTransactionCoordinator().getJdbcCoordinator().getLogicalConnection().getConnection();
		}

		private void bindWithTransaction(StatelessSession statelessSession) {
			TransactionSynchronizationManager.registerSynchronization(new StatelessSessionSynchronization(sessionFactory, statelessSession));
			TransactionSynchronizationManager.bindResource(sessionFactory, statelessSession);
		}
	}

	private static class StatelessSessionSynchronization extends TransactionSynchronizationAdapter {

		private final SessionFactory sessionFactory;
		private final StatelessSession statelessSession;

		public StatelessSessionSynchronization(SessionFactory sessionFactory, StatelessSession statelessSession) {
			this.sessionFactory = sessionFactory;
			this.statelessSession = statelessSession;
		}

		@Override
		public int getOrder() {
			return ENTITY_MANAGER_SYNCHRONIZATION_ORDER - 100;
		}

		@Override
		public void beforeCommit(boolean readOnly) {
			if (!readOnly) {
				((TransactionContext) statelessSession).managedFlush();
			}
		}

		@Override
		public void beforeCompletion() {
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			statelessSession.close();
		}

	}

}
