<?php
/**
 * Shibboleth authentication test class
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
require_once 'sys/authn/ShibbolethAuthentication.php';
require_once 'services/MyResearch/lib/User.php';

/**
 * Shibboleth authentication test class
 *
 * @category VuFind
 * @package  Tests
 * @author   Franck Borel <franck.borel@gbv.de>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
class ShibbolethAuthenticationTest extends PHPUnit_Framework_TestCase
{
    private $_authN;

    /**
     * Standard setup method.
     *
     * @return void
     * @access public
     */
    public function setUp()
    {
        $conf = dirname(__FILE__) . '/../../conf/authn/shib/good-config.ini';
        $this->_authN = new ShibbolethAuthentication($conf);
    }

    /**
     * Test that authentication fails when superglobal attributes are invalid.
     *
     * @return void
     * @access public
     */
    public function testAuthenticationWithInvalidAttributeValues()
    {
        $_SERVER['entitlement'] = 'wrong_Value';
        $_SERVER['unscoped-affiliation'] = 'wrong_Value';
        $this->assertTrue(PEAR::isError($this->_authN->authenticate()));
    }

    /* TODO -- figure out a way to make this test work cleanly in our continuous
               integration environment.
    public function test_authentication_with_working_attribute_values()
    {
        $_SERVER['persistent-id']    = '1234_1234';
        $_SERVER['entitlement'] = 'urn:mace:dir:entitlement:common-lib-terms';
        $_SERVER['unscoped-affiliation'] = 'member';
        $this->assertTrue($this->_authN->authenticate() instanceof User);
    }
     */

    /**
     * Test that authentication fails when the identifier is missing.
     *
     * @return void
     * @access public
     */
    public function testAuthenticationWithMissingUsername()
    {
        unset($_SERVER['persistent-id']);
        $_SERVER['entitlement'] = 'urn:mace:dir:entitlement:common-lib-terms';
        $_SERVER['unscoped-affiliation'] = 'member';
        $this->assertTrue(PEAR::isError($this->_authN->authenticate()));
    }
}

?>