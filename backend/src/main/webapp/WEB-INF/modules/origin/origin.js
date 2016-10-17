/**
 * @module Items
 * @summary Items module
 */

/*globals window, angular, document */

angular.module('origin', [
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
            .state('origin', {
                url: '/origin',
                views: {
                    'main@': {
                        templateUrl: 'modules/origin/origin.html'
                    }
                }
            })
            .state('origin.new', {
                url: '/:networkID/new'
            })
            .state('origin.edit', {
                url: '/:networkID/:originID',
            });
    }])

    .controller('origin', ['$scope', '$stateParams', '$filter', 'TableSrv', 'RestURLHelper', 'DataSrv', 'JSONNetworkSchemas', function ($scope, $stateParams, $filter, TableSrv, RestURLHelper, DataSrv, JSONNetworkSchemas) {
       
    	'use strict';
    	
    	$scope.is_new = true;
   
    	$scope.network_id = $stateParams.networkID;
    	
    	    	
    	if ($stateParams.originID != null) {
            $scope.origin_id = Number($stateParams.originID);
            $scope.is_new = false;
    	}
    	
    	// Indica si el formulario es válido, en principio se da por válido
    	// TODO: Creo que esto hay que manejarlo con más elegancia
    	$scope.is_form_valid = true;
    	
    	// schema del formulario de origen
    	$scope.origin_schema = JSONNetworkSchemas.origin_schema;
    	// estructura del formulario de origen
    	$scope.origin_form = JSONNetworkSchemas.origin_form;
    	// datos formulario de origen de datos
    	$scope.origin_model = {};


    	
    	if ( $scope.is_new) { // si es un origin nuevo 
    		$scope.origin_model = {};
    		$scope.origin_model.form_sets = [];
    		
        	DataSrv.get( RestURLHelper.networkURLByID($scope.network_id), function(network) { 
        		$scope.network_model = network;
        	});
        	
    	} else { // si es una origen existente
    		
    		DataSrv.get( RestURLHelper.originURLByID($scope.origin_id), function(origin) { 
        		$scope.origin_model = origin;	
        		
        		$scope.origin_model.form_sets = angular.copy( $scope.origin_model.getLinkItems("sets") );

        	});
    		
    	} 
    	
    	
    	////////////
    	// cuando se presion grabar
    	$scope.onSubmit = function(form) {
    			  
    	  	// Se valida el formulario
    	    $scope.$broadcast('schemaFormValidate');

    	    // Si resulta válido
    	    if (form.$valid) {
    	    	
    	    	// Si es un origin nueva y no fue grabada todavía
    	    	if ( $scope.is_new ) { 
    	    
    	    		var sets = $scope.origin_model.form_sets;
    	    		
    	    		// Se llama al servicio de add con url de orign y el modelo json del form
    	    		DataSrv.add( RestURLHelper.originURL(), $scope.origin_model,
    	    				
    	    			function(origin) { // callback de creación exitosa de origin
    	    			
    	    		 		$scope.saved = true;
    	    		 		$scope.is_new = false; // ya no es una red nueva
    	    			
    	    				$scope.origin_model = origin; // se actualiza el modelo del form con el objeto actualizable
    	    				$scope.origin_model.form_sets = sets;
    	    				
    	    				// Para cada set en el formulario 
		  		    		angular.forEach(sets, function(set, i) {
		  		    			
		  		    			if (set.name != null && set.spec != null) {
		  		    			
			  		    			// Se crea un objeto set en la bd
			  		    			DataSrv.add( RestURLHelper.setURL(), set, function(set_model) {
			  		    				
			  		    				// Si la creación es exitosa se agrega a la colección de sets del origin
			  		    				$scope.origin_model.addToCollection('sets',  RestURLHelper.urlFromEntity(set_model),
			  		    						// Callback agregado exitosa del set al origin
			      	    						function() { /* success callback */ }, 
			      	    		    			onSaveError
			      	    		    	); /* fin de addToCollection*/
			  		    				
			  		    			}, onSaveError);
		  		    			}
		  		    		}); 
					
    	    				
    	    				 // Agregar el origen a la colecction origins de la network
    	    				$scope.network_model.addToCollection('origins',  RestURLHelper.urlFromEntity($scope.origin_model) , 
    	    						// Callback agregado exitosa de origin a origins de network
    	    						function() {}, 
    	    		    			onSaveError
    	    		    	); /* fin de associate*/
    	 	    				
        				}, // fin callback de add origin 
        				onSaveError
    	    		); // fin de add origin  	
    	    	} // fin de nuevo origen
    	    	else { // si es un origen ya grabada entonces se llama a la función update 
    	    	
    	    		
    	    		 // Se graba el modelo	
	    		      $scope.origin_model.update( function() {
		    		      
			    		  	$scope.origin_model.original_sets =  angular.copy( $scope.origin_model.getLinkItems("sets") );	
			            	  
		            	    // se desvinculan todos los sets 
		            	  	$scope.origin_model.unbindCollection('sets', function( model ) {
		  		    		
						    		// Borra los sets antiguos
						    		angular.forEach($scope.origin_model.original_sets, function(set, i) {
						    			set.remove( function() {}, onSaveError);
						    		}); 
				  		    		
				  		    	
				  		    		// Para cada set en el formulario 
				  		    		angular.forEach($scope.origin_model.form_sets, function(set, i) {
				  	 	    			
				  		    			// Se crea un objeto set en la bd
				  		    			DataSrv.add( RestURLHelper.setURL(), set, function(set_model) {
				  		    				
				  		    				// Si la creación es exitosa se agrega a la colección de sets del origin
				  		    				$scope.origin_model.addToCollection('sets',  RestURLHelper.urlFromEntity(set_model),
				  		    						// Callback agregado exitosa del set al origin
				      	    						function() { /* success callback */ }, 
				      	    		    			onSaveError
				      	    		    	); /* fin de addToCollection*/
				  		    				
				  		    			}, onSaveError);
				  		    		}); 
				  		    		
				  		    		//
				  		    		$scope.saved = true; 
		    		      
	    		      },
	    		    	onSaveError
	    		      ); // fin de origin_model.update
  		
    	    	
		
    	    		}); // fin de callback reload
    	    	} /* fin del origen ya grabado */
    	      
    	    } else { // si no es valido se avisa a la interfaz
    	    	$scope.is_form_valid = false;
    	    }
    	}; /* fin de OnSubmit */
    	
    	/**
    	 * Handler de errores de almacenamiento en la bd
    	 */
    	function onSaveError(error) { // error callback
		 	$scope.saved = false;
    	    $scope.save_error = true;
    	    $scope.save_error_message = error.status + ": " + error.statusText;
    	};
    	
    	
    	
    

    }]);