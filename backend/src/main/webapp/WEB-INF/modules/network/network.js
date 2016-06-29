/**
 * @module Items
 * @summary Items module
 */

/*globals window, angular, document */

angular.module('network', [
    'ngResource',
    'ui.bootstrap',
    'ui.router',
    'schemaForm',
    'table.services',
    'rest.url.helper',
    'data.services',
    'network.json.schemas'
])

    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
            .state('network', {
            	abstract: true,
                url: '/network',
                views: {
                    'main@': {
                        templateUrl: 'modules/network/network.html'
                    }
                }
            })
            .state('network.new', {
                url: '/new'
            })
            .state('network.edit', {
                url: '/:id',
            });
        
       
        
    }])

    .controller('network', ['$scope', '$stateParams', '$filter', 'TableSrv', 'RestURLHelper', 'DataSrv', 'JSONNetworkSchemas', function ($scope, $stateParams, $filter, TableSrv, RestURLHelper, DataSrv, JSONNetworkSchemas) {
       
    	'use strict';
    	
    	$scope.is_new = true;
    	
    	if ($stateParams.id != null) {	
            $scope.network_id = Number($stateParams.id);
            $scope.is_new = false;
    	}
        
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

    			if ($scope.is_new) {
    				
    				if ( transformers.getItems()[0] != null )
    					$scope.network_validation_model['transformer'] = RestURLHelper.urlFromEntity(transformers.getItems()[0]);
    				
    				if ( validators.getItems()[0] != null )
    					$scope.network_validation_model['validator'] = RestURLHelper.urlFromEntity( validators.getItems()[0] );
    			}
    						
    		});
    		
    	});
    	

    	// datos formulario de propiedades de red
    	$scope.np_model = {};
    	
    	if ($scope.is_new) { // si es una red nueva 
    		
    		$scope.network_model = {};
    		
    		// schema del formulario de propiedades
    		DataSrv.get( RestURLHelper.propertyURL(), function(properties) {
    			$scope.np_schema = JSONNetworkSchemas.generate_network_properties_schema( properties.getItems() );
    		},
    		onSaveError); // ATENCION!!!!!!!! CAMBIAR POR CALLBACK DE ERROR DE LECTURA		
    		
    	} else { // Si no es una red nueva 
    		
	    	// obtención de datos,  utilizando el id obtiene la url y luego el servico de datos entrega una network, eso va al form model
			DataSrv.get( RestURLHelper.networkURLByID($scope.network_id), 
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
			
    	}
    	
    	
	/**
	 * Handler de errores de almacenamiento en la bd
	 */
	function onSaveError(error) { // error callback
	 	$scope.saved = false;
	    $scope.save_error = true;
	    $scope.save_error_message = error.status + ": " + error.statusText;
	};
    	
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
		   },
		   onSaveError		   
		   );   
		   
	  }; /* fin openEditOrigin */ 
  
    	
    	/***
    	 * Handler del submit del formulario
    	 ***/
    	// cuando se presion grabar
    	$scope.onSubmit = function(form) {
    	  
    	  	// Se valida el formulario
    	    $scope.$broadcast('schemaFormValidate');

    	    // Si resulta válido
    	    if (form.$valid) {
    	    	
    		 	$scope.saved = false;
        	    $scope.save_error = false;
    	    	
    	    	// Si es una red nueva y no fue grabada todavía
    	    	if ( $scope.is_new ) { 
    	    		
    	    		// Se llama al servicio de add con url de network y el modelo json del form
    	    		DataSrv.add( RestURLHelper.networkURL(), $scope.network_model,
    	    				
    	    			function(network) { // callback de creación exitosa de red
    	    			
    	    			 	$scope.saved = true;
    	    				$scope.is_new = false; // ya no es una red nueva
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
    	    		    					    			    		    		    	    
    			    		    		    	    // Una vez vinculada con Network se vincula con la Property
    				    		    				network_property.associate('property',  RestURLHelper.propertyURLByID(propertyID) , 
    				    		    						
    				    		    						// Callback de vinculación exitosa de NetworkProperty con Property
    				    		    						function() { /* success callback */ }, 
    				    		    		    			onSaveError
    				    		    		    	);
    				    		    		    }, // fin callback NP con N
    				    		    		    onSaveError
    	    		    				);// fin associate NP con N
    	    		    				
    	    		    			}, // fin de callback de agregado de NP
    	    		    			onSaveError); // TODO: en caso de error debiera interrumpirse el ciclo 
    	    	    		
    	    	    		});// fin forEarch Properties
    	    	    		
    	    	    		///////// FIN -- Agregado de propiedades  ////////////
    	    				    	    				
    	    			}, // fin callback de add Network 
    	    			onSaveError
    	    		); // fin de add Network 
    	    	
    	    	} // fin de nueva red
    	    	
    	    	else { // si es una red ya grabada entonces se llama a la función update 

    	    	  // Se graba el modelo	
    		      $scope.network_model.update(
    		    	function() { // success callback
    		    		
    				 	$scope.saved = true;
    		    		
    		    		///// Asociación del validador y el transformador
        				$scope.network_model.associate('validator', $scope.network_validation_model['validator'], function() {
        					$scope.network_model.associate('transformer', $scope.network_validation_model['transformer'], function() {}, onSaveError );
        				}, onSaveError );
        				
    		    		
    		    		// obtiene todas la propiedades para hacerles update de acuerdo a los datos de np_model
    		    		angular.forEach($scope.network_model.getLinkItems('properties'), function(networkProperty, i) {
    		    			networkProperty.value = $scope.np_model[networkProperty.name]; // asigna el valor de np el valor del model del formulario
    		    			networkProperty.update( function() {/*success callback*/}, onSaveError);
    		    		});
    		    		
    		    	},
    		    	onSaveError
    		      ); // fin de network_model.update
    		      
    	    	} /* fin de red ya grabada */ 
    	      
    	    } else { // si no es valido se avisa a la interfaz
    	    	$scope.is_form_valid = false;
    	    }
    	}; /* fin de OnSubmit */ 
    	
    	

    }]);