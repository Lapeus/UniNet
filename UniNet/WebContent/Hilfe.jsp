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
<title>UniNet - Emoticons</title>
</head>
<body>
<page:seitenAufbau chatfreunde="${chatfreunde}">
	<label class="verfasser">Alle verfügbaren Emoticons</label><br>
	<page:emoticons firstRow="true" listLinks="${emoticons1}" listRechts="${emoticons2}"></page:emoticons>
	<page:emoticons listLinks="${emoticons3}" listRechts="${emoticons4}"></page:emoticons>
	<page:emoticons listLinks="${emoticons5}" listRechts="${emoticons6}"></page:emoticons>
	<page:emoticons listLinks="${emoticons7}" listRechts="${emoticons8}"></page:emoticons>
	<page:emoticons listLinks="${emoticons9}" listRechts="${emoticons10}"></page:emoticons>
	<page:emoticons listLinks="${emoticons11}" listRechts="${emoticons12}"></page:emoticons>
	<page:emoticons listLinks="${emoticons13}" listRechts="${emoticons14}"></page:emoticons>
	<page:emoticons listLinks="${emoticons15}" listRechts="${emoticons16}"></page:emoticons>
</page:seitenAufbau>
</body>
</html>