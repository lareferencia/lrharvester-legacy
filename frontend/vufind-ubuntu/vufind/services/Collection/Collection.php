<?php
/**
 * Collection Controller.
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
require_once 'sys/Language.php';
require_once 'RecordDrivers/Factory.php';
require_once 'sys/VuFindDate.php';

/**
 * Collection module.
 *
 * @category VuFind
 * @package  Controller_Collection
 * @author   Lutz Biedinger <lutz.biedinger@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
class Collection extends Action
{
    protected $recordDriver;
    protected $db;
    protected $id;
    protected $searchObject;
    protected $treeList;

    /**
     * Constructor
     *
     * @access public
     */
    public function __construct()
    {
        global $action;
        global $interface;
        $interface->assign('collectionAction', $action);
        $interface->assign('disableKeepFilterControl', true);
        // Setup Search Engine Connection
        $this->db = ConnectionManager::connectToIndex();
        $this->id = $_REQUEST['collection'];

        // Retrieve the record from the index
        if (!($record = $this->db->getRecord($this->id))) {
            PEAR::raiseError(new PEAR_Error('Record Does Not Exist'));
        }
        $this->recordDriver = RecordDriverFactory::initRecordDriver($record);

        $this->treeList = $this->recordDriver->getHierarchyTrees();

        // Get the records part of this collection
        $this->searchObject
            = SearchObjectFactory::initSearchObject("SolrCollection");
        $this->searchObject->initForRecordDriver($this->recordDriver);
    }

    /**
     * Set Up Collection Record
     *
     * @return void
     * @access public
     */
    public function assignCollection()
    {
        global $configArray;
        global $interface;

        // Pass collection fields to the  interface
        $interface->assign('id', $this->id);
        $interface->assign('collectionID', $this->id);

        if ($this->recordDriver->hasRDF()) {
            $interface->assign(
                'addHeader', '<link rel="alternate" type="application/rdf+xml" ' .
                'title="RDF Representation" href="' . $configArray['Site']['url'] .
                '/Record/' . urlencode($this->id) . '/RDF" />' . "\n"
            );
        }

        //Set the page Title
        $interface->setPageTitle(
            translate('Collection') . ': ' . $this->recordDriver->getBreadcrumb()
        );

        $interface->assign('info', $this->recordDriver->getCollectionMetadata());

        // Set flags that control which tabs are displayed:
        // Hierarchy tree
        if (isset($configArray['Hierarchy']['showTree'])
            && $configArray['Hierarchy']['showTree']
        ) {
            $interface->assign('hierarchyTreeList', $this->treeList);
        }

        // Retrieve User Search History
        $interface->assign(
            'lastsearch',
            isset($_SESSION['lastSearchURL']) ? $_SESSION['lastSearchURL'] : false
        );

        // Send down text for inclusion in breadcrumbs
        $interface->assign('breadcrumbText', $this->recordDriver->getBreadcrumb());

        // Send down OpenURL for COinS use:
        $interface->assign('openURL', $this->recordDriver->getOpenURL());

        // Set AddThis User
        $interface->assign(
            'addThis', isset($configArray['AddThis']['key'])
            ? $configArray['AddThis']['key'] : false
        );

        // Set Default View Template
        $interface->setTemplate('view.tpl');
    }

    /**
     * Set Up Collection Facets
     *
     * @return void
     * @access public
     */
    public function assignCollectionFacets()
    {
        global $interface;
        global $configArray;

        $result = $this->searchObject->processSearch(false, true);
        if (PEAR::isError($result)) {
            PEAR::raiseError($result->getMessage());
        }

        $interface->assign(
            'topRecommendations',
            $this->searchObject->getRecommendationsTemplates('top')
        );
        $interface->assign(
            'sideRecommendations',
            $this->searchObject->getRecommendationsTemplates('side')
        );
        $interface->assign(
            'recordSet', $this->searchObject->getResultRecordHTML()
        );

        // Store search parameters so we can maintain them while switching tabs:
        $interface->assign(
            'filters', '?' . $this->searchObject->renderSearchUrlParams()
        );

        // Set Proxy URL
        if (isset($configArray['EZproxy']['host'])) {
            $interface->assign('proxy', $configArray['EZproxy']['host']);
        }
    }

    /**
     * Record a record hit to the statistics index when stat tracking is enabled;
     * this is called by the Home action.
     *
     * @return void
     * @access public
     */
    public function recordHit()
    {
        global $configArray;

        if ($configArray['Statistics']['enabled']) {
            // Setup Statistics Index Connection
            $solrStats = ConnectionManager::connectToIndex('SolrStats');

            // Save Record View
            $solrStats->saveRecordView($this->recordDriver->getUniqueID());
            unset($solrStats);
        }
    }

}
?>