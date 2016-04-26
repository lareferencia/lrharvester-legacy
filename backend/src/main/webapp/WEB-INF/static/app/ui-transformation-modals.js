var uvm_module = angular.module('ui.transformation.modals', 
		['ngAnimate', 'ui.bootstrap', 'schemaForm', 'data.services', 'table.services', 'transformation.json.schemas', 'rest.url.helper']);


uvm_module.controller('TransformationController', 
function ($scope, $uibModal, JSONTransformationSchemas) {
	
	
	 $scope.ruleDefinitionByClassName = JSONTransformationSchemas.ruleDefinitionByClassName;
	

	/** 
	 * openTransformerList: Apertura de modal de listado de Validadores
	 *     
	 **/	
	 $scope.openTransformersList = function () {
	
		    var modalInstance = $uibModal.open({
		      animation: true,
		      templateUrl: 'transformers-list-tpl.html',
		      controller: 'TransformersListCtrl',
		      size: 'lg',
		      resolve: {}
	    });
	
	    modalInstance.result.then( function () {}, function () {});
	   
	}; /* fin openTransformersList */ 
	
	$scope.openEditTransformer = function (isNew, transformer, onChangeCallback) {
		
	    var modalInstance = $uibModal.open({
	      animation: true,
	      templateUrl: 'transformer-edit-tpl.html',
	      controller: 'TransformerEditCtrl',
	      size: 'lg',
	      resolve: {
	    	  isNew: function() { return isNew; },
		      transformer: function () { return transformer; },    	  
	      }
	    });
	
	    modalInstance.result.then( function () {}, function () {});
	   
	}; /* fin openTransformersList */ 
	
	$scope.openEditTransformerRule = function (isNew, rule, transformer, onChangeCallback) {
		
	    var modalInstance = $uibModal.open({
	      animation: true,
	      templateUrl: 'rule-edit-tpl.html',
	      controller: 'RuleEditCtrl',
	      size: 'lg',
	      resolve: {
	    	  isNew: function() { return isNew; },
		      rule: function () { return rule; },
		      transformer: function () { return  transformer; }, // se pasa el validador que se está editanto actualmente.
	    	  
	      }
	    });
	
	    modalInstance.result.then( function () { onChangeCallback(); }, function () {});
	   
	}; /* fin openTransformersList */ 
	

	  /** 
		 * deleteTransformerRule: Borrado de un origin
		 * @param origin a borrar
		 * @param onChangeCallback función que será ejecutada para notificar el cambio al llamador
		 *     
		 ***/
	  $scope.deleteTransformerRule = function  (rule, onChangeCallback) {
		
		   // llamada al borrado
		  rule.remove( function() {
			   
			   alert("removido");
			   // Si es removido exitosamente se recarga la información 
			   onChangeCallback();
		   }
		   ///// ATENCION: FALTA LA LLAMADA AL CALLBACK DE ERROR
		   );   
		   
	  }; /* fin deleteTransformerRule */ 

    
    
    
}); /* fin de TransformationController*/


uvm_module.controller('TransformersListCtrl', function ($scope, $uibModalInstance, DataSrv, TableSrv, RestURLHelper,  JSONTransformationSchemas) {
	
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
	
	 $scope.transformersTable = TableSrv.createNgTableFromWsURL( RestURLHelper.transformerURL(), 
			 		function(data) { return data._embedded.transformer;},
			 		function(data) { return data.page.totalElements; },
			 		0, []		    
	);
	
	$scope.transformersTableRefreshCallback = function() {   $scope.transformersTable.reload(); };
	
}); /* fin de TransformersListCtrl */


uvm_module.controller('TransformerEditCtrl', function ($scope, $uibModalInstance, DataSrv, TableSrv, RestURLHelper,  JSONTransformationSchemas, isNew, transformer) {
	
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};

	
	// obtención de datos,  utilizando el id obtiene la url y luego el servico de datos entrega una network, eso va al form model
	DataSrv.get( RestURLHelper.urlFromEntity(transformer), 
			function(transformer_model) {
					// el objeto de red obtenido es ahora el modelo del formulario
					$scope.transformer_model = transformer_model;		
					
					// Carga los orígenes
					$scope.transformerRulesTable = TableSrv.createNgTableFromGetData( function(params) { return $scope.transformer_model.getLinkItems('rules'); }) ;
					$scope.transformerRulesTableRefreshCallback = function() {  $scope.transformerRulesTable.reload(); };
			}							
	);
		
}); /* fin de ValidatoEditCtrl */

