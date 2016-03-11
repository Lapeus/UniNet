<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hier könnte Ihre Werbung stehen</title>
</head>
<body>
<form action="testservlet" method="get">
Bitte Vornamen eingeben<input type="text" name="name"> 
<input type="submit">
</form>
<p>
	<table border="1px">
	<c:forEach var="res" items="${ result }">
		<c:out escapeXml="false" value="${ res }"></c:out>
	</c:forEach>
	</table>	
</body>
</html>