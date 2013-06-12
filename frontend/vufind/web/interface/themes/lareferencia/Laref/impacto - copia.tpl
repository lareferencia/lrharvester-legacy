    <script type='text/javascript' src='https://www.google.com/jsapi'></script>
    <script type='text/javascript'>
	{literal} 
     google.load('visualization', '1', {'packages': ['geochart']});
     google.setOnLoadCallback(drawMarkersMap);

      function drawMarkersMap() {
      var data = google.visualization.arrayToDataTable([
	  {/literal} 
{$list}
{literal} 
 var options = {
        region: 'world',
        displayMode: 'regions',
        colorAxis: {colors: ['blue']}
      };

      var chart = new google.visualization.GeoChart(document.getElementById('chart_div'));
      chart.draw(data, options);
    };
	{/literal} 
    </script>
   <div id="chart_div" style="width: 700px; height: 400px; margin:auto;"></div>
<h2 class="span-10"><a id="materias"></a>Impacto por Pa&iacute;s Visitante</h2> 
{$table}
<h2 class="span-10"><a id="materias"></a>Impacto por Pa&iacute;s por Tipo de Material</h2> 
	{$output6}
<h2 class="span-10"><a id="materias"></a>Impacto por Tipo de Material por Pa&iacute;s</h2> 
	{$output8}	
<div class="span-18 last">
  <h1>{translate text="Estad&iacute;sticas"}</h1>

  <h2>{translate text="Resumen"}</h2>
  <table class="citation">
  <tr>
    <th>{translate text="Total de B&uacute;squedas"}: </th>
    <td>{$searchCount}</td>
  </tr>
  <tr>
    <th>{translate text="B&uacute;squedas sin resultado "}: </th>
    <td>{$nohitCount}</td>
  </tr>
  <tr>
    <th>{translate text="Total de Consultas"}: </th>
    <td>{$recordViews}</td>
  </tr>
  </table>

  <h2>{translate text="B&uacute;squedas m&aacute;s frecuentes"}</h2>
  <ul>
  {foreach from=$termList item=term}
    <li><a href="{$url}/Search/Results?&type=AllFields&submit=Buscar&lookfor={$term.0|escape}">({$term.1}) {$term.0|escape}</a></li>
  {foreachelse}
    <li>{translate text="No Searches"}</li>
  {/foreach}
  </ul>
  
</div>

<div class="clear"></div>