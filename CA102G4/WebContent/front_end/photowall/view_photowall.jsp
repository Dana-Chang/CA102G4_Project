<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.photo_wall.model.*"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.photo_wall_like.model.*"%>
<%@ page import="com.photo_tag_list.model.*"%>
<%@ page import="com.photo_tag.model.*"%>

<%

	MemberService memSvc = new MemberService();
	

	MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
	String login, logout;
	if (memberVO != null) {
		login = "display:none;";
		logout = "display:'';";
	} else {
		login = "display:'';";
		logout = "display:none;";
	}

	boolean login_state = false;
	Object login_state_temp = session.getAttribute("login_state");
	if (login_state_temp != null) {
		login_state = (boolean) login_state_temp;
	}
	
	/***************取出登入者會員資訊******************/
	if(memberVO == null){
	  //如果沒取到某會員的ID，先讓他導向到照片牆首頁
	  response.sendRedirect(request.getContextPath()+"/front_end/photowall/photo_wall.jsp");
	  return;
	 }
	
	String memId = ((MemberVO)session.getAttribute("memberVO")).getMem_Id();
	
	String photo_No = request.getParameter("photo_No");
	
	System.out.println("登入者的="+memId);
	
	System.out.println("照片牆的="+photo_No);
	
	Photo_wallService photo_wallSvc = new Photo_wallService();
	
	Photo_wallVO photo_wallVO = photo_wallSvc.findByPrimaryKey(photo_No);
	pageContext.setAttribute("photo_wallVO", photo_wallVO);
	
	System.out.println(photo_wallVO);
	
	
	Photo_tag_listService photo_tag_listSvc = new Photo_tag_listService();
	List<Photo_tag_listVO> list = photo_tag_listSvc.getAll_Photo_No(photo_No);
	pageContext.setAttribute("list", list);
	
	MemberVO memVO = memSvc.findByPrimaryKey(photo_wallVO.getMem_Id());
	// 	 	System.out.println(memVO.getEncoded());   //檢查照片是否有編碼	
	//	 	System.out.println(memVO.getMem_Id()); 	  //取得照片牆的發文者
	
	pageContext.setAttribute("memVO", memVO);

	photo_wall_likeService photo_wall_likeSvc = new photo_wall_likeService();

	// 紀錄喜歡資料表裡面是否有此筆資料 ，有的話代表被喜歡過
	 int cnt = photo_wall_likeSvc.findByPrimaryKey(memId, photo_wallVO.getPhoto_No());
	 System.out.println("cnt="+memId);							//取得喜歡過的memId
	 System.out.println("cnt="+photo_wallVO.getPhoto_No());		//取得喜歡過的Photo_No

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
<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService" />
<jsp:useBean id="photo_tagSvc" scope="page" class="com.photo_tag.model.Photo_tagService" />

<%@ page import="com.fri.model.*,com.chat.model.*" %>
<jsp:useBean id="chatRoomSvc" scope="page" class="com.chat.model.ChatRoomService"></jsp:useBean>
<jsp:useBean id="chatRoomJoinSvc" scope="page" class="com.chat.model.ChatRoom_JoinService"></jsp:useBean>
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


<!DOCTYPE html>
<html>

<head>
<!-- 網頁title -->
<title>Travel Maker</title>
<!-- //網頁title -->
<!-- 指定螢幕寬度為裝置寬度，畫面載入初始縮放比例 100% -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- //指定螢幕寬度為裝置寬度，畫面載入初始縮放比例 100% -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- 設定網頁keywords -->
<meta name="keywords" content="TravelMaker,Travelmaker,自助旅行,照片牆" />
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

<!-- JQUERY -->
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<!-- //JQUERY -->

<!-- Bootstrap -->
<link
	href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<script
	src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>

<!-- font字體 -->
<link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Pacifico'
	rel='stylesheet' type='text/css'>
<!-- //font字體 -->

<!-- 套首頁herder和footer css -->
<link
	href="<%=request.getContextPath()%>/front_end/css/all/index_style.css"
	rel="stylesheet" type="text/css" media="all" />
<!-- //套首頁herder和footer css -->

<!-- photowall 自定義的css -->
<link
	href="<%=request.getContextPath()%>/front_end/css/photo_wall/photowall.css"
	rel="stylesheet" type="text/css">
