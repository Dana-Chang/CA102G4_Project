<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>

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
%>
<%
	//取得購物車商品數量
	Object total_items_temp = session.getAttribute("total_items");
	int total_items = 0;
	if(total_items_temp != null ){
		total_items= (Integer) total_items_temp;
	}
	pageContext.setAttribute("total_items",total_items);
%>


<%@ page import="com.fri.model.*,com.chat.model.*" %>
<jsp:useBean id="chatRoomSvc" scope="page" class="com.chat.model.ChatRoomService"></jsp:useBean>
<jsp:useBean id="chatRoomJoinSvc" scope="page" class="com.chat.model.ChatRoom_JoinService"></jsp:useBean>
<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService"></jsp:useBean>
<jsp:useBean id="friSvc" scope="page" class="com.fri.model.FriendService"></jsp:useBean>
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

<head>
	<!-- 網頁title -->
	<title>Travel Maker</title>
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
	<link href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css" rel="stylesheet" type="text/css" media="all" />
	<!-- //bootstrap-css -->
	<!-- css -->
	<link href="<%=request.getContextPath()%>/front_end/css/about_us/about_us.css" rel="stylesheet" type="text/css" media="all" />
	<link href="<%=request.getContextPath()%>/front_end/css/blog/blog.css" rel="stylesheet" type="text/css" media="all">
	<link href="<%=request.getContextPath()%>/front_end/css/content/content.css" rel="stylesheet" type="text/css" media="all">
	<!-- //css -->
	<!-- font-awesome icons -->
	<link href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" rel="stylesheet" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
	<!-- //font-awesome icons -->
	<!-- font字體 -->
	<link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
	<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
	<!-- //font字體 -->
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
	
	
	 <!-- 聊天相關CSS及JS -->
	 <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css">
	 <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script>
	 <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script>
	 <!-- //聊天相關CSS及JS -->
	 
	 <!-- 登入才會有的功能(檢舉、送出或接受交友邀請通知)-->
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
	<div class="banner about-bg">
		<div class="top-banner about-top-banner">
			<div class="container">
				<div class="top-banner-left">
					<ul>
						<li><i class="fa fa-phone" aria-hidden="true"></i>
							<a href="tel:034257387"> 03-4257387</a></li>
						<li><a href="mailto:TravelMaker@gmail.com"><i class="fa fa-envelope" aria-hidden="true"></i> TravelMaker@gmail.com</a></li>
					</ul>
				</div>
				<div class="top-banner-right">
					<ul>
						<li>
                         <!-- 判斷是否登入，若有登入將會出現登出按鈕 -->
                         <c:choose>
                          <c:when test="<%=login_state %>">
                           <a href="<%= request.getContextPath()%>/front_end/member/member.do?action=logout"><span class=" top_banner"><i class=" fas fa-sign-out-alt" aria-hidden="true"></i></span></a>
                          </c:when>
                          <c:otherwise>
                           <a href="<%= request.getContextPath()%>/front_end/member/mem_login.jsp"><span class="top_banner"><i class=" fa fa-user" aria-hidden="true"></i></span></a>
                          </c:otherwise>
                         </c:choose>
                         </li>
                        <li style="<%= logout %>"><a class="top_banner" href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_home.jsp"><i class="fa fa-user" aria-hidden="true"></i></a></li>
						<li><a class="top_banner" href="<%=request.getContextPath()%>/front_end/store/store_cart.jsp"><i class="fa fa-shopping-cart shopping-cart" aria-hidden="true"></i><span class="badge">${total_items}</span></a></li>
                        <li><a class="top_banner" href="#"><i class="fa fa-envelope" aria-hidden="true"></i></a></li>
                     </ul>
				</div>
				<div class="clearfix"> </div>
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
						<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">Menu						
							</button>
						<!-- //當網頁寬度太小時，導覽列會縮成一個按鈕 -->
						<!-- Collect the nav links, forms, and other content for toggling -->
						<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
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

							</ul>
						</div>
					</nav>
				</div>
				<div class="clearfix"> </div>
			</div>
		</div>
	</div>

	<!-- //banner -->
	<!-- contact -->
	<div class="contact">
		<div class="container">
			<div class="w3l-contact-heading">
				<h2>Contact Us</h2>
			</div>
			<div class="agile-contact-form">
				<div class="col-md-6 contact-form-left">
					<div class="contact-form-top">
						<h3>Travel Maker</h3>
						<h4>無論您因為任何原因或沒有原因，有話想跟Travel Maker說，歡迎隨時透過此功能與我們聯繫喔！</h4>
					</div>
					<div class="contact-address">
					<div style="font-size:20px">
						<ul>
							<li><i class="fas fa-phone" aria-hidden="true"></i> <span>03-4257387</span></li>
							<li><i class="fas fa-envelope-square" aria-hidden="true"></i> <span><a href="mailto:TravelMaker@gmail.com">TravelMaker@gmail.com</a></span></li>
							<li><i class="fas fa-map-marker-alt" aria-hidden="true"></i> <span>300 Zhongda Road, Zhongli District, Taoyuan City</span></li>
						</ul>
						</div>
					</div>
				</div>
				<div class="col-md-6 contact-form-right">
					<div class="contact-form-top">
						<h3>Send us a message</h3>
					</div>
					<div class="contact-form-grid">
						<form >
							<input type="text" name="Name" placeholder="Name" >
							<input type="email" name="Email" placeholder="Email" >
							<textarea name="Message" placeholder="Message" ></textarea>
							<button class="btn1">請參照上方必填資訊已e-mail的方式寄到信箱我們會盡快為您處理</button>
						</form>
					</div>
				</div>
				<div class="clearfix"> </div>
			</div>

		</div>
	</div>
	<!-- //contact -->
	<!-- different -->
	<div class="jarallax different">
		<div class="container">
			<div class="w3-different-heading">
			</div>
		</div>
	</div>
	<!-- //different -->
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
							<li><a href="https://www.facebook.com/InstaBuy.tw/"><i class="fab fa-facebook"></i></a></li>
							<li><a href="https://www.instagram.com/"><i class="fab fa-instagram"></i></a></li>
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
							<input type="email" id="mc4wp_email" name="EMAIL" placeholder="請輸入您的Email" required="">
							<input type="submit" value="訂閱">
						</form>
					</div>
				</div>
				<div class="clearfix"> </div>
			</div>
			<div class="copyright">
				<p>Copyright &copy; 2018 All rights reserved
					<a href="<%=request.getContextPath()%>/front_end/index.jsp" target="_blank" title="TravelMaker">TravelMaker</a>
				</p>
			</div>
		</div>
	</div>
	<!-- //footer -->
</body>

</html>
