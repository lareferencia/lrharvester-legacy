package org.lareferencia.backend.repositories;

import org.lareferencia.backend.domain.InvalidOccurrenceLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidOccurrenceLogRepository extends JpaRepository<InvalidOccurrenceLogEntry, Long> { 
}