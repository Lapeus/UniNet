<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="page" uri="/WEB-INF/TLD/pageTags.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UniNet - Profil bearbeiten</title>
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
</head>
<body>
<jsp:include page="/LadeChatFreundeServlet"></jsp:include>
<page:seitenAufbau chatfreunde="${chatfreunde}">
	<form action="ProfilBearbeitenServlet" method="post">
		<div class="row">
			<h4>Name*</h4>
			<div class="col-md-3">
				<input type="text" name="vorname" class="form-control" value="${vorname}" required>
			</div>
			<div class="col-md-3">
				<input type="text" name="nachname" class="form-control" value="${nachname}" required>
			</div>
		</div>
		<div class="row">
			<h4>Geburtstag</h4>
			<div class="col-md-6">
				<input type="text" name="geburtstag" class="form-control" value="${geburtstag}">
			</div>
		</div>
		<div class="row">
			<h4>Wohnort</h4>
			<div class="col-md-6">
				<input type="text" name="wohnort" class="form-control" value="${wohnort}">
			</div>
		</div>
		<div class="row">
			<h4>Hobbys</h4>
			<div class="col-md-6">
				<input type="text" name="hobbys" class="form-control" value="${hobbys}">
			</div>
		</div>
		<div class="row">
			<h4>Interessen</h4>
			<div class="col-md-6">
				<input type="text" name="interessen" class="form-control" value="${interessen}">
			</div>
		</div>
		<div class="row">
			<h4>Über mich</h4>
			<div class="col-md-6">
				<textarea style="resize: none;" type="text" row=2 name="ueberMich" class="form-control">${ueberMich}</textarea>
			</div>
		</div>
		<div class="row"><div class="col-md-6">
			<button type="submit" style="margin-top: 5px;" class="btn btn-success pull-right">Änderungen sichern</button>
		</div></div>
	</form>
</page:seitenAufbau>
</body>
</html>