<?php
/**
 * History action for Search module
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
 * History action for Search module
 *
 * @category VuFind
 * @package  Controller_Search
 * @author   Andrew S. Nagy <vufind-tech@lists.sourceforge.net>
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
class Comments extends Action
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
        global $user;
        global $configArray;
        // In some contexts, we want to require a login before showing search
        // history:
        if (isset($_REQUEST['require_login']) && !UserAccount::isLoggedIn()) {
            include_once 'services/MyResearch/Login.php';
            Login::launch();
            exit();
        }

        $interface->setPageTitle('Comments History');

		// Make a MySQL Connection
		mysql_connect("localhost", "vufind", "vufind") or die(mysql_error());
		mysql_select_db("vufind") or die(mysql_error());		
		$saved=array();
		// Get all the data from the "example" table $user->id
		$result = mysql_query("select record_id,comment,created from comments,resource where comments.user_id=".$user->id." and comments.resource_id=resource.id order by comments.created desc") 
		or die(mysql_error()); 
		while($row = mysql_fetch_array( $result )) {
		//echo print_r($row);
		 $ch = curl_init();
				$title ="";
				curl_setopt($ch, CURLOPT_URL,
				"http://200.0.207.91/vufind/getTitle.php?id=".$row['record_id']);
				curl_setopt($ch, CURLOPT_TIMEOUT, 30);
				curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
				curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
				$title = curl_exec ($ch);
				//echo $row['record_id'];
				//echo $title;
		$saved []=array('record_id'=>$row['record_id'],'title'=>$title,'comment'=>$row['comment'],'created'=>$row['created']);
		
		//array('name'=>$ccode,'amount'=>$value2)
		}
		
      /*  // Retrieve search history
        $s = new SearchEntry();
        $searchHistory = $s->getSearches(
            session_id(), is_object($user) ? $user->id : null
        );

        if (count($searchHistory) > 0) {
            // Build an array of history entries
            $links = array();
            $saved = array();

            // Loop through the history
            foreach ($searchHistory as $search) {
                $size = strlen($search->search_object);
                $minSO = unserialize($search->search_object);
                $searchObject = SearchObjectFactory::deminify($minSO);
                
                // Make sure all facets are active so we get appropriate
                // descriptions in the filter box.
                
				//$searchObject->activateAllFacets(); LA REFERENCIA PARA REBUSCAR

                $newItem = array(
                    'time' => date("g:ia, jS M y", $searchObject->getStartTime()),
                    'url'  => $searchObject->renderSearchUrl(),
                    'searchId' => $searchObject->getSearchId(),
                    'description' => $searchObject->displayQuery(),
                    'filters' => $searchObject->getFilterList(),
                    'hits' => number_format($searchObject->getResultTotal()),
					                    'nhits' => number_format($searchObject->getResultTotal()),
                    'speed' => round($searchObject->getQuerySpeed(), 2)."s",
                    // Size is purely for debugging. Not currently displayed in the
                    // template. It's the size of the serialized, minified search in
                    // the database.
                    'size' => round($size/1024, 3)."kb"
                );
				


				
				
				
                // Saved searches
                if ($search->saved == 1) {

						//$searchObject->init();
						
						// Process Search
						
						// $searchObject->purge();
									$result = $searchObject->processSearch(true, false);
								if (PEAR::isError($result)) {
									PEAR::raiseError($result->getMessage());
									}	
						//$searchObject->processSearch(true, false);
						//$searchObject->buildRSS();
						$newItem['nhits'] = number_format($searchObject->getResultTotal());				
				
                    $saved[] = $newItem;
                } else {
				
				

				
				
                    // All the others...

                    // If this was a purge request we don't need this
                    if (isset($_REQUEST['purge']) && $_REQUEST['purge'] == 'true') {
                        $search->delete();
                        
                        // We don't want to remember the last search after a purge:
                        unset($_SESSION['lastSearchURL']);
                    } else {
                        // Otherwise add to the list
                        $links[] = $newItem;
                    }
                }
            }

            // One final check, after a purge make sure we still have a history
            if (count($links) > 0 || count($saved) > 0) {
                $interface->assign('links', array_reverse($links));
                $interface->assign('saved', array_reverse($saved));
                $interface->assign('noHistory', false);
            } else {
                // Nothing left in history
                $interface->assign('noHistory', true);
            }
        } else {
            // No history
            $interface->assign('noHistory', true);
        }*/

		//echo print_r($saved);
		
		$interface->assign('saved', $saved);
        $interface->setTemplate('coments.tpl');
        $interface->display('layout.tpl');
    }
}

?>