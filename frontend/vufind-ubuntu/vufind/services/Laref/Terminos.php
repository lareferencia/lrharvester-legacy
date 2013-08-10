<?php
/**

 */
require_once 'Action.php';

/**
 * Material action for LaRef module
 *

 */
class Terminos extends Action
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
		 $vterminos_min=$configArray['Terminos']['terminos_detalle'];
		 
	
	$output4="";	

////////////////////////////////////////////////////////////////////		

$url4 = $vbiblio.'/biblio/select?q='.urlencode('topic_browse:[* TO *]').'&wt=xml&facet=true&facet.field=topic_browse&fl=topic_browse&facet.mincount='.$vterminos_min.'&facet.sort=index';
//$output4=$url4;
$xml4 = simpleXML_load_file($url4,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml4 ===  FALSE)
{
   //deal with error
	$output4 .= '<h2>ERROR</h1>';
}
else { //do stuff 

//echo $xml;

foreach ($xml4->xpath("//lst[@name='topic_browse']/int") as $busqueda) {
 if (($busqueda/100)>12)
    $output4.="<a style='font-size:".($busqueda/100)."px;text-decoration:none;' href='".$vurl."/Search/Results?lookfor=%22".$busqueda["name"]."%22&type=topic_browse'>".$busqueda["name"]."(".$busqueda.") </a>";
else
    $output4.="<a style='font-size:11px;text-decoration:none;' href='".$vurl."/Search/Results?lookfor=%22".$busqueda["name"]."%22&type=topic_browse'>".$busqueda["name"]."(".$busqueda.") </a>";

	}

} 

		$interface->assign('output4',$output4);
        $interface->setTemplate('terminos.tpl');
        $interface->setPageTitle('T&eacute;rminos');
        $interface->display('layout.tpl');
    }
}

?>