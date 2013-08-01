<?php
/**
 * List action for Collection module
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
require_once 'sys/Pager.php';

/**
 * List action for Collection module
 *
 * @category VuFind
 * @package  Controller_Record
 * @author   Lutz Biedinger <lutz.biedigner@gmail.com>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
class CollectionList extends Collection
{
    protected $record;
    private $_structure;
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

        // Assign interface variables
        $summary = $this->searchObject->getResultSummary();

        $interface->assign('recordCount', $summary['resultTotal']);
        $interface->assign('recordStart', $summary['startRecord']);
        $interface->assign('recordEnd',   $summary['endRecord']);

        $interface->assign('sortList',   $this->searchObject->getSortList());
        $interface->assign('viewList',   $this->searchObject->getViewList());
        $interface->assign('limitList',  $this->searchObject->getLimitList());
        $interface->assign('page', isset($_REQUEST['page']) ? $_REQUEST['page'] : 1);
        
        // Process Paging
        $link = $this->searchObject->renderLinkPageTemplate();
        $options = array('totalItems' => $summary['resultTotal'],
                         'fileName'   => $link,
                         'perPage'    => $summary['perPage']);
        $pager = new VuFindPager($options);
        $pageLinks = $pager->getLinks();
        $interface->assign('pageLinks', $pageLinks);

        // Set templates etc.
        $currentView  = $this->searchObject->getView();
        $interface->assign('searchPage', 'Search/list-' . $currentView . '.tpl');
        $interface->assign('subpage', 'Collection/list.tpl');
        // This is because if loaded from an link, the tab will
        // not automatically be selected
        $interface->assign('tab', 'list');
        // Display Page
        $interface->display('layout.tpl');
    }
}
?>
