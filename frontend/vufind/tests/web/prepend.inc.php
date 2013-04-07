<?php
/**
 * Include this file to your php unit test.
 *
 * This adds your web folder to the include path list (see also the PHP docs at
 * http: *us3.php.net/manual/en/function.include.php). After that, PHP can find
 * the class you would like to test by simply adding the relative path starting
 * from the web folder.
 *
 * IMPORTANT: To ensure that your tests work in all contexts, you should include
 * this file relative to dirname(__FILE__) (the directory containing your class).
 * i.e. use:
 *      require_once dirname(__FILE__) . '/../prepend.inc.php'
 * instead of:
 *      require_once '../prepend.inc.php'
 * for best results.
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
 * @package  Tests
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
$actualPath = dirname(__FILE__);
$pathToWeb = str_replace("/tests/web", "/web", $actualPath);
$includePaths = explode(PATH_SEPARATOR, get_include_path());
$includePaths[] = realpath($pathToWeb);
$includePaths = array_unique($includePaths);
set_include_path(implode(PATH_SEPARATOR, $includePaths));

// Set up autoloader
require_once 'sys/Autoloader.php';
spl_autoload_register('vuFindAutoloader');

?>
