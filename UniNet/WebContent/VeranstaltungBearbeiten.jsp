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
<title>UniNet - Veranstaltung anlegen</title>
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
	<form id="registrierung" name="registrierung" role="form"
		class="form-horizontal" action="AdminVeranstaltungBearbeitenServlet"
		method="post">
		<div class="form-group">
			<label for="name" class="col-sm-2 control-label">Name:</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="name"
					placeholder="Veranstaltungsname eingeben" name="name"
					value="${ name }" />
			</div>
		</div>
		<div class="form-group">
			<label for="dozent" class="col-sm-2 control-label">Dozent:</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="dozent"
					placeholder="Dozenten eingeben" name="dozent" value="${ dozent }" />
			</div>
		</div>
		<div class="form-group">
			<label for="semester" class="col-sm-2 control-label">Semester</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="semester"
					placeholder="Empfohlenes Semester" name="semester"
					value="${ semester }" />
			</div>
		</div>
		<div class="form-group">
			<label for="beschreibung" class="col-sm-2 control-label">Beschreibung:</label>
			<div class="col-sm-6">
				<textarea class="form-control" rows="5" id="beschreibung"
					name="beschreibung">${ beschreibung }</textarea>
			</div>
		</div>
		<div class="form-group">
			<label for="sonstiges" class="col-sm-2 control-label">Sonstiges:</label>
			<div class="col-sm-6">
				<textarea class="form-control" rows="5" id="sonstiges"
					name="sonstiges">${ sonstiges }</textarea>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-2"></div>
			<div class="col-sm-4">
				<button type="submit" class="btn btn-register" name="registrieren">Änderungen übernehmen</button>
			</div>
		</div>
	</form>
</body>
</html>