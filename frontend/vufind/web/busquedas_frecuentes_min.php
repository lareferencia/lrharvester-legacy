<?php
$url = 'http://200.0.206.214:8080/solr/stats/select?q='.urlencode('phrase:[* TO *]').'&wt=xml&facet=true&facet.field=phrase&fl=0&facet.limit=20&rows=0';
$xml = simpleXML_load_file($url,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml ===  FALSE)
{
   //deal with error
}
else { //do stuff 
echo '<ul class="span-5">';
foreach ($xml->xpath("//lst[@name='phrase']/int") as $busqueda) {
    echo '<li><a href="http://200.0.206.214/vufind/Search/Results?&type=AllFields&submit=Buscar&lookfor=',$busqueda['name'],'">',$busqueda['name'],'</a></li>',PHP_EOL;
}
echo '</ul>';
} 
?>
