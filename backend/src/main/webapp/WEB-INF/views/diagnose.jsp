<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ page session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>Diagnóstico de cosecha</title>
	<link rel="stylesheet" href="<spring:url value="/static/css/smoothness/jquery-ui.css"/>"></link>
	<link rel="stylesheet" href="<spring:url value="/static/css/jquery-select.css"/>"></link>
	
	<script type="text/javascript" src="<spring:url value="/d3.v3.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery-ui.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/jquery-select.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/rest.js"/>"></script>
	
	
	<script type="text/javascript">
	
		var snapID = <%= request.getParameter("snapID") %>;
		var mainOriginURL = ""; // to be updated by ws
		
		var InvalidRecordsByFieldBaseURL = "/public/listInvalidRecordsInfoByFieldAndSnapshotID";
		var InvalidRecordsByFieldCountBaseURL = "/public/rejectedFieldCountBySnapshotId";
		var ListOriginsBySnapshotIDBaseURL = "/public/listOriginsBySnapshotID";
		
		
		function loadMainOriginURL(snapshotID) { 
			 $.rest.retrieve(ListOriginsBySnapshotIDBaseURL + "/" + snapID, function(result) {	
				 mainOriginURL = result[0].uri;
			 });
		}
		
		
		function loadRejectedByFieldCount(snapshotID) { 
		
			 $.rest.retrieve(InvalidRecordsByFieldCountBaseURL + "/" + snapID, function(result) {	
				 
				 d3.select("#rejectedByFieldCountPanel").selectAll("button").remove();
				 
				 d3.select("#rejectedByFieldCountPanel").selectAll("button")
				    .data(result)
				  	.enter().append("button")
				    .text(function(d) { return d.field + " (" + d.value + ")";})
				 	.attr("onclick",  function(d) { 
				 			var url = InvalidRecordsByFieldBaseURL + "/" + d.field + "/" + snapshotID + "?size=" + 30;
				 			return "loadRejectedByFieldPage('" + url +  "');"; 	 		
				 	}); 
			 });
		}
		
		function loadRejectedByFieldPage(pageURL) { 
		
			 $.rest.retrieve(pageURL, function(result) {	
				 
				 d3.select("#rejectedByFieldPanel").selectAll("li").remove();
				 
				 
				 var item = d3.select("#rejectedByFieldPanel").selectAll("li")
				    .data(result.content)
				  	.enter().append("li");
				 
				    item.append("span")
				        .text(function(d) { return d.id; });
				    
				    item.append("span")
				    	.append("a")
				    	.text("Registro en Origen")
				    	.attr("target","_blank")
			        	.attr("href", function(d) { return mainOriginURL + "?verb=GetRecord&metadataPrefix=oai_dc&identifier=" + d.identifier; });
				    
				 
				 d3.select("#rejectedByFieldPageControls").selectAll("button").remove();
				 d3.select("#rejectedByFieldPageControls").selectAll("button")
				 				  .data(result.links)
				 				  .enter().append("button")
				 		 		  .text( function(d) { return d.rel; })
				                  .attr("onclick", function(d){ return "loadRejectedByFieldPage('" + d.href +  "');"; });
				 	
			 });
		}
		
		
		
		loadMainOriginURL(snapID);
		loadRejectedByFieldCount(snapID);
	
	</script>
	
</head>

<body>

		


	<div id="title">
		<span>Administración backend</span>
		<a  href="./logout">logout</a>
	</div> 
	
	<ul id="rejectedByFieldCountPanel"></ul>
	<ul id="rejectedByFieldPanel"></ul>
	<p id="rejectedByFieldPageControls"></p>
	
	
	<br/>
	
</body>
</html>
