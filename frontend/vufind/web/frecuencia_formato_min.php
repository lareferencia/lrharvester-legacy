
<?php
echo '<ul>';
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=type&fl=type&q=type'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=count';
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
	$tipo="";
	echo '<ul  class="span-5">';
foreach ($xml2->xpath("//lst[@name='type']/int") as $tipo) 
    {
	$num=number_format((int)$tipo);

	$tipos=$tipo['name'];
	

    echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=type%3A%22',$date['name'],'%22">',$tipos,'</a> - ',$num,'</li>',PHP_EOL;
		$sum+=$tipo;
	}
}
echo '<li> </li>';

echo '<li>Total ',number_format($sum),'</li>';
echo '</ul>';

?>
