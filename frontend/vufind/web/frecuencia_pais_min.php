<?php
echo '<ul>';
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=institution&fl=institution&q=format'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=count';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml2 ===  FALSE)
{
   //deal with error
echo '<h2>ERROR</h1>';
}
else 
{ //do stuff 
    $sum=0;
	$num=0;
	echo '<ul  class="span-4">';
foreach ($xml2->xpath("//lst[@name='institution']/int") as $date) 
    {
	$num=number_format((int)$date);
	$pais=$date['name'];
	if (strcmp($date['name'],"CEDIA - SENESCYT")==0)
	$pais="Ecuador";
	if (strcmp($date['name'],"COLCIENCIAS - MEN - RENATA")==0)
	$pais="Colombia";	
	if (strcmp($date['name'],"CONICyT")==0)
	$pais="Chile";	
	if (strcmp($date['name'],"IBICT")==0)
	$pais="Brasil";
	if (strcmp($date['name'],"La Red Académica de Centros de Investigación y Universidades Nacionales (Reacciun)")==0)
	$pais="Venezuela";
	if (strcmp($date['name'],"MINCYT-SNRD")==0)
	$pais="Argentina";	
	if (strcmp($date['name'],"Red Avanzada de Investigación, Ciencia y Educación Salvadoreña RAICES")==0)
	$pais="El Salvador";	
	if (strcmp($date['name'],"Red Mexicana de Repositorios Institucionales")==0)
	$pais="M&eacute;xico";
    if ($num==807)
	$pais="Venezuela";    
	if ($num==138)
	$pais="El Salvador";
	
    echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=institution%3A%22',$date['name'],'%22">',$pais,'</a> - ',$num,'</li>',PHP_EOL;
		$sum+=$date;
	}
}
echo '<li>Peru - 0</li>';
echo '<li>Total ',number_format($sum),'</li>';
echo '</ul>';

?>
