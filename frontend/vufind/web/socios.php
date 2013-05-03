<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="es" xml:lang="en">

<?php
header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
header("Expires: Sat, 26 Jul 1997 05:00:00 GMT"); // Date in the past
?>

        
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    
    <title>Paises Socios de LA-Referencia</title>

        <link rel="search" type="application/opensearchdescription+xml" title="Library Catalog Search" href="http://200.0.206.214/vufind/Search/OpenSearch?method=describe" />
    
        <link rel="stylesheet" type="text/css" media="screen, projection" href="/vufind/interface/themes/lareferencia/css/blueprint/screen.css" />
    <link rel="stylesheet" type="text/css" media="print" href="/vufind/interface/themes/lareferencia/css/blueprint/print.css" />
    <!--[if lt IE 8]><link rel="stylesheet" href="http://200.0.206.214/vufind/interface/themes/lareferencia/css/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
    
        <script type="text/javascript">
    //<![CDATA[
      var path = 'http://200.0.206.214/vufind';
    //]]>
    </script>

	    <script type="text/javascript" src="/vufind/interface/themes/lareferencia/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="/vufind/interface/themes/lareferencia/js/jquery.form.js"></script>
    <script type="text/javascript" src="/vufind/interface/themes/lareferencia/js/jquery.metadata.js"></script>
    <script type="text/javascript" src="/vufind/interface/themes/lareferencia/js/jquery.validate.min.js"></script>    
    
        <script type="text/javascript" src="/vufind/interface/themes/lareferencia/js/jquery-ui-1.8.7.custom/js/jquery-ui-1.8.7.custom.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen, projection" href="http://200.0.206.214/vufind/interface/themes/lareferencia/js/jquery-ui-1.8.7.custom/css/smoothness/jquery-ui-1.8.7.custom.css" />
        
        <script type="text/javascript" src="/vufind/interface/themes/lareferencia/js/lightbox.js"></script>

        <script type="text/javascript" src="/vufind/interface/themes/lareferencia/js/common.js"></script>

			        <script type="text/javascript" src="/vufind/interface/themes/lareferencia/js/ajaxrequest.js"></script>

		
        <link rel="stylesheet" type="text/css" media="screen, projection" href="/vufind/interface/themes/lareferencia/css/styles.css" />
    <link rel="stylesheet" type="text/css" media="print" href="/vufind/interface/themes/lareferencia/css/print.css" />
    <!--[if lt IE 8]><link rel="stylesheet" href="http://200.0.206.214/vufind/interface/themes/lareferencia/css/ie.css" type="text/css" media="screen, projection"><![endif]-->
  
      <style type="text/css">
 
      #container {
        width : 250px;
        height: 300px;
        margin: 8px auto;
      }
	        #container2 {
        width : 600px;
        height: 384px;
        margin: 8px auto;
      }
	        #container3 {
        width : 600px;
        height: 384px;
        margin: 8px auto;
      }
    </style>
  
  </head>

  <body onload="add_record_event()">
    <div class="container">
	  <div class="header">
	  <script type="text/javascript" src="/vufind/interface/themes/lareferencia/js/jquery.cookie.js"></script>
<a id="logo" href="http://200.0.206.214/vufind"></a>
<div id="headerRight">
    <div id="logoutOptions" class="hide">
    <a class="account" href="/vufind/MyResearch/Home">Su Cuenta</a> |
    <a class="logout" href="/vufind/MyResearch/Logout">Salir</a>
  </div>
  <div id="loginOptions">
      <a class="login" href="/vufind/MyResearch/Home">Entrar</a>
    </div>
    <form method="post" name="langForm" action="" id="langForm">
    <label for="langForm_mylang">Idioma:</label>
    <select id="langForm_mylang" name="mylang" class="jumpMenu">
              <option value="en">English</option>
              <option value="de">German</option>
              <option value="es" selected="selected">Spanish</option>
              <option value="fr">French</option>
              <option value="it">Italian</option>
              <option value="ja">Japanese</option>
              <option value="nl">Dutch</option>
              <option value="pt">Portuguese</option>
              <option value="zh-cn">Simplified Chinese</option>
              <option value="zh">Chinese</option>
              <option value="tr">Turkish</option>
              <option value="he">Hebrew</option>
              <option value="ga">Irish</option>
              <option value="cy">Welsh</option>
              <option value="el">Greek</option>
              <option value="ca">Catalan</option>
              <option value="eu">Basque</option>
              <option value="ru">Russian</option>
              <option value="cs">Czech</option>
          </select>
    <noscript><input type="submit" value="Poner" /></noscript>
  </form>
  </div>

