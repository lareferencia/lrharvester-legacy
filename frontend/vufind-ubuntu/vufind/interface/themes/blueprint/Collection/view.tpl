{js filename="ajax_common.js"}
{js filename="record.js"}
{js filename="collection_record.js"}
{js filename="check_save_statuses.js"}
{if !empty($addThis)}
<script type="text/javascript" src="https://s7.addthis.com/js/250/addthis_widget.js?pub={$addThis|escape:"url"}"></script>
{/if}
<div class="span-{if $tab == 'HierarchyTree'}23{else}18{/if}">
  <div class="record" id="collection{$id|escape}">
    <div class="toolbar">
      <span class="backSpan">
        {if $lastsearch}
          <a href="{$lastsearch|escape}#record{$id|escape:"url"}" class="backtosearch">&laquo; {translate text="Back to Search Results"}</a>
        {/if}
      </span>
      <ul>
        <li><a href="{$url}/Record/{$id|escape:"url"}/Cite" class="citeRecord cite" id="citeRecord{$id|escape}" title="{translate text="Cite this"}">{translate text="Cite this"}</a></li>
        <li><a href="{$url}/Record/{$id|escape:"url"}/SMS" class="smsRecord sms" id="smsRecord{$id|escape}" title="{translate text="Text this"}">{translate text="Text this"}</a></li>
        <li><a href="{$url}/Record/{$id|escape:"url"}/Email" class="mailRecord mail" id="mailRecord{$id|escape}" title="{translate text="Email this"}">{translate text="Email this"}</a></li>
        {if is_array($exportFormats) && count($exportFormats) > 0}
        <li>
          <a href="{$url}/Record/{$id|escape:"url"}/Export?style={$exportFormats.0|escape:"url"}" class="export exportMenu">{translate text="Export Record"}</a>
          <ul class="menu offscreen" id="exportMenu">
          {foreach from=$exportFormats item=exportFormat}
            <li><a {if $exportFormat=="RefWorks"}target="{$exportFormat}Main" {/if}href="{$url}/Record/{$id|escape:"url"}/Export?style={$exportFormat|escape:"url"}">{translate text="Export to"} {$exportFormat|escape}</a></li>
          {/foreach}
          </ul>
        </li>
        {/if}
        <li id="saveLink"><a href="{$url}/Record/{$id|escape:"url"}/Save" class="saveRecord fav" id="saveRecord{$id|escape}" title="{translate text="Add to favorites"}">{translate text="Add to favorites"}</a></li>
        {if !empty($addThis)}
        <li id="addThis"><a class="addThis addthis_button"" href="https://www.addthis.com/bookmark.php?v=250&amp;pub={$addThis|escape:"url"}">{translate text='Bookmark'}</a></li>
        {/if}
      </ul>
      <div class="clear"></div>
    </div>

    <div style="clear: right;"></div>
    {if $errorMsg || $infoMsg}
    <div class="messages">
      {if $errorMsg}<div class="error">{$errorMsg|translate}</div>{/if}
      {if $infoMsg}<div class="userMsg">{$infoMsg|translate}</div>{/if}
    </div>
    {/if}
    <div class="divCoreMetadata recordId collectionID" id="record{$collectionID|escape}">
      {include file=$info}
    </div>
    <div style="clear: left;"></div>
    <span class="Z3988" title="{$openURL|escape}"></span>
  </div>

  <div id="tabnav" >
    <ul>
      <li{if $tab == 'Home' || $tab == '' || $tab == 'list'} class="active"{/if}>
        <a href="{$url}/Collection/{$collectionID|urlencode}/CollectionList{$filters|escape}#tabnav" class="first holdingsView">{translate text='Collection Items'}</a>
      </li>
      {if $hierarchyTreeList}
      <li{if $tab == 'HierarchyTree'} class="active"{/if}>
        <a href="{$url}/Collection/{$collectionID|urlencode}/HierarchyTree{$filters|escape}#tabnav" class="first">{translate text='hierarchy_tree'}</a>
      </li>
      {/if}
    </ul>
    <div style="clear: both;"></div>
  </div>
  <div style="clear: both;"></div>
  <div class="collectionDetails{if $tab == 'HierarchyTree'}Tree{/if}">
    {if $subpage}
      {include file=$subpage}
    {/if}
  </div>
  <div class="clear"></div>
</div>

{if $tab != 'HierarchyTree'}
<div class="span-5 last">
  <div class="sidegroup">
  {* Recommendations *}
    {if $sideRecommendations}
      {foreach from=$sideRecommendations item="recommendations"}
        {include file=$recommendations}
      {/foreach}
    {/if}
  {* End Recommendations *}
  </div>
</div>
{/if}

<div class="clear"></div>