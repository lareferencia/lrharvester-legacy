<?php
/**

 */
require_once 'Action.php';

/**
 * Impacto action for LaRef module
 *
 */
class ImpactoRec extends Action
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
	
		$output10="";
		$output11="";
		$banks = array();

	global $ccode;$ccode="";
	
			$url=$configArray['WebServices']['ws']."/public/listProviderStats";
		$json = file_get_contents($url);
		$data = json_decode($json, TRUE);
		
	$output8="";
		// Make a MySQL Connection
		$ConnectionString=$configArray['Database']['database'];	
		$connection=array();
		$connection=parse_url($ConnectionString);
		
		mysql_connect($connection['host'], $connection['user'], $connection['pass']) or die(mysql_error());
		mysql_select_db(str_replace("/","",$connection['path'])) or die(mysql_error());
		
		$output10.= "<table border='1'>";
		$output10.=  "<tr> <th>Pais</th> <th>Cuenta</th></tr>";
//		foreach($data as $red){
			foreach($data as $key => $value){
			//  echo print_r($value)."-";
			  if ($key==="content")
			  {

			  	   foreach($value as $snap){
				   foreach($snap as $key2 => $value2){
					 { 
					
					 // echo print_r($value2)."******";
					  if ($key2==="ipAddress")
					{	

						
						
							// Get all the data from the "example" table
							$result = mysql_query("SELECT ccode FROM geoip WHERE INET_ATON('".$value2."') BETWEEN startipi AND endipi LIMIT 1") 
							or die(mysql_error()); 
							while($row = mysql_fetch_array( $result )) {
							$ccode= $row['ccode'];
							}
						
					  	$output10.=  "<tr><td>$ccode</td>";
						//$output10.= "<td>".$value2."</td>";
						$valtem=$value2;
					 }
					  else if ($key2==="requestCount")
					  {
					  $banks[] = array('name'=>$ccode,'amount'=>$value2);
					  //echo $ccode."-".$value2;
					   $output10.= "<td>".number_format((int)$value2)."</td></tr>";
					 }

					  }

				    }
			        }
			  
			  
			  
				}
			}
//		}
		 $output10 .= '</table>';

		 // for search if a bank has been added into $amount, returns the key (index)
function bank_exists($bankname, $array) {
    $result = -1;
    for($i=0; $i<sizeof($array); $i++) {
        if ($array[$i]['name'] == $bankname) {
            $result = $i;
            break;
        }
    }
    return $result;
}	
		 
	// begin the iteration for grouping bank name and calculate the amount
$amount = array();
foreach($banks as $bank) {
    $index = bank_exists($bank['name'], $amount);
    if ($index < 0) {
        $amount[] = $bank;
    }
    else {
        $amount[$index]['amount'] +=  (int)$bank['amount'];
    }
}
//print_r($amount); //display 	 
//['Country', 'Consultas'],['AR',2],['AR',2]
$output11.="['Country', 'Consultas']";
foreach($amount as $a) {
$output11.=",['".$a['name']."',".$a['amount']."]";
}
//echo $output11;		  

		$interface->assign('output10',$output10);
		$interface->assign('output11',$output11);
        $interface->setTemplate('impactorec.tpl');
        $interface->setPageTitle('Impacto Por Pais Recolector');
        $interface->display('layout.tpl');
    }
}

?>