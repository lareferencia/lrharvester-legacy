<?php
/**
 * Shibboleth configuration test class
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
require_once 'sys/authn/ShibbolethConfigurationParameter.php';

/**
 * Shibboleth configuration test class
 *
 * @category VuFind
 * @package  Tests
 * @author   Franck Borel <franck.borel@gbv.de>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
class ShibbolethConfigurationParameterTest extends PHPUnit_Framework_TestCase
{
    private $_pathToTestConfigurationFiles;

    /**
     * Standard setup method.
     *
     * @return void
     * @access public
     */
    public function setUp()
    {
        $this->_pathToTestConfigurationFiles = dirname(__FILE__) . '/../../conf';
    }

    /**
     * Test a configuration with no attributes.
     *
     * @return void
     * @access public
     */
    public function testWithoutAttributes()
    {
        try {
            $shibConfigParam = new shibbolethConfigurationParameter(
                $this->_pathToTestConfigurationFiles .
                "/authn/shib/no-user-attributes-config.ini"
            );
            $userAttributes = $shibConfigParam->getUserAttributes();
        } catch (UnexpectedValueException $expected) {
            return;
        }
        $this->fail('An expected UnexpectedValueException has not been raised');
    }

    /**
     * Test a configuration with a missing attribute value.
     *
     * @return void
     * @access public
     */
    public function testWithMissingAttributeValue()
    {
        try {
            $shibConfigParam = new shibbolethConfigurationParameter(
                $this->_pathToTestConfigurationFiles .
                "/authn/shib/attribute-value-is-missing-config.ini"
            );
            $userAttributes = $shibConfigParam->getUserAttributes();
        } catch (UnexpectedValueException $expected) {
            return;
        }
        $this->fail('An expected UnexpectedValueException has not been raised');
    }

    /**
     * Test a configuration with missing username.
     *
     * @return void
     * @access public
     */
    public function testWithoutUsername()
    {
        try {
            $shibConfigParam = new shibbolethConfigurationParameter(
                $this->_pathToTestConfigurationFiles .
                "/authn/shib/attribute-but-missing-username-config.ini"
            );
            $userAttributes = $shibConfigParam->getUserAttributes();
        } catch (UnexpectedValueException $expected) {
            return;
        }
        $this->fail('An expected UnexpectedValueException has not been raised');
    }

    /**
     * Test a successful configuration.
     *
     * @return void
     * @access public
     */
    public function testWithCorrectAttributeListAndUsername()
    {
        try {
            $shibConfigParam = new shibbolethConfigurationParameter(
                $this->_pathToTestConfigurationFiles .
                "/authn/shib/good-config.ini"
            );
            $userAttributes = $shibConfigParam->getUserAttributes();
            $this->assertTrue(is_array($userAttributes));
            $this->assertTrue(count($userAttributes) > 0);
            foreach ($userAttributes as $key => $value) {
                //echo "key = {$key}, value = {$value}\n";
            }
        } catch (Exception $unexpected){
            $this->fail('Unexpected Exception has been raised!');
        }
    }
}
?>