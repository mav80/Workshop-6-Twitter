<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<link rel="stylesheet" href="<%out.print(request.getContextPath());%>/static/css/style.css">
		
		<script type="text/javascript" src="<%out.print(request.getContextPath());%>/static/js/app.js"></script>
		<script type="text/javascript" src="<%out.print(request.getContextPath());%>/static/js/jquery-3.3.1.min.js"></script>
	
	</head>
	
	<body>
			
		<c:if test="${not empty info}">
			<b>${info}</b>
			<a href="panelUser" style="color: blue;">Panel użytkownika</a>
			<a href="logout">Wyloguj się</a>
		</c:if> 
		
		<c:if test="${empty info}">
				<a href="login">Logowanie</a>
				<a href="register">Rejestracja</a>
		</c:if>
					
		<a href="<%out.print(request.getContextPath());%>">Powrót do strony głównej</a>
	</body>
</html>