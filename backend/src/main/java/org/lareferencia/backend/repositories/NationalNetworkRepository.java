package org.lareferencia.backend.repositories;

import java.util.List;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NationalNetworkRepository extends JpaRepository<NationalNetwork, Long> { 
	
	  List<NationalNetwork> findByPublishedOrderByNameAsc(boolean published);
	  
	  @Query("select nn from NationalNetwork nn, Country co where co.iso = ?1 and nn.country_id = co.id")
	  NationalNetwork findByCountryISO(String iso);
	  

}