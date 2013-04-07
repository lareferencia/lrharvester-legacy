<?php
/**
 * LDAP authentication test class
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
require_once 'sys/authn/LDAPAuthentication.php';
require_once 'sys/authn/IOException.php';

/**
 * ILS authentication test class
 *
 * @category VuFind
 * @package  Tests
 * @author   Franck Borel <franck.borel@gbv.de>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
class LDAPAuthenticationTest extends PHPUnit_Framework_TestCase
{
    private $_username = "testuser";     // valid LDAP username
    private $_password = "testpass";     // valid LDAP password

    /**
     * Standard setup method.
     *
     * @return void
     * @access public
     */
    public function setUp()
    {
        // Setup Local Database Connection
        if (!defined('DB_DATAOBJECT_NO_OVERLOAD')) {
            define('DB_DATAOBJECT_NO_OVERLOAD', 0);
        }
        $options =& PEAR::getStaticProperty('DB_DataObject', 'options');
        $configurationReader = new ConfigurationReader();
        $options = $configurationReader->readConfiguration('Database');
    }

    /**
     * Verify that missing username causes failure.
     *
     * @return void
     * @access public
     */
    public function testWithEmptyUsername()
    {
        try {
            $_POST['username'] = '';
            $_POST['password'] = $this->_password;
            $authN = new LDAPAuthentication();
            $this->assertTrue(PEAR::isError($authN->authenticate()));
        } catch (InvalidArgumentException $unexpected) {
            $this->fail('An unexpected InvalidArgumentException has been raised');
        }
    }

    /**
     * Verify that missing password causes failure.
     *
     * @return void
     * @access public
     */
    public function testWithEmptyPassword()
    {
        try {
            $_POST['username'] = $this->_username;
            $_POST['password'] = '';
            $authN = new LDAPAuthentication();
            $this->assertTrue(PEAR::isError($authN->authenticate()));
        } catch (InvalidArgumentException $unexpected) {
            $this->fail('An unexpected InvalidArgumentException has been raised');
        }
    }

    /**
     * Verify that bad credentials cause failure.
     *
     * @return void
     * @access public
     */
    public function testWithWrongCredentials()
    {
        try {
            $_POST['username'] = $this->_username;
            $_POST['password'] = $this->_password . 'badpass';
            $authN = new LDAPAuthentication();
            $this->assertTrue(PEAR::isError($authN->authenticate()));
        } catch (IOException $unexpected) {
            $this->fail('Unexpected Exception with: ' . $unexpected->getMessage());
        }
    }

    /* TODO -- figure out a way to make this test work cleanly in our continuous
               integration environment.
    public function testWithWorkingCredentials()
    {
        $_POST['username'] = $this->_username;
        $_POST['password'] = $this->_password;
        $authN = new LDAPAuthentication();
        $this->assertTrue($authN->authenticate() instanceof User);
    }
     */
}
?>
