<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.attractions.model.*"%>
<%@ page import="com.mem.model.*"%>
 
<%
	MemberVO memberVO = (MemberVO)session.getAttribute("memberVO");
	String login,logout;
	if(memberVO != null){		
		login = "display:none;";
		logout = "display:'';";
	}else{
		login = "display:'';";
		logout = "display:none;";
		 }
	
	boolean login_state = false;
	Object login_state_temp = session.getAttribute("login_state");
	if(login_state_temp!=null){
		login_state=(boolean)login_state_temp;
	}
	
	//購物車
	Object total_items_temp = session.getAttribute("total_items");
	int total_items = 0;
	if(total_items_temp != null ){
		total_items= (Integer) total_items_temp;
	}
	pageContext.setAttribute("total_items",total_items);

	String reqAtt;
	if(session.getAttribute("att_no")==null){
		reqAtt = request.getParameter("att_no");
	}else{
		reqAtt = String.valueOf(session.getAttribute("att_no"));
	}
	
	if(request.getParameter("att_no")==null){
		response.sendRedirect(request.getContextPath()+"/front_end/attractions/att.jsp");
		return;
	}
	AttractionsService attSvc = new AttractionsService();
	AttractionsVO attVO = attSvc.getOneAttByPK(reqAtt);
	pageContext.setAttribute("attVO", attVO);
%>
<%@ page import="com.fri.model.*,com.chat.model.*" %>
<jsp:useBean id="chatRoomSvc" scope="page" class="com.chat.model.ChatRoomService"></jsp:useBean>
<jsp:useBean id="chatRoomJoinSvc" scope="page" class="com.chat.model.ChatRoom_JoinService"></jsp:useBean>
<jsp:useBean id="friSvc" scope="page" class="com.fri.model.FriendService"></jsp:useBean>
<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService"></jsp:useBean>
<%
	if(memberVO != null){
		//*****************聊天用：取得登錄者所參與的群組聊天*************/
		List<ChatRoom_JoinVO> myCRList =chatRoomJoinSvc.getMyChatRoom(memberVO.getMem_Id());
		Set<ChatRoom_JoinVO> myCRGroup = new HashSet<>(); //裝著我參與的聊天對話為群組聊天時
		
		for(ChatRoom_JoinVO myRoom : myCRList){
			//查詢我參與的那間聊天對話，初始人數是否大於2?? 因為這樣一定就是群組聊天
			int initJoinCount = chatRoomSvc.getOne_ByChatRoomID(myRoom.getChatRoom_ID()).getChatRoom_InitCNT();
			if(initJoinCount > 2){
				myCRGroup.add(myRoom);
			}
		}
		pageContext.setAttribute("myCRList", myCRGroup);
		
		/***************聊天用：取出會員的好友******************/
		List<Friend> myFri = friSvc.findMyFri(memberVO.getMem_Id(),2); //互相為好友的狀態
		pageContext.setAttribute("myFri",myFri);
		
		/**************避免聊天-新增群組重新整理後重複提交********/
		session.setAttribute("addCR_token",new Date().getTime());

	}

%>

<html>
<head>
<!-- 網頁title -->
<title>${attVO.att_name}</title>
<!-- //網頁title -->
<!-- 指定螢幕寬度為裝置寬度，畫面載入初始縮放比例 100% -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- //指定螢幕寬度為裝置寬度，畫面載入初始縮放比例 100% -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- 設定網頁keywords -->
<meta name="keywords" content="TravelMaker,Travelmaker,自助旅行" />
<!-- //設定網頁keywords -->
<!-- 隱藏iPhone Safari位址列的網頁 -->
<script type="application/x-javascript">
        addEventListener("load", function() {
        setTimeout(hideURLbar, 0);
        }, false);
        function hideURLbar() {
        window.scrollTo(0, 1);
        }
</script>
<!-- //隱藏iPhone Safari位址列的網頁 -->
<!-- bootstrap-css -->
<link
	href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<!-- //bootstrap-css -->

<!-- css -->
<link
	href="<%=request.getContextPath()%>/front_end/css/attractions/att_style.css"
	rel="stylesheet" type="text/css" media="all" />

<link
	href="<%=request.getContextPath()%>/front_end/css/attractions/attDetail.css"
	rel="stylesheet">
<!-- //css -->

<!-- font-awesome icons -->
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
	crossorigin="anonymous">
<!-- //font-awesome icons -->

<!-- font字體 -->
<link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Pacifico'
	rel='stylesheet' type='text/css'>
<!-- //font字體 -->

