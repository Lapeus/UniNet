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
<title>UniNet - Startseite</title>
</head>
<body>
<page:kopfzeile></page:kopfzeile>
<page:seitenAufbau>
<!-- Seiteninhalt einfuegen -->
<c:forEach var="beitrag" items="${ beitragList }">
	<page:beitrag beitrag="${ beitrag }"></page:beitrag>
</c:forEach>
</page:seitenAufbau>
</body>
</html>