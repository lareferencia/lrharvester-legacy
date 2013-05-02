<?php
$url = 'http://200.0.206.214:8080/solr/biblio/select?q='.urlencode('topic_browse:[* TO *]').'&wt=xml&facet=true&facet.field=topic_browse&fl=topic_browse&facet.limit=1000&rows=0&facet.mincount=500&facet.sort=index';


/*http://200.0.206.214:8080/solr/biblio/select?facet=true&facet.field=topic_browse&fl=topic_browse&q=topic_browse:[*%20TO%20*]&facet.limit=1000&rows=0&facet.mincount=50&facet.sort=index*/
//echo $url;
$xml = simpleXML_load_file($url,"SimpleXMLElement",LIBXML_NOCDATA);

if($xml ===  FALSE)
{
   //deal with error
}
else { //do stuff 

//echo $xml;
foreach ($xml->xpath("//lst[@name='topic_browse']/int") as $busqueda) {
    echo '<a style="font-size:',$busqueda/80,'px;text-decoration:none;" href="http://200.0.206.214/vufind/Search/Results?lookfor=%22',$busqueda["name"],'%22&type=topic_browse">',$busqueda["name"],'(',$busqueda,')</a>	',PHP_EOL;
}

} 

?>
