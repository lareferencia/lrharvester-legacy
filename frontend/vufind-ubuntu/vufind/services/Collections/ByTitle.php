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
class ByTitle extends Action
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
        global $interface;

        $collections = $this->getCollectionsFromTitle($_REQUEST['title']);
        if (is_array($collections) && count($collections) != 1) {
            $interface->assign('collections', $collections);
            $title = count($collections) > 0
                ? 'collection_disambiguation' : 'collection_empty';
            $interface->setPageTitle($title);
            $interface->setTemplate('disambiguation.tpl');
            $interface->display('layout.tpl');
        } else {
            header(
                'Location:' . $configArray['Site']['url'] . '/Collection/'
                . urlencode($collections[0]['id'])
            );
        }
    }

    /**
     * Get collection information matching a given title:
     *
     * @param string $title Title to search for
     *
     * @return array
     * @access protected
     */
    protected function getCollectionsFromTitle($title)
    {
        global $configArray;
        $limit = isset($configArray['Collections']['browseLimit'])
            ? $configArray['Collections']['browseLimit'] : 20;

        $db = ConnectionManager::connectToIndex();
        $title = addcslashes($title, '"');
        $query = "is_hierarchy_title:\"$title\"";
        $result = $db->search($query, 'AllFields', null, 0, $limit);

        return isset($result['response']['docs'])
            ? $result['response']['docs'] : array();
    }
}

?>
