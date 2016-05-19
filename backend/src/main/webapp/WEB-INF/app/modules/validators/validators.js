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

					    	
   	 	$scope.validatorsTable = TableSrv.createNgTableFromWsURL(
							RestURLHelper.validatorURL(), function(data) {
								return data._embedded.validator;
							}, function(data) {
								return data.page.totalElements;
							}, 0, []);

		$scope.validatorsTableRefreshCallback = function() {
			$scope.validatorsTable.reload();
		};
			
    	
    }]);