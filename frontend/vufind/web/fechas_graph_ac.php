<?php
 header('Content-Type: text/html; charset=UTF-8'); 
 ?>
<html>
  <head>
    <style type="text/css">
      body {
        margin: 0px;
        padding: 0px;
      }
      #container {
        width : 600px;
        height: 384px;
        margin: 8px auto;
      }
    </style>
  </head>
  <body>
    <div id="container"></div>
    <!--[if IE]>
    <script type="text/javascript" src="/static/lib/FlashCanvas/bin/flashcanvas.js"></script>
    <![endif]-->
    <script type="text/javascript" src="flotr2.min.js"></script>
    <script type="text/javascript">
(
function basic_time(container) {

<?php include 'fechas_array_ac.php'; ?>

    options = {
        xaxis: {
            title: 'Ano',
            labelsAngle: 0,
		noTicks: 17,
		mode:"time",
		timeformat: "%y"
        },
        yaxis: {
            title: 'Registros',
        },	
        selection: {
            mode: 'x'
        },
        HtmlText: false,
        title: 'Registros Acumulados por Fecha'
    };

    // Draw graph with default options, overwriting with passed options


    function drawGraph(opts) {

        // Clone the options, so the 'options' variable always keeps intact.
        o = Flotr._.extend(Flotr._.clone(options), opts || {});

        // Return a new graph.
        return Flotr.draw(
        container, [d1], o);
    }

    graph = drawGraph();

  })
( container=document.getElementById("container")
);
    </script>
  </body>
</html>
