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
	  <li><a href="{$url}">Inicio</a></li>
        <li><a href="#">Pa&iacute;ses</a>
          <ul>
            <li><a href="{$url}/Laref/Mapa">Mapa de la Red</a></li>
            <li><a href="{$url}/Laref/Instituciones">Listado de Instituciones 
              Cosechadas</a></li>
            <li><a href="{$url}/Laref/Cosechas">Estad&iacute;stica de Cosechas</a>
					<ul>
					<li {if !$countries.AR} class="hide"{/if}  ><a href="{$url}/Laref/CosechaAR">Argentina</a></li>
					<li {if !$countries.BR} class="hide"{/if}  ><a href="{$url}/Laref/CosechaBR">Brasil</a></li>
					<li {if !$countries.CL} class="hide"{/if}  ><a href="{$url}/Laref/CosechaCL">Chile</a></li>
					<li {if !$countries.CO} class="hide"{/if}  ><a href="{$url}/Laref/CosechaCO">Colombia</a></li>
					<li {if !$countries.EC} class="hide"{/if}  ><a href="{$url}/Laref/CosechaEC">Ecuador</a></li>
					<li {if !$countries.SV} class="hide"{/if}  ><a href="{$url}/Laref/CosechaSV">El Salvador</a></li>
					<li {if !$countries.MX} class="hide"{/if}  ><a href="{$url}/Laref/CosechaMX">M&eacute;xico</a></li>
					<li {if !$countries.PE} class="hide"{/if}  ><a href="{$url}/Laref/CosechaPE">Per&uacute;</a></li>
					<li {if !$countries.VE} class="hide"{/if}  ><a href="{$url}/Laref/CosechaVE">Venezuela</a></li>
					</ul>	
			</li>
          </ul>
        </li>
        <li><a href="#">Material</a>
          <ul>
            <li><a href="{$url}/Laref/Material">Distribuci&oacute;n del material</a></li>
            <li><a href="{$url}/Laref/Fechas">Distribuci&oacute;n del material cosechado por d&eacute;cada</a></li>
			<li><a href="{$url}/Laref/Licencias">Distribuci&oacute;n de licencias por Pa&iacute;s</a></li>
            <li><a href="{$url}/Laref/Terminos">Nube de T&eacute;rminos </a></li>
          </ul>
        </li>
        <li><a href="#">Impacto</a>
          <ul>
            <li><a href="#">Mapa de accesos </a>
				  <ul>
					<li><a href="{$url}/Laref/Impacto">Pa&iacute;s del visitante</a></li>
					<li><a href="{$url}/Laref/ImpactoRec">Pa&iacute;s del recolector</a></li>
				  </ul>				
			</li>
            <li><a href="{$url}/Laref/PorPais">Por Pa&iacute;s Visitante</a>
				  <ul>
					<li><a href="{$url}/Laref/PorPaisA">Mapa por Art&iacute;culo</a></li>
					<li><a href="{$url}/Laref/PorPaisR">Mapa por Reporte</a></li>
					<li><a href="{$url}/Laref/PorPaisTD">Mapa por Tesis de Doctorado</a></li>
					<li><a href="{$url}/Laref/PorPaisTM">Mapa por Tesis de Maestr&iacute;a</a></li>
				  </ul>				
			</li>
            <li><a href="{$url}/Laref/PorMaterial">Por Tipo de Material </a>
				  <ul>
					<li {if !$countries.AR} class="hide"{/if}  ><a href="{$url}/Laref/PorMaterialAR">Argentina</a></li>
					<li {if !$countries.BR} class="hide"{/if}  ><a href="{$url}/Laref/PorMaterialBR">Brasil</a></li>
					<li {if !$countries.CL} class="hide"{/if}  ><a href="{$url}/Laref/PorMaterialCL">Chile</a></li>
					<li {if !$countries.CO} class="hide"{/if}  ><a href="{$url}/Laref/PorMaterialCO">Colombia</a></li>
					<li {if !$countries.EC} class="hide"{/if}  ><a href="{$url}/Laref/PorMaterialEC">Ecuador</a></li>
					<li {if !$countries.SV} class="hide"{/if}  ><a href="{$url}/Laref/PorMaterialSV">El Salvador</a></li>
					<li {if !$countries.MX} class="hide"{/if}  ><a href="{$url}/Laref/PorMaterialMX">M&eacute;xico</a></li>
					<li {if !$countries.PE} class="hide"{/if}  ><a href="{$url}/Laref/PorMaterialPE">Per&uacute;</a></li>
					<li {if !$countries.VE} class="hide"{/if}  ><a href="{$url}/Laref/PorMaterialVE">Venezuela</a></li>
					</ul>						
			</li>
            <li><a href="{$url}/Laref/Busquedas">Ranking de b&uacute;squedas</a></li>
          </ul>
        </li>
        <li><a href="#">{translate text='Browse'}</a>
          <ul>
            <li><a href="{$path}/Browse/Home">{translate text='Browse the Catalog'}</a></li>
          </ul>
        </li>
        <li>
		<a href="#">B&uacute;squeda</a>
          <ul>
            <li><a href="{$path}/Search/Advanced">{translate text='Advanced Search'}</a></li>
             <li><a href="{$path}/Search/History">{translate text='Search History'}</a></li>
              <li><a href="{$url}/Help/Home?topic=search">{translate text='Search Tips'}</a></li>
          </ul>
        </li>
		<li>
		<a href="#">Usuarios</a>
			<ul>
				<li {if !$user} class="hide"{/if} >
					<a  href="{$path}/MyResearch/Home"> {translate text="Your Account"}</a>
						<ul>
							<li {if !$user } class="hide"{/if}  ><a href="{$url}/MyResearch/Favorites">Sus Favoritos</a></li>
							<li {if !$user } class="hide"{/if}><a  href="{$url}/Search/History?require_login">Sus B&uacute;squedas</a></li>	
							<li {if !$user } class="hide"{/if}><a  href="{$url}/Search/Comments">Sus Comentarios</a>	</li>
						</ul>
				</li>
				<li>
					<a {if !$user} class="hide"{/if} href="{$path}/MyResearch/Logout"> {translate text="Log Out"}</a>
				</li>
				<li {if $user} class="hide"{/if}>
					{if $authMethod == 'Shibboleth'}
					<a   href="{$sessionInitiator}">{translate text="Institutional Login"}</a>
					{else}
					<a  href="{$path}/MyResearch/Home"> Identificarse </a>
					{/if}
				</li>
				<li>
					{if $authMethod == 'Shibboleth'}
					<a {if $user} class="hide"{/if} href="{$sessionInitiator}">{translate text="Institutional Login"}</a>
					{else}
					<a {if $user} class="hide"{/if} href="{$path}/MyResearch/Account">Registro como usuario</a>
					{/if}
				</li>
				<li>
					<a {if $user} class="hide"{/if} href="{$path}/MyResearch/AccountAdmin">Registro como administrador</a>
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
				
				<li><a  {if (!$user || ($user->admin_country==""))} class="hide"{/if} href="{$path}/MyResearch/Home"> {translate text="ADMINISTRADOR"} {$user->admin_country}</a>
					<ul>
					        <li><a {if (!$user || ($user->admin_country==""))} class="hide"{/if} href="{$url}/Laref/ImpactoRec">Impacto por Pa&iacute;s del recolector</a></li>
						<li><a {if (!$user || ($user->admin_country==""))} class="hide"{/if} href="{$url}/Laref/Cosechas{$user->admin_country}">Registro Avanzado de Cosechas {$user->admin_country}</a></li>
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