<!-- //photowall 自定義的css -->

<!-- modal_report_photowall 自定義的css -->
<link
	href="<%=request.getContextPath()%>/front_end/css/photo_wall/login.css"
	rel="stylesheet" type="text/css">
<link
	href="<%=request.getContextPath()%>/front_end/css/photo_wall/modal_report_photowall.css"
	rel="stylesheet" type="text/css">
<!-- //view_photowall 自定義的css -->

<!-- view_photowall 自定義的css -->
<link
	href="<%=request.getContextPath()%>/front_end/css/photo_wall/view_photowall.css"
	rel="stylesheet" type="text/css">
<!-- //view_photowall 自定義的css -->

<!-- view_photowall 自定義的js -->
<script
	src="<%=request.getContextPath()%>/front_end/js/photo_wall/photo_wall.js"></script>
<!-- //view_photowall 自定義的js -->

<!-- view_photowall使用到的jQuery Dialog -->
<link
	href="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.css"
	rel="stylesheet">
<script
	src="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.js"></script>
<!-- //view_photowall使用到的jQuery Dialog -->

<!-- font-awesome icons -->
<link href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
	rel="stylesheet"
	integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<!-- //font-awesome icons -->

<!-- font字體 -->
<link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Pacifico'
	rel='stylesheet' type='text/css'>
<!-- //font字體 -->

	<style>
	
	
.profile_photo {
    border-radius: 50px;
    width: 50px;
    display: inline-block;
}

.row_photo{
    height: 590px;
    margin: 0px auto;
    margin-top: 1px;
    position: none;
    z-index: 100;
}

.tab-content{
	margin-left:10px;
}

.tag_context{
 	height:150px;
}

	</style>
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
						<li><i class="fa fa-phone" aria-hidden="true"></i> <a
							href="tel:034257387"> 03-4257387</a></li>
						<li><a href="mailto:TravelMaker@gmail.com"><i
								class="fa fa-envelope" aria-hidden="true"></i>
								TravelMaker@gmail.com</a></li>
						<ul>
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
						<button type="button" class="navbar-toggle collapsed"
							data-toggle="collapse"
							data-target="#bs-example-navbar-collapse-1">Menu</button>
						<!-- //當網頁寬度太小時，導覽列會縮成一個按鈕 -->
						<!-- Collect the nav links, forms, and other content for toggling -->
						<div class="collapse navbar-collapse"
							id="bs-example-navbar-collapse-1">
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
	<!-- //banner -->

	<ol class="breadcrumb">
		<li><a href="<%=request.getContextPath()%>/front_end/index.jsp">回到首頁</a></li>

		<li><a
			href="<%=request.getContextPath()%>/front_end/photowall/photo_wall.jsp">照片牆</a></li>

		<li class="active">瀏覽照片牆</li>

	</ol>

	<!-- view photowall -->

	<div class="container">
	<div class="row row_photo">
		<div class="card">
			<div class="container-fliud">
				<div class="wrapper row">
					<div class="preview col-md-6 col-xs-6 col-s-6">
						<div class="preview-pic tab-content">
							<div class="preview-pic tab-content">
								<img style="margin:0px" src="data:image/*;base64,${photo_wallVO.encoded}">
							</div>

						</div>
					</div>


					<div class="details col-md-6 col-xs-6 col-s-6">
						<div class="profile_all">
							<div class="profile_photo">
								<img style="margin:0px; border-radius:50%; text-decoration:none;" 
								src="data:image/*;base64,${memVO.encoded}" width="30"
									height="30">

							</div>
							<div class="profile_photo_hr">
								<a href="#" class="product-title" style="color:black;">${memberSvc.findByPrimaryKey(photo_wallVO.mem_Id).mem_Name}</a>

								<%-- 							<a href="#" class="product-title">${memVO.mem_Name}</a> --%>
							</div>
						</div>
						<hr class="hr_setting">
						<div class="rating">
							<div class="icon_all">

