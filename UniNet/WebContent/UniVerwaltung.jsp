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
<title>UniNet - Universitäten Verwalten</title>
</head>
<body>
	<page:adminKopfzeile></page:adminKopfzeile>
	<br>
	<br>
	<br>
	<div class="row">
		<div class="col-sm-4">
			<br>${ meldung }<br>
		</div>
	</div>
	<form id="registrieren" role="form" class="form-inline"
		action="UnisVerwaltenServlet" method="post">
		<div class="form-group">
			<label for="uniname" class="col-sm-2 control-label">Universit&auml;t:</label>
		</div>
		<div class="form-group">
			<input type="text" class="form-control" id="uniname" placeholder="Uniname eingeben" name="uniname" />
		</div>
		<div class="form-group">	
			<input type="text" class="form-control" id="standort" placeholder="Standort eingeben" name="standort" />
		</div>
		<div class="form-group">	
			<button type="submit" class="btn btn-register" name="registrieren">Hinzuf&uuml;gen</button>
		</div>
		<div class="form-group">	
			<button type="reset" class="btn btn-default">Zur&uuml;cksetzen</button>
		</div>
	</form>
	<br><br>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>Uni ID</th>
				<th>Universit&auml;t</th>
				<th>Standort</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="uni" items="${ uniList }">
				<page:uni uni="${ uni }"></page:uni>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>