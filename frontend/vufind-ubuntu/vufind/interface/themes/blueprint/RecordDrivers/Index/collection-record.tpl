{* Display Title *}
  <h1>{$collRecordShortTitle|escape}
  {if $collRecordSubtitle}{$coreSubtitle|escape}{/if}
  {if $collRecordTitleSection}{$coreTitleSection|escape}{/if}
  {* {if $collRecordTitleStatement}{$coreTitleStatement|escape}{/if} *}
  </h1>
{* End Title *}
{*link to the full page, either collection page or record page*}
{if $collCollection}
  <a href="{$url}/Collection/{$collRecordID|escape:"url"}/HierarchyTree#tabnav" class="title">{translate text="View Full Collection"}</a>
{else}
  <a href="{$url}/Record/{$collRecordID|escape:"url"}/HierarchyTree#tabnav" class="title">{translate text="View Full Record"}</a>
{/if}

<table cellpadding="2" cellspacing="0" border="0" class="citation" summary="{translate text='Bibliographic Details'}">
  {if !empty($collRecordSummary)}
  <tr valign="top">
    <th>{translate text='Description'}: </th>
    <td>{$collRecordSummary|escape}</td>
  </tr>
  {/if}

  {if !empty($collRecordMainAuthor)}
  <tr valign="top">
    <th>{translate text='Main Author'}: </th>
    <td><a href="{$url}/Author/Home?author={$collRecordMainAuthor|escape:"url"}">{$collRecordMainAuthor|escape}</a></td>
  </tr>
  {/if}

  {if !empty($collRecordCorporateAuthor)}
  <tr valign="top">
    <th>{translate text='Corporate Author'}: </th>
    <td><a href="{$url}/Author/Home?author={$collRecordCorporateAuthor|escape:"url"}">{$collRecordCorporateAuthor|escape}</a></td>
  </tr>
  {/if}

  {if (!empty($collRecordLanguage))}
  <tr valign="top">
    <th>{translate text='Language'}: </th>
    <td>{foreach from=$collRecordLanguage item=lang name=loop}{$lang|escape}{if !$smarty.foreach.loop.last},&nbsp;{/if}{/foreach}</td>
  </tr>
  {/if}

  <tr valign="top">
    <th>{translate text='Format'}: </th>
    <td>
     {if is_array($recordFormat)}
      {foreach from=$recordFormat item=displayFormat name=loop}
        <span class="iconlabel {$displayFormat|lower|regex_replace:"/[^a-z0-9]/":""}">{translate text=$displayFormat}</span>
      {/foreach}
    {else}
      <span class="iconlabel {$recordFormat|lower|regex_replace:"/[^a-z0-9]/":""}">{translate text=$recordFormat}</span>
    {/if}
    </td>
  </tr>

  {if !empty($collRecordAccess)}
  <tr valign="top">
    <th>{translate text='Access'}: </th>
    <td>
      {foreach from=$collRecordAccess item=field name=loop}
        {$field|escape}<br />
      {/foreach}
    </td>
  </tr>
  {/if}

  {if !empty($collRecordRelated)}
  <tr valign="top">
    <th>{translate text='Related Items'}: </th>
    <td>
      {foreach from=$collRecordRelated item=field name=loop}
        {$field|escape}<br />
      {/foreach}
    </td>
  </tr>
  {/if}
</table>