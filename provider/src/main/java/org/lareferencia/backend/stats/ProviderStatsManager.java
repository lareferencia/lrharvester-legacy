package org.lareferencia.backend.stats;

import org.lareferencia.backend.domain.OAIProviderStat;
import org.lareferencia.backend.repositories.OAIProviderStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class ProviderStatsManager {
	
	public ProviderStatsManager() {
		super();
	}

	@Autowired
	OAIProviderStatRepository providerStatRepository;
	
	@Transactional
	public void countIPAccess(String ipAddress) {
		
		OAIProviderStat stat = providerStatRepository.findOne(ipAddress);
		
		if ( stat == null ) 
			stat = new OAIProviderStat(ipAddress);
		
		stat.incrementRequestCounter();
		
		providerStatRepository.save(stat);
		
	}

	
}
