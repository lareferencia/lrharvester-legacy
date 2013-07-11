package org.lareferencia.backend.indexer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.lareferencia.backend.harvester.HarvestingEvent;
import org.lareferencia.backend.harvester.IHarvester;
import org.lareferencia.backend.harvester.IHarvestingEventListener;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.transformer.ITransformer;
import org.lareferencia.backend.validator.IValidator;
import org.lareferencia.backend.validator.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(value="prototype")
public class IndexerWorker implements Runnable {
	

	@Setter
	private Long snapshotID;
	
	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;
	
	@Autowired
	IIndexer indexer;
	
	public IndexerWorker() {
		
		
	};
	
	@Override
	public void run() {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(this.snapshotID);
		indexer.index(snapshot); 
		System.gc();
	}
	
	
}
