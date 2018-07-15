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
		<c:if test="${not empty param.operationInfo}">
			<b style="color: blue"><h2>${param.operationInfo}</h2></b><br>
		</c:if>
		
		<c:if test="${not empty info}">		
			<form:form method="post" modelAttribute="tweet">
				<span class="tweetCharCounter">Stwórz nowego tweeta. Pozostało 280 znaków do wpisania:</span><br>
				<form:textarea rows="4" cols="50" path="text" placeholder="treść tweeta" class="tweetTextArea"/><br> 
				<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>	
				<input type="submit" value="wyślij">	
			</form:form>
			<input type="hidden" class="UserIsLogged" />
		</c:if> 
		
		<br>Liczba wyświetlanych tweetów: ${tweetsPerPage}<br>
		
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
										
											<a href="tweet/${tweet.id}">Tweet użytkownika <a href="<%out.print(request.getContextPath());%>/userView/${tweet.user.id}"><b>${tweet.user.username}</b></a>, data utworzenia: ${tweet.created}</a>
										</li>
										<li>
											<pre class="preTweet">${tweet.text}</pre>
										</li>
										
										<li>Liczba komentarzy:		
				<!-- 1 1 -->				<c:forEach items="${commentCountMap}" var="mapEntry">
												<c:if test="${mapEntry.key == tweet.id}">
				<!-- 1 3 -->						${mapEntry.value}
				
													<c:if test="${not empty loggedUser}">
														<c:if test="${loggedUser.admin == true}">
															<a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserTweet/${tweet.id}">usuń tweeta</a>
															<a href="<%out.print(request.getContextPath());%>/adminEdit?tweetId=${tweet.id}">edytuj tweeta</a>
														</c:if>
													</c:if>
				
				<!-- 1 4 -->					</c:if>
											</c:forEach>
										</li>
									</ul>
								</list>					
							</td>
						</tr>
					</table>
				</div>  <!--  koniec div "row" -->
							
				<list class="commentListList" id="${tweet.id}"> <!-- This list and div are before c:if for the js "add comment" button to properly show up -->
					<div class="row" style="margin-left: 5em;">
						<c:if test="${not empty comments}">
							<table>
	 	<!-- 2 1 -->			<c:forEach items="${comments}" var="comment"><c:if test="${comment.tweet.id == tweet.id}">
		<!-- 2 3 -->					<%counter = counter+1;%>
										<tr>
											<td>
												<ul>
													<li class="commentListLi" id="<%=counter%>">
														
														<c:if test="${not empty comment.user.usrImg}">
															<img class="commentPicture" src="<%out.print(request.getContextPath());%>/imageDisplay?id=${comment.user.id}"/> 							
														</c:if> 
														
														Komentarz użytkownika <a href="<%out.print(request.getContextPath());%>/userView/${comment.user.id}"><b>${comment.user.username}</b></a>, data utworzenia: ${comment.created}s 
														<pre class="preComment">${comment.text}</pre>
														
														<c:if test="${not empty loggedUser}">
															<c:if test="${loggedUser.admin == true}">
																<a class="confirm" href="<%out.print(request.getContextPath());%>/adminHardDeleteUserComment/${comment.id}">usuń komentarz</a>
																<a href="<%out.print(request.getContextPath());%>/adminEdit?commentId=${tweet.id}">edytuj komentarz</a>
															</c:if>
														</c:if>
													</li>
												</ul>
											</td>
										</tr>			
		<!-- 2 4 -->				</c:if></c:forEach>					
							</table>
						</c:if>
					</div>  <!--  koniec div "row" -->
				</list>
				
				<c:if test="${not empty info}"> 
					<form:form method="post" modelAttribute="comment" action='mainPageAddComment/?pageNumber=${pageNumber}&tweetsPerPage=${tweetsPerPage}' class="commentForm" id="${tweet.id}">
						<span class="commentCharCounter">Napisz nowy komentarz. Pozostało 60 znaków do wpisania:</span><br>
						<form:textarea rows="4" cols="50" path="text" placeholder="treść komentarza" class="commentTextArea"/><br> 
						<form:errors path="text" style="font-weight: bold; font-style: italic; color: red"/><br>
						<form:hidden path="tweet.id" value="${tweet.id}"/>	
						<input type="submit" value="wyślij">	
					</form:form>  
				</c:if>
			</c:forEach>
		</table>
		
		<h4>Liczba stron: ${numberOfPages}, obecna strona: ${pageNumber}, wszystkich tweetów w bazie: ${tweetCount}</h4>
		<form style="display: inline-block">
			Przejdź do strony numer: 
			<input type="number" name="pageNumber" value=1">
			<input type="hidden" name="tweetsPerPage" value="${tweetsPerPage}">
			<input type="submit" value="przejdź">
		</form>
		
		<form style="display: inline-block" id="tweetsPerPageForm">
			Liczba tweetów na stronę: 
			<select name="tweetsPerPage">
				<option value="${tweetsPerPage}">${tweetsPerPage}</option>
			    <option value="10">10</option>
			    <option value="20">20</option>
			    <option value="30">30</option>
			    <option value="40">40</option>
			    <option value="50">50</option>
			    <option value="75">75</option>
			    <option value="100">100</option>
			</select>
			<input type="hidden" name="pageNumber" value="${pageNumber}">
		</form>
		
		<br><br>
		
		<c:if test="${pageNumber > 1}">
			<a href="<%out.print(request.getContextPath());%>?pageNumber=${pageNumber-1}&tweetsPerPage=${tweetsPerPage}"/>Poprzednia strona</a>
		</c:if>
		
		<c:if test="${pageNumber < numberOfPages}">
			<a href="<%out.print(request.getContextPath());%>?pageNumber=${pageNumber+1}&tweetsPerPage=${tweetsPerPage}"/>Następna strona</a>
		</c:if>
		
		<c:if test="${pageNumber > 1}">
			<a href="<%out.print(request.getContextPath());%>?pageNumber=1&tweetsPerPage=${tweetsPerPage}"/>Pierwsza strona</a>
		</c:if>
		
		<c:if test="${pageNumber < numberOfPages}">
			<a href="<%out.print(request.getContextPath());%>?pageNumber=${numberOfPages}&tweetsPerPage=${tweetsPerPage}"/>Ostatnia strona</a>
		</c:if>
		
		<br><br>
		
	</body>
	
	<%@ include file="fragments/footer.jsp"%>
	
</html>




