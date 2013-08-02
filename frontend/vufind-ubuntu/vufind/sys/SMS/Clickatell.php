<?php
/**
 * Class for text messaging via Clickatell's HTTP API
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
require_once 'Base.php';

/**
 * Class for text messaging via Clickatell's HTTP API
 *
 * @category VuFind
 * @package  Support_Classes
 * @author   Ronan McHugh <vufind-tech@lists.sourceforge.net>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/system_classes Wiki
 */
class SMS_Clickatell extends SMS_Base
{
    /**
     * Send a text message to the specified provider.
     *
     * @param string $provider The provider ID to send to
     * @param string $to       The phone number at the provider
     * @param string $from     The email address to use as sender
     * @param string $message  The message to send
     *
     * @return mixed           PEAR error on error, boolean true otherwise
     * @access public
     */
    public function text($provider, $to, $from, $message)
    {
        // Get API settings from config
        $user = isset($this->smsConfig['Clickatell']['user'])
            ? $this->smsConfig['Clickatell']['user'] : null;
        $password = isset($this->smsConfig['Clickatell']['password'])
            ? $this->smsConfig['Clickatell']['password'] : null;
        $api_id = isset($this->smsConfig['Clickatell']['api_id'])
            ? $this->smsConfig['Clickatell']['api_id'] : null;
        $url = isset($this->smsConfig['Clickatell']['url'])
            ? trim($this->smsConfig['Clickatell']['url'], '?')
            : "https://api.clickatell.com/http/sendmsg";

        // Clickatell expects UCS-2 encoding:
        if (!function_exists('iconv')) {
            return new PEAR_Error('Clickatell requires iconv PHP extension.');
        }
        // Normalize UTF-8 if intl extension is installed:
        if (class_exists('Normalizer')) {
            $message = Normalizer::normalize($message);
        }
        $message = iconv('UTF-8', 'ISO-8859-1//TRANSLIT', $message);

        // We need to trim to 160 bytes (note that we need to use substr and not
        // mb_substr, because the limit is BYTES not CHARACTERS; this may result
        // in broken multi-byte characters but it seems unavoidable):
        $message = substr($message, 0, 160);

        // Add parameters to URL:
        $url .= "?api_id=" . urlencode($api_id);
        $url .= "&user=" . urlencode($user);
        $url .= "&password=" . urlencode($password);
        $url .= "&to=" . urlencode($this->filterPhoneNumber($to));
        $url .= "&text=" . urlencode($message);

        $client = new Proxy_Request();
        $client->setMethod(HTTP_REQUEST_METHOD_GET);
        $client->setURL($url);
        $request = $client->sendRequest();
        if (PEAR::isError($request)) {
            return $request;
        }
        $response = trim($client->getResponseBody());
        if (empty($response)) {
            return new PEAR_Error('Problem sending text.');
        }
        if ('ID:' !== substr($response, 0, 3)) {
            return new PEAR_Error($response);
        }
        return true;
    }

    /**
     * Get a list of carriers supported by the module.  Returned as an array of
     * associative arrays indexed by carrier ID and containing "name" and "domain"
     * keys.
     *
     * @access public
     * @return array
     */
    public function getCarriers()
    {
        return array(
            'Clickatell' => array('name' => 'Clickatell', 'domain' => null)
        );
    }
}
