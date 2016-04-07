angular.module('rest.url.helper', []).service('RestURLHelper',  [ 
     
    function() {
	
	  this.networkURLByID = function (networkID) {
		  return '/rest/network/' + networkID;	
	  };
	  
	  this.propertyURLByID = function (propertyID) {
		  return '/rest/property/' + propertyID;	
	  };
	  	  
	  this.network_propertyURL = function () {
		  return '/rest/network_property';	
	  };
	  
	  this.propertyURL = function () {
		  return '/rest/property';	
	  }; 
	  
	  this.networkURL = function () {
		  return '/rest/network';	
	  }; 
	  
	  this.originURL = function () {
		  return '/rest/origin';	
	  }; 
	  
	  this.urlFromEntity = function (entity) {
		  return entity._links.self.href;
	  }; 
}]);