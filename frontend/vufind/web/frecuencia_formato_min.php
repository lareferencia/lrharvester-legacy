
<?php
echo '<ul>';
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=format&fl=format&q=format'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=count';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml2 ===  FALSE)
{
   //deal with error
echo '<h2>ERROR</h1>';
}
else 
{ //do stuff 
    $sum=0;
	$tipo="";
	echo '<ul  class="span-5">';
foreach ($xml2->xpath("//lst[@name='format']/int") as $date) 
    {
	$num=number_format((int)$date);
		if (strcmp($date['name'],"info:eu-repo/semantics/article")==0)
	$tipo="Art&iacute;culo";
		if (strcmp($date['name'],"info:eu-repo/semantics/masterThesis")==0)
	$tipo="Tesis de Maestr&iacute;a";
		if (strcmp($date['name'],"info:eu-repo/semantics/doctoralThesis")==0)
	$tipo="Tesis de Doctorado";
			if (strcmp($date['name'],"info:eu-repo/semantics/report")==0)
	$tipo="Reporte";
    echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=format%3A%22',$date['name'],'%22">',$tipo,'</a>-',$num,'</li>',PHP_EOL;
		$sum+=$date;
	}
}
echo '<li>Total ',number_format($sum),'</li>';
echo '</ul>';

?>
