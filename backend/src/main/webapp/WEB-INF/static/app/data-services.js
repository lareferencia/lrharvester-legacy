var BackendDataServiceModule = angular.module('data.services', ['spring-data-rest']);

BackendDataServiceModule.service('DataSrv',  ["$http", "SpringDataRestAdapter", 
     
    function($http, SpringDataRestAdapter) {
	
	 var linksToProcess = ['properties','property','networkProperty']; 
	

	 var add_methods = function(processedResponse) {
		
		  
		  /*  una funcion para asociar objetos al item */
		 processedResponse.associate = function(member_name, uri_list, success_callback, fail_callback) { 
				var resource = processedResponse._resources(member_name,{}, {associate: { method: 'PUT', headers : {'Content-Type' : 'text/uri-list'}}} );
				resource.associate(uri_list, success_callback, fail_callback); 
		  };
		  
		  /* una funcion para obtener objetos asociados a un miembro */
		  /*processedResponse.get_member = function(member_name, success_callback, fail_callback) { 
				var resource = processedResponse._resources(member_name);
				resource.get(success_callback,fail_callback); 
		  };*/
		  
		  /* una funcion para hacer un update del item  */
		  processedResponse.update = function(update_callback, fail_callback) {  
			  var resource = processedResponse._resources('self',{}, {update: { method: 'PUT'}} );
			  resource.update(processedResponse, update_callback, fail_callback); 
		  };
		  
		  
		  /**
		   * Obtención de los items 
		   */
		  processedResponse.getItems = function() {
			  if ( processedResponse._embeddedItems != null )
				  return processedResponse._embeddedItems;
			  else
				  return [];
		  }
		  
		  /**
		   * Obtención de los items de un link, esta función depende del procesamiento realizado recursivamente sobre processedResponse con add_methods
		   */
		  processedResponse.getLinkItems = function(association_link_name) {
			  
			  if (processedResponse['_' + association_link_name ] != null ) {
				  if ( processedResponse['_' + association_link_name ]._embeddedItems != null )
					  return processedResponse['_' + association_link_name ]._embeddedItems;
				  else
					  return processedResponse['_' + association_link_name ];
			  }
			  else
				  return null;
		  }
		  
		  
		  // para cada link para procesar
		  angular.forEach(linksToProcess, function(link, i) {
			  
			  if ( processedResponse[link] != null ) {
				  
				  if ( processedResponse[link]._embeddedItems != null  ) {
					  
					  angular.forEach(processedResponse[link]._embeddedItems, function(item, j) {
						  processedResponse[link]._embeddedItems[j] = add_methods(item);  
					  });  
					  
				  } else if ( processedResponse[link]._resources != null ) {
					  processedResponse[link] =  add_methods(processedResponse[link]);
				  }
				  
				  processedResponse['_' + link ] = processedResponse[link];
				  delete processedResponse[link];  
			  }
	
		  });
		  
	
		  return processedResponse;
		 
	 };	
	  	
		  
	  /**
	   * Get
	   * @param url
	   * @param success_callback
	   * @param error_callback
	   */
	  this.get = function(url, success_callback, error_callback ) {
		  
		  var httpPromise = $http.get(url);  
		  
		  SpringDataRestAdapter.process(httpPromise, linksToProcess, true).then( function (processedResponse) {
			   
			  var item = add_methods(processedResponse);
			  
		      /* devuelve el item creado al callback */
			  success_callback(item);
		             
		  }).catch(error_callback); // esto da error en el parse de eclipse pero no es error
	  }; 
	  
	  
	  /**
	   * Add
	   * @param url
	   * @param obj
	   * @param success_callback
	   * @param error_callback
	   */
	  this.add = function(url, obj, success_callback, error_callback) {
		  
		  var httpPromise = $http.post(url, obj); 
		  
		  SpringDataRestAdapter.process(httpPromise, linksToProcess, true).then( function (processedResponse) {
			    
			  var item = add_methods(processedResponse);
			
		      /* devuelve el item creado al callback */
			  success_callback(item);
		             
		  }).catch(error_callback); // esto da error en el parse de eclipse pero no es error
	 
	  };
		  
		  
	
	  
}]);

