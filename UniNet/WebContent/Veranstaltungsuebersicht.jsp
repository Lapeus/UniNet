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
<title>UniNet - Veranstaltungen</title>
</head>
<body>
<page:kopfzeile userID="${id}"></page:kopfzeile>
<div class="mainPart">
	<!-- In der linken Spalte sollen die eigenen Veranstaltungen angezeigt werden -->
	<page:linkeSpalte>
		<label class="verfasser" style="width:100%; text-align: center;">Veranstaltungen</label>
		<ul class="nav nav-pills nav-stacked" style="background-color: white;">
			<c:forEach var="veranstaltung" items="${veranstaltungList}">
				<li role="presentation"><a class="schwarz" href="VeranstaltungenServlet?tab=beitraege&veranstaltungsID=${veranstaltung.id}">${veranstaltung.name}</a></li>
			</c:forEach>
		</ul>
	</page:linkeSpalte>
	<!-- Uebersicht ueber alle besuchbaren Veranstaltungen mit Ausnahme der bereits besuchten -->
	<page:mittlereSpalte>
		<label class="verfasser">Alle Veranstaltungen</label><br><br>
		<div class="row">
			<form action="VeranstaltungenServlet?name=Suche" method="post">
				<div class="col-md-4">
					<input class="form-control" type="text" name="suche">
				</div>
				<div class="col-md-2">
					<button class="btn containerColor" type="submit">Suchen</button>
				</div>
			</form>
		</div><br>
		<div style="background-color: white;">
			<br>
			<c:forEach var="veranstaltung" items="${veranstaltungen}">
				<div class="row">
					<div class="col-md-1"></div>
					<div class="col-md-8">
						<label><a class="blau" href="VeranstaltungenServlet?tab=infos&veranstaltungsID=${veranstaltung.id}">${veranstaltung.name}</a></label>
					</div>
				</div>
			</c:forEach>
		</div>
	</page:mittlereSpalte>
	<page:rechteSpalte chatfreunde="${chatfreunde}"></page:rechteSpalte>
</div>
</body>
</html>