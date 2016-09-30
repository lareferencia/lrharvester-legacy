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
    'timer',
    'table.services',
    'rest.url.rebase',
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

	.filter('Status', function() {
		return function(input) {
			
			switch (input) {
			case "VALID": return "COSECHA VÁLIDA";
			case "HARVESTING_FINISHED_ERROR": return "COSECHA INVÁLIDA";
			case "HARVESTING": return "COSECHANDO ...";
			case "HARVESTING_STOPPED": return "COSECHA DETENIDA";
			case "RETRYING": return "REITENTANDO ...";
			case "INDEXING": return "INDEXANDO ...";

			default:
				return "DESCONOCIDA";
			}
			
		};
	})
	
	.filter('SiNo', function() {
    	return function(input) {
    		return input ? 'Si' : 'No';
    	};
    })
    
    .filter('ObjectID', function() {
    	return function(input) {
    		
    		var splitted_url = input._links.self.href.split("/");
    		return splitted_url[ splitted_url.length - 1 ];
    	};
    })
    
    .directive('ngConfirmClick', [
		  function() {
		    return {
		      priority: 1,
		      link: function(scope, element, attr) {
		        var msg = attr.ngConfirmClick || "Are you sure?";
		        var clickAction = attr.ngClick;
		        attr.ngClick = "";
		        element.bind('click', function(event) {
		          if (window.confirm(msg)) {
		            scope.$eval(clickAction)
		          }
		        });
		      }
		    };
		  }
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

    .controller('app', ['$rootScope', '$scope', '$state', '$uibModal', 'TableSrv', 'RestURLHelper', 'DataSrv', function ($rootScope, $scope, $state, $uibModal, TableSrv, RestURLHelper, DataSrv) {
        'use strict';
        
        $scope.loggedIn = true;
        $scope.navbarCollapsed = true;
        
        
        /*** Timer ******/
    	var MAX_SECS = 5;	
    	
        /* Indica acción ejecutada, se usa para mostrar alert */
        $scope.actionExecuted = false;
    	
    	$scope.restartTimerRemaining = function() {
          	$scope.timerRemainingSeconds = MAX_SECS;
        };	
    	
    	$scope.timerRunning = true;
    	$scope.timerRemainingSeconds = MAX_SECS;

        $scope.timerType = '';

        $scope.startTimer = function (){
        	$scope.restartTimerRemaining();
            $scope.$broadcast('timer-start');
            $scope.timerRunning = true;
        };

        $scope.stopTimer = function (){
            $scope.$broadcast('timer-stop');
            $scope.timerRunning = false;
        };

        $scope.$on('timer-tick', function (event, args) {
       	 
	       	 if ( $scope.timerRemainingSeconds == 0) {
	       		 $scope.networksTableRefreshCallback();
	       		 $scope.restartTimerRemaining();	       		 
	       		 $scope.actionExecuted = false;
	       	 }
	       	 else {
	       		 $scope.timerRemainingSeconds--;
	       	 }
        });   
        /*** FIN DE RUTINAS TIMER ****/
        
        

        
        
        $scope.networksTable = TableSrv.createNgTableFromWsURL(RestURLHelper.listNetworks(), 
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
         $scope.executeNetworkAction = function (action, networkIDs, success_callback, must_confim) {
         	 
        	 if (networkIDs != null && networkIDs != "" ) {
        		 DataSrv.callRestWS( RestURLHelper.networkActionURL(action,networkIDs), success_callback, function() { 
        			 alert("Error en la llamada a networkAction. Recargue la aplicación.");
        		 });
        		 
        		 $scope.restartTimerRemaining(); 
        		 $scope.actionExecuted = true;
        		 $scope.actionIdentifier = action;
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
         
   	  
         /** 
     	 * openHistory 
     	 *     
     	 **/	
     	 $scope.openHistory = function (networkID) {
     	
     		    var modalInstance = $uibModal.open({
     		      animation: true,
     		      templateUrl: 'modules/app/history-tpl.html',
     		      controller: 'HistoryCtrl',
     		      size: 'lg',
     		      resolve: {
     		  	      networkID: function() { return networkID; }
     		      }
   
     	    });
     	
     	    modalInstance.result.then( function () {}, function () {});
     	   
     	}; /* fin openRecordDiagnose */ 
     	
       
 
     
         
    }])

    /* History Controller*/
	.controller('HistoryCtrl', ['$scope', '$uibModal', '$uibModalInstance', 'RestURLHelper', 'DataSrv', 'TableSrv','networkID', function ($scope, $uibModal, $uibModalInstance,RestURLHelper, DataSrv, TableSrv, networkID) {
	
	
		 /** 
      	 * loadSnapshots 
      	 **/
     	$scope.loadSnapshots = function(networkID) {
	     	DataSrv.callRestWS( RestURLHelper.networkSnapshotsURLByID(networkID), function(response) {		
				$scope.snapshotsTable = TableSrv.createNgTableFromArray(response.data._embedded.snapshot);
			});
		};
		
		$scope.loadSnapshots(networkID);
		
		// Accciones de los botones del modal
		$scope.ok = function () { $uibModalInstance.close(null); };
		$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
		
		 /** 
     	 * openLog
     	 **/	
     	 $scope.openLog = function (snapshot) {
     	
     		    var modalInstance = $uibModal.open({
     		      animation: true,
     		      templateUrl: 'modules/app/log-tpl.html',
     		      controller: 'LogCtrl',
     		      size: 'lg',
     		      resolve: {
     		  	      snapshotID: function() { return RestURLHelper.OIDfromEntity(snapshot); }
     		      }
   
     	    });
     	
     	    modalInstance.result.then( function () {}, function () {});
     	   
     	}; /* fin openRecordDiagnose */ 
     	
     	 /** 
     	 * stopHarvesting  
     	 **/	
     	 $scope.stopHarvesting = function (snapshot) {
     	
     		  RestURLHelper.OIDfromEntity(snapshot); 
     		  
     		  DataSrv.callRestWS( RestURLHelper.networkActionURL('STOP_HARVESTING',RestURLHelper.OIDfromEntity(snapshot)), function(response) {
     				$scope.loadSnapshots(networkID); 
     		  });
	
     	 };
     	 
     	
		
     	 

}])
	/* Log Controller*/
	.controller('LogCtrl', ['$scope', '$uibModalInstance', 'RestURLHelper', 'DataSrv', 'TableSrv','snapshotID', function ($scope, $uibModalInstance,RestURLHelper, DataSrv, TableSrv, snapshotID) {
	
	
		DataSrv.callRestWS( RestURLHelper.snapshotLogURLByID(snapshotID), function(response) {		
			$scope.logTable = TableSrv.createNgTableFromArray(response.data._embedded.log);
		});
		
		
		// Accciones de los botones del modal
		$scope.ok = function () { $uibModalInstance.close(null); };
		$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
		
	
}]);  
