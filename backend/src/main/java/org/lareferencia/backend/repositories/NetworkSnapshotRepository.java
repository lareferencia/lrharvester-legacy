package org.lareferencia.backend.repositories;

import java.util.List;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NetworkSnapshotRepository extends JpaRepository<NetworkSnapshot, Long> { 
	
	  @Query("select ns from NetworkSnapshot ns where ns.network.id = ?1 and ns.status = 9 and ns.endTime >= (select max(s.endTime) from NetworkSnapshot s where s.network.id = ?1 and s.status = 9)")
	  NetworkSnapshot findLastGoodKnowByNetworkID(Long networkID);
	  
	  List<NetworkSnapshot> findByNetworkAndStatusOrderByEndTimeAsc(NationalNetwork network, SnapshotStatus status);
	  List<NetworkSnapshot> findByNetworkOrderByEndTimeAsc(NationalNetwork network);

	  
}