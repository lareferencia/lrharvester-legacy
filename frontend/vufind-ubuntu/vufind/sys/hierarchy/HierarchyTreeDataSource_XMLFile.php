<?php
/**
 * Hierarchy Tree Data Source (XML File)
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
require_once 'HierarchyTreeDataSource.php';

/**
 * Hierarchy Tree Data Source (XML File)
 *
 * This is a base helper class for producing hierarchy Trees.
 *
 * @category VuFind
 * @package  HierarchyTree
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_an_authentication_handler Wiki
 */
class HierarchyTreeDataSource_XMLFile extends HierarchyTreeDataSource
{
    protected $basePath;

    /**
     * Constructor.
     *
     * @param object $hierarchyDriver A Hierarchy Driver Object
     *
     * @access public
     */
    public function __construct($hierarchyDriver)
    {
        parent::__construct($hierarchyDriver);
        $treeSettings = $hierarchyDriver->getTreeSettings();
        $this->basePath = isset($treeSettings['XMLFileDir'])
            ? $treeSettings['XMLFileDir']
            : realpath(dirname(__FILE__) . '/../../services/Collection/xml');
    }

    /**
     * Get the full filename for the XML file for a specific ID.
     *
     * @param string $id Hierarchy ID.
     *
     * @return string
     * @access protected
     */
    protected function getFilename($id)
    {
        return $this->basePath . '/' . $id . '.xml';
    }

    /**
     * Get XML for the specified hierarchy ID.
     *
     * @param string $id Hierarchy ID.
     * @param array  $options Additional options for XML generation (unused here).
     *
     * @return string
     * @access public
     */
    public function getXML($id, $options = array())
    {
        return file_get_contents($this->getFilename($id));
    }

    /**
     * Does this data source support the specified hierarchy ID?
     *
     * @param string $id Hierarchy ID.
     *
     * @return bool
     * @access public
     */
    public function supports($id)
    {
        return file_exists($this->getFilename($id));
    }
}

?>