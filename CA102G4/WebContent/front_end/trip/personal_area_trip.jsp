<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="javax.servlet.http.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.mem.model.*" %>
<%@ page import="com.trip.model.*" %>
<%@ page import="java.util.*" %>
<%
	//取得當前的session，拿去確認是否已經存過了?取出對應會員ID
	//HttpSession session = request.getSession();省略
// 	MemberVO memberVO = (MemberVO)session.getAttribute("memberVO");浩源的
	String sid = session.getId();
	Object memId=session.getAttribute(sid);
	
	//若是未登錄的人，將會把目前位置存起來，並導向到登錄畫面
	if(memId == null){
		session.setAttribute("location",request.getRequestURI());
		response.sendRedirect(request.getContextPath()+"/front_end/member/mem_login.jsp");
		return;
	}
	
	//購物車
	Object total_items_temp = session.getAttribute("total_items");
	int total_items = 0;
	if(total_items_temp != null ){
		total_items= (Integer) total_items_temp;
	}
	pageContext.setAttribute("total_items",total_items);
	
	//取出會員相關資料
	MemberService memSvc = new MemberService();
	MemberVO memVO=memSvc.getOneMember((String)memId);//動態從session取得會員ID
//  	MemberVO memVO=memSvc.getOneMember("M000001");
	pageContext.setAttribute("memvo",memVO);
	
	
	TripService tripSvc = new TripService();
	List<TripVO> tripList = tripSvc.getByMem_id(memVO.getMem_Id());
	pageContext.setAttribute("tripList", tripList);
	
	List<TripVO> colList = tripSvc.getOneMemCollection(memVO.getMem_Id());
	pageContext.setAttribute("colList", colList);
%>

<%@ page import="com.fri.model.*" %>
<%@ page import="com.chat.model.*" %>
<jsp:useBean id="chatRoomSvc" scope="page" class="com.chat.model.ChatRoomService"></jsp:useBean>
<jsp:useBean id="chatRoomJoinSvc" scope="page" class="com.chat.model.ChatRoom_JoinService"></jsp:useBean>
<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService"></jsp:useBean>
<%

	//*****************聊天用：取得登錄者所參與的群組聊天*************/
	MemberVO memberVO = (MemberVO)session.getAttribute("memberVO");
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
	FriendService friSvc = new FriendService();
	List<Friend> myFri = friSvc.findMyFri(memberVO.getMem_Id(),2); //互相為好友的狀態
	pageContext.setAttribute("myFri",myFri);
	
	/**************避免聊天-新增群組重新整理後重複提交********/
	session.setAttribute("addCR_token",new Date().getTime());


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
    <meta name="keywords" content="TravelMaker,travelmaker,自助旅行,部落格,旅遊記" />
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
    <!-- //JQUERY -->
    
    <!-- bootstrap css及JS檔案 -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <script src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
    <!-- //bootstrap-css -->
    
    <!-- semantic css -->
    <link href="<%=request.getContextPath()%>/front_end/css/ad/ad_semantic.min.css" rel="stylesheet" type="text/css">
    <!-- //semantic css -->
    
    <!-- 套首頁herder和footer css -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_style.css" rel="stylesheet" type="text/css" media="all" />
    <!-- //套首頁herder和footer css -->
     
    <!-- font-awesome icons -->
    <link href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" rel="stylesheet" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- //font-awesome icons -->
    
    <!-- font字體 -->
    <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
    <!-- //font字體 -->
    
    <!-- LogoIcon -->
	<link href="<%=request.getContextPath()%>/front_end/images/all/Logo_Black_use.png" rel="icon" type="image/png">
	<!-- //LogoIcon -->
    
    <!-- AD_Page相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/ad/ad_page.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/personal/personal_area_home.css" rel="stylesheet" type="text/css">
    <!-- //AD_Page相關CSS及JS -->
    

    
    <!--自訂css -->
	<link href="<%=request.getContextPath()%>/front_end/css/trip/trip_list.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/attractions/att_style.css" rel="stylesheet" type="text/css">
     <!--//自訂css -->
     
     <!-- SweetAlert CDN -->
	<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <style>
    
    /*若搜尋不到文章或圖片時，將會顯示 驚嘆號圖片nothing.png*/
    .nothing{
    	width:50px;
    	heigh:50px;
    }
    
    <!-- 聊天相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css">
    <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script>
    <%@ include file="/front_end/personal_area/chatModal_JS.file" %>
    <!-- //聊天相關CSS及JS -->
    
    
    </style>
