<?php
echo '<ul>';
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=country&fl=country&q=type'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=index';
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
	echo '<ul  class="span-10">';
foreach ($xml2->xpath("//lst[@name='country']/int") as $country) 
    {
	$num=number_format((int)$country);
	
	$pais=$country['name'];
	
  //  echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=country%3A%22',$pais,'%22">',$pais,'</a> - ',$num,'</li>',PHP_EOL;
  
  echo '<li>',$pais,'</li>',PHP_EOL;
		$sum+=$country;

$url3 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=instname&fl=instname&q=country:'.urlencode('"'.$pais.'"').'&facet.limit=5&rows=0&facet.sort=index';
	
//echo $url3;
$xml3 = simpleXML_load_file($url3,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml3 ===  FALSE)
{
   //deal with error
}
else { 

	echo '<ul  class="span-10">';
foreach ($xml3->xpath("//lst[@name='instname']/int") as $date) 
    {
	$num=number_format((int)$date);
	if($date>0)
    echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=instname%3A%22',$date['name'],'%22">',$date['name'],'</a> - ',$num,'</li>',PHP_EOL;
		$sum+=$date;
	}
}
//echo '<li>Total ',number_format($sum),'</li>';
echo '<li>&nbsp;</li>';

echo '</ul>';	
}
} 
//echo '<li>Peru </li>';
echo '</ul>';

?>
