<?php
echo '<ul>';
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=collection&fl=collection&q=format'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=index';
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
	echo '<ul  class="span-6">';
foreach ($xml2->xpath("//lst[@name='collection']/int") as $date) 
    {
	$num=number_format((int)$date);
    echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=collection%3A%22',$date['name'],'%22">',$date['name'],'</a> - ',$num,'</li>',PHP_EOL;
		$sum+=$date;
	}
}
echo '<li>Total ',number_format($sum),'</li>';
echo '</ul>';

?>
