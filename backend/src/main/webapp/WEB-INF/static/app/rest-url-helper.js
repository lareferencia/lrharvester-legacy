angular.module('rest.url.helper', []).service('RestURLHelper',  [ 
     
    function() {
	
	  this.networkURLByID = function (networkID) {
		  return '/rest/network/' + networkID;	
	  };
    
	
}]);