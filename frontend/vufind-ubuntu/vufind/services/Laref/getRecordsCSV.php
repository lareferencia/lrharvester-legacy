<?php
ini_set('memory_limit', '-1');
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
class getRecordsCSV extends Action
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

header('Content-Type: text/plain');
header("Content-Disposition: attachment; filename=reporte.csv");

 $id=$_GET["id"];

if(isset($_GET["iso"]))
   $iso=$_GET["iso"];
   else
 	$iso="AR";
	
if(isset($_GET["url"]))
   $url2=$_GET["url"];
   else
 	$url2=$ws."/public/listInvalidRecordsInfoBySnapshotID/92";
	
	$json2 = file_get_contents($url2."?page=0&size=500000");
	$data2 = json_decode($json2, TRUE);

	$countr="";
    $countn=0;	
    $ni="";	
	
//	echo $url2;
	$datetemp="";
	$ni="";
	$countr="";
	$first=true;
	$first2=true;
    $link="";
	$lurl="";
	$outputN="";
	$output7="";
//	$output7.= "<table border='0' style='font-family:Verdana;font-size:8pt;background-color:#E5ECF9'>";
//	$output7.=  "<tr> <th>Pa&iacute;s</th><th>ID</th><th>Identificador</th><th>Estatus</th><th>Detalle</th><th></th></tr>";
		foreach($data2 as $red){
			foreach($red as $key => $value){
			foreach($value as $key2 => $value2){
			//echo print_r($key2);
			//echo print_r($value2);
			//echo "****************************************************";
				if ($key2==="rel")
				{
				//echo print_r($value2);
				
				    
				  //  $link=$value2;
					if ($link=="next") $link="siguiente";
					if ($link=="first") $link="primera";
					if ($link=="last") $link="&uacute;ltima";
					if ($link=="self") $link="actual";
					if ($link=="previous") $link="previa";
					//echo $value."-";
				}
				if ($key2==="href")
				{
				//$outputN.= "<a style='font-family:Verdana;font-size:8pt;color:#2E8CB4;text-decoration: none' href='http://200.0.207.91/vufind/getRecords.php?iso=".$iso."&url=".$value2."'>".$link."</a>-";
				}
			if ($key2==="id")
				{
				//echo print_r($value2);
				    $ni=$value2;
					//echo $value."-";
				}	
				if ($key2==="identifier")
				{			
					  	$output7 .=  "\"$iso\",".$ni;
						$output7 .= ",\"".$value2;
						$valtem=$value2;
					 }
				else if ($key2==="status")
					  {
						$output7 .= "\",\"".$value2;
					 }
				else if ($key2==="belongsToCollectionDetails")
					 {
						$output7 .= "\",\"".$vurl."/Laref/getRecordValidation?id=".$ni."&oid=".$valtem;
						$output7 .= "\",\"".$vurl."/Laref/getTransformedRecordValidation?id=".$ni."&oid=".$valtem;
						$output7 .= "\",\"".$value2."\"\n";

					}
			    
					 } 
				  }
				}
			
		// $output7.= '</table>';		 

//echo $outputN;
echo  $output7;
//echo $outputN;

			$interface->assign('output7',$output7);
		//	$interface->assign('outputN',$outputN);
			$interface->setTemplate('getRecordsCSV.tpl');
			$interface->setPageTitle('getRecordsCSV');
			$interface->display('blankt.tpl');
		
	}
}

?>