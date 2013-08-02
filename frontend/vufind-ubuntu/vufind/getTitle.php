<?php
 $id=$_GET["id"];
$url2 = 'http://localhost:8080/solr/biblio/select?q=oid:"'.$id.'"&fl=title';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
//echo $url2;
if($xml2 ===  FALSE)
{
   //deal with error
echo '<h2>ERROR</h1>';
}
else 
{ //do stuff 
    $sum=0;
	$num=0;
foreach ($xml2->xpath("//str[@name='title']") as $type) 
    {
	  
  echo $type;

}
} 
?>
