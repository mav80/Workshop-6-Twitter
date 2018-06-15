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
				<span class="tweetCharCounter">Stwórz nową wiadomość. Pozostało 280 znaków do wpisania:</span><br>
				<form:textarea rows="4" cols="50" path="text" placeholder="treść tweeta" class="tweetTextArea"/><br> 
				<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>	
				<input type="submit" value="wyślij">	
			</form:form>
			
			<input type="hidden" class="UserIsLogged" />

		</c:if> 
		

		<br>Oto wszystkie tweety znajdujące się w bazie (razem: ${tweetCount}): <br><br>
		
		<table>
			<c:forEach items="${tweets}" var="tweet">
			
				<%int counter = 0;%>
					
				<div class="row">
					<table>
						<tr>
							<td>
								<list>
									<ul>
										<li>
											<c:if test="${not empty tweet.user.usrImg}">
												<img class="tweetPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${tweet.user.id}"/> 							
											</c:if> 
										
											<a href="tweet/${tweet.id}">Tweet użytkownika <b>${tweet.user.username}</b>, data utworzenia: ${tweet.created}</a></li>
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
			
					<list class="commentListList" id="${tweet.id}"> <!-- This list and div are before c:if for the js "add comment" button to properly show up -->
					
						<div class="row" style="margin-left: 5em;">
						
							<table>
							
							<c:forEach items="${comments}" var="comment">
							

							
									<c:if test="${comment.tweet.id == tweet.id}">
									
										<% counter = counter +1; %>
					
										<tr>
											<td>
												
													<ul>
														<li class="commentListLi" id="<%=counter%>">
															
															<c:if test="${not empty comment.user.usrImg}">
																<img class="commentPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${comment.user.id}"/> 							
															</c:if> 
															
															Komentarz użytkownika <b>${comment.user.username}</b>, data utworzenia: ${comment.created}s 
															<pre class="preComment">${comment.text}</pre>
														</li>
														
													</ul>
												
											</td>
										</tr>
										
									</c:if>
								
	
							</c:forEach>
							</table>
						</div>  <!--  koniec div "row" -->

					</list>
					
				</c:if>
				
				<c:if test="${not empty info}"> 
					<form:form method="post" modelAttribute="comment" action='mainPageAddComment' class="commentForm" id="${tweet.id}">
						<span class="commentCharCounter">Napisz nowy komentarz. Pozostało 60 znaków do wpisania:</span><br>
						<form:textarea rows="4" cols="50" path="text" placeholder="treść komentarza" class="commentTextArea"/><br> 
						<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>
						<form:hidden path="tweet.id" value="${tweet.id}"/>	
						<input type="submit" value="wyślij">	
					</form:form>  
				</c:if>
	
			</c:forEach>
		</table>
		
		
	
	</body>
</html>




