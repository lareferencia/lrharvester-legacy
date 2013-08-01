{if $facetCloudSet}
  <div class="sidegroup">
    {foreach from=$facetCloudSet item=facets}
      {assign var='itemCount' value=0}
      <dl class="narrowList navmenu">
        <dt>{translate text=$facets.label}</dt>
        {assign var=ellipsesDisplayed value=0}
        {foreach from=$facets.list item=facetItem name="itemsLoop"}{assign var='itemCount' value=$itemCount+1}{if $itemCount <= $cloudLimit}{if $itemCount != 1}, {/if}<a href="{$facetItem.expandUrl|escape}">{$facetItem.value|escape}</a> ({$facetItem.count|escape}){else}{if !$ellipsesDisplayed}, ...{assign var=ellipsesDisplayed value=1}{/if}{/if}{/foreach}
      </dl>
    {/foreach}
  </div>
{/if}
