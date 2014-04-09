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
    <link rel="stylesheet" href="<spring:url value="/static/css/diagnose.css"/>" >
	
	<script type="text/javascript" src="<spring:url value="/d3.v3.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/nv.d3.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/rest.js"/>"></script>
	
    <script type="text/javascript" src="<spring:url value="/bootstrap.min.js"/>"></script>
	
	<script type="text/javascript">
	
		var snapID = ${snapID};
		var netISO = '${networkISO}';
		var mainOriginURL = ""; // to be updated by ws
		
		var InvalidRecordsByFieldBaseURL = "/public/listInvalidRecordsInfoByFieldAndSnapshotID";
		var InvalidRecordsByFieldCountBaseURL = "/public/rejectedFieldCountBySnapshotId";
		var ListOriginsBySnapshotIDBaseURL = "/public/listOriginsBySnapshotID";
		var ListSnapshotsByCountryISOBaseURL =  "/public/listSnapshotsByCountryISO";
		
		
		function loadMainOriginURL(snapshotID) { 
			 $.rest.retrieve(ListOriginsBySnapshotIDBaseURL + "/" + snapID, function(result) {	
				 mainOriginURL = result[0].uri;
			 });
		}
		
		
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
				 	.attr("class",  "text-muted")
				    .text(function(d) { return d.value + " regs rechazados"; });

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
				    	.attr("data-toggle","modal")
				    	.attr("data-targe","#modalViewMetadatal")
				    	.text("Metadata Origen")
				    	.attr("onclick", function (d) { 
				    		
				    		var xmlMetadataURL = mainOriginURL + "?verb=GetRecord&metadataPrefix=oai_dc&identifier=" + d.identifier;
				    	
				    		
				    		$.ajax({
			    	            dataType : "xml",
			    	            url : mainOriginURL,
			    	            success : function(results) {
			    	            	$( '#modalViewMetadataBody' ).html(results);
			    	            }
			    	        });
				    		
				    		return "$( '#modalViewMetadataBody' ).load('" + xmlMetadataURL +  "', function() {$('#modalViewMetadata').modal('show'); });"
				    	
				    		 
				    		
				    		
				    		
				    	});
				    	
			        	/*.attr("href", function(d) { return mainOriginURL + "?verb=GetRecord&metadataPrefix=oai_dc&identifier=" + d.identifier; });*/
				    
				    
				
				    
				 var links = result.links.filter( function (link) { return link.rel != 'self'; } ); 
				 links = links.map( function(link) { 
					 
					 switch(link.rel) {
						 case 'next':
							 link.rel = 'Próxima';
						   	 break;
						 case 'previous':
							 link.rel = 'Anterior';
							 break;
						 case 'first':
							 link.rel = 'Primera';
						   	 break;
						 case 'last':
							 link.rel = 'Última';	 
						     break; 
						 default:
							 break;
						   
					 }
					 
					 return {"rel":link.rel, "href":link.href}; } );
				 
				 d3.select("#rejectedByFieldTotalCount").text( "Registros totales: " + result.totalElements );
				 
				 d3.select("#rejectedByFieldPageControls").selectAll("p").remove();
				 d3.select("#rejectedByFieldPageControls").append("p")
				 										  .text( "Pagina: " + result.number + " de " + result.totalPages + "  " );
				 
				 d3.select("#rejectedByFieldPageControls").selectAll("button").remove();
				 d3.select("#rejectedByFieldPageControls").selectAll("button")
				 				  .data(links)
				 				  .enter().append("button")
				 		 		  .text( function(d) { return d.rel; })
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
		
		loadMainOriginURL(snapID);
		
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
            <li><a href="#">Dashboard</a></li>
            <li><a href="#">Settings</a></li>
            <li><a href="#">Profile</a></li>
            <li><a href="#">Help</a></li>
          </ul>
         
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li class="active"><a href="#">Registros inválidos</a></li>
            <li><a href="#">Reports</a></li>
            <li><a href="#">Analytics</a></li>
            <li><a href="#">Export</a></li>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h1 class="page-header">Registros rechazados por campo</h1>
          
          <!-- Button trigger modal -->
		



          <div id="rejectedByFieldCountPanel" class="row placeholders"></div>

          <h2 class="sub-header">Listado de registos rechazados por campo:</h2>
          <div class="table-responsive">
            <table class="table table-striped">
             
              <thead>
                <tr id="rejectedByFieldPageControls" ></tr>
                <tr>
                  <th>#</th>
                  <th>XML Origen OAI</th>
                  <th>XML Cosechado/Transformado</th>
                  <th>Validación</th>
                </tr>
              </thead>
              <tbody id="rejectedByFieldTable"></tbody>
              <tfoot><tr><td id="rejectedByFieldTotalCount"></td></tr></tfoot>
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
	      <div id="modalViewMetadataBody" class="modal-body" >
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
