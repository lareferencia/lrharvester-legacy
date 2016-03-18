var ufm_module = angular.module('ui.forms.modals', 
		['ngAnimate', 'ui.bootstrap', 'schemaForm', 'data.services', 'model.json.schemas', 'rest.url.helper']);

ufm_module.controller('NetworkActionsController', 
function ($scope, $uibModal) {

  // Apertura de modal de edición de Network	
  $scope.openEditNetwork = function (networkID, onChangeCallback) {

    var modalInstance = $uibModal.open({
      animation: true,
      templateUrl: 'network-edit-tpl.html',
      controller: 'NetworkEditCtrl',
      size: 'lg',
      resolve: {
        networkID: function () { return networkID; },
      }
    });

    modalInstance.result.then( 
    		function () {
    			onChangeCallback();
    		}, 
    		function () {
    			onChangeCallback();
    			//alert("Se canceló el modal");
    		});
    };
  
});

// Please note that $uibModalInstance represents a modal window (instance) dependency.
// It is not the same as the $uibModal service used above.
ufm_module.controller('NetworkEditCtrl', function ($scope, $uibModalInstance, DataSrv, RestURLHelper,  JSONFormSchemas, networkID) {

  $scope.is_form_valid = true;
  
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

  // obtención de datos,  utilizando el id obtiene la url y luego el servico de datos entrega una network, eso va al form model
  DataSrv.get( RestURLHelper.networkURLByID(networkID), function(network) {
	  $scope.model = network;
  });
   
  // cuando se presion grabar
  $scope.onSubmit = function(form) {
	  
	  	// Se valida el formulario
	    $scope.$broadcast('schemaFormValidate');

	    // Si resulta válido
	    if (form.$valid) {

	    	// Se graba el modelo	
	      $scope.model.update(
	    	function() { // success callback
	    	    $scope.saved = true;
	    	},
	    	function(error) { // error callback
	    	    $scope.save_error = true;
	    	    $scope.save_error_message = error.status + ": " + error.statusText;
	    	}
	      );
	      
	    } else { // si no es valido se avisa a la interfaz
	    	$scope.is_form_valid = false;
	    }
  };
  
});