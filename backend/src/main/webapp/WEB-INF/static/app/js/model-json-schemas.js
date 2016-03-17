
angular.module('model.json.schemas', []).service('JSONFormSchemas',  function() {
	
    /** Datos de formulario Network **/
	this.network_schema = {
	    type: "object",
	    properties: {
	      name: { type: "string", minLength: 2, title: "Repositorio", description: "Nombre" },
	      institutionName: { type: "string", minLength: 2, title: "Institución", description: "Nombre Institución" },
	      acronym : { type: "string", minLength: 2, title: "Acrónimo", description: "Nombre Institución" },
	      published :{ type: "boolean", title: "¿Es pública?", description: "La red es visible al público" },
	      runIndexing : { type: "boolean", title: "¿Indexar?", description: "La red es visible al público" },
	      runValidation : { type: "boolean", title: "¿Validar?", description: "La red es visible al público" },
	      runTransformation: { type: "boolean", title: "¿Transformar?", description: "La red es visible al público" },
	      runXOAI : { type: "boolean", title: "¿XOAI?", description: "La red es visible al público" },
	      scheduleCronExpression : { type: "string", title: "Cron de cosecha", description: "La red es visible al público" }
	    }
	 };
	
	this.network_form = [ "*", { type: "submit", title: "Guardar cambios" } ];

});