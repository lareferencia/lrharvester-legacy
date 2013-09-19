<?php
/**

 */
require_once 'Action.php';

/**
 * Origen action for LaRef module
 *
 */
class Licencias extends Action
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

		$output = '<ul>';

		$url2 = $vbiblio.'/biblio/select?facet=true&facet.field=country&fl=country&q=type'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=index';
		$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
		//$output .=$url2;

		//echo $url2;
		if($xml2 ===  FALSE)
		{
		   //deal with error
		$output .=  '<h2>ERROR</h1>';
		}
		else 
		{ //do stuff 
			$sum=0;
			$num=0;
			$output .=  '<ul >';
	 	foreach ($xml2->xpath("//lst[@name='country']/int") as $country) 
			{
			$num=number_format((int)$country);
			
			$pais=$country['name'];
			
		  //  echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=country%3A%22',$pais,'%22">',$pais,'</a> - ',$num,'</li>',PHP_EOL;
		  
		  $output .=  '<li>'.$pais.' ('.$num.')</li>';
		 
					$sum+=$country;

		$url3 = $vbiblio.'/biblio/select?facet=true&facet.field=rights&fl=rights&q=country:'.urlencode('"'.$pais.'"').'&facet.limit=5&rows=0';
	//$output .=$url3;
	
		
		//echo $url3;
		$xml3 = simpleXML_load_file($url3,"SimpleXMLElement",LIBXML_NOCDATA);
		if($xml3 ===  FALSE)
		{
		   //deal with error
		}
		else { 

			$output .=  '<ul>';
		foreach ($xml3->xpath("//lst[@name='rights']/int") as $inst) 
			{
			$num=number_format((int)$inst);
			if($inst>0)
			$output .=  '<li><a href="'.$vurl.'/Search/Results?lookfor=&type=AllFields&filter%5B%5D=rights%3A%22'.$inst['name'].'%22">'.$inst['name'].'</a> - '.number_format((int)$inst).'</li>';
				$sum+=$inst;
			}/**/
		}
		//$output .=  '<li>&nbsp;</li>';

		$output .=  '</ul>';
		}
		} 

		$output .=  '</ul>';		

/////////////////////////////////////////////////////////////////////

//$output2 .=  '<ul>';
$url2 = $vbiblio.'/biblio/select?facet=true&facet.field=rights&fl=rights&q=rights'.urlencode(':[* TO *]').'&facet.limit=5&rows=0';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);

//echo $url2;
if($xml2 ===  FALSE)
{
   //deal with error
$output2 .= '<h2>ERROR</h1>';
}
else 
{ //do stuff 
    $sum=0;
	$num=0;
	//$output .='<ul>';
foreach ($xml2->xpath("//lst[@name='rights']/int") as $rights) {
	
	
	$tipos=$rights['name'];
	
	//$output2 .='<li>'.$rights.': '.number_format((int)$rights).' </li>';
    $output2 .='barTicks.push(['.$num.', "'.$tipos.'"]);';
	$output2 .='barData.push(['.$rights.','.$num.']);';
    $output2 .='barLabels.push(['.$num.','.$rights.']);';	
	$num++;
	}
}

		$interface->assign('output',$output);
		$interface->assign('output2',$output2);

        $interface->setTemplate('licencias.tpl');
        $interface->setPageTitle('Tipos de Licencias');
        $interface->display('layout.tpl');
    }
}

?>