/**
 * Controlador de edicion de reglas
 * @rule es una regla concreta si estamos editanto isNew == false o es una definicion de regla si estamos creando una regla nueva isNew == true
 * @transformer es el objeto validador al cual pertenece la regla
 */
uvm_module.controller('RuleEditCtrl', function ($scope, $uibModalInstance, DataSrv, TableSrv, RestURLHelper,  JSONTransformationSchemas, isNew, rule, transformer) {
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
	
	$scope.saved = false;
	
	/** schema y formulario de la parte genérica de una regla **/
	$scope.rule_data_schema = JSONTransformationSchemas.rule_data_schema;
	$scope.rule_data_form = JSONTransformationSchemas.rule_data_form;
	
	// Esta variable contendrá la definición de la regla luego de if siguente
	var rule_definition = null;
	
	if ( !isNew ) { // si no es una regla nueva

		$scope.rule_data_model = rule; // si es una regla a editar tenemos al objeto como parámetro
	
		$scope.rule_model = JSON.parse(rule.jsonserialization); // el modelo de la regla conreta se deserializa del objeto rule campo jsonserialization
		
		rule_definition = JSONTransformationSchemas.ruleDefinitionByClassName[ $scope.rule_model["@class"] ]; 
			
	} else { // si una regla nueva 
		
		$scope.rule_data_model = {}; // si es una regla nueva no tenemos schema
		$scope.rule_model = { '@class' : rule.className }; // para una regla nueva no tenemos modelo de regla
		
		rule_definition = rule; // en este caso el partámetro rule es el objeto que contiene las definiciones de la regla

	} // fin si es una regla nueva
		
	
	/** schema y formulario de la parte específica de una regla **/
	$scope.rule_schema = rule_definition.schema;
	$scope.rule_form = rule_definition.form;
	
	
////////////
	// cuando se presion grabar
	$scope.onSubmit = function(rule_data_form, rule_form) {
			  
	  	
		 // Se convierte a string el json del formulario de la regla y se actualiza en el model de datos de regla	
		$scope.rule_data_model.jsonserialization = JSON.stringify($scope.rule_model);

	
    	// Si es una regla nueva y no fue grabada todavía
    	if ( isNew && !$scope.saved ) { 
    		
    		// Se llama al servicio de add con url de rule y el modelo de regla
    		DataSrv.add( RestURLHelper.transformerRuleURL(), $scope.rule_data_model,
    				
    			function(rule) { // callback de creación exitosa
    			 $scope.rule_data_model = rule; // se actualiza el modelo del form con el objeto actualizable
    				
    				 // Agregar el origen a la colecction origins de la network
    				transformer.addToCollection('rules',  RestURLHelper.urlFromEntity(rule) , 
    						
    						// Callback agregado exitosa de rule al transformer
    						function() { // success callback
    		    	    		$scope.saved = true;
    		    			}, 
    		    			onSaveError
    		    	); /* fin de agregar */
 	    				
				}, // fin callback de add rule 
				onSaveError
    		); // fin de add rule
    		
    		
    		
    	
    	} // fin de nueva regla
    	else { // si es una regla existente
    	
    	 
    	  // Se graba el modelo en la bd	
	      $scope.rule_data_model.update(
	    	function() { // success callback
	    		$scope.saved = true;
	    	},
	    	onSaveError
	      ); // fin de rule_model.update
	      
    	} /* fin del rule ya grabado */ 
	     
	}; /* fin de OnSubmit */
	
	/**
	 * Handler de errores de almacenamiento en la bd
	 */
	function onSaveError(error) { // error callback
	    $scope.save_error = true;
	    $scope.save_error_message = error.status + ": " + error.statusText;
	};
	


	
}); /* fin de TransformersListCtrl */