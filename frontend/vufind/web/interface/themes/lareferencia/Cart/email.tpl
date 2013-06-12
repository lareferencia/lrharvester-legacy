{if $errorMsg}<div class="error">{$errorMsg|translate}</div>{/if}
{if $infoMsg}<div class="info">{$infoMsg|translate}</div>{/if}
  <strong>{translate text='APA'}:</strong><br/><br/>
  {foreach from=$emailList item=emailItem}
  {$emailItem.apa|escape}<br />
  <a href={$url}/Record/{$emailItem.id|escape}>{$url}/Record/{$emailItem.id|escape}</a><br /><br />
  {/foreach}
  <br />
