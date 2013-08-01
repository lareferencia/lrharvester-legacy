<h1>{translate text="Impacto"}</h1>  
<h2 class="span-10"><a id="materias"></a>Impacto por Tesis de Doctorado por Pa&iacute;s</h2> <br/><br/>
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
        colorAxis: {colors: ['#D68989']}
      };

      var chart = new google.visualization.GeoChart(document.getElementById('chart_div'));
      chart.draw(data, options);
    };
	{/literal} 
    </script>
   <div id="chart_div" style="width: 700px; height: 400px; margin:auto;"></div>
	{$output8}	
<div class="clear"></div>