<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.admin.model.*"%>

<!DOCTYPE html>
<html>

<%

// 	AdminVO adminVO = (AdminVO)session.getAttribute("adminVO"); 

// 	boolean login_state = false;
// 	Object login_state_temp = session.getAttribute("login_state");
// 	if (login_state_temp != null) {
// 		login_state = (boolean) login_state_temp;
// 	}
	
// 	if(login_state==true){
// 		response.sendRedirect("/CA102G4/back_end/back_index.jsp");
// 	}
	
%>

<!--head_start-->
<head>
<title>Travel Maker 後台</title>
<meta charset="UTF-8">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/all/bootstrap.css">
<!-- Latest compiled and minified JavaScript -->
<script src="<%=request.getContextPath()%>/back_end/js/all/bootstrap.js"></script>
<!-- font字體 -->
<link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Pacifico'
	rel='stylesheet' type='text/css'>
<!-- //font字體 -->
<style>
@import url(//fonts.googleapis.com/earlyaccess/notosanstc.css);

body {
	background-image: url(<%=request.getContextPath()%>/back_end/images/back_bg2.jpg);
	background-repeat: no-repeat;
	background-size: cover;
	background-position: right bottom;
	background-attachment: fixed;
	background-color: #151515;
	font-family: 'Oswald', 'Noto Sans TC', sans-serif;
	font-weight: 900;
}

#title_name {
	color: black;
	font-size: 2em;
	text-decoration: none;
	font-family: 'Pacifico', cursive;
}

.container {
	height: auto;
	width: 400px;
	background-color: rgba(255, 250, 240, 0.8);
	position: absolute;
	top: 50%;
	left: 50%;
	margin-top: -140px;
	margin-left: -200px;
	border-color: white;
	border-radius: 10px;
}
</style>
</head>
<!--head_end-->

<!--body_start-->
<body>
	<%-- 錯誤表列 --%>
		<c:if test="${not empty errorMsgs}">
		<font color='red'>請修正以下錯誤:
			<ul>
				<c:forEach var="message" items="${errorMsgs}">
					<li>${message}</li>
				</c:forEach>
			</ul>
		</font>
	</c:if>
	
	<div class="container">
	
		<br>
		<div style="text-align: center">
			<span class="col-sm-12"><span id="title_name">Travel
					marker</span> &nbsp; 後台登入系統</span><br>
		</div>

		<form method="post" action="<%=request.getContextPath()+"/admin.do"%>">

			<div class="form-group col-sm-12">
				<!--class=form-group 是讓每一個區塊不要太相近-->
				<label for="account">帳號</label><br> 
				<input id="account" type="text" class="form-control" name="admin_Account"  value="" required="required">
			</div>

			<div class="form-group col-sm-12">
				<label for="psw">密碼</label><br> 
				<input id="password" type="password" class="form-control" name="admin_Password"  value="" required="required">
			</div>

			<div class="form-group col-sm-12" style="text-align: center">
				<input type="hidden" name="action" value="login_Admin">
				<button type="submit" class="btn btn-success btn-block">登入</button>
			</div>

		</form>

	</div>
</body>
<!--body_end-->

</html>