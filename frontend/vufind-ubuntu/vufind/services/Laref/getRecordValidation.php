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
class getRecordValidation extends Action
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
			

			$output7.= "<table border='0' style='font-family:Verdana;font-size:8pt;background-color:#E5ECF9'>";

			$output7.= "<tr> <th> REGISTRO ORIGINAL</th></tr>";
			$url2=$ws."/public/validateOriginalRecordByID/$id";
			$output7.= "<tr> <td><i>$oid</i></tr>";
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
			//$output7.=  "<tr> <th>Pa&iacute;s</th><th>ID</th><th>Identificador</th><th>Estatus</th><th>Detalle</th><th></th></tr>";
				foreach($data2 as $red){
					foreach($red as $key => $value){
					//echo "::::";
					// print_r($value);
					
					foreach($value as $key2 => $value2){
					
					//echo "****"; 
					//print_r($key2);
					// print_r($value2);
					
						if ($key2==="results")
						{
						$output7.=  "<tr> <td><i>&nbsp;&nbsp;&nbsp;Recibido</i></td></tr>";
						foreach($value2 as $key3 => $value3){
							foreach($value3 as $key4 => $value4){
								if ($key4==="valid")
								{
									if ($value4)
										$output7.=  "<tr> <td>&nbsp;&nbsp;&nbsp;&nbsp;V&Aacute;LIDO</td></tr>";
									else
										$output7.=  "<tr> <td>&nbsp;&nbsp;&nbsp;&nbsp;INV&Aacute;LIDO</td></tr>";
								}
								else
								 $output7.= "<tr> <td><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$key4</i> = $value4</td></tr>";
							}
						   }
									$output7.= "<tr><td>&nbsp;</td></tr>";	
						}
						if ($key2==="fieldName")
						{
								 $output7.= "<tr> <td><b><i>$value2</i></b></td></tr>";
						}
						if ($key2==="valid")
								{
									if ($value2)
										$output7.= "<tr> <td>V&Aacute;LIDO</td></tr>";
									else
										$output7.= "<tr> <td>INV&Aacute;LIDO</td></tr>";
								}
						if ($key2==="mandatory")
								{
									if ($value2)
										$output7.= "<tr> <td>OBLIGATORIO</td></tr>";
									else
										$output7.= "<tr> <td>NO OBLIGATORIO</td></tr>";
								}	

							
					if ($key2==="id")
						{
						//echo print_r($value2);
							$ni=$value2;
							//echo $value."-";
						}	
						if ($key2==="identifier")
						{			
								//$output7 .=  "<tr><td>$iso</td><td>".$ni."</td>";
								//$output7 .= "<td>".$value2."</td>";
								$valtem=$value;
							 }
						else if ($key2==="status")
							  {
								//$output7 .= "<td>".$value2."</td>";
							 }
						else if ($key2==="belongsToCollectionDetails")
							 {
								//$output7 .= "<td>".$value2."</td>";

							}
						
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
			$interface->setTemplate('getRecordValidation.tpl');
			$interface->setPageTitle('getRecordValidation');
			$interface->display('blank.tpl');
		}
}

?>