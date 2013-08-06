<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ page session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>LAReferencia Admin</title>
	<link rel="stylesheet" href="<spring:url value="/static/css/smoothness/jquery-ui.css"/>"></link>
	<link rel="stylesheet" href="<spring:url value="/static/css/jquery-select.css"/>"></link>
	<link rel="stylesheet" href="<spring:url value="/static/css/jquery-cron.css"/>"></link>
	<link rel="stylesheet" href="<spring:url value="/static/css/backend.css"/>"></link>
	<link rel="stylesheet" href="<spring:url value="/static/css/cubism.css"/>"></link>
	
	
	<script type="text/javascript" src="<spring:url value="/d3.v3.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery-ui.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery-select.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery-cron.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/rest.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/backend.js"/>"></script>
	
	<!--  Monitoreo de recursos usando Jolokia -->
	<script type="text/javascript" src="<spring:url value="/jolokia-min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jolokia-simple-min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/cubism.v1.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jolokia-cubism-min.js"/>"></script>	
	<script type="text/javascript" src="<spring:url value="/jolokia-backend.js"/>"></script>
	<!--  Necesita jolokia corriendo en /jolokia en tomcat -->
	
	
</head>

<body>	
	<script type="text/javascript">
	
	 
	 $(function() { 
		 
		
		  	 // cron editor create network 
		  	 $('#create_network_cron_selector').cron( { onChange: function() {
		  			$('[name=scheduleCronExpression]', '#form_create_network').attr('value', $(this).cron('value'));}});
		  
			 // cron editor create network 
		  	 $('#edit_network_cron_selector').cron( { onChange: function() {
		  		    var control = $('[name=scheduleCronExpression]', '#form_edit_network');
		  		    control.value = '';
		  			$('[name=scheduleCronExpression]', '#form_edit_network').attr('value', $(this).cron('value'));}});
		  
		  	 
		  	 // dialogo de borrar red, setup inicial
			 $("#dialog_delete_network").dialog( { autoOpen:false, 
				                                   resizable: false,
				      							   height:200,
				      							   modal: true
				      							});
		          
			 $("#dialog_network_snapshots").dialog( { autoOpen:false, 
                   resizable: false,
				   height:400,
				   width:800,
				   modal: true,
				   
				   buttons: {
				        
				        "Cerrar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				});
			 
		     
			 $("#dialog_create_network").dialog( { autoOpen:false, 
                   resizable: false,
				   height:500,
				   width:500,
				   modal: true,
				   
				   buttons: {
				        "Crear nueva red": function() {
				          createNetwork(refreshNetworks);
				          $( this ).dialog( "close" );
				        },
				        "Cerrar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
			 });
			 
			 $("#dialog_edit_network").dialog( { autoOpen:false, 
                 resizable: false,
				   height:500,
				   width:500,
				   modal: true,
				   
				   buttons: {
				        "Actualizar información": function() {
				          updateNetwork(actual_network_link, refreshNetworks);
				          $( this ).dialog( "close" );
				        },
				        "Cancelar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				});
			 
			 
			 $("#dialog_edit_origins").dialog( { autoOpen:false, 
                 resizable: false,
				   height:300,
				   width:680,
				   modal: true,
				   
				   buttons: {
					   "Agregar un nuevo orígen": function() {
					      openCreateOrigin();
					    },
				        "Cerrar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				});
			 
			 $("#dialog_edit_origin").dialog( { autoOpen:false, 
                 resizable: false,
				   height:250,
				   width:700,
				   modal: true,
				   
				   buttons: {
				        "Actualizar información": function() {
				          updateOrigin(actual_origin_link, refreshOrigins);
				          $( this ).dialog( "close" );
				        },
				        "Cancelar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				});
			 
			 $("#dialog_create_origin").dialog( { autoOpen:false, 
                 resizable: false,
				   height:250,
				   width:700,
				   modal: true,
				   
				   buttons: {
				        "Agregar origen a la red": function() {
				          createOrigin(actual_network_link, refreshOrigins);
				          $( this ).dialog( "close" );
				        },
				        "Cerrar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				});
		     
		     // carga de redes disponibles
			 loadNetworkList('./rest/network','#networks');
	  });
	</script>

	<div id="title">
		<span>Administración backend</span>
		<a  href="./logout">logout</a>
	</div> 
	
	<div id="memory" style="width:600;"></div>
	
	
	<div id="buttons">
		<button id="button_create_network" onclick="openCreateNetwork();">Crear una red</button>
	</div>
	<div id="networks"></div>
	<br/>
	
	
	<div id="dialog_network_snapshots" title="Listado de cosechas de la red nacional">
		<div id="snapshots"></div>
	</div>
	
	<div id="dialog_delete_network" title="¿Desea borrar la red nacional?">
  		<p>
  			<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
  			Todos los datos de la red nacional, incluidas sus cosechas serás borradas. ¿Está seguro?
  		</p>
  	</div>
  	
  	<div id="dialog_edit_network" title="Editar datos de la red nacional">	
			<form id="form_edit_network">
				Nombre:<input type="text" name="name" maxlength="255" size="20"/><br/>
				ISO País:<input type="text" name="countryISO" maxlength="2" size="2"/><br/> 
				Publicada: <input type="checkbox" name="published" value="1"/><br/>
				
				Ejecutar validación: <input type="checkbox" name="runValidation" value="1"/><br/>
				Ejecutar transformación: <input type="checkbox" name="runTransformation" value="1"/><br/>
				Ejecutar indexación: <input type="checkbox" name="runIndexing" value="1"/><br/>
				<br/>
				Cosecha programada: <input type="text" name="scheduleCronExpression" maxlength="255" size="20"/><br/>
			</form>
			
			<div id='edit_network_cron_selector'></div>
			
	 </div>
	 
	 <div id="dialog_create_network" title="Crear una nueva red nacional">	
			<form id="form_create_network">
				Nombre:<input type="text" name="name" maxlength="255" size="20"/><br/>
				ISO País:<input type="text" name="countryISO" maxlength="2" size="2"/><br/> 
				Publicada: <input type="checkbox" name="published" value="1"/><br/>
				Ejecutar validación: <input type="checkbox" name="runValidation" value="1"/><br/>
				Ejecutar transformación: <input type="checkbox" name="runTransformation" value="1"/><br/>
				Ejecutar indexación: <input type="checkbox" name="runIndexing" value="1"/><br/>
				<br/>
				Cosecha programada: <input type="text" name="scheduleCronExpression" maxlength="255" size="20"/><br/>
			</form>
			<div id='create_network_cron_selector'></div>
	 </div>		
	 
	<div id="dialog_edit_origins" title="Editar orígenes OAI de cosecha de la red">
		<div id="network_origins"></div>
	</div> 
	
	<div id="dialog_edit_origin" title="Editar un orígen OAI">
		<form id="form_edit_origin">
			Nombre:<input type="text" name="name" maxlength="255" size="20"/><br/>
			Metadata prefix:<input type="text" name="metadataPrefix" maxlength="255" size="30"/><br/> 
			URI:<input type="text" name="uri" maxlength="150" size="100"/><br/> 
		</form>
	</div>
	
	<div id="dialog_create_origin" title="Agregar un orígen OAI">
		<form id="form_create_origin">
			Nombre:<input type="text" name="name" maxlength="255" size="20"/><br/>
			Metadata prefix:<input type="text" name="metadataPrefix" maxlength="255" size="30"/><br/> 
			URI:<input type="text" name="uri" maxlength="150" size="100"/><br/> 
		</form>
	</div>		
	
</body>
</html>
