
<h2>Pa&iacute;ses Socios</h2>
<h2>Mapa</h2>
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
	
	<h2 class="span-10"><a id="instituciones"></a>Registros <br/>por Instituci&oacute;n <i> (instname) </i></h2> 
	 <i> El dc:source "instname" es parte de los acuerdos acerca del uso de DRIVER, sin embargo actualmente solo M&eacute;xico lo est&aacute; cumpliendo, a medida que ese acuerdo se cumpla, se agregar&aacute;n m&aacute;s instituciones</i>
<div class="clear"></div>
	{$output}
	
<div class="clear"></div>
	<div class="span-10">
  <h1>{translate text="Recolecciones"}</h1>
	{$output7}

</div>
<div class="clear"></div>
<h2>Material por Fecha de Recolecci&oacute;n</h2> 
{literal}
      <style type="text/css">
 
	        #container3 {
        width : 600px;
        height: 384px;
        margin: 8px auto;
      }
    </style>
{/literal}
<div id="container3"> </div>
 <script type="text/javascript" src="{$url}/flotr2.min.js"></script>
{literal}
<script type="text/javascript">
(
function basic_time(container) {
{/literal} 
{$output8}
{literal}ax;
    options = {
	
        xaxis: {
            title: 'Fecha',
            labelsAngle: 0,
		noTicks: 17,
		mode:"time",
		timeformat: "%y"
        },
        yaxis: {
            title: 'Registros',
			max:25000 
        },	
        selection: {
            mode: 'x'
        },
        HtmlText: false,
        title: 'Registros por Fecha'
    };

    // Draw graph with default options, overwriting with passed options


    function drawGraph(opts) {

        // Clone the options, so the 'options' variable always keeps intact.
        o = Flotr._.extend(Flotr._.clone(options), opts || {});

        // Return a new graph.
        return Flotr.draw(
        container,
[{
        data: d1,
        label: 'AR'
    }, {
        data: d2,
        label: 'CL'
    }, {
        data: d3,
        label: 'CO',
    }, {
        data: d4,
        label: 'EC'
    },{
        data: d5,
        label: 'SV'
    },{
        data: d6,
        label: 'MX'
    },{
        data: d7,
        label: 'PE'
    },{
        data: d8,
        label: 'VE'
    }],		
		 o);
    }

    graph = drawGraph();

  })
( container=document.getElementById("container3")
);
    </script>
{/literal} 	
<div class="clear"></div>