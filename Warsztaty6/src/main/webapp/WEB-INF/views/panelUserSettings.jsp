<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Ustawienia profilu</title>
	</head>
	<body>
		<br><br>
		<c:if test="${not empty operationInfo}">
			<b style="color: blue">${operationInfo}</b><br>
		</c:if>
		
				<c:if test="${not empty error}">
			<b style="color: red">${error}</b><br>
		</c:if>
		
		<center>
	
			<form method="post" enctype="multipart/form-data">
	            Wybierz plik do załadowania aby zmienić aktualny obrazek (max. 1MB):<br>
				<input type="file" name="fileUploaded"/><br>
				<input type="submit" value="wyślij" />
	        </form>
	        
	        <c:if test="${not empty user.usrImg}">
				<h3>Twój obecny avatar:</h3> <br>
				<img src="<%out.print(request.getContextPath());%>/imageDisplay?id=${user.id}"/><br><br>
			</c:if> 
			
			<c:if test="${empty user.usrImg}">
				<h3>Nie ustawiłeś jeszcze swojego obrazka.</h3> <br>
			</c:if>
			
			
			<form action="<%out.print(request.getContextPath());%>/panelUser/userSettings">
				<input type="hidden" name="userDeleteImage" value="true">
				<input type="submit" value="Usuń swój obrazek"><br><br>
			</form>
			
			
			<input id="changeLoginEmailButton" type="submit" value="Zmień swój login i email">
			<input id="changePasswordButton" type="submit" value="Zmień swoje hasło"> 
			
			<div id="changeLoginEmailDiv" style="display: none">
				<form:form method="post" modelAttribute="user" action="userChangeProfileData">
	
					<br>Podaj nowy login: <form:input path="username" placeholder="username"/><br>
					<form:errors path="username"/><br>
					
					Podaj nowy email: <form:input path="email" placeholder="email"/><br>
					<form:errors path="email"/><br>
					
					<form:hidden path="id"/>
					<form:hidden path="enabled"/>
					<form:hidden path="deleted"/>
					<form:hidden path="admin"/>
					<form:hidden path="password"/>
					<form:hidden path="usrImg"/>
					
					<input type="submit" value="zmień">
	
				</form:form>		
			</div>
			
			<div id="changePasswordDiv" style="display: none">
				<form method="post"  action="userChangeProfilePassword">
					<br>Podaj nowe hasło: <input type="password" name="passwordToChange" placeholder="hasło">
					<br><br><input type="submit" value="Zmień hasło"><br><br>
				</form>			
			</div>
			
			
			
			<a class="confirm" style="color: red" href="<%out.print(request.getContextPath());%>/deleteUserAccount/${user.id}"><h3>Usuń swoje konto.</h3></a>
			
			<h4>UWAGA! Usunięcie Twojego konta skasuje również wszystkie Twoje tweety oraz komentarze!</h4>
		
	        

		</center>

	</body>
	<%@ include file="fragments/footer.jsp"%>
</html>