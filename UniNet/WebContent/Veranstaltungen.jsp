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
<title>UniNet - Veranstaltungen</title>
</head>
<body>
<jsp:include page="/LadeChatFreundeServlet"></jsp:include>
<page:kopfzeile></page:kopfzeile>
<div class="mainPart">
	<page:linkeSpalte>
		<label class="verfasser"><a class="verfasser" href="VeranstaltungenServlet?name=Uebersicht">Veranstaltungen</a></label>
		<ul class="nav nav-pills nav-stacked">
			<c:forEach var="veranstaltung" items="${veranstaltungList}">
				<li role="presentation"><a href="VeranstaltungenServlet?tab=${tab}&id=${veranstaltung.id}">${veranstaltung.name}</a></li>
			</c:forEach>
		</ul>
	</page:linkeSpalte>
	<page:mittlereSpalte>
		<label class="verfasser">${veranstaltung.name}</label><br><br>
		<ul class="nav nav-tabs">
			<c:if test="${eingetragen}">
				<li role="presentation" class="${beitraegeActive}"><a href="VeranstaltungenServlet?tab=beitraege&id=${id}">Beiträge</a></li>
			</c:if>
			<li role="presentation" class="${infosActive}"><a href="VeranstaltungenServlet?tab=infos&id=${id}">Infos</a></li>
			<li role="presentation" class="${mitgliederActive}"><a href="VeranstaltungenServlet?tab=mitglieder&id=${id}">Mitglieder</a></li>
		</ul>
		<br>
		<c:choose>
			<c:when test="${tab == 'beitraege' && eingetragen}">
				<div class="row"><div class="col-md-1"></div>
					<div class="col-md-10"><div class="row">
					<form action="VeranstaltungenServlet?id=${id}&name=BeitragPosten" method="post">
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
				<c:forEach var="beitrag" items="${ beitragList }">
					<page:beitrag beitrag="${ beitrag }" page="VeranstaltungenServlet"></page:beitrag>
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
						<form action="VeranstaltungenServlet?name=Einschreiben" method="post">
							<button class="btn btn-success" type="submit">Einschreiben</button>
						</form>
					</c:when>
					<c:when test="${eingetragen}">
						<form action="VeranstaltungenServlet?name=Ausschreiben" method="post">
							<button class="btn btn-danger" type="submit">Ausschreiben</button>
						</form>
					</c:when>
				</c:choose>
			</c:when>
			<c:when test="${tab == 'mitglieder'}">
				Tolle Mitglieder
			</c:when>
		</c:choose>
	</page:mittlereSpalte>
	<page:rechteSpalte chatfreunde="${chatfreunde}"></page:rechteSpalte>
</div>
</body>
</html>