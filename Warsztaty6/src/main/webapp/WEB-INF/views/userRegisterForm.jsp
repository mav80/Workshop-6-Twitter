<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>userForm</title>
	</head>
	<body>
		<h1>To jest widok userRegisterForm.jsp</h1><br><br>
		${error}<br>
	
		<form:form method="post" modelAttribute="user">
	
			Podaj login: <form:input path="username" placeholder="username"/><br>
			<form:errors path="username"/><br>
			
			Podaj hasło: <form:password path="password" placeholder="password"/><br>
			<form:errors path="password"/><br>
			
			Podaj email: <form:input path="email" placeholder="email"/><br>
			<form:errors path="email"/><br>
			
			<input type="submit" value="zarejestruj">
	
		</form:form>
	
	</body>
	<%@ include file="fragments/footer.jsp"%>
</html>