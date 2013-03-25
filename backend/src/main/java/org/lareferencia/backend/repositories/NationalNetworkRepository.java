package org.lareferencia.backend.repositories;

import org.lareferencia.backend.domain.NationalNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface NationalNetworkRepository extends JpaRepository<NationalNetwork, Long> { 
}