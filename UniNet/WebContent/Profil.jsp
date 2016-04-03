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
<link rel="icon" href="UniNet_Logo.ico">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UniNet - Profilseite</title>
</head>
<body>
<page:kopfzeile></page:kopfzeile>
<div class="mainPart">
	<!-- In der linken Spalte sollen das Bild und die persoenlichen Informationen angezeigt werden -->
	<page:linkeSpalte>
		<img class="img-responsive profilbild" alt="Profilbild" src="LadeProfilbildServlet?userID=${userID}">
		<label class="verfasser" style="text-align: center; width: 100%">Persönliche Infos</label>
		<ul class="list-group">
			<li class="list-group-item">Studiengang:<br>${ studiengang }</li>
			<li class="list-group-item">${ semester }. Semester</li>
			<li class="list-group-item">E-Mail:<br>${ email }</li>
			<!-- Hier wird der komplette li-Tag als Attribut gelesen, da er weggelassen wird, wenn das Attribut leer ist -->
			${ geburtstag }
			${ wohnort }
			${ hobbys }
			${ interessen }
			${ ueberMich }
		</ul>
		<!-- Wenn es das eigene Profil ist, darf man auch die Informationen bearbeiten -->
		<c:if test="${beitragPosten == true}">
			<form action="ProfilBearbeitenServlet" method="get">
				<button style="width: 100%;" type="submit" class="btn btn-success">Informationen bearbeiten</button>
			</form>
		</c:if>
	</page:linkeSpalte>
	<page:mittlereSpalte>
	<!-- Beitrag posten -->
	<div class="row"><div class="col-md-1"></div>
		<div class="col-md-10">
			<div class="row"><label class="profilName">${ name }</label></div><br>
			<c:if test="${beitragPosten == true}">
				<div class="row">
					<form action="ProfilServlet?name=BeitragPosten" method="post">
						<div class="form-group">
							<textarea class="form-control" rows="4" name="beitrag" placeholder="Was machst du gerade?" required></textarea>
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
					</form><br>
				</div>
			</c:if>
		</div>
	</div><br>
	<!-- Bisherige Beitraege anzeigen -->
	<c:forEach var="beitrag" items="${ beitragList }">
		<page:beitrag beitrag="${ beitrag }" page="ProfilServlet&userID=${userID}"></page:beitrag>
	</c:forEach>
	</page:mittlereSpalte>
	<page:rechteSpalte chatfreunde="${ chatfreunde }">
	</page:rechteSpalte>
</div>
</body>
</html>