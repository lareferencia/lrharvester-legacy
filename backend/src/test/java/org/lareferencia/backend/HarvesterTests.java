package org.lareferencia.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.IHarvester;
import org.lareferencia.backend.indexer.IIndexer;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.transformer.ITransformer;
import org.lareferencia.backend.validator.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class HarvesterTests {
	

	
	@Autowired
	IHarvester harvester;


	@Test
	public void testListSets() throws Exception {
		
		harvester.listSets("http://www.scielo.br/oai/scielo-oai.php");
		
	}
	
	
	
	
}
