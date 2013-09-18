{js filename="jquery.cookie.js"}
{if $bookBag}
  {js filename="cart.js"}
  {assign var=bookBagItems value=$bookBag->getItems()}
{/if}
<a id="logo" href="{$url}"></a>
{literal}
<style type="text/css">

nav {
	width: 100%;
	height:32px;
	float: left;
	margin: 0px;
	padding: 0px;
	background-color: #ddd;
	border-bottom: 1px solid #999;
	position: relative;
	z-index: 2;
}

nav ul ul {
	display: none;
}

nav ul li:hover > ul {
display: block;
}


nav ul {
	background: #ddd; 
	list-style: none;
	position: relative;
	display: inline-table;
    padding: 0px;
}
nav ul:after {
content: ""; clear: both; display: block;
}

nav ul li {
float: left;
}
nav ul li:hover {
	background: #999;
}
nav ul li:hover a {
	color: #fff;
}

nav ul li a {
	display: block; 
	padding: 7px 12px;
	 text-decoration: none; 		
	 font-weight: bold;
	color: #069;
}
	
nav ul ul {
background: #5f6975;  padding: 0;
position: absolute; top: 100%;
}

nav ul ul li {
	width:150px;
	float: none; 
	border-top: 1px solid #6b727c;
	border-bottom: 1px solid #575f6a; 
	position: relative;
}
nav ul ul li a {
		padding: 7px 12px;
		color: #fff;
		
}	
nav ul ul li a:hover {
			background: #4b545f;
		
}

nav ul ul ul {
position: absolute; left: 100%; top:0;
}