<div class="clear"></div>		Bienvenido : <a href="http://200.0.206.214/vufind">Principal</a> - <a href="http://200.0.206.214/vufind/socios.php">Socios</a> - <a href="http://200.0.206.214/vufind/material.php">Material</a> - <a href="http://200.0.206.214/vufind/estadisticas.php"> Estad&iacute;sticas</a> - <a href="http://200.0.206.214/vufind/about.php">Acerca de  </a> 
 </div>

<div class="main">
<div style="margin: 20px 70px ;">
<h1>Socios</h1>
<h2><i>(datos al 28 de abril de 2013)</i></h2>

<h2><a id="mapa">Mapa de pa&iacute;ses socios</a></h2>
 </div>
  <script type="text/javascript" src="http://www.google.com/jsapi"></script>
	
	<script type="text/javascript">
    google.load('visualization', '1', {packages: ['geomap']});

    function drawVisualization() {
      var data = google.visualization.arrayToDataTable([
	<?php include 'tabla_pais_min.php'; ?>
		
      ]);
    
	var options = {};
      options['region'] = '005';
      options['colors'] = [0xFFB581, 0xc06000]; //orange colors
	  
      var geomap = new google.visualization.GeoMap(
          document.getElementById('visualization'));
      geomap.draw(data, options);
    }
    

    google.setOnLoadCallback(drawVisualization);
  </script>
  <div id="visualization" style="width: 100px; height: 100px;margin-left:70px;"></div>
<br/>
<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
 <script type="text/javascript" src="http://www.google.com/jsapi"></script>
	
	<script type="text/javascript">
    google.load('visualization', '1', {packages: ['geomap']});

    function drawVisualization() {
      var data = google.visualization.arrayToDataTable([
	<?php include 'tabla_pais_min.php'; ?>

      ]);
    
	var options = {};
      options['region'] = '013';
      options['colors'] = [0xFFB581, 0xc06000]; //orange colors
	  
      var geomap = new google.visualization.GeoMap(
          document.getElementById('visualization2'));
      geomap.draw(data, options);
    }
    

    google.setOnLoadCallback(drawVisualization);
  </script>
  <div id="visualization2" style="width: 100px; height: 100px;margin-left:70px;"></div>
<br/>
<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

<div class="searchHomeBrowse">
	<h2 class="span-4"><a id="paises"></a>Registros <br/>por Pa&iacute;s</h2> 
	<h2 class="span-10"><a id="instituciones"></a>Registros <br/>por Instituci&oacute;n <i> (instname) </i></h2> 

	<div class="span-14 last"><!-- pad out header row --></div>
	<?php include 'frecuencia_pais_min.php'; ?>
	<?php include 'frecuencia_pais_institucion_min.php'; ?>
	    <i> El dc:source instname es parte de los acuerdos acerca del uso de DRIVER, sin embargo actualmente solo México lo está cumpliendo, a medida que ese acuerdo se cumpla, se agregarán más instituciones</i>

<div class="clear"></div>
</div>
</div>
	  <div class="footer">
		<div class="span-5"><p><strong>Opciones de Búsqueda</strong></p>
  <ul>
    <li><a href="/vufind/Search/History">Historia de Búsqueda</a></li>
    <li><a href="/vufind/Search/Advanced">Búsqueda Avanzada</a></li>
  </ul>
</div>
<div class="span-5"><p><strong>Buscar Más</strong></p>
  <ul>
    <li><a href="/vufind/Browse/Home">Revisar el Catálogo</a></li>
    <li><a href="/vufind/AlphaBrowse/Home">Lista Alfabética</a></li>
    <li><a href="/vufind/Search/Reserves">Reservas de Curso</a></li>
    <li><a href="/vufind/Search/NewItem">Nuevos ejemplares</a></li>
  </ul>
</div>
<div class="span-5 last"><p><strong>¿Necesita Ayuda?</strong></p>
  <ul>
    <li><a href="http://200.0.206.214/vufind/Help/Home?topic=search" class="searchHelp">Consejos de Búsqueda</a></li>
    <li><a href="#">Consulte a un Bibliotecario</a></li>
    <li><a href="#">Preguntas Frecuentes</a></li>
  </ul>
</div>
<div class="clear"></div>
<a id="pie" href="http://200.0.206.214/vufind"></a>
	  </div>
    </div>
  </body>
</html>