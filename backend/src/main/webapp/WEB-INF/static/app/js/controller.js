var app = angular.module("myApp", ["ngTable", "ui.network.actions", "data.services", "table.services"]);

(function() {

  app.controller("mainController", mainController);
 
  mainController.$inject = ["$scope", 'TableSrv'];

  function mainController($scope, TableSrv) {

	  
    $scope.networksTable = TableSrv.createNgTableFromWsURL('/public/networks', function(data) {return data.networks.lenght;});
    
    // Estado de las redes
    $scope.networks = { areAllSelected: false, selected: {} };
    
    // watch para el check all de redes
    $scope.$watch('networks.areAllSelected', function(value) {
        angular.forEach($scope.networksTable.data, function(network) {
            if (angular.isDefined(network.acronym)) {
                $scope.networks.selected[network.acronym] = value;
            }
        });
    });
    
  } /* fin controller */
  
})();