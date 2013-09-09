<?php
/**

 */
require_once 'Action.php';

/**
 * Impacto action for LaRef module
 *
 */
class Impacto extends Action
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

	$output1="";
	$output2="";	
	$output3="";	
	$output4="";
	$output5="";
	$output6="";		
	$output8="";
	
	$output8="";
		// Make a MySQL Connection
		$ConnectionString=$configArray['Database']['database'];	
		$connection=array();
		$connection=parse_url($ConnectionString);
		
		mysql_connect($connection['host'], $connection['user'], $connection['pass']) or die(mysql_error());
		mysql_select_db(str_replace("/","",$connection['path'])) or die(mysql_error());
	// Get all the data from the "example" table
	$result = mysql_query("SELECT count(*) as acceso,ccode FROM record WHERE ccode<>'' GROUP BY ccode ORDER BY ccode ") 
	or die(mysql_error());  
	$table="";
	$list="";
	$list.="['Country', 'Consultas']";
	$table.= "<table border='1'>";
	$table.=  "<tr> <th>Pa&iacute;s</th> <th>Consultas</th></tr>";
	// keeps getting the next row until there are no more to get
	while($row = mysql_fetch_array( $result )) {
		// Print out the contents of each row into a table
		$table.=  "<tr><td>"; 
		$table.=  $row['ccode'];
		$table.=  "</td><td>"; 
		$table.=  $row['acceso'];
		$table.=  "</td>"; 
		$table.=  "</td></tr>"; 
		$list.=",['".$row['ccode']."',".$row['acceso']."]";
	}	
	$list.="]);";
	$table.=  "</table>";			 
		 

		$interface->assign('list',$list);
		$interface->assign('table',$table);		

        $interface->setTemplate('impacto.tpl');
        $interface->setPageTitle('Impacto');
        $interface->display('layout.tpl');
    }
}

?>