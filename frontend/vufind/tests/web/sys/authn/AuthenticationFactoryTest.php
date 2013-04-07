<?php
/**
 * Class that tests the factory class that constructs authentication modules.
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
require_once 'sys/authn/AuthenticationFactory.php';
require_once 'sys/authn/UnknownAuthenticationHandlerException.php';

/**
 * Class that tests the factory class for constructing authentication modules.
 *
 * @category VuFind
 * @package  Tests
 * @author   Franck Borel <franck.borel@gbv.de>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
class AuthenticationFactoryTest extends PHPUnit_Framework_TestCase
{
   
    private $_pathToTestConfigurationFiles;
    
    /**
     * Initialize the test module and sets the path to configuration files
     *
     * @access public
     */
    public function __construct()
    {
        $this->_pathToTestConfigurationFiles = dirname(__FILE__) . '/../../conf';
    }

    /**
     * Test if the factory throws an exception, when the authentication handler
     * paramater is empty
     *
     * @return void
     * @access public
     */
    public function testAuthNHandlerIsEmpty()
    {
        try {
            $authN = AuthenticationFactory::initAuthentication(null);
        } catch (UnknownAuthenticationHandlerException $expected) {
            $this->assertEquals(
                'UnknownAuthenticationHandlerException', get_class($expected)
            );
            return;
        }
        $this->fail(
            'An expected UnknownAuthenticationHandlerException has not been raised.'
        );
    }

    /**
     * Test if the factory throws an exception, when the authentication handler does
     * not exists
     *
     * @return void
     * @access public
     */
    public function testAuthNHandlerDoesNotExist()
    {
        try {
            $authN = AuthenticationFactory::initAuthentication(
                'AuthenticationHandlerDoesNotExist'
            );
        } catch (UnknownAuthenticationHandlerException $expected) {
            $this->assertEquals(
                'UnknownAuthenticationHandlerException', get_class($expected)
            );
            return;
        }
        $this->fail('An expected Exception has not been raised.');
    }

    /**
     * Test the Shibboleth authentication handler.
     *
     * @return void
     * @access public
     */
    public function testInvokeShibbolethAuthNHandler()
    {
        try {
            $authN = AuthenticationFactory::initAuthentication(
                'Shibboleth',
                $this->_pathToTestConfigurationFiles . "/authn/shib/good-config.ini"
            );
            $this->assertNotNull($authN);
        } catch (Exception $unexpected){
            $this->fail('An unexpected exception has been raised:' . $unexpected);
        }
        return;
    }

    /**
     * Test the LDAP authentication handler
     *
     * @return void
     * @access public
     */
    public function testInvokeLdapAuthNHandler()
    {
        try {
            $authN = AuthenticationFactory::initAuthentication('LDAP');
            $this->assertNotNull($authN);
        } catch (Exception $unexpected){
            $this->fail('An unexpected exception has been raised:' . $unexpected);
        }
        return;
    }

    /**
     * Test the data base authentication handler
     *
     * @return void
     * @access public
     */
    public function testInvokeDatabaseAuthNHandler()
    {
        try {
            $authN = AuthenticationFactory::initAuthentication('DB');
            $this->assertNotNull($authN);
        } catch (Exception $unexpected){
            $this->fail('An unexpected exception has been raised:' . $unexpected);
        }
        return;
    }

    /**
     * Test the SIP2 authentication handler
     *
     * @return void
     * @access public
     */
    public function testInvokeSIP2AuthNHandler()
    {
        try {
            $authN = AuthenticationFactory::initAuthentication('SIP2');
            $this->assertNotNull($authN);
        } catch (Exception $unexpected){
            $this->fail('An unexpected exception has been raised:' . $unexpected);
        }
        return;
    }

    /**
     * Test the ILS authentication handler
     *
     * @return void
     * @access public
     */
    public function testInvokeILSAuthNHandler()
    {
        try {
            $authN = AuthenticationFactory::initAuthentication('ILS');
            $this->assertNotNull($authN);
        } catch (Exception $unexpected){
            $this->fail('An unexpected exception has been raised:' . $unexpected);
        }
        return;
    }
}
?>