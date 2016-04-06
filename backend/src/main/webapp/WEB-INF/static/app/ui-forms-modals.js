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
  
	// schema del formulario de redes
	$scope.network_schema = JSONFormSchemas.network_schema;
	
	// schema del formulario de origenes
	$scope.origins_schema = JSONFormSchemas.origins_schema;

	// estructura del formulario de redes
	$scope.network_form = JSONFormSchemas.network_form;
	
	// estructura del formulario de propiedades de redes
	$scope.np_form = JSONFormSchemas.network_properties_form;
	
	// estructura del formulario de origenes
	$scope.origins_form = JSONFormSchemas.origins_form;

	// datos formulario de propiedades de red
	$scope.np_model = {};
	
	// datos formulario de origenes de datos
	$scope.origins_model = {};

	
	
	if ( !isNew ) { // si no es una red nueva 

		// obtención de datos,  utilizando el id obtiene la url y luego el servico de datos entrega una network, eso va al form model
		DataSrv.get( RestURLHelper.networkURLByID(networkID), 
				function(network) {
						// el objeto de red obtenido es ahora el modelo del formulario
						$scope.network_model = network;		
						
						$scope.np_schema = JSONFormSchemas.generate_network_properties_schema( network.getLinkItems('properties') );
						$scope.np_model = JSONFormSchemas.generate_network_properties_model( network.getLinkItems('properties') );
				}								
		);
			
	} else { // si es una red nueva 
		
		$scope.network_model = {};
		
		// schema del formulario de propiedades
		DataSrv.get( RestURLHelper.propertyURL(), function(properties) {
			$scope.np_schema = JSONFormSchemas.generate_network_properties_schema( properties.getItems() );
		},
		onSaveError); // ATENCION!!!!!!!! CAMBIAR POR CALLBACK DE ERROR DE LECTURA		
		
	} // si no es una red nueva, crea un model vacio 

	
	
	/**
	 * Handler de errores de almacenamiento en la bd
	 */
	function onSaveError(error) { // error callback
	    $scope.save_error = true;
	    $scope.save_error_message = error.status + ": " + error.statusText;
	};
	
	

	// cuando se presion grabar
	$scope.onSubmit = function(form) {
	  
	  	// Se valida el formulario
	    $scope.$broadcast('schemaFormValidate');

	    // Si resulta válido
	    if (form.$valid) {
	    	
	    	// Si es una red nueva y no fue grabada todavía
	    	if ( isNew && !$scope.saved ) { 
	    		
	    		// Se llama al servicio de add con url de network y el modelo json del form
	    		DataSrv.add( RestURLHelper.networkURL(), $scope.network_model,
	    				
	    			function(network) { // callback de creación exitosa de red
	    				$scope.network_model = network; // se actualiza el modelo del form con el objeto actualizable
	    				
	    				///////// Agregado de propiedades  ////////////
	    				
	    				// Para cada propiedad del modelo json provisto por el formulario
	    	    		angular.forEach($scope.np_model, function(propertyValue, propertyName) {
	    	    			
	    	    			
	    	    			var propertyID = propertyName; // el id de una propiedad es su nombre, es necesario declarar esta variable para acceder más abajo
	    	    			
	    	    			// Se llama al servicio de add con url de network y el modelo json del form
	    		    		DataSrv.add( RestURLHelper.network_propertyURL(), {value:propertyValue}, 
	    		    			
	    		    			// Callback de agregado existoso de NetworkPropery	
	    		    			function(network_property) { 
	    		    				
	    		    				// Una vez agregada la NP debe vincularse con la red a la que pertence
	    		    				network_property.associate('network', RestURLHelper.urlFromEntity($scope.network_model), 
	    		    						
	    		    						// Callback de vinculación exitosa de NetworkProperty con Network
	    		    						function() { 
	    		    					
			    		    		    	    $scope.saved = true;	    
			    		    		    	    
			    		    		    	    // Una vez vinculada con Network se vincula con la Property
				    		    				network_property.associate('property',  RestURLHelper.propertyURLByID(propertyID) , 
				    		    						
				    		    						// Callback de vinculación exitosa de NetworkProperty con Property
				    		    						function() { // success callback
				    		    		    	    		$scope.saved = true;
				    		    		    			}, 
				    		    		    			onSaveError
				    		    		    	);
				    		    		    }, // fin callback NP con N
				    		    		    onSaveError
	    		    				);// fin associate NP con N
	    		    				
	    		    				
	    		    				$scope.saved = true; // se marca como grabado para que no se vuelva a agregar
	    		
	    		    			}, // fin de callback de agregado de NP
	    		    			onSaveError); // TODO: en caso de error debiera interrumpirse el ciclo 
	    	    		
	    	    		});// fin forEarch Properties
	    	    		
	    	    		///////// FIN -- Agregado de propiedades  ////////////
	    				
	    				$scope.saved = true; // se marca como grabado para que no se vuelva a agregar
	    				
	    			}, // fin callback de add Network 
	    			onSaveError
	    		); // fin de add Network 
	    	
	    	} // fin de nueva red
	    	else { // si es una red ya grabada entonces se llama a la función update 

	    	  // Se graba el modelo	
		      $scope.network_model.update(
		    	function() { // success callback
		    		
		    		// obtiene todas la propiedades para hacerles update de acuerdo a los datos de np_model
		    		angular.forEach($scope.network_model._properties._embeddedItems, function(networkProperty, i) {
		    			networkProperty.value = $scope.np_model[networkProperty.name]; // asigna el valor de np el valor del model del formulario
		    			networkProperty.update( function() {/*success callback*/}, onSaveError);
		    		});
		    		
		    		$scope.saved = true;
		    	},
		    	onSaveError
		      ); // fin de network_model.update
		      
	    	} /* fin de red ya grabada */ 
	      
	    } else { // si no es valido se avisa a la interfaz
	    	$scope.is_form_valid = false;
	    }
	}; /* fin de OnSubmit */ 
	
}); /* fin de NetworkEditCtrl */