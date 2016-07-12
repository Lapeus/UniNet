<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="/WEB-INF/TLD/pageTags.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
<link rel="icon" href="UniNet_Logo.ico">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="author" content="Leon Schaffert">
<title>UniNet - Userverwaltung</title>
</head>
<body>
	<page:localAdminKopfzeile></page:localAdminKopfzeile>
	<br><br><br><br>
	<table class="table table-striped">
		<thead>
			<tr>
				<th><a href='AdminUserVerwaltenServlet?sort=id'>User ID</a></th>
				<th><a href='AdminUserVerwaltenServlet?sort=vorname'>Vorname</a></th>
				<th><a href='AdminUserVerwaltenServlet?sort=nachname'>Nachname</a></th>
				<th><a href='AdminUserVerwaltenServlet?sort=mail'>E-Mail</a></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="student" items="${ studentList }">
				<page:student student="${ student }"></page:student>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>