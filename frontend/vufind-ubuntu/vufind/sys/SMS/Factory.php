<?php
/**
 * Factory for instantiating SMS objects
 *
 * PHP version 5
 *
 * Copyright (C) Villanova University 2009.
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
 * @package  Support_Classes
 * @author   Ronan McHugh <vufind-tech@lists.sourceforge.net>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/system_classes Wiki
 */

/**
 * Factory for instantiating SMS objects
 *
 * @category VuFind
 * @package  Support_Classes
 * @author   Ronan McHugh <vufind-tech@lists.sourceforge.net>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/system_classes Wiki
 */
class SMS_Factory
{
    /**
     * Build SMS subclass based on method specified
     *
     * @param string $method Type of SMS object to build (null for default).
     *
     * @return mixed         Object on success, false on failure.
     */
    static function initSMS($method = null)
    {
        if ($method === null) {
            $method = self::_getMethod();
        }
        $path = dirname(__FILE__) . "/" . $method . ".php";
        if (is_readable($path)) {
            include_once $path;
            $class = "SMS_" . $method;
            if (class_exists($class)) {
                $sms = new $class();
                return $sms;
            }
        }
        return false;
    }

    /**
     * Get SMS sending method from config
     *
     * @return string
     */
    private static function _getMethod()
    {
        $smsConfig = getExtraConfigArray('sms');
        $method = isset($smsConfig['General']['smsType'])
            ? $smsConfig['General']['smsType'] : 'Mailer';
        return $method;
    }

}
