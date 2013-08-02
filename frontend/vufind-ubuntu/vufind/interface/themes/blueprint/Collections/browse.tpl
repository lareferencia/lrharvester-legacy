{capture name=pagelinks}
  <div class="alphaBrowsePageLinks">
    {if isset($prevpage)}
      <div class="alphaBrowsePrevLink"><a href="{$path}/Collections/Home?source={$source|escape:"url"}&amp;from={$from|escape:"url"}&amp;page={$prevpage|escape:"url"}{if $filtersString}{$filtersString}{/if}">&laquo; {translate text="Prev"}</a></div>
    {/if}

    {if isset($nextpage)}
      <div class="alphaBrowseNextLink"><a href="{$path}/Collections/Home?source={$source|escape:"url"}&amp;from={$from|escape:"url"}&amp;page={$nextpage|escape:"url"}{if $filtersString}{$filtersString}{/if}">{translate text="Next"} &raquo;</a></div>
    {/if}
    <div class="clear"></div>
  </div>
{/capture}
{if $filterList}
    <strong>{translate text='Remove Filters'}</strong>
    <ul class="filters">
    {foreach from=$filterList item=filters key=field}
      {foreach from=$filters item=filter}
        <li>
          <a href="{$filter.removalUrl|escape}"><img src="{$path}/images/silk/delete.png" alt="Delete"/></a>
          <a href="{$filter.removalUrl|escape}">{$filter.display|escape}</a>
        </li>
      {/foreach}
    {/foreach}
    </ul>
{/if}
<div class="browseAlphabetSelector">
  {foreach from=$letters item=letter}
   <div class="browseAlphabetSelectorItem"><a href="{$path}/Collections/Home?from={$letter}{if $filtersString}{$filtersString}{/if}">{$letter}</a></div>
  {/foreach}
</div>

<div class="browseJumpTo">
<form method="GET" action="{$path}/Collections/Home" class="browseForm">
  <input type="submit" value="{translate text='Jump to'}" />
  <input type="text" name="from" value="{$from|escape:"html"}" />
</form>
</div>

<div class="clear">&nbsp;</div>

<h2>{translate text="Collection Browse"}</h2>

<div class="collectionBrowseResult">
  {$smarty.capture.pagelinks}
  {include file=$browseView}
  <div class="clearer"></div>
  {$smarty.capture.pagelinks}
</div>