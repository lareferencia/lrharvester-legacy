	
	var origin_base_link = './rest/origin/'; 
	var network_base_link = './rest/network/'; 

	var actual_origin_link = null;
	var actual_network_link = null;

	function refreshNetworks() {
		loadNetworkList('./rest/network','#networks');
	}
	
	function refreshOrigins() {
		loadOriginsList(actual_network_link, '#network_origins');
	}
		
	/**** Acciones sobre la lista de orígenes *******/
	
	function editOrigins(network_link) {
		 actual_network_link = network_link;
		 loadOriginsList(network_link, '#network_origins');
		 $( "#dialog_edit_origins" ).dialog("open");
	}
	
	
    /**** Acciones sobre la lista de snpashots *******/
	
	function showSnapshots(network_link) {
		 actual_network_link = network_link;
		 loadSnapshotList(network_link, '#snapshots');
		 $( "#dialog_network_snapshots" ).dialog("open");
	}
	
	
	/**** Acciones sobre un Origin *****/
	
	function openEditOrigin(origin_link) {
		 actual_origin_link = origin_link;
		 $('#form_edit_origin').fromJson( $.rest.retrieve(origin_link) );
		 $( "#dialog_edit_origin" ).dialog("open");
	}

	function openCreateOrigin() {
		 $('#form_create_origin').fromJson( {name:'Main', metadataPrefix:'oai_dc'} );
		 $( "#dialog_create_origin" ).dialog("open");
	}
	
	function createOrigin(network_link, success_handler) { 		  
		  var data = {};
		  
		  data['origins'] = $.rest.retrieve_compact(network_link + '/origins').links;
		  data['snapshots'] = $.rest.retrieve_compact(network_link + '/snapshots').links;
		  
		  var new_origin_data = $('#form_create_origin').toJson();
		  data['origins'].push(new_origin_data);
		  
		  $.rest.update(network_link, data, success_handler);  		  
	}
	
	/**
	 * Actualiza el origen identificada por networklink, usando los datos de '#form_edit_network'
	 * @param network_link
	 * @param success_handler
	 */
 	function updateOrigin(origin_link, success_handler) {
		  var data = $('#form_edit_origin').toJson();
		  $.rest.update(origin_link, data, success_handler);  
	}
 	
 	
 	/**
	 * Borra el origen identificado
	 * @param network_link
	 * @param success_handler
	 */
 	function deleteOrigin(origin_link, success_handler) {
		  $.rest.destroy(origin_link, success_handler);  
	}
 	
 	
 	/*********** Acciones sobre un red nacional *****************/
 	
 	function openCreateNetwork() {
		 $('#form_create_network').fromJson( {} );
		 $( "#dialog_create_network" ).dialog("open");
	}
 	
 	function createNetwork(success_handler) { 		  
 		
		  var new_network_data = $('#form_create_network').toJson();
		  $.rest.create(network_base_link, new_network_data);
		  refreshNetworks();
		  
	}
 	
 	/*** 
 	 * Editar un red
 	 * @param network_link
 	 */
 	function editNetwork(network_link) {
		 actual_network_link = network_link;
		 $('#form_edit_network').fromJson( $.rest.retrieve(network_link) );
		 $('#dialog_edit_network').dialog('open');
	}

	/**
	 * Actualiza la red identificada por networklink, usando los datos de #form_edit_network
	 * @param network_link
	 * @param success_handler
	 */
 	function updateNetwork(network_link, success_handler) {
		  
		  var data = $('#form_edit_network').toJson();
		  
		  data['origins'] = $.rest.retrieve_compact(network_link + '/origins').links;
		  data['snapshots'] = $.rest.retrieve_compact(network_link + '/snapshots').links;
		  
		  $.rest.update(network_link, data, success_handler);  
	}


    /**
     * Borra una red apuntada por network_link y llama a succces_handler
     * @param network_link
     * @param success_handler
     */
	function deleteNetwork(network_link, success_handler) {
		
	    $( "#dialog_delete_network" ).dialog({
	      buttons: {
	        "Borrar la red": function() {
	    	  $.rest.destroy(network_link, success_handler ); 
	          $( this ).dialog( "close" );
	        },
	        Cancel: function() {
	          $( this ).dialog( "close" );
	        }
	      }
	    });
		
	    $( "#dialog_delete_network" ).dialog("open");
	}	

	
    /********** Acciones sobre snapshots ******/

   
	/**
	 * Carga los datos principales de las redes nacionales en dst_element_id
	 * @param networks_link
	 * @param dst_element_id
	 */
    function loadNetworkList(networks_link, dst_element_id) {
		$.rest.retrieve(networks_link, 
				
				function(result) {
			
					d3.select(dst_element_id)
					  .selectAll("p").remove();
					
					var p = d3.select(dst_element_id)
					  .selectAll("p")
		    		  .data(result.content)
		              .enter().append("p");
		    		  
		            p.append("span")
		              .attr("class", "network_name")
		              .text(function(d) { return d.name; });
					 
					p.append("span")
					  .attr("class", "column")
				      .text( function(d) { return '_' + $.rest.link2id( $.rest.relLink(d,'self') ) + '_'; });
					
					p.append("span")
					  .attr("class", "column")
					  .append("button")
					  .on("click", function(d) { showSnapshots($.rest.relLink(d,"self")); })	   
					  .text("snapshots"); 
					
					p.append("span")
					  .attr("class", "column")
					  .append("button")
					  .on("click", function(d) { editOrigins($.rest.relLink(d,"self"));  } )	   
					  .text("orígenes"); 
					
					p.append("span")
					  .attr("class", "column")
					  .append("button")
					  .on("click", function(d) { editNetwork($.rest.relLink(d,"self"));  } )	   
					  .text("editar"); 
					
					p.append("span")
					  .attr("class", "column")
					  .append("button")
					  .on("click", function(d) { deleteNetwork($.rest.relLink(d,"self"), refreshNetworks );  } )	   
					  .text("borrar"); 
				}
		);
	}
	
    
    
    
    /**
     * Carga los datos del snapshot correspondiente a @para network en #snapshots
     * @param network
     */
	function loadSnapshotList(network_link, dst_element_id) {
		
		
		$.rest.retrieve(network_link + '/snapshots', function(result) {
			
			d3.select(dst_element_id)
			  .selectAll('p').remove();
			
			var p = d3.select(dst_element_id)
			  .selectAll('p')
    		  .data(result.content)
              .enter().append('p');
			
			p.append("span")
			  .attr("class", "column")
		      .text( function(d) { return '#' + $.rest.link2id( $.rest.relLink(d,'self') );  });
			
		    p.append("span")
            .attr("class", "column_wide")
            .text(function(d) { return d.status; });
		    
		    p.append("span")
            .attr("class", "column")
            .text(function(d) { return d.deleted; });
    		  
		    p.append("span")
            .attr("class", "column_number")
            .text(function(d) { return d.size; });
		    
		    p.append("span")
            .attr("class", "column_number")
            .text(function(d) { return d.validSize; });
		    
		    p.append("span")
            .attr("class", "column_number")
            .text(function(d) { return d.transformedSize; });
		    
		    p.append("span")
            .attr("class", "column_date")
            .text(function(d) { return d.startTime; });
		    
		    p.append("span")
            .attr("class", "column_date")
            .text(function(d) { return d.endTime; });
	
		});
	}
	
	
	/**
	 * Carga los orígenes oai de la red identificada por network_link en dst_element_link
	 * @param network_link
	 * @param dst_element_id
	 */
	function loadOriginsList(network_link, dst_element_id) {
		
		$.rest.retrieve(network_link + '/origins', 
				 
				 function(result) {
				
					d3.select(dst_element_id)
					  .selectAll("p").remove();
					
					var p = d3.select(dst_element_id)
					  .selectAll("p")
		    		  .data(result.content)
		              .enter().append("p");
		    		  
		            p.append("span")
		              .attr("class", "column")
		              .text(function(d) { return d.name; });
		               
		            p.append("span")
		              .attr("class", "column_uri")
		              .text(function(d) {
		            	  		if (d.uri.length > 43)
		            	  			return d.uri.substring(0,43) + '...';
		            	  		else
		            	  			return d.uri;
		            	  	});
		            
		            p.append("span")
					  .attr("class", "column_button")
					  .append("button")
					  .on("click", function(d) { openEditOrigin($.rest.relLink(d,"self"));  } )	   
					  .text("editar"); 
		            
		            p.append("span")
					  .attr("class", "column_button")
					  .append("button")
					  .on("click", function(d) { deleteOrigin($.rest.relLink(d,"self"), refreshOrigins);  } )	   
					  .text("borrar"); 
				
		 		}
		 );
		
	}
	

	