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
<page:kopfzeile userID="${userID}"></page:kopfzeile>
<div class="mainPart">
	<!-- In der linken Spalte sollen alle besuchten Veranstaltungen angezeigt werden -->
	<page:linkeSpalte>
		<!-- Die Ueberschrift Veranstaltungen soll auf die Veranstaltungsuebersicht verweisen -->
		<label class="verfasser" style="width:100%; text-align: center;">
			<a class="verfasser" href="VeranstaltungenServlet?name=Uebersicht">Veranstaltungen</a>
		</label>
		<ul class="nav nav-pills nav-stacked" style="background-color: white;">
			<c:forEach var="veranstaltung" items="${veranstaltungList}">
				<li role="presentation">
					<a class="schwarz" href="VeranstaltungenServlet?tab=${tab}&veranstaltungsID=${veranstaltung.id}">${veranstaltung.name}</a>
				</li>
			</c:forEach>
		</ul>
	</page:linkeSpalte>
	<page:mittlereSpalte>
		<label class="verfasser">${veranstaltung.name}</label><br><br>
		<ul class="nav nav-tabs">
			<!-- Man kann Beitraege nur sehen, wenn man in der Veranstaltung eingetragen ist -->
			<c:if test="${eingetragen}">
				<li role="presentation" class="${beitraegeActive}"><a href="VeranstaltungenServlet?tab=beitraege&veranstaltungsID=${veranstaltungsID}">Beiträge</a></li>
			</c:if>
			<li role="presentation" class="${infosActive}"><a href="VeranstaltungenServlet?tab=infos&veranstaltungsID=${veranstaltungsID}">Infos</a></li>
			<li role="presentation" class="${mitgliederActive}"><a href="VeranstaltungenServlet?tab=mitglieder&veranstaltungsID=${veranstaltungsID}">Mitglieder</a></li>
		</ul>
		<br>
		<!-- Unterscheidung, welcher Tab gerade aktiv ist -->
		<c:choose>
			<c:when test="${tab == 'beitraege' && eingetragen}">
				<!-- Beitrag posten -->
				<div class="row"><div class="col-md-1"></div>
					<div class="col-md-10"><div class="row">
					<form action="VeranstaltungenServlet?veranstaltungsID=${veranstaltungsID}&name=BeitragPosten" method="post">
						<div class="form-group">
							<textarea class="form-control" rows="4" name="beitrag" placeholder="Teile deinen Kommilitonen etwas mit..." required></textarea>
							<div class="form-inline pull-right">
								<!-- Sichtbarkeitsbutton -->
								<select class="form-control glyphicon" name="sichtbarkeit">
									<option>Privat &#xe008;</option>
									<option>Öffentlich &#xe135;</option>
								</select>
								<button type="submit" class="btn containerColor">Posten</button>
							</div>
							<br>
						</div>
					</form>
					</div></div>
				</div><br>
				<!-- Alle Beitraege anzeigen -->
				<c:forEach var="beitrag" items="${ beitragList }">
					<page:beitrag beitrag="${ beitrag }" page="VeranstaltungenServlet&tab=beitraege&veranstaltungsID=${veranstaltungsID}"></page:beitrag>
				</c:forEach>
			</c:when>
			<c:when test="${tab == 'infos'}">
				<ul class="list-group">
					<li class="list-group-item">Dozent: ${veranstaltung.dozent}</li>
					<li class="list-group-item">Empfohlenes Semester: ${veranstaltung.semester}</li>
					<li class="list-group-item">Beschreibung:<br>${veranstaltung.beschreibung}</li>
					<li class="list-group-item">Sonstiges:<br>${veranstaltung.sonstiges}</li>
				</ul>
				<c:choose>
					<c:when test="${!eingetragen}">
						<form action="VeranstaltungenServlet?veranstaltungsID=${veranstaltungsID}&name=Einschreiben" method="post">
							<button class="btn btn-success" type="submit">Einschreiben</button>
						</form>
					</c:when>
					<c:when test="${eingetragen}">
						<form action="VeranstaltungenServlet?veranstaltungsID=${veranstaltungsID}&name=Ausschreiben" method="post">
							<button class="btn btn-danger" type="submit">Ausschreiben</button>
						</form>
					</c:when>
				</c:choose>
			</c:when>
			<c:when test="${tab == 'mitglieder'}">
				<div class="row">
					<div class="col-md-3">
						<label class="schwarz" style="font-size: 14px;">Insgesamt: ${anzahl}</label>
					</div>
					<div class="col-md-9">
						<label class="pull-right" style="font-size: 12px;">alphabetisch sortiert nach 
							<a class="blau" style="${vornameLink}" href="VeranstaltungenServlet?veranstaltungsID=${veranstaltungsID}&tab=mitglieder&sortByV=true">Vorname</a>
							 / 
							<a class="blau" style="${nachnameLink}" href="VeranstaltungenServlet?veranstaltungsID=${veranstaltungsID}&tab=mitglieder&sortByV=false">Nachname</a>
						</label>
					</div>
				</div><br>
				<div class="row" style="background-color: white;"><br>
					<ul class="nav nav-pills">
						<c:forEach var="mitglied" items="${mitglieder}">
							<div class="col-md-4">
								<li role="presentation">
									<p><a class="mitglieder" href="ProfilServlet?userID=${mitglied.userID}"><b>${mitglied.vorname} ${mitglied.nachname}</b></a>
								</li>
							</div>
						</c:forEach>
					</ul>
				</div>
			</c:when>
		</c:choose>
	</page:mittlereSpalte>
	<page:rechteSpalte chatfreunde="${chatfreunde}"></page:rechteSpalte>
</div>
</body>
</html>