<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Strona logowania</title>
	</head>
	<body>

		<br><br>
		
		<form method="post" action="<%out.print(request.getContextPath());%>/login">
		
			Login: 
			<input type="text" name="username" placeholder="login"><br><br>
			
			Hasło: 
			<input type="password" name="password" placeholder="password"><br><br>
			
			<input type="submit" value="zaloguj">
	
		</form>
		
				
		<br>
		<b>${infoError}</b><br>
		<br>
	
	</body>
</html>