package org.lareferencia.backend.repositories;

import org.lareferencia.backend.domain.OAIRecord;
import org.springframework.data.jpa.repository.JpaRepository;

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
}