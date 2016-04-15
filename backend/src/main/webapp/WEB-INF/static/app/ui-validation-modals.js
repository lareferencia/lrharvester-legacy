var uvm_module = angular.module('ui.validation.modals', 
		['ngAnimate', 'ui.bootstrap', 'schemaForm', 'data.services', 'table.services', 'validation.json.schemas', 'rest.url.helper']);


uvm_module.controller('ValidationController', 
function ($scope, $uibModal) {

	/** 
	 * openValidatorList: Apertura de modal de listado de Validadores
	 *     
	 **/	
	 $scope.openValidatorsList = function () {
	
		    var modalInstance = $uibModal.open({
		      animation: true,
		      templateUrl: 'validators-list-tpl.html',
		      controller: 'ValidatorsListCtrl',
		      size: 'lg',
		      resolve: {}
	    });
	
	    modalInstance.result.then( function () {}, function () {});
	   
	}; /* fin openValidatorsList */ 
	
	$scope.openEditValidator = function (isNew, validator, onChangeCallback) {
	
	    var modalInstance = $uibModal.open({
	      animation: true,
	      templateUrl: 'validator-edit-tpl.html',
	      controller: 'ValidatorEditCtrl',
	      size: 'lg',
	      resolve: {
	    	  isNew: function() { return isNew; },
		      validator: function () { return validator; },
	    	  
	      }
	    });
	
	    modalInstance.result.then( function () {}, function () {});
	   
	}; /* fin openValidatorsList */ 
	
	$scope.openEditValidatorRule = function (isNew, rule, onChangeCallback) {
		
	    var modalInstance = $uibModal.open({
	      animation: true,
	      templateUrl: 'rule-edit-tpl.html',
	      controller: 'RuleEditCtrl',
	      size: 'lg',
	      resolve: {
	    	  isNew: function() { return isNew; },
		      rule: function () { return rule; },
	    	  
	      }
	    });
	
	    modalInstance.result.then( function () {}, function () {});
	   
	}; /* fin openValidatorsList */ 

    
    
    
}); /* fin de ValidationController*/


uvm_module.controller('ValidatorsListCtrl', function ($scope, $uibModalInstance, DataSrv, TableSrv, RestURLHelper,  JSONValidationSchemas) {
	
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
	
	 $scope.validatorsTable = TableSrv.createNgTableFromWsURL( RestURLHelper.validatorURL(), 
			 		function(data) { return data._embedded.validator;},
			 		function(data) { return data.page.totalElements; },
			 		0, []		    
	);
	
	$scope.validatorsTableRefreshCallback = function() {   $scope.validatorsTable.reload(); };
	
}); /* fin de ValidatorsListCtrl */


uvm_module.controller('ValidatorEditCtrl', function ($scope, $uibModalInstance, DataSrv, TableSrv, RestURLHelper,  JSONValidationSchemas, isNew, validator) {
	
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
	
	// obtención de datos,  utilizando el id obtiene la url y luego el servico de datos entrega una network, eso va al form model
	DataSrv.get( RestURLHelper.urlFromEntity(validator), 
			function(validator_model) {
					// el objeto de red obtenido es ahora el modelo del formulario
					$scope.validator_model = validator_model;		
					
					// Carga los orígenes
					$scope.validatorRulesTable = TableSrv.createNgTableFromGetData( function(params) { return $scope.validator_model.getLinkItems('rules'); }) ;
					$scope.validatorRulesTableRefreshCallback = function() {  $scope.validatorRulesTable.reload(); };
			}							
	);
	
	$scope.validatorRulesTableRefreshCallback = function() {   $scope.validatorRulesTable.reload(); };
	
}); /* fin de ValidatoEditCtrl */


uvm_module.controller('RuleEditCtrl', function ($scope, $uibModalInstance, DataSrv, TableSrv, RestURLHelper,  JSONValidationSchemas, isNew, rule) {
	
	// Accciones de los botones del modal
	$scope.ok = function () { $uibModalInstance.close(null); };
	$scope.cancel = function () { $uibModalInstance.dismiss('cancel');};
	
	$scope.saved = false;
		
	$scope.rule_data_model = rule;
	$scope.rule_data_schema = JSONValidationSchemas.rule_data_schema;
	$scope.rule_data_form = JSONValidationSchemas.rule_data_form;
	
	$scope.rule_model = JSON.parse(rule.jsonserialization);
	var rule_form = JSONValidationSchemas.getRuleByClass( $scope.rule_model["@class"] );
	$scope.rule_schema = rule_form.schema;
	$scope.rule_form = rule_form.form;
	
////////////
	// cuando se presion grabar
	$scope.onSubmit = function(rule_data_form, rule_form) {
			  
	  	
	    	// Si es una regla nueva y no fue grabada todavía
	    	if ( isNew && !$scope.saved ) { 
	    		
	    		
	    		
	    	
	    	} // fin de nueva red
	    	else { // si es una regla existente
	    	
	    	  // Se convierte a string el json del formulario de la regla y se actualiza en el model de datos de regla	
	    	  $scope.rule_data_model.jsonserialization = JSON.stringify($scope.rule_model)

	    	  // Se graba el modelo en la bd	
		      $scope.rule_data_model.update(
		    	function() { // success callback
		    		
		    		$scope.saved = true;
		    	},
		    	onSaveError
		      ); // fin de origin_model.update
		      
	    	} /* fin del origen ya grabado */ 
	     
	}; /* fin de OnSubmit */
	
	/**
	 * Handler de errores de almacenamiento en la bd
	 */
	function onSaveError(error) { // error callback
	    $scope.save_error = true;
	    $scope.save_error_message = error.status + ": " + error.statusText;
	};
	


	
}); /* fin de ValidatorsListCtrl */