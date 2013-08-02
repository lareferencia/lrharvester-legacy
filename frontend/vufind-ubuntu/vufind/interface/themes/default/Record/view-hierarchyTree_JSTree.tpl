<div>
    {if count($hierarchyTreeList) > 1}
    <div id="treeSelector">
    {foreach from=$hierarchyTreeList item=hierarchyTitle key=hierarchy}
    <a class="tree{if $hierarchyID == $hierarchy} currentTree{/if}" href="{$url}/Record/{$id}/HierarchyTree?hierarchy={$hierarchy|escape:"url"}">{$hierarchyTitle|escape}</a>
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
</div>
