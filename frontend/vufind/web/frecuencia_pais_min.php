<?php
echo '<ul>';
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=country&fl=country&q=country'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=index';
//echo $url2;
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
foreach ($xml2->xpath("//lst[@name='country']/int") as $country) 
    {
	$num=number_format((int)$country);
	
	$pais=$country['name'];
    
	echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=country%3A%22',$country['name'],'%22">',$pais,'</a> - ',$num,'</li>',PHP_EOL;
		$sum+=$country;
	}
}
//echo '<li>Peru - 0</li>';
echo '<li>Total ',number_format($sum),'</li>';
echo '</ul>';

?>
