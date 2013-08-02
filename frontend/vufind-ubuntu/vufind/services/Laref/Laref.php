<?php
require_once 'Action.php';
class Laref extends Action
{
    function __construct()
    {
        global $interface;
        global $configArray;
				print_r($configArray);print_r("*****");
        print_r($configArray['Site']['url']);
				print_r("*****");
				print_r($configArray['Index']['url']);print_r("*****");
				print_r($configArray['Statistics']['solr']);
        $interface->setPageTitle('LA Referencia');
        $interface->setTemplate('laref.tpl');
		$interface->assign('solr',':8080/solr');	
         if (isset($configArray['Statistics']['solr'])) {
            $interface->assign('larefsolr', $configArray['Statistics']['solr']);
        }
        $interface->display('layout.tpl');
    }
}
?>