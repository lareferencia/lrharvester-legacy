/**
 * @module Item
 * @summary Item module
 */

/*globals window, angular, document */

angular.module('transformer', [
     'ngResource',
     'ui.bootstrap',
     'ui.router',
     'schemaForm',
     'table.services',
     'rest.url.helper',
     'data.services',
     'transformation.json.schemas'
])


    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
        .state('transformer', {
            url: '/transformer',
            views: {
                'main@': {
                    templateUrl: 'modules/transformer/transformer.html'
                }
            }
        })
        .state('transformer.new', {
            url: '/new'
        })
        .state('transformer.edit', {
            url: '/:transformerID',
        });     
   
    }])
    .controller('transformer', ['$rootScope',  '$scope', '$state', '$stateParams', 'TableSrv', 'RestURLHelper', 'DataSrv', 'JSONTransformationSchemas', function ($rootScope, $scope, $state, $stateParams, TableSrv, RestURLHelper, DataSrv, JSONTransformationSchemas) {
        'use strict';
        
    	$scope.transformer_schema = JSONTransformationSchemas.transformer_schema;
    	$scope.transformer_form = JSONTransformationSchemas.transformer_form;
    	$scope.transformer_model = {};
    	
    	$scope.is_transformer_new = true;
    	$scope.is_transformer_saved = false;
        
        $scope.ruleDefinitionByClassName = JSONTransformationSchemas.ruleDefinitionByClassName;
        $scope.is_new =  $state.includes('transformer.new');
        
        if ( $stateParams.transformerID != null) {
        	
                $scope.transformer_id = Number($stateParams.transformerID);
                $scope.is_transformer_new = false;
                
                // obtención de datos,  utilizando el id obtiene la url y luego el servico de datos entrega una network, eso va al form model
            	DataSrv.get( RestURLHelper.transformerURLByID($scope.transformer_id), 
            			function(transformer_model) {
            					// el objeto de red obtenido es ahora el modelo del formulario
            					$scope.transformer_model = transformer_model;		
            					
            					// Carga las reglas
            					$scope.transformerRulesTable = TableSrv.createNgTableFromGetData( function(params) { return $scope.transformer_model.getLinkItems('rules'); }) ;
            					
            			}							
            	);
 	
        }
        
        
        /***
         *  Callback de refresh de redes  
         **/
        $scope.transformerRulesTableRefreshCallback = function() { 
        	
        	if ( $scope.transformerRulesTable == null ) // Si la tabla es null, la crea
				$scope.transformerRulesTable = TableSrv.createNgTableFromGetData( function(params) { return $scope.transformer_model.getLinkItems('rules'); }) ;
		
			$scope.transformer_model.reload( function (obj) { $scope.transformerRulesTable.reload(); } );
		};
        
        /**
         * Edit rule
         */
        $scope.editTransformerRule = function(rule) {
        	
        	$scope.is_rule_visible = true;
        	$scope.is_rule_new = false;
        	$scope.is_rule_saved = false;
        	
        	/** schema y formulario de la parte genérica de una regla **/
        	$scope.rule_data_schema = JSONTransformationSchemas.rule_data_schema;
        	$scope.rule_data_form = JSONTransformationSchemas.rule_data_form;
        	

        	$scope.rule_data_model = rule; // si es una regla a editar tenemos al objeto como parámetro
        	$scope.rule_model = JSON.parse(rule.jsonserialization); // el modelo de la regla conreta se deserializa del objeto rule campo jsonserialization
        		
        	$scope.rule_definition = JSONTransformationSchemas.ruleDefinitionByClassName[ $scope.rule_model["@class"] ]; 
   
        	/** schema y formulario de la parte específica de una regla **/
        	$scope.rule_schema = $scope.rule_definition.schema;
        	$scope.rule_form = $scope.rule_definition.form;     	
        };
        
        /**
         * Add new rule
         */
        $scope.addTransformationRule = function(rule) {
        	
        	$scope.is_rule_visible = true;
        	$scope.is_rule_new = true;
        	$scope.is_rule_saved = false;
        	
        	/** schema y formulario de la parte genérica de una regla **/
        	$scope.rule_data_schema = JSONTransformationSchemas.rule_data_schema;
        	$scope.rule_data_form = JSONTransformationSchemas.rule_data_form;
        		
        	$scope.rule_data_model = {}; // si es una regla nueva no tenemos schema
        	$scope.rule_model = { '@class' : rule.className }; // para una regla nueva no tenemos modelo de regla
        	
        	$scope.rule_definition = rule;
        		
        	/** schema y formulario de la parte específica de una regla **/
        	$scope.rule_schema = $scope.rule_definition.schema;
        	$scope.rule_form = $scope.rule_definition.form;
        	 	
        };
        
        
        /** 
    	 * deleteTransformerRule: Borrado de un origin
    	 ***/
    	  $scope.deleteTransformerRule = function(rule) {
    		  
    		  if ( confirm("¿Esta seguro que desea borrar esta regla?" ) ) { 
    		  
	    		  $scope.is_rule_visible = false;
	    		  
	    		   // llamada al borrado
	    		  rule.remove( function() {	
	    			  $scope.transformerRulesTableRefreshCallback(); 
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
		       		DataSrv.add( RestURLHelper.transformerRuleURL(), $scope.rule_data_model,
		       				
		       			function(rule) { // callback de creación exitosa
		       				// se actualiza el modelo del form con el objeto actualizable
		       			 	$scope.rule_data_model = rule; 
		       				
		       				 // Agregar el origen a la colecction origins de la network
		       			 	$scope.transformer_model.addToCollection('rules',  RestURLHelper.urlFromEntity(rule), 
		       						// Callback agregado exitosa de rule al transformer
		       						function() { 
		       			 				$scope.is_rule_saved = true;
		       			 				$scope.is_rule_new = false;
		       			 				
		       			 				$scope.transformerRulesTableRefreshCallback();
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
     * Transformer forms submit handler
     */
    $scope.on_transformer_submit = function(transformer_form) {
    	
     	// Si es una regla nueva 
       	if ( $scope.is_transformer_new ) { 
       		
       		// Se llama al servicio de add con url de transformer y el modelo de regla
       		DataSrv.add( RestURLHelper.transformerURL(), $scope.transformer_model,
       				
       			function(transformer) { // callback de creación exitosa
       				// se actualiza el modelo del form con el objeto actualizable
       			 	$scope.transformer_model = transformer; 	
       			 	$scope.is_transformer_new = false;
   				}, // fin callback de add transformer
   				onSaveError
       		); // fin de add transformer
       		
       	} // fin de nueva regla
       	else { // si es una regla existente
       	
       	  // Se graba el modelo en la bd	
   	      $scope.transformer_model.update(
   	    	function() { // success callback
   	    		$scope.is_transformer_saved = true;
   	    	},
   	    	onSaveError
   	      ); // fin de transformer_model.update
   	      
       	} /* fin del transformer ya grabado */ 
	
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
	 	$scope.is_transformer_saved = false;
	    $scope.transformer_save_error = true;
	    $scope.transformer_save_error_message = error.status + ": " + error.statusText;
	};
        
        
 }]);


