/**
 * @module App
 * @summary First module loaded
 */

/*globals window, angular, document */

angular.module('overlay', [
    'ngResource',
    'ui.router',
    'ui.bootstrap'
])

    .provider('modalState', ['$stateProvider', function ($stateProvider) {
        'use strict';
        var provider = this;
        this.$get = function () {
            return provider;
        };
        this.state = function (stateName, options) {
            var modalInstance = null;
            $stateProvider.state(stateName, {
                url: options.url,
                onEnter: function ($modal, $state) {
                    modalInstance = $modal.open(options);
                    modalInstance.result['finally'](function () {
                        modalInstance = null;
                        if ($state.$current.name === stateName) {
                            window.history.back();
                            //$state.go('^');
                        }
                    });
                },
                onExit: function () {
                    if (modalInstance && modalInstance.close) {
                        modalInstance.close();
                    }
                }
            });
        };
    }])

    .config(['modalStateProvider', function (modalStateProvider) {
        'use strict';

        modalStateProvider.state('overlay', {
            url: '/overlay/:template',
            templateUrl: 'modules/overlay/overlay.html'
        });
    }]);