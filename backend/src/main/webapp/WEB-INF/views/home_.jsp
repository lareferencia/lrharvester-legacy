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
	
	 // cron editor copy to 
	 function copyCronToEditNetwork() {
		var value = $('#input_edit_network_cron_helper').attr('value');  
 	    $('#input_edit_network_cron').attr('value', value);   	 
 	 }
	 
	 $(function() { 
		 
		
		  	 // cron editor create network 
		  	 $('#create_network_cron_selector').cron( { initial: "0 0 1 1 *",
													    onChange: function() {
		  			$('[name=scheduleCronExpression]', '#form_create_network').attr('value', '* ' + $(this).cron('value'));},
		  												customValues: {
											                "Domingos 9AM" : "0 9 * * 0",
											            }			
		  	 
		  	 });
		  
		  	 // cron editor edit network 
		  	 $('#edit_network_cron_selector').cron( { onChange: function() {
		  			$('#input_edit_network_cron_helper').attr('value', '* ' + $(this).cron('value'));}
		  	 });
		  
				
		  	 // dialogo de borrar red, setup inicial
			 $("#dialog_delete_network").dialog( { autoOpen:false, 
				                                   resizable: false,
				      							   height:200,
				      							   modal: true
				      							});
		  	 
			 // dialogo de borrar red, setup inicial
			 $("#dialog_delete_network_from_index").dialog( { autoOpen:false, 
				                                   resizable: false,
				      							   height:200,
				      							   modal: true
				      							});
			 
			 
			 // dialogo de borrar red, setup inicial
			 $("#dialog_delete_network_from_xoai_index").dialog( { autoOpen:false, 
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
				       
					   "Recargar": function() {
							 loadSnapshotList(actual_network_link);
					    },
					   
				        "Cerrar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				});
			 
		     
			 $("#dialog_snapshot_log").dialog( { autoOpen:false, 
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
		     
			 
			 $("#dialog_edit_sets").dialog( { autoOpen:false, 
                 resizable: false,
				   height:300,
				   width:680,
				   modal: true,
				   
				   buttons: {
					   "Agregar un nuevo set": function() {
					      openCreateSet();
					    },
				        "Cerrar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				});
			 
			 $("#dialog_create_set").dialog( { autoOpen:false, 
                 resizable: false,
				   height:200,
				   width:400,
				   modal: true,
				   
				   buttons: {
				        "Agregar set al origen": function() {
				          createSet(actual_origin_link, refreshSets);
				          $( this ).dialog( "close" );
				        },
				        "Cerrar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				});
			 
			 $("#dialog_edit_set").dialog( { autoOpen:false, 
                 resizable: false,
				   height:200,
				   width:400,
				   modal: true,
				   
				   buttons: {
				        "Actualizar información": function() {
				          updateSet(actual_set_link, refreshSets);
				          $( this ).dialog( "close" );
				        },
				        "Cancelar": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				});
		     
			 
			 // Oculta el panel Jolokia
			 $('#jolokia').toggle();
			 
		     // carga de redes disponibles
			 loadNetworkList('./rest/network?size=1000&sort=id');
	  });
	</script>

	<div id="title">
		<span>Administración backend</span>
		<a  href="./logout">logout</a>
	</div> 
	
	<span onclick="$('#jolokia').toggle();toogleJolokia();"><b>Mostrar/Ocultar Status</b><br/></span>
	<div id="jolokia" class="slidingDiv"> 
			<div id="memory" style="width:600;"></div>
			<div id="harvesters" style="width:600;"></div>
	</div>
	
	
	<div id="buttons">
		<button id="button_create_network" onclick="openCreateNetwork();">Crear una red</button>
	</div>
	
	<div>
		<table>
		    <tr>
		      <th>ID</th>
		      <th>Nombre</th>
		      <th>ACRON</th>
		      <th>Publicada</th>
		    </tr>
  		<tbody id="networks"></tbody>
		</table>
		<br/>
		<div id="help">
			<p><strong>LKGS:</strong> ver último snapshot válido (LGK) </p>
			<p><strong>Snapshots:</strong> ver todos los snapshots </p>
			<p><strong>Orígenes:</strong> editar los orígenes OAI de la red </p>
			<p><strong>Cosechar:</strong> lanzar un proceso de cosecha </p>
			<p><strong>Index LKG:</strong> indexar el último snapshot válido (LGK) </p>
			<p><strong>Limpiar:</strong> eliminar información de snapshots antiguos </p>
			<p><strong>Editar:</strong> editar detalles y programar cosechas </p>
			<p><strong>Borrar :</strong> eliminar la red del sistema (!!) </p>
		    <p><strong>Borrar IDX:</strong> eliminar la red del índice Solr / Vufind (!!) </p>				
		</div>
	
	</div>
	<br/>
	
	
	<div id="dialog_network_snapshots" title="Listado de cosechas de la red nacional">
		
		<table>
		    <tr>
		      <th>ID</th>
		      <th>Estado</th>
		      <th>Borrado</th>
		      <th>#Registros</th>
		      <th>#Válidos</th>
		      <th>#Transformados</th>
		      <th>Iniciado</th>
		      <th>Terminado</th>
		      
		
		      
		    </tr>
  			<tbody id="snapshots"></tbody>
		</table>
	</div>
	
	<div id="dialog_snapshot_log" title="Bitácora de cosecha">
		
		<table>
		    <tr>
		      <th width="150">FechaHora</th>
		      <th width="600">Mensaje</th>
		    </tr>
  			<tbody id="snapshot_log"></tbody>
		</table>
	</div>
	
	<div id="dialog_delete_network" title="¿Desea borrar la red nacional?">
  		<p>
  			<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
  			Este proceso puede tomar varios minutos de acuerdo al tamaño de la red y sus snapshots. 
  			Todos los datos de la red nacional, incluidas sus cosechas serás borradas. ¿Está seguro?
  		</p>
  	</div>
  	
  	<div id="dialog_delete_network_from_index" title="¿Desea borrar la red nacional del índice vufind?">
  		<p>
  			<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
  			Este proceso puede tomar varios minutos de acuerdo al tamaño de la red y el estado de solr. 
  			Todos los datos de la red nacional serán removidos del índice ¿Está seguro?
  		</p>
  	</div>
  	
  	<div id="dialog_delete_network_from_xoai_index" title="¿Desea borrar la red nacional del índice XOAI?">
  		<p>
  			<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
  			Este proceso puede tomar varios minutos de acuerdo al tamaño de la red y el estado de solr. 
  			Todos los datos de la red nacional serán removidos del índice ¿Está seguro?
  		</p>
  	</div>
  	
  	<div id="dialog_edit_network" title="Editar datos de la red nacional">	
			<form id="form_edit_network">
				Nombre:<input type="text" name="name" maxlength="255" size="20"/><br/>
				Institución:<input type="text" name="institutionName" maxlength="255" size="20"/><br/>
				
				Acrónimo (ID de 4 dígitos):<input type="text" name="acronym" maxlength="4" size="4"/><br/> 
				Publicada: <input type="checkbox" name="published" value="1"/><br/>
				
				Validador:<input type="text" name="validatorName" maxlength="50" size="30"/><br/> 
				Transformador:<input type="text" name="transformerName" maxlength="50" size="30"/><br/> 
				
				Ejecutar validación: <input type="checkbox" name="runValidation" value="1"/><br/>
				Ejecutar transformación: <input type="checkbox" name="runTransformation" value="1"/><br/>
				Ejecutar indexación: <input type="checkbox" name="runIndexing" value="1"/><br/>
				Exportar XOAI: <input type="checkbox" name="runXOAI" value="1"/><br/>
				Ejecutar estadísticas: <input type="checkbox" name="runStats" value="1"/><br/>
				
				<br/>
				Cosecha programada: <input id="input_edit_network_cron" type="text" name="scheduleCronExpression" maxlength="255" size="20"/><br/>
			</form>
			
			<hr></hr>
			<p><b>Asistente de expresiones cron</b></p>
			<!--  p>Cree un expresión nueva y haga click en copiar para sobreescribir la expresión actual</p-->
			<p>Cree un expresión nueva y haga use copiar/pegar para  sobreescribir la expresión actual</p>
			
			<input id="input_edit_network_cron_helper"></input>
			<!--  button onclick="copyCronToEditNetwork()">copiar</button-->
			<br/>
			<div id='edit_network_cron_selector'></div>
			
	 </div>
	 
	 <div id="dialog_create_network" title="Crear una nueva red nacional">	
			<form id="form_create_network">
				Nombre:<input type="text" name="name" maxlength="255" size="20"/><br/>
				Institución:<input type="text" name="institutionName" maxlength="255" size="20"/><br/>
				Acrónimo (ID de 4 dígitos):<input type="text" name="acronym" maxlength="4" size="4"/><br/> 
				Publicada: <input type="checkbox" name="published" value="1"/><br/>
				
				Validador:<input type="text" name="validatorName" maxlength="50" size="30"/><br/> 
				Transformador:<input type="text" name="transformerName" maxlength="50" size="30"/><br/> 
				
				Ejecutar validación: <input type="checkbox" name="runValidation" value="1"/><br/>
				Ejecutar transformación: <input type="checkbox" name="runTransformation" value="1"/><br/>
				Ejecutar indexación: <input type="checkbox" name="runIndexing" value="1"/><br/>
				Exportar XOAI: <input type="checkbox" name="runXOAI" value="1"/><br/>
				Ejecutar estadísticas: <input type="checkbox" name="runStats" value="1"/><br/>
				
				<br/>
				Cosecha programada: <input type="text" name="scheduleCronExpression" maxlength="255" size="20"/><br/>
			</form>
			<div id='create_network_cron_selector'></div>
	 </div>		
	 
	<div id="dialog_edit_origins" title="Editar orígenes OAI de cosecha de la red">
			<table>
		    <tr>
		      <th width="150">Nombre</th>
		      <th width="600">URL</th>
		    </tr>
  			<tbody id="network_origins"></tbody>
		</table>
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
	
	<div id="dialog_edit_sets" title="Editar sets OAI de cosecha de la red">
			<table>
		    <tr>
		      <th width="150">Nombre</th>
		      <th width="600">Spec</th>
		    </tr>
  			<tbody id="origin_sets"></tbody>
		</table>
	</div> 
	
	<div id="dialog_create_set" title="Agregar un set OAI">
		<form id="form_create_set">
			Nombre:<input type="text" name="name" maxlength="255" size="20"/><br/>
			Spec:<input type="text" name="spec" maxlength="255" size="30"/><br/> 
		</form>
	</div>	
	
	<div id="dialog_edit_set" title="Agregar un set OAI">
		<form id="form_edit_set">
			Nombre:<input type="text" name="name" maxlength="255" size="20"/><br/>
			Spec:<input type="text" name="spec" maxlength="255" size="30"/><br/> 
		</form>
	</div>	
	
</body>
</html>
