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
<meta name="author" content="Christian Ackermann">
<title>UniNet - ${hashtag}</title>
</head>
<body>
<page:seitenAufbau chatfreunde="${chatfreunde}" userID="${userID}">
	<div class="row">
		<div class="col-md-1"></div>
		<div class="col-md-11">
			<label class="verfasser">Beiträge zu #${hashtag}</label><br><br>
		</div>
	</div>
	<c:forEach var="beitrag" items="${beitragList}">
		<page:beitrag beitrag="${beitrag}" page="HashtagServlet?tag=${hashtag}"></page:beitrag>
	</c:forEach>
</page:seitenAufbau>
</body>
</html>