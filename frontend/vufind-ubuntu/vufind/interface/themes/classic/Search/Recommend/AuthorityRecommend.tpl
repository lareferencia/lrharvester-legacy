{if $authResultSet}
  <div class="yui-g resulthead authoritybox">
    <div><strong>{translate text='See also'}:</strong></div>
    <div>
      {foreach from=$authResultSet item=record name=recordLoop}
        <a href="{$record.authLink}">{$record.heading|escape}</a>{if !$smarty.foreach.recordLoop.last}, {/if}
      {/foreach}
    </div>
  </div>
{/if}