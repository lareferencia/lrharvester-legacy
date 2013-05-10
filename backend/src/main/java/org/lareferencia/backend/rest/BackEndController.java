package org.lareferencia.backend.rest;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.tasks.ISnapshotWorker;
import org.lareferencia.backend.tasks.SnapshotManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class BackEndController {
	
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	@Autowired
	private NationalNetworkRepository nationalNetworkRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(BackEndController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value="/harvester/{networkID}", method=RequestMethod.GET)
	public String harvesting(@PathVariable Long networkID, Model model) {
		//TODO: debiera chequear la existencia de la red
		
		SnapshotManager manager = applicationContext.getBean("snapshotManager", SnapshotManager.class);
		
		manager.lauchHarvesting(networkID);

		model.addAttribute("workersSize", manager.getWorkers().size() );

		
		return "harvest";
	}
	
	@RequestMapping(value="/network/{id}", method=RequestMethod.GET)
	//@ResponseBody 
	public ResponseEntity<NationalNetwork> listNetworks(@PathVariable Long id) {
			
		NationalNetwork network = nationalNetworkRepository.findOne(id);
		ResponseEntity<NationalNetwork> response = new ResponseEntity<NationalNetwork>(
			network,
			network == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
		);
		return response;
	}
	
	
	
	
}