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
<title>UniNet - Gruppen</title>
</head>
<body>
<page:kopfzeile></page:kopfzeile>
<div class="mainPart">
	<!-- In der linken Spalte sollen alle eigenen Gruppen angezeigt werden -->
	<page:linkeSpalte>
		<label class="verfasser" style="width:100%; text-align: center;">Gruppen</label>
		<ul class="nav nav-pills nav-stacked" style="background-color: white;">
			<c:forEach var="gruppe" items="${gruppenList}">
				<li role="presentation">
					<a class="schwarz" href="GruppenServlet?tab=beitraege&gruppenID=${gruppe.id}">${gruppe.name}</a>
				</li>
			</c:forEach>
		</ul>
	</page:linkeSpalte>
	<page:mittlereSpalte>
		<label class="verfasser">Gründe eine neue Gruppe</label>
		<p><div class="row">
			<form action="GruppenServlet?name=Gruenden" method="post">
				<div class="col-md-4">
					<input type="text" class="form-control" name="gruppenname" placeholder="Bitte Gruppennamen eingeben...">
				</div>
				<div class="col-md-2">
					<button type="submit" class="btn btn-success">Gruppe gründen</button>
				</div>
			</form>
		</div>
	</page:mittlereSpalte>
	<page:rechteSpalte chatfreunde="${chatfreunde}"></page:rechteSpalte>
</div>
</body>
</html>