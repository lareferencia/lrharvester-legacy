<?php
 $id=$_GET["id"];
$url2 = 'http://200.0.207.91:8090/solr/biblio/select?q=oid:"'.$id.'"&fl=url';
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
foreach ($xml2->xpath("//arr [@name='url']/str") as $type) 
    {
	  
  echo $type;

}
} 
?>
