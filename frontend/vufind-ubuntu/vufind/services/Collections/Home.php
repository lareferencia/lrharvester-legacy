<?php
/**
 * Home action for Collection module
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
 * @package  Controller_Record
 * @author   Lutz Biedinger <lutz.biedinger@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
require_once 'Action.php';

/**
 * Home action for Collection module
 *
 * @category VuFind
 * @package  Controller_Record
 * @author   Lutz Biedinger <lutz.biedinger@gmail.com>
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
        global $configArray;

        $browseType = (isset($configArray['Collections']['browseType'])) ?
            $configArray['Collections']['browseType'] : 'Index';
        if ($browseType == 'Alphabetic') {
            $this->showBrowseAlphabetic();
        } else {
            $this->showBrowseIndex();
        }
    }

    /**
     * Get the delimiter used to separate title from ID in the browse strings.
     *
     * @return string
     */
    protected function getBrowseDelimiter()
    {
        global $configArray;
        return isset($configArray['Collections']['browseDelimiter'])
            ? $configArray['Collections']['browseDelimiter'] : '{{{_ID_}}}';
    }

    /**
     * Show the Browse Menu
     *
     * @return void
     * @access public
     */
    public function showBrowseAlphabetic()
    {
        global $configArray;
        global $interface;
        // Process incoming parameters:
        $source = "hierarchy";
        $from = isset($_GET['from']) ? $_GET['from'] : '';
        $page = (isset($_GET['page']) && is_numeric($_GET['page']))
            ? $_GET['page'] : 0;
        $view = 'List';
        $limit = isset($configArray['Collections']['browseLimit'])
            ? $configArray['Collections']['browseLimit'] : 20;

        // Load Solr data or die trying:
        $db = ConnectionManager::connectToIndex();
        $result = $db->alphabeticBrowse($source, $from, $page, $limit, true);
        $this->checkError($result);

        // No results?  Try the previous page just in case we've gone past the
        // end of the list....
        if ($result['Browse']['totalCount'] == 0) {
            $page--;
            $result = $db->alphabeticBrowse($source, $from, $page, $limit, true);
            $this->checkError($result);
        }

        // Only display next/previous page links when applicable:
        if ($result['Browse']['totalCount'] > $limit) {
            $interface->assign('nextpage', $page + 1);
        }
        if ($result['Browse']['offset'] + $result['Browse']['startRow'] > 1) {
            $interface->assign('prevpage', $page - 1);
        }

        // Send other relevant values to the template:
        $interface->assign('source', $source);
        $interface->assign('from', $from);
        $interface->assign('letters', $this->getAlphabetList());

        // Format the results for proper display:
        $finalresult = array();
        $delimiter = $this->getBrowseDelimiter();
        foreach ($result['Browse']['items'] as $rkey => $collection) {
            $collectionIdNamePair
                = explode($delimiter, $collection["heading"]);
            $finalresult[$rkey][0] = $collectionIdNamePair[0];
            $finalresult[$rkey][1] = $collection["count"];
            $finalresult[$rkey][2] = $collectionIdNamePair[1];
        }
        $interface->assign('result', $finalresult);

        // Display the page:
        $interface->assign('browseView', 'Collections/browse'. $view . '.tpl');
        $interface->setPageTitle('Collection Browse');
        $interface->setTemplate('browse.tpl');
        $interface->display('layout.tpl');
    }

    /**
     * Show the Browse Menu
     *
     * @return void
     * @access public
     */
    public function showBrowseIndex()
    {
        global $configArray;
        global $interface;
        // Process incoming parameters:
        $from = strtolower(isset($_GET['from']) ? $_GET['from'] : '');
        $page = (isset($_GET['page']) && is_numeric($_GET['page']))
            ? $_GET['page'] : 0;
        $appliedFilters = isset($_GET['filter'])? $_GET['filter'] : array();
        $view = 'List';
        $limit = isset($configArray['Collections']['browseLimit'])
            ? $configArray['Collections']['browseLimit'] : 20;

        $browseField = "hierarchy_browse";

        $searchObject = SearchObjectFactory::initSearchObject();
        $searchObject->init();
        foreach ($appliedFilters as $filter) {
            $searchObject->addFilter($filter);
        }

        // Only grab 150,000 facet values to avoid out-of-memory errors:
        $result = $searchObject->getFullFieldFacets(
            array($browseField), false, 150000, 'index'
        );
        $result = $result[$browseField]['data'];

        $delimiter = $this->getBrowseDelimiter();
        foreach ($result as $rkey => $collection) {
            list($name, $id) = explode($delimiter, $collection[0], 2);
            $result[$rkey][0] = $name;
            $result[$rkey][] =  $id;
        }

        // Sort the $results and get the position of the from string once sorted
        $key = $this->sortFindKeyLocation($result, $from);

        // Offset the key by how many pages in we are
        $key += ($limit * $page);

        // Catch out of range keys
        if ($key < 0) {
            $key = 0;
        }
        if ($key >= count($result)) {
            $key = count($result)-1;
        }

        // Only display next/previous page links when applicable:
        if (count($result) > $key + $limit) {
            $interface->assign('nextpage', $page + 1);
        }
        if ($key > 0) {
            $interface->assign('prevpage', $page - 1);
        }

        // Select just the records to display
        $result = array_slice(
            $result, $key, count($result) > $key + $limit ? $limit : null
        );

        // Send other relevant values to the template:
        $interface->assign('from', $from);
        $interface->assign('result', $result);
        $interface->assign('letters', $this->getAlphabetList());

        // Because the search object returns removal URLs for the search module
        // we have to cheat a little bit and do some string manipulation to the
        // removal urls
        $filters = $searchObject->getFilterList(true);
        if (isset($filters) && isset($filters['Other'])) {
            $filtersString = "";
            foreach ($filters['Other'] as $filterK =>$filter) {
                $filters['Other'][$filterK]['removalUrl'] = str_ireplace(
                    'Search/Results?lookfor=&type=AllFields',
                    'Collections/Home?from=' . $from . '&page=' .
                    $page, $filter['removalUrl']
                );
                $filtersString .= "&".urlencode("filter[]") . '=' .
                    urlencode("{$filter['field']}:\"{$filter['value']}\"");
            }
            $interface->assign('filtersString', $filtersString);
            $interface->assign('filterList', $filters);
        }

        // Display the page:
        $interface->assign('browseView', 'Collections/browse'. $view . '.tpl');
        $interface->setPageTitle('Collection Browse');
        $interface->setTemplate('browse.tpl');
        $interface->display('layout.tpl');
    }

    /**
     * Function to sort the results and find the position of the from
     * value in the result set; if the value doesn't exist, it's inserted.
     *
     * @param array  &$result Array to sort
     * @param string $from    Position to find
     *
     * @return int
     * @access protected
     */
    protected function sortFindKeyLocation(&$result, $from)
    {
        // Normalize the from value so it matches the values we are looking up
        $from = $this->normalizeForBrowse($from);

        // Push the from value into the array so we can find the matching position:
        array_push($result, array($from, 0, null, 'placeholder'));

        // Declare array to hold the $result array in the right sort order
        $sorted = array();
        foreach ($this->normalizeAndSortFacets($result) as $i => $val) {
            // If this is the placeholder we added earlier, we have found the
            // array position we want to use as our start; otherwise, it is an
            // element that needs to be moved into the sorted version of the
            // array:
            if (isset($result[$i][3]) && $result[$i][3] == 'placeholder') {
                $key = count($sorted);
            } else {
                $sorted[] = $result[$i];
                unset($result[$i]); //clear this out of memory
            }
        }
        $result = $sorted;

        return isset($key) ? $key : 0;
    }

    /**
     * Function to normalize the names so they sort properly
     *
     * @param array &$result Array to sort (passed by reference to use less memory)
     *
     * @return array $resultOut
     * @access protected
     */
    protected function normalizeAndSortFacets(&$result)
    {
        $valuesSorted = array();
        foreach ($result as $resKey => $resVal) {
            $valuesSorted[$resKey] = $this->normalizeForBrowse($resVal[0]);
        }
        asort($valuesSorted);

        // Now the $valuesSorted is in the right order
        return $valuesSorted;
    }

    /**
     * Normalize the value for the browse sort
     *
     * @param string $val Value to normalize
     *
     * @return string $valNormalized
     * @access protected
     */
    protected function normalizeForBrowse($val)
    {
        $valNormalized = iconv('UTF-8', 'US-ASCII//TRANSLIT//IGNORE', $val);
        $valNormalized = strtolower($valNormalized);
        $valNormalized = preg_replace("/[^a-zA-Z0-9\s]/", "", $valNormalized);
        $valNormalized = trim($valNormalized);
        return $valNormalized;
    }

    /**
     * Get a list of initial letters to display.
     *
     * @return array
     * @access protected
     */
    protected function getAlphabetList()
    {
        return array_merge(range('0', '9'), range('A', 'Z'));
    }

    /**
     * Given an alphabrowse response, die with an error if necessary.
     *
     * @param array $result Result to check.
     *
     * @return void
     * @access protected
     */
    protected function checkError($result)
    {
        if (isset($result['error'])) {
            // Special case --  missing alphabrowse index probably means the
            // user could use a tip about how to build the index.
            if (strstr($result['error'], 'does not exist')
                || strstr($result['error'], 'no such table')
                || strstr($result['error'], 'couldn\'t find a browse index')
            ) {
                $result['error'] = "Alphabetic Browse index missing.  See " .
                    "http://vufind.org/wiki/alphabetical_heading_browse for " .
                    "details on generating the index.";
            }
            PEAR::raiseError(new PEAR_Error($result['error']));
        }
    }
}

?>
