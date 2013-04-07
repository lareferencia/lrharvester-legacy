
<?php
 header('Content-Type: text/html; charset=UTF-8'); 

echo '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />';
echo '<html>';
echo '<head>';
echo '</head>';

echo '<body>';
echo '<h1>Registros por instituci&oacute;n</h1>';
echo '<ul>';

$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=institution&fl=institution&q=format'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=index';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml2 ===  FALSE)
{
   //deal with error
echo '<h2>ERROR</h1>';
}
else 
{ //do stuff 
    echo $url2;
    echo '<br>';
    echo $xml2;
    echo '<br>';
foreach ($xml2->xpath("//lst[@name='institution']/int") as $date) 
    {
    echo '<li>',$date['name'],'-',$date,'</li>',PHP_EOL;
	}
}


echo '</ul>';
echo '</body>';
echo '</html>';


?>
