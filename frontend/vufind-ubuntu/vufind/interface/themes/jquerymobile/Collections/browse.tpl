{capture name=pagelinks}
  <div class="ui-grid-a">
    {if isset($prevpage)}
      <div class="ui-block-a">
        <a rel="external" data-role="button" data-mini="true" data-icon="arrow-l" href="{$path}/Collections/Home?source={$source|escape:"url"}&amp;from={$from|escape:"url"}&amp;page={$prevpage|escape:"url"}{if $filtersString}{$filtersString}{/if}">&laquo; {translate text="Prev"}</a>
      </div>
    {/if}
    {if isset($nextpage)}
      <div class="ui-block-b">
        <a rel="external" data-role="button" data-mini="true" data-icon="arrow-r" href="{$path}/Collections/Home?source={$source|escape:"url"}&amp;from={$from|escape:"url"}&amp;page={$nextpage|escape:"url"}{if $filtersString}{$filtersString}{/if}">{translate text="Next"} &raquo;</a>
       </div>
    {/if}
  </div>
{/capture}
<div data-role="page" id="Search-list" class="results-page">
  {include file="header.tpl"}
  {$smarty.capture.pagelinks}
  <div data-role="content">
    {include file=$browseView}
  </div>
  {$smarty.capture.pagelinks}
  {include file="footer.tpl"}
</div>
