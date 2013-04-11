package org.lareferencia.backend.repositories;

import org.lareferencia.backend.domain.ValidationRuleLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationRuleLogRepository extends JpaRepository<ValidationRuleLogEntry, Long> { 
}