<?php
/**

 */
require_once 'Action.php';

/**
 * Impacto action for LaRef module
 *
 */
class Busquedas extends Action
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


        // Load SOLR Statistics
        $solr = ConnectionManager::connectToIndex('SolrStats');
		

        // Search Statistics
        $result = $solr->search(
            'phrase:[* TO *]', null, null, 0, null,
            array('field' => array('noresults', 'phrase')),
            '', null, null, null, HTTP_REQUEST_METHOD_POST
        );
        if (!PEAR::isError($result)) {
            $interface->assign('searchCount', $result['response']['numFound']);

            // Extract the count of no hit results by finding the "no hit" facet
            // entry set to boolean true.
            $nohitCount = 0;
            $nhFacet = & $result['facet_counts']['facet_fields']['noresults'];
            if (isset($nhFacet) && is_array($nhFacet)) {
                foreach ($nhFacet as $nhRow) {
                    if ($nhRow[0] == 'true') {
                        $nohitCount = $nhRow[1];
                    }
                }
            }
            $interface->assign('nohitCount', $nohitCount);

            $interface->assign(
                'termList', $result['facet_counts']['facet_fields']['phrase']
            );
        }

        // Record View Statistics
        $result = $solr->search(
            'recordId:[* TO *]', null, null, 0, null,
            array('field' => array('recordId')),
            '', null, null, null, HTTP_REQUEST_METHOD_POST
        );
        if (!PEAR::isError($result)) {
            $interface->assign('recordViews', $result['response']['numFound']);
            $interface->assign(
                'recordList', $result['facet_counts']['facet_fields']['recordId']
            );
        }

        $interface->setTemplate('busquedas.tpl');
        $interface->setPageTitle('Impacto :: B&uacute;squedas');
        $interface->display('layout.tpl');
    }
}

?>