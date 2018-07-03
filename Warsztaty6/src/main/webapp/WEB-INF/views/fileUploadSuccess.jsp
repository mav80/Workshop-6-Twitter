<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="fragments/header.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Plik wysłano</title>
	</head>
	<body>
	
		<center>
		
			<c:if test="${empty message}">	
				<h3>Plik wysłano poprawnie:</h3> <br>
				<img src="<%out.print(request.getContextPath());%>/imageDisplay?id=${user.id}"/>
			</c:if> 
			
			<c:if test="${not empty message}">	
				<br><br><br><b>${message}</b> <br><br><br>
				<img src="<%out.print(request.getContextPath());%>/imageDisplay?id=${user.id}"/>
			</c:if> 
			
		</center>
		
	</body>
	<%@ include file="fragments/footer.jsp"%>
</html>