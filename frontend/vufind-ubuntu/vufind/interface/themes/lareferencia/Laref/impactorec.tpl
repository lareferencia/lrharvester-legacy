<h1 class="span-10">Impacto</h1> 
<div class="clear"></div>
<h2 class="span-10">Impacto por Pa&iacute;s Recolector</h2> 
<br/><br/>

    <script type='text/javascript' src='https://www.google.com/jsapi'></script>
    <script type='text/javascript'>
	{literal} 
     google.load('visualization', '1', {'packages': ['geochart']});
     google.setOnLoadCallback(drawMarkersMap);

      function drawMarkersMap() {
      var data = google.visualization.arrayToDataTable([
	  {/literal} 
{$output11}
{literal} 
]);
 var options = {
        region: 'world',
        displayMode: 'regions',
        colorAxis: {colors: ['#FF8CA2']}
      };

      var chart = new google.visualization.GeoChart(document.getElementById('chart_div'));
      chart.draw(data, options);
    };
	{/literal} 
    </script>
   <div id="chart_div" style="width: 700px; height: 400px; margin:auto;"></div>
<div class="clear"></div>
<br/><br/>
{$output10}
<div class="clear"></div>