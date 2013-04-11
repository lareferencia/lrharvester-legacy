package org.lareferencia.backend.repositories;

import org.lareferencia.backend.domain.ValidationFieldLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationFieldLogRepository extends JpaRepository<ValidationFieldLogEntry, Long> { 
}