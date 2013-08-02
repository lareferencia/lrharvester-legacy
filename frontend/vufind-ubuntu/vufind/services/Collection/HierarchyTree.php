<?php
/**
 * Hierarchy Tree action for Collection module
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
 * @author   Lutz Biedinger <lutz.biedigner@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
require_once 'Collection.php';

/**
 * Hierarchy Tree action for Collection module
 *
 * @category VuFind
 * @package  Controller_Record
 * @author   Lutz Biedinger <lutz.biedigner@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
class HierarchyTree extends Collection
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

        // Set Up Collection
        $this->assignCollection();
        $this->assignCollectionFacets();

        if (!$this->treeList) {
            $url = $configArray['Site']['url'] . "/Collection/"
                . urlencode($_REQUEST['collection']) . "/CollectionList";
            header('Location: '. $url);
            return;
        }

        $hierarchyType = $this->recordDriver->getHierarchyType();
        $hierarchyDriver = HierarchyFactory::initHierarchy($hierarchyType);

        $renderer = $hierarchyDriver->getTreeRendererType();
        $template = 'Collection/view-hierarchyTree_' . $renderer . '.tpl';
        $recordID = isset($_GET['recordID']) ? $_GET['recordID'] : false;

        if ($recordID) {
            $interface->assign('collectionRecord', $this->getRecord($recordID));
            $interface->assign('recordID', $recordID);
        } else {
            $interface->assign('collectionRecord', false);
        }

        if (isset($configArray['Hierarchy']['search'])
            && $configArray['Hierarchy']['search']
        ) {
            $interface->assign('showTreeSearch', true);
            $searchLimit = isset($configArray['Hierarchy']['treeSearchLimit'])
                ? $configArray['Hierarchy']['treeSearchLimit'] : -1;
            $interface->assign(
                'treeLimitTokens', array(
                    "%%limit%%" => $searchLimit,
                    "%%url%%" => $configArray["Site"]["url"] . "/Search/Results"
                )
            );
        }
        $interface->setPageTitle(
            translate('hierarchy_tree') . ': ' .
            $this->recordDriver->getBreadcrumb()
        );

        if (count($this->treeList) == 1) {
            $keys = array_keys($this->treeList);
            $hierarchyID = $keys[0];
        } else {
            $hierarchyID = isset($_REQUEST['hierarchy'])
                ? $_REQUEST['hierarchy'] : false;
        }

        if ($hierarchyID) {
            $hierarchyTree = $hierarchyDriver->render(
                $this->recordDriver, 'Collection', 'List', $hierarchyID
            );
            $interface->assign('hierarchyTree', $hierarchyTree);
            $interface->assign('treeSettings', $hierarchyDriver->getTreeSettings());
        }
        $interface->assign('context', "Collection");
        $interface->assign('hierarchyID', $hierarchyID);
        $interface->assign('hierarchyTreeList', $this->treeList);
        $interface->assign(
            'disablePartialHierarchy',
            $this->id == $hierarchyID ? true : false
        );

        // Set Templates etc
        $interface->assign('subpage', $template);
        // This is because if loaded from an link, the tab will
        // not automatically be selected
        $interface->assign('tab', 'HierarchyTree');
        //$interface->setTemplate('collectionview.tpl');
        // Display Page
        $interface->display('layout.tpl');
    }

    /**
     * Gets An individual Collection Record
     *
     * @param string $id The Collection Record ID
     *
     * @return mixed null if the record doesn't exist, the location of a
     * template file on success
     * @access protected
     */
    public function getRecord($id)
    {
        global $interface;
        // Retrieve the record from the index
        if (!($record = $this->db->getRecord($id))) {
            return null;
        } else {
            $recordDriver = RecordDriverFactory::initRecordDriver($record);
            return $recordDriver->getCollectionRecord();
        }
    }
}

?>
