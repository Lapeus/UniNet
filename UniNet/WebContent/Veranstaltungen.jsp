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
	 Dies ist ein toller Test für die linke Spalte
	</page:linkeSpalte>
	<page:mittlereSpalte>
		<ul class="nav nav-tabs">
			<li role="presentation" class="${beitraegeActive}"><a href="VeranstaltungenServlet?tab=beitraege">Beiträge</a></li>
			<li role="presentation" class="${infosActive}"><a href="VeranstaltungenServlet?tab=infos">Infos</a></li>
			<li role="presentation" class="${mitgliederActive}"><a href="VeranstaltungenServlet?tab=mitglieder">Mitglieder</a></li>
		</ul>
		<c:choose>
			<c:when test="${tab == 'beitraege'}">
				<!--<c:forEach var="beitrag" items="${ beitragList }">
					<page:beitrag beitrag="${ beitrag }" page="VeranstaltungenServlet"></page:beitrag>
				</c:forEach>-->
				Tolle Beiträge
			</c:when>
			<c:when test="${tab == 'infos'}">
				Tolle Infos
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