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
		Autor tweeta: <b>${tweet.user.username}</b>, data utworzenia: ${tweet.created}<br><br>
		Treść: <pre class="preTweet">${tweet.text}</pre>
		
		<c:if test="${not empty tweet.receiver}">
			Adresat: <b>${tweet.receiver}</b><br>
		</c:if> 
		
		<br>
		
		<c:if test="${not empty comments}">
			Komentarze do tweeta:<br>
			
			<c:forEach items="${comments}" var="comment">
					
				<div class="row">
					<table>
						<tr>
							<td>
								<list>
									<ul>
										<li>Autor komentarza: <b>${comment.user.username}</b>, data utworzenia: ${comment.created}</li>
										<li>Treść: <pre class="preComment">${comment.text}</pre></li>
									</ul>
								</list>
							</td>
						</tr>
					</table>
				</div>  <!--  koniec div "row" -->
		
			</c:forEach>
			
		</c:if>  
	
	</body>
</html>