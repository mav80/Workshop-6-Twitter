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
		<div><h4>Zarządzanie użytkownikami</h4></div>
			<ul>
				<li>Znajdź użytkowników:</li>
				<li>
					<form>
						po loginie:<br>
						<input type="text" name="userSearchNameLike" placeholder="podaj login"><br>
						<input type="submit" value="Wyszukaj"> <br> <br>
					</form>
				</li>
				<li>
					<form>
						po emailu:<br>
						<input type="text" name="userSearchEmailLike" placeholder="podaj email"><br>
						<input type="submit" value="Wyszukaj"> <br> <br>
					</form>
				</li>
				<li>
					<form>
						po id:<br>
						<input type="number" name="userSearchId" placeholder="podaj id"><br>
						<input type="submit" value="Wyszukaj"> <br> <br>
					</form>
				</li>
				<li>
					<form>
						<input type="hidden" name="userSearchShowAll" value="true">
						<input type="submit" value="Pokaż wszystkich użytkowników znajdujących się w bazie"><br><br>
					</form>
				</li>
			</ul>
			
			<c:if test="${not empty param.operationInfo}">
				<b style="color: blue">${param.operationInfo}</b><br>
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
													<li> <a href="<%out.print(request.getContextPath());%>/adminShowUserComments?userId=${user.id}">pokaż wszystkie komentarze tego użytkownika</a></li>
													<li><a href="<%out.print(request.getContextPath());%>/adminEditUser/${user.id}">edytuj użytkownika</a></li>
													<c:if test="${user.deleted == true}">
														<li><a href="<%out.print(request.getContextPath());%>/adminToggleDeleteUser/${user.id}">ustaw status użytkownika na nie skasowany</a></li>
													</c:if>
													<c:if test="${user.deleted == false}">
														<li><a href="<%out.print(request.getContextPath());%>/adminToggleDeleteUser/${user.id}">ustaw status użytkownika na skasowany</a></li>
													</c:if>
													
													<c:if test="${user.enabled == true}">
														<li><a href="<%out.print(request.getContextPath());%>/adminToggleBanUser/${user.id}">ustaw status użytkownika na zbanowany</a></li>
													</c:if>
													<c:if test="${user.enabled == false}">
														<li><a href="<%out.print(request.getContextPath());%>/adminToggleBanUser/${user.id}">odbanuj użytkownika</a></li>
													</c:if>
													<li><a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserAndData/${user.id}">usuń fizycznie konto użytkownika i wszystkie jego tweety oraz komentarze z bazy</a></li>
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
											<a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserTweet/${tweet.id}">Skasuj z bazy tego tweeta i komentarze do niego</a></li>
										</td>
									</tr>
								</div>  <!--  koniec div "row" -->
					
							</c:forEach>
						</table>
					
					
					</section>
				</center>
			</c:if>
			
			
	</body>
</html>