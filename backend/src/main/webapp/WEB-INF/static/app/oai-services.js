var OAIServiceModule = angular.module('oai.services', []);

OAIServiceModule.service('OAISrv',  ["$http", 
     
    function($http) {
	

		  
	  /**
	   * Get
	   * @param url
	   * @param success_callback
	   * @param error_callback
	   */
	  this.listSets = function(url, success_callback, error_callback ) {
		  
		  var httpPromise = $http.get(url+"?verb=ListSets").success( function(data) {
			  
			  var mydata = data;
			  
			  
			  
		  });  
		  
		  
		  
		
	  }; 
	  

		  
		  
	
	  
}]);

