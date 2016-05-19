/**
 * @module Item
 * @summary Item module
 */

/*globals window, angular, document */

angular.module('validator', [
    'ui.router',
])


    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
        .state('validator', {
            url: '/validator',
            views: {
                'main@': {
                    templateUrl: 'modules/validator/validator.html'
                }
            }
        })
        .state('validator.new', {
            url: '/new'
        })
        .state('validator.edit', {
            url: '/:validatorID',
        });     
   
    }])
    .controller('validator', ['$rootScope', '$scope', '$stateParams', function ($rootScope, $scope, $stateParams) {
        'use strict';
        
        $scope.data = "test";
      
    }]);


