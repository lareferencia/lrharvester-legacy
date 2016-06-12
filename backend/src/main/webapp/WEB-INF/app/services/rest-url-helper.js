angular.module('rest.url.helper', []).service('RestURLHelper',  [ 
     
    function() {
	
	  this.networkURLByID = function (networkID) {
		  return '/rest/network/' + networkID;	
	  };
	  
	  this.validatorURLByID = function (id) {
		  return '/rest/validator/' + id;	
	  };
	  
	  this.transformerURLByID = function (id) {
		  return '/rest/transformer/' + id;	
	  };
	  
	  
	  this.propertyURLByID = function (propertyID) {
		  return '/rest/property/' + propertyID;	
	  };
	  
	  this.originURLByID = function (originID) {
		  return '/rest/origin/' + originID;	
	  };
	  	  
	  this.network_propertyURL = function () {
		  return '/rest/network_property';	
	  };
	  
	  this.propertyURL = function () {
		  return '/rest/property';	
	  }; 
	  
	  this.setURL = function () {
		  return '/rest/set';	
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
	  
	 /* this.idFromEntity = function (entity) {
		  return entity._links.self.href;
	  }; */
	  
	  this.networkActionURL = function (action, networkIDs) {
		  
		  var idsString = networkIDs;
		  
		  
		  return "/private/networkAction/" + action + "/" + idsString;  
	  };
	  
	  this.diagnoseURLByID = function (snapshotID) {
		  return '/public/diagnose/' + snapshotID;	
	  };
}]);