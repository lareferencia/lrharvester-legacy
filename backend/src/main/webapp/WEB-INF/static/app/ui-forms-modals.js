var ufm_module = angular.module('ui.forms.modals', 
		['ngAnimate', 'ui.bootstrap', 'schemaForm', 'data.services', 'model.json.schemas', 'rest.url.helper']);


ufm_module.controller('DropdownCtrl', function ($scope, $log) {
	  $scope.items = [
	    'The first choice!',
	    'And another choice for you.',
	    'but wait! A third!'
	  ];

	  $scope.status = {
	    isopen: false
	  };

	  $scope.toggled = function(open) {
	    $log.log('Dropdown is now: ', open);
	  };

	  $scope.toggleDropdown = function($event) {
	    $event.preventDefault();
	    $event.stopPropagation();
	    $scope.status.isopen = !$scope.status.isopen;
	  };

	  $scope.appendToEl = angular.element(document.querySelector('#dropdown-long-content'));
});

ufm_module.controller('NetworkActionsController', 
function ($scope, $uibModal) {

  /** 
   * openEditNetwork: Apertura de modal de edición de Network 
   * @param isNew Booleano indica si se crea o se carga la red
   * @param networkID Long identificador de la red a editar, sólo tiene sentido cuando isNew == true
   * @param onChangeCallback función que será ejecutada para notificar el cambio al llamador
   *     
   ***/	
  $scope.openEditNetwork = function (isNew, networkID, onChangeCallback) {

	    var modalInstance = $uibModal.open({
	      animation: true,
	      templateUrl: 'network-edit-tpl.html',
	      controller: 'NetworkEditCtrl',
	      size: 'lg',
	      resolve: {
	    	isNew: function() { return isNew; },
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
	   
    }; /* fin openEditNetwork */ 
    

});

// Please note that $uibModalInstance represents a modal window (instance) dependency.
// It is not the same as the $uibModal service used above.
ufm_module.controller('NetworkEditCtrl', function ($scope, $uibModalInstance, DataSrv, RestURLHelper,  JSONFormSchemas, isNew, networkID) {

	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};

	// Indica si el formulario es válido, en principio se da por válido
	// TODO: Creo que esto hay que manejarlo con más elegancia
	$scope.is_form_valid = true;
  
	// schema del formulario
	$scope.schema = JSONFormSchemas.network_schema;
  
	// estructura del formulario
	$scope.form = JSONFormSchemas.network_form;
	
	if ( !isNew ) { // si no es una red nueva 

		// obtención de datos,  utilizando el id obtiene la url y luego el servico de datos entrega una network, eso va al form model
		DataSrv.get( RestURLHelper.networkURLByID(networkID), function(network) {
			$scope.model = network;
		});
		
	} else { $scope.model = {}; } // si no es una red nueva, crea un model vacio 
		

	// cuando se presion grabar
	$scope.onSubmit = function(form) {
	  
	  	// Se valida el formulario
	    $scope.$broadcast('schemaFormValidate');

	    // Si resulta válido
	    if (form.$valid) {
	    	
	    	if ( isNew && !$scope.saved ) { // Si es una red nueva y no fue grabada todavía
	    		
	    		// Se llama al servicio de add con url de network y el modelo json del form
	    		DataSrv.add('/rest/network/', $scope.model, 
	    			function(network) {
	    				$scope.model = network; // se actualiza el modelo del form con el objeto actualizable
	    				$scope.saved = true; // se marca como grabado para que no se vuelva a agregar
	    			},
			    	function(error) { // error callback
			    	    $scope.save_error = true;
			    	    $scope.save_error_message = error.status + ": " + error.statusText;
			    	}
	    		);
	    		
	    	} else { // si es una red ya grabada entonces se llama a la función update 

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
	    	} /* fin de nueva red o ya grabada */ 
	      
	    } else { // si no es valido se avisa a la interfaz
	    	$scope.is_form_valid = false;
	    }
	}; /* fin de OnSubmit */ 
	
}); /* fin de NetworkEditCtrl */