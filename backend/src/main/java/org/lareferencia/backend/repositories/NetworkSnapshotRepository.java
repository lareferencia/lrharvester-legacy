package org.lareferencia.backend.repositories;

import org.lareferencia.backend.domain.NetworkSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkSnapshotRepository extends JpaRepository<NetworkSnapshot, Long> { 
}