<div id="bd">
  <div id="yui-main" class="content">
    <div class="disambiguationDiv" >
      {if (empty($collections))}
        <h1>{translate text="collection_empty"}</h1>
      {else}
        <h1>{translate text="collection_disambiguation"}</h1>
        <div id="disambiguationItemsDiv">
          {foreach from=$collections item=collection name="disambLoop"}
           <div class="disambiguationItem {if ($smarty.foreach.disambLoop.iteration % 2) != 0}alt {/if}record{$smarty.foreach.disambLoop.iteration}">
             <a href="{$url}/Collection/{$collection.id|urlencode}">{$collection.title|escape}</a>
             <p>{$collection.description|escape}</p>
           </div>
          {/foreach}
        </div>
      {/if}
    </div>
  </div>
</div>
