<?php
/**
 * SideFacets Recommendations Module for Collections
 *
 * PHP version 5
 *
 * Copyright (C) Lutz Biedinger 2011.
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
 * @author   Lutz Biedinger <lutz.biedinger@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_recommendations_module Wiki
 */
require_once 'sys/Recommend/Interface.php';
require_once 'sys/Recommend/SideFacets.php';

/**
 * SideFacets Recommendations Module for Collections
 *
 * This class provides recommendations displaying facets beside search results
 *
 * @category VuFind
 * @package  Recommendations
 * @author   Lutz Biedinger <lutz.biedinger@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_recommendations_module Wiki
 */
class CollectionSideFacets extends SideFacets
{
    private $_collectionKeywordFilter;

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
        parent::__construct($searchObject, $params);

        // Collection keyword filter
        $parts = explode(':', $params);
        $this->_collectionKeywordFilter = isset($parts[3]) && $parts[3] !== 'false';
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
        global $interface;
        $interface->assign(
            'checkboxFilters', $this->searchObject->getCheckboxFacets()
        );
        $interface->assign(
            'dateFacets',
            $this->processDateFacets($this->searchObject->getFilters())
        );
        $interface->assign(
            'sideFacetSet', $this->searchObject->getFacetList($this->mainFacets)
        );

        $filterList = $this->searchObject->getFilterList(true);
        $interface->assign('collectionKeywordFilterList', $filterList);
        $interface->assign(
            'collectionKeywordFilters', $this->_collectionKeywordFilter
        );
        $search = $this->searchObject->getSearchTerms();
        foreach ($search as $lookfor) {
            if ($lookfor['lookfor'] != '') {
                $interface->assign('keywordLookfor', $lookfor['lookfor']);
                $filterList['Keyword'] = array(
                    array(
                        'value' => $lookfor['lookfor'],
                        'display'=>$lookfor['lookfor'],
                        'field' => $lookfor['index'],
                        'removalUrl' =>
                            $this->searchObject->renderLinkWithReplacedTerm(
                                $lookfor['lookfor'], ''
                            )
                    )
                );
            }
        }
        $interface->assign('filterList', $filterList);
        $interface->assign('sideFacetLabel', 'In This Collection');
    }
}

?>