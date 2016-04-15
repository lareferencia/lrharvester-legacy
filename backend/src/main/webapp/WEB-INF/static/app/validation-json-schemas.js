angular.module('validation.json.schemas', []).service('JSONValidationSchemas',  function() {
	
	
	this.rule_data_form = [ "*" ];
	
	this.rule_data_schema = {
			type: "object",
		    properties: {
		      name :{ type: "string", title: "Nombre", description: "" }, 
		      description :{ type: "string", title: "Description", description: "" } 
		    }
	};
	
	
	var base_rule_schema_properties =  {
		      fieldname :{ type: "string", title: "Campo", description: "El nombre del campo oai_dc. Ej: dc:type " }, 
		      mandatory :{ type: "boolean", title: "¿Es obligatoria?", description: "La regla es determinante en la validez de registro" },
		      quantifier: {
		          title: "Cuantificador",
		          type: "string",
		          enum: [ "ZERO_ONLY", "ONE_ONLY", "ZERO_OR_MORE", "ONE_OR_MORE", "ALL"],
		          description: "¿Cuántas ocurrencias deben cumplir la regla?"
		      }
	};
	
	var base_rule_form = [ "fieldname",
	         			   "mandatory",
        			        { key: "quantifier", type: "select",
        			            "titleMap": {
        			            	"ZERO_ONLY" : "Ninguna", 
        			            	"ONE_ONLY" : "Una y sólo una",
        			            	"ZERO_OR_MORE" : "Cero o más", 
        			            	"ONE_OR_MORE" : "Al menos una", 
        			            	"ALL" : "Todas"
        			            }
        			        }];
	
	
	var controlled_value_rule = { 
			name : "Validación por valores controlados",
			form: [ { "type": "help", "helpvalue": "Esta regla es válida si el campo contiene occurrencias con estos valores resulta válido</p>"}, 
			        "controlledValues", { type: "submit", title: "Guardar cambios" }],
			schema: {
				type: "object",
				    properties: {
				      controlledValues : {
				          type: "array",
				          title: "Valores Controlados", 
				          items: { "type": "string", "title":"valor" }
				      }
				    }
				} /* fin schema */
	};
	
	$.extend(controlled_value_rule.schema.properties, base_rule_schema_properties);
	controlled_value_rule.form = base_rule_form.concat(controlled_value_rule.form);
	
	
	var large_controlled_value_rule = {
		name: "Validación por valores controlados (large)",
		form: [ { "type": "help", "helpvalue": "Esta regla es válida si el campo contiene occurrencias con estos valores resulta válido</p>"}, 
		        "controlledValuesCSV", { type: "submit", title: "Guardar cambios" }],
		schema: {
			    type: "object",
			    properties: {
			      controlledValuesCSV : {
			          type: "string",
			          title: "CSV Valores Controlados", 
			          description: "Texto con valores separados por ; sin espacios" 
			      }
			    }
		 } /* fin schema */
	};
	
	$.extend(large_controlled_value_rule.schema.properties, base_rule_schema_properties);
	large_controlled_value_rule.form = base_rule_form.concat(large_controlled_value_rule.form);
	
	
	var value_length_rule = { 
			name : "Validación por longitud de contenido",
			form: [ { "type": "help", "helpvalue": "Esta regla es válida si el campo contiene occurrencias de longitud entre un mínimo y un máximo</p>"}, 
			        "minLength", "maxLength", { type: "submit", title: "Guardar cambios" }],
			schema: {
				type: "object",
				    properties: {
				    	minLength :{ type: "integer", title: "Longitud mínima", description: "La longitud mímina aceptada" }, 
				    	maxLength :{ type: "integer", title: "Longitud máxima", description: "La longitud máxima aceptada" }, 
				    }
				} /* fin schema */
	};
	
	$.extend(value_length_rule.schema.properties, base_rule_schema_properties);
	value_length_rule.form = base_rule_form.concat(value_length_rule.form);
	
	
	
	var value_regex_rule = { 
			name : "Validación por longitud de contenido",
			form: [ { "type": "help", "helpvalue": "Esta regla es válida si el campo contiene occurrencias de longitud entre un mínimo y un máximo</p>"}, 
			        "regexString", { type: "submit", title: "Guardar cambios" }],
			schema: {
				type: "object",
				    properties: {
				    	regexString : {
					          type: "string",
					          title: "Expresión regular", 
					          description: "Expresión regular que debe ser válida para la ocurrencia" 
					      }
				    }
				} /* fin schema */
	};
	
	$.extend(value_regex_rule.schema.properties, base_rule_schema_properties);
	value_regex_rule.form = base_rule_form.concat(value_regex_rule.form);


	
	this.getRuleByClass  = function(classname) {
		
		switch (classname) {
		
		case "org.lareferencia.backend.validation.validator.ControlledValueFieldContentValidatorRule":
			return controlled_value_rule; 
			break;
			
		case "org.lareferencia.backend.validation.validator.LargeControlledValueFieldContentValidatorRule":
			return large_controlled_value_rule; 
			break;
	
		case "org.lareferencia.backend.validation.validator.ContentLengthFieldContentValidatorRule":
			return value_length_rule; 
			break;
			
		case "org.lareferencia.backend.validation.validator.RegexFieldContentValidatorRule":
			return value_regex_rule; 
			break;
			

		default:
			break;
		};	
	};
	

	
});