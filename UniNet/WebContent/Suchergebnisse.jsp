<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="page" uri="/WEB-INF/TLD/pageTags.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
<link rel="stylesheet" href="/UniNet/CSS/suchergebnisse.css">
<link rel="icon" href="UniNet_Logo.ico">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UniNet - Suchergebnisse</title>
</head>
<body>
<jsp:include page="/LadeChatAlleFreundeServlet"></jsp:include>
<page:kopfzeile></page:kopfzeile>
<div class="mainPart">
<page:linkeSpalte>
</page:linkeSpalte>
<page:mittlereSpalte>
<div class="container">
	<form action='SuchergebnisseServlet' method='post'>
		<input type='hidden' name='search' value='${ search }'>
	    <hgroup class="mb20">
			<h1>Suchergebnisse</h1>
			<h2 class="lead"><strong class="text-danger">Personen</strong> für den Suchbegriff <strong class="text-danger">${ Suche }</strong></h2>								
		</hgroup>
	    <section class="col-xs-12 col-sm-6 col-md-12">
	    	<c:forEach items="${ Nutzerliste }" var="nutzer">
		    	<page:gesuchtNutzer user="${ nutzer }"></page:gesuchtNutzer>
			</c:forEach>
		</section>
		<hgroup class="mb20">
			<h2 class="lead"><strong class="text-danger">Gruppen</strong> für den Suchbegriff <strong class="text-danger">${ Suche }</strong></h2>								
		</hgroup>
		<section class="col-xs-12 col-sm-6 col-md-12">
	    	<c:forEach items="${ Gruppenliste }" var="gruppe">
		    	<page:gesuchtGruppe group="${ gruppe }"></page:gesuchtGruppe>
			</c:forEach>
		</section>
		<hgroup class="mb20">
			<h2 class="lead"><strong class="text-danger">Veranstaltungen</strong> für den Suchbegriff <strong class="text-danger">${ Suche }</strong></h2>								
		</hgroup>
		<section class="col-xs-12 col-sm-6 col-md-12">
	    	<c:forEach items="${ Veranstaltungenliste }" var="event">
		    	<page:gesuchtVeranstaltung veranstaltungen="${ event }"></page:gesuchtVeranstaltung>
			</c:forEach>
		</section>
	</form>
	<a class='verfasser' href='ProfilServlet?userID=2'>Marvin</a> <span>m&ouml;chte mit dir befreundet sein!</span>
</div>
</page:mittlereSpalte>
</div>
</body>
</html>