<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="page" uri="/WEB-INF/TLD/pageTags.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
<link rel="icon" href="UniNet_Logo.ico">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="author" content="Christian Ackermann">
<title>UniNet - Profil bearbeiten</title>
</head>
<body>
<page:seitenAufbau chatfreunde="${chatfreunde}" userID="${id}">
	<form action="LadeProfilbildServlet" method="post" enctype="multipart/form-data">
		<div class="row">
			<div class="col-md-3">
				<img class="img-responsive profilbild" alt="Profilbild" src="LadeProfilbildServlet">
			</div>
			<div class="col-md-3">
				<label class="btn btn-block btn-success">
					<input type="file" name="image" style="display:none;" onchange="this.form.submit()">
					Bild ändern
				</label>
			</div>
		</div>
	</form>
	<form action="ProfilBearbeitenServlet" method="post">
		<div class="row">
			<h4>Name*</h4>
			<div class="col-md-3">
				<input type="text" name="vorname" class="form-control" value="${vorname}" required>
			</div>
			<div class="col-md-3">
				<input type="text" name="nachname" class="form-control" value="${nachname}" required>
			</div>
		</div>
		<div class="row">
			<h4>Aktuelles Semester</h4>
			<div class="col-md-6">
				<input type="number" min="1" max="30" class="form-control" value="${semester}" name="semester">
			</div>
		</div>
		<div class="row">
			<h4>Geburtstag</h4>
			<div class="col-md-6">
				<input type="text" name="geburtstag" class="form-control" value="${geburtstag}">
			</div>
			<div class="col-md-4">
				<label class="radio-inline"><input type="radio" name="radGeburt" value="privat" checked>Privat</label>
				<c:choose >
					<c:when test="${geburtSichtbar}">
						<label class="radio-inline"><input type="radio" name="radGeburt" value="oeffentlich" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radGeburt" value="oeffentlich">Öffentlich</label>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="row">
			<h4>Wohnort</h4>
			<div class="col-md-6">
				<input type="text" name="wohnort" class="form-control" value="${wohnort}">
			</div>
			<div class="col-md-4">
				<label class="radio-inline"><input type="radio" name="radWohnort" value="privat" checked>Privat</label>
				<c:choose >
					<c:when test="${wohnortSichtbar}">
						<label class="radio-inline"><input type="radio" name="radWohnort" value="oeffentlich" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radWohnort" value="oeffentlich">Öffentlich</label>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="row">
			<h4>Hobbys</h4>
			<div class="col-md-6">
				<input type="text" name="hobbys" class="form-control" value="${hobbys}">
			</div>
			<div class="col-md-4">
				<label class="radio-inline"><input type="radio" name="radHobbys" value="privat" checked>Privat</label>
				<c:choose >
					<c:when test="${hobbysSichtbar}">
						<label class="radio-inline"><input type="radio" name="radHobbys" value="oeffentlich" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radHobbys" value="oeffentlich">Öffentlich</label>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="row">
			<h4>Interessen</h4>
			<div class="col-md-6">
				<input type="text" name="interessen" class="form-control" value="${interessen}">
			</div>
			<div class="col-md-4">
				<label class="radio-inline"><input type="radio" name="radInteressen" value="privat" checked>Privat</label>
				<c:choose >
					<c:when test="${interessenSichtbar}">
						<label class="radio-inline"><input type="radio" name="radInteressen" value="oeffentlich" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radInteressen" value="oeffentlich">Öffentlich</label>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="row">
			<h4>Über mich</h4>
			<div class="col-md-6">
				<textarea style="resize: none;" type="text" row=2 name="ueberMich" class="form-control">${ueberMich}</textarea>
			</div>
			<div class="col-md-4">
				<label class="radio-inline"><input type="radio" name="radUeberMich" value="privat" checked>Privat</label>
				<c:choose >
					<c:when test="${ueberMichSichtbar}">
						<label class="radio-inline"><input type="radio" name="radUeberMich" value="oeffentlich" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radUeberMich" value="oeffentlich">Öffentlich</label>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="row">
			<h4>Passwort ändern</h4>
			<div class="col-md-3">
				<input type="password" class="form-control" id="password1" placeholder="Passwort" name="password1">
			</div>
			<div class="col-md-3">
				<input type="password" class="form-control" id="password2" placeholder="Passwort erneut eingeben" name="password2">
			</div>
		</div>
		<div class="row"><div class="col-md-6">
			<button type="submit" style="margin-top: 5px;" class="btn btn-success pull-right">Änderungen sichern</button>
		</div></div>
	</form>
</page:seitenAufbau>
</body>
</html>