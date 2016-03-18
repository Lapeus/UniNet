<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="page" uri="/WEB-INF/TLD/pageTags.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/UniNet/CSS/liveChat.css">
<link rel="stylesheet" href="/UniNet/CSS/bootstrap.css">
<link rel="stylesheet" href="/UniNet/CSS/page.css">
<script type="text/javascript" src="liveChat.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<title>Insert title here</title>
</head>
<script type="text/javascript">
$(
function() {
	$(".panel.panel-chat > .panel-heading > .chatMinimize")
			.click(
					function() {
						if ($(this).parent().parent().hasClass(
								'mini')) {
							$(this).parent().parent().removeClass(
									'mini').addClass('normal');

							$('.panel.panel-chat > .panel-body')
									.animate({
										height : "250px"
									}, 500).show();

							$('.panel.panel-chat > .panel-footer')
									.animate({
										height : "75px"
									}, 500).show();
						} else {
							$(this).parent().parent().removeClass(
									'normal').addClass('mini');

							$('.panel.panel-chat > .panel-body')
									.animate({
										height : "0"
									}, 500);

							$('.panel.panel-chat > .panel-footer')
									.animate({
										height : "0"
									}, 500);

							setTimeout(
									function() {
										$(
												'.panel.panel-chat > .panel-body')
												.hide();
										$(
												'.panel.panel-chat > .panel-footer')
												.hide();
									}, 500);

						}

					});
	$(".panel.panel-chat > .panel-heading > .chatClose").click(
			function() {
				$(this).parent().parent().remove();
			});
	})
</script>
<body>
<jsp:include page="/LadeChatAlleFreundeServlet"></jsp:include>
<page:kopfzeile></page:kopfzeile>
<page:linkeSpalte use="standard"></page:linkeSpalte>
<page:chat idUser="2" idFreund="3"></page:chat>
</body>

</html>