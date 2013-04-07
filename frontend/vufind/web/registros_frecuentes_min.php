
<?php
$url = 'http://200.0.206.180:8080/solr/stats/select?q='.urlencode('recordId:[* TO *]').'&wt=xml&facet=true&facet.field=recordId&fl=0&facet.limit=5&rows=0';
$xml = simpleXML_load_file($url,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml ===  FALSE)
{
   //deal with error
}
else { //do stuff 
echo '<ul class="span-5">';
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
	echo '<li>&nbsp;</li>',PHP_EOL;

	}
}	
}
echo '</ul>';
} 

?>
