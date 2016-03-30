<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="page" uri="/WEB-INF/TLD/pageTags.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UniNet - Beitrag von ${beitrag.name}</title>
</head>
<body>
<page:seitenAufbau chatfreunde="${chatfreunde}">
	<div class="row">
		<div class="col-md-1"><!-- Leere Linke Spalte --></div>
		<div class="col-md-10 beitrag"> <!-- Mittelblock -->
			<c:choose>
				<c:when test="${beitragAnzeigen}">
					<div class="row kopf"><br>
						<div class="col-md-1">
							<a href='#'><img class="media-object kommentarbild profil" alt="Profilbild" src="LadeProfilbildServlet?userID=${beitrag.userID}"></a>
						</div>
						<div class="col-md-10">
							<c:choose>
								<c:when test="${beitrag.nichtChronik}">
									<a class="verfasser" href="ProfilServlet?userID=${beitrag.userID}">${beitrag.name}</a> <span class="glyphicon glyphicon-arrow-right" style="color: #3b5998;"></span>
									<a class="verfasser" href="#">${beitrag.ortName}</a><br>
								</c:when>
								<c:when test="${!beitrag.nichtChronik}">
									<a class="verfasser" href="ProfilServlet?userID=${beitrag.userID}">${beitrag.name}</a><br>
								</c:when>
							</c:choose>
							<label class="zeitstempel">${beitrag.timeStamp}</label>
						</div> <!-- col-md-10 -->
						<div class="col-md-1">
							<!-- Wenn es ein eigener Beitrag ist, darf er geloescht werden -->
							<c:if test="${beitrag.loeschenErlaubt}"> 
								<a href="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=BeitragLoeschen" title="Beitrag löschen"><span class='glyphicon glyphicon-remove-sign' style='color:#3b5998;'></span></a>
							</c:if>
						</div>
					</div> <!-- row kopf -->
					<label class="beitrag"><br>
						<!-- Der eigentliche Beitrag -->
						${beitrag.nachricht}
					</label><br><br>
					<div>
						<ul class="nav nav-pills border">
			  				<li role="presentation" class="like ${liClass}"><a href="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=Like">Interessiert mich nicht besonders</a></li>
			  				<li role="presentation"><a href="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=Melden">Melden</a></li>
			  				<li role="presentation" class="kommentareAnzeigen"><a href="#">Alle Kommentare anzeigen</a></li>
						</ul>
						<label class="anzahlLikes"><p><p>
							<a class="blau" href="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=LikesAnzeigen">${beitragLikesPersonen} interessiert das nicht besonders</a>
						</label>
						<label class="anzahlKommentare">${beitragKommentare}</label>
					</div>
					<div class="row">
						<div class="col-md-1">
							<a href='#'><img class="media-object kommentarbild" alt="Profilbild" src="LadeProfilbildServlet?userID=${userID}"></a>
						</div>
						<div class="col-md-11">
							<form action="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=Kommentar" method="post">
								<input type="text" class="form-control" name="kommentar" placeholder="Schreibe einen Kommentar...">
							</form>
						</div>
					</div><p>
					<!-- Hier werden alle Kommentare geladen -->
					<c:forEach var="kommentar" items="${beitrag.kommentarList}">
						<page:kommentar kommentar="${kommentar}"></page:kommentar>
					</c:forEach>
					<!-- Unterscheidung, ob das Antworttextfeld angezeigt werden soll, oder nicht -->
					<c:if test="${anzeigen}">
						<p><div class="row">
							<div class="col-md-1">
								<a><img class="media-object kommentarbild" alt="Profilbild" src="LadeProfilbildServlet?"></a>
							</div>
							<div class="col-md-11">
								<form action="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=KommentarAntwort&tiefe=${tiefe}&kommID=${kommID}" method="post">
									<input autofocus type="text" class="form-control" name="kommentar" placeholder="Schreibe einen Kommentar...">
								</form>
							</div> <!-- col-md-11 -->
						</div> <!-- row --><p>
					</c:if>
				</c:when>
				<c:when test="${!beitragAnzeigen}">
					<br><a class="blau" href="BeitragServlet?beitragsID=${beitragsID}">zurück zum Beitrag</a>
					<p><div class="row">
						<div class="col-md-3">
							<label class="schwarz" style="font-size: 14px;">Insgesamt: ${anzahl}</label>
						</div>
						<div class="col-md-9">
							<label class="pull-right" style="font-size: 12px;">sortiert nach 
								<a class="blau" style="${vornameLink}" href="BeitragServlet?beitragsID=${beitragsID}&name=LikesAnzeigen&sortBy=Vorname">Vorname</a>
								 / 
								<a class="blau" style="${nachnameLink}" href="BeitragServlet?beitragsID=${beitragsID}&name=LikesAnzeigen&sortBy=Nachname">Nachname</a>
								 / 
								<a class="blau" style="${zeitLink}" href="BeitragServlet?beitragsID=${beitragsID}&name=LikesAnzeigen&sortBy=Zeit">Zeitpunkt</a>
							</label>
						</div>
					</div><br>
					<p><c:forEach var="user" items="${user}">
						<a class="mitglieder" href="ProfilServlet?userID=${user.userID}"><b>${user.vorname} ${user.nachname}</b></a><br>
					</c:forEach>
				</c:when>
			</c:choose>		
		</div> <!-- beitrag -->
	</div> <!-- row -->
</page:seitenAufbau>
</body>
</html>