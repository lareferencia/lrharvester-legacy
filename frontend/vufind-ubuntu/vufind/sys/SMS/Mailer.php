<?php
/**
 * SMS Mailer Class
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
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/system_classes Wiki
 */
require_once 'Interface.php';
require_once 'Base.php';

/**
 * SMS Mailer Class
 *
 * This class extends the VuFindMailer to send text messages.
 *
 * @category VuFind
 * @package  Support_Classes
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/system_classes Wiki
 */
class SMS_Mailer extends SMS_Base
{
    /**
     * Default carriers, usually overridden by contents of web/conf/sms.ini.
     *
     * @var array
     */
    private $_carriers = array(
        'virgin' => array('name' => 'Virgin Mobile', 'domain' => 'vmobl.com'),
        'att' => array('name' => 'AT&T', 'domain' => 'txt.att.net'),
        'verizon' => array('name' => 'Verizon', 'domain' => 'vtext.com'),
        'nextel' => array('name' => 'Nextel', 'domain' => 'messaging.nextel.com'),
        'sprint' => array('name' => 'Sprint', 'domain' => 'messaging.sprintpcs.com'),
        'tmobile' => array('name' => 'T Mobile', 'domain' => 'tmomail.net'),
        'alltel' => array('name' => 'Alltel', 'domain' => 'message.alltel.com'),
        'Cricket' => array('name' => 'Cricket', 'domain' => 'mms.mycricket.com')
    );

    /**
     * Constructor
     *
     * Sets up SMS carriers and other settings from sms.ini.
     *
     * @access public
     */
    public function __construct()
    {
        parent::__construct();

        // if using sms.ini, then load the carriers from it
        // otherwise, fall back to the default list of US carriers
        if (isset($this->smsConfig['Carriers'])
            && !empty($this->smsConfig['Carriers'])
        ) {
            $this->_carriers = array();
            foreach ($this->smsConfig['Carriers'] as $id=>$config) {
                list($domain, $name) = explode(':', $config, 2);
                $this->_carriers[$id] = array('name'=>$name, 'domain'=>$domain);
            }
        }
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
        return $this->_carriers;
    }

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
        $knownCarriers = array_keys($this->_carriers);
        if (empty($provider) || !in_array($provider, $knownCarriers)) {
            return new PEAR_Error('Unknown Carrier');
        }

        $to = $this->filterPhoneNumber($to)
            . '@' . $this->_carriers[$provider]['domain'];
        $mail = new VuFindMailer();
        $subject = '';
        return $mail->send($to, $from, $subject, $message);
    }
}
