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
		
	  
	  this.mytest = function(scope) { 	
		
		  var result = null;
		  var httpPromise = $http.get('/rest/network/1');
		
		  SpringDataRestAdapter.process(httpPromise).then(function (processedResponse) {
			  
		      scope.mynetwork = processedResponse;
		      
		      var network = processedResponse;
		      network.name = "un nombre nuevo";
		      
		      var networkResources = processedResponse._resources('self',{}, {update: { method: 'PUT'}} );
		      
		      
		      /*networkResources.update(network, function(network) { 
		    	  
		    	  alert("una red actualizad");
		    	  
		      });*/
		      
		     var originsResource = network._resources('origins',{}, {append: { 
		    	 method: 'POST', 
		    	 headers:{'Content-Type':'text/uri-list; charset=UTF-8'} 
		     }});
		     
		     originsResource.append('http://localhost:8090/rest/origin/1', function(origins) { 
		    	  scope.myorigins = origins;
		    	  alert("un origen actualizado");
		     });
		        
		  });
		  
		  
		  
	  };
	  
}]);

