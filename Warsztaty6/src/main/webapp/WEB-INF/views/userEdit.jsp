<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/headerUserPanel.jsp"%>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Edycja</title>
</head>
	<body>
	
		<!--  TWEET EDIT -->
		
		<c:if test="${not empty tweet}">
						
			<table>	
				<div class="row">
					<tr>
						<td>
							<list>
								<ul>
									<li>
										<c:if test="${not empty comment.user.usrImg}">
											<img class="commentPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${comment.user.id}"/> 							
										</c:if>  
									
									Autor tweeta: <b>${tweet.user.username}</b>, data utworzenia: ${tweet.created}</li>
									<li>Treść: <pre class="preTweet">${tweet.text}</pre></li>
									<li><a href="<%out.print(request.getContextPath());%>/tweet/${tweet.id}">Zobacz szczegóły tweeta i komentarze do niego</a></li>
									</ul>
							</list>
						</td>
							
						<td>
							<c:if test="${not empty loggedUser}">
								<c:if test="${loggedUser.admin == true}">
									<a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserTweet/${tweet.id}">Skasuj z bazy tego tweeta i komentarze do niego</a>
								</c:if>
							</c:if>
						</td>
					</tr>
				</div>  <!--  koniec div "row" -->
			</table>
			
			<form:form method="post" modelAttribute="tweet">
				<span class="tweetCharCounter">Edytuj tweeta. Pozostało 280 znaków do wpisania:</span><br>
				<form:textarea rows="4" cols="50" path="text" placeholder="treść tweeta" class="tweetTextArea"/><br> 
				<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>
				<form:hidden path="id"/>	
				<form:hidden path="user.id"/>	
				<input type="submit" value="zmień">	
			</form:form>
			
		</c:if>
		
		
		
		
		<!--  COMMENT EDIT -->
		
		<c:if test="${not empty comment}">
		
			<table>
									
				<tr>
					<td>
						
						<ul>
							<li class="commentListLi">
								
								<c:if test="${not empty comment.user.usrImg}">
									<img class="commentPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${comment.user.id}"/> 							
								</c:if> 
								
								Komentarz użytkownika <a href="<%out.print(request.getContextPath());%>/userView/${comment.user.id}"><b>${comment.user.username}</b></a>, data utworzenia: ${comment.created} 
								<pre class="preComment">${comment.text}</pre>
							</li>
							
						</ul>
						
					</td>
						
					<td>
						<c:if test="${not empty loggedUser}">
							<c:if test="${loggedUser.admin == true}">
								<a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserComment/${comment.id}">Skasuj z bazy ten komentarz</a>
							</c:if>
						</c:if>
					</td>
				</tr>
		
			</table>
			
			<form:form method="post" modelAttribute="comment" action='userEditComment'>
				<span class="commentCharCounter">Edytuj komentarz. Pozostało 60 znaków do wpisania:</span><br>
				<form:textarea rows="4" cols="50" path="text" placeholder="treść komentarza" class="commentTextArea"/><br> 
				<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>
				<form:hidden path="id"/>
				<form:hidden path="tweet.id"/>		
				<form:hidden path="user.id"/>	
				<form:hidden path="receiver"/>
				<input type="submit" value="wyślij">	
			</form:form>  
			
			
		</c:if>
	
	</body>
</html>