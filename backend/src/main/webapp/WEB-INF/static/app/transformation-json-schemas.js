angular.module('transformation.json.schemas', []).service('JSONTransformationSchemas',  function() {
	
	
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
			        "*", { type: "submit", title: "Guardar cambios" }],
			schema: {
				type: "object",
				    properties: {
				    translationArray: { type: "array", title: "translationArray", description: "El nombre del campo oai_dc. Ej: dc:type ", 
				    	items: {
				            "type": "object",
				            "properties": {
				              "search": { "type": "string" },
				              "replace": { "type": "string" },	
				            }
				    	}
				    },
				    testFieldName:{ type: "string", title: "testFieldName", description: "El nombre del campo oai_dc. Ej: dc:type " },
				    writeFieldName:{ type: "string", title: "writeFieldName", description: "El nombre del campo oai_dc. Ej: dc:type " },
				    replaceOccurrence:{ type: "boolean", title: "replace", description: "El nombre del campo oai_dc. Ej: dc:type " },
				    testValueAsPrefix:{ type: "boolean", title: "prefix", description: "El nombre del campo oai_dc. Ej: dc:type " }, 
				    	
					
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