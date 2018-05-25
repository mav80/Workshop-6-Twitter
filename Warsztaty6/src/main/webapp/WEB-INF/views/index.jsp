<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Twitter</title>
	</head>
	<body>
		<h1>To jest widok index.jsp - Twitter</h1>
		
		${info}<br>
		
		<a href="login">Logowanie</a> <a href="register">Rejestracja</a> <a href="logout">Wylogowanie</a>
		
		<br><br>
		
		Oto wszystkie tweety znajdujące się w bazie: <br><br>
		
		<table>
			<c:forEach items="${tweets}" var="tweet">
				
				<div class="row">
					<tr>
						<td>
							<list>
								<ul>
									<li>Autor tweeta: ${tweet.user.username}</li>
									<li>Data utworzenia: ${tweet.created}</li>
									<li>Treść: ${tweet.text}</li>
									</ul>
							</list>
						</td>
				</div>  <!--  koniec div "row" -->
	
			</c:forEach>
		</table>
	
	</body>
</html>