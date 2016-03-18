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
        
    <!-- Custom CSS -->
    <link href="/static/diagnose/css/sb-admin-2.css" rel="stylesheet">
    
    <!-- Bootstrap -->
    <!--  script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script-->
    
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
	<script type="text/javascript" src="<spring:url value="/static/app/table-services.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/rest-url-helper.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/data-services.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/ui-forms-modals.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/main-controller.js"/>" ></script>
	

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body ng-app="myApp">

<div>
  <div ng-controller="mainController as main">
  
    <h2 class="page-header">Loading data - External array</h2>
    <div class="bs-callout bs-callout-info">
      <h4>Overview</h4>
      <p>Hand <code>NgTableParams</code> a custom <code>getData</code> function that it will call to load data into the table. Typically you will use this option to load server-side data</p>
    </div>
    
    
    
    <table ng-table="networksTable" class="table table-bordered table-striped table-condensed">
      <tr ng-repeat="network in $data track by network.acronym">
      
      	<td width="30" style="text-align: left" header="'ng-table/headers/checkbox.html'">
        	<input type="checkbox" ng-model="networks.selected[network.acronym]" />
        </td>
      	<td data-title="'Acrónimo'"    filter="{acronym: 'text'}" sortable="'name'">{{network.acronym}}</td>
        <td data-title="'Repositorio'" filter="{name: 'text'}" sortable="'name'">{{network.name}}</td>
        <td data-title="'Institución'" filter="{institution: 'text'}" sortable="'institutionName'">{{network.institution}}</td>
        <td data-title="''">
        	<ul>
        		<li>{{network.lstSnapshotStatus}}</li>
        		<li>{{network.lstSize}}</li>
        		<li>{{network.lstTransformedSize}}</li>
        		<li>{{network.lstValidSize}}</li>
        		<li>{{network.lstSnapshotDate}}</li>
        	</ul>
        </td>
        
        <td ng-controller="NetworkActionsController">
         	  <button class="btn btn-primary btn-sm" ng-click="addItem('test')">   <span class="glyphicon glyphicon-ok"></span>		</button>
              <button class="btn btn-primary btn-sm" ng-click="main.cancel(network, networkForm)"> <span class="glyphicon glyphicon-remove"></span> </button>
              <button class="btn btn-primary btn-sm" ng-click="openEditNetwork(network.networkID,networksTableRefreshCallback)"> 	   <span class="glyphicon glyphicon-pencil"></span>	</button>
              <button class="btn btn-danger  btn-sm" ng-click="main.del(network)">			   <span class="glyphicon glyphicon-trash"></span>	</button> 
        </td>
      </tr>
    </table>
    <h2>{{networks.selected}}</h2>
    
    <script type="text/ng-template" id="ng-table/headers/checkbox.html">
        <input type="checkbox" ng-model="networks.areAllSelected" id="select_all" name="filter-checkbox" value="" />
    </script>
    
  </div>
</div>

<!----------------- TEMPLATES ------------------------------ -->

<script type="text/ng-template" id="network-edit-tpl.html">
<div class="modal-header">
	<h3 class="modal-title">Editando: {{network.name}}</h3>
</div>

<div class="modal-body">
	<form name="networkEditForm" sf-schema="schema" sf-form="form" sf-model="model" ng-submit="onSubmit(networkEditForm)" >
	</form>

	<div ng-if="saved" class="alert alert-success" role="alert">Los datos han sido grabados con éxito</div>
	<div ng-if="!is_form_valid" class="alert alert-danger" role="alert">Los datos no son válidos, no se han grabado</div>
	<div ng-if="save_error" class="alert alert-danger" role="alert">No se han podido guardar los datos - {{save_error_message}}</div>

</div>

<div class="modal-footer">
	<button class="btn btn-success" type="button" ng-click="ok()">Cerrar</button>
	<button class="btn btn-warning" type="button" ng-click="cancel()">Cerrar sin guardar</button>

</div>
</script>

<!----------------- FIN TEMPLATES -------------------------------->

	
</body>

</html>
