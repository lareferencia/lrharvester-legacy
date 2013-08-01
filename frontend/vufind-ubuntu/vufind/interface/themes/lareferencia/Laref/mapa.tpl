
<h1>Pa&iacute;ses Socios</h1>
<h2>Mapa de la Red.</h2>
  <script type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript">
{literal} 
    google.load('visualization', '1', {packages: ['geochart']});

    function drawVisualization() {
      var data = google.visualization.arrayToDataTable([
	  
	  ['Pais', 'Registros'],
{/literal}
	  {foreach from=$cisoList item=term}
        {literal}['{/literal}{$term.0}{literal}',{/literal}{$term.1}{literal}],{/literal}
      {/foreach}
 {literal}
	  ]);
    
	var options = {};
      options['region'] = '005';
     // options['colors'] = [0xFFB581, 0xc06000]; //orange colors
	  
      var geochart = new google.visualization.GeoChart(
          document.getElementById('visualization'));
      geochart.draw(data, options);
    }
    google.setOnLoadCallback(drawVisualization);
{/literal} 
  </script>
  <div id="visualization" style="width:400px;height:300px;margin-left:10px;float:left"></div>
	<script type="text/javascript">
{literal} 
    google.load('visualization', '1', {packages: ['geochart']});

    function drawVisualization2() {
      var data = google.visualization.arrayToDataTable([
	  
	  ['Pais', 'Registros'],
{/literal}
	  {foreach from=$cisoList item=term}
        {literal}['{/literal}{$term.0}{literal}',{/literal}{$term.1}{literal}],{/literal}
      {/foreach}
{literal}
	  ]);
    
	var options = {};
      options['region'] = '013';
     // options['colors'] = [0xFFB581, 0xc06000]; //orange colors
	  
      var geochart = new google.visualization.GeoChart(
          document.getElementById('visualization2'));
      geochart.draw(data, options);
    }
    google.setOnLoadCallback(drawVisualization2);
{/literal} 
  </script>
  <div id="visualization2" style="width:400px;height:300px;margin-left:10px;float:right"></div>
	<div class="clear"></div>

	<div class="span-10">
	{$output1}
</div>
<div class="clear"></div>
