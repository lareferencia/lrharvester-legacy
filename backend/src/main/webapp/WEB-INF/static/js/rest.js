

/**
 * Resetea un form
 */
$.fn.resetForm = function () {
  $(this).each (function() { this.reset(); });
};


/**
 * Obtiene el Json de un form
 */
$.fn.toJson = function()
{
    var form = $(this);
    
    var jsonResult = {};

    /* Get input values from form */
    var values = this.serializeArray();
    
    /* Because serializeArray() ignores unset checkboxes and radio buttons: */
    values = values.concat(
    		$('input[type=checkbox]:not(:checked)', form).map(function() {return {"name": this.name, "value": "0"};}).get()
    );
	
    $.each(values, function() {
    	    	
	    var $ctrl = $('[name='+this.name+']', form);  
	    
	    if ( $ctrl.attr("type") == 'checkbox' ) {
	    	if ( this.value == '1' ) 
	    		this.value = true;
	    	if ( this.value == '0' ) 
	    		this.value = false;
	    }
    	
        jsonResult[this.name] = this.value;

    });   
    
    return jsonResult;
};

/**
 * LLena un form con datos json
 */
$.fn.fromJson = function(data) {
	
	this.resetForm();

	var form = $(this);
	
    $.each(data, function(key, value) {  
	    var $ctrl = $('[name='+key+']', form);  
	    switch($ctrl.attr("type"))  
	    {  
	        case "text" :   
	        case "hidden":  
	        case "textarea":  
	        	$ctrl.val(value);   
	        break;   
	        
	        case "radio" : case "checkbox":
	        	$ctrl.attr("checked", value); 
	        break;  
	    }  
    });   
};

$.rest = {
		
	/**
	Obtiene el link según rel de una entidad de acuerdo al standard de HATEOAS 
	*/
	relLink: function (entity, rel) {
		
		return entity.links.filter( function(e) { return e.rel == rel; } )[0].href;
	},
	
	/**
	Obtiene el id del link asumiendo que el link termina con /{id} 
	*/
	link2id: function (link) {
		
		var splitted_link = link.split('/');
		return splitted_link[splitted_link.length-1];
	},
	
	retrieve : function(entity_url, success_handler, error_handler) {
		
		var isAsync = true;
		if ( success_handler == null ) {
			isAsync = false;
		}
		
		var result = $.ajax({
	        type: 'GET',
	        url:  entity_url,
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json',
	        async: isAsync,
	        success: success_handler,
	        accepts: {json:'application/json'},
	        error: error_handler
	        
	    }).responseJSON;
		
		return result;
	},
	

	retrieve_full : function(entity_url, success_handler, error_handler) {
		
		var isAsync = true;
		
		if ( success_handler == null ) {
			isAsync = false;
		}
		
		var result = $.ajax({
	        type: 'GET',
	        url:  entity_url,
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json',
	        async: isAsync,
	        success: success_handler,
	        accepts: {json:'application/x-spring-data-compact+json'},
	        error: error_handler
	        
	    }).responseJSON;
		
		for (var i=0;i<result.links.length; i++) {
			result.content.push( $.rest.retrieve(result.links[i].href) );			  
		}
		
		return result;
	},
	
	
	
	retrieve_compact : function(entity_url, success_handler, error_handler) {
		
		var isAsync = true;
		
		if ( success_handler == null ) {
			isAsync = false;
		}
		
		var result = $.ajax({
	        type: 'GET',
	        url:  entity_url,
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json',
	        async: isAsync,
	        success: success_handler,
	        accepts: {json:'application/x-spring-data-compact+json'},
	        error: error_handler
	        
	    }).responseJSON;
		
		return result;
	},
	
	create : function(entity_url, data, success_handler, error_handler) {
		
		var isAsync = true;
		
		if ( success_handler == null ) {
			isAsync = false;
		}
		
		$.ajax({
	        type: 'POST',
	        url:  entity_url,
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json',
	        data: JSON.stringify(data),
	        async: isAsync,
	        success: success_handler,
	        accepts: {json:'application/json'},
	        error: error_handler
	        
	    });
	},
	
	update: function(entity_url, data, success_handler, error_handler) {
		$.ajax({
	        type: 'PUT',
	        url: entity_url,
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json',
	        data: JSON.stringify(data),
	        async: true,
	        success: success_handler,
	        accepts: {json:'application/json'},
	        error: error_handler
	        
	    });
	},
	
	destroy: function(entity_url, success_handler, error_handler) {
		$.ajax({
	        type: 'DELETE',
	        url:  entity_url,
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json',
	        async: true,
	        success: success_handler,
	        accepts: {json:'application/json'},
	        error: error_handler
	        
	    });
	}
}

