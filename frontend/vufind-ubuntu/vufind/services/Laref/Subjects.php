<?php
require_once 'Action.php';
class Subjects extends Action
{
    function __construct()
    {
        global $interface;
        global $configArray;
		global $output; 
		global $vurl;
		global $vbiblio;
		global $vstats;
		 $vurl=$configArray['Site']['url'];
		 $vbiblio=$configArray['Index']['url'];
		 $vstats=$configArray['Statistics']['solr'];
		
        $interface->setTemplate('subjects.tpl');
        $interface->setPageTitle('Temas');
	
		$interface->assign('solr',':8080/solr');	

        $interface->display('layout.tpl');
    }
}
?>