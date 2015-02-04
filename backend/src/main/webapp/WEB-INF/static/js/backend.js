	
	var origin_base_link = './rest/origin/'; 
	var set_base_link = './rest/set/'; 	
	var network_base_link = './rest/network/'; 

	var actual_set_link = null;
	var actual_origin_link = null;
	var actual_network_link = null;
	

	function refreshNetworks() {
		loadNetworkList('./rest/network?size=1000&sort=id');
	}
	
	function refreshOrigins() {
		loadOriginsList(actual_network_link);
	}
		
	function refreshSets() {
		loadSetsList(actual_origin_link);
	}
	
	/*** Error handler general */
	
	// Sucede que las llamadas a acciones las maneja con este handler entonces se evalua si es 200 o no
	function error_handler(response) {
		if ( response.status > 300)
			alert("Hubo un error de comunicaciones, si persiste, recargue la aplicación.");
		else 
			alert("Acción ejecutada: " + response.responseText);
	}
	
	
	/*** LLamadas a acciones privadas sobre objetos ***/
	
	function sendStopHarvestingBySnapshotID(snapshotID) {	
		stop_service_url = './private/stopHarvestingBySnapshotID/' + snapshotID;	
		$.rest.retrieve(stop_service_url, function(result) { alert('Cosecha detenida');}, error_handler); 

	}
	
	function sendStartHarvestingByNetworkID(networkID) {	
		service_url = './private/startHarvestingByNetworkID/' + networkID;
		$.rest.retrieve(service_url, function(result) { alert('Cosecha lanzada');}, error_handler); 
	}
	
	function 	sendStartIndexingLGKByNetworkID(networkID) {	
		service_url = './private/indexLGKSnapshotByNetworkID/' + networkID;
		$.rest.retrieve(service_url, function(result) { alert('Indexación lanzada');}, error_handler); 
	}
	
	function sendDeleteAllButLGKSnapshotByNetworkID(networkID) {
		service_url = './private/deleteAllButLGKSnapshot/' + networkID;
		$.rest.retrieve(service_url, function(result) { alert('Se han borrado todos los snapshots antiguos');}, error_handler); 
	}
	
	function sendDeleteNetworkByID(networkID, success_handler) {
		service_url = './private/deleteNetworkByID/' + networkID;
		$.rest.retrieve(service_url, function(result) { alert('Se han borrado todos los datos de la red'); refreshNetworks(); } ); 
	}
	
	
	/**** Acciones sobre la lista de orígenes *******/
	function editOrigins(network_link) {
		 actual_network_link = network_link;
		 loadOriginsList(network_link);
		 $( "#dialog_edit_origins" ).dialog("open");
	}
	
	
    /**** Acciones sobre la lista de snpashots *******/
	function showSnapshots(network_link, lgkOnly) {
		 actual_network_link = network_link;
		 loadSnapshotList(network_link, lgkOnly);
		 $( "#dialog_network_snapshots" ).dialog("open");
	}
	
	
	
	function openSnapshotLog(snapshot_link) {
			
		log_service_url = './rest/log/search/findBySnapshotId?snapshot_id=' +  $.rest.link2id(snapshot_link);
		dst_element_id = '#snapshot_log';
		
		d3.select(dst_element_id)
		  .selectAll('tr').remove();
		
		$.rest.retrieve(log_service_url, function(result) {
			
			if ( result._embedded == null )
				result._embedded = { "log" : []};
			
					
			var p = d3.select(dst_element_id)
				      .selectAll('tr')
				      .data(result._embedded.log)
	                  .enter().append('tr');
				
			   p.append("td")
			     .text(function(d) { 
			            	var date = new Date();
			            	date.setTime(d.timestamp);
			            	return date.toLocaleDateString() + ' ' +date.toLocaleTimeString().substring(0, 5); });
				
		       p.append("td")
		        .text(function(d) { return d.message; });
		       
		    $( "#dialog_snapshot_log" ).dialog("open");
		    
		}, error_handler);
		
	}
	
	/**** Acciones sobre un Origin *****/
	
	function openEditOrigin(origin_link) {
		 actual_origin_link = origin_link;
		 
		 $.rest.retrieve(origin_link, function(result) {
			 	$('#form_edit_origin').fromJson(result);
			 	$( "#dialog_edit_origin" ).dialog("open");
		 });
	}

	function openCreateOrigin() {
		 $('#form_create_origin').fromJson( {name:'Main', metadataPrefix:'oai_dc'} );
		 $( "#dialog_create_origin" ).dialog("open");
	}
	
	function createOrigin(network_link, success_handler) { 		 
		var data = $('#form_create_origin').toJson();
		$.rest.append( network_link + '/origins', origin_base_link, data, success_handler);
	}
	
	/**
	 * Actualiza el origen identificada por networklink, usando los datos de '#form_edit_network'
	 * @param network_link
	 * @param success_handler
	 */
 	function updateOrigin(origin_link, success_handler) {
		  var data = $('#form_edit_origin').toJson();
		  $.rest.update(origin_link, data, success_handler, error_handler);  
	}
 	
 	
 	/**
	 * Borra el origen identificado
	 * @param network_link
	 * @param success_handler
	 */
 	function deleteOrigin(origin_link, success_handler) {
		  $.rest.destroy(origin_link, success_handler, error_handler);  
	}
 	
 	
 	/*********** Acciones sobre un set *****************/
 	
 	function showSets(origin_link) {
		actual_origin_link = origin_link;
		loadSetsList(origin_link); 
		 $( "#dialog_edit_sets" ).dialog("open");
	}
 	
 	function openCreateSet() {
		 $('#form_create_set').fromJson( {} );
		 $( "#dialog_create_set" ).dialog("open");
	}
 	
 	function openEditSet(set_link) {
		 actual_set_link = set_link;
		 $.rest.retrieve(set_link, function(result) {
			 $('#form_edit_set').fromJson(result);
			 $( "#dialog_edit_set" ).dialog("open");
		 });
	}
 	
 	function createSet(origin_link, success_handler) { 		  
		  var data = $('#form_create_set').toJson();
		  $.rest.append( origin_link + '/sets', set_base_link, data, success_handler);		  
	}
 	
 	function updateSet(set_link, success_handler) {
		  var data = $('#form_edit_set').toJson();
		  $.rest.update(set_link, data, success_handler, success_handler);  
	}
 	
 	
 	function deleteSet(set_link, success_handler) {
		  $.rest.destroy(set_link, success_handler, error_handler);  
	}
 	/*********** Acciones sobre un red nacional *****************/
 	
 	function openCreateNetwork() {
		 $('#form_create_network').fromJson( {validatorName:'defaultValidator', transformerName:'defaultTransformer'} );
		 $( "#dialog_create_network" ).dialog("open");
	}
 	
 	function createNetwork(success_handler) { 		  
 		
		  var new_network_data = $('#form_create_network').toJson();
		  $.rest.create(network_base_link, new_network_data, success_handler, success_handler);
		  
	}
 	
 	/*** 
 	 * Editar un red
 	 * @param network_link
 	 */
 	function editNetwork(network_link) {
		 actual_network_link = network_link;
				 
		 $.rest.retrieve(network_link, function(result) {
			 $('#form_edit_network').fromJson(result); 
			 $('#dialog_edit_network').dialog('open');
		 }, error_handler );	 
		 
	}

	/**
	 * Actualiza la red identificada por networklink, usando los datos de #form_edit_network
	 * @param network_link
	 * @param success_handler
	 */
 	function updateNetwork(network_link, success_handler) {
 		
		 var data = $('#form_edit_network').toJson();  
		 $.rest.update(network_link, data, success_handler, success_handler);  
	}


    /**
     * Borra una red apuntada por network_link y llama a succces_handler
     * @param network_link
     * @param success_handler
     */
	function deleteNetwork(network_id, success_handler) {
		
	    $( "#dialog_delete_network" ).dialog({
	      buttons: {
	        "Borrar la red": function() {
	          
	        	sendDeleteNetworkByID(network_id, success_handler);
	        	
	          $( this ).dialog( "close" );
	        },
	        Cancel: function() {
	          $( this ).dialog( "close" );
	        }
	      }
	    });
		
	    $( "#dialog_delete_network" ).dialog("open");
	}	

	

   
	/**
	 * Carga los datos principales de las redes nacionales en dst_element_id
	 * @param networks_link
	 * @param dst_element_id
	 */
    function loadNetworkList(networks_link) {
    	
    	dst_element_id = "#networks";
    	
    	d3.select(dst_element_id)
		  .selectAll("tr").remove();
		
		$.rest.retrieve(networks_link, 
				
				function(result) {
			
					if ( result._embedded == null )
						result._embedded = { "network" : []};
					
					var p = d3.select(dst_element_id)
					  .selectAll("tr")
		    		  .data(result._embedded.network)
		              .enter().append("tr");
					
					p.append("td")
				      .text( function(d) { return '#' + $.rest.link2id( $.rest.relLink(d,'self') ); });
					
		    		  
		            p.append("td")
		              .text(function(d) { return d.institutionName + " - " +  d.name; });
		            
		            p.append("td")
		              .text(function(d) { return d.acronym; });
		            
		            p.append("td")
		              .text(function(d) { if (d.published) return 'Si'; else return 'No'; });
					
		        	p.append("td")
					  .append("button")
					  .on("click", function(d) { showSnapshots($.rest.relLink(d,"self"), true); })	   
					  .text("LGK snapshot"); 
		            
					p.append("td")
					  .append("button")
					  .on("click", function(d) { showSnapshots($.rest.relLink(d,"self")); })	   
					  .text("snapshots"); 
					
					p.append("td")
					  .append("button")
					  .on("click", function(d) { editOrigins($.rest.relLink(d,"self"));  } )	   
					  .text("orígenes"); 
					
					p.append("td")
					  .append("button")
					  .on("click", function(d) { sendStartHarvestingByNetworkID( $.rest.link2id($.rest.relLink(d,"self")) );  } )	   
					  .text("cosechar"); 
					
					p.append("td")
					  .append("button")
					  .on("click", function(d) { sendStartIndexingLGKByNetworkID( $.rest.link2id($.rest.relLink(d,"self")) );  } )	   
					  .text("indexar LGK"); 
					
					p.append("td")
					  .append("button")
					  .on("click", function(d) { window.open("diagnose/" + d.acronym  );  } )	   
					  .text("diagnóstico LGK"); 
					
					p.append("td")
					  .append("button")
					  .on("click", function(d) { sendDeleteAllButLGKSnapshotByNetworkID( $.rest.link2id($.rest.relLink(d,"self")) );  } )	   
					  .text("limpiar"); 
					
					p.append("td")
					  .append("button")
					  .on("click", function(d) { editNetwork($.rest.relLink(d,"self"));  } )	   
					  .text("editar"); 
					
					p.append("td")
					  .append("button")
					  .on("click", function(d) { deleteNetwork($.rest.link2id( $.rest.relLink(d,'self') ), refreshNetworks );  } )	   
					  .text("borrar"); 
				},
				error_handler
		);
	}
	
    
    /********** Acciones sobre snapshots ******/

    
    /**
     * Carga los datos del snapshot correspondiente a @para network en #snapshots
     * @param network
     */
	function loadSnapshotList(network_link, lgkOnly) {
		
		
		// lista todo, no solo el lgk
		if ( lgkOnly == null || !lgkOnly ) {
			// Esto los da ordenados de mayor fecha a menor
			service_url = './rest/snapshot/search/findByNetworkIdOrderByStartTimeDesc?size=50&network_id=' +  $.rest.link2id(network_link);
		}
		else {
			// lista solo el lgk
			service_url = './rest/snapshot/search/findLastGoodKnowByNetworkID?network_id=' +  $.rest.link2id(network_link);
		}
				
		dst_element_id = '#snapshots';
		
		d3.select(dst_element_id)
		  .selectAll('tr').remove();
	
		
		$.rest.retrieve(service_url, function(result) {
					
			
			if ( result._embedded == null )
				result._embedded = { "snapshot" : []};
					
			var p = d3.select(dst_element_id)
			  .selectAll('tr')
    		  .data(result._embedded.snapshot)
              .enter().append('tr');
			
			p.append("td")
		      .text( function(d) { return '#' + $.rest.link2id( $.rest.relLink(d,'self') );  });
			
		    p.append("td")
            .text(function(d) { return d.status; });
		    
		    p.append("td")
            .text(function(d) { return d.deleted; });
    		  
		    p.append("td")
            .text(function(d) { return d.size; });
		    
		    p.append("td")
            .text(function(d) { return d.validSize; });
		    
		    p.append("td")
            .text(function(d) { return d.transformedSize; });
		    
		    p.append("td")
            .text(function(d) { 
            	var date = new Date(d.startTime);
            	//date.setTime(d.startTime);
            	return date.toLocaleDateString() + ' ' +date.toLocaleTimeString().substring(0, 5); 	
            });
		    
		    p.append("td")
		    .text(function(d) { 
            	var date = new Date(d.endTime);
            	//date.setTime(d.endTime);
            	return date.toLocaleDateString() + ' ' +date.toLocaleTimeString().substring(0, 5); 	
            });
		    
		    p.append("td")
		      .append("button")
			  .on('click', function(d) { 
				  if ( d.deleted == false ) 
					  openSnapshotLog($.rest.relLink(d,'self'));}) 
			  .text('log'); 

		    
		    p.append("td")
		      .append("button")
			  .on('click', function(d) { 
				  //if ( d.state == 'HARVESTING' ) 
					  sendStopHarvestingBySnapshotID( $.rest.link2id( $.rest.relLink(d,'self')) );}) 
			  .text('detener'); 

			
		    
		    
			  /*
			  .attr('disabled', function(d) { 
				  				if (d.deleted=='true') 
				  					return 'disabled';
				  				else
				  					return '';
				  				} )
		    
		  */
		    
		    
		}, error_handler);
	}
	
	
	
	/**
	 * Carga los sets oai del origen identificada por origin_link en dst_element_link
	 * @param network_link
	 * @param dst_element_id
	 */
	function loadSetsList(origin_link) {
		
    	dst_element_id = "#origin_sets";
		
		d3.select(dst_element_id)
		  .selectAll("tr").remove();
		
		$.rest.retrieve(origin_link + '/sets', 
				 
				 function(result) {
				
					if ( '_embedded' in result ) {
						
						var p = d3.select(dst_element_id)
						  .selectAll("tr")
			    		  .data(result._embedded.set)
			              .enter().append("tr");
			    		  
			            p.append("td")
			              .text(function(d) { return d.name; });
			               
			            p.append("td")
			              .text(function(d) { return d.spec; });
			           
			            p.append("td")
						  .append("button")
						  .on("click", function(d) { openEditSet($.rest.relLink(d,"self"));  } )	   
						  .text("editar"); 
			            
			            p.append("td")
						  .append("button")
						  .on("click", function(d) { deleteSet($.rest.relLink(d,"self"), refreshSets);  } )	   
						  .text("borrar"); 
					}
				
		 		}, error_handler
		 );
	}
	
	/**
	 * Carga los orígenes oai de la red identificada por network_link en dst_element_link
	 * @param network_link
	 * @param dst_element_id
	 */
	function loadOriginsList(network_link) {
		
    	dst_element_id = "#network_origins";
		
		d3.select(dst_element_id)
		  .selectAll("tr").remove();
		
		$.rest.retrieve(network_link + '/origins', 
				 
				 function(result) {
				
					if ( '_embedded' in result ) {
	
						var p = d3.select(dst_element_id)
						  .selectAll("tr")
			    		  .data(result._embedded.origin)
			              .enter().append("tr");
			    		  
			            p.append("td")
			              .text(function(d) { return d.name; });
			               
			            p.append("td")
			              .text(function(d) {
			            	  		if (d.uri.length > 80)
			            	  			return d.uri.substring(0,80) + '...';
			            	  		else
			            	  			return d.uri;
			            	  	});
			            
			            
			            p.append("td")
						  .append("button")
						  .on("click", function(d) { showSets($.rest.relLink(d,"self"));  } )	   
						  .text("sets"); 
			            
			            
			            p.append("td")
						  .append("button")
						  .on("click", function(d) { openEditOrigin($.rest.relLink(d,"self"));  } )	   
						  .text("editar"); 
			            
			            p.append("td")
						  .append("button")
						  .on("click", function(d) { deleteOrigin($.rest.relLink(d,"self"), refreshOrigins);  } )	   
						  .text("borrar"); 
		            
					}
				
		 		}
		 );
		
		
		
		
	}
	

	