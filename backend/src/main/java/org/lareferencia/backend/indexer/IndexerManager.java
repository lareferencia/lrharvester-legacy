package org.lareferencia.backend.indexer;

import java.util.Date;

import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class IndexerManager {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	TaskScheduler scheduler;

	@Autowired
	@Qualifier("indexerVufind")
	IIndexer indexerVufind;

	@Autowired
	@Qualifier("indexerXOAI")
	IIndexer indexerXOAI;

	public void indexNetworkInVufind(Long networkID) throws Exception {
		launchIndexerWorker(networkID, indexerVufind, false);
	}

	public void indexNetworkInXOAI(Long networkID) throws Exception {
		launchIndexerWorker(networkID, indexerXOAI, false);
	}

	public void deleteNetworkFromVufind(Long networkID) throws Exception {
		launchIndexerWorker(networkID, indexerVufind, true);
	}

	public void deleteNetworkFromXOAI(Long networkID) throws Exception {
		launchIndexerWorker(networkID, indexerXOAI, true);
	}

	public String indexSnapshotInVufind(NetworkSnapshot snapshot) {

		snapshot.setStatus(SnapshotStatus.INDEXING);

		// Indexa
		boolean isSuccesfullyIndexed = indexerVufind.index(
				snapshot.getNetwork(), snapshot, false);

		// Si el indexado es exitoso marca el snap válido
		if (isSuccesfullyIndexed) {
			// Graba el status
			snapshot.setStatus(SnapshotStatus.VALID);
			return "Indexación Vufind terminada con éxito.";

		} else {
			// Graba el status
			snapshot.setStatus(SnapshotStatus.INDEXING_FINISHED_ERROR);
			return "Error en proceso de indexación Vufind.";
		}
	}

	public String indexSnapshotInXOAI(NetworkSnapshot snapshot) {

		snapshot.setStatus(SnapshotStatus.INDEXING);

		// Indexa
		boolean isSuccesfullyIndexed = indexerXOAI.index(snapshot.getNetwork(),
				snapshot, false);

		// Si el indexado es exitoso marca el snap válido
		if (isSuccesfullyIndexed) {
			// Graba el status
			snapshot.setStatus(SnapshotStatus.VALID);
			return "Indexación XOAI terminada con éxito.";

		} else {
			// Graba el status
			snapshot.setStatus(SnapshotStatus.INDEXING_FINISHED_ERROR);
			return "Error en proceso de indexación XOAI.";
		}
	}

	// Precondición: Debe existir una LGKSnapshot
	private void launchIndexerWorker(Long networkID, IIndexer specificIndexer,
			boolean deleteOnly) throws Exception {

		IndexerWorker worker = applicationContext.getBean("indexerWorker",
				IndexerWorker.class);
		worker.setNetworkID(networkID);
		worker.setIndexer(specificIndexer);
		worker.setDeleteNetworkWithoutReindexing(deleteOnly);

		// Esto encola el worker para que trabaje inmeditamente si es posible o
		// cuando el scheduler decida
		scheduler.schedule(worker, new Date());
	}

}
