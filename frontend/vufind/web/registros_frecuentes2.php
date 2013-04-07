
<?php
 header('Content-Type: text/html; charset=UTF-8'); 
//$url = 'http://200.0.206.180/vufind/busquedas10_test.xml';
//http://200.0.206.180:8080/solr/stats/select?facet=true&facet.field=phrase&fl=0&q=phrase:[*%20TO%20*]&facet.limit=10&rows=0
//http://200.0.206.180:8080/solr/stats/select?facet=true&facet.field=phrase&fl=0&facet.limit=10&rows=0
//http://200.0.206.180:8080/solr/stats/select?facet=true&facet.field=recordId&fl=0&q=recordId:[*%20TO%20*]&facet.limit=10&rows=0
//http://200.0.206.180:8080/solr/stats/select?facet=true&facet.field=recordId&fl=0&facet.limit=10&rows=0
$url = 'http://200.0.206.180:8080/solr/stats/select?q='.urlencode('recordId:[* TO *]').'&wt=xml&facet=true&facet.field=recordId&fl=0&facet.limit=5&rows=0';
$xml = simpleXML_load_file($url,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml ===  FALSE)
{
   //deal with error
}
else { //do stuff 
echo '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />';
echo '<html>';
echo '<head>';
echo '</head>';

echo '<body>';
echo '<h1>Registros consultados m&aacute;s frecuentes 2.</h1>';
echo '<ul>';
foreach ($xml->xpath("//lst[@name='recordId']/int") as $busqueda) {
$url2 = 'http://200.0.206.180:8080/solr/biblio/select?q=id:"'.urlencode($busqueda['name']).'"';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml2 ===  FALSE)
{
   //deal with error
}
else { //do stuff 
foreach ($xml2->xpath("//str[@name='title']") as $title) {
    echo '<li><a href="http://200.0.206.180/vufind/Record/',$busqueda['name'],'">',$title,'</a></li>',PHP_EOL;
	}
}	
}
echo '</ul>';
echo '</body>';
echo '</html>';
} 

?>
