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

    <title>LA Referencia - Diagnóstico</title>

    <!-- Bootstrap Core CSS -->
    <link href="/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- DataTables CSS -->
    <link href="/static/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css" rel="stylesheet">

    <!-- DataTables Responsive CSS -->
    <link href="/static/bower_components/datatables-responsive/css/dataTables.responsive.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="/static/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/static/diagnose/css/sb-admin-2.css" rel="stylesheet">
    
    <!-- HighLight CSS -->
    <link rel="stylesheet" href="<spring:url value="/static/diagnose/css/jquery.highlight.css"/>" >
    

    <!-- Morris Charts CSS -->
    <link href="/static/bower_components/morrisjs/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/static/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.html">Diagnóstico LAReferencia 3.01 beta</a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-envelope fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-messages">
                        <li>
                            <a href="#">
                                <div>
                                    <strong>John Smith</strong>
                                    <span class="pull-right text-muted">
                                        <em>Yesterday</em>
                                    </span>
                                </div>
                                <div>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eleifend...</div>
                            </a>
                        </li>
                        <li class="divider"></li>
                       
                    </ul>
                    <!-- /.dropdown-messages -->
                </li>
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-tasks fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-tasks">
                        <li>
                            <a href="#">
                                <div>
                                    <p>
                                        <strong>Task 1</strong>
                                        <span class="pull-right text-muted">40% Complete</span>
                                    </p>
                                    <div class="progress progress-striped active">
                                        <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                                            <span class="sr-only">40% Complete (success)</span>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </li>
                        
                    </ul>
                    <!-- /.dropdown-tasks -->
                </li>
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        
                    </ul>
                    <!-- /.dropdown-alerts -->
                </li>
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="#"><i class="fa fa-user fa-fw"></i> User Profile</a>
                        </li>
                        <li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
                        </li>
                        <li class="divider"></li>
                        <li><a href="login.html"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                        </li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
            </ul>
            <!-- /.navbar-top-links -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        
                      	<li>
                            <a href="#"><i class="fa fa-university fa-fw"></i> Instituciones <span class="fa arrow"></span></a>
                            <ul id="institution_name_facet" class="nav nav-second-level">  </ul>  
                            <!-- /.nav-second-level -->
                        </li>
                       
                        <li>
                            <a href="#"><i class="fa fa-sitemap fa-fw"></i> Subredes <span class="fa arrow"></span></a>
                            <ul id="repository_name_facet" class="nav nav-second-level"></ul>   
                            <!-- /.nav-second-level -->
                        </li>
                        
                        <li>
                            <a href="#"><i class="fa fa-check-square-o fa-fw"></i> Validez de registros <span class="fa arrow"></span></a>
                            <ul id="record_is_valid_facet" class="nav nav-second-level">  </ul>  
                            <!-- /.nav-second-level -->
                        </li>
                       
                        <li>
                            <a href="#"><i class="fa fa-check fa-fw"></i> Reglas Válidas  <span class="fa arrow"></span></a>
                            <ul id="valid_rules_facet" class="nav nav-second-level">  </ul>  
                            <!-- /.nav-second-level -->
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-times fa-fw"></i> Reglas Inválidas <span class="fa arrow"></span></a>
                            <ul id="invalid_rules_facet" class="nav nav-second-level">  </ul>  
                            <!-- /.nav-second-level -->
                        </li>
                        
                         
                      
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <br/>
                    <!--h1 class="page-header">Dashboard</h1-->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-files-o fa-2x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="medium" id="totalRegs"></div>
                                    <div>Reg. Totales</div>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-green">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-check fa-2x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                       <div class="medium" id="validRegs"></div>
                                    <div>Reg. Válidos</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-yellow">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-cogs fa-2x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <div class="medium" id="transformedRegs"></div>
                                    <div>Reg. Transformados</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-red">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-times  fa-2x"></i>
                                </div>
                                
                                 <div class="col-xs-9 text-right">
                                    <div class="medium" id="invalidRegs"></div>
                                    <div>Reg. Inválidos</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                  
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-bar-chart-o fa-fw"></i> Nivel de cumplimiento de reglas
                            <div class="pull-right">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                        Descargar
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu pull-right" role="menu">
                                        <li><a href="#">Action</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="table-responsive">
                                        <table class="table table-striped table-bordered table-hover" id="table-rules">
                                            <thead>
                                                <tr>
                                                    <th>Regla</th>
                                                    <th>Descripción</th>
                                                    <th>Mandatoria</th>
                                                    <th>Válidos</th>
                                                    <th>%</th>
                                                    <th></th>
                                                    

                                                </tr>
                                            </thead>
                                            <tbody></tbody>
                                        </table>
                                    </div>
                                    <!-- /.table-responsive -->
                                </div>
                                <!-- /.col-lg-4 (nested) -->
                                
                            </div>
                            <!-- /.row -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                    
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-bar-chart-o fa-fw"></i> Detalle de registros 
                            <div class="pull-right">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                        Descargar
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu pull-right" role="menu">
                                        <li><a href="#">Action</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="table-responsive">
                                        <table class="table table-striped table-bordered table-hover" id="table-records">
                                            <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Identifier</th>
                                                    <th>VLD</th>
                                                    <th>TRF</th>
                                                    <th></th>

                                                </tr>
                                            </thead>
                                            <tbody></tbody>
                                        </table>
                                    </div>
                                    <!-- /.table-responsive -->
                                </div>
                                <!-- /.col-lg-4 (nested) -->
                                <div class="col-lg-8">
                                    <div id="morris-bar-chart"></div>
                                </div>
                                <!-- /.col-lg-8 (nested) -->
                            </div>
                            <!-- /.row -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->

                </div>
                <!-- /.col-lg-8 -->
                
            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->
    
    <!-- Modal View Validation Result -->
	<div class="modal fade" id="modalViewValidationResult" tabindex="-1" role="dialog" aria-labelledby="titleLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" id="titleLabel">Resultado de validación</h4>
	      </div>
	      <div  class="modal-body" id="modalViewValidationResultBody" >
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- Modal View Register -->
	<div class="modal fade" id="modalViewMetadata" tabindex="-1" role="dialog" aria-labelledby="titleLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" id="titleLabel">Metadata</h4>
	      </div>
	      <div  class="modal-body" id="modalViewMetadataBody">
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<!-- Modal View Values -->
	<div class="modal fade" id="modalViewRuleValues" tabindex="-1" role="dialog" aria-labelledby="titleLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" id="titleLabel">Valores más frecuentes</h4>
	      </div>
	      <div  class="modal-body" id="modalViewRuleValuesBody">
	        
	      	<div class="row">
	      	  
	      	   <div class="col-lg-6">
				    <div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover" id="table-valid-values">
                            <thead>
                                <tr>
                                    <th>Válidos</th>
                                    <th>Freq</th>
                                    
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>	   
	      	   </div>
	      	   <div class="col-lg-6">
	      	   		<div class="table-responsive">
                        <table class="table table-striped table-bordered table-hover" id="table-invalid-values">
                            <thead>
                                <tr>
                                    <th>Inválidos</th>
                                    <th>Freq</th>
                                    
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>	   
	      	   </div>
	      	</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	      </div>
	    </div>
	  </div>
	</div>
	

    <!-- jQuery -->
    <script src="/static/bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="/static/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="/static/bower_components/metisMenu/dist/metisMenu.min.js"></script>

    <!-- Morris Charts JavaScript -->
    <!--script src="/static/bower_components/raphael/raphael-min.js"></script>
    <script src="/static/bower_components/morrisjs/morris.min.js"></script>
    <script src="../js/morris-data.js"></script-->
    
    <!-- DataTables JavaScript -->
    <script src="/static/bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
    <script src="/static/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
    
     <!-- JQUERYSOLR JavaScript -->
    <script src="/static/diagnose/js/jquery.querysolr.js"></script>
    
     <!-- Rest Interface -->
    <script src="/static/diagnose/js/rest.js"></script>
    
    <script type="text/javascript" src="<spring:url value="/static/diagnose/js/jquery.highlight.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/static/diagnose/js/vkbeautify.js"/>"></script>

    <!-- Custom Theme JavaScript -->
    <script src="/static/diagnose/js/sb-admin-2.js"></script>
    
    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
	<script type="text/javascript">
	

		var fqHash = {};
		
		function str2id(input) {
			return input.replace(/\W/g, ''); 
		}
		
		
		function onFacetClick(facetName, facetValue) {
			
			var key = facetName + ':"' + facetValue + '"';
			
			$( "#" + str2id(facetValue) + "_facet_value" ).toggleClass("selected");
			
			if (fqHash[key] == null )
				fqHash[key] = true;
			else 
				delete fqHash[key];
			
       	 	obtainStatsFromSolr();
        }
	
		
		/////////////////////////////////////////////////////////////////////////
		   
        var tableValidValues =  $('#table-valid-values').DataTable({
                responsive: true,
                paging: true,
                info: false,
                searching: false,
                columns:[ 
                         
                         { data: 'value' },
                         { data: 'freq' },
               ]
        });
		
        var tableInvalidValues =  $('#table-invalid-values').DataTable({
            responsive: true,
            paging: true,
            info: false,
            searching: false,
            columns:[ 
                     
                     { data: 'value' },
                     { data: 'freq' },
           ]
    	});
		
		
		
		///////////////////////////////////////////////////////////////////////////	
		var tableRecordsButtons =
			'<button id="btnTRF" class="btn btn-default">R. Transformado</button>' +
			'<button id="btnRPS" class="btn btn-default">Ver en el Repositorio</button>' +
			'<button id="btnVLD" class="btn btn-default">Verificar Validación</button>';

        
		function boolean2string ( data, type, full, meta ) {
     			if (data)
     				return 'Sí';
     			else
     				return 'No';
     		}	
			
        var tableRecords =  $('#table-records').DataTable({
                responsive: true,
                paging: false,
                info: false,
                searching: false,
                columns:[ 
                	
                	 { data: 'id', "width": "10%" },
                     { data: 'oai_identifier', "width": "40%", 'sort': false },
                     { data: 'record_is_valid', "width": "5%"},                   
                     { data: 'record_is_transformed', "width": "5%" },
                     { data: null, "defaultContent":  tableRecordsButtons },
                ],
                "columnDefs": [
                    {
                    "targets": 2,
                    "data": "record_is_valid",
                    "render":boolean2string
                    },
                    {
                    "targets": 3,
                    "data": "record_is_transformed",
                    "render":boolean2string
                    }
                 ],
		        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		            if ( aData['record_is_valid']  )
	                    $(nRow).css('background-color', '#dff0d8');
		            else
		                $(nRow).css('background-color', '#f2dede');
		        }
        });
        
        $('#table-records tbody').on( 'click', 'button', 
        		function () {
        	
					var data = tableRecords.row( $(this).parents('tr') ).data();

        			switch (this.id) {
        			
	        			case "btnTRF":    				
		       				requestAndLoadMetadata( '/public/getRecordMetadataByID/' + data.id );
	        				break;
	        				
	        			case "btnRPS":
	            			window.open( data.origin + '?verb=GetRecord&metadataPrefix=oai_dc&identifier=' + data.oai_identifier, '_blank'); 		
	        				break;
	        				
	        			case "btnVLD":
	        				
	        				displayValidationResult(data);
	        				break;
        			
        			}
        	
        				
        		});
        
		////////////////////////////////////////////////////////////////////////
        
        var tableRules =  $('#table-rules').DataTable({
            responsive: true,
            paging: false,
            info: false,
            searching:false,
            
            columns:[ 
                  
                     { data: 'name' },
                     { data: 'description' },
                     { data: 'mandatory', "width": "5%", "title":"M." },
                     { data: 'valid', "width": "10%" },
                     { data: 'score', "width": "10%" },
                     { data: null, "defaultContent":  '<button id="btnVLR" class="btn btn-default">Valores</button>', "width": "10%" }
                    ],
                    "columnDefs": [
                                   {
                                   "targets": 2,
                                   "data": "record_is_valid",
                                   "render":boolean2string
                                   }
                     			  ]      

    	});
		
        $('#table-rules tbody').on( 'click', 'button', 
        		function () {
					var data = tableRules.row( $(this).parents('tr') ).data();
					showRuleOccurrences(data.id);					
        		}
        );
        
      /////////////////////////////////////////////////////////////////////////////////////////  
        
      function runSolrQuery(params, callback) {
    	  $.querysolr({
              host: "http://localhost",     // the IP or domain where the Solr server runs (default '127.0.0.1')
              port: "8983",                 // the port the Solr server listens (default '8983')
              component: "vstats/select",   // the query component to use (default 'select')
              params: params,
              callback: callback,
           	  encodeURI: true // automatically encode the URI (default 'true')
			}).query(); // and finally send the request   
             
      }
        
 	  function requestAndLoadMetadata(serviceURL) {
			
			$.ajax({
		        type: 'GET',
		        url:  serviceURL,
		        contentType: "application/xml; charset=utf-8",
		        async: true,
		        success: function (result) {	
			      	$('#modalViewMetadataBody').children(":first").remove();
			      	$('#modalViewMetadataBody').append('<pre  class="code" lang="html"></pre>');
		        	$('#modalViewMetadataBody .code').text( vkbeautify.xml(result) );
		        	$('#modalViewMetadataBody .code').highlight();  
		        	$('#modalViewMetadata').modal('show');
		        }		        
		    });
			
		}
        
        
        function renderFacet(data, facetName, translateDict) {
        	
	        var facetData = data.facet_counts.facet_fields[facetName];
	 
	  	  	$('#' + facetName+ '_facet').html("");
	
	        for (var i=0;i<facetData.length-1;i=i+2) {
	        	
	             var facetValue = facetData[i];
	             var facetDisplayValue = facetValue;
	             if (translateDict != null)
	            	 facetDisplayValue = translateDict[facetValue];
	             
	             var facetFunction = "javascript:onFacetClick('" + facetName + "','" + facetValue + "');";   
	         	
	             $('#' + facetName + '_facet').append('<li id="' + str2id(facetValue) + "_facet_value" +  '"><a href="'+ facetFunction + '"> +' + facetDisplayValue +  ' (' + facetData[i+1] +')</a></li>');
	         	
	             var key = facetName + ':"' + facetValue + '"';
	             
	             if ( fqHash[key] != null && fqHash[key]  )
	     			$( "#" + str2id(facetValue) + "_facet_value" ).toggleClass("selected");

	            	 
	         	 
	        }
        }
        
        
       function facet2object(facetArray) {
        	 facetObj = {};	
        
             for (var i=0;i<facetArray.length-1;i=i+2) {
           	  facetObj[ facetArray[i] ] = facetArray[i+1];
             }	
             
             return facetObj;
        }
        
       function facet2freqtable(facetArray) {
       	 	var resultArray = [];
    	   
            for (var i=0;i<facetArray.length-1;i=i+2) {
              facetObj = resultArray.push( { 'value':facetArray[i], 'freq': facetArray[i+1]} );  
        
            }	
            
            return resultArray;
       }
        
        function getFacetValueCount(data, facetName, facetValue) {
        	
        	 var validDocsObj = facet2object(data.facet_counts.facet_fields[facetName]);
             var result =  validDocsObj[facetValue];
             
             if (result == null)
            	 return 0;
             else 
            	 return result;
        }
        
        function displayValidationResult(data) {
        	
        	
        	$('#modalViewValidationResultBody').html("");
        	
        	
        	if ( data.record_is_valid == "true" )
        		$('#modalViewValidationResultBody').append( '<h2><span class="label label-success">registro válido</span></h2>' );
        	else
        		$('#modalViewValidationResultBody').append( '<h2><span class="label label-danger">registro inválido</span></h2>' );

        	
        	
        	$.each(rulesNames, function (ruleID, ruleName) {
        		
        		var html = '<div class="alert';
        		
                if ( $.inArray(ruleID, data.valid_rules) != -1 ) 
            		html += " alert-success";
            	else
            		html += " alert-danger";	
        		
        		html += '">';  
        		
        		html += '<b>' + ruleName + '</b>';
        		
        			
        		var valid_occrs = data[ruleID + "_rule_valid_occrs" ];
        		if (valid_occrs == null ) valid_occrs = [];
        		
        		var invalid_occrs = data[ruleID + "_rule_invalid_occrs" ];  
        		if (invalid_occrs == null ) invalid_occrs = [];
        		
        		$.each(valid_occrs, function (index, occr) {
					html += '<p><span style="margin-right:5px;" class="glyphicon glyphicon-ok"></span>' + occr +'</p>';

        		});
        		
        		$.each(invalid_occrs, function (index, occr) {
					html += '<p><span style="margin-right:5px;" class="glyphicon glyphicon-remove"></span>' + occr +'</p>';
        		});
        		
        		html += '</div>';        	

        		
        		$('#modalViewValidationResultBody').append(html);

            });
				
        	$('#modalViewValidationResult').modal('show');
        }
        
        
        
        var rulesCount = {};
        var rulesNames = {};
        
        function obtainRulesFromBackend(callback) {
                	
            $.rest.retrieve("/public/listValidatorRulesByNetworkID/1", 
                    function(results) { 
         
            				var data = [];
            	
			            	$.each(results, function (index, rule) {
			                    
			            		var rdata = rulesCount[rule.id];
			            		
			            		var row = {};
			            		
			            		row["id"] = rule.id;
			            		row["name"] = rule.name;
			            		row["description"] = rule.description;
			            		row["mandatory"] = rule.mandatory;
			            		row["valid"] = rdata["valid"];
			            		row["score"] =  (100 * rdata["valid"]/rdata["total"]).toFixed(2);
			            		
			            		data.push( row );
			            		
			                    rulesNames[rule.id] = rule.name;
			                });
			   			
			               tableRules.rows().remove();
			               tableRules.rows.add(data).draw();
			               callback();
                    }); 	
        }
        
        
        
       
        
        function showRuleOccurrences(ruleID) {
        	
        	 var params =  [
	                            {q: 'snapshot_id:26'},
	                            {rows: '0'},
	                            {facet: true},
	                            {'facet.mincount': '1'},
	                            {'facet.field': ruleID + '_rule_invalid_occrs'},
	                            {'facet.field': ruleID + '_rule_valid_occrs'},

                            ];
        	 
        	 
        	 $.each(fqHash, function (fqString, value) {
             	params.push( {fq:fqString} );
             });
        	 
        	
        	 runSolrQuery(params, function(data) {
        		
            	 var invalidFreqData = facet2freqtable(data.facet_counts.facet_fields[ruleID + '_rule_invalid_occrs']);
            	 var validFreqData = facet2freqtable(data.facet_counts.facet_fields[ruleID + '_rule_valid_occrs']);
            	
            	 tableInvalidValues.rows().remove();
            	 tableInvalidValues.rows.add(invalidFreqData).draw();
            	 
            	 tableValidValues.rows().remove();
            	 tableValidValues.rows.add(validFreqData).draw();


		       	 $('#modalViewRuleValues').modal('show');

        	 } );
  	
        }
        
        
        function obtainStatsFromSolr() {
        	
        	 rulesCount = {};

        	 var params =  [
                            {q: 'snapshot_id:26'},
                            
                            {facet: true},
                            {'facet.mincount': '1'},
                            {'facet.field': 'record_is_valid'},
                            {'facet.field': 'record_is_transformed'},  
                            {'facet.field': 'valid_rules'},
                            {'facet.field': 'invalid_rules'},
                            {'facet.field': 'institution_name'},
                            {'facet.field': 'repository_name'},
                        ];
        	 
        	 
        	 $.each(fqHash, function (fqString, value) {
             	params.push( {fq:fqString} );
             });
       	  
        	 
         	runSolrQuery(params, function(data) { 

                     var docs = data.response.docs;
                     tableRecords.rows().remove();
                     tableRecords.rows.add(docs).draw();
                     
                     var totaldocs = data.response.numFound;
                     $('#totalRegs').html(totaldocs);             
                     $('#validRegs').html( getFacetValueCount(data, "record_is_valid", "true") );
                     $('#invalidRegs').html( getFacetValueCount(data, "record_is_valid", "false") );
                     $('#transformedRegs').html( getFacetValueCount(data, "record_is_transformed", "true") );

                     ////////////////
                     var valid_rules_facet = data.facet_counts.facet_fields.valid_rules;
                     var invalid_rules_facet = data.facet_counts.facet_fields.invalid_rules;
                     
                     for (var i=0;i<valid_rules_facet.length-1;i=i+2) {
                   	  rulesCount[ valid_rules_facet[i] ] = { "valid" : valid_rules_facet[i+1], "invalid":0, "total":totaldocs };
                     }
                     
                     for (var i=0;i<invalid_rules_facet.length-1;i=i+2) {
                   	  
	                   	  if ( rulesCount[ invalid_rules_facet[i] ] == null ) {
	                   		  rulesCount[ invalid_rules_facet[i] ] = { "valid" : 0, "invalid":0, "total":totaldocs};
	                  	  }
	                   		  
	                   		  
	                   	  rulesCount[ invalid_rules_facet[i] ]["invalid"] = invalid_rules_facet[i+1];
                     }
                    
                     //////////////////
                   
                     obtainRulesFromBackend( function () {
                    	 
                    	   renderFacet(data,"repository_name", null);
                           renderFacet(data,"institution_name", null);
                           renderFacet(data,"valid_rules", rulesNames);
                           renderFacet(data,"invalid_rules", rulesNames); 
                           renderFacet(data,"record_is_valid", { "true":"Válidos", "false":"Inválidos"}); 

                     });
        	}); 
        }
        
        
        $(document).ready(function() {
      		obtainStatsFromSolr();
      	}); 
    
    </script>


</body>

</html>
