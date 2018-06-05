<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Moje wiadomości</title>
	</head>
	<body>
	
		<h3>Oto wszystkie wiadomości które otrzymałeś:</h3>
		<table class="messagesList">
			<tr>
				<th>nadawca</th>
				<th>tytuł</th>
				<th>data utworzenia</th>
				<th>przeczytana?</th>
			</tr>
			
			<c:forEach items="${messages}" var="message">
				<tr>		
					<td>${message.sender.username}</td>
					<td><a href="<%out.print(request.getContextPath());%>/message/${message.id}">
						
						<!-- if the message is unread, we make the title bold -->		
						<c:if test="${message.viewed == false}">
							<b>
						</c:if>
					
						${message.topic}</a>
						
						<c:if test="${message.viewed == false}">
							</b>
						</c:if>
						
						
					</td>
					<td>${message.created}</td>	
					<td>
					
						<c:if test="${message.viewed == false}">
							nie
						</c:if>
						
						<c:if test="${message.viewed == true}">
							tak
						</c:if>
	
					</td>				
				</tr>
			</c:forEach>
		</table>
		
		<h3>Oto wszystkie wiadomości które wysłałeś:</h3>
		
		<table class="messagesList">
			<tr>
				<th>odbiorca</th>
				<th>tytuł</th>
				<th>data utworzenia</th>
				<th>przeczytana?</th>
			</tr>
			
			<c:forEach items="${messagesSent}" var="message">
				<tr>		
					<td>${message.receiver.username}</td>
					<td><a href="<%out.print(request.getContextPath());%>/message/${message.id}">
						
						<!-- if the message is unread, we make the title bold -->		
						<c:if test="${message.viewed == false}">
							<b>
						</c:if>
					
						${message.topic}</a>
						
						<c:if test="${message.viewed == false}">
							</b>
						</c:if>
						
						
					</td>
					<td>${message.created}</td>	
					<td>
					
						<c:if test="${message.viewed == false}">
							nie
						</c:if>
						
						<c:if test="${message.viewed == true}">
							tak
						</c:if>
	
					</td>				
				</tr>
			</c:forEach>
		</table>
	</body>
</html>