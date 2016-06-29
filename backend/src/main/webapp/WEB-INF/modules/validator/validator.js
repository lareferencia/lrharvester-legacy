/**
 * @module Item
 * @summary Item module
 */

/*globals window, angular, document */

angular.module('validator', [
     'ngResource',
     'ui.bootstrap',
     'ui.router',
     'schemaForm',
     'table.services',
     'rest.url.helper',
     'data.services',
     'validation.json.schemas'
])


    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
        .state('validator', {
            url: '/validator',
            views: {
                'main@': {
                    templateUrl: 'modules/validator/validator.html'
                }
            }
        })
        .state('validator.new', {
            url: '/new'
        })
        .state('validator.edit', {
            url: '/:validatorID',
        });     
   
    }])
    .controller('validator', ['$rootScope',  '$scope', '$state', '$stateParams', 'TableSrv', 'RestURLHelper', 'DataSrv', 'JSONValidationSchemas', function ($rootScope, $scope, $state, $stateParams, TableSrv, RestURLHelper, DataSrv, JSONValidationSchemas) {
        'use strict';
        
    	$scope.validator_schema = JSONValidationSchemas.validator_schema;
    	$scope.validator_form = JSONValidationSchemas.validator_form;
    	$scope.validator_model = {};
    	
    	$scope.is_validator_new = true;
    	$scope.is_validator_saved = false;
        
        $scope.ruleDefinitionByClassName = JSONValidationSchemas.ruleDefinitionByClassName;
        $scope.is_new =  $state.includes('validator.new');
        
        if ( $stateParams.validatorID != null) {
        	
                $scope.validator_id = Number($stateParams.validatorID);
                $scope.is_validator_new = false;
                
                // obtención de datos,  utilizando el id obtiene la url y luego el servico de datos entrega una network, eso va al form model
            	DataSrv.get( RestURLHelper.validatorURLByID($scope.validator_id), 
            			function(validator_model) {
            					// el objeto de red obtenido es ahora el modelo del formulario
            					$scope.validator_model = validator_model;		
            					
            					// Carga las reglas
            					$scope.validatorRulesTable = TableSrv.createNgTableFromGetData( function(params) { return $scope.validator_model.getLinkItems('rules'); }) ;
            					
            			}							
            	);
 	
        }
        
        
        /***
         *  Callback de refresh de redes  
         **/
        $scope.validatorRulesTableRefreshCallback = function() { 
        	
        	if ( $scope.validatorRulesTable == null ) // Si la tabla es null, la crea
				$scope.validatorRulesTable = TableSrv.createNgTableFromGetData( function(params) { return $scope.validator_model.getLinkItems('rules'); }) ;
		
			$scope.validator_model.reload( function (obj) { $scope.validatorRulesTable.reload(); } );
		};
        
        /**
         * Edit rule
         */
        $scope.editValidatorRule = function(rule) {
        	
        	$scope.is_rule_visible = true;
        	$scope.is_rule_new = false;
        	$scope.is_rule_saved = false;
        	
        	/** schema y formulario de la parte genérica de una regla **/
        	$scope.rule_data_schema = JSONValidationSchemas.rule_data_schema;
        	$scope.rule_data_form = JSONValidationSchemas.rule_data_form;
        	

        	$scope.rule_data_model = rule; // si es una regla a editar tenemos al objeto como parámetro
        	$scope.rule_model = JSON.parse(rule.jsonserialization); // el modelo de la regla conreta se deserializa del objeto rule campo jsonserialization
        		
        	$scope.rule_definition = JSONValidationSchemas.ruleDefinitionByClassName[ $scope.rule_model["@class"] ]; 
   
        	/** schema y formulario de la parte específica de una regla **/
        	$scope.rule_schema = $scope.rule_definition.schema;
        	$scope.rule_form = $scope.rule_definition.form;     	
        };
        
        /**
         * Add new rule
         */
        $scope.addValidatorRule = function(rule) {
        	
        	$scope.is_rule_visible = true;
        	$scope.is_rule_new = true;
        	$scope.is_rule_saved = false;
        	
        	/** schema y formulario de la parte genérica de una regla **/
        	$scope.rule_data_schema = JSONValidationSchemas.rule_data_schema;
        	$scope.rule_data_form = JSONValidationSchemas.rule_data_form;
        		
        	$scope.rule_data_model = {}; // si es una regla nueva no tenemos schema
        	$scope.rule_model = { '@class' : rule.className }; // para una regla nueva no tenemos modelo de regla
        	
        	$scope.rule_definition = rule;
        		
        	/** schema y formulario de la parte específica de una regla **/
        	$scope.rule_schema = $scope.rule_definition.schema;
        	$scope.rule_form = $scope.rule_definition.form;
        	 	
        };
        
        
        /** 
    	 * deleteValidatorRule: Borrado de un origin
    	 ***/
    	  $scope.deleteValidatorRule = function(rule) {
    		  
    		  
    		  if ( confirm("¿Esta seguro que desea borrar esta regla?" ) ) { 
    		  
	    		  $scope.is_rule_visible = false;
	    		  
	    		   // llamada al borrado
	    		  rule.remove( function() {	
	    			  $scope.validatorRulesTableRefreshCallback(); 
	    		  }
	    		   ///// ATENCION: FALTA LA LLAMADA AL CALLBACK DE ERROR
	    		  );   	
    		  }   
    	  }; 
        
        /***
         * Rule forms submit handler
         */
        $scope.on_rule_submit = function(rule_data_form, rule_form) {
			  
		   		 // Se convierte a string el json del formulario de la regla y se actualiza en el model de datos de regla	
		   		$scope.rule_data_model.jsonserialization = JSON.stringify($scope.rule_model);
	
		       	// Si es una regla nueva 
		       	if ( $scope.is_rule_new ) { 
		       		
		       		// Se llama al servicio de add con url de rule y el modelo de regla
		       		DataSrv.add( RestURLHelper.validatorRuleURL(), $scope.rule_data_model,
		       				
		       			function(rule) { // callback de creación exitosa
		       				// se actualiza el modelo del form con el objeto actualizable
		       			 	$scope.rule_data_model = rule; 
		       				
		       				 // Agregar el origen a la colecction origins de la network
		       			 	$scope.validator_model.addToCollection('rules',  RestURLHelper.urlFromEntity(rule), 
		       						// Callback agregado exitosa de rule al validator
		       						function() { 
		       			 				$scope.is_rule_saved = true;
		       			 				$scope.is_rule_new = false;
		       			 				
		       			 				$scope.validatorRulesTableRefreshCallback();
		       			 			}, 
		       		    			onRuleSaveError
		       		    	); /* fin de agregar */
		    	    				
		   				}, // fin callback de add rule 
		   				onRuleSaveError
		       		); // fin de add rule
		       		
		       	} // fin de nueva regla
		       	else { // si es una regla existente
		       	
		       	  // Se graba el modelo en la bd	
		   	      $scope.rule_data_model.update(
		   	    	function() { // success callback
		   	    		$scope.is_rule_saved = true;
		   	    	},
		   	    	onRuleSaveError
		   	      ); // fin de rule_model.update
		   	      
		       	} /* fin del rule ya grabado */ 
		   	     
   	}; /* fin de on rule submit */
     
   	
    /***
     * Validator forms submit handler
     */
    $scope.on_validator_submit = function(validator_form) {
    	
     	// Si es una regla nueva 
       	if ( $scope.is_validator_new ) { 
       		
       		// Se llama al servicio de add con url de validator y el modelo de regla
       		DataSrv.add( RestURLHelper.validatorURL(), $scope.validator_model,
       				
       			function(validator) { // callback de creación exitosa
       				// se actualiza el modelo del form con el objeto actualizable
       			 	$scope.validator_model = validator; 	
       			 	$scope.is_validator_new = false;
   				}, // fin callback de add validator
   				onSaveError
       		); // fin de add validator
       		
       	} // fin de nueva regla
       	else { // si es una regla existente
       	
       	  // Se graba el modelo en la bd	
   	      $scope.validator_model.update(
   	    	function() { // success callback
   	    		$scope.is_validator_saved = true;
   	    	},
   	    	onSaveError
   	      ); // fin de validator_model.update
   	      
       	} /* fin del validator ya grabado */ 
	
    };
    
 	/**
	 * Handler de errores de almacenamiento en la bd
	 */
	function onRuleSaveError(error) { // error callback
	 	$scope.rule_saved = false;
	    $scope.rule_save_error = true;
	    $scope.rule_save_error_message = error.status + ": " + error.statusText;
	};
	
	/**
	 * Handler de errores de almacenamiento en la bd
	 */
	function onSaveError(error) { // error callback
	 	$scope.is_validator_saved = false;
	    $scope.validator_save_error = true;
	    $scope.validator_save_error_message = error.status + ": " + error.statusText;
	};
        
        
 }]);


