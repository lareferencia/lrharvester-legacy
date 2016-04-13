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

    <title>LA Referencia - Administración</title>

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
	<script type="text/javascript" src="<spring:url value="/static/app/model-json-schemas.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/validation-json-schemas.js"/>"></script>
	
	<script type="text/javascript" src="<spring:url value="/static/app/table-services.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/rest-url-helper.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/data-services.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/oai-services.js"/>"></script>	
	
	<script type="text/javascript" src="<spring:url value="/static/app/ui-forms-modals.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/ui-validation-modals.js"/>"></script>
	
	
	<script type="text/javascript" src="<spring:url value="/static/app/main-controller.js"/>" ></script>
	

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body ng-app="myApp" ng-controller="mainController as main">

<div id="wrapper">
<div class="row" >
	<div class="col-xs-12">
		<h2 class="page-header">Administración de cosecha, transformación e indexación</h2>
      
		<div class="row">
		
			<div ng-controller="NetworkActionsController" class="col-md-6">
				<h5>Acciones colectivas</h5>
				<p>Puede ejecutar estas acciones sobre un grupo de repositorios seleccionados utilizando los checkbox de la tabla.</p>
		
	          	<button type="button" class="btn btn-primary btn-sm" ng-click="openEditNetwork(true, null, networksTableRefreshCallback)">
	          		<span class="glyphicon glyphicon-pencil"></span>
	          		Crear una nueva red
	          	</button>
		    
			    <div class="btn-group" uib-dropdown>
			      <button id="split-button" type="button" class="btn btn-sm btn-success">Acciones</button>
			      <button type="button" class="btn btn-sm btn-success" uib-dropdown-toggle>
			        <span class="caret"></span>
			        <span class="sr-only">Split button!</span>
			      </button>
			      <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
			        <li role="menuitem"><a href="#">Cosechar</a></li>
			        <li role="menuitem"><a href="#">Indexar Vufind</a></li>
			        <li role="menuitem"><a href="#">Indexar XOAI</a></li>
			        <li class="divider"></li>
			        <li role="menuitem"><a href="#">Retirar de Vufind</a></li>
			        <li role="menuitem"><a href="#">Retirar de XOAI</a></li>
			        <li class="divider"></li>
			        <li role="menuitem"><a href="#">Borrar</a></li>  	        
			      </ul>
			    </div>
	        </div>
			<div ng-controller="ValidationController" class="col-md-6">
			
				<h5>Edición de Validadores y Tranformadores</h5>
				<p></p>
		
	          	<button type="button" class="btn btn-primary btn-sm" ng-click="openValidatorsList()">
	          		<span class="glyphicon glyphicon-pencil"></span>
	          		Validadores
	          	</button>
	          	<button type="button" class="btn btn-primary btn-sm" ng-click="openEditNetwork(true, null, networksTableRefreshCallback)">
	          		<span class="glyphicon glyphicon-pencil"></span>
	          		Transformadores
	          	</button>
				
			</div>

		</div> 
		<!-- End Row cabecera -->
        <div class="row"><p></p></div>
		<div class="row">
	      	<div class="col-xs-12">
				<table ng-table="networksTable" class="table table-bordered table-striped table-condensed">
					<tr ng-repeat="network in $data track by network.acronym">
						<td width="30" style="text-align: left" header="'ng-table/headers/checkbox.html'">
					    	<input type="checkbox" ng-model="networks.selected[network.acronym]" />
					    </td>
					  	<td data-title="'Acrónimo'"    filter="{acronym: 'text'}" sortable="'acronym'">{{network.acronym}}</td>
					    <td data-title="'Repositorio'" filter="{name: 'text'}" sortable="'name'">{{network.name}}</td>
					    <td data-title="'Institución'" filter="{institution: 'text'}" sortable="'institutionName'">{{network.institution}}</td>
					    <td data-title="'Última cosecha'">
	                                <h5>{{network.lstSnapshotStatus}}</h5>
	                                <div class="small">{{network.lstSize}} | V: {{network.lstValidSize}} | T: {{network.lstTransformedSize}} </div>
	                                <div class="small"><i class="fa fa-clock-o"> </i> <span class="c-white">{{network.lstSnapshotDate}}</span>  </div>
					    </td>
					    <td data-title="'Última cosecha exitosa'">
	                                <h5>ÚLTIMO VÁLIDO</h5>
	                                <div class="small">{{network.lgkSize}} | V: {{network.lgkValidSize}} | T: {{network.lgkTransformedSize}} </div>
	                                <div class="small"><i class="fa fa-clock-o"> </i> <span class="c-white">{{network.lgkSnapshotDate}}</span>  </div>
					    </td>
					    
					    <td ng-controller="NetworkActionsController">
					    	<p>
								<div class="btn-group" uib-dropdown>
								      <button id="split-button" type="button" class="btn btn-sm btn-success">Acciones</button>
								      <button type="button" class="btn btn-sm btn-success" uib-dropdown-toggle>
								        <span class="caret"></span>
								        <span class="sr-only">Split button!</span>
								      </button>
								      <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
								        <li role="menuitem"><a href="#">Cosechar</a></li>
								        <li role="menuitem"><a href="#">Detener cosecha </a></li>
								       	<li class="divider"></li>
								        <li role="menuitem"><a href="#">Indexar Vufind</a></li>
								        <li role="menuitem"><a href="#">Indexar XOAI</a></li>
								        <li class="divider"></li>
								        <li role="menuitem"><a href="#">Retirar de Vufind</a></li>
								        <li role="menuitem"><a href="#">Retirar de XOAI</a></li>
								        <li class="divider"></li>
								      </ul>
								</div>
							</p>
							<p>
						        <button class="btn btn-primary btn-sm" ng-click="openEditNetwork(false,network.networkID,networksTableRefreshCallback)"> 	   
						        	<span class="glyphicon glyphicon-pencil"></span>	
						        </button>
						        <button class="btn btn-danger  btn-sm" ng-click="main.del(network)">
						        	<span class="glyphicon glyphicon-trash"></span>	
						        </button> 
						   	</p>        
					    </td>
				  </tr>
				</table>
	      	</div>    
  		</div>
	</div>
