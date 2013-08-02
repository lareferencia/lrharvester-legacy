<?php
/**
 * Hierarchy Tree Renderer
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
 * @package  HierarchyTree
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_an_authentication_handler Wiki
 */

/**
 * Hierarchy Tree Renderer
 *
 * This is a base helper class for producing hierarchy Trees.
 *
 * @category VuFind
 * @package  HierarchyTree
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_an_authentication_handler Wiki
 */
abstract class HierarchyTreeRenderer
{
    protected $recordDriver;
    protected $hierarchyDriver;
    protected $dataSource;

    /**
     * Constructor. Loads the record Driver.
     *
     * @param object $recordDriver A Record Driver Object
     *
     * @access public
     */
    public function __construct($recordDriver)
    {
        $this->recordDriver = $recordDriver;

        // Load the hierarchy driver from the record driver -- throw an exception if
        // this fails, since we shouldn't be using this class for drivers that do not
        // support hierarchies!
        $this->hierarchyDriver = $recordDriver->getHierarchyDriver();
        if (!$this->hierarchyDriver) {
            throw new Exception('Cannot load hierarchy driver from record driver.');
        }
        $this->dataSource = $this->hierarchyDriver->getTreeSource();
    }

    /**
     * Get a list of trees containing the item represented by the stored record
     * driver.
     *
     * @param string $hierarchyID Optional filter: specific hierarchy ID to retrieve
     *
     * @return mixed An array of hierarchy IDS if an archive tree exists,
     * false if it does not
     * @access public
     */
    abstract public function getTreeList($hierarchyID = false);

    /**
     * Render the Hierarchy Tree
     *
     * @param string $context     The context from which the call has been made
     * @param string $mode        The mode in which the tree should be generated
     * @param string $hierarchyID The hierarchy ID of the tree to fetch (optional)
     * @param string $recordID    The current record ID (optional)
     *
     * @return mixed The desired hierarchy tree output (or false on error)
     * @access public
     */
    abstract public function render(
        $context, $mode, $hierarchyID, $recordID = false
    );

    /**
     * Get Hierarchy Name
     *
     * @param string $hierarchyID        The hierarchy ID to find the title for
     * @param string $inHierarchies      An array of hierarchy IDs
     * @param string $inHierarchiesTitle An array of hierarchy Titles
     *
     * @return string A hierarchy title
     * @access public
     */
    public function getHierarchyName(
        $hierarchyID, $inHierarchies, $inHierarchiesTitle
    ) {
        $keys = array_flip($inHierarchies);
        $key = $keys[$hierarchyID];
        return $inHierarchiesTitle[$key];
    }
}

?>