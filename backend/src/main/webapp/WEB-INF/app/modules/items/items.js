/**
 * @module Items
 * @summary Items module
 */

/*globals window, angular, document */

angular.module('items', [
    'ngResource',
    'ui.bootstrap',
    'ui.router'
])

    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
            .state('items', {
                url: '/items',
                views: {
                    'main@': {
                        templateUrl: 'modules/items/items.html'
                    }
                }
            })
            .state('items.new', {
                url: '/new',
                views: {
                    'sidebar@items': {
                        templateUrl: 'modules/item-new/item-new.html',
                        controller: 'item-new'
                    }
                }
            })
            .state('items.view', {
                url: '/:id',
                views: {
                    'sidebar@items': {
                        templateUrl: 'modules/item/item.html',
                        controller: 'item'
                    }
                }
            });
    }])

    .controller('items', ['$scope', '$stateParams', '$filter', 'Data', function ($scope, $stateParams, $filter, Data) {
        'use strict';
        
        $scope.filters = {
            name: '',
            category: ''
        };

        $scope.currentPage = 1;
        $scope.pageSize = 10;
        $scope.data = Data;
        $scope.data.query({section: 'items'});
        
        $scope.createDate = function () {
            var d = new Date();
            d.setHours(0);
            d.setMinutes(0);
            d.setSeconds(0);
            d.setMilliseconds(0);
            return d;
        };
        
        $scope.filter = function (key, value) {
            if (key === 'date') {
                $scope.dateType = value;
                $scope.dateStart = $scope.createDate();
                $scope.dateEnd = $scope.createDate();
                $scope.showRange = false;
                if (value === 'week') {
                    $scope.dateStart.setDate($scope.dateStart.getDate() - 7);
                } else if (value === 'month') {
                    $scope.dateStart.setMonth($scope.dateStart.getMonth() - 1);
                } else if (value === 'year') {
                    $scope.dateStart.setFullYear($scope.dateStart.getFullYear() - 1);
                } else if (value === 'range') {
                    $scope.showRange = true;
                    $scope.dateStart.setFullYear($scope.dateStart.getFullYear() - 1);
                }
            } else {
                $scope.filters[key] = value;
            }
        };
        
        $scope.openDate = function ($event, name) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope[name] = true;
        };
        
        $scope.selectDate = function (val) {
            $scope.dateType = 'range';
        };

        $scope.dateFilter = function (item) {
            var day = (1000 * 60 * 60 * 24),
                date = new Date(item.date);
            if (date.getTime() >= $scope.dateStart.getTime() && date.getTime() <= $scope.dateEnd.getTime() + day) {
                return true;
            } else {
                return false;
            }
        };

        $scope.filter('date', 'year');
    }])

    .filter('startFrom', [function () {
        'use strict';
        return function (input, value) {
            return input ? input.slice(value) : input;
        };
    }]);