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

			
/*$url2 = 'http://200.0.206.214:8080/solr/biblio/select?q=institution:'.urlencode('"'.$date['name'].'"').'&fl=institution,format';
	
$url2 = 'http://200.0.206.214:8080/solr/stats/select?q='.urlencode('institution:"'.$date['name'].'"').'&wt=xml&fl='.urlencode('recordId,datestamp').'&sort='.urlencode('datestamp desc').'&rows=1000';		
*/


$tipoa=0;
$tipom=0;
$tipod=0;
$tipor=0;
$tipotot=0;


$url3 = 'http://200.0.206.214:8080/solr/biblio/select?facet=true&facet.field=format&fl=format&q=institution:'.urlencode('"'.$date['name'].'"').'&facet.limit=5&rows=0&facet.sort=count';
	
//echo $url3;
echo '<ul class="span-8">';
$xml3 = simpleXML_load_file($url3,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml3 ===  FALSE)
{
   //deal with error
}
else { 

//do stuff 
foreach ($xml3->xpath("//lst[@name='format']/int") as $title) {
    		if (strcmp($title[@name],"info:eu-repo/semantics/article")==0)
	echo  '<li>Art&iacute;culo: '.number_format((int)$title).' </li>';
		else if (strcmp($title[@name],"info:eu-repo/semantics/masterThesis")==0)
	echo  '<li>Tesis de Maestr&iacute;a: '.number_format((int)$title).' </li>';
		else if (strcmp($title[@name],"info:eu-repo/semantics/doctoralThesis")==0)
	echo  '<li>Tesis de Doctorado: '.number_format((int)$title).' </li>';
		else 	if (strcmp($title[@name],"info:eu-repo/semantics/report")==0)
	echo  '<li>Reporte: '.number_format((int)$title).' </li>';
	
	
	//echo '<li><a href="http://200.0.206.214/vufind/Record/',$busqueda['name'],'">',$title,'</a></li>',PHP_EOL;
	}
}
echo '<li>&nbsp;</li>';
echo '</ul>';	
}
} 
echo '<li>Peru - 0</li>';
echo '<li>&nbsp;</li>';
echo '<li>Total ',number_format($sum),'</li>';
echo '</ul>';

?>