<!-- 								<img src="" width="20" height="20">&nbsp;87人看過 -->

								<div id="collectMessage"></div>

								<div class="ui basic buttons">

									<button  class="ui.button.collect like_heart_position" id="collect" style="margin:0px">
											<i class="collection far fa-heart" style="font-weight:<%=(cnt == 0) ? "400" : "900"%>;
												color:<%=(cnt == 0) ? "black" : "red"%>">
											</i> 喜歡

									</button>
								</div>
								<!--
                                
                                <a class="like_heart"><img src="" width="20" height="20"></a>&nbsp;87人喜歡
								-->
								<hr class="hr_setting">
							</div>
							<div>
								<table class="tag_context">

										<c:forEach var="Photo_tag_listVO" items="${list}">
										<c:forEach var="Photo_tagVO" items="${photo_tagSvc.all}">
										<c:if test="${Photo_tagVO.photo_Tag_No==Photo_tag_listVO.photo_Tag_No}">
									<tr style="height:fit-content"><td>${Photo_tagVO.tag_Content}</td></tr>
									</c:if>
										</c:forEach>
										</c:forEach>
										
										
									<tr><td style="max-width:330px;height:fit-content">${photo_wallVO.photo_Content}</td></tr>
								</table>
							</div>
							<div style="float:right">
								<button type="button" data-toggle="modal" data-target="#myModal"
									class="fas fa-exclamation-triangle" style="border:0; background-color:white; height: 32px;"></button>
							</div>
							<hr class="hr_setting" >
						</div>
						<div class="action">

							<!--
							<button class="fab fa-facebook-f" type="button"></button>
							
							<button class="" type="button"><span class="fab fa-line"></span></button>
-->
						</div>
					</div>
				</div>
			</div>
		</div>
		</div>
	</div>


	<!-- //view photowall -->


	<div class="footer">
		<div class="container">
			<div class="footer-grids">
				<div class="col-md-3 footer-grid">
					<div class="footer-grid-heading">
						<h4>關於我們</h4>
					</div>
					<div class="footer-grid-info">
						<ul>
							<li><a href="about.html">關於Travel Maker</a></li>
							<li><a href="about.html">聯絡我們</a></li>
							<li><a href="about.html">常見問題</a></li>
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
					Copyright &copy; 2018 All rights reserved <a href="index.html"
						target="_blank" title="TravelMaker">TravelMaker</a>
				</p>
			</div>
		</div>
	</div>

    <div id="collectMessage"></div>

	<div id="reportPicDialog">
		<div class="reportContent">
			<div class="reportContentTitle">檢舉理由：</div>
			
				<form class="ui reportMessage form" METHOD="POST" ACTION="<%=request.getContextPath()%>/photo_wall.do">
				
				<textarea class="reportReasonContent" name="report_Reason" maxlength="90"></textarea>
								
				<input type="hidden" name="photo_No" id="reportPhotoNo" value="${photo_wallVO.photo_No}"> 
				
				<input type="hidden" name="mem_Id" value="<%=memberVO.getMem_Id()%>"> 
				
				<input type="hidden" name="action" value="reportPhoto">
				
			</form>
		</div>
	</div>
	
	<c:if test="${not empty errorMsgs}">
    	<div id="reportPicDialog2">
    		<div class="messageContent">
    			<c:forEach var="message" items="${errorMsgs}">
    			${message}
    			</c:forEach>
    		</div>
    	</div>
    	</c:if>

	<script type="text/javascript">
	
$(document).ready(function () {
	
      $("#collect").click(function(){
       var action = "collect";
       var photo_No = "<%=photo_wallVO.getPhoto_No()%>";
	   var mem_Id = "<%=memberVO.getMem_Id()%>";
	   var collectMessage = document.getElementById("collectMessage");
				$.ajax({
					url : "<%=request.getContextPath()%>/photo_wall.do",
					method : "POST",
					data : {
						action:action,
						photo_No:photo_No,
						mem_Id:mem_Id 
					},
					async : false,
					success : function(msg) {
						collectMessage.innerHTML = msg;

					},
					error : function(msg) {
						collectMessage.innerHTML = msg;
					}
				});
			});
		});
	</script>
	<script type="text/javascript">
	
	$("#collect").click(function () {
        if ($(".collection").css('font-weight') == 900) {
            $(".collection").css('font-weight', '400');
            $(".collection").css('color', 'black');
        } else {
            $(".collection").css('font-weight', '900');
            $(".collection").css('color', 'red');
        }
    });
	
	</script>
	
</body>

</html>

