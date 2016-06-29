/**
 * @module Items
 * @summary Items module
 */

/*globals window, angular, document */

angular.module('transformers', [
    'ngResource',
    'ui.bootstrap',
    'ui.router',
    'schemaForm',
    'table.services',
    'rest.url.helper',
    'data.services',
    'transformer'
])

    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
            .state('transformers', {
                url: '/transformers',
                views: {
                    'main@': {
                        templateUrl: 'modules/transformers/transformers.html'
                    }
                }
            });
            
        
   
    }])

    .controller('transformers', ['$scope', '$stateParams', '$filter', 'TableSrv', 'RestURLHelper', 'DataSrv', function ($scope, $stateParams, $filter, TableSrv, RestURLHelper, DataSrv) {
       
    	'use strict';

    	
    	DataSrv.list( RestURLHelper.transformerURL(), function(transformers) {
    		
    		$scope.transformers = transformers;
    		$scope.transformersTable = TableSrv.createNgTableFromGetData( 
    				function(params) { 
    					return $scope.transformers.getItems(); 
    				}
    		);
    		
			
    	});
    	
    	/**
    	 * Refresh de tabla
    	 */
    	$scope.transformersTableRefreshCallback = function() {  
        	$scope.transformers.reload( function (obj) { $scope.transformersTable.reload();  } );
    	};
    	
    	
    	 /** 
    	 * deleteValidator: Borrado de un transformador
    	 ***/
    	  $scope.deleteTransformer = function(transformer) {
    		      		  
    		   // llamada al borrado
    		  transformer.remove( function() {	
    			  		$scope.transformersTableRefreshCallback(); 
    		  }
    		   ///// ATENCION: FALTA LA LLAMADA AL CALLBACK DE ERROR
    		  );   	   
    	  }; 
        
    
			
    	
    }]);