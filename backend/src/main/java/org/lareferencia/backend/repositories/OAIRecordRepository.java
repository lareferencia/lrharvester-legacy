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


import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 
 * @author lmatas
 * 
 * Se define un repositorio JPA de acuerdo a Spring Data, en el caso de records
 * no es recomendable usarlo para bulk inserts ya que el consumo de memoria resulta
 * prohibitivo. Se implementa OAIRecordDAO para ese fin.
 * 
 */
public interface OAIRecordRepository extends JpaRepository<OAIRecord, Long> { 
	
	 Page<OAIRecord> findBySnapshotAndStatus(NetworkSnapshot snapshot, RecordStatus status, Pageable pageable);
	 Page<OAIRecord> findBySnapshot(NetworkSnapshot snapshot, Pageable pageable);
	 
	 // Esta query sirve para obtener el total de páginas
	 @Query("select rc from OAIRecord rc where rc.snapshot.id = ?1")
	 Page<OAIRecord> findBySnapshotId(Long snapshotID, Pageable pageable);

	 // Esta query sirve para obtener el total de páginas
     @Query("select rc from OAIRecord rc where rc.snapshot.id = ?1 and rc.status=?2")
	 Page<OAIRecord> findBySnapshotIdAndStatus(Long snapshotID, RecordStatus status, Pageable pageable);

	 /***
	  * 
	  * Debido a que las operaciones con limit en mysql son suboptimas se implementa
	  * un mecanismo para evitar que se escaneen todos los registros en cada paginación.
	  * La idea es utilizar el ultimo id para generar siempre páginas con 0 offset
	  * 
	  * UPDATE: Es necesario encontrar una forma de garantizar el orden de los ids para poder implementar esta solución
	  * de otro modo el sort consume más tiempo que el paginado
	  *
	  */
	 /**
	
	 // Esta es la query que retorna la página con offset 0 si se le provee el ultimo id anterior
	 @Query("select rc from OAIRecord rc where rc.snapshot.id = ?1 and rc.id > ?2 order by rc.id asc")
	 Page<OAIRecord> findBySnapshotIdLimited(Long snapshotID, Long previousOAIRecord, Pageable pageable);
	 
	 
	 // Esta es la query que retorna la página con offset 0 si se le provee el ultimo id anterior
	 @Query("select rc from OAIRecord rc where rc.snapshot.id = ?1 and rc.status=?2 and rc.id > ?3 order by rc.id asc")
	 Page<OAIRecord> findBySnapshotIdAndStatusLimited(Long snapshotID, RecordStatus status, Long previousID, Pageable pageable);
     **/
	 
	 @Modifying
	 @Transactional
	 @Query("delete from OAIRecord r where r.snapshot.id = ?1")
	 void deleteBySnapshotID(Long snapshot_id);
	
}
