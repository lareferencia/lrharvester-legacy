<h2>{translate text="Cuenta de Administrador"}</h2>

{if $message}<div class="error">{$message|translate}</div>{/if}

<form method="post" action="{$url}/MyResearch/Account" name="accountForm" id="accountForm">
 <input type="hidden" name="type" value="administrator">
<label class="span-3">{translate text="Red"}</label> <select id="admin" name="admin" size="1">
	<option value="AR">Argentina</option>
	<option value="BR">Brasil</option>
	<option value="CL">Chile</option>
	<option value="CO">Colombia</option>
	<option value="EC">Ecuador</option>
	<option value="SV">El Salvador</option>
	<option value="MX">M&#233;xico</option>
	<option value="PE">Per&#250;</option>
	<option value="VE">Venezuela</option>
</select>
<br class="clear"/>	
  <label class="span-3" for="account_authorization">{translate text="C&oacute;digo de Autorizaci&oacute;n"}:</label>
  <input id="account_authorization" type="text" name="authorization" value="{$formVars.authorization|escape}" size="10" 
    class="mainFocus {jquery_validation required='This field is required'}"/><br class="clear"/>
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