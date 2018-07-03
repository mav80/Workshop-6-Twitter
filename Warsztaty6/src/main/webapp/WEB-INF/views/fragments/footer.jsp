<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script type="text/javascript" src="<%out.print(request.getContextPath());%>/static/js/jquery-3.3.1.min.js"></script>
		<script>
		
		// When the user scrolls down 20px from the top of the document, show the button
		window.onscroll = function() {scrollFunction()};
		
		function scrollFunction() {
		    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
		        document.getElementById("myScrollToTopBtn").style.display = "block";
		    } else {
		        document.getElementById("myScrollToTopBtn").style.display = "none";
		    }
		}
		
		// When the user clicks on the button, scroll to the top of the document
		function topFunction() {
			console.log("Klik!");
			$('html, body').animate({
		        scrollTop: 0
		    }, 500);
		    //document.body.scrollTop = 0; // For Safari
		    //document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
		}

		</script>
	</head>
	<body>
		<footer>
			<a href="<%out.print(request.getContextPath());%>/">powrót do strony głównej</a>
			<button onclick="topFunction()" id="myScrollToTopBtn" title="Powrót na górę strony"><b>^</b></button>				
		</footer>
	</body>
</html>