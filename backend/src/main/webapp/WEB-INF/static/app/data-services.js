var BackendDataServiceModule = angular.module('data.services', ['spring-data-rest', 'rest.url.helper']);

BackendDataServiceModule.service('DataSrv',  ["$http", "SpringDataRestAdapter", "RestURLHelper", 
     
    function($http, SpringDataRestAdapter, RestURLHelper) {
	
	 var linksToProcess = ['properties','property','networkProperty','origins','origin','rules']; 
	

	 var add_methods = function(processedResponse) {
		 
		 
		 /* Reload */
		  processedResponse.reload = function(success_callback) {
			  
			  var httpPromise = $http.get( RestURLHelper.urlFromEntity(processedResponse) );  
			  
			  SpringDataRestAdapter.process(httpPromise, linksToProcess, true).then( function (item) {
				   
				  processedResponse = add_methods(item);
				  
			      /* devuelve el item creado al callback */
				  success_callback(processedResponse);
			             
			  });
			
		  };
		 
		 
		 /*  una funcion para asociar objetos al item */
		  processedResponse.associate = function(member_name, uri_list, success_callback, fail_callback) { 
			  
			  	// al asociar un objeto debe recargarse el objeto desde el ww
			  	var intercept_success_callback = function(data) {
			  		processedResponse.reload(success_callback);
			  	};
			  	
				var resource = processedResponse._resources(member_name,{}, {associate: { method: 'PUT', headers : {'Content-Type' : 'text/uri-list'}}} );
				resource.associate(uri_list, intercept_success_callback, fail_callback); 
		  };
		
		  	  
		  /*  una funcion para agregar objetos a una colección miembro */
		  processedResponse.addToCollection = function(collection_name, uri_list, success_callback, fail_callback) { 
			    // al agregar a una colección debe recargarse el objeto desde el ww
			  	var intercept_success_callback = function(data) {
			  		processedResponse.reload(success_callback);
			  	};
			  
				var resource = processedResponse._resources(collection_name,{}, {addToCollection: { method: 'POST', headers : {'Content-Type' : 'text/uri-list'}}} );
				resource.addToCollection(uri_list, intercept_success_callback, fail_callback); 
		  };
		  
		  
		  /* una funcion para hacer un update del item  */
		  processedResponse.update = function(update_callback, fail_callback) {  
			  var resource = processedResponse._resources('self',{}, {update: { method: 'PUT'}} );
			  resource.update(processedResponse, update_callback, fail_callback); 
		  };
		  
		  /* una funcion para hacer un delete item  */
		  processedResponse.remove = function(delete_callback, fail_callback) {  	  
			  var resource = processedResponse._resources('self',{}, {remove: { method: 'DELETE'}} );
			  resource.remove(delete_callback, delete_callback); 
		  };
		  
		  
		  /**
		   * Obtención de los items 
		   */
		  processedResponse.getItems = function() {
			  if ( processedResponse._embeddedItems != null )
				  return processedResponse._embeddedItems;
			  else
				  return [];
		  };
		  
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
		  };
		  
		  
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
	  
	  /**
	   * Llamada a una función remota
	   */
	  this.callRestWS = function(url, success_callback, error_callback) {
		  $http.get(url).then(success_callback, error_callback);
	  };
		  
		  
	
	  
}]);

