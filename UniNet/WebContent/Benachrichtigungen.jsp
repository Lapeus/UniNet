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
<title>UniNet - Benachrichtungen</title>
</head>
<body>
<jsp:include page="/LadeChatAlleFreundeServlet"></jsp:include>
<page:kopfzeile userID="${id}"></page:kopfzeile>
<div class="mainPart">
<page:linkeSpalte>
</page:linkeSpalte>
<page:mittlereSpalte>
<div class="container">
	<form action='BenachrichtigungenServlet' method='post'>
		<hgroup class="mb20">
			<h1>Benachrichtigungen</h1>
			<h2 class="lead">Deine <strong class="text-danger">Freunschaftsanfragen</strong></h2>								
		</hgroup>
		<section class="col-xs-12 col-sm-6 col-md-12">
			<c:forEach items="${ Freunschaftsanfragen }" var="anfrage">
				<page:freundschaftsanfrage request="${ anfrage }"></page:freundschaftsanfrage>
			</c:forEach>
		</section>
		<hgroup class="mb20">
			<h2 class="lead">Deine <strong class="text-danger">Beitragsbenachrichtigungen</strong></h2>								
		</hgroup>
		<section class="col-xs-12 col-sm-6 col-md-12">
		</section>
	</form>
</div>
</page:mittlereSpalte>
<page:rechteSpalte chatfreunde="${ chatfreundeAlle }"></page:rechteSpalte>
</div>
</body>
</html>