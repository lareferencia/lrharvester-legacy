/**
 * @module Items
 * @summary Items module
 */

/*globals window, angular, document */

angular.module('validators', [
    'ngResource',
    'ui.bootstrap',
    'ui.router',
    'schemaForm',
    'table.services',
    'rest.url.helper',
    'data.services',
    'validation.json.schemas',
    'validator'
])

    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
            .state('validators', {
                url: '/validators',
                views: {
                    'main@': {
                        templateUrl: 'modules/validators/validators.html'
                    }
                }
            });
            
        
   
    }])

    .controller('validators', ['$scope', '$stateParams', '$filter', 'TableSrv', 'RestURLHelper', 'DataSrv', 'JSONNetworkSchemas', function ($scope, $stateParams, $filter, TableSrv, RestURLHelper, DataSrv, JSONNetworkSchemas) {
       
    	'use strict';

					    	
   	 	/*$scope.validatorsTable = TableSrv.createNgTableFromWsURL(
							RestURLHelper.validatorURL(), function(data) {
								return data._embedded.validator;
							}, function(data) {
								return data.page.totalElements;
							}, 0, []);*/
    	
    	
    	DataSrv.list( RestURLHelper.validatorURL(), function(validators) {
    		
    		$scope.validators = validators;
    		$scope.validatorsTable = TableSrv.createNgTableFromGetData( 
    				function(params) { 
    					return $scope.validators.getItems(); 
    				}
    		);
    		
			
    	});
    	
    	/**
    	 * Refresh de tabla
    	 */
    	$scope.validatorsTableRefreshCallback = function() {  
        	$scope.validators.reload( function (obj) { $scope.validatorsTable.reload();  } );
    	};
    	
    	
    	 /** 
    	 * deleteValidator: Borrado de un validador
    	 ***/
    	  $scope.deleteValidator = function(validator) {
    		      		  
    		   // llamada al borrado
    		  validator.remove( function() {	
    			  		$scope.validatorsTableRefreshCallback(); 
    		  }
    		   ///// ATENCION: FALTA LA LLAMADA AL CALLBACK DE ERROR
    		  );   	   
    	  }; 
        
    
			
    	
    }]);