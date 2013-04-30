package org.lareferencia.backend.repositories;

import java.util.List;

import org.lareferencia.backend.domain.InvalidOccurrenceLogEntry;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidOccurrenceLogRepository extends JpaRepository<InvalidOccurrenceLogEntry, Long> { 
	
	List<InvalidOccurrenceLogEntry> findBySnapID(Long snapID);

	
}