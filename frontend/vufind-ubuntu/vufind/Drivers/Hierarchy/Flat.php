<?php
/**
 * Flat Hierarchy Driver
 *
 * PHP version 5
 *
 * Copyright (C) Villanova University 2012.
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
 * @package  Hierarchy_Drivers
 * @author   Lutz Biedinger <lutz.biedinger@gmail.com>
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_an_ils_driver Wiki
 */

require_once 'Drivers/Hierarchy/Default.php';

/**
 * Flat Hierarchy Driver; a hierarchy driver for collections without hierarchichal
 * trees.
 *
 * @category VuFind
 * @package  Hierarchy_Drivers
 * @author   Lutz Biedinger <lutz.biedinger@gmail.com>
 * @author   Luke O'Sullivan <l.osullivan@swansea.ac.uk>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_an_ils_driver Wiki
 */
class Hierarchy_Flat extends Hierarchy_Default
{
    /**
     * Constructor
     *
     * @param string $configFile The location of an alternative config file
     *
     * @access public
     */
    public function __construct($configFile = false)
    {
        // For now, Flat is exactly the same as Default except for the ini file.
        $configFile = $configFile ? $configFile : 'HierarchyFlat.ini';
        parent::__construct($configFile);
    }
}

?>