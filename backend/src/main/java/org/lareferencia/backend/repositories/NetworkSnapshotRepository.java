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
package org.lareferencia.backend.repositories;

import java.util.List;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.repository.annotation.RestResource;

@RestResource(path = "snapshot", rel="snapshot")
public interface NetworkSnapshotRepository extends JpaRepository<NetworkSnapshot, Long> { 
	
	  @Query("select ns from NetworkSnapshot ns where ns.network.id = ?1 and ns.status = 9 and ns.endTime >= (select max(s.endTime) from NetworkSnapshot s where s.network.id = ?1 and s.status = 9)")
	  NetworkSnapshot findLastGoodKnowByNetworkID(Long networkID);
	  
	  List<NetworkSnapshot> findByNetworkAndStatusOrderByEndTimeAsc(NationalNetwork network, SnapshotStatus status);
	  List<NetworkSnapshot> findByNetworkOrderByEndTimeAsc(NationalNetwork network);

	  
}
