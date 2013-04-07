<?php
header('Content-Type: text/html'); 
//http://localhost:8080/solr/biblio/select?facet=true&facet.field=format&fl=format&q=format:[*%20TO%20*]&facet.limit=100&rows=10
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=format&fl=format&q=format'.urlencode(':[* TO *]').'&facet.limit=5&rows=0&facet.sort=count';
//echo 'DB ',$url2,'<br>';

$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
//echo 'PASO <br>';
if($xml2 === FALSE)
{
   //deal with error
echo 'ERROR';
}
else 
{ //do stuff 

  $first=true;
  $cuenta=0;
  echo 'var',PHP_EOL;
foreach ($xml2->xpath("//lst[@name='format']/int") as $tipo) 
    {
      $cuenta++;
     if ($first)
	{
         echo 'd',$cuenta,'=[[0,',$tipo,']]',PHP_EOL;
	$first=false;
	}
	else
         echo ', d',$cuenta,'=[[0,',$tipo,']]',PHP_EOL;
    }
    echo ',';

$cuenta=0;
$first=true;
echo 'graph;',PHP_EOL;
echo 'graph = Flotr.draw(container, [ ',PHP_EOL;
foreach ($xml2->xpath("//lst[@name='format']/int") as $tipo) 
    {
      $cuenta++;
     if ($first)
	{
         echo '{ data:d',$cuenta,',label:"',$tipo['name'],'"}',PHP_EOL;
	$first=false;
	}
	else
         echo ',{ data:d',$cuenta,',label:"',$tipo['name'],'"}',PHP_EOL;
    }
    echo '],'; 
}

?>
