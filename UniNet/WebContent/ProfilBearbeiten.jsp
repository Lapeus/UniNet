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
<page:seitenAufbau chatfreunde="${chatfreunde}">
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
				<label class="radio-inline"><input type="radio" name="radGeburt" checked>Privat</label>
				<c:choose >
					<c:when test="${geburtsichtbar}">
						<label class="radio-inline"><input type="radio" name="radGeburt" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radGeburt">Öffentlich</label>
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
				<label class="radio-inline"><input type="radio" name="radWohnort" checked>Privat</label>
				<c:choose >
					<c:when test="${wohnortsichtbar}">
						<label class="radio-inline"><input type="radio" name="radWohnort" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radWohnort">Öffentlich</label>
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
				<label class="radio-inline"><input type="radio" name="radHobbys" checked>Privat</label>
				<c:choose >
					<c:when test="${hobbyssichtbar}">
						<label class="radio-inline"><input type="radio" name="radHobbys" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radHobbys">Öffentlich</label>
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
				<label class="radio-inline"><input type="radio" name="radInteressen" checked>Privat</label>
				<c:choose >
					<c:when test="${interessensichtbar}">
						<label class="radio-inline"><input type="radio" name="radInteressen" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radInteressen">Öffentlich</label>
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
				<label class="radio-inline"><input type="radio" name="radÜberMich" checked>Privat</label>
				<c:choose >
					<c:when test="${uebermichsichtbar}">
						<label class="radio-inline"><input type="radio" name="radUeberMich" checked>Öffentlich</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"><input type="radio" name="radUeberMich">Öffentlich</label>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="row"><div class="col-md-6">
			<button type="submit" style="margin-top: 5px;" class="btn btn-success pull-right">Änderungen sichern</button>
		</div></div>
	</form>
</page:seitenAufbau>
</body>
</html>