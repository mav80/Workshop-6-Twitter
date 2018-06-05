<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Wiadomość od użytkownika ${message.sender.username}</title>
	</head>
	<body>
	
	<h3>Oto szczegóły otrzymanej wiadomości:</h3>
	
		<table class="messagesSingle">
			<tr>
				<th>nadawca: ${message.sender.username}</th>
				<th>tytuł: ${message.topic}</th>
				<th>data utworzenia: ${message.created}</th>
			</tr>
		</table>
		
		<table>

			<tr>
				<td>${message.text}</td>
			</tr>
		</table>
	
	</body>
</html>