
angular.module('model.json.schemas', []).service('JSONFormSchemas',  function() {
	
    /** Datos de formulario Network **/
	this.network_schema = {
	    type: "object",
	    properties: {
	      name: { type: "string", minLength: 2, title: "Repositorio", description: "Nombre" },
	      institutionName: { type: "string", minLength: 2, title: "Institución", description: "Nombre Institución" },
	      acronym : { type: "string", minLength: 2, maxLength: 10, title: "Acrónimo", description: "Nombre Institución" },
	      published :{ type: "boolean", title: "¿Es pública?", description: "La red es visible al público" },
	      scheduleCronExpression : { type: "string", title: "Cron de cosecha", description: "La red es visible al público" }
	    }
	 };
	
	this.network_form = [ "*", { type: "submit", title: "Guardar cambios" } ];
	
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
				 
		 },
		"required": [
		  "name",
		  "uri",
		  "metadataPrefix"
		]
	};
			  
	
	this.origin_form = [ "*", { type: "submit", title: "Guardar cambios" } ];
	
	
	

	

});