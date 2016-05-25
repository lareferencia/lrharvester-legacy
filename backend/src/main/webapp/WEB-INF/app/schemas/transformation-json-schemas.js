angular.module('transformation.json.schemas', []).service('JSONTransformationSchemas',  function() {
	
	
	this.transformer_form = [   
	  { "type": "section",
 	    "htmlClass": "row",
	    "items": [
	      {
	        "type": "section",
	        "htmlClass": "col-xs-6",
	        "items": [
	          "name"
	        ]
	      },
	      {
	        "type": "section",
	        "htmlClass": "col-xs-6",
	        "items": [
	          "description"
	        ]
	      },
	 
	    ]
	  },
	  
	  { type: "submit", title: "Guardar" }
    
   ];




	this.transformer_schema = {
		type: "object",
		properties: {
			name : { type: "string", title: "Nombre", description: "" }, 
			description : { type: "string", title: "Descripción", description: "" },
		}
	};
	
	
	this.rule_data_form = [ "name", "description"];
	
	this.rule_data_schema = {
			type: "object",
		    properties: {
		      name :{ type: "string", title: "Nombre", description: "" }, 
		      description :{ type: "string", title: "Description", description: "" }
		    }
	};
	
	
	
	var _RULES = [  
	
	{ 		
			name : "Traducción de valores de campos",
			className: "org.lareferencia.backend.validation.transformer.FieldContentTranslateRule",
			form: [ 
			        "testFieldName", "writeFieldName", "replaceOccurrence", "testValueAsPrefix", "translationArray", { type: "submit", title: "Guardar cambios" }],
			schema: {
				type: "object",
				    properties: {
				    translationArray: { type: "array", title: "Listado de traducciones", description: "Si se encuentra una ocurrencia con alguno de los valores listado se reemplaza.", 
				    	items: {
				            "type": "object",
				            "title": "Traducción",
				            "properties": {
				              "search": { "type": "string", "title": "Buscar" },
				              "replace": { "type": "string", "title": "Reemplazo" },	
				            }
				    	}
				    },
				    testFieldName:{ type: "string", title: "Campo de búsqueda", description: "El nombre del campo oai_dc donde se buscara el valor. Ej: dc:type " },
				    writeFieldName:{ type: "string", title: "Campo de escritura", description: "El nombre del campo oai_dc que se creará con la ocurrencia de reemplazo Ej: dc:type " },
				    replaceOccurrence:{ type: "boolean", title: "¿Se reemplaza la ocurrencia encontrada?", description: "Indica si se eliminará la ocurrencia en el campo de búsqueda" },
				    testValueAsPrefix:{ type: "boolean", title: "¿Evaluar como prefijo prefijo?", description: "Indica si el valor de búsqueda se evaluará como prefijo del contenido del campo de búsqueda." }, 
				    	
					
				    }
				} /* fin schema */
	},
	
	{ 		
		name : "Traducción de nombres de campo",
		className: "org.lareferencia.backend.validation.transformer.FieldNameTranslateRule",
		form: [ 
		        "sourceFieldName", "targetFieldName", { type: "submit", title: "Guardar cambios" }],
		schema: {
			type: "object",
			    properties: {
			    	sourceFieldName:{ type: "string", title: "Campo origen", description: "El nombre del campo oai_dc origen. Ej: dc:type.calif " },
			    	targetFieldName:{ type: "string", title: "Campo destino", description: "El nombre del campo oai_dc de reemplazo Ej: dc:type " },
			    }
		} /* fin schema */
	},
	
	{ 		
		name : "Agregado de metatos de nombre repositorio",
		className: "org.lareferencia.backend.validation.transformer.AddRepoNameRule",
		form: [ "doRepoNameAppend","doRepoNameReplace","repoNameField","repoNamePrefix",
		        "doInstNameAppend","doInstNameReplace","instNameField","instNamePrefix", 
		        { type: "submit", title: "Guardar cambios" }
		],
		schema: {
			type: "object",
			    properties: {
			    	doRepoNameAppend:{ type: "boolean", title: "¿Agregar ocurrencia con nombre del repositorio?", description: "" },
			    	doRepoNameReplace:{ type: "boolean", title: "¿Se reemplaza las ocurrencia existentes de nombre de repositorio?", description: "" },
			    	repoNameField:{ type: "string", title: "Campo utilizado para nombre de repositorio", description: "Ej: dc:source" },
			    	repoNamePrefix:{ type: "string", title: "Prefijo utilizado para nombre de repositorio", description: "Ej: reponame" },
			    	doInstNameAppend:{ type: "boolean", title: "¿Agregar ocurrencia con nombre de la institución?", description: "" },
			    	doInstNameReplace:{ type: "boolean", title: "¿Se reemplaza las ocurrencia existentes de nombre de la institución?", description: "" },
			    	instNameField:{ type: "string", title: "Campo utilizado para nombre de la institución", description: "Ej: dc:source" },
			    	instNamePrefix:{ type: "string", title: "Prefijo utilizado para nombre de la institución", description: "Ej: instname" },
			    	
			    	
			    }
		} /* fin schema */
	},
	
	
	
]; //***** fin de _RULES ******/// 
	


	/** mapeo de las definiciones de reglas a un objeto **/
	var rules_defs_by_class  =  {}
	$.map( _RULES, function(rule, i ) { rules_defs_by_class[rule.className] = rule; });
	
	/** export del objeto con las definiciones de reglas **/
	this.ruleDefinitionByClassName = rules_defs_by_class;

	
	
});