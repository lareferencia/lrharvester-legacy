<h1 class="span-10">Impacto</h1> 
<div class="clear"></div>
<h2 class="span-10">Impacto por Pa&iacute;s Visitante</h2> 
<br/><p>Este es el impacto en la consulta de objetos digitales por pais a trav&eacute;s de LA-Referencia.</p><br/>
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

<div class="clear"></div>