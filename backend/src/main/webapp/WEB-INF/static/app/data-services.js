var BackendDataServiceModule = angular.module('data.services', ['spring-data-rest']);

BackendDataServiceModule.service('DataSrv',  ["$http", "SpringDataRestAdapter", 
     
    function($http, SpringDataRestAdapter) {
		  
	  this.get = function(url, callback) {
		  
		  var httpPromise = $http.get(url);  
		  
		  SpringDataRestAdapter.process(httpPromise).then( function (processedResponse) {
			    
			  var item = processedResponse;
			  
			  /* obtiene el resource a self */
			  item.resources = processedResponse._resources('self',{}, {update: { method: 'PUT'}} );
			  
			  /* crea una funcion para hacer un update del item */
			  item.update = function(update_callback, fail_callback) { item.resources.update(item, update_callback, fail_callback); };
			  
		      /* devuelve el item creado al callback */
			  callback(item);
		             
		  });
	  }; 
	  
	  this.add = function(url, obj, callback) {
		  
		  var httpPromise = $http.post(url, obj); 
		  
		  SpringDataRestAdapter.process(httpPromise).then( function (processedResponse) {
			    
			  var item = processedResponse;
			  
			  /* obtiene el resource a self */
			  item.resources = processedResponse._resources('self',{}, {update: { method: 'PUT'}} );
			  
			  /* crea una funcion para hacer un update del item */
			  item.update = function(update_callback, fail_callback) { item.resources.update(item, update_callback, fail_callback); };
			  
		      /* devuelve el item creado al callback */
			  callback(item);
		             
		  });
	 
	  };
		  
		  
	
	  
}]);

