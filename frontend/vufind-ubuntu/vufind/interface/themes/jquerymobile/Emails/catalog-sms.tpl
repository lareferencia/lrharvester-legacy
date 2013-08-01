{* This is a text-only email template; do not include HTML! *}
{if !empty($callnumber)}{translate text="callnumber_abbrev"}: {$callnumber}
{/if}
{if !empty($location)}{translate text="Location"}: {$location}
{/if}
{$title}
{$url}/Record/{$recordID|escape:"url"}