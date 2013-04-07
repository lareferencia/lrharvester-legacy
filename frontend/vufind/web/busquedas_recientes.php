
<?php
 header('Content-Type: text/html; charset=UTF-8'); 
//$url = 'http://200.0.206.180/vufind/busquedas10_test.xml';
$url = 'http://200.0.206.180:8080/solr/stats/select?q='.urlencode('phrase:[* TO *]').'&wt=xml&fl='.urlencode('phrase,url,datestamp').'&sort='.urlencode('datestamp desc').'&rows=10';
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
echo '<h1>B&uacute;squedas m&aacute;s recientes.</h1>';
echo '<ol>';
foreach ($xml->xpath("//doc") as $busqueda) {
    echo '<li><a href="http://200.0.206.180',$busqueda->str,'">',$busqueda->arr->str,'</a></li>',PHP_EOL;
}
echo '</ol>';
echo '</body>';
echo '</html>';
} 

?>
