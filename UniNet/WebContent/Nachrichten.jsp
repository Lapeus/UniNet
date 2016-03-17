<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="page" uri="/WEB-INF/TLD/pageTags.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
<link rel="stylesheet" href="/UniNet/CSS/chat.css">
<link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UniNet - Nachrichten</title>
</head>
<body>
<jsp:include page="/LadeChatFreundeServlet"></jsp:include>
<page:kopfzeile></page:kopfzeile>
<div class="mainPart">
<page:linkeSpalte use="standard"></page:linkeSpalte>
<page:mittlereSpalte>
<div class="row">
	<div class="msg-warp">
		 <div class="msg-wrap">
		 	<c:forEach items="${ nachrichten }" var="nachrichten">
		    	<page:nachricht nachricht="${ nachrichten }"></page:nachricht>
		 	</c:forEach>
		 </div>
		 <form action="NachrichtenServlet" method="POST">
			 <div class="send-wrap">
			     <textarea class="form-control send-message" rows="3" placeholder="Antworte jetzt..." name="nachricht"></textarea>
			 </div>
			 <div class="btn-panel">
			     <a href="NachrichtenServlet" class=" col-lg-3 btn send-message-btn " role="button" name="reload">Reload</a>
			     <input type="submit" class="col-lg-4 text-right btn send-message-btn pull-right" name="senden"/>
			 </div>
		 </form>
	</div>
</div>
</page:mittlereSpalte>
<page:rechteSpalte chatfreunde="${ chatfreunde }"></page:rechteSpalte>
</div>
</body>
</html>