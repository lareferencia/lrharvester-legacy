package org.lareferencia.backend.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.criterion.Order;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.tasks.ISnapshotWorker;
import org.lareferencia.backend.tasks.SnapshotManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	
	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(BackEndController.class);
	
	private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	
	
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
	public ResponseEntity<String> harvesting(@PathVariable Long networkID) {
		//TODO: debiera chequear la existencia de la red
		
		SnapshotManager manager = applicationContext.getBean("snapshotManager", SnapshotManager.class);
		manager.lauchHarvesting(networkID);
		
		return new ResponseEntity<String>("Havesting:" + networkID, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/network/{id}", method=RequestMethod.GET)
	public ResponseEntity<NationalNetwork> getNetwork(@PathVariable Long id) {
			
		NationalNetwork network = nationalNetworkRepository.findOne(id);
		ResponseEntity<NationalNetwork> response = new ResponseEntity<NationalNetwork>(
			network,
			network == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
		);
		return response;
	}
	
	
	@RequestMapping(value="/lastGoodKnowSnapshotByNetworkID/{id}", method=RequestMethod.GET)
	public ResponseEntity<NetworkSnapshot> getLGKSnapshot(@PathVariable Long id) {
			
		NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(id);
		ResponseEntity<NetworkSnapshot> response = new ResponseEntity<NetworkSnapshot>(
			snapshot,
			snapshot == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
		);
		return response;
	}
	
	@RequestMapping(value="/listNetworks", method=RequestMethod.GET)
	public ResponseEntity<List<NetworkInfo>> listNetworks() {
				
		List<NationalNetwork> allNetworks = nationalNetworkRepository.findAll();
		List<NetworkInfo> NInfoList = new ArrayList<NetworkInfo>();

		for (NationalNetwork network:allNetworks) {
			
			NetworkInfo ninfo = new NetworkInfo();
			ninfo.networkID = network.getId();
			ninfo.country = network.getCountry().getIso();
			ninfo.name = network.getName();
			
			NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
			
			if ( snapshot != null) {
				
				ninfo.snapshotID = snapshot.getId();
				ninfo.datestamp = dateFormater.format( snapshot.getEndTime() );
				ninfo.size = snapshot.getSize();
				ninfo.validSize = snapshot.getValidSize();
				
			}
			NInfoList.add( ninfo );		
		}
	
		ResponseEntity<List<NetworkInfo>> response = new ResponseEntity<List<NetworkInfo>>(NInfoList, HttpStatus.OK);
		
		return response;
	}
	
	@Getter
	@Setter
	class NetworkInfo {	
		private Long   networkID;
		private String country;
		private String name;
		
		private Long snapshotID;
		private String datestamp;
		private int size;
		private int validSize;
	}
}