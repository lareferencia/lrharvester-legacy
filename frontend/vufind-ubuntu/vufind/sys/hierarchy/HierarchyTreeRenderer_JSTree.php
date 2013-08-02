<?php
/**
 * Hierarchy Tree Renderer for the JS_Tree plugin
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
require_once 'HierarchyTreeRenderer.php';

/**
 * Hierarchy Tree Renderer
 *
 * This is a helper class for producing hierarchy trees.
 *
 * @category VuFind
 * @package  HierarchyTree
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_an_authentication_handler Wiki
 */

class HierarchyTreeRenderer_JSTree extends HierarchyTreeRenderer
{
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
    public function getTreeList($hierarchyID = false)
    {
        $hierarchies = array();
        $id = $this->recordDriver->getUniqueID();
        $inHierarchies = $this->recordDriver->getHierarchyTopID();
        $inHierarchiesTitle = $this->recordDriver->getHierarchyTopTitle();

        if ($hierarchyID) {
            // Specific Hierarchy Supplied
            if (in_array($hierarchyID, $inHierarchies)
                && $this->dataSource->supports($hierarchyID)
            ) {
                return array(
                    $hierarchyID => $this->getHierarchyName(
                        $hierarchyID, $inHierarchies, $inHierarchiesTitle
                    )
                );
            }
        } else {
            // Return All Hierarchies
            $i = 0;
            foreach ($inHierarchies as $hierarchyTopID) {
                if ($this->dataSource->supports($hierarchyTopID)) {
                    $hierarchies[$hierarchyTopID] = $inHierarchiesTitle[$i];
                }
                $i++;
            }
            if (!empty($hierarchies)) {
                return $hierarchies;
            }
        }

        // If we got this far, we couldn't find valid match(es).
        return false;
    }

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
    public function render($context, $mode, $hierarchyID, $recordID = false)
    {
        if (!empty($context) && !empty($mode)) {
            return $this->transformCollectionXML(
                $context, $mode, $hierarchyID, $recordID
            );
        }
        return false;
    }

    /**
     * transformCollectionXML
     *
     * Transforms Collection XML to Desired Format
     *
     * @param string $context     The Context in which the tree is being displayed
     * @param string $mode        The Mode in which the tree is being displayed
     * @param string $hierarchyID The hierarchy to get the tree for
     * @param string $recordID    The currently selected Record (false for none)
     *
     * @return string A HTML List
     * @access public
     */
    protected function transformCollectionXML(
        $context, $mode, $hierarchyID, $recordID
    ) {
        global $configArray;
        $transformation = ucfirst($context) . ucfirst($mode);
        $inHierarchies = $this->recordDriver->getHierarchyTopID();
        $inHierarchiesTitle = $this->recordDriver->getHierarchyTopTitle();

        $hierarchyTitle = $this->getHierarchyName(
            $hierarchyID, $inHierarchies, $inHierarchiesTitle
        );

        $base = $configArray['Site']['url'];

        $xmlFile = $this->dataSource->getXML($hierarchyID);
        $xslFile = "services/Collection/xsl/Storeto" . $transformation . ".xsl";
        if (!file_exists($xslFile) || $xmlFile == false) {
            return false;
        }
        $doc = new DOMDocument();
        $xsl = new XSLTProcessor();

        $doc->load($xslFile);
        $xsl->importStyleSheet($doc);
        $doc->loadXML($xmlFile);

        // Append Collection ID, Collection Title && Record ID
        $xsl->setParameter('', 'titleText', translate("collection_view_record"));
        $xsl->setParameter('', 'collectionID', $hierarchyID);
        $xsl->setParameter('', 'collectionTitle', $hierarchyTitle);
        $xsl->setParameter('', 'baseURL', $base);
        $xsl->setParameter('', 'context', $context);
        $xsl->setParameter('', 'recordID', $recordID);
        return $xsl->transformToXML($doc);
    }
}

?>