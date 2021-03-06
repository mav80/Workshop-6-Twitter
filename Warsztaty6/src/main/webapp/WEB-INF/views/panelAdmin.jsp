<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/headerUserPanel.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Panel administracyjny</title>
	</head>
	<body>
		<div class="searchList"><h4>Zarządzanie użytkownikami</h4>
			Znajdź użytkowników:
			<ul>
				<li>
					<form action="<%out.print(request.getContextPath());%>/panelAdmin">
						po loginie:<br>
						<input type="text" name="userSearchNameLike" placeholder="podaj login"><br>
						<input type="submit" value="Wyszukaj"> <br> <br>
					</form>
				</li>
				<li>
					<form action="<%out.print(request.getContextPath());%>/panelAdmin">
						po emailu:<br>
						<input type="text" name="userSearchEmailLike" placeholder="podaj email"><br>
						<input type="submit" value="Wyszukaj"> <br> <br>
					</form>
				</li>
				<li>
					<form action="<%out.print(request.getContextPath());%>/panelAdmin">
						po id:<br>
						<input type="number" name="userSearchId" placeholder="podaj id"><br>
						<input type="submit" value="Wyszukaj"> <br> <br>
					</form>
				</li>
				<li>
					<form action="<%out.print(request.getContextPath());%>/panelAdmin">
						<input type="hidden" name="userSearchShowAll" value="true">
						<input type="submit" value="Pokaż wszystkich użytkowników znajdujących się w bazie"><br><br>
					</form>
				</li>
			</ul>
		</div>
		
		<div class="searchList">
			<ul>
				<li>
					<form action="<%out.print(request.getContextPath());%>/adminAddTweets">
						<input type="hidden" name="tweetCount" value="10">
						<input type="submit" value="Dodaj 10 tweetów do bazy."><br><br>
					</form>
				</li>
			
				<li>
					<form action="<%out.print(request.getContextPath());%>/adminAddComments">
						<input type="submit" value="Dodaj komentarze do tweetów które ich nie mają"><br><br>
					</form>
				</li>
			</ul>
		</div>
			
		<c:if test="${not empty param.operationInfo}">
			<b style="color: blue">${param.operationInfo}</b><br>
		</c:if>
		
		<c:if test="${not empty operationInfo}">
			<b style="color: blue">${operationInfo}</b><br>
		</c:if>
		
		<c:if test="${not empty searchResultMessage}">
			<b>${searchResultMessage}</b>
		</c:if>
		
		
		
		<!-- USERS -->
		
		<c:if test="${not empty users}">
			<center>
				<section class="mytable">
					<table>
						<c:forEach items="${users}" var="user">		
							<div class="row">	
								<tr>
								
									<td>
										<c:if test="${not empty user.usrImg}">
											<img class="userPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${user.id}"/>
											 <br><a href="<%out.print(request.getContextPath());%>/adminDeleteUserImage/${user.id}">usuń obrazek</a>						
										</c:if>
									</td>
								
									<td>
										<list>
											<ul>
												<li>Login: <a href="<%out.print(request.getContextPath());%>/userView/${user.id}"><b>${user.username}</b></a></li>
												<li>Email: ${user.email}</li>
												<li>ID: ${user.id}</li>
											</ul>
										</list>
									</td>
								
								
								
									<td>
										<list>
											<ul>
												<li>Aktywny od: ${user.created}</li>
												<li>Czy zbanowany:
												
													<c:if test="${user.enabled == false}">
														<img src="<%out.print(request.getContextPath());%>/static/images/tick.png">
													</c:if>
		
													<c:if test="${user.enabled == true}">
														<img src="<%out.print(request.getContextPath());%>/static/images/cross.png">
													</c:if>
												
												</li>	
												<li>Czy usunięty:
												
													<c:if test="${user.deleted == true}">
														<img src="<%out.print(request.getContextPath());%>/static/images/tick.png">
													</c:if>
		
													<c:if test="${user.deleted == false}">
														<img src="<%out.print(request.getContextPath());%>/static/images/cross.png">
													</c:if>
												
												</li>							
											</ul>
										</list>
									</td>
									
									
									
									
									<td>
										<list>
											<ul>
												<li> <a href="<%out.print(request.getContextPath());%>/adminShowUserTweets/${user.id}">pokaż wszystkie tweety tego użytkownika</a></li>
												<li> <a href="<%out.print(request.getContextPath());%>/adminShowUserComments/${user.id}">pokaż wszystkie komentarze tego użytkownika</a></li>
												<li> <a href="<%out.print(request.getContextPath());%>/adminShowUserMessages/${user.id}">pokaż wszystkie wiadomości</a></li>
												
												<c:if test="${user.admin == false}">
													<li><a href="<%out.print(request.getContextPath());%>/adminEdit?userId=${user.id}">edytuj użytkownika</a></li>
												</c:if>
												
												<c:if test="${user.admin == false}">
													<c:if test="${user.deleted == true}">
														<li><a href="<%out.print(request.getContextPath());%>/adminToggleDeleteUser/${user.id}">ustaw status użytkownika na nie skasowany</a></li>
													</c:if>
													
													<c:if test="${user.deleted == false}">
														<li><a href="<%out.print(request.getContextPath());%>/adminToggleDeleteUser/${user.id}">ustaw status użytkownika na skasowany</a></li>
													</c:if>
												</c:if>
												
												<c:if test="${user.admin == false}">
													<c:if test="${user.enabled == true}">
														<li><a href="<%out.print(request.getContextPath());%>/adminToggleBanUser/${user.id}">ustaw status użytkownika na zbanowany</a></li>
													</c:if>
													<c:if test="${user.enabled == false}">
														<li><a href="<%out.print(request.getContextPath());%>/adminToggleBanUser/${user.id}">odbanuj użytkownika</a></li>
													</c:if>
												</c:if>
												
												<c:if test="${user.admin == false}">
													<li><a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserAndData/${user.id}">usuń fizycznie konto użytkownika i wszystkie jego tweety oraz komentarze z bazy</a></li>
												</c:if>
											</ul>
										</list>
									</td>
								
								</tr>
							
							</div>
						<!--  koniec div "row" -->
					
						</c:forEach>
					
					</table>
				</section>
			</center>
		</c:if>
		
		
		
		
		
		<!-- USER'S TWEETS -->
		
		<c:if test="${not empty userTweets}">
			<center>
				<section>
				
					<table>
						<c:forEach items="${userTweets}" var="tweet">
							
							<div class="row">
								<tr>
									<td>
										<list>
											<ul>
												<li>Autor tweeta: <b>${tweet.user.username}</b>, data utworzenia: ${tweet.created}</li>
												<li>Treść: <pre class="preTweet">${tweet.text}</pre></li>
												<li>Liczba komentarzy:
												
													<c:forEach items="${commentCountMap}" var="mapEntry">
														<c:if test="${mapEntry.key == tweet.id}">
															${mapEntry.value}
														</c:if>
													</c:forEach>
												
												</li><br>
												<li><a href="<%out.print(request.getContextPath());%>/tweet/${tweet.id}">Zobacz szczegóły tweeta i komentarze do niego</a></li>
												</ul>
										</list>
									</td>
										
									<td>
										<li><a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserTweet/${tweet.id}">Skasuj z bazy tego tweeta i komentarze do niego</a></li>
										<li><a href="<%out.print(request.getContextPath());%>/adminEdit?tweetId=${tweet.id}">Edytuj</a></li>
									</td>
								</tr>
							</div>  <!--  koniec div "row" -->
				
						</c:forEach>
					</table>
				
				
				</section>
			</center>
		</c:if>
		
		
		
		
		
		
		<!-- USER'S COMMENTS -->
		
		<c:if test="${not empty userComments}">
			<center>
				<section>
				
					<table>	
						<c:forEach items="${userComments}" var="comment">

							<tr>
								<td>
									
										<ul>
											<li class="commentListLi">
												
												<c:if test="${not empty comment.user.usrImg}">
													<img class="commentPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${comment.user.id}"/> 							
												</c:if> 
												
												Komentarz użytkownika <a href="<%out.print(request.getContextPath());%>/userView/${comment.user.id}"><b>${comment.user.username}</b></a>, data utworzenia: ${comment.created}s 
												<pre class="preComment">${comment.text}</pre>
											</li>
											
										</ul>
									
								</td>
								
								<td>
										<li><a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserComment/${comment.id}">Skasuj z bazy ten komentarz</a></li>
										<li><a href="<%out.print(request.getContextPath());%>/adminEdit?commentId=${comment.id}">Edytuj</a></li>
								</td>
							</tr>

								
	
						</c:forEach>
					</table>
				
				
				</section>
			</center>
		</c:if>
		
		
		
		

		
		
		
		<!-- USER'S MESSAGES -->
		
		<c:if test="${not empty messagesUser}">
		
			<h3>Wiadomości które użytkownik ${messagesUser.username} otrzymał:</h3>
			<table class="messagesList">
				<tr>
					<th>nadawca</th>
					<th>tytuł</th>
					<th>data utworzenia</th>
					<th>przeczytana?</th>
					<th>akcje</th>
				</tr>
				
				<c:forEach items="${messages}" var="message">
					<tr>		
						<td><a href="<%out.print(request.getContextPath());%>/userView/${message.sender.id}">${message.sender.username}</a></td>
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
						<td><a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserMessage/${message.id}">usuń</a></td>				
					</tr>
				</c:forEach>
			</table>
			
			<h3>Wiadomości które użytkownik ${messagesUser.username} wysłał:</h3>
			
			<table class="messagesList">
				<tr>
					<th>odbiorca</th>
					<th>tytuł</th>
					<th>data utworzenia</th>
					<th>przeczytana?</th>
					<th>akcje</th>
				</tr>
				
				<c:forEach items="${messagesSent}" var="message">
					<tr>		
						<td><a href="<%out.print(request.getContextPath());%>/userView/${message.receiver.id}">${message.receiver.username}</a></td>
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
						<td><a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserMessage/${message.id}">usuń</a></td>					
					</tr>
				</c:forEach>
			</table>
		</c:if>
		
		
		
		
		
		
		
		
		<!-- GENERATED TWEETS -->
		
		<c:if test="${not empty newGeneratedTweets}">
		
			<table>
				<c:forEach items="${newGeneratedTweets}" var="tweet">
				
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
											
												<a href="tweet/${tweet.id}">Tweet użytkownika <a href="<%out.print(request.getContextPath());%>/userView/${tweet.user.id}"><b>${tweet.user.username}</b></a>, data utworzenia: ${tweet.created}</a></li>
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
					
				</c:forEach>
			</table>
		
		</c:if>
		
		
		
		
		
		
		
		<!-- GENERATED COMMENTS -->
		
		
		<c:if test="${not empty newGeneratedComments}">
	
			
				<div class="row" style="margin-left: 5em;">
				
					<table>
					
						<c:forEach items="${newGeneratedComments}" var="comment">

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
								</tr>

						</c:forEach>
					</table>
				</div>  <!--  koniec div "row" -->
			
		</c:if>	
			
	</body>
	<%@ include file="fragments/footer.jsp"%>
</html>