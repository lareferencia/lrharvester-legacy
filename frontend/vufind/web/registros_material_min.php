
<?php
$url = 'http://200.0.206.214:8080/solr/stats/select?q='.urlencode('recordId:[* TO *]').'&wt=xml&fl='.urlencode('recordId,datestamp').'&sort='.urlencode('datestamp desc').'&rows=1000';
//echo $url;
$xml = simpleXML_load_file($url,"SimpleXMLElement",LIBXML_NOCDATA);
$tipoa=0;
$tipom=0;
$tipod=0;
$tipor=0;
$tipotot=0;

if($xml ===  FALSE)
{
   //deal with error
}
else { //do stuff 

//echo '<ul class="span-5">';
foreach ($xml->xpath("//str[@name='recordId']") as $busqueda) {

$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=format&fl=format&q=id:"'.urlencode($busqueda).'".'&facet.limit=5&rows=0&facet.sort=count';

//$url2 = 'http://200.0.206.214:8080/solr/biblio/select?q=id:"'.urlencode($busqueda).'"';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml2 ===  FALSE)
{
   //deal with error
}
else { //do stuff 
foreach ($xml2->xpath("//arr[@name='format']/str") as $title) {
    		if (strcmp($title,"info:eu-repo/semantics/article")==0)
	$tipoa++;
		if (strcmp($title,"info:eu-repo/semantics/masterThesis")==0)
	$tipom++;
		if (strcmp($title,"info:eu-repo/semantics/doctoralThesis")==0)
	$tipod++;
			if (strcmp($title,"info:eu-repo/semantics/report")==0)
	$tipor++;
	
	
	//echo '<li><a href="http://200.0.206.214/vufind/Record/',$busqueda['name'],'">',$title,'</a></li>',PHP_EOL;
	}
}	
}
//echo '</ul>';
$tipotot=$tipoa+$tipom+$tipod+$tipor;
echo  '<ul class="span-5"><li>Art&iacute;culo: '.$tipoa.' </li><li>Tesis de Maestr&iacute;a: '.$tipom.'</li><li> Tesis de Doctorado: '.$tipod.'</li><li> Reporte: '.$tipor.'</li><li> Total: '.$tipotot.'</li></ul>';
} 

?>
