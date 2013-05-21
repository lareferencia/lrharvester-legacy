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

	
	 @Modifying
	 @Transactional
	 @Query("delete from OAIRecord r where r.snapshot.id = ?1")
	 void deleteBySnapshotID(Long snapshot_id);
	
}