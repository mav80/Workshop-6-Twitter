<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/headerUserPanel.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Panel użytkownika</title>
	</head>
	<body>
	
		<br><br>Oto wszystkie Twoje tweety:<br><br>
			
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
									<li><a href="<%out.print(request.getContextPath());%>/tweet/${tweet.id}">Zobacz szczegóły tweeta i komentarze do niego</a></li>
									
								</ul>
							</list>
						</td>
				</div>  <!--  koniec div "row" -->
	
			</c:forEach>
		</table>
	
	</body>
	<%@ include file="fragments/footer.jsp"%>
</html>