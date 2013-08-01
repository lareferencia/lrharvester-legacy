{foreach from=$result item=item name=recordLoop}
  <div class="collectionBrowseEntry listBrowse {if ($smarty.foreach.recordLoop.iteration % 2) == 0}alt {/if}">
    <div class="collectionBrowseHeading">
      <a href="{$path}/Collection/{$item[2]|urlencode}">{$item[0]|escape:"html"}</a>
    </div>
    {* subtract one from the number of items to exclude the record representing the collection itself. *}
    <div class="collectionBrowseCount"><b>{math equation="a - 1" a=$item[1]}</b> {translate text="items"}</div>
    <div class="clearer"><!-- empty --></div>
  </div>
{/foreach}