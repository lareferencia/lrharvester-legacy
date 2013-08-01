<h1>{translate text="Impacto"}</h1>  
<div class="span-18 last">
  <h1>{translate text="Per&uacute;"}</h1>
  <h2>{translate text="Distribuci&oacute;n de Material"}</h2>
 Gr&aacute;fico que representa la distribuci&oacute;n de los tipos de materiales mas usados para el pa&iacute;s integrante de LA Referencia. 

     	{* Load graphic library *}{js filename="flotr2.min.js"}ript>
  <div class="clear"></div>
  {literal}
       <style type="text/css">

      #container {
        width : 600px;
        height: 300px;
        margin: 8px 100px;
      }

    </style>
  	{/literal} 
  <div class="span-5" id="container"></div> 
    <script type="text/javascript">
{literal}		
		function addCommas(nStr)
		{
		nStr += '';
		x = nStr.split('.');
		x1 = x[0];
		x2 = x.length > 1 ? '.' + x[1] : '';
		var rgx = /(\d+)(\d{3})/;
		while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
		}
		return x1 + x2;
		}
		
		(
		
		function basic_pie(container) {
		var 
{/literal} 
{$list}
{literal}	graph;

graph = Flotr.draw(container, [ 
{/literal}
{$table}
{literal}	],
			 {
			HtmlText : true,
			grid : {
			  verticalLines : false,
			  horizontalLines : false
			},
			xaxis : { showLabels : false },
			yaxis : { showLabels : false },
			pie : {
			  show : true,
				labelFormatter: function(total, value) {                            
                            return addCommas(value);
                          },			  
			  explode : 10
			},
			 legend : { position : "so"},
		  });
		})
		( container=document.getElementById("container")
		);
{/literal} 
</script>
<div class="clear"></div>
<h2 class="span-10"><a id="materias"></a>Impacto por Tipo de Material para Per&uacute;</h2> <br/>
	{$output8}	
<div class="clear"></div>