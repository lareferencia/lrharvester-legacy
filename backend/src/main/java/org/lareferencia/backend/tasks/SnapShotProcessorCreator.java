package org.lareferencia.backend.tasks;

import org.lareferencia.backend.domain.NationalNetwork;
import org.springframework.stereotype.Component;

@Component
public class SnapShotProcessorCreator {
	
	   public static SnapshotProcessor createSnapshotProcessor(NationalNetwork network) {
		   return new SnapshotProcessor(network);
	   }
	
}
