
<h1>Brasil</h1>
<h2>&Uacute;ltima Cosecha V&aacute;lida</h2>
{$output1}
<h3>Listado de Registros Inv&aacute;lidos en la &Uacute;ltima Cosecha</h3>
<iframe width="100%" src="http://200.0.206.214/vufind/getRecords.php?iso=BR&url=http://lareferencia.shell.la:8090//public/listInvalidRecordsInfoBySnapshotID/17"></iframe>

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
{$output8}{$output9}
{literal}
    options = {
	
        xaxis: {
            title: 'Fecha',
            labelsAngle: 0,
		noTicks: 7, 
		mode:"time",
		timeformat: "%y",
		minTickSize: [1, "week"]
        },
        yaxis: {
            title: 'Registros',
			max:300000 
        },	
        mouse: {
            track: true,
			trackFormatter: function(obj) {
            return obj.y;
				},
			trackDecimals: 0,
            relative: true
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
        label: 'LA-BR'
    },
	{
        data: d2,
        label: 'BR'
    }
	],	
		 o);
    }

    graph = drawGraph();

  })
( container=document.getElementById("container3")
);
    </script>
{/literal} 	
<div class="clear"></div>
<h2>Estad&iacute;sticas de Cosechas</h2>
{$output7}

<div class="clear"></div>