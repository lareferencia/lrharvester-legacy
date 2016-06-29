/**
 * @module App
 * @summary First module loaded
 */

/*globals window, angular, document */

angular.module('app', [
    'ngResource',
    'ngTable',
    'ui.router',
    'ui.bootstrap',
    'table.services',
    'rest.url.helper',
    'data.services',
    'origin',
    'network',
    'validators',
    'validator',
    'transformers',
    'transformer',
    'diagnose'

])
    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
            .state('start', {
                url: '',
                views: {
                    'main': {
                        templateUrl: 'modules/app/app.html'
                    }
                }
            });
    }])

    .controller('app', ['$rootScope', '$scope', '$state', 'TableSrv', 'RestURLHelper', 'DataSrv', function ($rootScope, $scope, $state, TableSrv, RestURLHelper, DataSrv) {
        'use strict';
        
        $scope.loggedIn = true;
        $scope.navbarCollapsed = true;
        
        
        $scope.networksTable = TableSrv.createNgTableFromWsURL('/public/networks', 
        		function(data) { 
					return { 
							 data:   data.networks,
							 total: data.totalElements    							 
					};
				}, 1, 25
       
        );
            
        $scope.networksTableRefreshCallback = function() { $scope.networksTable.reload(); };
        
        // Estado de las redes
        $scope.networks = { areAllSelected: false, selected: {} };
        
        // watch para el check all de redes
        $scope.$watch('networks.areAllSelected', function(value) {
            angular.forEach($scope.networksTable.data, function(network) {
                if (angular.isDefined(network.networkID)) {
                    $scope.networks.selected[network.networkID] = value;
                }
            });
        });
        
        /**
         * Ejecuta una acción de red sobre una o más ids de red
         * @param Action
         * @param networkID or networkList
         */	
         $scope.executeNetworkAction = function (action, networkIDs, success_callback) {
        	 
        	 if (networkIDs != null && networkIDs != "" ) {
        		 DataSrv.callRestWS( RestURLHelper.networkActionURL(action,networkIDs), success_callback, function() { alert("Error en la llamada a networkAction");} );	
        	 }
         };
         
         /**
          * Retorna una lista de los ids de reds seleccionados separados por comas en formato string
          */
         $scope.currentSelectedNetworkList = function() {
        	 
        	 var selected = []; 
        	 
        	 angular.forEach($scope.networks.selected, function(value, key) {
        		 if (value) 
        			 selected.push(key);
        	 });
        	 
        	 return  selected.join(',');
        	 
         };
         
         


    }])

   
   ;