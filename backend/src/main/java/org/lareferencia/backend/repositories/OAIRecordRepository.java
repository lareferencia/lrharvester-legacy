package org.lareferencia.backend.repositories;

import org.lareferencia.backend.domain.OAIRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAIRecordRepository extends JpaRepository<OAIRecord, Long> { 
}