</style>	
{/literal}	
<nav>
  <ul>
	  <li><a href="{$url}">{translate text='Home'}</a></li>
        <li><a href="#">{translate text='Countries'}</a>
          <ul>
            <li><a href="{$url}/Laref/Mapa">{translate text='Country Map'}</a></li>
            <li><a href="{$url}/Laref/Instituciones">{translate text='List of Harvested Institutions'} </a></li>
            <li><a href="{$url}/Laref/Cosechas">{translate text='Harvesting Statistics'}</a>
					<ul>
					{foreach from=$networks key=key item=term}
					    {foreach from=$term key=key2 item=i} 
							{if $key2 == 'name' }
							 <li ><a href="{$url}/Laref/Cosecha?iso={$key}">{translate text=$i}</a></li>
							{/if}	
						{/foreach}
					{/foreach}
					</ul>	
			</li>
          </ul>
        </li>
        <li><a href="#">{translate text='Material'}</a>
          <ul>
            <li><a href="{$url}/Laref/Material">{translate text='Material Distribution'}</a></li>
            <li><a href="{$url}/Laref/Fechas">{translate text='Material Distribution by date'}</a></li>
			<li><a href="{$url}/Laref/Licencias">{translate text='Licence Distribution by country'}</a></li>
            <li><a href="{$url}/Laref/Terminos">{translate text='Cloud Tag'}</a></li>
          </ul>
        </li>
        <li><a href="#">{translate text='Impact'}</a>
          <ul>
            <li><a href="#">{translate text='Access Maps'}</a>
				  <ul>
					<li><a href="{$url}/Laref/Impacto">{translate text='Visitors Map'}</a></li>
					<li><a href="{$url}/Laref/ImpactoRec">{translate text='Harvesters Map'}</a></li>
				  </ul>				
			</li>
            <li><a href="{$url}/Laref/PorPais">{translate text='Visitors Country'}</a>
				  <ul>
					<li><a href="{$url}/Laref/PorPaisA">{translate text='Map by Article'}</a></li>
					<li><a href="{$url}/Laref/PorPaisR">{translate text='Map by Report'}</a></li>
					<li><a href="{$url}/Laref/PorPaisTD">{translate text='Map by Doctoral Thesis'}</a></li>
					<li><a href="{$url}/Laref/PorPaisTM">{translate text='Map by Master Thesis'}</a></li>
				  </ul>				
			</li>
            <li><a href="{$url}/Laref/PorMaterial">{translate text='By Material'}</a>
					<ul>
					{foreach from=$networks key=key item=term}
					    {foreach from=$term key=key2 item=i} 
							{if $key2 == 'name' }
							 <li ><a href="{$url}/Laref/PorMaterialp?iso={$key}">{translate text=$i}</a></li>
							{/if}	
						{/foreach}
					{/foreach}
					</ul>						
			</li>
            <li><a href="{$url}/Laref/Busquedas">{translate text='Search Ranking'}</a></li>
          </ul>
        </li>
        <li><a href="#">{translate text='Browse'}</a>
          <ul>
            <li><a href="{$path}/Browse/Home">{translate text='Browse the Catalog'}</a></li>
          </ul>
        </li>
        <li>
		<a href="#">{translate text='Search'}</a>
          <ul>
            <li><a href="{$path}/Search/Advanced">{translate text='Advanced Search'}</a></li>
             <li><a href="{$path}/Search/History">{translate text='Search History'}</a></li>
              <li><a href="{$url}/Help/Home?topic=search">{translate text='Search Tips'}</a></li>
          </ul>
        </li>
		<li>
		<a href="#">{translate text='User'}</a>
			<ul>
				<li {if !$user} class="hide"{/if} >
					<a  href="{$path}/MyResearch/Home"> {translate text="Your Account"}</a>
						<ul>
							<li {if !$user } class="hide"{/if}  ><a href="{$url}/MyResearch/Favorites">{translate text='Your Favorites'}</a></li>
							<li {if !$user } class="hide"{/if}><a  href="{$url}/Search/History?require_login">{translate text='Your search terms'}</a></li>	
							<li {if !$user } class="hide"{/if}><a  href="{$url}/Search/Comments">{translate text='Your Comment'}</a>	</li>
						</ul>
				</li>
				<li>
					<a {if !$user} class="hide"{/if} href="{$path}/MyResearch/Logout"> {translate text="Log Out"}</a>
				</li>
				<li {if $user} class="hide"{/if}>
					{if $authMethod == 'Shibboleth'}
					<a   href="{$sessionInitiator}">{translate text="Institutional Login"}</a>
					{else}
					<a  href="{$path}/MyResearch/Home">{translate text='Login'}</a>
					{/if}
				</li>
				<li>
					{if $authMethod == 'Shibboleth'}
					<a {if $user} class="hide"{/if} href="{$sessionInitiator}">{translate text="Institutional Login"}</a>
					{else}
					<a {if $user} class="hide"{/if} href="{$path}/MyResearch/Account">{translate text='Create New User Account'}</a>
					{/if}
				</li>
				<li>
					<a {if $user} class="hide"{/if} href="{$path}/MyResearch/AccountAdmin">{translate text='Create New Adminstrator Account'}</a>
				</li>
			</ul>
		</li>
		<li>
		 {if is_array($allLangs) && count($allLangs) > 1}
		  <form method="post" name="langForm" action="" id="langForm">
			<label for="langForm_mylang"> {translate text="Language"}: </label>
			<select id="langForm_mylang" name="mylang" class="jumpMenu"  style="width: 100px">
			  {foreach from=$allLangs key=langCode item=langName}
				<option value="{$langCode}"{if $userLang == $langCode} selected="selected"{/if}>{translate text=$langName}</option>
			  {/foreach}
			</select>
			<noscript><input type="submit" value="{translate text="Set"}" /></noscript>
		  </form>
		  {/if}
		</li>
		<li><a href="{$url}/Laref/About">{translate text="About"} </a></li>
		<li>
				
				<li><a  {if (!$user || ($user->admin_country==""))} class="hide"{/if} href="{$path}/MyResearch/Home"> {translate text="ADMINISTRATOR"} {$user->admin_country}</a>
					<ul>
						<li><a {if (!$user || ($user->admin_country==""))} class="hide"{/if} href="{$url}/Laref/Cosechaad?iso={$user->admin_country}">{translate text='Advanced Harvesting Statistics'} {$user->admin_country}</a></li>
					<li><a {if (!$user || ($user->admin_country==""))} class="hide"{/if} href="{$url}/Laref/ImpactoRec">{translate text='Harvesters Map'}</a></li>
					</ul>
				</li>
			
		</li>
  </ul>
</nav>
<div id="headerRight">
  {if $bookBag}
  <div id="cartSummary" class="cartSummary">
      <a id="cartItems" title="{translate text='View Book Bag'}" class="bookbag" href="{$url}/Cart/Home"><strong><span>{$bookBagItems|@count}</span></strong> {translate text='items'} {if $bookBag->isFull()}({translate text='bookbag_full'}){/if}</a>
      <a id="viewCart" title="{translate text='View Book Bag'}" class="viewCart bookbag offscreen" href="{$url}/Cart/Home"><strong><span id="cartSize">{$bookBagItems|@count}</span></strong> {translate text='items'}<span id="cartStatus">{if $bookBag->isFull()}({translate text='bookbag_full'}){else}&nbsp;{/if}</span></a>
  </div>
  {/if}

</div>

<div class="clear"></div>
