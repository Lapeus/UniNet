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
<title>UniNet - Gruppen</title>
</head>
<body>
<page:kopfzeile></page:kopfzeile>
<div class="mainPart">
	<!-- Uebersicht ueber die eigenen Gruppen in der linken Spalte -->
	<page:linkeSpalte>
		<!-- Die Ueberschrift Gruppen ist gleichzeitig ein Link auf die Gruppenuebersicht -->
		<label class="verfasser" style="width:100%; text-align: center;"><a class="verfasser" href="GruppenServlet?name=Uebersicht">Gruppen</a></label>
		<ul class="nav nav-pills nav-stacked" style="background-color: white;">
			<c:forEach var="gruppe" items="${gruppenList}">
				<!-- Wenn der aktuelle Tab Bearbeiten ist, muss dieser auf Beitraege gesetzt werden, sonst wird er beibehalten -->
				<c:set var="nextTab" value="${tab}"></c:set>
				<c:if test="${nextTab == 'bearbeiten'}">
					<c:set var="nextTab" value="beitraege"></c:set>
				</c:if>
				<li role="presentation">
					<a class="schwarz" href="GruppenServlet?tab=${nextTab}&gruppenID=${gruppe.id}">${gruppe.name}</a>
				</li>
			</c:forEach>
		</ul>
	</page:linkeSpalte>
	<page:mittlereSpalte>
		<label class="verfasser">${gruppe.name}</label><br><br>
		<ul class="nav nav-tabs">
			<li role="presentation" class="${beitraegeActive}"><a href="GruppenServlet?tab=beitraege&gruppenID=${gruppenID}">Beiträge</a></li>
			<li role="presentation" class="${infosActive}"><a href="GruppenServlet?tab=infos&gruppenID=${gruppenID}">Infos</a></li>
			<li role="presentation" class="${mitgliederActive}"><a href="GruppenServlet?tab=mitglieder&gruppenID=${gruppenID}">Mitglieder</a></li>
			<c:if test="${gruppe.adminID == userID}">
				<li role="presentation" class="${bearbeitenActive}"><a href="GruppenServlet?tab=bearbeiten&gruppenID=${gruppenID}">Bearbeiten</a></li>
			</c:if>
		</ul>
		<br>
		<!-- Unterscheidung, welcher Tab geoeffnet ist -->
		<c:choose>
			<c:when test="${tab == 'beitraege'}">
				<div class="row"><div class="col-md-1"></div>
					<div class="col-md-10"><div class="row">
					<form action="GruppenServlet?gruppenID=${gruppenID}&name=BeitragPosten" method="post">
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
				<!-- Alle Beitraege dieser Gruppe anzeigen -->
				<c:forEach var="beitrag" items="${ beitragList }">
					<page:beitrag beitrag="${ beitrag }" page="GruppenServlet&tab=beitraege&gruppenID=${gruppenID}"></page:beitrag>
				</c:forEach>
			</c:when>
			<c:when test="${tab == 'infos'}">
				<ul class="list-group">
					<li class="list-group-item">Beschreibung:<br>${gruppe.beschreibung}</li>
					<li class="list-group-item">gegründet am:<br>${gruppe.gruendung}</li>
					<li class="list-group-item">Administrator:<br>${gruppe.admin}</li>
				</ul>
				<form action="GruppenServlet?gruppenID=${gruppenID}&name=Verlassen" method="post">
					<button class="btn btn-danger" type="submit">Gruppe verlassen</button>
				</form>
			</c:when>
			<c:when test="${tab == 'mitglieder'}">
				<div class="row">
					<div class="col-md-3">
						<label class="schwarz" style="font-size: 14px;">Insgesamt: ${anzahlMitglieder}</label>
					</div>
					<div class="col-md-9">
						<label class="pull-right" style="font-size: 12px;">alphabetisch sortiert nach 
							<a class="blau" style="${vornameLink}" href="GruppenServlet?gruppenID=${gruppenID}&tab=mitglieder&sortByV=true">Vorname</a>
							 / 
							<a class="blau" style="${nachnameLink}" href="GruppenServlet?gruppenID=${gruppenID}&tab=mitglieder&sortByV=false">Nachname</a>
						</label>
					</div>
				</div><br>
				<!-- Alle Mitglieder der Gruppe -->
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
			<c:when test="${tab == 'bearbeiten'}">
				<div class="row">
					<label class="blau">Beschreibung ändern</label>
					<form action="GruppenServlet?tab=bearbeiten&gruppenID=${gruppenID}&name=BeschreibungBearbeiten" method="post">
						<textarea style="resize: none;" class="form-control" type="text" name="beschreibung" placeholder="Bitte beschreiben Sie den Zweck dieser Gruppe">${gruppe.beschreibung}</textarea>
						<button class="btn btn-success pull-right" type="submit">Speichern</button>
					</form>
				</div><br>
				<div class="row">
					<label class="blau">Mitglieder zufügen</label>
					<div class="row">
						<div class="col-md-1"></div>
						<form action="GruppenServlet?gruppenID=${gruppenID}&tab=bearbeiten&name=MitgliedZufuegen" method="post">
							<div class="col-md-4">
								<select class="form-control" name="mitgliedID">
									<c:forEach var="freund" items="${freunde}">
										<c:if test="${!mitglieder.contains(freund.userID)}">
											<option value="${freund.userID}">${freund.vorname} ${freund.nachname}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							<div class="col-md-1">
								<button class="btn containerColor" type="submit">OK</button>
							</div>
						</form>
					</div>
				</div><br>
				<div class="row">
					<label class="blau">Mitglieder entfernen</label>
					<div class="row">
						<div class="col-md-1"></div>
						<form action="GruppenServlet?gruppenID=${gruppenID}&tab=bearbeiten&name=MitgliedEntfernen" method="post">
							<div class="col-md-4">
								<select class="form-control" name="mitgliedID">
									<c:forEach var="mitglied" items="${mitglieder}">
										<c:if test="${mitglied.userID != gruppe.adminID }">
											<option value="${mitglied.userID}">${mitglied.vorname} ${mitglied.nachname}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							<div class="col-md-1">
								<button class="btn containerColor" type="submit">OK</button>
							</div>
						</form>
					</div>
				</div><br>
				<div class="row">
					<form action="GruppenServlet?tab=bearbeiten&gruppenID=${gruppenID}&name=GruppeLoeschen" method="post">
						<button class="btn btn-danger" type="submit">Gruppe löschen</button>
					</form>
				</div>
			</c:when>
		</c:choose>
	</page:mittlereSpalte>
	<page:rechteSpalte chatfreunde="${chatfreunde}"></page:rechteSpalte>
</div>
</body>
</html>