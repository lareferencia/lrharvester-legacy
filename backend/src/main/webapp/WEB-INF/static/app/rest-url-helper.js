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
	  
	  this.validatorURL = function () {
		  return '/rest/validator';	
	  }; 
	  
	  this.transformerURL = function () {
		  return '/rest/transformer';	
	  }; 
	  
	  this.originURL = function () {
		  return '/rest/origin';	
	  }; 
	  
	  this.validatorRuleURL = function () {
		  return '/rest/validatorRule';	
	  };
	  
	  this.transformerRuleURL = function () {
		  return '/rest/transformerRule';	
	  };
	  
	  
	  this.urlFromEntity = function (entity) {
		  return entity._links.self.href;
	  }; 
	  
	  this.networkActionURL = function (action, networkIDs) {
		  
		  var idsString = networkIDs;
		  
		  
		  return "/private/networkAction/" + action + "/" + idsString;  
	  };
}]);