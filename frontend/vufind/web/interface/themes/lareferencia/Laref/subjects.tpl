<div class="span-12" >
<h1 >Materias</h1>
<div class="span-12" style="word-wrap:break-word;">
{$output}
</div>
</div>
<div class="span-10 last">
  <h1>{translate text="Recursos en la colecci&oacute;n"}</h1>
  <table class="citation">
  <tr>
    <th>{translate text="Tipo de Material"}</th>
	</tr>
  <tr>
    <td>
      <ul>
      {foreach from=$typeList item=term}
        <li><a href="{$url}/Search/Results?lookfor=&type=AllFields&filter%5B%5D=type%3A%22{$term.0|escape}%22">{$term.0|escape}</a> - {$term.1|number_format:0:".":","}</li>
      {assign var="sum2" value="`$sum2+$term.1`"}
	  {/foreach}
	  <li>Total - {$sum2|number_format:0:".":","}</li>
      </ul>
    </td>
	</tr>
  </table> 
  {literal}
       <style type="text/css">

      #container {
        width : 400px;
        height: 300px;
        margin: 1px auto;
      }

    </style>
  	{/literal} 
  <div class="span-5" id="container"></div> 
   <script type="text/javascript" src="{$url}/flotr2.min.js"></script>
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
{foreach from=$typeList item=term name=ciclo}
        {literal}d{/literal}{$smarty.foreach.ciclo.index}{literal}=[[0,{/literal}{$term.1}{literal}]],{/literal}
      {/foreach}
{literal}	graph;

graph = Flotr.draw(container, [ 
{/literal}
{foreach from=$typeList item=term name=ciclo}
        {literal}{ data:d{/literal}{$smarty.foreach.ciclo.index}{literal},label:"{/literal}{$term.0}{literal}"},{/literal}
			{/foreach}
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
</div>


	<div class="clear"></div>
