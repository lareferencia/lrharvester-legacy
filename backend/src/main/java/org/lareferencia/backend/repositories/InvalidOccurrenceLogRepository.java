package org.lareferencia.backend.repositories;

import java.util.List;

import org.lareferencia.backend.domain.InvalidOccurrenceLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidOccurrenceLogRepository extends JpaRepository<InvalidOccurrenceLogEntry, Long> { 
	
	List<InvalidOccurrenceLogEntry> findBySnapID(Long snapID);

	
}