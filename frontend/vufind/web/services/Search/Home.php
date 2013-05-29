<?php
/**
 * Home action for Search module
 *
 * PHP version 5
 *
 * Copyright (C) Villanova University 2007.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * @category VuFind
 * @package  Controller_Search
 * @author   Andrew S. Nagy <vufind-tech@lists.sourceforge.net>
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
require_once 'Action.php';

/**
 * Home action for Search module
 *
 * @category VuFind
 * @package  Controller_Search
 * @author   Andrew S. Nagy <vufind-tech@lists.sourceforge.net>
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
class Home extends Action
{
    /**
     * Process incoming parameters and display the page.
     *
     * @return void
     * @access public
     */
    public function launch()
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

$output="";
		 
$url = $vbiblio.'/biblio/select?q='.urlencode('topic_browse:[* TO *]').'&wt=xml&facet=true&facet.field=topic_browse&fl=topic_browse&facet.limit=1000&rows=0&facet.mincount=700&facet.sort=index';
$xml = simpleXML_load_file($url,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml ===  FALSE)
{
   //deal with error
}
else { //do stuff 

//echo $xml;
foreach ($xml->xpath("//lst[@name='topic_browse']/int") as $busqueda) {
    if (($busqueda/100)>12)
    $output.="<a style='font-size:".($busqueda/100)."px;text-decoration:none;' href='".$vurl."/Search/Results?lookfor=%22".$busqueda["name"]."%22&type=topic_browse'>".$busqueda["name"]." </a>";
	else 
	    $output.="<a style='font-size:11px;text-decoration:none;' href='".$vurl."/Search/Results?lookfor=%22".$busqueda["name"]."%22&type=topic_browse'>".$busqueda["name"]." </a>";

}

} 	
		//print_r($output);
		$interface->assign('output',$output);

////////////////////////////////////////////////////////////////////		
		
        // Load SOLR Paises
        $solr = ConnectionManager::connectToIndex();

		        // Paises Socios
        $result = $solr->search(
            '*:*', null, null, 0, null,
            array('field' => array('country', 'type','institution','country_iso'),'sort' => 'index'), '', null, null, null,
            HTTP_REQUEST_METHOD_GET
        );
        if (!PEAR::isError($result)) {
            if (isset($result['facet_counts']['facet_fields']['country'])) {
                $interface->assign(
                    'countryList', $result['facet_counts']['facet_fields']['country']
                );
            }
            if (isset($result['facet_counts']['facet_fields']['type'])) {
                $interface->assign(
                    'typeList',
                    $result['facet_counts']['facet_fields']['type']
                );
            }
            if (isset($result['facet_counts']['facet_fields']['institution'])) {
                $interface->assign(
                    'institutionList',
                    $result['facet_counts']['facet_fields']['institution']
                );
            } 
            if (isset($result['facet_counts']['facet_fields']['country_iso'])) {
                $interface->assign(
                    'cisoList',
                    $result['facet_counts']['facet_fields']['country_iso']
                );
            } 
 }
		
////////////////////////////////////////////////////////////////////	
		
		
        // Cache homepage
        $interface->caching = 1; 
        $cacheId = 'homepage|' . $interface->lang . '|' .
            (UserAccount::isLoggedIn() ? '1' : '0') . '|' .
            (isset($_SESSION['lastUserLimit']) ? $_SESSION['lastUserLimit'] : '') .
            '|' .
            (isset($_SESSION['lastUserSort']) ? $_SESSION['lastUserSort'] : '');
        if (!$interface->is_cached('layout.tpl', $cacheId)) {
            $interface->setPageTitle('Search Home');
            $interface->assign('searchTemplate', 'search.tpl');
            $interface->setTemplate('home.tpl');

            // Create our search object
            $searchObject = SearchObjectFactory::initSearchObject();
            // Re-use the advanced search facets method,
            //   it is (for now) calling the same facets.
            // The template however is looking for specific
            //   facets. Bear in mind for later.
            $searchObject->initAdvancedFacets();
            // We don't want this search in the search history
            $searchObject->disableLogging();
            // Go get the facets
            $searchObject->processSearch();
            $facetList = $searchObject->getFacetList();
            // Shutdown the search object
            $searchObject->close();

            // Add a sorted version to the facet list:
            if (count($facetList) > 0) {
                $facets = array();
                foreach ($facetList as $facet => $details) {
                    $facetList[$facet]['sortedList'] = array();
                    foreach ($details['list'] as $value) {
                        $facetList[$facet]['sortedList'][$value['value']]
                            = $value['url'];
                    }
                    natcasesort($facetList[$facet]['sortedList']);
                }
                $interface->assign('facetList', $facetList);
            }
        }
        $interface->display('layout.tpl', $cacheId);
    }

}

?>
