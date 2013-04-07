
<?php
 header('Content-Type: text/html; charset=UTF-8'); 
//$url = 'http://200.0.206.180/vufind/busquedas10_test.xml';
//http://200.0.206.180:8080/solr/stats/select?facet=true&facet.field=phrase&fl=0&q=phrase:[*%20TO%20*]&facet.limit=10&rows=0
//http://200.0.206.180:8080/solr/stats/select?facet=true&facet.field=phrase&fl=0&facet.limit=10&rows=0

$url = 'http://200.0.206.180:8080/solr/stats/select?q='.urlencode('phrase:[* TO *]').'&wt=xml&facet=true&facet.field=phrase&fl=0&facet.limit=10&rows=0';

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
echo '<h1>B&uacute;squedas m&aacute;s frecuentes.</h1>';
echo '<ul>';
foreach ($xml->xpath("//lst[@name='phrase']/int") as $busqueda) {
    echo '<li><a href="http://200.0.206.180/vufind/Search/Results?&type=AllFields&submit=Buscar&lookfor=',$busqueda['name'],'">',$busqueda,'-',$busqueda['name'],'</a></li>',PHP_EOL;
}
echo '</ul>';
echo '</body>';
echo '</html>';
} 

?>
