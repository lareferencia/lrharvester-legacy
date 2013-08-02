{* Display Title *}
  <h3>{$collRecordShortTitle|escape}
  {if $collRecordSubtitle}{$coreSubtitle|escape}{/if}
  {if $collRecordTitleSection}{$coreTitleSection|escape}{/if}
  </h3>
{* End Title *}
{*link to the full page, either collection page or record page*}
<p>
{if $collCollection}
  <a rel="external" href="{$url}/Collection/{$collRecordID|escape:"url"}/HierarchyTree#tabnav" class="title">{translate text="View Full Collection"}</a>
{else}
  <a rel="external" href="{$url}/Record/{$collRecordID|escape:"url"}/HierarchyTree#tabnav" class="title">{translate text="View Full Record"}</a>
{/if}
</p>

<dl class="biblio" title="{translate text='Bibliographic Details'}">
  {if !empty($collRecordSummary)}
    <dt>{translate text='Description'}: </dt>
    <dd><p>{$collRecordSummary|escape}</p></dd>
  {/if}

  {if !empty($collRecordMainAuthor)}
    <dt>{translate text='Main Author'}: </dt>
    <dd><p><a href="{$url}/Author/Home?author={$collRecordMainAuthor|escape:"url"}">{$collRecordMainAuthor|escape}</a></p></dd>
  {/if}

  {if !empty($collRecordCorporateAuthor)}
    <dt>{translate text='Corporate Author'}: </dt>
    <dd><p><a href="{$url}/Author/Home?author={$collRecordCorporateAuthor|escape:"url"}">{$collRecordCorporateAuthor|escape}</a></p></dd>
  {/if}

  {if (!empty($collRecordLanguage))}
    <dt>{translate text='Language'}: </dt>
    <dd><p>{foreach from=$collRecordLanguage item=lang name=loop}{$lang|escape}{if !$smarty.foreach.loop.last},&nbsp;{/if}{/foreach}</p></dd>
  {/if}

  <dt>{translate text='Format'}:</dt>
  <dd>
    {if is_array($recordFormat)}
      {foreach from=$recordFormat item=displayFormat name=loop}
        <span class="iconlabel {$displayFormat|lower|regex_replace:"/[^a-z0-9]/":""}">{translate text=$displayFormat}</span>
      {/foreach}
    {else}
      <span class="iconlabel {$recordFormat|lower|regex_replace:"/[^a-z0-9]/":""}">{translate text=$recordFormat}</span>
    {/if}
  </dd>

  {if !empty($collRecordAccess)}
    <dt>{translate text='Access'}: </dt>
    <dd>
      {foreach from=$collRecordAccess item=field name=loop}
        <p>{$field|escape}</p>
      {/foreach}
    </dd>
  {/if}

  {if !empty($collRecordRelated)}
    <dt>{translate text='Related Items'}: </dt>
    <dd>
      {foreach from=$collRecordRelated item=field name=loop}
        <p>{$field|escape}</p>
      {/foreach}
    </dd>
  {/if}
</dl>