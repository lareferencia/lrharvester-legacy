/**
 * @module Item new
 * @summary Item new module
 */

/*globals window, angular, document */

angular.module('item-new', [
    'ui.router'
])
    .controller('item-new', ['$scope', '$state', 'Data', function ($scope, $state, Data) {
        'use strict';
        
        $scope.data = Data;
        $scope.data.items_new = {
            date: new Date()
        };
        $scope.accordianOpen1 = true;
        $scope.accordianOpen2 = false;

        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.post({section: 'items', id: 0}, function (data) {
                $scope.isDisabled = false;
                $state.go('items');
            });
        };
    }]);