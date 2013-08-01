
<div class="span-18 last">
  <h1>{translate text="Paises Socios"}</h1>
  <table class="citation">
  <tr>
    <th>{translate text="Registros por Pa&iacute;s"}</th>
	</tr>
  <tr>
    <td>
      <ul>

      {foreach from=$countryList item=term}
        <li><a href="{$url}/Search/Results?lookfor=&type=AllFields&filter%5B%5D=country%3A%22{$term.0|escape}%22">{$term.0|escape}</a> - {$term.1|number_format:0:".":","}</li>
	  {assign var="sum1" value="`$sum1+$term.1`"}
	  {/foreach}
	    <li>Total - {$sum1|number_format:0:".":","}</li>
      </ul>
    </td>
	</tr>
  </table>

</div>
<div class="clear"></div>
<h2><a id="mapa">Mapa de pa&iacute;ses socios</a></h2>
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
  <div id="visualization" style="width: 400px; height: 400px;margin-left:70px;"></div>
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
  <div id="visualization2" style="width: 400px; height: 400px;margin-left:70px;"></div>
	<div class="clear"></div>
	<h2 class="span-10"><a id="instituciones"></a>Registros <br/>por Instituci&oacute;n <i> (instname) </i></h2> 
	    <i> El dc:source "instname" es parte de los acuerdos acerca del uso de DRIVER, sin embargo actualmente solo M&eacute;xico lo est&aacute; cumpliendo, a medida que ese acuerdo se cumpla, se agregar&aacute;n m&aacute;s instituciones</i>
	<div class="clear"></div>
	{$output}
	
<div class="clear"></div>
	<h2 class="span-10"><a id="instituciones"></a>Estad&iacute;sticas por Red</h2> 
<div class="clear"></div>
	{$output1}
	
<div class="clear"></div>