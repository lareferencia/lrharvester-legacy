angular.module('ui.network.actions', ['ngAnimate', 'ui.bootstrap', 'schemaForm', 'data.services', 'model.json.schemas']);

angular.module('ui.network.actions').controller('NetworkActionsController', 
function ($scope, $uibModal) {

	
  // Apertura de modal de edición de Network	
  $scope.openEditNetwork = function (network) {

    var modalInstance = $uibModal.open({
      animation: true,
      templateUrl: 'networkEditModal.html',
      controller: 'NetworkEditCtrl',
      size: 'lg',
      resolve: {
        network: function () {
          return network;
        }
      }
    });

    modalInstance.result.then( 
    		function (selectedItem) {
    			$scope.selected = selectedItem;
    			alert("Se cerró el modal");
    		}, 
    		function () {
    			alert("Se canceló el modal");
    		});
    };
  
});

// Please note that $uibModalInstance represents a modal window (instance) dependency.
// It is not the same as the $uibModal service used above.
angular.module('ui.network.actions').controller('NetworkEditCtrl', function ($scope, $uibModalInstance, network, DataSrv, JSONFormSchemas) {

  $scope.network = network;	
  
  $scope.ok = function () {
    $uibModalInstance.close(null);
  };

  $scope.cancel = function () {
    $uibModalInstance.dismiss('cancel');
  };
  
  // schema
  $scope.schema = JSONFormSchemas.network_schema;
  
  // estructura del formulario
  $scope.form = JSONFormSchemas.network_form;

  // obtención de datos
  DataSrv.get('', function(_network) {
	  $scope.model = _network;
  });
  
  // cuando se presion grabar
  $scope.onSubmit = function(form) {
	  
	  	// Se valida el formulario
	    $scope.$broadcast('schemaFormValidate');

	    // Si resulta válido
	    if (form.$valid) {
	      
	      // Se graba el modelo	
	      $scope.model.update(function() {
	    	  alert("Submit");
	    	  //$scope.networksTable.reload();
	      });
	    }
  };
  
});