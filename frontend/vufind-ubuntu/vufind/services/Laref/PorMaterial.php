<?php
/**

 */
require_once 'Action.php';

/**
 * Origen action for LaRef module
 *
 */
class PorMaterial extends Action
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
		
	$output8="";
		// Make a MySQL Connection
		$ConnectionString=$configArray['Database']['database'];	
		$connection=array();
		$connection=parse_url($ConnectionString);
		
		mysql_connect($connection['host'], $connection['user'], $connection['pass']) or die(mysql_error());
		mysql_select_db(str_replace("/","",$connection['path'])) or die(mysql_error());
		
$result = mysql_query("SELECT count(*) as total,red,type from record WHERE ccode<>'' OR not(NULL) OR type<>'' group by red,type order by type,total desc") 
or die(mysql_error());  

$output8.="<table border='1' >";
$output8.="<tr>Material<th></th> <th>Pa&iacute;s</th> <th>Consultas</th></tr>";
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
		 
		 

		$interface->assign('output8',$output8);
        $interface->setTemplate('pormaterial.tpl');
        $interface->setPageTitle('Impacto por Material');
        $interface->display('layout.tpl');
    }
}

?>