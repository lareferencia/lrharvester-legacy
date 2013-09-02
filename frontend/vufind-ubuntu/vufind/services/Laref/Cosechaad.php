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
class Cosechaad extends Action
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
		global $networks;
		
		 $vurl=$configArray['Site']['url'];
		 $vbiblio=$configArray['Index']['url'];
		 $vstats=$configArray['Statistics']['solr'];
		 		 $ws=$configArray['WebServices']['ws'];
		 $lnetworks=$networks;

$ncountry="";
$nname="";
$nvalidSize=0;

if(isset($_GET["iso"]))
   $ncountry=$_GET["iso"];
   else
 	$ncountry="AR";


$nname=$lnetworks[$ncountry]['name'];
$nvalidSize=$lnetworks[$ncountry]['validSize'];


	$output1="";
	$output2="";	
	$output3="";	
	$output4="";
		$output7="";
		$output8="";
		$output9="";		
		
		$url=$ws."/public/lastGoodKnowSnapshotByCountryISO/$ncountry";
		 //echo $url;
		$json = file_get_contents($url);
		$data = json_decode($json, TRUE);
		$lastid="";
   //echo print_r($data);
	$output1.= "<table border='1'>";
	$output1.=  "<tr> <th>Pa&iacute;s</th><th>ID</th><th>Status</th><th>Fecha de actualizaci&oacute;n</th><th>Registros consultados</th><th>Registros incorporados</th><th>Registros transformados</th><th>Metadatos</th><th>Metadatos </th></tr>";

			foreach($data as $key => $value){
			//echo print_r($value);
				if ($key==="id")
				{
				    $ni=$value;
					$lastid=$value;
					//echo $value."-";
				}	
				if ($key==="status")
				{			
					  	$output1 .=  "<tr><td>$ncountry</td><td>".$ni."</td>";
						$output1 .= "<td>".$value."</td>";
						$valtem=$value;
					 }
				 if ($key==="endTime")
					  {
						$output1 .= "<td>".substr($value,0,10)."</td>";
						$datetemp='new Date("'.str_replace('-','/',substr($value,0,10)).'").getTime()';
					 }
				 if ($key==="size")
					 {
						$output1 .= "<td> ".number_format((int)$value)."</td>";	$nsize=$value;

					}
			 if ($key==="validSize")
					 {
						$output1 .= "<td> ".number_format((int)$value)."</td>";
						$valtemp=$value;

						
					   }

			if ($key==="transformedSize")
					 {
						$output1 .= "<td> ".number_format((int)$value)."</td>";	
						$output1 .=  '<td><a href="#" style="font-family:Verdana;font-size:8pt;color:#2E8CB4;text-decoration: none" onclick="window.open(\''.$vurl.'/Laref/metadataOccurrence?id='.$ni.'\',\'Recibidos\',\'width=200,height=330,scrollbars=yes\');return false;">Recibidos</a></td>';
						$output1 .=  '<td><a href="#" style="font-family:Verdana;font-size:8pt;color:#2E8CB4;text-decoration: none" onclick="window.open(\''.$vurl.'/Laref/rejectedField?id='.$ni.'\',\'Rechazados\',\'width=200,height=330,scrollbars=yes\');return false;">Rechazados</a></td></tr>';
  				  	    //$output1 .=  "<td><a href='".$vurl."/Laref/metadataOccurrence?id=".$ni."'>Recibidos</a></td>";
					  	//$output1 .=  "<td><a href='".$vurl."/Laref/rejectedField?id=".$ni."'>Rechazados</a></td></tr>";
						
					}
	   }

		 $output1 .= '</table>';

	$url2=$ws."/public/listSnapshotsByCountryISO/$ncountry";
	$json2 = file_get_contents($url2);
	$data2 = json_decode($json2, TRUE);

	$countr="";
    $countn=0;	
    $ni="";	
	
	//echo $url2;
	 $datetemp="";
	$ni="";
	$countr="";
	$first=true;
	$first2=true;
	$output8.="var d1=[";
	$output9.="var d2=[";
	$output7.= "<table border='1'>";
	$output7.=  "<tr> <th>Pa&iacute;s</th><th>ID</th><th>Status</th><th>Fecha de actualizaci&oacute;n</th><th>Registros consultados</th><th>Registros incorporados</th><th>Registros transformados</th><th>Metadatos</th><th>Metadatos </th></tr>";
		foreach($data2 as $red){
			foreach($red as $key => $value){
			
			//echo print_r($value);
			
				if ($key==="id")
				{
				    $ni=$value;
					//echo $value."-";
				}	
				if ($key==="status")
				{			
					  	$output7 .=  "<tr><td>$ncountry</td><td>".$ni."</td>";
						$output7 .= "<td>".$value."</td>";
						$valtem=$value;
					 }
				else if ($key==="endTime")
					  {
						$output7 .= "<td>".substr($value,0,10)."</td>";
						$datetemp='new Date("'.str_replace('-','/',substr($value,0,10)).'").getTime()';
					 }
				else if ($key==="size")
					 {
						$output7 .= "<td> ".number_format((int)$value)."</td>";	
						if (!$first2)
							{
								$output9.=',['.$datetemp.','.$value.']';
								
							}
							else
							{
								$first2=false;
								$output9.='['.$datetemp.','.$value.']';
							}
					}
			if ($key==="transformedSize")
					 {
					 
						$output7 .= "<td> ".number_format((int)$value)."</td>";	
  				  	    $output7 .=  '<td><a href="#" style="font-family:Verdana;font-size:8pt;color:#2E8CB4;text-decoration: none" onclick="window.open(\''.$vurl.'/Laref/metadataOccurrence?id='.$ni.'\',\'Recibidos\',\'width=200,height=330,scrollbars=yes\');return false;">Recibidos</a></td>';
						$output7 .=  '<td><a href="#" style="font-family:Verdana;font-size:8pt;color:#2E8CB4;text-decoration: none" onclick="window.open(\''.$vurl.'/Laref/rejectedField?id='.$ni.'\',\'Rechazados\',\'width=200,height=330,scrollbars=yes\');return false;">Rechazados</a></td></tr>';
					}

					else if ($key==="validSize")
					 {
						$output7 .= "<td> ".number_format((int)$value)."</td>";


						$valtemp=$value;
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
			
		 $output7.= '</table>';		 
		 $output8.='];';
		 $output9.='];';
		 $output = '<ul>';

		$interface->assign('output1',$output1);
		$interface->assign('output7',$output7);
		$interface->assign('output8',$output8);
		$interface->assign('output9',$output9);
		$interface->assign('ncountry',$ncountry);
		$interface->assign('nname',$nname);
		$interface->assign('nvalidSize',$nvalidSize);		
		$interface->assign('lastid',$lastid);
		$interface->assign('nsize',$nsize);
		
				$interface->assign('ws',$ws);	

		$interface->setTemplate('cosechaad.tpl');
        $interface->setPageTitle('Cosechas '.$nname);
        $interface->display('layout.tpl');
    }
}

?>