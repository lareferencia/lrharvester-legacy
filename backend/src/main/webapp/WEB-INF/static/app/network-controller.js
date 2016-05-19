var ufm_module = angular.module('ui.forms.modals', 
		['ngAnimate', 'ui.bootstrap', 'schemaForm', 'data.services', 'table.services', 'network.json.schemas', 'rest.url.helper']);

ufm_module.controller('NetworkActionsController', 
function ($scope, $uibModal, RestURLHelper, DataSrv) {
	
	
  /**
   * Ejecuta una acción de red sobre una o más ids de red
   * @param Action
   * @param networkID or networkList
   */	
   $scope.executeNetworkAction = function (action, networkIDs, success_callback) {
	   DataSrv.callRestWS( RestURLHelper.networkActionURL(action,networkIDs), success_callback, function() { alert("Error en la llamada a networkAction");} )	  
   };
	

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

	
	/** 
	 * openEditOrigin: Apertura de modal de edición de Network 
	 * @param isNew Booleano indica si se crea o se carga la red
	 * @param model Objeto de modelo, es el origen si es un update (isNew=false) y es un network para agregar el nuevo origen si es nuevo (isNew=true)
	 * @param onChangeCallback función que será ejecutada para notificar el cambio al llamador
	 *     
	 ***/	
	$scope.openEditOrigin = function (isNew, model, onChangeCallback) {
	
		    var modalInstance = $uibModal.open({
		      animation: true,
		      templateUrl: 'origin-edit-tpl.html',
		      controller: 'OriginEditCtrl',
		      size: 'lg',
		      resolve: {
		    	isNew: function() { return isNew; },
		        model: function () { return model; },
		      }
		    });
	
		    modalInstance.result.then( 
		    		function () {
		    			onChangeCallback();
		    		}, 
		    		function () {
		    			onChangeCallback();
		    });
		   
	  }; /* fin openEditOrigin */ 
	  
	  
	  /** 
		 * deleteOrigin: Borrado de un origin
		 * @param network red a la cual pertenece
		 * @param origin a borrar
		 * @param onChangeCallback función que será ejecutada para notificar el cambio al llamador
		 *     
		 ***/
	  $scope.deleteOrigin = function  (network, origin, onChangeCallback) {
		
		   // llamada al borrado
		   origin.remove( function() {
			   
			   alert("removido");
			   // Si es removido exitosamente se recarga la información de la red a la que pertence y se llama al callback de refresco de UI
			   network.reload(onChangeCallback);
		   }
		   ///// ATENCION: FALTA LA LLAMADA AL CALLBACK DE ERROR
		   );   
		   
	  }; /* fin openEditOrigin */ 
  
  
}); /* fin NetworkActionsController */ 




ufm_module.controller('OriginEditCtrl', function ($scope, $uibModalInstance, DataSrv, TableSrv, RestURLHelper,  JSONNetworkSchemas, isNew, model) {
	
	
	// Indica si el formulario es válido, en principio se da por válido
	// TODO: Creo que esto hay que manejarlo con más elegancia
	$scope.is_form_valid = true;
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
	
	// schema del formulario de origen
	$scope.origin_schema = JSONNetworkSchemas.origin_schema;
	// estructura del formulario de origen
	$scope.origin_form = JSONNetworkSchemas.origin_form;
	// datos formulario de origen de datos
	$scope.origin_model = {};


	
	if ( !isNew ) { // si no es una red nueva 
		$scope.origin_model = model;
	} else { // si es una origen nuevo 
		$scope.origin_model = {};
		$scope.network_model = model; // la red a donde hay que agregarlo
	} 
	
	
	////////////
	// cuando se presion grabar
	$scope.onSubmit = function(form) {
			  
	  	// Se valida el formulario
	    $scope.$broadcast('schemaFormValidate');

	    // Si resulta válido
	    if (form.$valid) {
	    	
	    	// Si es un origin nueva y no fue grabada todavía
	    	if ( isNew && !$scope.saved ) { 
	    		
	    		// Se llama al servicio de add con url de orign y el modelo json del form
	    		DataSrv.add( RestURLHelper.originURL(), $scope.origin_model,
	    				
	    			function(origin) { // callback de creación exitosa de origin
	    				$scope.origin_model = origin; // se actualiza el modelo del form con el objeto actualizable
	    				
	    				 // Agregar el origen a la colecction origins de la network
	    				$scope.network_model.addToCollection('origins',  RestURLHelper.urlFromEntity($scope.origin_model) , 
	    						
	    						// Callback agregado exitosa de origin a origins de network
	    						function() { // success callback
	    		    	    		$scope.saved = true;
	    		    			}, 
	    		    			onSaveError
	    		    	); /* fin de associate*/
	 	    				
    				}, // fin callback de add origin 
    				onSaveError
	    		); // fin de add origin 
	    		
	    		
	    	
	    	} // fin de nuevo origen
	    	else { // si es un origen ya grabada entonces se llama a la función update 

	    	  // Se graba el modelo	
		      $scope.origin_model.update(
		    	function() { // success callback
		    		
		    		$scope.saved = true;
		    	},
		    	onSaveError
		      ); // fin de origin_model.update
		      
	    	} /* fin del origen ya grabado */ 
	      
	    } else { // si no es valido se avisa a la interfaz
	    	$scope.is_form_valid = false;
	    }
	}; /* fin de OnSubmit */
	
	/**
	 * Handler de errores de almacenamiento en la bd
	 */
	function onSaveError(error) { // error callback
	    $scope.save_error = true;
	    $scope.save_error_message = error.status + ": " + error.statusText;
	};
	

}); /*Fin de OriginEditCtrl*/



