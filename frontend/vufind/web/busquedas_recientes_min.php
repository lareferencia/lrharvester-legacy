<?php
$url = 'http://200.0.206.180:8080/solr/stats/select?q='.urlencode('phrase:[* TO *]').'&wt=xml&fl='.urlencode('phrase,url,datestamp').'&sort='.urlencode('datestamp desc').'&rows=20';
$xml = simpleXML_load_file($url,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml ===  FALSE)
{
   //deal with error
}
else { //do stuff 
echo '<ul  class="span-5">';
foreach ($xml->xpath("//doc") as $busqueda) {
    echo '<li><a href="http://200.0.206.180',$busqueda->str,'">',$busqueda->arr->str,'</a></li>',PHP_EOL;
}
echo '</ul>';
} 

?>
