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
		
	        

		</center>
	
	
	
	</body>
</html>