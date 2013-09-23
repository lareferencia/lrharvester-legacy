<h2>{translate text="Password Recovery"}</h2>

{if $message}<div class="error">{$message|translate}</div>{/if}

<form method="post" action="{$url}/MyResearch/Recover" name="accountForm" id="accountForm">
   <label class="span-3" for="account_email">{translate text="Email Address"}:</label>
  <input id="account_email" type="text" name="email" value="{$formVars.email|escape}" size="30"
    class="{jquery_validation required='This field is required' email='Email address is invalid'}"/><br class="clear"/>
   <strong>{translate text="Enter Code"}:</strong><input autocomplete="off" type="text" name="captcha_code" size="12" maxlength="16" class="{jquery_validation required='This field is required'}"/>
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