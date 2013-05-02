<?php
echo '<ul>';
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=country&fl=country&q=type'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=count';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);


//echo $url2;
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
foreach ($xml2->xpath("//lst[@name='country']/int") as $country) 
    {
	$num=number_format((int)$country);
	
	$pais=$country['name'];
	
    echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=country%3A%22',$pais,'%22">',$pais,'</a> - ',$num,'</li>',PHP_EOL;
		$sum+=$country;

$tipoa=0;
$tipom=0;
$tipod=0;
$tipor=0;
$tipotot=0;


$url3 = 'http://200.0.206.214:8080/solr/biblio/select?facet=true&facet.field=type&fl=type&q=country:'.urlencode('"'.$pais.'"').'&facet.limit=5&rows=0&facet.sort=index';
	
//echo $url3;
echo '<ul class="span-8">';
$xml3 = simpleXML_load_file($url3,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml3 ===  FALSE)
{
   //deal with error
}
else { 

//do stuff 
foreach ($xml3->xpath("//lst[@name='type']/int") as $tipo) {
	

	$tipos=$tipo['name'];
	
	echo  '<li>'.$tipos.': '.number_format((int)$tipo).' </li>';

	}
}
echo '<li>&nbsp;</li>';
echo '</ul>';	
}
} 
//echo '<li>Peru - 0</li>';
echo '<li>&nbsp;</li>';
echo '<li>Total ',number_format($sum),'</li>';
echo '</ul>';

?>
