<div data-role="content">
  {if $recordCount}
    <p>
      <strong>{$recordStart}</strong> - <strong>{$recordEnd}</strong>
      {translate text='of'} <strong>{$recordCount}</strong>
      {translate text='Items'}
    </p>
    {include file=$searchPage}
  {else}
    {translate text='collection_empty'}
  {/if}
</div>
