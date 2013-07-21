<div class="span-5 browseNav">
  {include file="Browse/top_list.tpl" currentAction="Rights"}
</div>

<div class="span-5 browseNav">
  <ul class="browse" id="list2">
    <li><a href="{$url}/Browse/Rights" class="loadSubjects query_field:rights facet_field:country target:list3container">{translate text="Por Pais"}</a></li>
    {if $rightsEnabled}<li><a href="{$url}/Browse/Rights" class="loadSubjects query_field:country facet_field:rights target:list3container">{translate text="Por Tipo"}</a></li>{/if}
  </ul>
</div>

<div id="list3container" class="span-5">
</div>

<div id="list4container" class="span-5">
</div>

<div class="clear"></div>
