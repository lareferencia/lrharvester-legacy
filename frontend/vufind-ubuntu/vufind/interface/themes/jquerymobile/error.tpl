<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1"/>
    <title>{$site.title|escape}</title>

    {* Set global javascript variables *}
    <script type="text/javascript">
    //<![CDATA[
      var path = '{$path}';
    //]]>
    </script>

    {css filename="jquery.mobile-1.0rc2.min.css"}
    {js filename="jquery-1.6.4.min.js"}
    {js filename="common.js"}
    {js filename="jquery.mobile-1.0rc2.min.js"}
    {js filename="jquery.cookie.js"}
    {js filename="cart_cookie.js"}
    {js filename="cart.js"}
    {js filename="scripts.js"}
    {css filename="styles.css"}
    {css filename="formats.css"}
  </head>
  <body>
    <div data-role="page" id="error-page" class="error-page">
      <div data-role="content" class="fatalError">
        <h1>{translate text="An error has occurred"}</h1>
        <p class="errorMsg">{$error->getMessage()}</p>
        {if $debug}
          <p class="errorStmt">{$error->getDebugInfo()}</p>
        {/if}
        <p>
          {translate text="Please contact the Library Reference Department for assistance"}
          <br/>
          <a href="mailto:{$supportEmail}">{$supportEmail}</a>
        </p>
        {if $debug}
        <div class="debug">
          <h2>{translate text="Debug Information"}</h2>
          {assign var=errorCode value=$error->getCode()}
          {if $errorCode}
          <p class="errorMsg">{translate text="Code"}: {$errorCode}</p>
          {/if}
          <p>{translate text="Backtrace"}:</p>
          <code>
          {foreach from=$error->backtrace item=trace}
            [{$trace.line}] {$trace.file}<br/>
          {/foreach}
          </code>
        </div>
        {/if}
      </div>
    </div>
  </body>
</html>
