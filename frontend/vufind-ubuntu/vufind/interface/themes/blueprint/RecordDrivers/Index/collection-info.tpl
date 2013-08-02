{if $collThumbMedium}
  <div class="floatright">
    {if $collThumbLarge}<a href="{$collThumbLarge|escape}">{/if}
      <img id="fullRecBookCover" style="display: block;" src="{$collThumbMedium|escape}" alt="{translate text='Cover Image'}" />
    {if $collThumbLarge}</a>{/if}
  </div>
{/if}
<h1>{$collShortTitle|escape}</h1>
<p>{$collSummary|escape}</p>
<a id="moreInfoToggle" href="#" style="display:none">{translate text="more_info_toggle"}</a>
<div id="collectionInfo" class="collectionInfo">
<table cellpadding="2" cellspacing="0" border="0" class="citation" summary="{translate text='Bibliographic Details'}">
  {if !empty($collMainAuthor)}
  <tr valign="top">
    <th>{translate text='Main Author'}: </th>
    <td><a href="{$url}/Author/Home?author={$collMainAuthor|escape:"url"}">{$collMainAuthor|escape}</a></td>
  </tr>
  {/if}

  {if !empty($collCorporateAuthor)}
  <tr valign="top">
    <th>{translate text='Corporate Author'}: </th>
    <td><a href="{$url}/Author/Home?author={$collCorporateAuthor|escape:"url"}">{$collCorporateAuthor|escape}</a></td>
  </tr>
  {/if}

  {if !empty($collContributors)}
  <tr valign="top">
    <th>{translate text='Other Authors'}: </th>
    <td>
      {foreach from=$collContributors item=field name=loop}
        <a href="{$url}/Author/Home?author={$field|escape:"url"}">{$field|escape}</a>{if !$smarty.foreach.loop.last}, {/if}
      {/foreach}
    </td>
  </tr>
  {/if}

  {if !empty($collSummaryAll)}
  <tr valign="top">
    <th>{translate text='Summary'}: </th>
    <td>
      {foreach from=$collSummaryAll key=sumKey item=field name=loop}
        {$field|escape}<br />{if !$smarty.foreach.loop.last}<br />{/if}
      {/foreach}
    </td>
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

  {if (!empty($recordLanguage)) || (!empty($collLanguageNotes))}
  <tr valign="top">
    <th>{translate text='Language'}: </th>
    <td>{foreach from=$recordLanguage item=lang name=loop}{$lang|escape}{if !$smarty.foreach.loop.last},&nbsp;{/if}{/foreach}
        {if (!empty($recordLanguage)) && (!empty($collLanguageNotes))}<br />Note:{/if}
        {foreach from=$collLanguageNotes item=field name=loop}
            {$field|escape}<br />{if !$smarty.foreach.loop.last}<br />{/if}
        {/foreach}
    </td>
  </tr>
  {/if}

  {if !empty($collPublications)}
  <tr valign="top">
    <th>{translate text='Published'}: </th>
    <td>
      {foreach from=$collPublications item=field name=loop}
        {$field|escape}<br />
      {/foreach}
    </td>
  </tr>
  {/if}

  {if !empty($collEdition)}
  <tr valign="top">
    <th>{translate text='Edition'}: </th>
    <td>
      {$collEdition|escape}
    </td>
  </tr>
  {/if}

  {* Display series section if at least one series exists. *}
  {if !empty($collSeries)}
  <tr valign="top">
    <th>{translate text='Series'}: </th>
    <td>
      {foreach from=$collSeries item=field name=loop}
        {* Depending on the record driver, $field may either be an array with
           "name" and "number" keys or a flat string containing only the series
           name.  We should account for both cases to maximize compatibility. *}
        {if is_array($field)}
          {if !empty($field.name)}
            <a href="{$url}/Search/Results?lookfor=%22{$field.name|escape:"url"}%22&amp;type=Series">{$field.name|escape}</a>
            {if !empty($field.number)}
              {$field.number|escape}
            {/if}
            <br />
          {/if}
        {else}
          <a href="{$url}/Search/Results?lookfor=%22{$field|escape:"url"}%22&amp;type=Series">{$field|escape}</a><br />
        {/if}
      {/foreach}
    </td>
  </tr>
  {/if}

  {if !empty($collSubjects)}
  <tr valign="top">
    <th>{translate text='Subjects'}: </th>
    <td>
      {foreach from=$collSubjects item=field name=loop}
        {assign var=subject value=""}
        {foreach from=$field item=subfield name=subloop}
          {if !$smarty.foreach.subloop.first} &gt; {/if}
          {assign var=subject value="$subject $subfield"}
          <a id="subjectLink_{$smarty.foreach.loop.index}_{$smarty.foreach.subloop.index}"
            href="{$url}/Search/Results?lookfor=%22{$subject|escape:"url"}%22&amp;type=Subject"
          onmouseover="subjectHighlightOn({$smarty.foreach.loop.index}, {$smarty.foreach.subloop.index});"
          onmouseout="subjectHighlightOff({$smarty.foreach.loop.index}, {$smarty.foreach.subloop.index});">{$subfield|escape}</a>
        {/foreach}
        <br />
      {/foreach}
    </td>
  </tr>
  {/if}

  {if !empty($collURLs) || $collOpenURL}
  <tr valign="top">
    <th>{translate text='Online Access'}: </th>
    <td>
      {foreach from=$collURLs item=desc key=currentUrl name=loop}
        <a href="{if $proxy}{$proxy}/login?qurl={$currentUrl|escape:"url"}{else}{$currentUrl|escape}{/if}">{$desc|escape}</a><br/>
      {/foreach}
      {if $collOpenURL}
        {include file="Search/openurl.tpl" openUrl=$collOpenURL}<br/>
      {/if}
    </td>
  </tr>
  {/if}

  {if !empty($collNotes)}
  <tr valign="top">
    <th>{translate text='Notes'}: </th>
    <td>
      {foreach from=$collNotes item=field name=loop}
        {$field|escape}<br/>
      {/foreach}
    </td>
  </tr>
  {/if}

  {if !empty($collCredits)}
  <tr valign="top">
    <th>{translate text='Production Credits'}: </th>
    <td>
      {foreach from=$collCredits item=field name=loop}
        {$field|escape}<br />
      {/foreach}
    </td>
  </tr>
  {/if}

  {if !empty($collISBNs)}
  <tr valign="top">
    <th>{translate text='ISBN'}: </th>
    <td>
      {foreach from=$collISBNs item=field name=loop}
        {$field|escape}<br />
      {/foreach}
    </td>
  </tr>
  {/if}

  {if !empty($collISSNs)}
  <tr valign="top">
    <th>{translate text='ISSN'}: </th>
    <td>
      {foreach from=$collISSNs item=field name=loop}
        {$field|escape}<br />
      {/foreach}
    </td>
  </tr>
  {/if}
</table>
</div>
