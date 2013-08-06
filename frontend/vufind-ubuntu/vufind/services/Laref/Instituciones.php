<?php
/**

 */
require_once 'Action.php';

/**
 * Instituciones action for Laref module
 *
 * @category VuFind
 * @package  Laref
 * @author   Antonio Razo <>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 */
class Instituciones extends Action
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

		$sum=0;
		$countr="";
		$countn=0;	
		$ni="";		 
		$saved=array();

		$output = '<ul>';

		$url2 = $vbiblio.'/biblio/select?facet=true&facet.field=country&fl=country&q=type'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=index';
		$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);

		if($xml2 ===  FALSE)
		{
			$output .=  '<h2>ERROR</h2>';
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
					  
					$output .=  '<li>'.$pais.'</li>';

					$sum+=$country;

					$url3 = $vbiblio.'/biblio/select?facet=true&facet.field=instname&fl=instname&q=country:'.urlencode('"'.$pais.'"').'&rows=0&facet.sort=index';

					$xml3 = simpleXML_load_file($url3,"SimpleXMLElement",LIBXML_NOCDATA);
					if($xml3 ===  FALSE)
					{
						//deal with error
					}
					else { 

						$output .=  '<ol>';
						foreach ($xml3->xpath("//lst[@name='instname']/int") as $inst) 
							{
							$num=number_format((int)$inst);
							if($inst>0)


							$output .=  '<li> <a href="'.$vurl.'/Search/Results?lookfor=&type=AllFields&filter%5B%5D=instname%3A%22'.$inst['name'].'%22">'.$inst['name'].'</a> - '.number_format((int)$inst).'</li>';
							$sum+=$inst;
							}
					}
				
					$output .=  '</ol>';
				}
			} 

			$output .=  '</ul>';		

			$interface->assign('output',$output);
			$interface->setTemplate('instituciones.tpl');
			$interface->setPageTitle('Instituciones');
			$interface->display('layout.tpl');
		}
}

?>