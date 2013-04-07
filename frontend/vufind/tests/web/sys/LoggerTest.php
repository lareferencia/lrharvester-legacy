<?php
/**
 * Logger Test Class
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
require_once dirname(__FILE__) . '/../prepend.inc.php';
require_once 'sys/Logger.php';

/**
 * Logger Test Class
 *
 * @category VuFind
 * @package  Tests
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/unit_tests Wiki
 */
class LoggerTest extends PHPUnit_Framework_TestCase
{
    private $_logger;
    private $_testOutputFile;
    private $_oldConfigArray;

    /**
     * Constructor
     *
     * @access public
     */
    public function __construct()
    {
        $this->_testOutputFile = dirname(__FILE__) . "/../log/messages.log";
    }

    /**
     * Standard setup method.
     *
     * @return void
     * @access public
     */
    public function setUp()
    {
        global $configArray;
        $this->_oldConfigArray = isset($configArray) ? $configArray : array();
        $configArray = array("Logging" => array("file" => $this->_testOutputFile . ':emerge-3,alert-3,error-1,notice-1,debug-1'));
        $this->_logger = new Logger();
    }

    /**
     * Test that constructor succeeded.
     *
     * @return void
     * @access public
     */
    public function testLogger()
    {
        $this->assertNotNull($this->_logger);
    }

    /**
     * Test file-based logging.
     *
     * @return void
     * @access public
     */
    public function testLogToFile()
    {
        // Make sure there is no old log hanging around:
        @unlink($this->_testOutputFile);

        // Log some messages:
        $this->_logger->log(array(1 => 'Emerge', 2 => 'Emerge more!', 3 => 'Emerge even more'), PEAR_LOG_EMERG);
        $this->_logger->log(array(1 => 'Alert', 2 => 'Alert more!', 3 => 'Alert even more'), PEAR_LOG_ALERT);
        $this->_logger->log('Critical', PEAR_LOG_CRIT);
        $this->_logger->log('Error', PEAR_LOG_ERR);
        $this->_logger->log('Warning', PEAR_LOG_WARNING);
        $this->_logger->log('Notice', PEAR_LOG_NOTICE);
        $this->_logger->log('Info', PEAR_LOG_INFO);
        $this->_logger->log('Debug', PEAR_LOG_DEBUG);

        // Check the log file for the expected content:
        $log = file($this->_testOutputFile);
        $expected = array(
            "vufind [emergency] Emerge even more",
            "vufind [alert] Alert even more",
            "vufind [critical] Critical",
            "vufind [error] Error",
            "vufind [warning] Warning",
            "vufind [notice] Notice",
            "vufind [info] Info",
            "vufind [debug] Debug");
        $this->assertEquals(count($expected), count($log));
        for ($x = 0; $x < count($log); $x++) {
            $log[$x] = trim($log[$x]);
            $this->assertEquals(substr($log[$x], -strlen($expected[$x])), $expected[$x]);
        }

        // Clean up after ourselves:
        unlink($this->_testOutputFile);
    }

    /**
     * Standard teardown method.
     *
     * @return void
     * @access public
     */
    public function tearDown()
    {
        global $configArray;
        $configArray = $this->_oldConfigArray;
    }
}
?>
