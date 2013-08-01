<?php
/**
 * Hierarchy Tree action for Record module
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
require_once 'Record.php';
require_once 'Drivers/Hierarchy/HierarchyFactory.php';

/**
 * Home action for Record module
 *
 * @category VuFind
 * @package  Controller_Record
 * @author   Lutz Biedinger <lutz.biedigner@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
class HierarchyTree extends Record
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

        $hierarchyDriver = $this->recordDriver->getHierarchyDriver();

        $renderer = $hierarchyDriver->getTreeRendererType();
        $template = 'view-hierarchyTree_' . $renderer . '.tpl';

        $treeList = $this->recordDriver->getHierarchyTrees();
        if (!$treeList) {
            $url = $configArray['Site']['url'] . "/Record/" . $_REQUEST['id'] . "/Description";
            header('Location: '. $url);
            return;
        }

        // Pick the active hierarchy -- either the user-requested option, or the
        // first available one.
        if (count($treeList) == 1 || !isset($_REQUEST['hierarchy'])) {
            $keys = array_keys($treeList);
            $hierarchyID = $keys[0];
        } else {
            $hierarchyID = $_REQUEST['hierarchy'];
        }

        if ($hierarchyID) {
            $hierarchyTree = $hierarchyDriver->render(
                $this->recordDriver, 'Record', 'List', $hierarchyID
            );
            $interface->assign('hierarchyTree', $hierarchyTree);
            $interface->assign('treeSettings', $hierarchyDriver->getTreeSettings());
        }
        $interface->assign('context', "Record");
        $interface->assign('hierarchyID', $hierarchyID);
        $interface->assign('hierarchyTreeList', $treeList);
        if (!isset($configArray['Hierarchy']['search'])
            || $configArray['Hierarchy']['search']
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

        // Don't hide the full hierarchy if we are looking at the top node:
        $interface->assign(
            'disablePartialHierarchy', ($_REQUEST['id'] == $hierarchyID)
        );

        $interface->assign('id', $_GET['id']);

        // Display Page
        if (isset($_GET['lightbox'])) {
            $interface->assign('title', $_GET['message']);
            $interface->assign('lightbox', true);
            return $interface->fetch('Record/'.$template);
        } else {
            $interface->setPageTitle(
                translate('hierarchy_tree') .
                ': ' . $this->recordDriver->getBreadcrumb()
            );
            $interface->assign('subTemplate', $template);
            // Set Messages
            $interface->assign('infoMsg', $this->infoMsg);
            $interface->assign('errorMsg', $this->errorMsg);
            // This is because if loaded from a link, the tab will not automatically
            // be selected
            $interface->assign('tab', 'Hierarchytree');
            $interface->setTemplate('view.tpl');
            $interface->display('layout.tpl');
        }
    }
}

?>
