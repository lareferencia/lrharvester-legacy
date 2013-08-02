<div data-role="page" id="Record-view">
  {include file="header.tpl"}
  <div class="record" data-role="content" data-record-id="{$id}">
    {include file=$info}
    {if $subpage}
      {include file=$subpage}
    {/if}
  </div>
  {include file="footer.tpl"}
</div>
