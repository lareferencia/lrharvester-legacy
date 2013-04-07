<?php
/**
 * Database authentication test class
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
 * @author   Franck Borel <franck.borel@gbv.de>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
require_once dirname(__FILE__) . '/../../prepend.inc.php';
require_once 'PEAR.php';
require_once 'sys/authn/DatabaseAuthentication.php';
require_once 'sys/authn/ConfigurationReader.php';

/**
 * Database authentication test class
 *
 * @category VuFind
 * @package  Tests
 * @author   Franck Borel <franck.borel@gbv.de>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
class DatabaseAuthenticationTest extends PHPUnit_Framework_TestCase
{
    private $_databaseAuthN;

    /**
     * Standard setup method.
     *
     * @return void
     * @access public
     */
    public function setUp()
    {
        // TODO: Figure out a way to avoid DB_DataObject from terminating program
        //       execution when connections fail.  When it does this, it kills the
        //       whole test process.  Until we figure this out, we will skip this
        //       test.
        $this->markTestSkipped();
        return;

        // Setup Local Database Connection
        if (!defined('DB_DATAOBJECT_NO_OVERLOAD')) {
            define('DB_DATAOBJECT_NO_OVERLOAD', 0);
        }
        $options =& PEAR::getStaticProperty('DB_DataObject', 'options');
        $configurationReader = new ConfigurationReader();
        $options = $configurationReader->readConfiguration('Database');
        $this->_databaseAuthN = new DatabaseAuthentication();
    }

    /**
     * Test incorrect credentials.
     *
     * @return void
     * @access public
     */
    public function testWithWrongCredentials()
    {
        $_POST['username'] = 'sdfasdfajksdfaksdfajklasdf';
        $_POST['password'] = 'asdfjklaskldfjaklserklar';
        $this->assertTrue(PEAR::isError($this->_databaseAuthN->authenticate()));
    }

    /**
     * Test missing credentials.
     *
     * @return void
     * @access public
     */
    public function testWithEmptyCredentials()
    {
        $_POST['username'] = '';
        $_POST['password'] = '';
        $this->assertTrue(PEAR::isError($this->_databaseAuthN->authenticate()));
    }

    /* TODO -- figure out a way to make this test work cleanly in our continuous
               integration environment.
    public function testWithWorkingCredentials()
    {
        $_POST['username'] = 'username';
        $_POST['password'] = 'password';
        $this->assertFalse(PEAR::isError($this->_databaseAuthN->authenticate()));
    }
     */

    /**
     * Test SQL injection attack as username/password.
     *
     * @return void
     * @access public
     */
    public function testWithSQLInjection()
    {
        $_POST['username'] = "' OR 1=1 ";
        $_POST['password'] = "' OR 1=1 LIMIT 1";
        $this->assertTrue(PEAR::isError($this->_databaseAuthN->authenticate()));
    }
}
?>
