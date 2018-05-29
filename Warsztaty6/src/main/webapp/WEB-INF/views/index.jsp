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
		<h1>Twitter</h1>
		
		<c:if test="${not empty info}">
					
			<form:form method="post" modelAttribute="tweet">
				Stwórz nową wiadomość:<br>
				<form:textarea rows="4" cols="50" path="text" placeholder="treść tweeta"/><br> 
				<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>	
				<input type="submit" value="wyślij">	
			</form:form>
			
		</c:if> 
		

		<br>Oto wszystkie tweety znajdujące się w bazie (razem: ${tweetCount}): <br><br>
		
		<table>
			<c:forEach items="${tweets}" var="tweet">
				
				<div class="row">
					<table>
						<tr>
							<td>
								<list>
									<ul>
										<li><a href="tweet/${tweet.id}">Tweet użytkownika <b>${tweet.user.username}</b>, data utworzenia: ${tweet.created}</a></li>
										<li>
											<pre class="preTweet">${tweet.text}</pre>
										</li>
										<li>Liczba komentarzy:
										
											<c:forEach items="${commentCountMap}" var="mapEntry">
												<c:if test="${mapEntry.key == tweet.id}">
													${mapEntry.value}
												</c:if>
											</c:forEach>
										
										</li>
									</ul>
								</list>					
							</td>
						</tr>
					</table>
				</div>  <!--  koniec div "row" -->
				
				
		<c:if test="${not empty comments}">
	
			
			<c:forEach items="${comments}" var="comment">
							
			
				<c:if test="${comment.tweet.id == tweet.id}">

				
					<div class="row" style="margin-left: 5em;">
						<tr>
							<td>
								<list>
									<ul>
										<li>Komentarz użytkownika <b>${comment.user.username}</b>, data utworzenia: ${comment.created}s 
											<pre class="preComment">${comment.text}</pre>
										</li>
										
									</ul>
								</list>
							</td>
						</tr>
					</div>  <!--  koniec div "row" -->
				
				
				</c:if>
					
				
		
			</c:forEach>
			
		</c:if>  
	
			</c:forEach>
		</table>
		
		
	
	</body>
</html>