</div>
</div>
<!----------------- TEMPLATES ------------------------------ -->

<script type="text/ng-template" id="network-edit-tpl.html">
<div class="modal-header">
	<h3 class="modal-title">Editando: {{network.name}}</h3>
</div>

<div class="modal-body">

	<!-- TABSET -->
 	<uib-tabset active="activeJustified" justified="true">
    	<uib-tab index="0" heading="Principal">
			<form name="networkEditForm" sf-schema="network_schema" sf-form="network_form" sf-model="network_model" ng-submit="onSubmit(networkEditForm)" ></form>
			<div ng-if="saved" class="alert alert-success" role="alert">Los datos han sido grabados con éxito</div>
			<div ng-if="!is_form_valid" class="alert alert-danger" role="alert">Los datos no son válidos, no se han grabado</div>
			<div ng-if="save_error" class="alert alert-danger" role="alert">No se han podido guardar los datos - {{save_error_message}}</div>
		</uib-tab>
    	<uib-tab index="1" heading="Propiedades">
			<form name="networkPropertiesEditForm" sf-schema="np_schema" sf-form="np_form" sf-model="np_model" ng-submit="onSubmit(networkEditForm)" ></form>
		</uib-tab>
    	<uib-tab index="2" heading="Orígenes">
				<div ng-controller="OriginActionsController">
				<div class="row" >
					<div class="col-xs-12">
	          			<button type="button" class="btn btn-default btn-sm" ng-click="openEditOrigin(true, network_model, originsTableRefreshCallback)">Nuevo Origen</button>
   					</div>    
  				</div>
				<div class="row" >
					<div class="col-xs-12">
						<table ng-table="originsTable" class="table table-bordered table-striped table-condensed">
							<tr ng-repeat="origin in $data track by origin.name">
					
					  			<td data-title="'Nombre'">{{origin.name}}</td>
					    		<td data-title="'URI'">{{origin.uri}}</td>
					    		<td data-title="'MetadataPrefix'">{{origin.metadataPrefix}}</td>
					   	    
					    		<td>
					          		<button class="btn btn-primary btn-sm" ng-click="openEditOrigin(false,origin,originsTableRefreshCallback)"> 	   
										<span class="glyphicon glyphicon-pencil"></span>	
									</button>
					          		<button class="btn btn-danger  btn-sm" ng-click="deleteOrigin(network_model,origin,originsTableRefreshCallback)">
										<span class="glyphicon glyphicon-trash"></span>	
									</button> 
					    		</td>
				  			</tr>
						</table>
					</div>    
  				</div>
				</div>
		</uib-tab>
	</uib-tabset>

