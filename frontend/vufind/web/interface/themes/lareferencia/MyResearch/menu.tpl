  <div class="sidegroup">
    <h4 class="account">{translate text='Your Account'}</h4>
    <ul class="bulleted">
      <li{if $pageTemplate=="favorites.tpl"} class="active"{/if}><a href="{$url}/MyResearch/Favorites">{translate text='Favorites'}</a></li>
      {* Only highlight saved searches as active if user is logged in: *}
      <li{if $user && $pageTemplate=="history.tpl"} class="active"{/if}><a href="{$url}/Search/History?require_login">{translate text='history_saved_searches'}</a></li>
    <li{if $user && $pageTemplate=="comments.tpl"} class="active"{/if}><a href="{$url}/Search/Comments">{translate text='Comentarios Realizados'}</a></li>
	  </ul>
  </div>
