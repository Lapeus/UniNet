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
<title>UniNet - Studieng&auml;nge verwalten</title>
</head>
<body>
	<page:localAdminKopfzeile></page:localAdminKopfzeile>
	<br>
	<br>
	<br>
	<div class="row">
		<div class="col-sm-4">
			<br>${ meldung }<br>
		</div>
	</div>
	<form id="hinzufuegen" name="hinzufuegen" role="form" class="form-inline"
		action="AdminStudiengaengeServlet" method="post">
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">${ universitaet }:</div>
				<input type="text" class="form-control" name="studiengang" id="studiengang"
					placeholder="Studiengang">
			</div>
		</div>
		<button type="submit" class="btn btn-register">Hinzuf&uuml;gen</button>
	</form>
	<br><br>
	<table class="table table-striped">
		<thead>
			<tr>
				<th><a href='AdminStudiengaengeServlet?sort=id'>Studiengangs ID</a></th>
				<th><a href='AdminStudiengaengeServlet?sort=name'>Studiengangsname</a></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="studiengang" items="${ studiengangsList }">
				<page:studiengang studiengang="${ studiengang }"></page:studiengang>
			</c:forEach>
		</tbody>
	</table>
	
</body>
</html>