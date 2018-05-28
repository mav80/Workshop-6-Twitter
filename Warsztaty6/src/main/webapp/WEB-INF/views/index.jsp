<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Twitter</title>
	</head>
	<body>
		<h1>To jest widok index.jsp - Twitter</h1>
		
		<c:if test="${not empty info}">
					
			<form:form method="post" modelAttribute="tweet">
				Stwórz nową wiadomość:<br>
				<form:textarea rows="4" cols="50" path="text" placeholder="treść tweeta"/><br> 
				<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>	
				<input type="submit" value="wyślij">	
			</form:form>
			
		</c:if> 
		

		
		
		<br><br>Oto wszystkie tweety znajdujące się w bazie: <br><br>
		
		<table>
			<c:forEach items="${tweets}" var="tweet">
				
				<div class="row">
					<tr>
						<td>
							<list>
								<ul>
									<li>Autor tweeta: <b>${tweet.user.username}</b>, data utworzenia: ${tweet.created}</li>
									<li>Treść: <pre>${tweet.text}</pre></li>
									</ul>
							</list>
						</td>
				</div>  <!--  koniec div "row" -->
	
			</c:forEach>
		</table>
	
	</body>
</html>