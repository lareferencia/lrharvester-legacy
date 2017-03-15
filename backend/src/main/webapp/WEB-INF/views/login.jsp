<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Autenticación Back End</title>
    <link rel="stylesheet" href="<spring:url value="/static/css/login.css"/>"></link>
    
</head>
<body>
  
 
    <!-- TOP BAR -->
	<div id="top-bar">
		
		<div class="page-full-width">
		
			<!--  a href="#" class="round button dark ic-left-arrow image-left ">Return to website</a-->

		</div> <!-- end full-width -->	
	
	</div> <!-- end top-bar -->
	
	
	
	<!-- HEADER -->
	<div id="header">
		
		<div class="page-full-width cf">
	
			<div id="login-intro" class="fl">
			
				<h1>Acceso a administración</h1>
				<h5>Ingrese sus credenciales a continuación</h5>
			
			</div> <!-- login-intro -->
			
			<!-- Change this image to your own company's logo -->
			<!-- The logo will automatically be resized to 39px height. -->
			<a href="#" id="company-branding" class="fr"><img src="<spring:url value="/static/css/img/logo.png"/>" alt="Blue Hosting" /></a>
			
		</div> <!-- end full-width -->	

	</div> <!-- end header -->
	
	
	
	<!-- MAIN CONTENT -->
	<div id="content">
	
	        <form action="<%=request.getContextPath()%>/j_spring_security_check" method="post" id="login-form">
		
		
			<fieldset>

				<p>
					<label for="login-username">username</label>
					<input name="j_username" type="text" id="login-username" class="round full-width-input" autofocus />
				</p>

				<p>
					<label for="login-password">password</label>
					<input name="j_password" type="password" id="login-password" class="round full-width-input" />
				</p>
				
				<input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
				
				
				<input  class="button round blue image-right ic-right-arrow" type="submit" value="Log in"/>    
				

			</fieldset>

			<!--  br/><div class="information-box round">Just click on the "LOG IN" button to continue, no login information required.</div-->
		<c:if test="${loginFailed}">
			<br/><div class="error-box round">Usuario o contraseña incorrectos</div>
  		</c:if>


		</form>
		
	</div> <!-- end content -->
	
	
	
	<!-- FOOTER -->
	<div id="footer">

		<p>&copy; Copyright 2013 <a href="http://lareferencia.redclara.net/">LAReferencia</p>
		
	</div> <!-- end footer -->
</body>
</html>