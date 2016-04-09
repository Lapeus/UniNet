<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
<link rel="icon" href="UniNet_Logo.ico">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Willkommen bei UniNet</title>
</head>
<body class="anmeldung">
	<jsp:include page="/AnmeldePassivServlet"></jsp:include>
	<form class="form-inline containerColor anmeldeKopf" action="AnmeldeAktivServlet" method="post">
		<div class="row"><br></div>
		<div class="col-md-2"></div>
		<div class="col-md-4">
			<div class="hSelf">UniNet - Sei auch du dabei</div>
		</div>
		<div class="form-group">
			<label class="sr-only" for="email">E-Mail Adresse</label> 
			<input autofocus type="email" class="form-control" id="email" placeholder="E-Mail" name="email">
		</div>
		<div class="form-group">
			<label class="sr-only" for="password">Passwort</label>
			<input type="password" class="form-control" id="password" placeholder="Passwort" name="password">
		</div>
		<div class="checkbox checkboxWhite">
			<label> <input type="checkbox"> Angemeldet bleiben
			</label>
		</div>
		<button type="submit" class="btn btn-default buttonLightBlue" name="anmelden">Anmelden</button>
		<div class="row"><br></div>
	</form>
	
	<div class="row">
		<div class="col-sm-4">
			<br>${ meldung }<br>
		</div>
	</div>
	<form id="registrierung" role="form" class="form-horizontal" action="AnmeldeAktivServlet" method="post">
		<div class="form-group">
			<label for="Anrede" class="col-sm-2 control-label">Anrede</label>
			<div class="col-sm-4">
				<div class="row">
					<div class="col-md-3">
						<select class="form-control" name="anrede" >
							<option ${anredeM}>Herr</option>
							<option ${anredeF}>Frau</option>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="name" class="col-sm-2 control-label">Name</label>
			<div class="col-sm-2">
				<input type="text" class="form-control" id="vorname" placeholder="Vorname eingeben" name="vorname" value="${ vorname }"/>
			</div>
			<div class="col-sm-2">
				<input type="text" class="form-control" id="nachname" placeholder="Nachname eingeben" name="nachname" value="${ nachname }"/>
			</div>
		</div>
		<div class="form-group">
			<label for="email" class="col-sm-2 control-label">Email</label>
			<div class="col-sm-4">
				<input type="email" class="form-control" id="email" placeholder="Email" name="email" value="${ email }"/>
			</div>
		</div>
		<div class="form-group">
			<label for="uni" class="col-sm-2 control-label">Universität</label>
			<div class="col-sm-8">
				<div class="row">
					<div class="col-sm-3">
						<select class="form-control" name="uni" onchange="this.form.submit()">
							<c:forEach var="uni" items="${ unis }">
								<option>${ uni }</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>	
		</div>
		<div class="form-group">
			<label for="studiengang" class="col-sm-2 control-label">Studiengang</label>
			<div class="col-sm-8">
				<div class="row">
					<div class="col-sm-3">
						<select class="form-control" name="studiengang">
							<c:forEach var="uni" items="${ studiengaenge }">
								<option>${ uni }</option>
							</c:forEach>
						</select>
					</div>
				</div>	
			</div>
		</div>
		<div class="form-group">
			<label for="semester" class="col-sm-2 control-label">Semester</label>
			<div class="col-sm-2">
				<input type="number" min="1" max="30" value="1" class="form-control" id="semester" placeholder="Semester" name="semester">
			</div>
		</div>
		<div class="form-group">
			<label for="password" class="col-sm-2 control-label">Passwort</label>
			<div class="col-sm-4">
				<input type="password" class="form-control" id="password" placeholder="Passwort eingeben" name="password1" />
			</div>
		</div>
		<div class="form-group">
			<label for="password" class="col-sm-2 control-label">
				Passwort</label>
			<div class="col-sm-4">
				<input type="password" class="form-control" id="password" placeholder="Passwort erneut eingeben" name="password2" />
			</div>
		</div>
		<div class="row">
			<div class="col-sm-2"></div>
			<div class="col-sm-4">
				<button type="submit" class="btn btn-register" name="registrieren">Registrieren</button>
				<button type="reset" class="btn btn-default">Zurücksetzen</button>
			</div>
		</div>
	</form>
</body>
</html>