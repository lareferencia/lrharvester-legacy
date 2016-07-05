
angular.module('network.json.schemas', []).service('JSONNetworkSchemas',  function() {
	
    /** Datos de formulario Network **/
	this.network_schema = {
	    type: "object",
	    properties: {
	      name: { type: "string", minLength: 2, title: "Repositorio", description: "Nombre de repositorio", required: true },
	      institutionName: { type: "string", minLength: 2, title: "Institución", description: "Nombre Institución", required: true},
	      acronym : { type: "string", minLength: 2, maxLength: 10, title: "Acrónimo", description: "Acrónimo identificatorio del repositorio (debe ser único)", required: true },
	      published :{ type: "boolean", title: "¿Es pública?", description: "La red es visible al público" },
	      scheduleCronExpression : { type: "string", title: "Cron de cosecha", description: "La red es visible al público", "default": "* 0 0 30 2 *" }
	    }
	 };
	
	this.network_form = [ "*" ];
	
	this.network_validation_schema = {
		 type: "object",
		    properties: {
		      validator: { type: "string", title: "Validador", description: "" },
		      transformer: { type: "string", title: "Transformador", description: "" },
		    }	
	};
	
	this.network_validation_form =  function(validatorsArray, transformersArray) {

		validatorTitleMap = {};
		transformerTitleMap = {};
		
		for (var i=0;i<validatorsArray.length;i++) {
			var validator = validatorsArray[i];
			validatorTitleMap[ validator._links.self.href ] = validator.name;
		}
		
		for (var i=0;i<transformersArray.length;i++) {
			var transformer = transformersArray[i];
			transformerTitleMap[ transformer._links.self.href ] = transformer.name;
		}
		
		
		return [ { key: "validator", type: "select", "titleMap": validatorTitleMap },
		         { key: "transformer", type: "select", "titleMap": transformerTitleMap },
		       ];
		
	};
	
	this.buildTitleMap =  function(keyName, objArray) {

		objTitleMap = {};
		
		for (var i=0;i<objArray.length;i++) {
			var obj = objArray[i];
			objTitleMap[ obj._links.self.href ] = obj.name;
		}
	
		return  { "key": keyName, type: "select", "titleMap": objTitleMap };
		       
	};
	      	                       
	                         	    
	
	
    /** Datos de formulario Network Properties **/
	
	this.generate_network_properties_schema = function(propertiesArray) {
		
		var schema = { type: "object", properties: {} };
		
		for (var i=0;i<propertiesArray.length;i++) {
			var property = propertiesArray[i];
			schema.properties[property.name] = { type: "boolean", title: property.name, description:property.description};
		}
		
		return schema;
	};
	
	this.generate_network_properties_model = function(propertiesArray) {
		
		var model = {};
		
		for (var i=0;i<propertiesArray.length;i++) {
			var property = propertiesArray[i];
			model[property.name] = property.value;
		}
		
		return model;
	};
		
	
	this.network_properties_form = [ "*", { type: "submit", title: "Guardar cambios" } ];
	
    /** Datos de formulario de origenes **/
	
	this.origin_schema =
	{
		 "type": "object",
		 "title": "Orígenes",

		"properties": {
			  "name": {
				"title": "Nombre",
				"type": "string",
				"default": "Main"	
			  },
			  "uri": {
				"title": "URI",
				"type": "string",
				/*"pattern": "^\\S+@\\S+$",*/
				"description": "La URL del origen a cosechar"
			  },
			  "metadataPrefix": {
				"title": "MetadataPrefix",
				"type": "string",
				"enum": ["oai_dc"],
				"default": "oai_dc"
			  },
			  "form_sets": { 
				  type: "array", 
				  title: "Listado de sets", 
				  description: "Puede agregar aquí los sets específicos a cosechar", 
			    	items: {
			            "type": "object",
			            "title": "Set",
			            "properties": {
			              "name": { "type": "string", "title": "Nombre" },
			              "spec": { "type": "string", "title": "SetSpec" },	
			            }
			    	}
			    },
				 
		 },
		"required": [
		  "name",
		  "uri",
		  "metadataPrefix"
		]
	};
			  
	
	this.origin_form = [   { "type": "section",
   	    "htmlClass": "row",
  	    "items": [
  	      {
  	        "type": "section",
  	        "htmlClass": "col-xs-6",
  	        "items": [
  	          "name", "uri", "metadataPrefix", { type: "submit", title: "Guardar" }
  	        ]
  	      },
  	      {
  	        "type": "section",
  	        "htmlClass": "col-xs-6",
  	        "items": [
  	          "form_sets"
  	        ]
  	      },
  	 
  	    ]
  	  },
  	  
  	  
  	 ];
	
	

});