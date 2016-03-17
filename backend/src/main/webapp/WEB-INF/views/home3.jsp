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
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
           
    <!-- HighLight CSS -->
    <link rel="stylesheet" href="<spring:url value="/static/diagnose/css/jquery.highlight.css"/>" >

    <!-- Custom Fonts -->
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    
    <!-- Ng Table CSS -->
    <link rel="stylesheet" href="https://rawgit.com/esvit/ng-table/master/dist/ng-table.min.css">
        
    <!-- Custom CSS -->
    <link href="/static/diagnose/css/sb-admin-2.css" rel="stylesheet">
    
    <!-- Bootstrap -->
    <!--  script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script-->
    
    <!-- Angular js -->
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular-sanitize.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular-resource.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular-route.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular-animate.js"></script>
    
    <script src="https://rawgit.com/esvit/ng-table/master/dist/ng-table.min.js"></script>
    
	<!-- Libs de Angular Spring Data Rest -->
	<script type="text/javascript" src="<spring:url value="/static/app/js/angular-spring-data-rest.js"/>"></script>
	    
    
    <!-- Libs de forms -->
    <script type="text/javascript" src="<spring:url value="static/app/js/tv4.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="static/app/js/ObjectPath.js"/>"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-schema-form/0.8.12/schema-form.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-schema-form/0.8.12/bootstrap-decorator.min.js"></script>
    
    
    <!-- LoDash -->
    <script type="text/javascript" src="https://cdn.jsdelivr.net/lodash/4.6.1/lodash.min.js"></script>
   
    <!-- Angular UI Bootstrap --> 
    <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/1.2.4/ui-bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/1.2.4/ui-bootstrap-tpls.min.js"></script>
    
	
	<!-- Controladores Angular -->
	<script type="text/javascript" src="<spring:url value="/static/app/js/model-json-schemas.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/js/table-services.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/js/data-services.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/js/modal.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/app/js/controller.js"/>" ></script>
	

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>


<div ng-app="myApp">


	

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
              <button class="btn btn-primary btn-sm" ng-click="openEditNetwork(network)"> 	   <span class="glyphicon glyphicon-pencil"></span>	</button>
              <button class="btn btn-danger  btn-sm" ng-click="main.del(network)">			   <span class="glyphicon glyphicon-trash"></span>	</button> 
        </td>
      </tr>
    </table>
    <h2>{{networks.selected}}</h2>
    
    <script type="text/ng-template" id="ng-table/headers/checkbox.html">
        <input type="checkbox" ng-model="networks.areAllSelected" id="select_all" name="filter-checkbox" value="" />
    </script>
    
    <script type="text/ng-template" id="networkEditModal.html">
        
		<div class="modal-header">
            <h3 class="modal-title">Editando: {{network.name}}</h3>
        </div>

        <div class="modal-body">

	    	<form name="networkEditForm" sf-schema="schema" sf-form="form" sf-model="model" ng-submit="onSubmit(networkEditForm)"></form>
       		<b>{{ model }}</b

        </div>

	
        <div class="modal-footer">
            <button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
            <button class="btn btn-warning" type="button" ng-click="cancel()">Cancel</button>
        </div>
    </script>

  </div>
  

    
</div>
	
</body>

</html>
