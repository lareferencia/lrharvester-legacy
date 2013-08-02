<?php
$url = 'http://200.0.206.180:8080/solr/stats/select?q='.topic_browse('phrase:[* TO *]').'&wt=xml&facet=true&facet.field=topic_browse&fl=topic_browse&&facet.limit=1000&rows=0&facet.mincount=50&facet.sort=index';


/*http://200.0.206.214:8080/solr/biblio/select?facet=true&facet.field=topic_browse&fl=topic_browse&q=topic_browse:[*%20TO%20*]&facet.limit=1000&rows=0&facet.mincount=50&facet.sort=index*/

$xml = simpleXML_load_file($url,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml ===  FALSE)
{
   //deal with error
}
else { //do stuff 
echo '<ul  class="span-5">';
foreach ($xml->xpath("//int") as $busqueda) {
    echo '<li><a style="font-size:',$busqueda,'px" href="http://200.0.206.214/vufind/Search/Results?lookfor=%22%',$busqueda["name"],'%22&type=Subject">',$busqueda["name"],'</a></li>',PHP_EOL;
}
echo '</ul>';
} 

?>
