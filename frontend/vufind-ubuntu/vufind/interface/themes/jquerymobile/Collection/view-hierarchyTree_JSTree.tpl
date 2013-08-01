<div>
  {if $collectionRecord !== false}
    {if $collectionRecord === null}
      {include file="Collection/collection-record-error.tpl"}
    {else}
      {include file=$collectionRecord}
    {/if}
  {else}
    {if count($hierarchyTreeList) > 1}
      <div id="treeSelector">
        {foreach from=$hierarchyTreeList item=hierarchyTitle key=hierarchy}
          <a class="tree{if $hierarchyID == $hierarchy} currentTree{/if}" href="{$url}/Collection/{$id|urlencode}/HierarchyTree?hierarchy={$hierarchy|escape:"url"}">{$hierarchyTitle|escape}</a>
        {/foreach}
      </div>
    {/if}
    {if $hierarchyID}
      <div id="hierarchyTreeHolder">
        <div id="hierarchyTree">
          {$hierarchyTree}
        </div>
      </div>
    {/if}
  {/if}
</div>