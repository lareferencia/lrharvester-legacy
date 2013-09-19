<h2>{translate text="User Account"}</h2>

{if $message}<div class="error">{$message|translate}</div>{/if}

<form method="post" action="{$url}/MyResearch/Account" name="accountForm" id="accountForm">
  <label class="span-3" for="account_firstname">{translate text="First Name"}:</label>
  <input id="account_firstname" type="text" name="firstname" value="{$formVars.firstname|escape}" size="30" 
    class="mainFocus {jquery_validation required='This field is required'}"/><br class="clear"/>
  <label class="span-3" for="account_lastname">{translate text="Last Name"}:</label>
  <input id="account_lastname" type="text" name="lastname" value="{$formVars.lastname|escape}" size="30"
    class="{jquery_validation required='This field is required'}"/><br class="clear"/>
  <label class="span-3" for="account_email">{translate text="Email Address"}:</label>
  <input id="account_email" type="text" name="email" value="{$formVars.email|escape}" size="30"
    class="{jquery_validation required='This field is required' email='Email address is invalid'}"/><br class="clear"/>
  <label class="span-3" for="account_username">{translate text="Desired Username"}:</label>
  <input id="account_username" type="text" name="username" value="{$formVars.username|escape}" size="30"
    class="{jquery_validation required='This field is required'}"/><br class="clear"/>
  <label class="span-3" for="institution_username">{translate text="Institution}:</label>
  <input id="institution_username" type="text" name="institution" value="{$formVars.institution|escape}" size="40"
    class="{jquery_validation required='This field is required'}"/><br class="clear"/>	
<label class="span-3">{translate text="Country"}</label> <select id="country" name="country" size="1">
<option value="AF">Afganist&#225;n</option>
<option value="AL">Albania</option>
<option value="DE">Alemania</option>
<option value="AD">Andorra</option>
<option value="AO">Angola</option>
<option value="AI">Anguila</option>
<option value="AQ">Ant&#225;rtida</option>
<option value="AG">Antigua y Barbuda</option>
<option value="SA">Arabia Saudita</option>
<option value="DZ">Argelia</option>
<option value="AR">Argentina</option>
<option value="AM">Armenia</option>
<option value="AW">Aruba</option>
<option value="AU">Australia</option>
<option value="AT">Austria</option>
<option value="AZ">Azerbaiy&#225;n</option>
<option value="BS">Bahamas</option>
<option value="BH">Bahrein</option>
<option value="BD">Bangladesh</option>
<option value="BB">Barbados</option>
<option value="BY">Belar&#250;s</option>
<option value="BE">B&#233;lgica</option>
<option value="BZ">Belice</option>
<option value="BJ">Benin</option>
<option value="BM">Bermudas</option>
<option value="BT">Bhut&#225;n</option>
<option value="BO">Bolivia</option>
<option value="BQ">Bonaire, San Estaquio y Saba</option>
<option value="BA">Bosnia y Herzegovina</option>
<option value="BW">Botswana</option>
<option value="BR">Brasil</option>
<option value="BN">Brunei Darussalam</option>
<option value="BG">Bulgaria</option>
<option value="BF">Burkina Faso</option>
<option value="BI">Burundi</option>
<option value="CV">Cabo Verde</option>
<option value="KH">Camboya</option>
<option value="CM">Camer&#250;n</option>
<option value="CA">Canad&#225;</option>
<option value="TD">Chad</option>
<option value="CL">Chile</option>
<option value="CN">China</option>
<option value="CY">Chipre</option>
<option value="CO">Colombia</option>
<option value="KM">Comoras</option>
<option value="CG">Congo</option>
<option value="CR">Costa Rica</option>
<option value="CI">C&#244;te d'Ivoire</option>
<option value="HR">Croacia</option>
<option value="CU">Cuba</option>
<option value="CW">Cura&#231;&#227;o</option>
<option value="DK">Dinamarca</option>
<option value="DJ">Djibouti</option>
<option value="DM">Dominica</option>
<option value="EC">Ecuador</option>
<option value="EG">Egipto</option>
<option value="SV">El Salvador</option>
<option value="AE">Emiratos &#193;rabes Unidos</option>
<option value="ER">Eritrea</option>
<option value="SI">Eslovenia</option>
<option value="ES">Espa&#241;a</option>
<option value="US">Estados Unidos de Am&#233;rica</option>
<option value="UM">Estados Unidos Islas Menores</option>
<option value="EE">Estonia</option>
<option value="ET">Etiop&#237;a</option>
<option value="RU">Federaci&#243;n de Rusia</option>
<option value="FJ">Fiji</option>
<option value="PH">Filipinas</option>
<option value="FI">Finlandia</option>
<option value="FR">Francia</option>
<option value="GA">Gab&#243;n</option>
<option value="GM">Gambia</option>
<option value="GE">Georgia</option>
<option value="GS">Georgia del Sur e Islas Sandwich del Sur</option>
<option value="GH">Ghana</option>
<option value="GI">Gibraltar</option>
<option value="GD">Granada</option>
<option value="GR">Grecia</option>
<option value="GL">Groenlandia</option>
<option value="GP">Guadalupe</option>
<option value="GU">Guam</option>
<option value="GT">Guatemala</option>
<option value="GF">Guayana Francesa</option>
<option value="GG">Guernsey</option>
<option value="GN">Guinea</option>
<option value="GQ">Guinea Ecuatorial</option>
<option value="GW">Guinea-Bissau</option>
<option value="GY">Guyana</option>
<option value="HT">Hait&#237;</option>
<option value="HN">Honduras</option>
<option value="HK">Hong Kong </option>
<option value="HU">Hungr&#237;a</option>
<option value="IN">India</option>
<option value="ID">Indonesia</option>
<option value="IR">Ir&#225;n, Rep&#250;blica Isl&#225;mica del</option>
<option value="IQ">Iraq</option>
<option value="IE">Irlanda</option>
<option value="BV">Isla Bouvet</option>
<option value="IM">Isla de Man</option>
<option value="CX">Isla de Navidad</option>
<option value="IS">Islandia</option>
<option value="AX">Islas &#197;land</option>
<option value="KY">Islas Caim&#225;n</option>
<option value="CC">Islas Cocos (Keeling)</option>
<option value="CK">Islas Cook</option>
<option value="HM">Islas de Heard y McDonald</option>
<option value="FK">Islas Falkland (Malvinas)</option>
<option value="FO">Islas Feroe</option>
<option value="MP">Islas Marianas Septentrionales</option>
<option value="MH">Islas Marshall</option>
<option value="NF">Islas Norfolk</option>
<option value="SB">Islas Salom&#243;n</option>
<option value="SJ">Islas Svalbard y Jan Mayen</option>
<option value="TC">Islas Turcas y Caicos</option>
<option value="VG">Islas V&#237;rgenes Brit&#225;nicas</option>
<option value="VI">Islas V&#237;rgenes de los Estados Unidos</option>
<option value="WF">Islas Wallis y Futuna</option>
<option value="IL">Israel</option>
<option value="IT">Italia</option>
<option value="LY">Jamahiriya &#193;rabe Libia</option>
<option value="JM">Jamaica</option>
<option value="JP">Jap&#243;n</option>
<option value="JE">Jersey</option>
<option value="JO">Jordania</option>
<option value="KZ">Kazajst&#225;n</option>
<option value="KE">Kenya</option>
<option value="KG">Kirguist&#225;n</option>
<option value="KI">Kiribati</option>
<option value="KW">Kuwait</option>
<option value="LS">Lesotho</option>
<option value="LV">Letonia</option>
<option value="LB">L&#237;bano</option>
<option value="LR">Liberia</option>
<option value="LI">Liechtenstein</option>
<option value="LT">Lituania</option>
<option value="LU">Luxemburgo</option>
<option value="MO">Macao</option>
<option value="MK">Macedonia</option>
<option value="MG">Madagascar</option>
<option value="MY">Malasia</option>
<option value="MW">Malawi</option>
<option value="MV">Maldivas</option>
<option value="ML">Mal&#237;</option>
<option value="MT">Malta</option>
<option value="MA">Marruecos</option>
<option value="MQ">Martinica</option>
<option value="MU">Mauricio</option>
<option value="MR">Mauritania</option>
<option value="YT">Mayotte</option>
<option value="MX">M&#233;xico</option>
<option value="FM">Micronesia, Estados Federados de</option>
<option value="MC">M&#243;naco</option>
<option value="MN">Mongolia</option>
<option value="ME">Montenegro</option>
<option value="MS">Montserrat</option>
<option value="MZ">Mozambique</option>
<option value="MM">Myanmar</option>
<option value="NA">Namibia</option>
<option value="NR">Nauru</option>
<option value="NP">Nepal</option>
<option value="NI">Nicaragua</option>
<option value="NE">N&#237;ger</option>
<option value="NG">Nigeria</option>
<option value="NU">Niue</option>
<option value="NO">Noruega</option>
<option value="NC">Nueva Caledonia</option>
<option value="NZ">Nueva Zelandia</option>
<option value="OM">Om&#225;n</option>
<option value="NL">Pa&#237;ses Bajos</option>
<option value="PK">Pakist&#225;n</option>
<option value="PW">Palau</option>
<option value="PA">Panam&#225;</option>
<option value="PG">Papua Nueva Guinea</option>
<option value="PY">Paraguay</option>
<option value="PE">Per&#250;</option>
<option value="PN">Pitcairn</option>
<option value="PF">Polinesia Francesa</option>
<option value="PL">Polonia</option>
<option value="PT">Portugal</option>
<option value="TW">Provincia china de Taiw&#225;n</option>
<option value="PR">Puerto Rico</option>
<option value="QA">Qatar</option>
<option value="GB">Reino Unido</option>
<option value="SY">Rep&#250;blica &#193;rabe Siria</option>
<option value="CF">Rep&#250;blica Centroafricana</option>
<option value="CZ">Rep&#250;blica Checa</option>
<option value="KR">Rep&#250;blica de Corea</option>
<option value="MD">Rep&#250;blica de Moldova</option>
<option value="CD">Rep&#250;blica Democr&#225;tica del Congo</option>
<option value="LA">Rep&#250;blica Democr&#225;tica Popular Lao</option>
<option value="DO">Rep&#250;blica Dominicana</option>
<option value="SK">Rep&#250;blica Eslovaca</option>
<option value="KP">Rep&#250;blica Popular Democr&#225;tica de Corea</option>
<option value="TZ">Rep&#250;blica Unida de Tanzan&#237;a</option>
<option value="RE">Reuni&#243;n</option>
<option value="RO">Rumania</option>
<option value="RW">Rwanda</option>
<option value="EH">Sahara Occidental</option>
<option value="KN">Saint Kitts y Nevis</option>
<option value="MF">Saint Martin (parte Francesa)</option>
<option value="BL">Saint-Barth&#233;lemy</option>
<option value="WS">Samoa</option>
<option value="AS">Samoa Americana</option>
<option value="SM">San Marino</option>
<option value="SX">San Martin (parte Holandesa)</option>
<option value="PM">San Pedro y Miquel&#243;n</option>
<option value="VC">San Vicente y las Granadinas</option>
<option value="SH">Santa Elena, Ascensi&#243;n y Trist&#225;n da Cunha</option>
<option value="LC">Santa Luc&#237;a</option>
<option value="VA">Santa Sede</option>
<option value="ST">Santo Tom&#233; y Pr&#237;ncipe</option>
<option value="SN">Senegal</option>
<option value="RS">Serbia</option>
<option value="SC">Seychelles</option>
<option value="SL">Sierra Leona</option>
<option value="SG">Singapur</option>
<option value="SO">Somal&#237;</option>
<option value="LK">Sri Lanka</option>
<option value="ZA">Sud&#225;frica</option>
<option value="SD">Sud&#225;n</option>
<option value="SS">Sud&#225;n del Sur</option>
<option value="SE">Suecia</option>
<option value="CH">Suiza</option>
<option value="SR">Suriname</option>
<option value="SZ">Swazilandia</option>
<option value="TH">Tailandia</option>
<option value="TJ">Tayikist&#225;n</option>
<option value="PS">Territori Palest&#237;na Ocupat</option>
<option value="IO">Territorio Brit&#225;nico del Oc&#233;ano Indico</option>
<option value="TF">Territorios Australes Franceses</option>
<option value="TL">Timor-Leste</option>
<option value="TG">Togo</option>
<option value="TK">Tokelau</option>
<option value="TO">Tonga</option>
<option value="TT">Trinidad y Tabago</option>
<option value="TN">T&#250;nez</option>
<option value="TM">Turkmenist&#225;n</option>
<option value="TR">Turqu&#237;a</option>
<option value="TV">Tuvalu</option>
<option value="UA">Ucrania</option>
<option value="UG">Uganda</option>
<option value="UY">Uruguay</option>
<option value="UZ">Uzbekist&#225;n</option>
<option value="VU">Vanuatu</option>
<option value="VE">Venezuela</option>
<option value="VN">Viet Nam</option>
<option value="YE">Yemen</option>
<option value="ZM">Zambia</option>
<option value="ZW">Zimbabwe</option>
</select>
<br class="clear"/>	
  <label class="span-3" for="account_password">{translate text="Password"}:</label>
  <input id="account_password" type="password" name="password" size="15"
    class="{jquery_validation required='This field is required'}"/><br class="clear"/>
  <label class="span-3" for="account_password2">{translate text="Password Again"}:</label>
  <input id="account_password2" type="password" name="password2" size="15"
    class="{jquery_validation required='This field is required' equalTo='Passwords do not match' equalToField='#account_password'}"/><br class="clear"/>

   <strong>Proporcione el c&oacute;digo:</strong><input type="text" name="captcha_code" size="12" maxlength="16" class="{jquery_validation required='This field is required'}"/>
<a tabindex="-1" style="border-style: none;" href="#" title="Refresh Image" onclick="document.getElementById('siimage').src = '{$localhost}{$captcha}/securimage_show.php?sid=' + Math.random(); this.blur(); return false"><img src="{$localhost}{$captcha}/images/refresh.png" alt="Reload Image" onclick="this.blur()" align="bottom" border="0" width="20px"></a><br />	
	<div class="clear"></div>
	<img id="siimage" style="border: 1px solid #000; margin-left: 120px" src="{$localhost}{$captcha}/securimage_show.php?sid=<?php echo md5(uniqid()) ?>" alt="CAPTCHA Image" align="left">
	<br />  
	<div class="clear"></div>
    
  <input class="push-3 button" type="submit" name="submit" value="{translate text="Submit"}"/>
  <div class="clear"></div>
</form>
<script>
  {literal}
  $(document).ready(function() {
    $('#accountForm').validate();
  });
  {/literal}
</script>