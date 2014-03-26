<?php
/**

 */
require_once 'Action.php';

/**
 * Fechas action for LaRef module
 *

 */
class Fechas extends Action
{
    /**
     * Process parameters and display the page.
     *
     * @return void
     * @access public
     */
    public function launch()
    {
        global $configArray;
        global $interface;
		global $output; 
		 $vurl=$configArray['Site']['url'];
		 $vbiblio=$configArray['Index']['url'];
		 $vstats=$configArray['Statistics']['solr'];
	$output1="";
	$output2="";	
	$output3="";	
	$output4="";	
	$output9="";
	$output10="";


$url9 = $vbiblio.'/biblio/select?facet=true&facet.limit=-1&rows=0&facet.sort=index&q=*:*&facet.field=publishDate&facet.mincount=25';
//$url9 = $vurl.'/biblio/select?facet=true&facet.field=publishDate&fl=publishDate&q=publishDate'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=index';
$xml9 = simpleXML_load_file($url9,"SimpleXMLElement",LIBXML_NOCDATA);


if($xml9 ===  FALSE)
{
   //deal with error
echo 'ERROR';
}
else 
{ 

//do stuff 
	//echo date("Y"); 
$output9.='var d1 = [';
  $first=true;
  $cuenta=0;
  
foreach ($xml9->xpath("//lst[@name='publishDate']/int") as $date) 
    {
      $cuenta++;
	  $output10.='<a href="'.$vbiblio.'/Search/Results?join=AND&bool0[]=AND&lookfor0[]=&type0[]=AllFields&lookfor0[]=&type0[]=AllFields&lookfor0[]=&type0[]=AllFields&submit=Buscar&daterange[]=publishDate&publishDatefrom='.$date['name'].'&publishDateto='.$date['name'].'">'.$date['name'].'</a>('.$date.') ';



	//echo $date['name'];
	//if ((int)$date['name']<=date("Y"))
     if ($first)
	{
         $output9.='[new Date("'.$date['name'].'/01/01 01:00").getTime(),'.$date.']';
	$first=false;
	}
	else
         $output9.=',[new Date("'.$date['name'].'/01/01 01:00").getTime(),'.$date.']';
    
    }
   $output9.='];';
 
}

		$interface->assign('output9',$output9);
		$interface->assign('output10',$output10);
        $interface->setTemplate('fechas.tpl');
        $interface->setPageTitle('Fechas');
        $interface->display('layout.tpl');
    }
}

?>
