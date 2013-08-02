{if $errorMsg}<div class="error">{$errorMsg|translate}</div>{/if}
{if $infoMsg}<div class="info">{$infoMsg|translate}</div>{/if}

<form action="{$url}/Search/Email" method="post" name="emailSearch">
    <input type="hidden" name="url" value="{$searchURL|escape:"html"}"/>

	<label class="displayBlock" for="email_from">{translate text='From'}:</label>
    <input id="email_from_false" disabled value="{$user->email}" type="text" name="from_false" size="40" />
    <input id="email_from"  type="hidden" value="{$user->email}" type="text" name="from" size="40" />

	
    <label class="displayBlock" for="email_to">{translate text='To'}:</label>
    <input id="email_to" type="text" name="to" size="40" class="mainFocus {jquery_validation required='This field is required' email='Email address is invalid'}"/>

	<label class="displayBlock" for="email_message">{translate text='Asunto'}:</label>
    <input id="subject" type="text" name="subject" size="60" "/>
	
    <label class="displayBlock" for="email_message">{translate text='Message'}:</label>
    <textarea id="email_message" name="message" rows="3" cols="60"></textarea>
    <br/>
    <input class="button" type="submit" name="submit" value="{translate text='Send'}"/>
</form>
