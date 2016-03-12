<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="bootstrap.css">
<link rel="stylesheet" href="Self.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Willkommen bei UniNet</title>
</head>
<body >
	<jsp:include page="/testservlet"></jsp:include>
	<form class="form-inline containerColor">
		<div class="row"><br></div>
		<div class="col-md-2"></div>
		<div class="col-md-4">
			<div class="hSelf">UniNet - Sei auch du dabei</div>
		</div>
		<div class="form-group">
			<label class="sr-only" for="exampleInputEmail3">E-Mail Adresse</label> <input type="email" class="form-control"
				id="exampleInputEmail3" placeholder="E-Mail">
		</div>
		<div class="form-group">
			<label class="sr-only" for="exampleInputPassword3">Passwort</label>
			<input type="password" class="form-control"
				id="exampleInputPassword3" placeholder="Passwort">
		</div>
		<div class="checkbox checkboxWhite">
			<label> <input type="checkbox"> Angemeldet bleiben
			</label>
		</div>
		<button type="submit" class="btn btn-default buttonLightBlue">Anmelden</button>
		<div class="row"><br></div>
	</form>
	
	<div class="row"><br><br><br></div>
		
	<form id="registrierung" role="form" class="form-horizontal" action="testservlet" method="post">
		<div class="form-group">
			<label for="Anrede" class="col-sm-2 control-label">Anrede</label>
			<div class="col-sm-4">
				<div class="row">
					<div class="col-md-3">
						<select class="form-control" name="anrede">
							<option>Herr</option>
							<option>Frau</option>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="name" class="col-sm-2 control-label"> Name</label>
			<div class="col-sm-2">
				<input type="text" class="form-control" id="vorname" placeholder="Vorname eingeben" name="vorname" />
			</div>
			<div class="col-sm-2">
				<input type="text" class="form-control" id="nachname" placeholder="Nachname eingeben" name="nachname" />
			</div>
		</div>
		<div class="form-group">
			<label for="email" class="col-sm-2 control-label">Email</label>
			<div class="col-sm-4">
				<input type="email" class="form-control" id="email" placeholder="Email" name="email"/>
			</div>
		</div>
		<div class="form-group">
			<label for="uni" class="col-sm-2 control-label">Universität</label>
			<div class="col-md-8">
				<div class="row">
					<div class="col-sm-3">
						<select class="form-control" name="uni">
							<c:forEach var="uni" items="${ unis }">
								<option>${ uni }</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-sm-1">
						<button form="reg">OK</button>
					</div>
				</div>
			</div>	
		</div>
		<div class="form-group">
			<label for="studiengang" class="col-sm-2 control-label">Studiengang</label>
			<div class="col-md-4">
				<div class="row">
					<div class="col-sm-8">
						<select class="form-control" name="uni">
							<c:forEach var="uni" items="${ studiengaenge }">
								<option>${ uni }</option>
							</c:forEach>
						</select>
					</div>
				</div>	
			</div>
		</div>
		<div class="form-group">
			<label for="password" class="col-sm-2 control-label">
				Passwort</label>
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
				<button type="submit" class="btn btn-primary btn-sm">
					Registrieren</button>
				<button type="reset" class="btn btn-default btn-sm">
					Zurücksetzen</button>
			</div>
		</div>
	</form>
	
	<form id="reg" role="form" class="form-horizontal" action="StudiengaengeServlet" method="get">
		<select class="form-control" name="uni">
			<c:forEach var="uni" items="${ unis }">
				<option>${ uni }</option>
			</c:forEach>
		</select>
	</form>
	
	
</body>
</html>