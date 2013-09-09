<?php
/**

 */
require_once 'Action.php';

/**
 * Origen action for LaRef module
 *
 */
class PorMaterialp extends Action
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
		 $lnetworks=$networks;

		 //print_r($configArray);

$ncountry="";
$nname="";
$nvalidSize=0;

if(isset($_GET["iso"]))
   $ncountry=$_GET["iso"];
   else
 	$ncountry="AR";
	
$nname=$lnetworks[$ncountry]['name'];
		 	
	$output8="";
	

	//print_r($connection);
	
	//echo $connection['host'];
	//echo $connection['user'];
	//echo $connection['pass'];

	// Make a MySQL Connection
	$ConnectionString=$configArray['Database']['database'];	
	$connection=array();
	$connection=parse_url($ConnectionString);
	
	mysql_connect($connection['host'], $connection['user'], $connection['pass']) or die(mysql_error());
	mysql_select_db(str_replace("/","",$connection['path'])) or die(mysql_error());

	// Get all the data from the "example" table
	$result = mysql_query("SELECT count(*) as total,red,type from record WHERE red='".$ncountry."' group by red,type order by type") 
	or die(mysql_error());  
	$table="";
	$list="";
	$list.="";

	$cont=0;

	// keeps getting the next row until there are no more to get
	while($row = mysql_fetch_array( $result )) {

		$table.=  "{data:d".$cont.",label:'".$row['type']."'},";
		$list.="d".$cont."=[[0,".$row['total']."]],";
		$cont++;
	}	
	$list.="";
//	$table.=  "</table>";			 
	$table.=  "";			 

// Get all the data from the "example" table
$result = mysql_query("SELECT count(*) as total,red,type from record WHERE red='".$ncountry."' group by red,type order by total desc") 
or die(mysql_error());  

$output8.="<table border='1' >";
$output8.="<tr><th>Pa&iacute;s</th> <th>Material</th> <th>Consultas</th></tr>";
// keeps getting the next row until there are no more to get
while($row = mysql_fetch_array( $result )) {
	// Print out the contents of each row into a table
	$output8.="<tr><td>"; 
	$output8.=$row['red'];
	$output8.="</td><td>"; 
	$output8.=$row['type'];
	$output8.="</td><td>"; 
	$output8.=$row['total'];
		$output8.="</td><td>"; 	 
}
		 $output8.="</table>";			 

		$interface->assign('ncountry',$ncountry);
		$interface->assign('nname',$nname);		 
		$interface->assign('list',$list);
		$interface->assign('table',$table);		
		$interface->assign('output8',$output8);
        $interface->setTemplate('pormaterialp.tpl');
        $interface->setPageTitle('Impacto por Material - '.$nname);
        $interface->display('layout.tpl');
    }
}

?>