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
<title>UniNet - Veranstaltungen Verwalten</title>
</head>
<body>
	<page:localAdminKopfzeile></page:localAdminKopfzeile>
	<br>
	<br>
	<br>
	<br> Veranstaltungen der ${ universitaet }:
	<table class="table table-striped">
		<thead>
			<tr>
				<th>Veranstaltungs ID</th>
				<th>Name</th>
				<th>Dozent</th>
				<th>Semester</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="veranstaltung" items="${ veranstaltungList }">
				<page:veranstaltung veranstaltung="${ veranstaltung }"></page:veranstaltung>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>