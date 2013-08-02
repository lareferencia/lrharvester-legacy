<a href="{$url}/Collections/Home">{translate text="Collections"}</a> <span>&gt;</span>
{if $breadcrumbText}
<em>{$breadcrumbText|truncate:30:"..."|escape}</em> <span>&gt;</span>
{/if}
{if $subpage!=""}
<em>{$subpage|replace:'hierarchyTree_JSTree':'Context'|replace:'view-':''|replace:'.tpl':''|replace:'Collection/':''|capitalize|translate}</em>
{/if}
