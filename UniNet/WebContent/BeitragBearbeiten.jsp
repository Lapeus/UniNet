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
<meta name="author" content="Leon Schaffert">
<title>UniNet - Beitrag von ${beitrag.name}</title>
</head>
<body>
	<page:localAdminKopfzeile></page:localAdminKopfzeile>
	<br>
	<br>
	<br>
	<br>
	<form
		action="AdminBeitraegeServlet?name=BeitragBearbeitet&beitragsID=${beitrag.beitragsID}"
		method="post" class="form-horizontal">
		<div class="col-sm-6">
			<textarea class="form-control" rows="5" name="neuerBeitrag">${beitrag.nachricht}</textarea>
		</div>
		<button type="submit" class="btn btn-success" name="registrieren">Ändern</button>
	</form>
</body>
</html>