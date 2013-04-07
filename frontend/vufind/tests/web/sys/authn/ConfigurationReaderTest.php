<?php
/**
 * ConfigurationReader test class
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
require_once 'sys/authn/ConfigurationReader.php';
require_once 'sys/authn/IOException.php';
require_once 'sys/authn/FileParseException.php';

/**
 * ConfigurationReader test class
 *
 * @category VuFind
 * @package  Tests
 * @author   Franck Borel <franck.borel@gbv.de>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
class ConfigurationReaderTest extends PHPUnit_Framework_TestCase
{
    private $_pathToTestConfigurationFile;

    /**
     * Constructor
     *
     * @access public
     */
    public function __construct()
    {
        $this->_pathToTestConfigurationFile = dirname(__FILE__) . '/../../conf';
    }

    /**
     * Make sure that an exception is thrown when a missing file is requested.
     *
     * @return void
     * @access public
     */
    public function testNoConfigurationFileFound()
    {
        try {
            $configurationReader = new ConfigurationReader(
                $this->_pathToTestConfigurationFile .
                "/authn/shib/this-file-do-not-exist.ini"
            );
        } catch (IOException $expected) {
            return;
        }

        $this->fail('An expected IOException has not been raised');
    }

    /**
     * Make sure that an exception is thrown when a missing section is requested.
     *
     * @return void
     * @access public
     */
    public function testUnknownSection()
    {
        try {
            $configurationReader = new ConfigurationReader(
                $this->_pathToTestConfigurationFile .
                "/authn/shib/no-shibboleth-section-config.ini"
            );
            $section = $configurationReader->readConfiguration("Shibboleth");
        } catch (UnexpectedValueException $expected) {
            return;
        }
        $this->fail('An expected UnexpectedValueException has not been raised');
    }

    /**
     * Make sure that an exception is thrown when a section is completely empty.
     *
     * @return void
     * @access public
     */
    public function testEmptySection()
    {
        try {
            $configurationReader = new ConfigurationReader(
                $this->_pathToTestConfigurationFile .
                "/authn/shib/empty-shibboleth-section-config.ini"
            );
            $section = $configurationReader->readConfiguration("Shibboleth");
        } catch (UnexpectedValueException $expected) {
            return;
        }
        $this->fail('An expected UnexpectedValueException has not been raised');
    }

    /**
     * Make sure that a parse exception is thrown when an attribute name is missing.
     *
     * @return void
     * @access public
     */
    public function testWithAttributeValueButMissingAttributename()
    {
        try {
            $configurationReader = new ConfigurationReader(
                $this->_pathToTestConfigurationFile .
                "/authn/shib/attribute-value-but-missing-attributename-config.ini"
            );
            $section = $configurationReader->readConfiguration("Shibboleth");
        } catch (FileParseException $expected) {
            return;
        }
        $this->fail('An expected FileParseException has not been raised');
    }

    /**
     * Make sure that a known good configuration file loads correctly.
     *
     * @return void
     * @access public
     */
    public function testWithCorrectConfigurationFile()
    {
        try {
            $configurationReader = new ConfigurationReader(
                $this->_pathToTestConfigurationFile . "/config.ini"
            );
            $section = $configurationReader->readConfiguration("Extra_Config");
            $this->assertEquals($section['facets'], "facets.ini");
            $this->assertEquals($section['searches'], "searches.ini");
        } catch (Exception $unexpected) {
            $this->fail($unexpected);
        }
    }

    /**
     * Make sure that the default configuration file loads correctly.
     *
     * @return void
     * @access public
     */
    public function testWithoutCommitedConfigurationFile()
    {
        try {
            $configurationReader = new ConfigurationReader();
            $section = $configurationReader->readConfiguration("Extra_Config");
            $this->assertEquals($section['facets'], "facets.ini");
            $this->assertEquals($section['searches'], "searches.ini");
        } catch (Exception $unexpected) {
            $this->fail($unexpected);
        }
    }
}
?>