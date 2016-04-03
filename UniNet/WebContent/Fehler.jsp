<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
<link rel="icon" href="UniNet_Logo.ico">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<jsp:include page="/AnmeldePassivServlet"></jsp:include>
<form class="form-inline containerColor">
	<div class="row containerColor"><br></div>
	<div class="col-md-2"></div>
	<div class="col-md-4">
		<div class="hSelf">UniNet - Sei auch du dabei</div>
	</div>
	<div class="form-group">
		
	</div>
	<div class="form-group">
		
	</div>
	<div class="checkbox checkboxWhite">
		
	</div>
	<button class="btn btn-default buttonLightBlue" style="visibility: hidden;">Platzhalter</button>
	<div class="row"><br></div>
</form>
<br><br><br><br>
<div class="row">
	<div class="col-md-2"></div>
	<div class="col-md-10">
		<label stlye="font-size: 25px"><span style="font-size: 30px">&#x26A0 </span> ${meldung}</label>
	</div>
</div>
<div class="row">
	<div class="col-md-2"></div>
	<div class="col-md-10">
		<a class="blau" href="${page}">zurück</a>
	</div>
</div>
</body>
</html>