ufm_module.controller('NetworkEditCtrl', function ($scope, $uibModalInstance, DataSrv, TableSrv, RestURLHelper,  JSONNetworkSchemas, isNew, networkID) {
	
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};

	// Indica si el formulario es válido, en principio se da por válido
	// TODO: Creo que esto hay que manejarlo con más elegancia
	$scope.is_form_valid = true;
  
	// schema del formulario de redes
	$scope.network_schema = JSONNetworkSchemas.network_schema;
	
	// estructura del formulario de redes
	$scope.network_form = JSONNetworkSchemas.network_form;
	
	// estructura del formulario de propiedades de redes
	$scope.np_form = JSONNetworkSchemas.network_properties_form;
	
	$scope.network_validation_schema = JSONNetworkSchemas.network_validation_schema;
		
	
	$scope.network_validation_model = {};
	
	// llamadas a los servicios para carga de los validadores y transformadores disponibles
	DataSrv.get( RestURLHelper.validatorURL(), function(validators) { 
		
		DataSrv.get( RestURLHelper.transformerURL(), function(transformers) { 
			
			$scope.network_validation_form = JSONNetworkSchemas.network_validation_form(validators.getItems(),transformers.getItems());

			if (isNew ) {
				$scope.network_validation_model['transformer'] = RestURLHelper.urlFromEntity(transformers.getItems()[0]);
				$scope.network_validation_model['validator'] = RestURLHelper.urlFromEntity( validators.getItems()[0] );
			}
						
		});
		
	});
	

	// datos formulario de propiedades de red
	$scope.np_model = {};
	

	if ( !isNew ) { // si no es una red nueva 

		// obtención de datos,  utilizando el id obtiene la url y luego el servico de datos entrega una network, eso va al form model
		DataSrv.get( RestURLHelper.networkURLByID(networkID), 
				function(network) {
						// el objeto de red obtenido es ahora el modelo del formulario
						$scope.network_model = network;		
						
						$scope.np_schema = JSONNetworkSchemas.generate_network_properties_schema( network.getLinkItems('properties') );
						$scope.np_model = JSONNetworkSchemas.generate_network_properties_model( network.getLinkItems('properties') );
						
						
						// se cargan los valores de transformadores y validadores de la red
						$scope.network_validation_model = {};
						$scope.network_validation_model['validator'] = RestURLHelper.urlFromEntity(network.getLinkItems("validator")),
						$scope.network_validation_model['transformer'] =  RestURLHelper.urlFromEntity(network.getLinkItems("transformer"));

						// Carga los orígenes
						$scope.originsTable = TableSrv.createNgTableFromGetData( function(params) { return $scope.network_model.getLinkItems('origins'); }) ;
						$scope.originsTableRefreshCallback = function() {  $scope.originsTable.reload(); };
				}							
		);
		
		 
			
	} else { // si es una red nueva 
		
		$scope.network_model = {};
		
		
		// schema del formulario de propiedades
		DataSrv.get( RestURLHelper.propertyURL(), function(properties) {
			$scope.np_schema = JSONNetworkSchemas.generate_network_properties_schema( properties.getItems() );
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
	    				
	    		 		///// Asociación del validador y el transformador
	    				$scope.network_model.associate('validator', $scope.network_validation_model['validator'], function() {
		    				$scope.network_model.associate('transformer', $scope.network_validation_model['transformer'], function() {}, onSaveError );
	    				}, onSaveError );
	    				
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
		    		
		    		///// Asociación del validador y el transformador
    				$scope.network_model.associate('validator', $scope.network_validation_model['validator'], function() {
    					$scope.network_model.associate('transformer', $scope.network_validation_model['transformer'], function() {}, onSaveError );
    				}, onSaveError );
    				
		    		
		    		// obtiene todas la propiedades para hacerles update de acuerdo a los datos de np_model
		    		angular.forEach($scope.network_model.getLinkItems('properties'), function(networkProperty, i) {
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