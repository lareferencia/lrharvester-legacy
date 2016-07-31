angular.module('rest.url.helper', []).service('RestURLHelper',  [ 
     
    function() {
	
	  this.networkURLByID = function (networkID) {
		  return '/rest/network/' + networkID;	
	  };
	  
	  this.networkSnapshotsURLByID = function (networkID) {
		  return '/rest/network/' + networkID + '/snapshots';	
	  };
	  
	  this.snapshotLogURLByID = function (snapshotID) {
		  return '/rest/log/search/findBySnapshotId?snapshot_id=' + snapshotID;
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
	  
	  
	  this.OIDfromEntity = function (entity) {
		  var splitted_url = entity._links.self.href.split("/");
  		  return splitted_url[ splitted_url.length - 1 ];
	  }; 
	  
	  this.networkActionURL = function (action, networkIDs) { 
		  var idsString = networkIDs;
		  return "/private/networkAction/" + action + "/" + idsString;  
	  };
	  
	  this.diagnoseURLByID = function (snapshotID, fqList) {
		  
		  var fqEncodedList = [];
		  
		  for(var i=0;i<fqList.length;i++) {
			  fqEncodedList.push( encodeURI(fqList[i]) );
		  }
		  
		  return '/public/diagnose/' + snapshotID + '/[' + fqEncodedList.join(',') + ']';	
	  };
	  
	 
	  
	  this.diagnoseRuleOccrURLByID = function (snapshotID, ruleID, fqList) {  
		  
		  var fqEncodedList = [];
		  
		  for(var i=0;i<fqList.length;i++) {
			  fqEncodedList.push( encodeURI(fqList[i]) );
		  }
		  
		  
		  return ' /public/diagnoseValidationOcurrences/' + snapshotID + '/' + ruleID + '/[' + fqEncodedList.join(',') + ']';	
	  };
	  
	  
	  this.diagnoseRecordListURLByID = function (snapshotID, fqList) {
		  
		  var fqEncodedList = [];
		  
		  for(var i=0;i<fqList.length;i++) {
			  fqEncodedList.push( encodeURI(fqList[i]) );
		  }
		  
		  return '/public/diagnoseListRecordValidationResults/' + snapshotID + '/[' + fqEncodedList.join(',') + ']';	
	  };
	  
	  
	  this.recordMetadataURLByID = function (recordID) {		  
		  return '/public/getRecordMetadataByID/' + recordID;	
	  };
}]);