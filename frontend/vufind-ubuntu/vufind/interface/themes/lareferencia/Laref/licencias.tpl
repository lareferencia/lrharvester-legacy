<h1>{translate text="Licencias"}</h1>  
<div class="span-18 last">
  <h2>{translate text="Distribuci&oacute;n de Licencias"}</h2>
 Gr&aacute;fico que representa la distribuci&oacute;n de los tipos de licencias mas usados en LA Referencia. 

     	{* Load graphic library *}{js filename="flotr2.min.js"}
  <div class="clear"></div>
  {literal}
       <style type="text/css">

      #container {
        width : 800px;
        height: 300px;
        margin: 8px 10px;
      }

		.flotr-grid-label {
			font-size: 12px;
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
		
(function basic(container) {

  var
    barData = [],
    barTicks = [],
    barLabels = [],
    i, graph;
{/literal} 
{$output2}
 {literal}   
  drawBars(container, barData,  barTicks, barLabels);                    
})(document.getElementById("container"));
    
  function drawBars(container, data, barTicks, barlabels) {  
  // Draw Graph
  Flotr.draw(
        container,
        [data],
        {
            colors: ['#EF5205'],
            htmlText: true,
            fontSize: 12,
            grid: {
                outlineWidth: 1,
                outline: 'ws',
                horizontalLines: false,
                verticalLines: true
            },
            bars: {
                show: true,
                horizontal: true,
                shadowSize: 0,
                barWidth: 0.5,
                fillOpacity: 1
            },
            mouse: {
                track: true,
                relative: true,
                trackFormatter: function (pos) {
                    var ret;
                    $.each(barlabels, function (k, v) {
                        if (v[0] == pos.y) {
                            ret = v[1];
                        };
                    });
                    return ret
                }
            },
            xaxis: {
                min: 0,
                max: 20000,
                margin: true,
                labelsAngle: 90,
                tickDecimals: 0
                
            },
            yaxis: {
                ticks: barTicks
            }
        }
      );
   }
{/literal} 
</script>
<div class="clear"></div>
<h2 class="span-10"><a id="materias"></a>Licencias por Pa&iacute;s</h2> <div class="clear"></div>
	{$output}	
<div class="clear"></div>
