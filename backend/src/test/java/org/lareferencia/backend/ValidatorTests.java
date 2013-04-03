package org.lareferencia.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.lareferencia.backend.domain.Country;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.validator.FieldOccurrenceValidationRule;
import org.lareferencia.backend.validator.IValidatorRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ValidatorTests {

	@Test
	public void testSave() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<IValidatorRule> myList = new ArrayList<IValidatorRule>();
		//myList.add( new FieldOccurrenceValidationRule("dc:type", 1) );
		
		System.out.println( mapper.writeValueAsString( myList ) );
		
		
	}

}
