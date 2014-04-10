<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Diagnóstico de cosecha</title>


    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    

	<link rel="stylesheet" href="<spring:url value="/static/css/nv.d3.css"/>"></link>
	<link rel="stylesheet" href="<spring:url value="/static/css/bootstrap.min.css"/>" >
	<link rel="stylesheet" href="<spring:url value="/static/css/jquery.highlight.css"/>" >
    <link rel="stylesheet" href="<spring:url value="/static/css/diagnose.css"/>" >
	
	<script type="text/javascript" src="<spring:url value="/d3.v3.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/nv.d3.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery.highlight.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/vkbeautify.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/rest.js"/>"></script>
	
    <script type="text/javascript" src="<spring:url value="/bootstrap.min.js"/>"></script>
	
	<script type="text/javascript">
	
		var snapID = ${snapID};
		var netISO = '${networkISO}';
		
		var InvalidRecordsByFieldBaseURL = "/public/listInvalidRecordsInfoByFieldAndSnapshotID";
		var InvalidRecordsByFieldCountBaseURL = "/public/rejectedFieldCountBySnapshotId";
		var ListOriginsBySnapshotIDBaseURL = "/public/listOriginsBySnapshotID";
		var ListSnapshotsByCountryISOBaseURL =  "/public/listSnapshotsByCountryISO";
		var ListSnapshotsByCountryISOBaseURL =  "/public/listSnapshotsByCountryISO";
		var HarvestMetadataByRecordIDBaseURL = "/public/harvestMetadataByRecordID"; 
		var TransformMetadataByRecordIDBaseURL = "/public/transformRecordByID/";
		var ValidateTransformedRecordByIDBaseURL = "/public/validateTransformedRecordByID/";
		
		function loadRejectedByFieldCount(snapshotID) { 
		
			 $.rest.retrieve(InvalidRecordsByFieldCountBaseURL + "/" + snapID, function(result) {	
				 
				 d3.select("#rejectedByFieldCountPanel").selectAll("div").remove();
				 
				 var div = d3.select("#rejectedByFieldCountPanel").selectAll("div")
				    .data(result)
				  	.enter().append("div")
				  	.attr("class", "col-xs-6 col-sm-3 placeholder")
				  	.attr("onclick",  function(d) { 
				 			var url = InvalidRecordsByFieldBaseURL + "/" + d.field + "/" + snapshotID + "?size=" + 15;
				 			return "loadRejectedByFieldPage('" + url +  "');"; 	 		
				 	}); 
				  	
				  div.append("h4")	
				    .text(function(d) { return d.field; });
				
				  
				  div.append("span")	
				 	.attr("class",  function(d) { if (d.value == 0) return "label label-success"; else return "label label-danger"; }) 
				    .text(function(d) { return d.value + " regs rechazados"; });
			 });		 
			
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
	    
	    function requestAndLoadValidationResult(recordID) {
	    	
	    	serviceURL = ValidateTransformedRecordByIDBaseURL + "/" + recordID;
	    	
	    	$.rest.retrieve(serviceURL, function(result) {	
	    		
	    		
	    		 d3.select("#modalViewValidationResultBody").selectAll("h2").remove();
	    		 d3.select("#modalViewValidationResultBody")
	    		    .append("h2")
	    		    .text("")
	    		    .append("span")
	    		    .attr("class", function (d) { if (result.valid) return "label label-success"; else return "label label-danger"; })
	    		    .text( function (d) { if (result.valid) return "registro válido"; else return "registro inválido"; });
	    		 
	    		 
	    		 // convierte el diccionario en un array
	    		 var fieldResults = $.map(result.fieldResults, function(value, index) { return [value];});
	    		 // ordena los inválidos primero
	    		 ///fieldResults = fieldResults.sort( function(a,b) {return a.fieldName - b.fieldName;} );
	    		 
	    		 d3.select("#modalViewValidationResultBody").selectAll("div").remove();
	    		 var field = d3.select("#modalViewValidationResultBody").selectAll("div")
	    		 	.data(fieldResults)
	    		 	.enter().append("div")
	    		 	.attr("class", function (d) { 
	    		 						if (d.valid) return "alert alert-success"; 
	    		 						else 
	    		 							if (d.mandatory) return "alert alert-danger";
	    		 							else return "alert alert-warning"; 
	    		 	                }) 
	    		    .html(function(d) {return "<b>" + d.fieldName + "</b>"; })
	    		    .append("p").html( function(d) { 
	    		    	
	    		    	// si hay resultados de validación
	    		    	if ( d.results.length > 0 ) {
	    		    	 
		    		    	d.results.sort( function(a,b) { return a.ruleName > b.ruleName;})
		    		    	
		    		    	return $.map(d.results, function(val,index) {  
		    		    		 
		    		    		 var icon = "<span style='margin-right:5px;' class='glyphicon glyphicon-ok'></span>";
		    		    		 if (!val.valid) icon  = "<span style='margin-right:5px;' class='glyphicon glyphicon-remove'></span>";
		    		    		
		    		    	     return icon + val.ruleName + ": " + val.receivedValue;
		    		    	}).join("<br/>");  
	    		    	} else
	    		    		return "<span style='margin-right:5px;' class='glyphicon glyphicon-exclamation-sign'></span> No hay ocurrencias de este campo"
	    		    		
	    		    } );
	    		    
		    	
	    		
	    		
	    		$('#modalViewValidationResult').modal('show');
	    		
	    	});
	    	
	    }
	    
		
		function loadRejectedByFieldPage(pageURL) { 
		
			 $.rest.retrieve(pageURL, function(result) {	
				 
				 d3.select("#rejectedByFieldTable").selectAll("tr").remove();
				
				 var item = d3.select("#rejectedByFieldTable").selectAll("tr")
				    .data(result.content)
				  	.enter().append("tr");
				 
				    item.append("td")
				        .text(function(d) { return d.id; });
				    
				    item.append("td")
				    	.append("button")
				    	.attr("class", "btn btn-primary btn-xs")
				    	.text("ver metadatos")
				    	.attr("onclick", function (d) { 
				    		return  "requestAndLoadMetadata('"+ HarvestMetadataByRecordIDBaseURL + "/"  + d.id + "');"; });
				    	
				    item.append("td")
			        .text(function(d) { 
				       	 switch(d.wasTransformed) {
							 case true:
								 return 'Si';
							   	 break;
							 case false:
								 return 'No';
								 break;
				       	 }
				      });
				    
				    item.append("td")
			    	.append("button")
			    	.attr("class", "btn btn-primary btn-xs")
			    	.text("ver metadatos")
			    	.attr("onclick", function (d) { 
			    		return  "requestAndLoadMetadata('"+ TransformMetadataByRecordIDBaseURL + "/"  + d.id + "');"; });
				    
				    item.append("td")
			    	.append("button")
			    	.attr("class", "btn btn-primary btn-xs")
			    	.text("ver validación")
			    	.attr("onclick", function (d) { return  "requestAndLoadValidationResult(" + d.id + ");"; });
			    	
					    
		
				 var links = result.links.filter( function (link) { return link.rel != 'self'; } ); 
	
				 d3.select("#rejectedByFieldTotalCount").text( "Registros totales: " + result.totalElements );
				 
				 d3.select("#rejectedByFieldPageControls").selectAll("p").remove();
				 d3.select("#rejectedByFieldPageControls").append("p")
				 										  .text( "Página: " + result.number + " de " + result.totalPages + "  " );
				 
				 d3.select("#rejectedByFieldPageControls").selectAll("li").remove();
				 d3.select("#rejectedByFieldPageControls").selectAll("li")
				 				  .data(links)
				 				  .enter().append("li")
				 				  .append('a')
				 		 		  .html( function(d) { 
				 		 			
				 		 			  switch(d.rel) {
										 case 'next':
											 return '&raquo;';  	 
										 case 'previous':
											 return '&laquo;';
										 case 'first':
											 return '&laquo;&laquo;'; 
										 case 'last':
											 return '&raquo;&raquo;';	 
										 default:
											 break;
								 		}
				 		 		   })
				                  .attr("onclick", function(d){ return "loadRejectedByFieldPage('" + d.href +  "');"; });
				 	
			 });
		}
		
		
		function createHarvestingHistoryChart(networkISO) {
				 
			$.rest.retrieve(ListSnapshotsByCountryISOBaseURL + "/" + networkISO, function(result) {	
				
				
				var format = d3.time.format("%Y-%m-%d %H:%M:%S");
				
				var totalArray = result.map( function(d) { return [format.parse(d.startTime),  d.size]; } );
				var validArray = result.map( function(d) { return [format.parse(d.startTime), d.validSize]; } );
				var transformedArray = result.map( function(d) { return [format.parse(d.startTime), d.transformedSize]; } );

				var data = [ { "key":"Cosechados", "values":totalArray  },
				             { "key":"Válidos", "values":validArray},
				             { "key":"Transformados", "values":transformedArray},      
				           ];
				
				
				nv.addGraph(function() {
							
					var chart = nv.models.lineChart()
								  .x(function(d) { return d[0]; })
				    			  .y(function(d) { return d[1];  })
				    			  .color(d3.scale.category10().range())
				    		      .useInteractiveGuideline(true);
	
					chart.xAxis
					  .tickFormat(function(d) {
					    return d3.time.format('%d-%m-%Y')(new Date(d));
					  });
					
					chart.yAxis.tickFormat(d3.format('.d'));
					
					d3.select('#chart svg')
					  .datum(data)
					  .transition().duration(500)
					  .call(chart);
					
					nv.utils.windowResize(chart.update);

					return chart;
				});
			});
		}
		
		
		//createHarvestingHistoryChart(netISO);
				
		loadRejectedByFieldCount(snapID);	
	
	</script>
	
</head>
    
    
    

  <body>

    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Diagnóstico de cosecha</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="#">Ayuda</a></li>
          </ul>
         
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li class="active"><a href="#">Registros inválidos</a></li>
            <li><a href="#">Historial de cosechas</a></li>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h1 class="page-header">Registros rechazados por campo</h1>
          
          <!-- Button trigger modal -->

          <div id="rejectedByFieldCountPanel" class="row placeholders"></div>

          <!-- h3 class="sub-header">Listado de registos rechazados por campo:</h2-->
          <div class="table-responsive">
            <table class="table table-striped">
             
              <thead>
                <tr><td colspan="4"><ul id="rejectedByFieldPageControls" class="pagination"></ul></td></tr>
                <tr>
                  <th width="80">#</th>
                  <th width="60">XML Origen</th>
                  <th width="40">Trasformado</th>
                  <th width="60">XML Cosechado/Transformado</th>
                  <th width="60">Validación</th>
                </tr>
              </thead>
              <tbody id="rejectedByFieldTable"></tbody>
              <tfoot><tr><td colspan="4" id="rejectedByFieldTotalCount"></td></tr></tfoot>
            </table>
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
  
  </body>
</html>





	

<!-- body>



	<div id="chart" style="height:200;">
		  <svg></svg>
	</div> 

	
</body-->
