<h1>{translate text="Impacto"}</h1>  
<h2 class="span-10"><a id="materias"></a>Impacto de Tesis de Maestr&iacute;a por Pa&iacute;s</h2> 
<div class="clear"></div>
Mapa que presenta las distribuciones de accesos y usos del tipo de material.
<br/><br/>
<div class="clear"></div>
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
        colorAxis: {colors: ['#8AC08A']}
      };

      var chart = new google.visualization.GeoChart(document.getElementById('chart_div'));
      chart.draw(data, options);
    };
	{/literal} 
    </script>
   <div id="chart_div" style="width: 700px; height: 400px; margin:auto;"></div>
	{$output6}
<div class="clear"></div>