</div>

<div class="modal-footer">
	<button class="btn btn-success" type="button" ng-click="onSubmit(networkEditForm)">Grabar</button>
	<button class="btn btn-warning" type="button" ng-click="cancel()">Cerrar</button>
</div>
</script>

<script type="text/ng-template" id="origin-edit-tpl.html">
<div class="modal-header">
	<h3 class="modal-title">Editando: {{origin_model.name}}</h3>
</div>
<div class="modal-body">

	<form name="originEditForm" sf-schema="origin_schema" sf-form="origin_form" sf-model="origin_model" ng-submit="onSubmit(originEditForm)"></form>
	<div ng-if="saved" class="alert alert-success" role="alert">Los datos han sido grabados con éxito</div>
	<div ng-if="!is_form_valid" class="alert alert-danger" role="alert">Los datos no son válidos, no se han grabado</div>
	<div ng-if="save_error" class="alert alert-danger" role="alert">No se han podido guardar los datos - {{save_error_message}}</div>

</div>
<div class="modal-footer">
	<button class="btn btn-success" type="button" ng-click="onSubmit(originEditForm)">Grabar</button>
	<button class="btn btn-warning" type="button" ng-click="cancel()">Cerrar</button>
</div>
</script>


<script type="text/ng-template" id="validators-list-tpl.html">
<div class="modal-header">
	<h3 class="modal-title">Listado de validadores</h3>
</div>
<div class="modal-body" ng-controller="ValidationController">

		<div class="row">
			<div class="col-xs-12">
				<button type="button" class="btn btn-default btn-sm"
					ng-click="openEditOrigin(true, network_model, originsTableRefreshCallback)">Agregar
					Validador</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<table ng-table="validatorsTable"
					class="table table-bordered table-striped table-condensed">
					<tr ng-repeat="validator in $data track by validator.name">

						<td data-title="'Nombre'">{{validator.name}}</td>
						<td data-title="'Descripción'">{{validator.description}}</td>
						<td>
					        <button class="btn btn-primary btn-sm" ng-click="openEditValidator(false,validator,validatorsTableRefreshCallback)"> 	   
								<span class="glyphicon glyphicon-pencil"></span>	
							</button>
						</td>

					</tr>
				</table>
			</div>
		</div>
</div>
<div class="modal-footer">
	<button class="btn btn-warning" type="button" ng-click="cancel()">Cerrar</button>
</div>
</script>

<script type="text/ng-template" id="validator-edit-tpl.html">
<div class="modal-header">
	<h3 class="modal-title">Editando: {{validator_model.name}}</h3>
</div>
<div class="modal-body" ng-controller="ValidationController">
	<table ng-table="validatorRulesTable"
		class="table table-bordered table-striped table-condensed">
			<tr ng-repeat="vrule in $data track by vrule.name">
				<td data-title="'Nombre'">{{vrule.name}}</td>
				<td data-title="'Descripción'">{{vrule.description}}</td>
				<td>
					<button class="btn btn-primary btn-sm" ng-click="openEditValidatorRule(false,vrule,validatorRulesTableRefreshCallback)"> 	   
						<span class="glyphicon glyphicon-pencil"></span>	
					</button>
				</td>
			</tr>
	</table>
</div>
<div class="modal-footer">
	<button class="btn btn-warning" type="button" ng-click="cancel()">Cerrar</button>
</div>
</script>

<script type="text/ng-template" id="rule-edit-tpl.html">
<div class="modal-header">
	<h3 class="modal-title">Editando: {{rule_model.name}}</h3>
</div>
<div class="modal-body">
	<form name="ruleEditForm" sf-schema="rule_schema" sf-form="rule_form" sf-model="rule_model" ng-submit="onSubmit(ruleEditForm)"></form>
</div>
<div class="modal-footer">
	<button class="btn btn-warning" type="button" ng-click="cancel()">Cerrar</button>
</div>
</script>


<script type="text/ng-template" id="ng-table/headers/checkbox.html">
<input type="checkbox" ng-model="networks.areAllSelected" id="select_all" name="filter-checkbox" value="" />
</script>



<!----------------- FIN TEMPLATES -------------------------------->

	
</body>

</html>
