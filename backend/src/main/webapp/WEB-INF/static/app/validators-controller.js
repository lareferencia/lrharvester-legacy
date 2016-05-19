var app = angular.module("validatorsApp", [ "ngTable", "schemaForm", "ui.bootstrap", "data.services", "table.services", "rest.url.helper", "validation.json.schemas" ]);

(function() {

	app.controller("validatorsController", mainController);

	mainController.$inject = [ "$scope", "$uibModal",'TableSrv', 'DataSrv', 'RestURLHelper' ];

	function mainController($scope, $uibModal, TableSrv, DataSrv, RestURLHelper) {

		$scope.selectedValidatorURL = null;
		$scope.$watch("selectedValidatorURL", onValidatorSelect, true);

		DataSrv.list(RestURLHelper.validatorURL(), function(validators) {
			$scope.validators = validators.getItems();
		});
		
		
		function onValidatorSelect() {
			
			if ( $scope.selectedValidatorURL != null ) {
				
				DataSrv.get( $scope.selectedValidatorURL, 
						function(validator) {
								// el objeto de red obtenido es ahora el modelo del formulario
								$scope.validator_model = validator;		
								
								// Carga los orígenes
								$scope.validatorRulesTable = TableSrv.createNgTableFromGetData( function(params) { return $scope.validator_model.getLinkItems('rules'); }) ;
								$scope.validatorRulesTableRefreshCallback = function() {  $scope.validatorRulesTable.reload(); };
						}							
				);
				
			}	
		}
		
		
		$scope.openEditValidatorRule = function (isNew, rule, validator, onChangeCallback) {
			
		    var modalInstance = $uibModal.open({
		      animation: true,
		      templateUrl: 'rule-edit-tpl.html',
		      controller: 'ValidationRuleEditCtrl',
		      size: 'lg',
		      resolve: {
		    	  isNew: function() { return isNew; },
			      rule: function () { return rule; },
			      validator: function () { return  validator; }, // se pasa el validador que se está editanto actualmente.
		    	  
		      }
		    });
		
		    modalInstance.result.then( function () { onChangeCallback(); }, function () {});
		   
		}; /* fin openValidatorsList */ 
		

		  /** 
			 * deleteValidatorRule: Borrado de un origin
			 * @param origin a borrar
			 * @param onChangeCallback función que será ejecutada para notificar el cambio al llamador
			 *     
			 ***/
		  $scope.deleteValidatorRule = function  (rule, onChangeCallback) {
			
			   // llamada al borrado
			  rule.remove( function() {
				   
				   alert("removido");
				   // Si es removido exitosamente se recarga la información 
				   onChangeCallback();
			   }
			   ///// ATENCION: FALTA LA LLAMADA AL CALLBACK DE ERROR
			   );   
			   
		  }; /* fin deleteValidatorRule */ 
		

	} /* fin controller */

})();


/**
 * Controlador de edicion de reglas
 * @rule es una regla concreta si estamos editanto isNew == false o es una definicion de regla si estamos creando una regla nueva isNew == true
 * @validator es el objeto validador al cual pertenece la regla
 */
app.controller('ValidationRuleEditCtrl', function ($scope, $uibModalInstance, DataSrv, TableSrv, RestURLHelper,  JSONValidationSchemas, isNew, rule, validator) {
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
	
	$scope.saved = false;
	
	/** schema y formulario de la parte genérica de una regla **/
	$scope.rule_data_schema = JSONValidationSchemas.rule_data_schema;
	$scope.rule_data_form = JSONValidationSchemas.rule_data_form;
	
	// Esta variable contendrá la definición de la regla luego de if siguente
	var rule_definition = null;
	
	if ( !isNew ) { // si no es una regla nueva

		$scope.rule_data_model = rule; // si es una regla a editar tenemos al objeto como parámetro
	
		$scope.rule_model = JSON.parse(rule.jsonserialization); // el modelo de la regla conreta se deserializa del objeto rule campo jsonserialization
		
		rule_definition = JSONValidationSchemas.ruleDefinitionByClassName[ $scope.rule_model["@class"] ]; 
			
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
    		DataSrv.add( RestURLHelper.validatorRuleURL(), $scope.rule_data_model,
    				
    			function(rule) { // callback de creación exitosa
    			 $scope.rule_data_model = rule; // se actualiza el modelo del form con el objeto actualizable
    				
    				 // Agregar el origen a la colecction origins de la network
    				validator.addToCollection('rules',  RestURLHelper.urlFromEntity(rule) , 
    						
    						// Callback agregado exitosa de rule al validator
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

	
}); /* fin de ValidatorsListCtrl */