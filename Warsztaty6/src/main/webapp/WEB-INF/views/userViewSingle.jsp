<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Widok użytkownika ${viewedUser.username}</title>
	</head>
		<body>
			<c:if test="${not empty messageSentStatus}">
				<h3 style="color: red;">${messageSentStatus}</h3>
			</c:if> 
			
			<c:if test="${empty viewedUser}">
				<h3>Taki użytkownik nie istnieje.</h3>
			</c:if>
		
			<c:if test="${not empty viewedUser}">
			
				<c:if test="${not empty viewedUser.usrImg}">
					<br><br><img class="profilePicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${viewedUser.id}"/> 							
				</c:if>
				
				<br><br>
				<c:if test="${not empty info}">
					<form:form method="post" modelAttribute="message">
						<span>Napisz wiadomość do <b>${viewedUser.username}.</b></span> <span class="messageCharCounter">Pozostało 2048 znaków do wpisania:</span><br>
						<form:input path="topic" placeholder="tytuł, min. 2 znaki, max. 30" class="messageTopicArea"/>
						<form:errors path="topic" style="font-weight: bold; font-style: italic; color: red"/><br>
						
						<form:textarea rows="4" cols="50" path="text" placeholder="treść wiadomości, min. 2 znaki, max. 2048" class="messageTextArea"/> 
						<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>	
						<input type="submit" value="wyślij">	
					</form:form>
				</c:if>
				
			
				<br><br>Oto wszystkie tweety użytkownika <b>${viewedUser.username}</b>:<br><br>
			
				<table>
					<c:forEach items="${tweets}" var="tweet">
						
						<div class="row">
							<tr>
								<td>
									<list>
										<ul>
											<li>
											<c:if test="${not empty tweet.user.usrImg}">
												<img class="tweetPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${tweet.user.id}"/> 							
											</c:if>
											Autor tweeta: <b>${tweet.user.username}</b>, data utworzenia: ${tweet.created}</li>
											<li>Treść: <pre class="preTweet">${tweet.text}</pre></li>
											<li>Liczba komentarzy:
											
												<c:forEach items="${commentCountMap}" var="mapEntry">
													<c:if test="${mapEntry.key == tweet.id}">
														${mapEntry.value}
													</c:if>
												</c:forEach>
											
											</li>
											<br>
											<li><a href="<%out.print(request.getContextPath());%>/tweet/${tweet.id}">Zobacz szczegóły tweeta i komentarze do niego</a></li>
	
											<c:if test="${loggedUser.admin == true}">
												<li>
												<form action="<%out.print(request.getContextPath());%>/adminEdit">
													<input type="hidden" name="tweetId" value="${tweet.id}">
													<input type="submit" value="Edytuj tego tweeta"><br><br>
												</form>
												</li>
											</c:if>
											
											</ul>
									</list>
								</td>
						</div>  <!--  koniec div "row" -->
			
					</c:forEach>
				</table>
		
			</c:if>
		
		</body>
</html>