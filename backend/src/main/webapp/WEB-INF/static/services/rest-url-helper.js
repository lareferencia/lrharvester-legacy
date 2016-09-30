angular.module('rest.url.helper', ['rest.url.rebase']).service('RestURLHelper',  [ 'ReBaseURLHelper',
     
    function(ReBaseURLHelper) {
	
    	
      this.listNetworks = function () {
  		  return ReBaseURLHelper.rebaseURL('public/networks');	
  	  };	
    	
	  this.networkURLByID = function (networkID) {
		  return ReBaseURLHelper.rebaseURL('rest/network/' + networkID);	
	  };
	  
	  this.networkSnapshotsURLByID = function (networkID) {
		  return ReBaseURLHelper.rebaseURL('rest/network/' + networkID + 'snapshots');	
	  };
	  
	  this.snapshotLogURLByID = function (snapshotID) {
		  return ReBaseURLHelper.rebaseURL('rest/log/search/findBySnapshotId?snapshot_id=' + snapshotID);
	  };
	  
	  this.validatorURLByID = function (id) {
		  return ReBaseURLHelper.rebaseURL('rest/validator/' + id);	
	  };
	  
	  this.transformerURLByID = function (id) {
		  return ReBaseURLHelper.rebaseURL('rest/transformer/' + id);	
	  };
	  
	  
	  this.propertyURLByID = function (propertyID) {
		  return ReBaseURLHelper.rebaseURL('rest/property/' + propertyID);	
	  };
	  
	  this.originURLByID = function (originID) {
		  return ReBaseURLHelper.rebaseURL('rest/origin/' + originID);	
	  };
	  	  
	  this.network_propertyURL = function () {
		  return ReBaseURLHelper.rebaseURL('rest/network_property');	
	  };
	  
	  this.propertyURL = function () {
		  return ReBaseURLHelper.rebaseURL('rest/property');	
	  }; 
	  
	  this.setURL = function () {
		  return ReBaseURLHelper.rebaseURL('rest/set');	
	  }; 
	  
	  this.networkURL = function () {
		  return ReBaseURLHelper.rebaseURL('rest/network');	
	  }; 
	  
	  this.validatorURL = function () {
		  return ReBaseURLHelper.rebaseURL('rest/validator');	
	  }; 
	  
	  this.transformerURL = function () {
		  return ReBaseURLHelper.rebaseURL('rest/transformer');	
	  }; 
	  
	  this.originURL = function () {
		  return ReBaseURLHelper.rebaseURL('rest/origin');	
	  }; 
	  
	  this.validatorRuleURL = function () {
		  return ReBaseURLHelper.rebaseURL('rest/validatorRule');	
	  };
	  
	  this.transformerRuleURL = function () {
		  return ReBaseURLHelper.rebaseURL('rest/transformerRule');	
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
		  return ReBaseURLHelper.rebaseURL('private/networkAction/' + action + '/' + idsString);  
	  };
	  
	  this.diagnoseURLByID = function (snapshotID, fqList) {
		  
		  var fqEncodedList = [];
		  
		  for(var i=0;i<fqList.length;i++) {
			  fqEncodedList.push( encodeURI(fqList[i]) );
		  }
		  
		  return ReBaseURLHelper.rebaseURL('public/diagnose/' + snapshotID + '/[' + fqEncodedList.join(',') + ']');	
	  };
	  
	 
	  
	  this.diagnoseRuleOccrURLByID = function (snapshotID, ruleID, fqList) {  
		  
		  var fqEncodedList = [];
		  
		  for(var i=0;i<fqList.length;i++) {
			  fqEncodedList.push( encodeURI(fqList[i]) );
		  }
		  
		  
		  return ReBaseURLHelper.rebaseURL('public/diagnoseValidationOcurrences/' + snapshotID + '/' + ruleID + '/[' + fqEncodedList.join(',') + ']');	
	  };
	  
	  
	  this.diagnoseRecordListURLByID = function (snapshotID, fqList) {
		  
		  var fqEncodedList = [];
		  
		  for(var i=0;i<fqList.length;i++) {
			  fqEncodedList.push( encodeURI(fqList[i]) );
		  }
		  
		  return ReBaseURLHelper.rebaseURL('public/diagnoseListRecordValidationResults/' + snapshotID + '/[' + fqEncodedList.join(',') + ']');	
	  };
	  
	  
	  this.recordMetadataURLByID = function (recordID) {		  
		  return ReBaseURLHelper.rebaseURL('public/getRecordMetadataByID/' + recordID);	
	  };
}]);