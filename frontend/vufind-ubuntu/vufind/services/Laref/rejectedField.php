<?php
/**

 */
require_once 'Action.php';

/**
 * getRecordValidation action for Laref module
 *
 * @category VuFind
 * @package  Laref
 * @author   Antonio Razo <>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 */
class rejectedField extends Action
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
		$ws=$configArray['WebServices']['ws'];

		if(isset($_GET["id"]))
		{
			$url2="";
			$output7="";
			
			$id=$_GET["id"];
			$oid="";
			
			if(isset($_GET["oid"]))
			 $oid=$_GET["oid"];
			

			$output7.= "<table border='0' style='font-family:Verdana;font-size:8pt'>";

			$output7.= "<tr><th>METADATOS</th><th>RECHAZADOS</th></tr>";
			$url2=$ws."/public/rejectedFieldCountBySnapshotId/$id";
			$output7.= "<tr><td>&nbsp;</td></tr>";
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
			$link="";
			$lurl="";
			$outputN="";
			$par=false;
			//$output7.=  "<tr> <th>Pa&iacute;s</th><th>ID</th><th>Identificador</th><th>Estatus</th><th>Detalle</th><th></th></tr>";
				foreach($data2 as $red){
					foreach($red as $key => $value){
					//echo "::::";
					//print_r($value);
					if ($par)
					{
					$output7.= "<td>".number_format((int)$value)."</td></tr>";
					$par=false;
					}
					else
					{
					$output7.= "<tr><td><b><a style='font-family:Verdana;font-size:8pt;color:#2E8CB4;text-decoration: none' href='getInvalidRecordValidation?dc=".$value."&id=".$id."'>".$value."</a></b></td>";
					$par=true;
					}
						
							 } 
						  }
						
					
				 $output7.= '</table>';		 

		//echo $outputN;
		echo  $output7;
		//echo $outputN;
		}
		else
		{
		 echo "ERROR";
		}

			$interface->assign('output',$output);
			$interface->setTemplate('rejectedField.tpl');
			$interface->setPageTitle('rejectedField');
			$interface->display('blank.tpl');
		}
}

?>