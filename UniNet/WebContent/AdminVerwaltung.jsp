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
<title>UniNet - Adminverwaltung</title>
</head>
<body>
	<page:adminKopfzeile></page:adminKopfzeile>
	<br><br><br><br>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>Admin ID</th>
				<th>Vorname</th>
				<th>Nachname</th>
				<th>Universität</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="admin" items="${ adminList }">
				<page:admin admin="${ admin }"></page:admin>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>