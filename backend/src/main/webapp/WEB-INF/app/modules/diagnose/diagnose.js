/**
 * @module App
 * @summary First module loaded
 */

/*globals window, angular, document */

angular.module('diagnose', [
    'ngResource',
    'ngTable',
    'ui.router',
    'ui.bootstrap',
    'table.services',
    'rest.url.helper',
    'data.services',


])
    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
            .state('diagnose', {
                url: '/diagnose/:snapshotID',
                views: {
                    'main': {
                        templateUrl: 'modules/diagnose/diagnose.html'
                    }
                }
            });     
            
    }])

    .controller('diagnose', ['$rootScope', '$scope', '$state', '$stateParams', 'TableSrv', 'RestURLHelper', 'DataSrv', function ($rootScope, $scope, $state, $stateParams, TableSrv, RestURLHelper, DataSrv) {
        'use strict';
       
  
        $scope.update_diagnose = function() {
        	
        	
        	// 
        	DataSrv.callRestWS( RestURLHelper.diagnoseURLByID($scope.snapshotID), function(response) {	
        		
        		$scope.recordsSize = response.data.size;
        		$scope.validSize = response.data.validSize;
        		$scope.transformedSize = response.data.transformedSize;
        		
        		$scope.rulesTable = TableSrv.createNgTableFromArray(response.data.rules);
        		$scope.facets = response.data.facets;
        		
        		
        	}, $scope.error_callback);
        	
    
        };
        
        $scope.error_callback = function() {
        	alert("Error en llamada a WS de diagnóstico");
        };
        
        
        if ( $stateParams.snapshotID != null) {
        	
            $scope.snapshotID = $stateParams.snapshotID;
            
            $scope.update_diagnose();
        }
        

    }])

   
   ;