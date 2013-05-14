package org.lareferencia.backend.repositories;

import java.util.List;

import org.lareferencia.backend.domain.NetworkSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NetworkSnapshotRepository extends JpaRepository<NetworkSnapshot, Long> { 
	
	  @Query("select ns from NetworkSnapshot ns where ns.network.id = ?1 and ns.status = 4 and ns.endTime = (select max(s.endTime) from NetworkSnapshot s where s.network.id = ?1 and ns.status = 4)")
	  NetworkSnapshot findLastGoodKnowByNetworkID(Long networkID);
}