<?php
/**
 * Innovative Driver Test Class
 *
 * PHP version 5
 *
 * Copyright (C) Villanova University 2011.
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
require_once dirname(__FILE__) . '/../prepend.inc.php';
require_once 'Drivers/Innovative.php';

/**
 * Innovative ILS Driver Test Class
 *
 * @category VuFind
 * @package  Tests
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
class InnovativeTest extends PHPUnit_Framework_TestCase
{
    private $_driver;

    /**
     * Standard setup method.
     *
     * @return void
     * @access public
     */
    public function setUp()
    {
        $this->_driver = new Innovative();
    }

    /**
     * Test the constructor.
     *
     * @return void
     * @access public
     */
    public function testConstructor()
    {
        $this->assertTrue(is_object($this->_driver));
    }

    /**
     * Test the ID formatter based on different configurations:
     *
     * @return void
     * @access public
     */
    public function testPrepID()
    {
        // Test that RecordID:use_full_id setting defaults to true:
        $iii = new InnovativeTestHarness(
            '../../tests/web/conf/InnovativeNoIdSetting.ini'
        );
        $this->assertEquals($iii->testPrepID('.b1000167x'), '1000167');

        // Test for correct value when RecordID:use_full_id is true and full ID
        // is passed in:
        $iii = new InnovativeTestHarness(
            '../../tests/web/conf/InnovativeFullId.ini'
        );
        $this->assertEquals($iii->testPrepID('.b1000167x'), '1000167');

        // Test for correct value when RecordID:use_full_id is false and stripped ID
        // is passed in:
        $iii = new InnovativeTestHarness(
            '../../tests/web/conf/InnovativeNonFullId.ini'
        );
        $this->assertEquals($iii->testPrepID('b1000167'), '1000167');
    }

    /**
     * Standard teardown method.
     *
     * @return void
     * @access public
     */
    public function tearDown()
    {
    }
}

/**
 * Innovative ILS Driver Test Harness
 *
 * @category VuFind
 * @package  Tests
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
class InnovativeTestHarness extends Innovative
{
    /**
     * Expose protected method as public for the purposes of testing.
     *
     * @param string $id ID to format
     *
     * @return string
     * @access public
     */
    public function testPrepID($id)
    {
        return parent::prepID($id);
    }
}
?>
