<?php
/**
 * Solr Search Object class for collections
 *
 * PHP version 5
 *
 * Copyright (C) Villanova University 2010.
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
 * @package  SearchObject
 * @author   Lutz Biedinger <lutz.biedinger@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_search_object Wiki
 */
require_once 'sys/Proxy_Request.php';   // needed for constant definitions
require_once 'sys/SearchObject/Base.php';
require_once 'RecordDrivers/Factory.php';

/**
 * Solr Search Object class for collections
 *
 * This is a speccial search object class to be used for collection searches.
 *
 * @category VuFind
 * @package  SearchObject
 * @author   Lutz Biedinger <lutz.biedinger@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_search_object Wiki
 */
class SearchObject_SolrCollection extends SearchObject_Solr
{
    /**
     * The field which defines somehting as being a collection
     * this is usually either hierarchy_parent_id or
     * hierarchy_top_id
     *
     * @var string
     */
    protected $collectionField = null;

    /**
     * The ID of the collection being searched
     *
     * @var string
     */
    protected $collectionID = null;

    /**
     * Constructor. Initialise some details about the server
     *
     * @access public
     */
    public function __construct()
    {
        // Call base class constructor
        parent::__construct();

        $searchSettings = getExtraConfigArray('Collection');

        // Get collection specific sort options
        if (isset($searchSettings['Sort'])) {
            $this->sortOptions = $searchSettings['Sort'];
        } else {
            $this->sortOptions = array(
                'title' => 'sort_title',
                'year' => 'sort_year', 'year asc' => 'sort_year asc',
                'author' => 'sort_author'
            );
        }
        $this->defaultSort = key($this->sortOptions);
    }

    /**
     * Initialize the object using a record driver
     *
     * @param object $driver Record driver
     *
     * @return boolean
     * @access public
     */
    public function initForRecordDriver($driver)
    {
        $this->collectionID = $driver->getUniqueID();
        if ($hierarchyDriver = $driver->getHierarchyDriver()) {
            switch ($hierarchyDriver->getCollectionLinkType()) {
            case 'All':
                $this->collectionField = 'hierarchy_parent_id';
                break;
            case 'Top':
                $this->collectionField = 'hierarchy_top_id';
                break;
            }
        }
        return $this->init();
    }
     
    /**
     * Initialise the object from the global
     *  search parameters in $_REQUEST.
     *
     * @return boolean
     * @access public
     */
    public function init()
    {
        if (null === $this->collectionID) {
            throw new Exception('Collection ID missing');
        }
        if (null === $this->collectionField) {
            throw new Exception('Collection field missing');
        }

        // Call the standard initialization routine in the parent:
        if (!parent::init()) {
            return false;
        }

        // Log a special type of search
        $this->searchType = 'collection';

        // We don't spellcheck this screen
        // it's not for free user input anyway
        $this->spellcheck  = false;

        // Prepare the search
        $safeId = addcslashes($this->collectionID, '"');
        $this->addHiddenFilter($this->collectionField . ':"' . $safeId . '"');
        $this->addHiddenFilter('!id:"' . $safeId . '"');

        return true;
    } // End init()

    /**
     * Get the base URL for search results (including ? parameter prefix).
     *
     * @return string Base URL
     * @access protected
     */
    protected function getBaseUrl()
    {
        global $action;

        // Base URL is different for collection searches:
        return $this->serverUrl . "/Collection/" . urlencode($this->collectionID)
            . "/" . $action . "?";
    }

    /**
     * Load all recommendation settings from the relevant ini file.  Returns an
     * associative array where the key is the location of the recommendations (top
     * or side) and the value is the settings found in the file (which may be either
     * a single string or an array of strings).
     *
     * @return array associative: location (top/side) => search settings
     * @access protected
     */
    protected function getRecommendationSettings()
    {
        // Collection recommendations
        $searchSettings = getExtraConfigArray('Collection');
        return isset($searchSettings['Recommend'])
            ? $searchSettings['Recommend']
            : array('side' => array('CollectionSideFacets:Facets::Collection:true'));
    }
}
?>
