<?php
/**
 * AuthorityRecommend Recommendations Module
 *
 * PHP version 5
 *
 * Copyright (C) Villanova University 2012.
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
 * @package  Recommendations
 * @author   Lutz Biedinger (National Library of Ireland)
 * <vufind-tech@lists.sourceforge.net>
 * @author   Ronan McHugh (National Library of Ireland)
 * <vufind-tech@lists.sourceforge.net>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_recommendations_module Wiki
 */
require_once 'sys/Recommend/Interface.php';

/**
 * AuthorityRecommend Module
 *
 * This class provides recommendations based on Authority records.
 * i.e. searches for a pseudonym will provide the user with a link
 * to the official name (according to the Authority index)
 *
 * @category VuFind
 * @package  Recommendations
 * @author   Lutz Biedinger (National Library of Ireland)
 * <vufind-tech@lists.sourceforge.net>
 * @author   Ronan McHugh (National Library of Ireland)
 * <vufind-tech@lists.sourceforge.net>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_recommendations_module Wiki
 */
class AuthorityRecommend implements RecommendationInterface
{
    protected $searchObject;
    protected $filters = array();

    /**
     * Constructor
     *
     * Establishes base settings for making recommendations.
     *
     * @param object $searchObject The SearchObject requesting recommendations.
     * @param string $params       Additional settings from searches.ini.
     *
     * @access public
     */
    public function __construct($searchObject, $params)
    {
        // Save the basic parameters:
        $this->searchObject = $searchObject;
        $params = explode(':', $params);
        for ($i = 0; $i < count($params); $i += 2) {
            if (isset($params[$i+1])) {
                $this->filters[] = $params[$i] . ':(' . $params[$i + 1] . ')';
            }
        }
    }

    /**
     * init
     *
     * Called before the SearchObject performs its main search.  This may be used
     * to set SearchObject parameters in order to generate recommendations as part
     * of the search.
     *
     * @return void
     * @access public
     */
    public function init()
    {
        // No action needed here.
    }

    /**
     * inArrayR
     *
     * This helper function is used to do recursive searches
     * of multi-dimensional arrays
     *
     * @param string $needle   Search term
     * @param array  $haystack Multi-dimensional array
     *
     * @return boolean
     * @access public
     */
    public function inArrayR($needle, $haystack)
    {
        foreach ($haystack as $v) {
            if ($needle == $v) {
                return true;
            } elseif (is_array($v)) {
                if ($this->inArrayR($needle, $v)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * getAuthority
     *
     * Called by process(). Uses lookfor to create Authority search object.
     * Results are assigned to array and made available to Smarty file.
     *
     * @return void
     * @access protected
     */
    protected function getAuthority()
    {
        global $interface;

        $lookfor = $_REQUEST['lookfor'];

        // function will return blank on Advanced Search
        if ($this->searchObject->getSearchType()== 'advanced'
            || ConnectionManager::connectToIndex()->isAdvanced($lookfor)
        ) {
            return;
        }

        // Initialise and process search query, catching results in authResults var
        $authSearchObject = SearchObjectFactory::initSearchObject('SolrAuth');
        foreach ($this->filters as $filter) {
            $authSearchObject->addHiddenFilter($filter);
        }
        $authSearchObject->initAuthorityRecommendationSearch($lookfor);
        $authResults = $authSearchObject->processSearch();

        // Create array to hold recordSet
        $authResultSet= array();

        // loop through records and assign id and headings to separate arrays defined
        // above
        foreach ($authResults['response']['docs'] as $resultSet) {
            $recordArray = array();
            // creates search urls based on find and replace - this preserves active
            // filters
            $authLink = $this->searchObject
                ->renderLinkWithReplacedTerm($lookfor, $resultSet['heading']);

            $recordArray['authLink']= $authLink;
            $recordArray['id']= $resultSet['id'];
            $recordArray['heading']= $resultSet['heading'];

            // check for duplicates before adding record to recordSet
            if (!$this->inArrayR($recordArray['heading'], $authResultSet)) {
                array_push($authResultSet, $recordArray);
            } else {
                continue;
            }
        }
        // assign result set to Smarty template
        $interface->assign('authResultSet', $authResultSet);
    }

    /**
     * process
     *
     * Called after the SearchObject has performed its main search.  This may be
     * used to extract necessary information from the SearchObject or to perform
     * completely unrelated processing.
     *
     * @return void
     * @access public
     */
    public function process()
    {
        $this->getAuthority();
    }

    /**
     * getTemplate
     *
     * This method provides a template name so that recommendations can be displayed
     * to the end user.  It is the responsibility of the process() method to
     * populate all necessary template variables.
     *
     * @return string The template to use to display the recommendations.
     * @access public
     */
    public function getTemplate()
    {
        return 'Search/Recommend/AuthorityRecommend.tpl';
    }
}

?>
