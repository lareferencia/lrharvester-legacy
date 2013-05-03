<?php
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=country_iso&fl=country_iso&q=country_iso'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=count';
//echo $url2;
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml2 ===  FALSE)
{
//deal with error
echo 'ERROR';
}
else 
{ //do stuff 
    $sum=0;
	$num=0;
	echo "['Pais', 'Registros'],";
foreach ($xml2->xpath("//lst[@name='country_iso']/int") as $country) 
    {
	$pais=$country['name'];
	echo "['",$pais,"', ",$country,"],",PHP_EOL;
	}
}


?>
