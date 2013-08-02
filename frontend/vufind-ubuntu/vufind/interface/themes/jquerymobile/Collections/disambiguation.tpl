<div data-role="page" id="Search-list" class="results-page">
  {include file="header.tpl"}
  <div data-role="content">
    {if (!empty($collections))}
      <ul class="results" data-role="listview" data-split-icon="plus" data-split-theme="c">
        {foreach from=$collections item=collection name="disambLoop"}
         <li>
           <a rel="external" href="{$url}/Collection/{$collection.id|urlencode}"><h3>{$collection.title|escape}</h3>
           <p>{$collection.description|escape}</p>
           </a>
         </li>
        {/foreach}
      </ul>
    {/if}
  </div>
  {include file="footer.tpl"}
</div>
