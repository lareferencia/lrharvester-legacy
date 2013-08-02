<?php
/**
 * Account action for MyResearch module
 *
 * PHP version 5
 *
 * Copyright (C) Villanova University 2007.
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
 * @package  Controller_MyResearch
 * @author   Andrew S. Nagy <vufind-tech@lists.sourceforge.net>
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
require_once "Action.php";
require_once 'sys/Mailer.php'; //Para enviar mail
require_once 'sys/User.php';

require_once 'Mail/RFC822.php';

include_once $_SERVER['DOCUMENT_ROOT'] . '/securimage/securimage.php';
/**
 * Account action for MyResearch module
 *
 * @category VuFind
 * @package  Controller_MyResearch
 * @author   Andrew S. Nagy <vufind-tech@lists.sourceforge.net>
 * @author   Demian Katz <demian.katz@villanova.edu>
 * @license  http://opensource.org/licenses/gpl-2.0.php GNU General Public License
 * @link     http://vufind.org/wiki/building_a_module Wiki
 */
class AccountAdmin extends Action
{
    /**
     * Constructor
     *
     * @access public
     */
    public function __construct()
    {
    }

    /**
     * Process parameters and display the page.
     *
     * @return void
     * @access public
     */
    public function launch()
    {
        global $interface;
        global $configArray;
        
        // Don't allow account creation if a non-DB authentication method
        // is being used!!
        if ($configArray['Authentication']['method'] !== 'DB') {
            header('Location: Home');
            die();
        }

        if (isset($_POST['submit'])) {
            $result = $this->_processInput();
            if (PEAR::isError($result)) {
                $interface->assign('message', $result->getMessage());
                $interface->assign('formVars', $_POST);
                $interface->setTemplate('accountadmin.tpl');
                $interface->display('layout.tpl');
            } else {
                // Now that the account is created, log the user in:
                UserAccount::login();
                header('Location: Home');
                die();
            }
        } else {
            $interface->setPageTitle('Cuenta de Administrador');
            $interface->setTemplate('accountadmin.tpl');
            $interface->display('layout.tpl');
        }
    }

    /**
     * Send a record email.
     *
     * @param string $to      Message recipient address
     * @param string $from    Message sender address
     * @param string $message Message to send
     *
     * @return mixed          Boolean true on success, PEAR_Error on failure.
     * @access public
     */
    public function sendEmail($to, $from, $message)
    {
        global $interface;

        $subject = translate("Registro de usuario en LA-Referencia");
        $body = $message;
        $mail = new VuFindMailer();
        return $mail->send($to, $from, $subject, $body);
    }
	
    
    /**
     * Process incoming parameters for account creation.
     *
     * @return mixed True on successful account creation, PEAR_Error otherwise.
     * @access private
     */
    private function _processInput()
    {
        // Validate Input
        if (trim($_POST['username']) == '') {
            return new PEAR_Error('Username cannot be blank');
        }
        if (trim($_POST['password']) == '') {
            return new PEAR_Error('Password cannot be blank');
        }
        if ($_POST['password'] != $_POST['password2']) {
            return new PEAR_Error('Passwords do not match');
        }
        if (!Mail_RFC822::isValidInetAddress($_POST['email'])) {
            return new PEAR_Error('Email address is invalid');
        }

        // Create Account
        $user = new User();
        $user->username = $_POST['username'];
        if (!$user->find()) {
            // No username match found -- check for duplicate email:
            $user = new User();
            $user->email = $_POST['email'];
            if (!$user->find()) {
			
				 session_start();
			     $securimage = new Securimage();
				 
				 if ($securimage->check($_POST['captcha_code'])) {
					//if (($_POST['authorization']==="adminl4r3f")) {
						// We need to reassign the username since we cleared it out when
						// we did the search for duplicate email addresses:
						$user->username = $_POST['username'];
						$user->password = $_POST['password'];
						$user->firstname = $_POST['firstname'];
						$user->lastname = $_POST['lastname'];
						//$user->laref_country = $_POST['country'];
						$user->laref_country = $_POST['admin'];
						$user->laref_institution = $_POST['institution'];
						$user->admin_country = $_POST['admin'];
						$user->created = date('Y-m-d h:i:s');
						$user->insert();
						$bienvenida="Datos de registro\nAdministrador:".$user->username."\nContraseña:".$user->password."\nFecha:".date('Y-m-d h:i:s');
						echo $bienvenida;
						$result2 = $this->sendEmail($user->email, 'admin@lareferencia.net', $bienvenida);
					//}
					
				} else {
                return new PEAR_Error('The security code entered was incorrect');
				}
            } else {
                return new PEAR_Error('That email address is already used');
            }
        } else {
            return new PEAR_Error('That username is already taken');
        }
        
        return true;
    }
}

?>