<!-- LogoIcon -->
<link href="<%=request.getContextPath()%>/front_end/images/all/Logo_Black_use.png" rel="icon" type="image/png">
<!-- //LogoIcon -->

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script
	src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
	
	<!-- 聊天相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css">
    <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script>
    <!-- //聊天相關CSS及JS -->
	
	<!-- 登入才會有的功能(聊天)-->
	<c:if test="${memberVO != null}">
		<%@ include file="/front_end/personal_area/chatModal_JS.file" %>
	</c:if>


</head>
<body>
    <%-- 錯誤表列 --%>
	<c:if test="${not empty errorMsgs_Ailee}">
		<div class="modal fade" id="errorModal_Ailee">
		    <div class="modal-dialog modal-sm" role="dialog">
		      <div class="modal-content">
		        <div class="modal-header">
		          <i class="fas fa-exclamation-triangle"></i>
		          <span class="modal-title"><h4>&nbsp;注意：</h4></span>
		        </div>
		        <div class="modal-body">
					<c:forEach var="message" items="${errorMsgs_Ailee}">
						<li style="color:red" type="square">${message}</li>
					</c:forEach>
		        </div>
		        <div class="modal-footer">
		          <button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
		        </div>
		      </div>
		    </div>
		 </div>
	</c:if>
	<%-- 錯誤表列 --%>
	
	<!-- banner -->
	<div class="banner-bg">
		<div class="banner about-bg">
			<div class="top-banner about-top-banner">
				<div class="container">
					<div class="top-banner-left">
						<ul>
							<li><i class="fa fa-phone" aria-hidden="true"></i> <a
								href="tel:034257387"> 03-4257387</a></li>
							<li><a href="mailto:TravelMaker@gmail.com"><i
									class="fa fa-envelope" aria-hidden="true"></i>
									TravelMaker@gmail.com</a></li>
						</ul>
					</div>
					<div class="top-banner-right">
						<ul>
							<li>
								<!-- 判斷是否登入，若有登入將會出現登出按鈕 -->
								<c:choose>
									<c:when test="<%=login_state%>">
										<a href="<%=request.getContextPath()%>/front_end/member/member.do?action=logout"><span
											class=" top_banner"><i class=" fas fa-sign-out-alt" aria-hidden="true"></i></span></a>
									</c:when>
									<c:otherwise>
										<a href="<%=request.getContextPath()%>/front_end/member/mem_login.jsp"><span
											class="top_banner"><i class=" fa fa-user" aria-hidden="true"></i></span></a>
									</c:otherwise>
								</c:choose>
							</li>
							<li style="<%=logout%>"><a class="top_banner"
								href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_home.jsp">
								<i class="fa fa-user" aria-hidden="true"></i></a></li>
							<li><a class="top_banner" href="<%=request.getContextPath()%>/front_end/store/store_cart.jsp"><i class="fa fa-shopping-cart shopping-cart" aria-hidden="true"></i><span class="badge">${total_items}</span></a></li>
							<li><a class="top_banner" href="#">
								<i class="fa fa-envelope" aria-hidden="true"></i></a></li>
						</ul>
					</div>
					<div class="clearfix"></div>
				</div>
			</div>
			<div class="header">
				<div class="container">
					<div class="logo">
						<h1>
							<a href="<%=request.getContextPath()%>/front_end/index.jsp">Travel Maker</a>
						</h1>
					</div>
					<div class="top-nav">
						<!-- 當網頁寬度太小時，導覽列會縮成一個按鈕 -->
						<nav class="navbar navbar-default">
							<div class="navbar-header">
								<button type="button" class="navbar-toggle collapsed"
									data-toggle="collapse" data-target="#navbar-collapse-menu">Menu
								</button>
							</div>
							<!-- //當網頁寬度太小時，導覽列會縮成一個按鈕 -->
							<!-- Collect the nav links, forms, and other content for toggling -->
							<div class="collapse navbar-collapse" id="navbar-collapse-menu">
								<ul class="nav navbar-nav">
									<li><a href="<%=request.getContextPath()%>/front_end/news/news.jsp">最新消息</a></li>
	                                <li><a href="<%=request.getContextPath()%>/front_end/attractions/att.jsp">景點介紹</a></li>
	                                <li><a href="<%=request.getContextPath()%>/front_end/trip/trip.jsp">行程規劃</a></li>
	                                <li><a href="<%=request.getContextPath()%>/blog.index">旅遊記</a></li>
	                                <li><a href="<%=request.getContextPath()%>/front_end/question/question.jsp">問答區</a></li>
	                                <li><a href="<%=request.getContextPath()%>/front_end/photowall/photo_wall.jsp">照片牆</a></li>
	                                <li><a href="<%=request.getContextPath()%>/front_end/grp/grpIndex.jsp">揪團</a></li>
	                                <li><a href="<%=request.getContextPath()%>/front_end/store/store.jsp">交易平台</a></li>
	                                <li><a href="<%=request.getContextPath()%>/front_end/ad/ad.jsp">專欄</a></li>
									<div class="clearfix"></div>
								</ul>
							</div>
						</nav>
					</div>
					<div class="clearfix"></div>
				</div>
			</div>
		</div>
	</div>
	<!-- //banner -->

	<div class="ui container">

		<!-- 我是隔板 -->
		<div class="ui hidden divider"></div>
		<!-- //我是隔板 -->

		
		<div class="img-container">
		<div class="row">
			<div class="col-lg-8 col-md-8 col-sm-8 col-8" align="left">
				<h1>${attVO.att_name}</h1>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-4 col-4" align="right">
				<form method="post" action="<%= request.getContextPath()%>/attEdit/attEdit.do">
					<button class="btn btn-primary"  type="submit">
	                    	我要編輯  <i class="fas fa-user-edit"></i>
	                </button>
	                <input type="hidden" name="action" value="userEdit">
	                <input type="hidden" name="att_no" value="${attVO.att_no}">
                </form>
			</div>
		</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-6 col-6">
					<div class="polaroid">
						<img
							src="<%= request.getContextPath()%>/trip/getPicture.do?att_no=${attVO.att_no}"
							alt="${attVO.att_name}" style="width: 100%">
					</div>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-4 col-4 col-lg-offset-2 col-md-offset-2 col-sm-offset-2 col-offset-2">
					<div class="table-responsive">
						<table class="table table-bordered table-hover">
							<tr>
								<th>國家</th>
								<td>${attVO.country}</td>
							</tr>
							<tr>
								<th>區域</th>
								<td>${attVO.administrative_area}</td>
							</tr>
							<tr>
								<th>緯度</th>
								<td>${attVO.att_lat}</td>
							</tr>
							<tr>
								<th>經度</th>
								<td>${attVO.att_lon}</td>
							</tr>
							<tr>
								<th>地址</th>
								<td>${attVO.att_address}</td>
							</tr>
						</table>
					</div>

				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12 col-md-12 col-sm-12 col-12">
				<h3>景點介紹</h3>
				<hr>
				<p>
				<c:out value="${attVO.att_information}" default="尚無內容"></c:out>
				</p>
				<hr>
			</div>
		</div>
		<!-- 我是隔板 -->
		<div class="ui hidden divider"></div>
		<!-- //我是隔板 -->
	</div>

	<!-- footer -->

	<div class="footer">
		<div class="container">
			<div class="footer-grids">
				<div class="col-md-3 footer-grid">
					<div class="footer-grid-heading">
						<h4>關於我們</h4>
					</div>
					<div class="footer-grid-info">
						<ul>
							<li><a href="<%=request.getContextPath()%>/front_end/about_us/about_us.jsp">關於Travel Maker</a></li>
							<li><a href="<%=request.getContextPath()%>/front_end/content/content.jsp">聯絡我們</a></li>
							<li><a href="<%=request.getContextPath()%>/front_end/faq/faq.jsp">常見問題</a></li>
						</ul>
					</div>
				</div>
				<div class="col-md-3 footer-grid">
					<div class="footer-grid-heading">
						<h4>網站條款</h4>
					</div>
					<div class="footer-grid-info">
						<ul>
							<li><a href="about.html">服務條款</a></li>
							<li><a href="services.html">隱私權條款</a></li>
						</ul>
					</div>
				</div>
				<div class="col-md-3 footer-grid">
					<div class="footer-grid-heading">
						<h4>社群</h4>
					</div>
					<div class="social">
						<ul>
							<li><a href="https://www.facebook.com/InstaBuy.tw/"><i
									class="fab fa-facebook"></i></a></li>
							<li><a href="https://www.instagram.com/"><i
									class="fab fa-instagram"></i></a></li>
							<li><a href="#"><i class="fab fa-line"></i></a></li>
						</ul>
					</div>
				</div>
				<div class="col-md-3 footer-grid">
					<div class="footer-grid-heading">
						<h4>訂閱電子報</h4>
					</div>
					<div class="footer-grid-info">
						<form action="#" method="post">
							<input type="email" id="mc4wp_email" name="EMAIL"
								placeholder="請輸入您的Email" required=""> <input
								type="submit" value="訂閱">
						</form>
					</div>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="copyright">
				<p>
					Copyright &copy; 2018 All rights reserved <a href="<%=request.getContextPath()%>/front_end/index.jsp"
						target="_blank" title="TravelMaker">TravelMaker</a>
				</p>
			</div>
		</div>
	</div>
	<!-- //footer -->
</body>
</html>