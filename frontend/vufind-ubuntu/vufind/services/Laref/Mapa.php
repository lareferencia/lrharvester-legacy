<?php
/**

 */
require_once 'Action.php';

/**
 * Paises action for Laref module
 *
 * @category VuFind
 * @package  Controller_Admin
 * @author   Andrew S. Nagy <vufind-tech@lists.sourceforge.net>
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
class Mapa extends Action
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
	$output1="";
	$output2="";	
	$output3="";	
	$output4="";
		$output7="";
		$output8="";
		$output9="";		
		
		$url=$configArray['WebServices']['ws']."/public/listNetworks";
		$json = file_get_contents($url);
		$data = json_decode($json, TRUE);
//echo $url;
	$output1.= "<table border='1'>";
	$output1.=  "<tr> <th>Red</th> <th>Fecha de &uacute;ltima actualizaci&oacute;n</th><th>Registros incorporados</th></tr>";
		foreach($data as $red){
			foreach($red as $key => $value){
			  if ($key==="name")
				$output1 .=  "<tr> <td><a href= '$vurl/Search/Results?lookfor=&type=AllFields&filter[]=country%3A%22".$value."%22'>$value</a> </td>";
			  if ($key==="datestamp")
				$output1 .= "<td>".substr($value,0,10)."</td>";
			  /*if ($key==="size")
				$output1 .= "<td>".number_format((int)$value)."</td>";*/
			  if ($key==="validSize")
			  {
				$output1 .= "<td> ".number_format((int)$value)."</td></tr>";
				$sum+=$value;
				}
			}
		}
			$output1.=  "<tr> <th></th> <th></th><th>".number_format((int)$sum)."</th></tr>";

		 $output1 .= '</table>';

		
        // Load SOLR Paises
        $solr = ConnectionManager::connectToIndex();

		        // Paises Socios
        $result = $solr->search(
            '*:*', null, null, 0, null,
            array('field' => array('country','country_iso'),'sort' => 'index'), '', null, null, null,
            HTTP_REQUEST_METHOD_GET
        );
        if (!PEAR::isError($result)) {
            if (isset($result['facet_counts']['facet_fields']['country'])) {
                $interface->assign(
                    'countryList', $result['facet_counts']['facet_fields']['country']
                );
            }

            if (isset($result['facet_counts']['facet_fields']['country_iso'])) {
                $interface->assign(
                    'cisoList',
                    $result['facet_counts']['facet_fields']['country_iso']
                );
            } 
 }

        $interface->assign('output1',$output1);		

		
		$interface->setTemplate('mapa.tpl');
        $interface->setPageTitle('Mapa de Paises Socios');
        $interface->display('layout.tpl');
    }
}

?>