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
<title>UniNet - Startseite</title>
</head>
<body>
<page:seitenAufbau chatfreunde="${chatfreunde}">
	<!-- Beitrag posten -->
	<div class="row">
		<div class="col-md-1"></div>
		<div class="col-md-10">
			<div class="row">
				<form action="ProfilServlet?id=${id}&name=BeitragPosten&page=StartseiteServlet" method="post">
					<div class="form-group">
						<textarea class="form-control" rows="4" name="beitrag" placeholder="Was machst du gerade?" required></textarea>
						<div class="form-inline pull-right">
							<!-- Sichtbarkeitsbutton -->
							<select class="form-control glyphicon" name="sichtbarkeit">
								<option>Privat &#xe008;</option>
								<option>Öffentlich &#xe135;</option>
							</select>
							<button type="submit" class="btn containerColor">Posten</button>
						</div><br>
					</div>
				</form>
			</div>
		</div>
	</div><br>
	<!-- Alle Beitraege anzeigen -->
	<c:forEach var="beitrag" items="${ beitragList }">
		<page:beitrag beitrag="${ beitrag }" page="StartseiteServlet"></page:beitrag>
	</c:forEach>
</page:seitenAufbau>
</body>
</html>