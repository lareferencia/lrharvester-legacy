<div data-role="page" id="Record-view">
  {include file="header.tpl"}
  <div class="record" data-role="content" data-record-id="{$id}">
    {if $action == 'Home' || $action == 'Holdings'}
      {include file=$coreMetadata}
    {else}
      <h3>
        {$coreShortTitle|escape}
        {if $coreSubtitle}{$coreSubtitle|escape}{/if}
        {if $coreTitleSection}{$coreTitleSection|escape}{/if}
      </h3>
    {/if}
    {include file="Record/$subTemplate"}
    {* Show the "Tag this" button only on Record/Home or Record/Holdings *} 
    {if $action == 'Home' || $action == 'Holdings'}
      <div data-role="controlgroup">
        <a href="{$path}/Record/{$id}/Save" data-role="button" rel="external">{translate text="Add to favorites"}</a>
        <a href="{$path}/Record/{$id}/AddTag" data-role="button" rel="external">{translate text="Add Tag"}</a>
      </div>
    {/if}
  </div>    
  {include file="footer.tpl"}
</div>