</head>

<body>
	<%-- 錯誤表列 --%>
	<c:if test="${not empty errorMsgs_Ailee}">
		<div class="modal fade" id="errorModal_Ailee">
		    <div class="modal-dialog modal-sm" role="dialog">
		      <div class="modal-content">
		        <div class="modal-header">
		          <i class="fas fa-exclamation-triangle"></i>
		          <span class="modal-title"><h4>請修正以下錯誤:</h4></span>
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
                          <li><a href="<%= request.getContextPath()%>/front_end/member/member.do?action=logout"><span class=" top_banner"><i class=" fas fa-sign-out-alt" aria-hidden="true"></i></span></a></li>
                          <li><a class="top_banner" href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_home.jsp"><i class="fa fa-user" aria-hidden="true"></i></a></li>
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
                                <div class="clearfix"> </div>
                            </ul>
                        </div>
                    </nav>
                </div>
                <div class="clearfix"> </div>
            </div>
        </div>
    </div>
    <!-- //banner -->
    
    <!--container-->
    <div class="ui container mb-3">
        <!--會員個人頁面標頭-->
        <div class="mem_ind_topbar">
            <!--會員封面-->
            <div class="mem_ind_banner">
                <img src="<%=request.getContextPath()%>/front_end/images/all/person_bar.jpg">
            </div>
            <!--會員訊息--> 
            <div class="mem_ind_info"> 
                <div class="mem_ind_img">
                    <img src="<%=request.getContextPath()%>/front_end/readPic?action=member&id=${memvo.mem_Id}">
                </div>
                <div class="mem_ind_name">
                    <p>${memvo.mem_Name}
                    	<c:if test="${memvo.mem_Sex == 1}">
       						<i class='fas fa-male' style='color:#4E9EE2'></i>
      					</c:if>
      					<c:if test="${memvo.mem_Sex == 2}">
       						<i class='fas fa-female' style='color:#EC7555'></i>
     					</c:if>
                    </p>
                    <p class="text-truncate" style="font-size:0.9em;padding-top:10px;max-height:110px">
					   ${memvo.mem_Profile}
                    </p>
                </div>
            </div> 
        </div>
        <!--//會員個人頁面標頭-->
        <!--會員個人頁面-首頁內容-->
        <div class="mem_ind_content">
          <!-- 頁籤項目 -->
          <ul class="nav nav-tabs" role="tablist">
            <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_home.jsp">
                  <i class="fas fa-home"></i>首頁
              </a>
            </li>
            <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_friend.jsp">
                  <i class="fas fa-user-friends"></i>好友
              </a>
            </li>
            <li class="nav-item">
              <a href="<%=request.getContextPath()%>/blog.do?action=myBlog&mem_id=${memberVO.mem_Id}">
                  <i class="fab fa-blogger"></i>旅遊記
              </a>
            </li>
            <li class="nav-item active">
              <a href="<%=request.getContextPath()%>/front_end/trip/personal_area_trip.jsp">
                  <i class="fas fa-map"></i>行程
              </a>
            </li>
            <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/grp/personal_area_grp.jsp">
                  <i class="fas fa-bullhorn"></i>揪團
              </a>
            </li>
            <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/personal/personal_area_question.jsp">
                  <i class="question circle icon"></i>問答
              </a>
            </li>
            <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_photoWall.jsp">
                  <i class="image icon"></i>相片
              </a>
            </li>
            
             <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_sell.jsp">
                  <i class="money bill alternate icon"></i>銷售
              </a>
            </li>

             <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_buy.jsp">
                  <i class="shopping cart icon"></i>購買
              </a>
            </li>
            <li class="nav-item" style="float: right">
              <a href="<%=request.getContextPath()%>/front_end/member/update_mem_profile.jsp">
                  <i class="cog icon"></i>設置
              </a>
            </li>
          </ul>
          <!-- //頁籤項目 -->
          <!-- 頁籤項目-首頁內容 -->
          <div class="tab-content" style="float:left;width:100%">
            <!--首頁左半邊-個人首頁-->
				<ul class="nav nav-tabs" id="myTab" role="tablist">
					<li class="nav-item active">
						<a class="nav-link active" id="myTrip-tab" data-toggle="tab" href="#myTrip" role="tab" aria-controls="myTrip" aria-selected="true">我的行程</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" id="myCollection-tab" data-toggle="tab" href="#myCollection" role="tab" aria-controls="myCollection" aria-selected="false">收藏行程</a>
					</li>
				</ul>
				<div class="tab-content mb-2" id="myTabContent">
					<div class="tab-pane show active" id="myTrip" role="tabpanel" aria-labelledby="myTrip-tab">
						<%@ include file="/front_end/trip/include/personal_page1.file"%>
						<div class="ui four stackable cards">
							<c:forEach var="tripVO" items="${tripList}" begin="<%=pageIndex%>"
								end="<%=pageIndex+rowsPerPage-1%>">
								
								<a class="fluid card card-${tripVO.trip_no}" href="javascript:void(0)" title="${tripVO.trip_name}" style="text-decoration: none;">
									<div class="image">
										<div class="wrapper">
											<div class="ui image pic" style="background-color: #ccc;">
												<img src="<%= request.getContextPath()%>/trip/getPicture.do?trip_no=${tripVO.trip_no}">
												<div class="ui pink ribbon label">
													<i class="far fa-calendar-alt"></i> ${tripVO.trip_days} 天
												</div>
											</div>
										</div>
									</div>
									<div class="content">
										<div class="header">${tripVO.trip_name}</div>
										<div class="meta" style="font-size: 14px">
											<i class="far fa-calendar-alt"></i>出發日期：${tripVO.trip_startDay}
										</div>
										<div class="description">
										</div>
									</div>
									<div class="extra content" align="center">
										<div class="btn-group w-100" role="group">
											<button id="btn-edit-${tripVO.trip_no}" class="btn btn-success w-50"><i class="fas fa-edit"></i>&nbsp;編輯</button>
											<button id="btn-delete-${tripVO.trip_no}" class="btn btn-danger w-50"><i class="fas fa-trash-alt"></i>&nbsp;刪除</button>
										</div>
										<form id="form-edit-${tripVO.trip_no}" action="<%=request.getContextPath()%>/trip/trip.do" method="post">
											<input type="hidden" name="action" value="editTrip">
											<input type="hidden" name="trip_no" value="${tripVO.trip_no}">
										</form>
										<form id="form-delete-${tripVO.trip_no}" action="<%=request.getContextPath()%>/trip/trip.do" method="post">
											<input type="hidden" name="action" value="deleteTrip">
											<input type="hidden" name="trip_no" value="${tripVO.trip_no}">
										</form>
									</div>
									<div class="extra content">
										<span class="right floated"> </span> <i class="unhide icon"></i>
										${tripVO.trip_views} 次瀏覽
									</div>
									<div class="extra content">
										<div class="right floated author nowrap">
											<img class="ui avatar image"
												src="<%= request.getContextPath()%>/trip/getPicture.do?mem_id=${tripVO.mem_id}">
											${memvo.mem_Name}
										</div>
									</div>
								</a>
								<script>
									$(document).ready(function(){
										$(".card-${tripVO.trip_no}").click(function(){
											window.location="<%= request.getContextPath()%>/front_end/trip/tripDetail.jsp?trip_no=${tripVO.trip_no}";
										});
										
									    $("#btn-edit-${tripVO.trip_no}").click(function(event){
									    	event.stopPropagation();
									    	$("#form-edit-${tripVO.trip_no}").submit();
									    });
									    $("#btn-delete-${tripVO.trip_no}").click(function(event){
									    	event.stopPropagation();
									    	$("#form-delete-${tripVO.trip_no}").submit();
									    });
									});
								</script>
							</c:forEach>
						</div>
						<%@ include file="/front_end/trip/include/personal_page2.file"%>
						<% if(tripList.size()==0){%>
							<div class="my-5" align="left"><h3>尚無建立行程</h3></div>
						<%}%>
					</div>
					<div class="tab-pane fade" id="myCollection" role="tabpanel" aria-labelledby="myCollection-tab">
					<div class="ui four stackable cards">
							<c:forEach var="tripVO" items="${colList}">
								<a class="fluid card card-collect-${tripVO.trip_no}"
									href="javascript:void(0)"
									title="${tripVO.trip_name}" style="text-decoration: none;">
									<div class="image">
										<div class="wrapper">
											<div class="ui image pic" style="background-color: #ccc;">
												<img src="<%= request.getContextPath()%>/trip/getPicture.do?trip_no=${tripVO.trip_no}">
												<div class="ui pink ribbon label">
													<i class=" checked calendar icon"></i> ${tripVO.trip_days} 天
												</div>
											</div>
										</div>
									</div>
									<div class="content">
										<div class="header">${tripVO.trip_name}</div>
										<div class="meta" style="font-size: 14px">
											<i class="calendar icon"></i>出發日期：${tripVO.trip_startDay}
										</div>
										<div class="description">
										</div>
									</div>
									<div class="extra content" align="center">
										<div class="btn-group w-100" role="group">
											<button class="btn btn-danger w-50" id="btn-cancel-${tripVO.trip_no}"><i class="fas fa-trash-alt"></i>取消收藏</button>
										</div>
									</div>
									<div class="extra content">
										<span class="right floated"> </span> <i class="unhide icon"></i>
										${tripVO.trip_views} 次瀏覽
									</div>
									<div class="extra content">
										<div class="right floated author nowrap">
											<img class="ui avatar image"
												src="<%= request.getContextPath()%>/trip/getPicture.do?mem_id=${tripVO.mem_id}">
											${memSvc.getOneMember(tripVO.mem_id).mem_Name}
										</div>
									</div>
								</a>
								<script>
									$(document).ready(function(){
										$(".card-collect-${tripVO.trip_no}").click(function(){
											window.location="<%= request.getContextPath()%>/front_end/trip/tripDetail.jsp?trip_no=${tripVO.trip_no}";
										});
										
									    $("#btn-cancel-${tripVO.trip_no}").click(function(event){
									    	event.stopPropagation();
									    	$.ajax({
												url:"/CA102G4/trip/trip.do",
												data:{
													'action':'cancelCollect',
													'trip_no':'${tripVO.trip_no}'
													},
												type:'POST',
												success: function(result){
													if(result=="fail"){
														window.location="<%= request.getContextPath()+"/front_end/member/mem_login.jsp"%>";
													}else if(result==0){
														swal('取消收藏失敗', 'You clicked the button!', 'error');
													}else if(result>=1){
														swal('取消收藏成功', 'You clicked the button!', 'success');
														$(".card-collect-${tripVO.trip_no}").remove();
													}
											}});
									    });

									});
								</script>
							</c:forEach>
						</div>
						<% if(colList.size()==0){%>
							<div class="my-5" align="left"><h3>尚無收藏行程</h3></div>
						<%}%>
					</div>
				</div>
			
            <!--//首頁左半邊-個人首頁-->
          </div>
          <!--頁籤項目-首頁內容-->
