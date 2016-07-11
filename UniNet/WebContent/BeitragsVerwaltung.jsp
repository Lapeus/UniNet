<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="/WEB-INF/TLD/pageTags.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
<link rel="icon" href="UniNet_Logo.ico">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="author" content="Leon Schaffert">
<title>UniNet - Beitr&auml;ge verwalten </title>
</head>
<body>
<page:localAdminKopfzeile></page:localAdminKopfzeile>
<br><br><br><br>
<c:forEach var="beitrag" items="${ beitragList }">
		<page:gemeldeterBeitrag gemeldeterBeitrag="${ beitrag }" page="AdminBeitraegeServlet"></page:gemeldeterBeitrag>
	</c:forEach>
</body>
</html>