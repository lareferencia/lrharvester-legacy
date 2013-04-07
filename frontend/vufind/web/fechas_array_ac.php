<?php
header('Content-Type: text/html'); 
$url2 = 'http://localhost:8080/solr/biblio/select?facet=true&facet.field=publishDate&fl=publishDate&q=publishDate'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=index';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml2 ==  FALSE)
{
   //deal with error
echo 'ERROR';
}
else 
{ //do stuff 
    echo 'var d1 = [';
  $first=true;
  $cuenta=0;
  $acum=0;
foreach ($xml2->xpath("//lst[@name='publishDate']/int") as $date) 
    {
      $cuenta++;
	$acum+=$date;
    if ($cuenta>14)
    {
     if ($first)
	{
         echo '[new Date("',$date['name'],'/01/01 01:00").getTime(),',$acum,']',PHP_EOL;
	$first=false;
	}
	else
         echo ',[new Date("',$date['name'],'/01/01 01:00").getTime(),',$acum,']',PHP_EOL;
    }
    }
    echo '];';
 
}

?>
