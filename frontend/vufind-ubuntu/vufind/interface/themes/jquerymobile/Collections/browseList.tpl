<ul class="ui-listview" data-role="listview">
  {foreach from=$result item=item name=recordLoop}
    <li class="ui-li-has-count">
      <a class="ui-link-inherit" data-ajax="false" href="{$path}/Collection/{$item[2]|urlencode}">
        <div class="ui-btn-text">{$item[0]|escape:"html"}</div>
        {* subtract one from the number of items to exclude the record representing the collection itself. *}
        <span class="ui-li-count ui-btn-up-c ui-btn-corner-all"><b>{math equation="a - 1" a=$item[1]}</b> {translate text="items"}</span>
        <span class="ui-icon ui-icon-arrow-r ui-icon-shadow"></span>
      </a>
    </li>
  {/foreach}
</ul>