<?php
/**
 * Hierarchy interface.
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
 * @package  Hierarchy
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_search_object Wiki
 */

/**
 * Hierarchy interface class.
 *
 * Interface Hierarchy based drivers.
 * This should be extended to implement functionality for specific
 * Hierarchy Systems (i.e. Calm etc.).
 *
 * @category VuFind
 * @package  Hierarchy
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_search_object Wiki
 */
abstract class Hierarchy
{
    /**
     * showTree()
     * Whether or not to show the tree
     *
     * @return bool
     * @access public
     */
    abstract public function showTree();

    /**
     * getTreeSource()
     * Returns the Source of the Tree
     *
     * @return object The tree data source object
     * @access public
     */
    abstract public function getTreeSource();

    /**
     * getTreeRenderer()
     * Returns the actual object for generating trees
     *
     * @param object $driver Record driver
     *
     * @return object
     * @access public
     */
    public function getTreeRenderer($driver)
    {
        $renderer = 'HierarchyTreeRenderer_' . $this->getTreeRendererType();
        include_once 'sys/hierarchy/' . $renderer . '.php';
        return new $renderer($driver);
    }

    /**
     * render()
     * Render the tree for a given record.
     *
     * @param object $driver      Record driver
     * @param string $context     The context in which the tree is being created
     * @param string $mode        The type of tree required
     * @param string $hierarchyID The hierarchy ID to get the tree for
     *
     * @return string
     */
    public function render($driver, $context, $mode, $hierarchyID)
    {
        if (!$this->showTree()) {
            return false;
        }
        return $this->getTreeRenderer($driver)
            ->render($context, $mode, $hierarchyID, $driver->getUniqueID());
    }

    /**
     * getTreeRendererType()
     * Returns the Tree Renderer Type
     *
     * @return string
     * @access public
     */
    abstract public function getTreeRendererType();

    /**
     * Get Tree Settings
     *
     * Returns all the configuration settings for a hierarchy tree
     *
     * @return array The values of the configuration setting
     * @access public
     */
    abstract public function getTreeSettings();
}
?>