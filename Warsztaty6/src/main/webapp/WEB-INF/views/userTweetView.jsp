<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Tweet</title>
	</head>
	<body>
	
	
		<br><br>
		
		<c:if test="${not empty tweet}">
		
			<c:if test="${not empty tweet.user.usrImg}">
				<img class="tweetPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${tweet.user.id}"/> 							
			</c:if>
			
			Autor tweeta: <a href="<%out.print(request.getContextPath());%>/userView/${tweet.user.id}"><b>${tweet.user.username}</b></a>, data utworzenia: ${tweet.created}<br><br>
			Treść: <pre class="preTweet">${tweet.text}</pre>
			
			<c:if test="${not empty tweet.receiver}">
				Adresat: <b>${tweet.receiver}</b><br>
			</c:if> 
			
			<br>
			
			<c:if test="${loggedUser.admin == true}">
				<form action="<%out.print(request.getContextPath());%>/adminEdit">
					<input type="hidden" name="tweetId" value="${tweet.id}">
					<input type="submit" value="Edytuj tego tweeta"><br><br>
				</form>
			</c:if> 
			
			<c:if test="${not empty comments}">
				Komentarze do tweeta:<br>
				
				<c:forEach items="${comments}" var="comment">
						
					<div class="row">
						<table>
							<tr>
								<td>
									<list>
										<ul>
											<li>
											
											<c:if test="${not empty comment.user.usrImg}">
												<img class="commentPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${comment.user.id}"/> 							
											</c:if> 
						
											Komentarz użytkownika <a href="<%out.print(request.getContextPath());%>/userView/${comment.user.id}"><b>${comment.user.username}</b></a>, data utworzenia: ${comment.created}</li>
											
											<li><pre class="preComment">${comment.text}</pre></li>
											
											<c:if test="${loggedUser.admin == true}">
												<li>
													<form action="<%out.print(request.getContextPath());%>/adminEdit">
														<input type="hidden" name="commentId" value="${comment.id}">
														<input type="submit" value="Edytuj ten komentarz"><br><br>
													</form>
												</li>
											</c:if> 
											
										</ul>
									</list>
								</td>
							</tr>
						</table>
					</div>  <!--  koniec div "row" -->
			
				</c:forEach>
				
			</c:if>  
			
			<!-- comment section -->
			
			<c:if test="${not empty info}">	
				<form:form method="post" modelAttribute="comment" >
					Napisz nowy komentarz:<br>
					<form:textarea rows="4" cols="50" path="text" placeholder="treść komentarza"/><br> 
					<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>	
					<input type="submit" value="wyślij">	
				</form:form>
			</c:if> 
		
		</c:if> 
		
		<c:if test="${empty tweet}">
			<h3>Brak tweeta o podanym bumerze w bazie.</h3>
		</c:if>
	
	</body>
</html>