<!--           首頁右半邊                 -->
<!--           <div style="width:25%;float:left"> -->
<!--                 <div class="add_Div"> -->
<!--                     <a href="blog_add.jsp" class="adddiv_a"> -->
<!--                         <div style="color:rgb(93,187,133)"> -->
<!--                             <i class="fas fa-edit"></i><br> -->
<!--                             寫旅遊記 -->
<!--                         </div>         -->
<!--                     </a> -->
<!--                     <a href="" class="adddiv_a"> -->
<!--                         <div style="color:rgb(245,177,0)"> -->
<!--                             <i class="far fa-calendar-check"></i><br> -->
<!--                             規劃行程 -->
<!--                         </div>   -->
<!--                     </a> -->
<!--                     <a href="" class="adddiv_a"> -->
<!--                         <div style="color:rgb(242,102,34)"> -->
<!--                         <i class="fas fa-bullhorn"></i><br> -->
<!--                         揪旅伴去 -->
<!--                         </div>   -->
<!--                     </a> -->
<!--                     <a href="" class="adddiv_a"> -->
<!--                         <div style="color:rgb(81,167,219)"> -->
<!--                             <i class="fas fa-comment-dots"></i><br> -->
<!--                             提問題去 -->
<!--                         </div>  -->
<!--                     </a> -->

<!--                 </div> -->
<!--             </div> -->
<!--           //首頁右半邊  -->
        </div>
        <!-- //會員個人頁面內容 -->
    </div>
    <!--//container-->
        
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
                            <li><a href="https://www.facebook.com/InstaBuy.tw/" target="_blank"><i class="fab fa-facebook"></i></a></li>
                            <li><a href="https://www.instagram.com/" target="_blank"><i class="fab fa-instagram"></i></a></li>
                            <li><a href="#" target="_blank"><i class="fab fa-line"></i></a></li>
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
