{js filename="search_hierarchyTree.js}
{if $recordCount}
  {* Recommendations *}
  {if $topRecommendations}
    {foreach from=$topRecommendations item="recommendations"}
      {include file=$recommendations}
    {/foreach}
  {/if}
  {translate text="Showing"}
  <b>{$recordStart}</b> - <b>{$recordEnd}</b>
  {translate text='of'} <b>{$recordCount}</b>
  {translate text='Items'}
  <form class="collectionSortSelector" action="{$path}/Search/SortResults" method="post">
      <label for="sort_options_1">{translate text='Sort'}</label>
      <select id="sort_options_1" name="sort" class="jumpMenu">
        {foreach from=$sortList item=sortData key=sortLabel}
          <option value="{$sortData.sortUrl|escape}"{if $sortData.selected} selected="selected"{/if}>{translate text=$sortData.desc}</option>
        {/foreach}
      </select>
      <noscript><input type="submit" value="{translate text="Set"}" /></noscript>
  </form>
  {if $viewList|@count gt 1}
    <div class="collectionViewSelection">
      {foreach from=$viewList item=viewData key=viewLabel}
        {if !$viewData.selected}<a href="{$url|escape}/Collection/{$id|urlencode}/CollectionList?page={$page}&view={$viewData.desc|lower}#tabnav" title="{translate text='Switch view to'} {translate text=$viewData.desc}" >{/if}
        <img src="{$path}/images/view_{$viewData.viewType}.png" {if $viewData.selected}title="{translate text=$viewData.desc} {translate text='view already selected'}"{/if}/>
        {if !$viewData.selected}</a>{/if}
      {/foreach}
    </div>
  {/if}
  <div class="paginationTop">{$pageLinks.all}</div>
  <div class="clearer"></div>
{/if}
{if $recordSet}
  {include file= $searchPage}
{else}
  {translate text='collection_empty'}
{/if}
{if $pageLinks.all}<div class="pagination">{$pageLinks.all}</div>{/if}
