<script type="text/javascript">
{literal}
var hierarchySettings = {
{/literal}
    lightboxMode: {if $lightbox == true}true{else}false{/if},
    fullHierarchy: {if !isset($treeSettings.fullHierarchyRecordView) || $treeSettings.fullHierarchyRecordView == true || $disablePartialHierarchy == true}true{else}false{/if}
{literal}
};
{/literal}
vufindString.showTree = "{translate text="hierarchy_show_tree"}";
vufindString.hideTree = "{translate text="hierarchy_hide_tree"}";
</script>
{js filename="jsTree/jquery.jstree.js"}
{js filename="hierarchyTree_JSTree.js"}

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
        {if $showTreeSearch}
          <div id="treeSearch">
            <span id="treeSearchNoResults">{translate text="nohit_heading"}</span>
            <input id="search" type="button" value="search" />
            <select id="treeSearchType" name="type">
              <option value="AllFields">{translate text="All Fields"}</option>
              <option value="Title">{translate text="Title"}</option>
            </select>
            <input id="treeSearchText" type="text" value="" />
            <span id="treeSearchLoadingImg"><img src="{$path}/images/loading.gif"/></span>
          </div>
          <div id="treeSearchLimitReached">{translate text="tree_search_limit_reached_html" tokens=$treeLimitTokens}</div>
        {/if}
        <div id="hierarchyTree">
          <input type="hidden" value="{$id|escape}" class="hiddenRecordId" />
          <input type="hidden" value="{$hierarchyID|escape}" class="hiddenHierarchyId" />
          <input type="hidden" value="{$context|escape}" class="hiddenContext" />
          <noscript>
            {$hierarchyTree}
          </noscript>
        </div>
    </div>
    {/if}
</div>
