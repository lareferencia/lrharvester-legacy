/**
 * @module Item
 * @summary Item module
 */

/*globals window, angular, document */

angular.module('item', [
    'ui.router'
])
    .controller('item', ['$rootScope', '$scope', '$stateParams', 'Data', function ($rootScope, $scope, $stateParams, Data) {
        'use strict';
        
        $scope.data = Data;
        $scope.editable = false;
        $scope.num = false;
        
        if ($stateParams.id) {
            $rootScope.id = Number($stateParams.id);
            $scope.data.get({section: 'items', id: $rootScope.id}, function (data) {
                $scope.item = data;
            });
        }
        
        $scope.edit = function () {
            $scope.editable = true;
        };
        
        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.post({section: 'items', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
            });
        };
        
        $scope.addSection = function () {
            var copy = angular.copy($scope.item.sections[$scope.item.sections.length - 1]);
            $scope.item.sections.push(copy);
        };
        
        $scope.removeSection = function () {
            $scope.item.sections.splice($scope.item.sections.length - 1, $scope.item.sections.length);
        };
    }])

    .directive('player', function () {
        'use strict';
        return {
            restrict: 'E',
            scope: {
                videos: '='
            },
            link: function (scope, element, attrs) {
                var video = element.find('video');
                element.addClass('player');
                scope.playing = false;
                
                video.on('timeupdate', function (e) {
                    scope.$apply(function () {
                        scope.percent = (video[0].currentTime / video[0].duration) * 100;
                    });
                });
                
                scope.frame = function (num) {
                    if (video[0].readyState !== 0) {
                        video[0].currentTime += num;
                    }
                };
                
                scope.toggle = function () {
                    if (video[0].paused === true) {
                        video[0].play();
                        scope.playing = true;
                    } else {
                        video[0].pause();
                        scope.playing = false;
                    }
                };
            },
            template: '<video preload="none" poster="{{ videos[0].poster }}">' +
                            '<source ng-repeat="item in videos" ng-src="{{ item.src | trusted }}" type="video/{{ item.type }}" />' +
                            '<track kind="captions" ng-src="{{ videos[0].captions | trusted }}" srclang="en" label="English" />' +
                        '</video>' +
                        '<progressbar value="percent" max="100"></progressbar>' +
                        '<div class="controls noselect">' +
                            '<a ng-click="frame(-0.04)"><span class="glyphicon glyphicon-step-backward"></span></a>' +
                            '<a ng-click="toggle()"><span class="glyphicon glyphicon-play" ng-show="!playing"></span><span class="glyphicon glyphicon-pause" ng-show="playing"></span></a>' +
                            '<a ng-click="frame(0.04)"><span class="glyphicon glyphicon-step-forward"></span></a>' +
                        '</div>'
        };
    })

    .filter('timecode', [function () {
        'use strict';
        return function (num) {
            function format(num) {
                return num < 10 ? String('0' + Math.floor(num)) : String(Math.floor(num));
            }
            
            var second = 30,
                minute = 60 * second,
                hour = 60 * minute,
                hours = format(num / hour),
                minutes = format((num % hour) / minute),
                seconds = format(((num % hour) % minute) / second),
                milliseconds = format(((num % hour) % minute) % second);
            
            return hours + ':' + minutes + ':' + seconds + ':' + milliseconds;
        };
    }])

    .directive('timecode', [function () {
        'use strict';
        
        return {
            restrict: 'AE',
            template: '<input type="text" ng-model="hours" ng-focus="onFocus($event)" ng-keyup="onChange(\'hours\', $event)" maxlength="2" /><span class="divider">:</span><input type="text" ng-model="minutes" ng-focus="onFocus($event)" ng-keyup="onChange(\'minutes\', $event)" maxlength="2" /><span class="divider">:</span><input type="text" ng-model="seconds" ng-focus="onFocus($event)" ng-keyup="onChange(\'seconds\', $event)" maxlength="2" /><span class="divider">:</span><input type="text" ng-model="milliseconds" ng-focus="onFocus($event)" ng-keyup="onChange(\'milliseconds\', $event)" maxlength="2" />',
            scope: {
                model: '=ngModel'
            },
            link: function (scope, element, attr) {
                var input = null,
                    second = attr.ngSecond ? Number(attr.ngSecond) : 1000,
                    minute = attr.ngMinute ? Number(attr.ngMinute) * second : 60 * second,
                    hour = attr.ngHour ? Number(attr.ngHour) * minute : 60 * minute;

                scope.numToTime = function (num) {
                    scope.model = num;
                    scope.hours = scope.format(num / hour);
                    scope.minutes = scope.format((num % hour) / minute);
                    scope.seconds = scope.format(((num % hour) % minute) / second);
                    scope.milliseconds = scope.format(((num % hour) % minute) % second);
                };

                scope.format = function (num) {
                    return num < 10 ? String('0' + Math.floor(num)) : String(Math.floor(num));
                };

                scope.timeToNum = function () {
                    scope.model = (Number(scope.hours) * hour) + (Number(scope.minutes) * minute) + (Number(scope.seconds) * second);
                };

                scope.onChange = function (type, e) {
                    if (input) {
                        if (e.which === 38 || e.which === 40) { // up, down
                            var num = 0;
                            if (type === 'hours') {
                                num = e.which === 38 ? scope.model + hour : scope.model - hour;
                            } else if (type === 'minutes') {
                                num = e.which === 38 ? scope.model + minute : scope.model - minute;
                            } else if (type === 'seconds') {
                                num = e.which === 38 ? scope.model + second : scope.model - second;
                            } else {
                                num = e.which === 38 ? scope.model + 1 : scope.model - 1;
                            }
                            if (num >= 0) {
                                scope.numToTime(num);
                                scope.highlight();
                            }
                        } else if (e.which === 37) { // left
                            e.preventDefault();
                            input.value = scope.format(input.value);
                            input = input.previousElementSibling ? input.previousElementSibling.previousElementSibling : input;
                            scope.highlight();
                        } else if (e.which === 39) { // right
                            e.preventDefault();
                            input.value = scope.format(input.value);
                            input = input.nextElementSibling ? input.nextElementSibling.nextElementSibling : input;
                            scope.highlight();
                        } else if (input.value.length === 2) {
                            input.value = scope.format(input.value);
                            scope.timeToNum();
                        }
                    }
                };

                scope.onFocus = function (e) {
                    input = e.target || input;
                    scope.highlight();
                };

                scope.highlight = function (e) {
                    window.setTimeout(function () {
                        input.focus();
                        input.select();
                    }, 1);
                };

                scope.numToTime(scope.model);
            }
        };
    }]);