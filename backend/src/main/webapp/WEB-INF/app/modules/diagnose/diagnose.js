/**
 * @module App
 * @summary First module loaded
 */

/*globals window, angular, document */

var mod_diagnose = angular.module('diagnose', [
    'ngResource',
    'ngTable',
    'ngTableToCsv',
    'ui.router',
    'ui.bootstrap',
    'table.services',
    'rest.url.helper',
    'data.services',
]);


mod_diagnose.config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
            .state('diagnose', {
                url: '/diagnose/:snapshotID',
                views: {
                    'main@': {
                        templateUrl: 'modules/diagnose/diagnose.html'
                    }
                }
            });     
            
    }])
     
	.filter('successOrDanger', function() {
		return function(input) {
			return input ? 'success' : 'danger';
		};
	})
	
    .filter('siNo', function() {
    	return function(input) {
    		return input ? 'Si' : 'No';
    	};
    })
    
    .filter('isValid', function() {
    	return function(input) {
    		return input ? 'Válido' : 'Inválido';
    	};
    })
    
    .filter('isTransformed', function() {
    	return function(input) {
    		return input ? ' | Transformado' : '';
    	};
    })
   
    .controller('diagnose', ['$rootScope', '$scope', '$state', '$stateParams', '$uibModal', 'TableSrv', 'RestURLHelper', 'DataSrv', function ($rootScope, $scope, $state, $stateParams,$uibModal, TableSrv, RestURLHelper, DataSrv) {
        'use strict';
       
        /**
         * Actualiza la consulta de diagnóstico
         */
        $scope.update_diagnose = function() {
        	
        	var fqList = $scope.currentFilterQueryList();
        	       	
        	// 
        	DataSrv.callRestWS( RestURLHelper.diagnoseURLByID($scope.snapshotID, fqList ), function(response) {	
        		
        		$scope.recordsSize = response.data.size;
        		$scope.validSize = response.data.validSize;
        		$scope.transformedSize = response.data.transformedSize;
        		
        		$scope.rulesTable = TableSrv.createNgTableFromArray(response.data.rules);
        		
        		$scope.facets = response.data.facets;
        		$scope.ruleNameByID = response.data.ruleNameByID;
        		
        		// Actualización de tabla de registros
        		var recordsURL = RestURLHelper.diagnoseRecordListURLByID($scope.snapshotID,  fqList);
        		$scope.recordsTable = TableSrv.createNgTableFromWsURL(recordsURL, 
    	            	function(data) { 
        					return { data:  data.content,
        							 total: data.totalElements    							 
        					};
        				}, 1, 20, [20]
    	        );
        		
        	
        	}, $scope.error_callback);
        };
        
        $scope.error_callback = function() {
        	alert("Error en llamada a WS de diagnóstico");
        };
        
        /**
         *  Cambia el valor de seleción de una faceta
         */
        $scope.toogleFacetFilter = function (facet) {
        	
        	if (facet == null) return;
        	
        	var key = facet.key.name + '@@"' + facet.value + '"';
        	
        	if ( $scope.select_facets[key] )
        		$scope.select_facets[key] = false;
        	else 
        		$scope.select_facets[key] = true; 
        	
        	$scope.update_diagnose();
        };
        
        /**
         * Indica si una faceta está seleccionada
         */
        $scope.is_facet_selected = function (facet) {
        	var key = facet.key.name + '@@"' + facet.value + '"';
        	
        	if ($scope.select_facets[key] == null)
        		return false;
        	else
        		return $scope.select_facets[key]; 
        	
        };
        
        
        /**
         * Retorna una lista de los fq en formato SOLR
         */
        $scope.currentFilterQueryList = function() {
       	 
       	 	var fq = []; 
       	 
	       	 angular.forEach($scope.select_facets, function(is_selected, key) { 
	       		 if (is_selected)
	       			 fq.push(key);
	       	 });
	     
	       	 return fq;
        };
        
        
        /** 
    	 * openRecordDiagnose: Apertura de modal de detalle de diagnostico de un registro
    	 *     
    	 **/	
    	 $scope.openRecordDiagnose = function (record) {
    	
    		    var modalInstance = $uibModal.open({
    		      animation: true,
    		      templateUrl: 'modules/diagnose/record-diagnose-tpl.html',
    		      controller: 'RecordDiagnoseCtrl',
    		      size: 'lg',
    		      resolve: {
    		  	      record: function() { return record; },
    	  		      rulesMap: function () { return $scope.ruleNameByID; },    
    		      }
  
    	    });
    	
    	    modalInstance.result.then( function () {}, function () {});
    	   
    	}; /* fin openRecordDiagnose */ 
    	
    	
    	  
        /** 
    	 * openRuleOccrStats: Apertura de modal de detalle de occr 
    	 *     
    	 **/	
    	 $scope.openRuleOccrStats = function (snapshotID, rule) {
    	
    		    var modalInstance = $uibModal.open({
    		      animation: true,
    		      templateUrl: 'modules/diagnose/rule-occrs-tpl.html',
    		      controller: 'RuleOccrStatsCtrl',
    		      size: 'lg',
    		      resolve: {
    		  	      snapshotID: function() { return snapshotID; }, 
    		  	      rule: function() { return rule; }, 
    		      }
  
    	    });
    	
    	    modalInstance.result.then( function () {}, function () {});
    	   
    	}; /* fin openRecordDiagnose */ 
        
        
  
        //////// MAIN /////////
        /**
         * Recolección de los parámetros y primer llamada al ws
         */
        
        if ( $stateParams.snapshotID != null) {
        	
            $scope.snapshotID = $stateParams.snapshotID;
            $scope.select_facets = {};
            
            $scope.update_diagnose();
        }
        
      
    }]);

mod_diagnose.controller('RecordDiagnoseCtrl', ['$scope', '$uibModalInstance', 'RestURLHelper', 'DataSrv', 'record', 'rulesMap', function ($scope, $uibModalInstance,RestURLHelper, DataSrv, record, rulesMap) {
	
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
	$scope.record = record;
	$scope.rules = rulesMap;
	
	DataSrv.callRestXMLWS( RestURLHelper.recordMetadataURLByID(record.id), function(response) {	
		
		$scope.recordMetadata = vkbeautify.xml(response.data);
		
    
    	//$('#modalViewMetadataBody .code').highlight();  
	});

}]); /* RecordDiagnoseCtrl */

mod_diagnose.controller('RuleOccrStatsCtrl', ['$scope', '$uibModalInstance', 'RestURLHelper', 'DataSrv', 'TableSrv', 'snapshotID', 'rule', function ($scope, $uibModalInstance,RestURLHelper, DataSrv, TableSrv, snapshotID, rule) {
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};

	$scope.snapshotID = snapshotID;
	$scope.ruleID = rule.ruleID;
	$scope.rule = rule;
	
	DataSrv.callRestWS( RestURLHelper.diagnoseRuleOccrURLByID($scope.snapshotID, $scope.ruleID), function(response) {	
		
			$scope.validOccrTable = TableSrv.createNgTableFromArray(response.data.validRuleOccrs);
			$scope.invalidOccrTable = TableSrv.createNgTableFromArray(response.data.invalidRuleOccrs);

	});

}]); /* RuleOccrStatsCtrl */
    
