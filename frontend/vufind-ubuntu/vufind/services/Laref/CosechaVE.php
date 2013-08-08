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
class CosechaVE extends Action
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

		$output7="";
		$output8="";
	
		$url2=$configArray['WebServices']['ws']."/public/listNetworksHistory";
		$json2 = file_get_contents($url2);
		$data2 = json_decode($json2, TRUE);

	$countr="";
    $countn=0;	
    $ni="";	
	

	$output8.="var ";
	$output7.= "<table border='1'>";
	$output7.=  "<tr> <th>Red</th><th>Pa&iacute;s</th><th>ID</th><th>Fecha de actualizaci&oacute;n</th><th>Registros incorporados</th></tr>";
		foreach($data2 as $red){
			foreach($red as $key => $value){
				if ($key==="networkID")
				{
				    $ni=$value;
				}	
				if ($key==="country")
				{	$countr=$value;
				}
				if ($key==="validSnapshots")
				{
				if ($countr==="VE")
				{
							$countn++;
							$first=true;
							$output8.="d".$countn."=[";
					  $datetemp="";
					  $valtemp=0;
					   foreach($value as $snap){
					   foreach($snap as $key2 => $value2){
						 { 
						  if ($key2==="id")
						{					
							$output7 .=  "<tr><td>".$ni."</td>";
							$output7 .=  "<td><a href='".$vurl."/Search/Results?lookfor=&type=AllFields&filter[]=country_iso%3A%22".$countr."%22'>$countr</a> </td>";;
							$output7 .= "<td>".$value2."</td>";
							$valtem=$value2;
						 }
						  else if ($key2==="endTime")
						  {
							$output7 .= "<td>".substr($value2,0,10)."</td>";
							$datetemp='new Date("'.str_replace('-','/',substr($value2,0,10)).'").getTime()';
						 }
						 else if ($key2==="validSize")
						 {
						 
							$output7 .= "<td> ".number_format((int)$value2)."</td></tr>";
							$valtemp=$value2;
							if (!$first)
								{
									$output8.=',['.$datetemp.','.$valtemp.']';
									
								}
								else
								{
									$first=false;
									$output8.='['.$datetemp.','.$valtemp.']';
								}
							
						   }
						  }

						}
							}
					
					  $output8.='];';
					}  
				  }
				}
			}
		 $output7.= '</table>';		 
		 

		$interface->assign('output7',$output7);
		$interface->assign('output8',$output8);
		
		$interface->setTemplate('cosechave.tpl');
        $interface->setPageTitle('Cosechas por Pa&iacute;s');
        $interface->display('layout.tpl');
    }
}

?>