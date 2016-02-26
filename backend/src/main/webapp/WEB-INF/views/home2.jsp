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
                <a class="navbar-brand" href="index.html">Administración - Harvester LAReferencia 3.0</a>
            </div>
            <!-- /.navbar-header -->

        </nav>

        <!-- #page-wrapper -->
        <div style="padding:20px;">
           
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
            
            
              <!-- .row -->
            <div class="row">
                <div class="col-lg-12">
                
                 </div>
                <!-- /.col-lg-12 -->
                
            </div>
            <!-- /.row -->
            
            <!-- .row -->
            <div class="row">
                <div class="col-lg-12">
                  
                                     <div class="table-responsive">
                                        <table class="table table-striped table-bordered table-hover" id="table-networks">
                                            <thead>
                                                <tr>
                                                    <th></th>
                                                    <th>ID</th>
                                                    <th>Nombre</th>
                                                    <th>Institución</th>
                                                    <th>LGK Snapshot</th>
                                                    <th>LST Snapshot</th>
                                                    <th>Edición</th>
                                                    <th>Acciones</th>
                                                </tr>
                                            </thead>
                                            <tbody></tbody>
                                        </table>
                                    </div>
                                    <!-- /.table-responsive -->
           

                </div>
                <!-- /.col-lg-12 -->
                
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
	
		
		///////////////////////////////////////////////////////////////////////////	
		
        
		function boolean2string ( data, type, full, meta ) {
     			if (data)
     				return 'Sí';
     			else
     				return 'No';
     		}
		
		function renderCheckBox ( data, type, full, meta ) {
 			return '<input type="checkbox" name=chk_"'+ data.networkID +'" value="false"/> ';		
 		}	
		
		function renderLGKSnapshot ( data, type, full, meta ) {
 			return  '<ul class="snapshotCaption">' +
 			        '<li><b>ID:</b>' + data.lgkSnapshotID   + '</li>' + 
 			        '<li><b>Fecha:</b>' + data.lgkSnapshotDate + '</li>' +
 			        '<li><b>Valid / Total:</b>' + data.lgkValidSize + data.lgkSize + '</li>' +
 			        '</ul>';		
 		}	
		
		function renderLSTSnapshot ( data, type, full, meta ) {
			return  '<ul class="snapshotCaption">' +
		        '<li><b>ID:</b>' + data.lstSnapshotID   + '</li>' + 
		        '<li><b>ID:</b>' + data.lstSnapshotStatus   + '</li>' + 
		        '<li><b>Fecha:</b>' + data.lstSnapshotDate + '</li>' +
		        '<li><b>Valid / Total:</b>' + data.lstValidSize + ' / '+ data.lstSize + '</li>' +
		        '</ul>';	 		
		}
		
		
		function renderActions ( data, type, full, meta ) {
			return  '<div class="dropdown">' +
			        '<button class="btn btn-default dropdown-toggle" type="button" id="dropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">' +
		            'Acciones <span class="caret"></span></button>' +
		  			'<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">' +
		    			'<li><a href="javascript:sendAction(\'START_HARVESTING\',' + data.networkID +');">Cosechar</a></li>' +
		    			'<li><a href="javascript:sendAction(\'STOP_HARVESTING\',' + data.networkID +');">Detener Cosecha</a></li>' +
		    			'<li><a href="#">Another action</a></li>' +
		    			'<li><a href="#">Something else here</a></li>' +
		    			'<li role="separator" class="divider"></li>' +
		    			'<li><a href="#">Separated link</a></li>' +
		 		    '</ul></div>';	
		}
		
		////////////////////////////////////////////////////////////////////////
        
        var tableNetworks =  $('#table-networks').DataTable({
            responsive: true,
            paging: true,
            info: false,
            searching:true,
            
            columns:[ 
                     { data: null, "width": "5px", orderable: false },
                     { data: 'networkID', "width": "30px" },
                     { data: 'name' },
                     { data: 'institution' },
                     { data: null },
                     { data: null },
                     { data: null },
                     { data: null },
                    ],
                    "columnDefs": [                                  
                                   {"targets": 0, "data": null, "render":renderCheckBox},
                                   {"targets": 4, "data": null, "render":renderLGKSnapshot},
                                   {"targets": 5, "data": null, "render":renderLSTSnapshot},
                                   {"targets": 7, "data": null, "render":renderActions}
                      			  ]      

    	});
		
        $('#table-rules tbody').on( 'click', 'button', 
        		function () {
					var data = tableRules.row( $(this).parents('tr') ).data();
					showRuleOccurrences(data.id);					
        		}
        );
        
      /////////////////////////////////////////////////////////////////////////////////////////  
        
	  function sendAction(action, networkID) {
		  $.rest.retrieve('/private/networkAction/' + action + '/' + networkID, 
				  function(result) { 
			  			alert('Acción: ' + action + ' lanzada');
			  	   } ); 
      }	
      
  
        $(document).ready(function() {
        	
        	$.rest.retrieve('/public/listNetworks', function(data) {
        		
        		 tableNetworks.rows().remove();
	             tableNetworks.rows.add(data).draw();
        	});
        	
        	
      	}); 
    
    </script>


</body>

</html>
