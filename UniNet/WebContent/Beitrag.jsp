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
<jsp:include page="/LadeChatFreundeServlet"></jsp:include>
<page:seitenAufbau chatfreunde="${chatfreunde}">
<div class="row">
	<div class="col-md-1"></div>
	<div class="col-md-10 beitrag">
		<div class="row kopf"><br>
			<div class="col-md-1">
				<a href='#'><img class="media-object kommentarbild profil" alt="Testbild" src="Testbild.jpg"></a>
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
				<a href="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=BeitragLoeschen" title="Beitrag löschen">${beitrag.loeschenErlaubt}</a>
			</div>
		</div> <!-- row kopf -->
		<label class="beitrag"><br>
			${beitrag.nachricht}
		</label><br><br>
		<div>
			<ul class="nav nav-pills border">
  				<li role="presentation" class="like ${liClass}"><a href="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=Like">Interessiert mich nicht besonders</a></li>
  				<li role="presentation"><a href="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=Melden">Melden</a></li>
  				<li role="presentation" class="kommentareAnzeigen"><a href="#">Alle Kommentare anzeigen</a></li>
			</ul>
			<label class="anzahlLikes"><p><p>${beitrag.likes} Personen interessiert das nicht besonders</label>
			<label class="anzahlKommentare">${beitrag.kommentare} Kommentare</label></div>
			<div class="row">
				<div class="col-md-1">
					<a href='#'><img class="media-object kommentarbild" alt="Testbild" src="Testbild.jpg"></a>
				</div>
				<div class="col-md-11">
					<form action="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=Kommentar" method="post">
						<input type="text" class="form-control" name="kommentar" placeholder="Schreibe einen Kommentar...">
					</form>
				</div> <!-- col-md-11 -->
			</div> <!-- row --><p>
			<c:forEach var="kommentar" items="${beitrag.kommentarList}">
				<page:kommentar kommentar="${kommentar}"></page:kommentar>
			</c:forEach>
			<page:kommentarAntwort anzeigen="${anzeigen}">
				<div class="row">
					<div class="col-md-1">
						<a><img class="media-object kommentarbild" alt="Testbild" src="Testbild.jpg"></a>
					</div>
					<div class="col-md-11">
						<form action="BeitragServlet?beitragsID=${beitrag.beitragsID}&name=KommentarAntwort&tiefe=${tiefe}&kommID=${kommID}" method="post">
							<input autofocus type="text" class="form-control" name="kommentar" placeholder="Schreibe einen Kommentar...">
						</form>
					</div> <!-- col-md-11 -->
				</div> <!-- row --><p>
			</page:kommentarAntwort>
		</div> <!-- beitrag -->
	</div> <!-- row -->
</page:seitenAufbau>
</body>
</html>