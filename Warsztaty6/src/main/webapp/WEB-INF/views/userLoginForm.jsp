<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>login form</title>
	</head>
	<body>
		<h3>userLoginForm.jsp</h3>
		
		${info}
		<form method="post">
		
			Login<br> 
			<input type="text" name="username" placeholder="login"><br>
			
			Hasło<br>
			<input type="password" name="password" placeholder="password"><br>
			
			<input type="submit" value="Zaloguj">
	
		</form>
	
	</body>
</html>