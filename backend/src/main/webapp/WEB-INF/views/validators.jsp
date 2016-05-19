<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=urf-8">
    
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>LA Referencia - Validación y transformación </title>

    <!-- Bootstrap Core CSS -->
    <link type="text/css" rel="stylesheet" href="<spring:url value="/static/css/bootstrap.min.css"/>" >
             
    <!-- HighLight CSS -->
    <link type="text/css" rel="stylesheet" href="<spring:url value="/static/css/jquery.highlight.css"/>" >

    <!-- Custom Fonts -->
    <link type="text/css" rel="stylesheet" href="<spring:url value="/static/css/font-awesome.min.css"/>" >
    
    <!-- Ng Table CSS -->
    <link rel="stylesheet" href="<spring:url value="/static/css/ng-table.min.css"/>" >   
    
    <!-- App CSS -->
    <link type="text/css" rel="stylesheet" href="<spring:url value="/static/app/css/styles.css"/>" >
       
    <!-- JQuery JS -->
    <script type="text/javascript" src="<spring:url value="/static/js/jquery-1.12.2.min.js"/>"></script>
        
    <!-- Bootstrap JS -->
    <script type="text/javascript" src="<spring:url value="/static/js/bootstrap.min.js"/>"></script>
    
    <!-- Angular js -->
	<script type="text/javascript" src="<spring:url value="/static/js/angular.min.js"/>"></script>
    <script type="text/javascript" src="<spring:url value="/static/js/angular-sanitize.min.js"/>"></script>
    <script type="text/javascript" src="<spring:url value="/static/js/angular-resource.min.js"/>"></script>
    <script type="text/javascript" src="<spring:url value="/static/js/angular-route.min.js"/>"></script>
    <script type="text/javascript" src="<spring:url value="/static/js/angular-animate.min.js"/>"></script>
    
    <!-- Libs de Angular NG Tables -->
    <script type="text/javascript" src="<spring:url value="/static/js/ng-table.min.js"/>"></script>
    
	<!-- Libs de Angular Spring Data Rest -->
	<script type="text/javascript" src="<spring:url value="/static/js/angular-spring-data-rest.js"/>"></script>
	    
    <!-- Libs Angular de forms -->
    <script type="text/javascript" src="<spring:url value="/static/js/tv4.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/js/ObjectPath.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/js/schema-form.min.js"/>"></script>
    <script type="text/javascript" src="<spring:url value="/static/js/bootstrap-decorator.min.js"/>"></script>
    
    <!-- LoDash -->
    <script type="text/javascript" src="<spring:url value="/static/js/lodash.core.js"/>"></script>
   
    <!-- Angular UI Bootstrap --> 
    <script type="text/javascript" src="<spring:url value="/static/js/ui-bootstrap-tpls-1.2.4.min.js"/>"></script>
   
	<!-- Controladores Angular de la aplicación -->
	<script type="text/javascript" src="<spring:url value="/static/app/validation-json-schemas.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/transformation-json-schemas.js"/>"></script>
	
	<script type="text/javascript" src="<spring:url value="/static/app/table-services.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/rest-url-helper.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/data-services.js"/>"></script>
	
	<script type="text/javascript" src="<spring:url value="/static/app/validators-controller.js"/>" ></script>
	

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body ng-app="validatorsApp" ng-controller="validatorsController as main">

<div id="wrapper">
<div class="row" >
	<div class="col-xs-12">
		<h2 class="page-header">Administración de reglas de validación</h2>
      
      	<div class="row">
      		<div class="col-md-12"> 
			 	<label for="validatorsSelect"> Elija una validador para editar: </label>
			    <select name="validatorsSelect" id="validatorsSelect" ng-model="selectedValidatorURL">
			      <option ng-repeat="validator in validators" value="{{validator._links.self.href}}">{{validator.name}} -- {{validator.description}}</option>
			    </select>
      		</div>
      	</div>
      	
      	<div class="row">
      		<div class="col-xs-12">
      			
			</div>	
      	</div>
      
		<div class="row">
		
			<div class="col-md-6">
				<h5>Reglas</h5>
				<div class="row" >
					<div class="col-xs-12">
			 		<div class="btn-group" uib-dropdown>
						<button id="split-button" type="button" class="btn btn-sm btn-primary">Agregar nueva regla</button>
							<button type="button" class="btn btn-sm btn-primary" uib-dropdown-toggle>
						    	<span class="caret"></span>
						        <span class="sr-only">Split button!</span>
						    </button>
						    <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
						        <li ng-repeat="ruleDefinition in ruleDefinitionByClassName" role="menuitem"><a ng-click="openEditValidatorRule(true, ruleDefinition, validator_model, validatorRulesTableRefreshCallback)" href="">{{ruleDefinition.name}}</a></li>   
						    </ul>
					</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<table ng-table="validatorRulesTable"
							class="table table-bordered table-striped table-condensed">
							<tr ng-repeat="vrule in $data">
								<td data-title="'Nombre'">{{vrule.name}}</td>
								<td data-title="'Descripción'">{{vrule.description}}</td>
								<td data-title="'Mandatoria'">{{vrule.mandatory}}</td>
								<td>
									<button class="btn btn-primary btn-sm"
										ng-click="openEditValidatorRule(false,vrule, validator_model, validatorsRuleTableRefreshCallback)">
										<span class="glyphicon glyphicon-pencil"></span>
									</button>
									<button class="btn btn-danger  btn-sm" ng-click="deleteValidatorRule(vrule,validatorsRuleTableRefreshCallback)">
										<span class="glyphicon glyphicon-trash"></span>	
									</button> 
								</td>
							</tr>
						</table>
					</div>
				</div>

	        </div>
	        <div class="col-md-6">
				<h5>Edición de regla: </h5>

	        </div>
	    
			

		</div> 
		<!-- End Row cabecera -->
	</div>
</div>
</div>


<script type="text/ng-template" id="rule-edit-tpl.html">
<div class="modal-header">
	<h3 class="modal-title">Editando: {{rule_data_model.name}}</h3>
</div>
<div class="modal-body">
	<form name="ruleDataForm" sf-schema="rule_data_schema" sf-form="rule_data_form" sf-model="rule_data_model" ng-submit="onSubmit(ruleDataForm,ruleEditForm)"></form>
	<form name="ruleEditForm" sf-schema="rule_schema" sf-form="rule_form" sf-model="rule_model" ng-submit="onSubmit(ruleDataForm,ruleEditForm)"></form>
	<div ng-if="saved" class="alert alert-success" role="alert">Los datos han sido grabados con éxito</div>

</div>
<div class="modal-footer">
	<button class="btn btn-warning" type="button" ng-click="cancel()">Cerrar</button>
</div>
</script>
</body>

</html>
