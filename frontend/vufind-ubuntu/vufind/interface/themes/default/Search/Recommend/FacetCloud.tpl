{if $facetCloudSet}
  <div class="box submenu narrow">
    <ul>
      {foreach from=$facetCloudSet item=facets}
        {assign var='itemCount' value=0}
        <li>
          <h3>{translate text=$facets.label}</h3>
          {assign var=ellipsesDisplayed value=0}
          {foreach from=$facets.list item=facetItem name="itemsLoop"}{assign var='itemCount' value=$itemCount+1}{if $itemCount <= $cloudLimit}{if $itemCount != 1}, {/if}<a href="{$facetItem.expandUrl|escape}">{$facetItem.value|escape}</a> ({$facetItem.count|escape}){else}{if !$ellipsesDisplayed}, ...{assign var=ellipsesDisplayed value=1}{/if}{/if}{/foreach}
        </li><br/>
      {/foreach}
    </ul>
  </div>
{/if}
