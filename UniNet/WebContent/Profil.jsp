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
<title>UniNet - Profilseite</title>
</head>
<jsp:include page="/LadeChatFreundeServlet"></jsp:include>
<body>
<page:kopfzeile>
</page:kopfzeile>
<div class="mainPart">
	<div class="linkeSpalte">
		<img class="img-responsive profilbild" alt="Testbild" src="Testbild.jpg">
		<label class="verfasser">Persönliche Infos</label>
		<ul class="list-group">
			<li class="list-group-item">Universität:<br>${ uni }</li>
			<li class="list-group-item">Studiengang:<br>${ studiengang }</li>
			<li class="list-group-item">Studienbeginn:<br>${ studienbeginn }</li>
			<li class="list-group-item">Freunde: ${ anzFreunde }</li>
		</ul>
	</div>
	
	<page:mittlereSpalte>
	<div class="row">
		<div class="col-md-1"></div>
		<div class="col-md-10">
			<div class="row"><label class="profilName">${ name }</label></div><br>
			<div class="row">
				<form action="ProfilServlet?name=BeitragPosten" method="post">
					<div class="form-group">
						<textarea class="form-control" rows="4" name="beitrag" placeholder="Was machst du gerade?" required></textarea>
						<div class="form-inline pull-right">
							<!-- Sichtbarkeitsbutton -->
							<select class="form-control" name="sichtbarkeit">
								<option>Freunde</option>
								<option>Öffentlich</option>
							</select>
							<button type="submit" class="btn btn-default">Posten</button>
						</div>
						<br>
					</div>
				</form><br>
			</div>
		</div>
	</div><br>
	<c:forEach var="beitrag" items="${ beitragList }">
		<page:beitrag beitrag="${ beitrag }" page="ProfilServlet"></page:beitrag>
	</c:forEach>
	</page:mittlereSpalte>
	
	<page:rechteSpalte chatfreunde="${ chatfreunde }">
	</page:rechteSpalte>
</div>
</body>
</html>