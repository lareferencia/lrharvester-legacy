<?php
/**

 */
require_once 'Action.php';

/**
 * Origen action for LaRef module
 *
 */
class PorMaterialTM extends Action
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

	// Get all the data from the "example" table
	$result = mysql_query("SELECT count(*) as total,red,type from record WHERE ((ccode<>'' OR not(NULL)) AND (type='Tesis de Maestría')) group by red,type order by total desc") 
	or die(mysql_error());  
	$table="";
	$list="";
	$list.="['Country', 'Consultas']";

	// keeps getting the next row until there are no more to get
	while($row = mysql_fetch_array( $result )) {
		// Print out the contents of each row into a table

		$list.=",['".$row['red']."',".$row['total']."]";
	}	
	$list.="]);";
		 
		 
// Get all the data from the "example" table
$result = mysql_query("SELECT count(*) as total,ccode,type from record WHERE ((ccode<>'' OR not(NULL)) AND (type='Tesis de Maestría')) group by ccode,type order by ccode") 
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

// Get all the data from the "example" table
$result = mysql_query("SELECT count(*) as total,red,type from record WHERE ((ccode<>'' OR not(NULL)) AND (type='Tesis de Maestría')) group by red,type order by total desc") 
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
		 
		 

		$interface->assign('list',$list);
		$interface->assign('table',$table);		
		$interface->assign('output6',$output6);
		$interface->assign('output8',$output8);
        $interface->setTemplate('pormaterialtm.tpl');
        $interface->setPageTitle('Impacto por Tesis de Maestr&iacute;a');
        $interface->display('layout.tpl');
    }
}

?>