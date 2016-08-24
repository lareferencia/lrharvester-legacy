<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ page session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>
<html lang="en" ng-app="app">
<head>
    <meta charset="utf-8" />
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    
    <title>LAReferencia - Administración</title>
    
    <link rel="stylesheet" href="static/css/bootstrap.css" />
    <link rel="stylesheet" href="modules/app/app.css" /> 
    
    <!-- Bootstrap Core CSS -->
    <link type="text/css" rel="stylesheet" href="static/css/bootstrap.min.css" />
    
    <!-- MetisMenu CSS -->
    <link type="text/css" rel="stylesheet" href="static/css/metisMenu.min.css" >
             
    <!-- HighLight CSS -->
    <link type="text/css" rel="stylesheet" href="static/css/highlight/github.css"/>

    <!-- Custom Fonts -->
    <link type="text/css" rel="stylesheet" href="static/css/font-awesome.min.css"/>
    
    <!-- Ng Table CSS -->
    <link rel="stylesheet" href="static/css/ng-table.min.css"/>   
    
    <!-- App CSS -->
    <!-- >link type="text/css" rel="stylesheet" href="static/css/styles.css"/-->
       

</head>
<body class="app" ng-controller="app">
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" ng-click="navbarCollapsed = !navbarCollapsed">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="home">Administración de repositorios</a>
            </div>
            <div class="collapse navbar-collapse" collapse="navbarCollapsed">
                <ul class="nav navbar-nav" ng-show="loggedIn">
                    <li ui-sref-active="active"><a ui-sref="validators">Editar Validadores</a></li>
                    <li ui-sref-active="active"><a ui-sref="transformers">Editar Transformadores</a></li>
                    
                    <li ng-show="!navbarCollapsed"><a href ng-click="logout()">Logout</a></li>
                </ul>
                <ul class="nav navbar-nav" ng-show="!loggedIn">
                    <li ng-show="!navbarCollapsed"><a href="#">Login</a></li>
                </ul>
                <!-- >span class="dropdown user pull-right" on-toggle="toggled(open)" ng-show="navbarCollapsed">
                    <a href class="link dropdown-toggle"><img src="" ng-src="{{ user.image }}" class="img-circle" width="35" height="35" />{{ user.name  || 'Logged out'}}</a>
                    <ul ng-show="loggedIn" class="dropdown-menu">
                        <li><a ui-sref="overlay">My profile</a></li>
                        <li><a href ng-click="logout()">Logout</a></li>
                    </ul>
                    <ul ng-show="!loggedIn" class="dropdown-menu">
                        <li><a href="#">Login</a></li>
                    </ul>
                </span-->
            </div>
        </div>
    </div>
    <div class="container" ui-view="main">
    </div>
    
    
    <!-- JQuery JS -->
    <script type="text/javascript" src="static/libs/jquery-1.12.2.min.js"/></script>
        
    <!-- Bootstrap JS -->
    <script type="text/javascript" src="static/libs/bootstrap.min.js"/></script>
    
    <!-- metisMenu JS -->
    <script src="static/libs/metisMenu.min.js"></script>
    
    <script type="text/javascript" src="static/libs/moment.min.js"></script>
    <script type="text/javascript" src="static/libs/humanize-duration.js"></script>
    
    <!-- Angular js -->
	<script type="text/javascript" src="static/libs/angular.min.js"/></script>
    <script type="text/javascript" src="static/libs/angular-sanitize.min.js"/></script>
    <script type="text/javascript" src="static/libs/angular-resource.min.js"/></script>
    <script type="text/javascript" src="static/libs/angular-route.min.js"/></script>
    <script type="text/javascript" src="static/libs/angular-animate.min.js"/></script>
    <script type="text/javascript" src="static/libs/angular-timer.min.js"/></script>
    
 
    <!-- script src="static/libs/angular-ui-bootstrap.min.js"></script-->
    <script src="static/libs/angular-ui-router.min.js"></script>
    
      <!-- Angular UI Bootstrap --> 
    <script type="text/javascript" src="static/libs/ui-bootstrap-tpls-1.3.3.min.js"/></script>
   
    
    <!-- Libs de Angular NG Tables -->
    <script type="text/javascript" src="static/libs/ng-table.min.js"/></script>
    <script type="text/javascript" src="static/libs/ng-table-to-csv.min.js"/></script>
    
	<!-- Libs de Angular Spring Data Rest -->
	<script type="text/javascript" src="static/libs/angular-spring-data-rest.js"/></script>
	    
    <!-- Libs Angular de forms -->
    <script type="text/javascript" src="static/libs/tv4.min.js"/></script>
	<script type="text/javascript" src="static/libs/ObjectPath.js"/></script>
	<script type="text/javascript" src="static/libs/schema-form.min.js"/></script>
    <script type="text/javascript" src="static/libs/bootstrap-decorator.min.js"/></script>
    
    
    <!-- Libs Angular de sintax highlight -->
    
    <script type="text/javascript" src="static/libs/highlight.pack.js"/></script>
    <script type="text/javascript" src="static/libs/angular-highlightjs.min.js"/></script>
	<script type="text/javascript" src="static/libs/vkbeautify.js"></script>
    
    
    <!-- LoDash -->
    <script type="text/javascript" src="static/libs/lodash.core.js"/></script>
     
    <!-- Servicios -->
    <script src="static/services/table-services.js"></script>
    <script src="static/services/data-services.js"></script>
    <script src="static/services/rest-url-helper.js"/></script>
    
    <!-- Schemas  -->
    <script type="text/javascript" src="static/schemas/network-json-schemas.js"/></script>
    <script type="text/javascript" src="static/schemas/transformation-json-schemas.js"/></script>
    <script type="text/javascript" src="static/schemas/validation-json-schemas.js"/></script>
   
    <!-- Modulos -->
    <script src="modules/app/app.js"></script>
    <script src="modules/diagnose/diagnose.js"></script>
    <script src="modules/network/network.js"></script>
    <script src="modules/origin/origin.js"></script>
    <script src="modules/validators/validators.js"></script>
    <script src="modules/validator/validator.js"></script>
    <script src="modules/transformers/transformers.js"></script>
    <script src="modules/transformer/transformer.js"></script>
    
    

</body>
</html>