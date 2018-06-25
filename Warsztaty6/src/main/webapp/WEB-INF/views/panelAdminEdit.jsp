<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/headerUserPanel.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Panel administracyjny - edycja</title>
	</head>
	<body>
		
		<c:if test="${not empty operationInfo}">
			<br><br><b style="color: blue">${operationInfo}</b><br>
		</c:if>

		
		
		
		<!--  TWEET EDIT -->
		
		<c:if test="${not empty tweet}">
						
			<table>	
				<div class="row">
					<tr>
						<td>
							<list>
								<ul>
									<li>Autor tweeta: <b>${tweet.user.username}</b>, data utworzenia: ${tweet.created}</li>
									<li>Treść: <pre class="preTweet">${tweet.text}</pre></li>
									<li><a href="<%out.print(request.getContextPath());%>/tweet/${tweet.id}">Zobacz szczegóły tweeta i komentarze do niego</a></li>
									</ul>
							</list>
						</td>
							
						<td>
							<a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserTweet/${tweet.id}">Skasuj z bazy tego tweeta i komentarze do niego</a>
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
								
								Komentarz użytkownika <a href="<%out.print(request.getContextPath());%>/userView/${comment.user.id}"><b>${comment.user.username}</b></a>, data utworzenia: ${comment.created}s 
								<pre class="preComment">${comment.text}</pre>
							</li>
							
						</ul>
						
					</td>
						
					<td>
							<a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserComment/${comment.id}">Skasuj z bazy ten komentarz</a>
					</td>
				</tr>
		
			</table>
			
			<form:form method="post" modelAttribute="comment" action='adminEditComment' class="adminEditCommentForm">
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
		
		
		
		<!--  USER EDIT -->
		
		<c:if test="${not empty user}">
		
			<table>
								
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
									<li>Login: ${user.username}</li>
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
				
				</div>	<!--  koniec div "row" -->			
						
			</table>
			
		<form:form method="post" modelAttribute="user" action='adminEditUser' class="adminEditUserForm">
	
			Podaj login: <form:input path="username" placeholder="username"/><br>
			<form:errors path="username"/><br>
			
			Podaj hasło: <form:input path="password" placeholder="password"/><br>
			<form:errors path="password"/><br>
			
			Podaj email: <form:input path="email" placeholder="email"/><br>
			<form:errors path="email"/><br>
			
			<form:hidden path="id"/>
			<form:hidden path="enabled"/>
			<form:hidden path="deleted"/>
			<form:hidden path="admin"/>
			<form:hidden path="usrImg"/>
			
			
			<input type="submit" value="zmień">
	
		</form:form>
			
			
		
		</c:if>
		
		
		
		
		
	</body>
</html>