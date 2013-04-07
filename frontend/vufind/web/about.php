<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="es" xml:lang="en">

<?php
header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
header("Expires: Sat, 26 Jul 1997 05:00:00 GMT"); // Date in the past
?>

        
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    
    <title>Prototipo para LA-Referencia</title>

        <link rel="search" type="application/opensearchdescription+xml" title="Library Catalog Search" href="http://200.0.206.180/vufind/Search/OpenSearch?method=describe" />
    
        <link rel="stylesheet" type="text/css" media="screen, projection" href="/vufind/interface/themes/blueprint/css/blueprint/screen.css" />
    <link rel="stylesheet" type="text/css" media="print" href="/vufind/interface/themes/blueprint/css/blueprint/print.css" />
    <!--[if lt IE 8]><link rel="stylesheet" href="http://200.0.206.180/vufind/interface/themes/blueprint/css/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
    
        <script type="text/javascript">
    //<![CDATA[
      var path = 'http://200.0.206.180/vufind';
    //]]>
    </script>

	    <script type="text/javascript" src="/vufind/interface/themes/blueprint/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="/vufind/interface/themes/blueprint/js/jquery.form.js"></script>
    <script type="text/javascript" src="/vufind/interface/themes/blueprint/js/jquery.metadata.js"></script>
    <script type="text/javascript" src="/vufind/interface/themes/blueprint/js/jquery.validate.min.js"></script>    
    
        <script type="text/javascript" src="/vufind/interface/themes/blueprint/js/jquery-ui-1.8.7.custom/js/jquery-ui-1.8.7.custom.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen, projection" href="http://200.0.206.180/vufind/interface/themes/blueprint/js/jquery-ui-1.8.7.custom/css/smoothness/jquery-ui-1.8.7.custom.css" />
        
        <script type="text/javascript" src="/vufind/interface/themes/blueprint/js/lightbox.js"></script>

        <script type="text/javascript" src="/vufind/interface/themes/blueprint/js/common.js"></script>

		
	        <script type="text/javascript" src="/vufind/interface/themes/blueprint/js/ajaxrequest.js"></script>
		
        <link rel="stylesheet" type="text/css" media="screen, projection" href="/vufind/interface/themes/blueprint/css/styles.css" />
    <link rel="stylesheet" type="text/css" media="print" href="/vufind/interface/themes/blueprint/css/print.css" />
    <!--[if lt IE 8]><link rel="stylesheet" href="http://200.0.206.180/vufind/interface/themes/blueprint/css/ie.css" type="text/css" media="screen, projection"><![endif]-->
  </head>

  <body onload="add_record_event()">
    <div class="container">
	  <div class="header">
		<script type="text/javascript" src="/vufind/interface/themes/blueprint/js/jquery.cookie.js"></script>
<a id="logo" href="http://200.0.206.180/vufind"></a>
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

<div class="clear"></div>	 
Bienvenido :<a href="estats.php">B&uacute;squedas</a> - <a href="grafs.php">Colecci&oacute;n</a> - <a href="about.php">Acerca de </a> 
 </div>

	  
      
<div class="main">


<div class="searchHomeBrowse">
	<h2 class="span-5">Coordinaci&oacute;n</h2> 
	<h2 class="span-5">Back-end</h2> 
	<h2 class="span-5">Front-end </h2>

	<div class="span-14 last"><!-- pad out header row --></div>
	<ul  class="span-5">
	<li>Emiliano Marmonti</li>
	</ul>
	<ul  class="span-5">
	<li>Lautaro Matas</li>
	</ul>
		<ul  class="span-5">
	<li>Antonio Razo</li>
	</ul>
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
    <li><a href="http://200.0.206.180/vufind/Help/Home?topic=search" class="searchHelp">Consejos de Búsqueda</a></li>
    <li><a href="#">Consulte a un Bibliotecario</a></li>
    <li><a href="#">Preguntas Frecuentes</a></li>
  </ul>
</div>
<div class="clear"></div>
<a id="pie" href="http://200.0.206.180/vufind"></a>
	  </div>
    </div>
  </body>
</html>