package org.lareferencia.backend.repositories;

import org.lareferencia.backend.domain.OAIProviderStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAIProviderStatRepository extends JpaRepository<OAIProviderStat, String> { 
	
	//OAIProviderStat findByIpAddress(String ipAddress);
}