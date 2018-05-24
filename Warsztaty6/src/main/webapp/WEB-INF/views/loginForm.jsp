<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>login form</title>
</head>
<body>
	<h3>loginForm.jsp</h3>
	
	${info}
	<form method="post">
	
		Email<br> 
		<input type="text" name="email" placeholder="email"><br>
		
		Password<br>
		<input type="password" name="password" placeholder="password"><br>
		
		<input type="submit" value="zaloguj">

	</form>

</body>
</html>