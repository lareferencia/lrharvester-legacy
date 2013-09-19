<?php
/**

 */
require_once 'Action.php';

/**
 * Origen action for LaRef module
 *
 */
class PorPais extends Action
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


	$output6="";		

	
		// Make a MySQL Connection
		$ConnectionString=$configArray['Database']['database'];	
		$connection=array();
		$connection=parse_url($ConnectionString);
		
		mysql_connect($connection['host'], $connection['user'], $connection['pass']) or die(mysql_error());
		mysql_select_db(str_replace("/","",$connection['path'])) or die(mysql_error());
		 
$result = mysql_query("SELECT count(*) as total,ccode,type from record WHERE ccode<>'' OR not(NULL) group by ccode,type order by ccode") 
or die(mysql_error());  

$output6.="<table border='1' >";
$output6.="<tr> <th>Pa&iacute;s</th> <th>Material</th> <th>Consultas</th></tr>";
// keeps getting the next row until there are no more to get
while($row = mysql_fetch_array( $result )) {
	// Print out the contents of each row into a table
	$output6.="<tr><td>"; 
	$output6.=$row['ccode'];
	$output6.="</td><td>"; 
	$output6.=$row['type'];
	$output6.="</td><td>"; 
	$output6.=$row['total'];
		$output6.="</td><td>"; 	 
}
		 $output6.="</table>";	


		$interface->assign('output6',$output6);

        $interface->setTemplate('porpais.tpl');
        $interface->setPageTitle('Impacto por Pa&iacute;s');
        $interface->display('layout.tpl');
    }
}

?>