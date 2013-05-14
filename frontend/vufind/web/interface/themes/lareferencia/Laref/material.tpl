

<div class="span-18 last">
  <h1>{translate text="Material"}</h1>
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
</div>
<div class="clear"></div>
  {literal}
       <style type="text/css">

      #container {
        width : 500px;
        height: 400px;
        margin: 8px auto;
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
	<div class="clear"></div>
<h2 class="span-10"><a id="materias"></a>Materias </h2> 
	<div class="clear"></div>
	{$output4}
<h2 class="span-10"><a id="instituciones"></a>Material <br/>por Red</h2> 
<div class="clear"></div>
	{$output2}

<div class="clear"></div>