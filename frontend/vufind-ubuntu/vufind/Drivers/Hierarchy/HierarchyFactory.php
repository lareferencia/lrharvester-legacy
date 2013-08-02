<?php
/**
 * Hierarchy Factory Class
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
 * Hierarchy Factory Class
 *
 * This is a factory class to build objects for managing hierarchies.
 *
 * @category VuFind
 * @package  Hierarchy
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_search_object Wiki
 */
class HierarchyFactory
{
    /**
     * initHierarchy
     *
     * This constructs an Hierarchy Object for the specified driver.
     *
     * @param string $driver The type of Driver to build.
     *
     * @return mixed         The driver object on success, false otherwise
     * @access public
     */
    public static function initHierarchy($driver = false)
    {
        global $configArray;
        if ($driver) {
            $path = $configArray['Site']['local'] .
                "/Drivers/Hierarchy/" . $driver . ".php";
            if (is_readable($path)) {
                include_once $path;
                $class = 'Hierarchy_' . $driver;
                if (class_exists($class)) {
                    $hierarchy = new $class();
                    return $hierarchy;
                }
            }
        }
        return false;
    }
}
?>