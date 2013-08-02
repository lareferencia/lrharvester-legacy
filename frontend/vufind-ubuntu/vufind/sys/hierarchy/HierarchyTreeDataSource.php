<?php
/**
 * Hierarchy Tree Data Source (abstract base)
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
 * Hierarchy Tree Data Source (abstract base)
 *
 * This is a base helper class for producing hierarchy Trees.
 *
 * @category VuFind
 * @package  HierarchyTree
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_an_authentication_handler Wiki
 */
abstract class HierarchyTreeDataSource
{
    protected $debug = false;
    protected $hierarchyDriver;

    /**
     * Constructor.
     *
     * @param object $hierarchyDriver A Hierarchy Driver Object
     *
     * @access public
     */
    public function __construct($hierarchyDriver)
    {
        global $configArray;

        $this->hierarchyDriver = $hierarchyDriver;
        if ($configArray['System']['debug']) {
            $this->debug = true;
        }
    }

    /**
     * Output a debug message, if appropriate
     *
     * @param string $msg Message to display
     *
     * @return void
     * @access protected
     */
    protected function debug($msg)
    {
        if ($this->debug) {
            echo $msg;
        }
    }

    /**
     * Get XML for the specified hierarchy ID.
     *
     * @param string $id      Hierarchy ID.
     * @param array  $options Additional options for XML generation.
     *
     * @return string
     * @access public
     */
    abstract public function getXML($id, $options = array());

    /**
     * Does this data source support the specified hierarchy ID?
     *
     * @param string $id Hierarchy ID.
     *
     * @return bool
     * @access public
     */
    abstract public function supports($id);
}

?>