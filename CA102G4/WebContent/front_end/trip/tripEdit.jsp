<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.*"%>
<%@ page import="com.attractions.model.*"%>
<%@ page import="com.tripDays.model.*"%>
<%@ page import="com.trafficTrip.model.*"%>
<%@ page import="com.attTrip.model.*"%>
<%@ page import="com.trip.model.*"%>
<%@ page import="com.mem.model.*"%>

<%
 
	MemberVO memberVO = (MemberVO) request.getAttribute("memberVO"); 
	if(memberVO == null){
		memberVO = (MemberVO)session.getAttribute("memberVO");
	}
	
	boolean login_state = false;   
	Object login_state_temp = session.getAttribute("login_state");
	if(login_state_temp!=null){
		login_state=(boolean)login_state_temp;
	}
	
	if(login_state!=true){
		session.setAttribute("location", request.getRequestURI());
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

	TripVO tripVO = (TripVO) session.getAttribute("tripVO_edit");
	List<TripDaysVO> tdList = (List<TripDaysVO>) session.getAttribute("tdList");
	Map<Integer,List<Object>> tripDayMap = (Map<Integer,List<Object>>)session.getAttribute("tripDayMap");
	//是否重正當管道進入
	if (tripVO == null || tdList == null) {
		response.sendRedirect(request.getContextPath()+"/front_end/trip/trip.jsp");
		return;
	}

	//取得景點列表
	AttractionsService attSvc = new AttractionsService();
	List<AttractionsVO> list = null;
	list = (List<AttractionsVO>) request.getAttribute("list");
	if (list == null) {
		attSvc = new AttractionsService();
		list = attSvc.getAll();
		pageContext.setAttribute("list", list);
	}
	
	AttractionsTripService attTripSvc = new AttractionsTripService(); 
	
	request.setAttribute("list", list);
	pageContext.setAttribute("attSvc", attSvc);
	pageContext.setAttribute("attTripSvc", attTripSvc);
%>

<%@ page import="com.fri.model.*" %>
<%@ page import="com.chat.model.*" %>
<jsp:useBean id="chatRoomSvc" scope="page" class="com.chat.model.ChatRoomService"></jsp:useBean>
<jsp:useBean id="chatRoomJoinSvc" scope="page" class="com.chat.model.ChatRoom_JoinService"></jsp:useBean>
<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService"></jsp:useBean>
<%

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
	FriendService friSvc = new FriendService();
	List<Friend> myFri = friSvc.findMyFri(memberVO.getMem_Id(),2); //互相為好友的狀態
	pageContext.setAttribute("myFri",myFri);
	
	/**************避免聊天-新增群組重新整理後重複提交********/
	session.setAttribute("addCR_token",new Date().getTime());


%>

<html>
<head>
<!-- 網頁title -->
<title>行程編輯</title>
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
	href="<%=request.getContextPath()%>/front_end/css/attractions/att_card.css"
	rel="stylesheet">

<link
	href="<%=request.getContextPath()%>/front_end/css/attractions/attDetail.css"
	rel="stylesheet">

<link
	href="<%=request.getContextPath()%>/front_end/css/trip/tripEdit.css"
	rel="stylesheet">
	
<link
	href="<%=request.getContextPath()%>/front_end/css/trip/gMap_style.css"
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

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script
	src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
<style>
	#attTable tr, #attTable th, #attTable td {
		padding: 5px !important;
	}
</style>

	<!-- 聊天相關CSS及JS -->
<%--     <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css"> --%>
<%--     <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script> --%>
<%--     <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script> --%>
<%--     <%@ include file="/front_end/personal_area/chatModal_JS.file" %> --%>
    <!-- //聊天相關CSS及JS -->

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
								<a href="<%=request.getContextPath()%>/front_end/member/member.do?action=logout">
									<span class=" top_banner">
										<i class=" fas fa-sign-out-alt" aria-hidden="true"></i>
									</span>
								</a>
							</li>
							<li>
								<a class="top_banner" href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_home.jsp"><i class="fa fa-user" aria-hidden="true"></i></a>
							</li>
							<li><a class="top_banner" href="<%=request.getContextPath()%>/front_end/store/store_cart.jsp"><i class="fa fa-shopping-cart shopping-cart" aria-hidden="true"></i><span class="badge">${total_items}</span></a></li>
							<li>
								<a class="top_banner" href="#"><i class="fa fa-envelope" aria-hidden="true"></i></a>
							</li>
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


	<!-- Modal區塊  -->
	<form action="<%=request.getContextPath()%>/trip/trip.do" method="post">
		<div class="modal fade" id="changeName" tabindex="-1" role="dialog"
			aria-labelledby="exampleModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header bg-light">
						修改行程名稱
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						行程名稱<font size="2" color=red><b>*</b></font> <input
							class="form-control mt-1" type="text" name="trip_name"
							pattern=".{1,20}" required placeholder="您的行程名稱(20字以內)"
							value="${tripVO.trip_name}">
					</div>
					<div class="modal-footer bg-light">
						<div align="center">
							<button type="button" class="btn btn-secondary"
								data-dismiss="modal">取消</button>
							<button type="submit" class="btn btn-danger">確定</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" name="action" value="changeName">
	</form>
	<!-- //Modal區塊  -->

	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-md-6 col-sm-6 col-6">
				<h2 class="m-0" id="tripName">${tripVO_edit.trip_name}&nbsp;<a
						href="javascript:void(0)" style="color: orange; font-size: 12px;"
						data-toggle="modal" data-target="#changeName"><i
						class="fas fa-pencil-alt"></i>修改行程名稱</a>
				</h2>
			</div>
			<div class="col-lg-6 col-md-6 col-sm-6 col-6">
				<form method="post" action="<%=request.getContextPath()%>/trip/trip.do">
					<div align="right">
						<button type="submit" class="btn btn-success">送出編輯</button>
					</div>
					<input type="hidden" name="action" value="commitEdit">
				</form>
			</div>
		</div>
	</div>
	<hr>
	<div class="container-fluid">
		<div class="row">
			<!--行程天數 -->
			<div class="col-lg-1 col-md-1 col-sm-1 col-1 p-0">
				<div class="card bg-light mx-0 my-2 w-100" style="overflow-y: auto;">
					<form class="m-2 p-0 w-auto" id="dateForm" method="post"
						action="<%=request.getContextPath()%>/trip/trip.do">
						<input class="form-control m-0 w-100" id="f_date1" type="text"
							style="font-size: 11px;" name="trip_startDay"
							value="${tripVO_edit.trip_startDay}"> <input
							type="hidden" name="action" value="changeDate">
					</form>

					<c:forEach var="tripDaysVO" items="${tdList}">
						<button id="btn_${tripDaysVO.tripDay_days}"
							class="btn btn-info m-2 p-2 w-auto">第${tripDaysVO.tripDay_days}天</button>
					</c:forEach>
				</div>
			</div>
			<!--//行程天數 -->

			<!--行程內容-->
			<div class="col-lg-4 col-md-4 col-sm-4 col-4">
				<div id="card_tripDay" class="card bg-light p-2 mx-0 my-2 w-100"
					style="overflow-y: auto;">
					<% int dayCount = 0;%>
					<c:forEach var="tripDaysVO" items="${tdList}" varStatus="s">
					<% dayCount++;%>
						<div class="card p-2" id="card_${tripDaysVO.tripDay_days}">
							<div class="card-body p-2">
								<span class="icon-day">D${tripDaysVO.tripDay_days}</span> <span>${tripDaysVO.tripDay_date}</span>
							</div>
							<% List<Object> detailList = tripDayMap.get(dayCount); %>
							<% for(int i=0;i<detailList.size();i++){ %>
								<%//如果是交通行程 %>
								<% if(detailList.get(i) instanceof TrafficTripVO) {%>
								<% TrafficTripVO traTripVO = (TrafficTripVO)detailList.get(i);%>
								<% pageContext.setAttribute("detail_traTripVO", traTripVO); %>
									<div class="card bg-light p-1" id="card-${tripDaysVO.tripDay_days}-<%=i%>">
										<div class="container-fluid m-0">
											<div class="row">
												<div class="col-lg-3 col-md-3 col-sm-3 col-3 traffic-icon">
												<% int type = traTripVO.getTraTrip_type();%>
												<% if(type==0){ %>
														<i class="fas fa-subway"></i>
												<%}else if(type==1){%>
														<i class="fas fa-plane"></i>
												<%}else if(type==2){%>
														<i class="fas fa-train"></i>
												<%}else if(type==3){%>
														<i class="fas fa-bus"></i>
												<%}else if(type==4){%>
														<i class="fas fa-walking"></i>
												<%}else if(type==5){%>
														<i class="fas fa-taxi"></i>
												<%}else if(type==6){%>
														<i class="fas fa-car"></i>
												<%} %>
												</div>
												<div class="col-lg-9 col-md-9 col-sm-9 col-9">
													<h4>${detail_traTripVO.traTrip_name}</h4>
														<i class="far fa-clock"></i>
														<span style="font-size:14px">
														<%= String.format("%02d", traTripVO.getTraTrip_start()/60)+":"+String.format("%02d", traTripVO.getTraTrip_start()%60)%>
														~
														<%= String.format("%02d", traTripVO.getTraTrip_end()/60)+":"+String.format("%02d", traTripVO.getTraTrip_end()%60)%>
														</span>
														<span class="ml-4" style="font-size:14px">
															<i class="fas fa-dollar-sign"></i> TWD ${detail_traTripVO.traTrip_cost}
														</span>
														<div id="btn-group-${tripDaysVO.tripDay_days}-<%=i%>" class="detail-btn btn-group btn-group-sm" role="group"  style="display: none">
															<button class="btn btn-default" title="編輯" data-toggle="modal" data-target="#edit-${tripDaysVO.tripDay_days}-<%=i%>"><i class="fas fa-pencil-alt"></i></button>
															<button class="btn btn-danger" title="刪除"  data-toggle="modal" data-target="#del-${tripDaysVO.tripDay_days}-<%=i%>"><i class="fas fa-trash-alt"></i></button>
														</div>
												</div>
												
												<div class="col-lg-12 col-md-12 col-sm-12 col-12">
													<hr>
													<%= traTripVO.getTraTrip_note()%>
												</div>
											</div>
										</div>
									</div>

								<%//如果是景點行程 %>
								<%}else if(detailList.get(i) instanceof AttractionsTripVO){%>
								<% AttractionsTripVO attTripVO = (AttractionsTripVO)detailList.get(i);%>
								<% pageContext.setAttribute("detail_attTrip", attTripVO); %>
									<div class="card bg-light p-1" id="card-${tripDaysVO.tripDay_days}-<%=i%>">
										<div class="container-fluid">
											<div class="row">
												<div class="col-lg-3 col-md-3 col-sm-3 col-3">
													<div class="polaroid my-auto">
														<img src="<%= request.getContextPath()%>/trip/getPicture.do?att_no=<%= attTripVO.getAtt_no()%>" style="width: 100%">
													</div>
												</div>
												<div class="col-lg-9 col-md-9 col-sm-9 col-9">
													<h4>${attSvc.getOneAttByPK(detail_attTrip.att_no).att_name}</h4>
														<i class="far fa-clock"></i>
														<span style="font-size:14px
														">
														<%= String.format("%02d", attTripVO.getAttTrip_start()/60)+":"+String.format("%02d", attTripVO.getAttTrip_start()%60)%>
														~
														<%= String.format("%02d", attTripVO.getAttTrip_end()/60)+":"+String.format("%02d", attTripVO.getAttTrip_end()%60)%>
														</span>
														<span class="ml-4" style="font-size:14px">
															<i class="fas fa-dollar-sign"></i> TWD ${detail_attTrip.attTrip_cost}
														</span>
														<div id="btn-group-${tripDaysVO.tripDay_days}-<%=i%>" class="detail-btn btn-group btn-group-sm" role="group"  style="display: none">
															<button class="btn btn-default" title="編輯" data-toggle="modal" data-target="#edit-${tripDaysVO.tripDay_days}-<%=i%>"><i class="fas fa-pencil-alt"></i></button>
															<button class="btn btn-danger" title="刪除" data-toggle="modal" data-target="#del-${tripDaysVO.tripDay_days}-<%=i%>"><i class="fas fa-trash-alt"></i></button>
														</div>
	
												</div>
												
												<div class="col-lg-12 col-md-12 col-sm-12 col-12">
													<hr>
													<%= attTripVO.getAttTrip_note()%>
												</div>
											</div>
										</div>
									</div>
								<%}%>
							<script>
									$(document).ready(function(){
										$("#card-${tripDaysVO.tripDay_days}-<%=i%>").mouseenter(function(){
											$("#btn-group-${tripDaysVO.tripDay_days}-<%=i%>").show();
										});
			
										$("#card-${tripDaysVO.tripDay_days}-<%=i%>").mouseleave(function(){
										$("#btn-group-${tripDaysVO.tripDay_days}-<%=i%>").hide();
									});
								});
							</script>
							<%} %>
							<%if(tdList.get(dayCount-1).getTripDay_hotelName()!=null){%>
							<div class="card bg-light p-1">
								<div class="container-fluid m-0">
									<div class="row">
										<div class="col-lg-3 col-md-3 col-sm-3 col-3 traffic-icon">
											<i class="fas fa-home"></i>
										</div>
										<div class="col-lg-9 col-md-9 col-sm-9 col-9">
											<h4>${tripDaysVO.tripDay_hotelName}</h4>
											<i class="far fa-clock"></i>
											<span style="font-size:14px">
											<%= String.format("%02d", tdList.get(dayCount-1).getTripDay_hotelStart() /60)+":"+String.format("%02d", tdList.get(dayCount-1).getTripDay_hotelStart()%60)%>
											</span>
											<span class="ml-4" style="font-size:14px">
												<i class="fas fa-dollar-sign"></i> TWD ${tripDaysVO.tripDay_hotelCost}
											</span>
										</div>
										<div class="col-lg-12 col-md-12 col-sm-12 col-12">
										<hr>
										<%= tdList.get(dayCount-1).getTripDay_hotelNote()%>
										</div>
									</div>
								</div>
							</div>
							<%}%>
							<a href="javascript:void(0)"
								class="btn btn-danger ml-2 mb-2 mr-auto" data-toggle="modal"
								
								data-target="#tt_modal${tripDaysVO.tripDay_days}" title="加入交通行程"><i
								class="fas fa-subway"></i>交通</a>
							<div class="card-body p-1">
							<a href="javascript:void(0)" style="color: orange; font-size: 14px;" data-toggle="modal" data-target="#editHotel-${tripDaysVO.tripDay_days}">
							<i class="fas fa-pencil-alt"></i>編輯此日住宿資訊</a>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
			<!--//行程內容-->

			<div class="col-lg-6 col-md-6 col-sm-6 col-6 p-0">
				<div class="card bg-light mx-0 my-2" id="gmap-all">
					<div class="card-body p-1">
					    <div id="map"></div>
						    <div id="right-panel" style="overflow-y: scroll">
						      <p>總路程: <span id="total"></span></p>
						    </div>
					</div>
				</div>
				    <script>
				        var map;
				        var directionsService;
				        var directionsDisplay;
				        var waypts = [];
				
				        function initMap() {
				            map = new google.maps.Map(document.getElementById('map'), {
				                zoom: 7,
				                mapTypeId: google.maps.MapTypeId.TERRAIN,
				                center: {
				                    lat: 23.57565,
				                    lng: 120.973882
				                } 
				            });
				
				            var transitLayer = new google.maps.TransitLayer();
				            transitLayer.setMap(map);
				
				            directionsService = new google.maps.DirectionsService;
				            directionsDisplay = new google.maps.DirectionsRenderer({
				                draggable: true,
				                map: map,
				                panel: document.getElementById('right-panel')
				            });
				
				            directionsDisplay.addListener('directions_changed', function() {
				                computeTotalDistance(directionsDisplay.getDirections());
				                // console.log(directionsDisplay.getDirections().request);
				            });
							<% int whichDays = 1;
								if(request.getParameter("belongDays") != null){
									whichDays = Integer.parseInt(request.getParameter("belongDays"));
								}
							%>
							<% List<AttractionsTripVO> attTripList = new ArrayList<>(); %>
							<% List<Object> detailListGM = tripDayMap.get(whichDays);%>
							<% for(Object detail : detailListGM){
									if(detail instanceof AttractionsTripVO){
										attTripList.add((AttractionsTripVO)detail);
									}
								}
							%>
							<% pageContext.setAttribute("attTripList", attTripList);%>
							<%System.out.println(attTripList.size()); %>
							<%if(attTripList==null){%>
							<%}else if(attTripList.size()>2){%>
								<c:forEach var="attTripGMap" items="${attTripList}" begin="1" end="${attTripList.size()-2}" varStatus="s">
									waypts.push({
						                location: new google.maps.LatLng(${attSvc.getOneAttByPK(attTripGMap.att_no).att_lat}, ${attSvc.getOneAttByPK(attTripGMap.att_no).att_lon}),
						                stopover: true
						            });
					            </c:forEach>
					            
					            displayRoute(
					            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lon()%>),
					            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(attTripList.size()-1).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(attTripList.size()-1).getAtt_no()).getAtt_lon()%>),
					            		directionsService,
					            		directionsDisplay);
			            	<%}else if(attTripList.size()==2){%>
			            		displayRoute(
				            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lon()%>),
				            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(attTripList.size()-1).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(attTripList.size()-1).getAtt_no()).getAtt_lon()%>),
				            		directionsService,
				            		directionsDisplay);
			            	<%}else if(attTripList.size()==1){%>
				            	displayRoute(
					            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lon()%>),
					            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lon()%>),
					            		directionsService,
					            		directionsDisplay);
			            	<%}%>
				        }
				
				        function displayRoute(origin, destination, service, display) {
				
				            service.route({
				                origin: origin,
				                destination: destination,
				                waypoints: waypts,
				                travelMode: 'DRIVING',
				                avoidTolls: true
				            }, function(response, status) {
				                if (status === 'OK') {
// 				                    console.log(response);
				                    display.setDirections(response);
				                } else {
				                    alert('Could not display directions due to: ' + status);
				                }
				            });
				        }
				
				        function computeTotalDistance(result) {
				            var total = 0;
				            var myroute = result.routes[0];
				            for (var i = 0; i < myroute.legs.length; i++) {
				                total += myroute.legs[i].distance.value;
				            }
				            total = total / 1000;
				            document.getElementById('total').innerHTML = total + ' km';
				        }
				
				    </script>
				</div>
				<div class="col-lg-1 col-md-1 col-sm-1 col-1 p-0">
					<!-- 我是景點列表 -->
					<div class="card bg-light mx-0 my-2 border-dark" id="attList">
						<!--<div class="card-header p-1"> -->
	
						<!-- </div> -->
						<div class="card-body p-1">
							<h3 style="display: inline-block !important;" class="card-title">景點列表</h3>
							<%@ include file="/front_end/trip/include/tripEditpage1.file"%>
							<form method="post"
								action="<%=request.getContextPath()%>/attractions/att.do">
								<div>
									<div class="input-group input-group-sm">
										<input type="hidden" name="action" value="tripEditSearch">
										<input type="text"  placeholder="輸入關鍵字..."
											name="keyword"> 
										<span class="input-group-append">
											<button class="btn btn-default" type="submit">
												<i class="fas fa-search"></i>
											</button>
										</span>
									</div>
								</div>
							</form>
	
							<div>
								<c:forEach var="attVO" items="${list}" begin="<%=pageIndex%>"
									end="<%=pageIndex+rowsPerPage-1%>">
									<div class="col-sm-3 col-md-3 col-lg-3 col-3">
										<div class="card">
											<a href="javascript:void(0)" data-toggle="modal"
												data-target="#attDetail_${attVO.att_no}" class="card-link"
												style="color: #fff" title="${attVO.att_name}">
												<div class="wrapper">
													<img class="card-img" id="${attVO.att_no}"
														src="<%= request.getContextPath()%>/trip/getPicture.do?att_no=${attVO.att_no}">
												</div>
												<div class="card-img-overlay">
													<p class="card-text" style="padding: 40% 0 0 0;">
													<h5 align="center">
														<i class="fas fa-map-marker-alt"></i>${attVO.att_name}</h5>
													</p>
												</div>
											</a>
										</div>
									</div>
								</c:forEach>
							</div>
							<%
								if (list.size() == 0) {
							%>
							<div align="center">
								<h3>查無資料</h3>
							</div>
						<%}%>
						<%@ include file="/front_end/trip/include/tripEditpage2.file"%>
						<!-- //我是景點列表 -->
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 景點Modal區塊  -->
	<c:forEach var="attVO" items="${list}" begin="<%=pageIndex%>"
		end="<%=pageIndex+rowsPerPage-1%>">
			<div class="modal fade" id="attDetail_${attVO.att_no}" tabindex="-1"
				role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header bg-light">
							${attVO.att_name}
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<div class="container-fluid">
								<div class="row">
									<div class="col-lg-7 col-md-7 col-sm-7 col-7">
										<div class="polaroid">
											<img
												src="<%= request.getContextPath()%>/trip/getPicture.do?att_no=${attVO.att_no}"
												alt="${attVO.att_name}" style="width: 100%">
										</div>
									</div>
									<div class="col-lg-5 col-md-5 col-sm-5 col-5">
										<div class="table-responsive">
											<table id="attTable"
												class="table table-bordered table-hover p-0"
												style="font-size: 14px;">
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
								<div class="row">
									<div class="col-lg-12 col-md-12 col-sm-12 col-12">
										<h3>景點介紹</h3>
										<hr>
										<div class="w-100"
											style="font-size: 16px; height: 7rem; overflow-y: auto;">
											<c:out value="${attVO.att_information}" default="尚無內容"></c:out>
										</div>
										<hr>
									</div>
								</div>
							</div>
						</div>
						<div class="modal-footer bg-light">
							<div align="center">
								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">取消</button>
								<button type="submit" class="btn btn-danger" data-dismiss="modal" data-toggle="modal" data-target="#addAttTrip_${attVO.att_no}">加入行程</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			
		<form action="<%=request.getContextPath()%>/trip/trip.do"
			method="post">
			<div class="modal fade" id="addAttTrip_${attVO.att_no}"
				tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
				aria-hidden="true">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-body">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span style="font-size:30px" aria-hidden="true">&times;</span>
							</button>
							<div class="container-fluid">
								<div class="row">
									<div class="col-lg-4 col-md-4 col-sm-4 col-4">
										<div class="polaroid">
											<img
												src="<%= request.getContextPath()%>/trip/getPicture.do?att_no=${attVO.att_no}"
												alt="${attVO.att_name}" style="width: 100%">
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-6">
										<h3>${attVO.att_name}</h3>
									</div>
									<div class="col-lg-2 col-md-2 col-sm-2 col-2 p-0">
										<p style="font-size: 12px">加入哪一天行程</p>
										<select style="font-size: 14px" class="custom-select" name="belongDays">
											<c:forEach var="tripDaysVO" items="${tdList}" varStatus="s">
												<option value="${s.count}">第${tripDaysVO.tripDay_days}天
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-3 col-md-3 col-sm-3 col-3 p-1">
										開始時間
										<div class="input-group">
											<select class="custom-select" name="startHour">
												<option value="" disabled selected>時
												<c:forEach var="i" begin="0" end="23">
													<option value="${i}">${i}
												</c:forEach>
											</select>
											<select class="custom-select" name="startMin">
												<option value="" disabled selected>分
												<c:forEach var="i" begin="0" end="59">
													<option value="${i}">${i}
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-lg-3 col-md-3 col-sm-3 col-3 p-1">
										結束時間
										<div class="input-group">
											<select class="custom-select" name="endHour">
												<option value="" disabled selected>時
												<c:forEach var="i" begin="0" end="23">
													<option value="${i}">${i}
												</c:forEach>
											</select>
											<select class="custom-select" name="endMin">
												<option value="" disabled selected>分
												<c:forEach var="i" begin="0" end="59">
													<option value="${i}">${i}
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-6 p-1">
										花費(TWD)
										<input class="form-control" type="number" min="0" name="attTrip_cost">
									</div>
								</div>
								<hr>
								<div class="row">
									<div class="col-lg-12 col-md-12 col-sm-12 col-12">
										行程備註
										<textarea name="attTrip_note" class="form-control" style="width:100%;height:10rem;resize:none;"></textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="modal-footer bg-light">
							<div align="center">
								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">取消</button>
								<button type="submit" class="btn btn-danger">加入行程</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<input type="hidden" name="att_no" value="${attVO.att_no}">
			<input type="hidden" name="action" value="addAttTrip">
		</form>
	</c:forEach>
	<!-- //景點Modal區塊  -->

	<!-- 交通行程Modal區塊  -->
	<c:forEach var="tripDaysVO" items="${tdList}">
		<form action="<%=request.getContextPath()%>/trip/trip.do"
			method="post">
			<div class="modal fade" id="tt_modal${tripDaysVO.tripDay_days}"
				tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
				aria-hidden="true">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header bg-light">
							<h5 class="modal-title" id="exampleModalLabel">第${tripDaysVO.tripDay_days}天
								增加交通行程</h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<div class="container-fluid">
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-6 col-6 p-1">
										交通類別
										<select class="form-control" name="traTrip_type">
											<c:forEach var="index" begin="0" end="6">
												<option value="${index}" ${index==0?'selected':''}>${traTrip_type.get(index)}
											</c:forEach>
										</select>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-6 p-1">
										交通名稱(線路、航空、車種等)
										<input class="form-control" type="text" name="traTrip_name">
									</div>
									<div class="col-lg-3 col-md-3 col-sm-3 col-3 p-1">
										開始時間
										<div class="input-group">
											<select class="custom-select" name="startHour">
												<option value="" disabled selected>時
												<c:forEach var="i" begin="0" end="23">
													<option value="${i}">${i}
												</c:forEach>
											</select>
											<select class="custom-select" name="startMin">
												<option value="" disabled selected>分
												<c:forEach var="i" begin="0" end="59">
													<option value="${i}">${i}
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-lg-3 col-md-3 col-sm-3 col-3 p-1">
										結束時間
										<div class="input-group">
											<select class="custom-select" name="endHour">
												<option value="" disabled selected>時
												<c:forEach var="i" begin="0" end="23">
													<option value="${i}">${i}
												</c:forEach>
											</select>
											<select class="custom-select" name="endMin">
												<option value="" disabled selected>分
												<c:forEach var="i" begin="0" end="59">
													<option value="${i}">${i}
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-6 p-1">
										花費(TWD)
										<input class="form-control" type="number" min="0" name="traTrip_cost">
									</div>
								</div>
								<hr>
								<div class="row">
									<div class="col-lg-12 col-md-12 col-sm-12 col-12">
										行程備註
										<textarea name="traTrip_note" class="form-control" style="width:100%;height:15rem;resize:none;"></textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="modal-footer bg-light">
							<div align="center">
								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">取消</button>
								<button type="submit" class="btn btn-danger">加入行程</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<input type="hidden" name="belongDays" value="${tripDaysVO.tripDay_days}"> 
			<input type="hidden" name="action" value="addTrafficTrip">
		</form>
	</c:forEach>
	<!-- //交通行程Modal區塊  -->
	
	<!--刪除&編輯modal區域 -->
	<% int dc = 0;%>
	<c:forEach var="tripDaysVO" items="${tdList}" varStatus="s">
	<% dc++;%>
		<% List<Object> dl = tripDayMap.get(dc); %>
		<% for(int j=0;j<dl.size();j++){ %>
			<%//如果是交通行程 %>
			<% if(dl.get(j) instanceof TrafficTripVO) {%>
			<% TrafficTripVO ttVO = (TrafficTripVO)dl.get(j);%>
			<% pageContext.setAttribute("ttVO", ttVO); %>
			<!--交通刪除部分 -->
			<form action="<%=request.getContextPath()%>/trip/trip.do" method="post">
				<div class="modal fade" id="del-${tripDaysVO.tripDay_days}-<%=j%>"
					tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
					aria-hidden="true">
					<div class="modal-dialog modal-dialog-centered" role="document">
						<div class="modal-content">
							<div class="modal-body">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span style="font-size: 30px" aria-hidden="true">&times;</span>
								</button>
								<h4>確定刪除 ${ttVO.traTrip_name} ?</h4>
							</div>
							<div class="modal-footer bg-light">
								<div align="center">
									<button type="button" class="btn btn-secondary"
										data-dismiss="modal">取消</button>
									<button type="submit" class="btn btn-danger">確定</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" name="belongDays" value="${tripDaysVO.tripDay_days}"> 
				<input type="hidden" name="order" value="${ttVO.trip_order}"> 
				<input type="hidden" name="action" value="delectDetailTrip">
			</form>
			<!--//交通刪除部分 -->
			<!--交通編輯部分 -->
			<form action="<%=request.getContextPath()%>/trip/trip.do" method="post">
				<div class="modal fade" id="edit-${tripDaysVO.tripDay_days}-<%=j%>"
					tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
					aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header bg-light">
								<h5 class="modal-title" id="exampleModalLabel">第${tripDaysVO.tripDay_days}天
									修改交通行程</h5>
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<div class="container-fluid">
									<div class="row">
										<div class="col-lg-6 col-md-6 col-sm-6 col-6 p-1">
											交通類別
											<select class="form-control" name="traTrip_type">
												<c:forEach var="index" begin="0" end="6">
													<option value="${index}" ${index==ttVO.traTrip_type?'selected':''}>${traTrip_type.get(index)}
												</c:forEach>
											</select>
										</div>
										<div class="col-lg-6 col-md-6 col-sm-6 col-6 p-1">
											交通名稱(線路、航空、車種等)
											<input class="form-control" type="text" name="traTrip_name" value="${ttVO.traTrip_name}">
										</div>
										<div class="col-lg-3 col-md-3 col-sm-3 col-3 p-1">
											開始時間
											<div class="input-group">
												<select class="custom-select" name="startHour">
													<option value="" disabled selected>時
													<c:forEach var="i" begin="0" end="23">
													<fmt:parseNumber var="startHour" integerOnly="true" value="${(ttVO.traTrip_start)/60}" />
														<option value="${i}" ${i==startHour? 'selected':''}>${i}
													</c:forEach>
												</select>
												<select class="custom-select" name="startMin">
													<option value="" disabled selected>分
													<c:forEach var="i" begin="0" end="59">
													<fmt:parseNumber var="startMin" integerOnly="true" value="${(ttVO.traTrip_start)%60}" />
														<option value="${i}" ${i==startMin? 'selected':''}>${i}
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="col-lg-3 col-md-3 col-sm-3 col-3 p-1">
											結束時間
											<div class="input-group">
												<select class="custom-select" name="endHour">
													<option value="" disabled selected>時
													<c:forEach var="i" begin="0" end="23">
													<fmt:parseNumber var="endHour" integerOnly="true" value="${(ttVO.traTrip_end)/60}" />
														<option value="${i}" ${i==endHour? 'selected':''}>${i}
													</c:forEach>
												</select>
												<select class="custom-select" name="endMin">
													<option value="" disabled selected>分
													<c:forEach var="i" begin="0" end="59">
													<fmt:parseNumber var="endMin" integerOnly="true" value="${(ttVO.traTrip_end)%60}" />
														<option value="${i}" ${i==endMin? 'selected':''}>${i}
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="col-lg-6 col-md-6 col-sm-6 col-6 p-1">
											花費(TWD)
											<input class="form-control" type="number" min="0" name="traTrip_cost" value="${ttVO.traTrip_cost}">
										</div>
									</div>
									<hr>
									<div class="row">
										<div class="col-lg-12 col-md-12 col-sm-12 col-12">
											行程備註
											<textarea name="traTrip_note" class="form-control" style="width:100%;height:15rem;resize:none;">${ttVO.traTrip_note}</textarea>
										</div>
									</div>
								</div>
							</div>
							<div class="modal-footer bg-light">
								<div align="center">
									<button type="button" class="btn btn-secondary"
										data-dismiss="modal">取消</button>
									<button type="submit" class="btn btn-danger">修改行程</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" name="belongDays" value="${tripDaysVO.tripDay_days}"> 
				<input type="hidden" name="order" value="${ttVO.trip_order}"> 
				<input type="hidden" name="action" value="editTraTrip">
			</form>
			<!--//交通編輯部分 -->
			<%//如果是景點行程 %>
			<%}else if(dl.get(j) instanceof AttractionsTripVO){%>
			<% AttractionsTripVO atVO = (AttractionsTripVO)dl.get(j);%>
			<% pageContext.setAttribute("atVO", atVO); %>
			<!--景點刪除部分 -->
			<form action="<%=request.getContextPath()%>/trip/trip.do" method="post">
				<div class="modal fade" id="del-${tripDaysVO.tripDay_days}-<%=j%>"
					tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
					aria-hidden="true">
					<div class="modal-dialog modal-dialog-centered" role="document">
						<div class="modal-content">
							<div class="modal-body">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span style="font-size: 30px" aria-hidden="true">&times;</span>
								</button>
								<h4>確定刪除 ${attSvc.getOneAttByPK(atVO.att_no).att_name} ?</h4>
							</div>
							<div class="modal-footer bg-light">
								<div align="center">
									<button type="button" class="btn btn-secondary"
										data-dismiss="modal">取消</button>
									<button type="submit" class="btn btn-danger">確定</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" name="belongDays" value="${tripDaysVO.tripDay_days}"> 
				<input type="hidden" name="order" value="${atVO.trip_order}"> 
				<input type="hidden" name="action" value="delectDetailTrip">
			</form>	
			<!--//景點刪除部分 -->
			
			<!--景點編輯部分 -->
			<form action="<%=request.getContextPath()%>/trip/trip.do" method="post">
				<div class="modal fade" id="edit-${tripDaysVO.tripDay_days}-<%=j%>"
					tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
					aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header bg-light">
								<h5 class="modal-title" id="exampleModalLabel">第${tripDaysVO.tripDay_days}天
									修改景點行程</h5>
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<div class="container-fluid">
									<div class="row">
										<div class="col-lg-4 col-md-4 col-sm-4 col-4">
											<div class="polaroid">
												<img
													src="<%= request.getContextPath()%>/trip/getPicture.do?att_no=${atVO.att_no}"
													alt="${attSvc.getOneAttByPK(atVO.att_no).att_name}" style="width: 100%">
											</div>
										</div>
										<div class="col-lg-8 col-md-8 col-sm-8 col-8">
											<h3>${attSvc.getOneAttByPK(atVO.att_no).att_name}</h3>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-3 col-md-3 col-sm-3 col-3 p-1">
											開始時間
											<div class="input-group">
												<select class="custom-select" name="startHour">
													<option value="" disabled selected>時
													<c:forEach var="i" begin="0" end="23">
													<fmt:parseNumber var="startHour" integerOnly="true" value="${(atVO.attTrip_start)/60}" />
														<option value="${i}" ${i==startHour? 'selected':''}>${i}
													</c:forEach>
												</select>
												<select class="custom-select" name="startMin">
													<option value="" disabled selected>分
													<c:forEach var="i" begin="0" end="59">
													<fmt:parseNumber var="startMin" integerOnly="true" value="${(atVO.attTrip_start)%60}" />
														<option value="${i}" ${i==startMin? 'selected':''}>${i}
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="col-lg-3 col-md-3 col-sm-3 col-3 p-1">
											結束時間
											<div class="input-group">
												<select class="custom-select" name="endHour">
													<option value="" disabled selected>時
													<c:forEach var="i" begin="0" end="23">
													<fmt:parseNumber var="endHour" integerOnly="true" value="${(atVO.attTrip_end)/60}" />
														<option value="${i}" ${i==endHour? 'selected':''}>${i}
													</c:forEach>
												</select>
												<select class="custom-select" name="endMin">
													<option value="" disabled selected>分
													<c:forEach var="i" begin="0" end="59">
													<fmt:parseNumber var="endMin" integerOnly="true" value="${(atVO.attTrip_end)%60}" />
														<option value="${i}" ${i==endMin? 'selected':''}>${i}
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="col-lg-6 col-md-6 col-sm-6 col-6 p-1">
											花費(TWD)
											<input class="form-control" type="number" min="0" name="attTrip_cost" value="${atVO.attTrip_cost}">
										</div>
									</div>
									<hr>
									<div class="row">
										<div class="col-lg-12 col-md-12 col-sm-12 col-12">
											行程備註
											<textarea name="attTrip_note" class="form-control" style="width:100%;height:10rem;resize:none;">${atVO.attTrip_note}</textarea>
										</div>
									</div>
								</div>
							</div>
							<div class="modal-footer bg-light">
								<div align="center">
									<button type="button" class="btn btn-secondary"
										data-dismiss="modal">取消</button>
									<button type="submit" class="btn btn-danger">修改行程</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" name="belongDays" value="${tripDaysVO.tripDay_days}"> 
				<input type="hidden" name="order" value="${atVO.trip_order}"> 
				<input type="hidden" name="action" value="editAttTrip">
			</form>
			<!--//景點編輯部分 -->
			<%} %>
		<%} %>
		<!--行程天住宿資訊編輯-->
		<form action="<%=request.getContextPath()%>/trip/trip.do"
			method="post">
			<div class="modal fade" id="editHotel-${tripDaysVO.tripDay_days}"
				tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
				aria-hidden="true">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header bg-light">
							<h5 class="modal-title" id="exampleModalLabel">第${tripDaysVO.tripDay_days}天
								編輯住宿資訊</h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<div class="container-fluid">
								<div class="row">
									<div class="col-lg-12 col-md-12 col-sm-12 col-12 p-1">
										住處名稱 
										<input class="form-control" type="text" name="tripDay_hotelName" value="${tripDaysVO.tripDay_hotelName}">
									</div>
									<div class="col-lg-3 col-md-3 col-sm-3 col-3 p-1">
										入住時間
										<div class="input-group">
											<select class="custom-select" name="startHour">
												<option value="" disabled selected>時
													<c:forEach var="i" begin="0" end="23">
														<fmt:parseNumber var="startHour" integerOnly="true"
															value="${(tripDaysVO.tripDay_hotelStart)/60}" />
														<option value="${i}" ${i==startHour? 'selected':''}>${i}
													</c:forEach>
											</select> <select class="custom-select" name="startMin">
												<option value="" disabled selected>分
													<c:forEach var="i" begin="0" end="59">
														<fmt:parseNumber var="startMin" integerOnly="true"
															value="${(tripDaysVO.tripDay_hotelStart)%60}" />
														<option value="${i}" ${i==startMin? 'selected':''}>${i}
													</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-6 col-lg-offset-3 col-md-offset-3 col-sm-offset-3 col-offset-3 p-1">
										花費(TWD) <input class="form-control" type="number" min="0"
											name="tripDay_hotelCost" value="${tripDaysVO.tripDay_hotelCost}">
									</div>
								</div>
								<hr>
								<div class="row">
									<div class="col-lg-12 col-md-12 col-sm-12 col-12">
										住宿備註
										<textarea name="tripDay_hotelNote" class="form-control"
											style="width: 100%; height: 15rem; resize: none;">${tripDaysVO.tripDay_hotelNote}</textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="modal-footer bg-light">
							<div align="center">
								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">取消</button>
								<button type="submit" class="btn btn-danger">修改住宿</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<input type="hidden" name="index" value="${s.index}"> 
			<input type="hidden" name="action" value="editHotel">
		</form>
		<!--//行程天住宿資訊編輯-->
	</c:forEach>
	<!--//刪除&編輯modal區域 -->

	<!-- footer -->

<!-- 	<div class="footer"> -->
<!-- 		<div class="container"> -->
<!-- 			<div class="footer-grids"> -->
<!-- 				<div class="col-md-3 footer-grid"> -->
<!-- 					<div class="footer-grid-heading"> -->
<!-- 						<h4>關於我們</h4> -->
<!-- 					</div> -->
<!-- 					<div class="footer-grid-info"> -->
<!-- 						<ul> -->
<%-- 							<li><a href="<%=request.getContextPath()%>/front_end/about_us/about_us.jsp">關於Travel Maker</a></li> --%>
<%-- 							<li><a href="<%=request.getContextPath()%>/front_end/content/content.jsp">聯絡我們</a></li> --%>
<%-- 							<li><a href="<%=request.getContextPath()%>/front_end/faq/faq.jsp">常見問題</a></li> --%>
<!-- 						</ul> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				<div class="col-md-3 footer-grid"> -->
<!-- 					<div class="footer-grid-heading"> -->
<!-- 						<h4>網站條款</h4> -->
<!-- 					</div> -->
<!-- 					<div class="footer-grid-info"> -->
<!-- 						<ul> -->
<!-- 							<li><a href="about.html">服務條款</a></li> -->
<!-- 							<li><a href="services.html">隱私權條款</a></li> -->
<!-- 						</ul> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				<div class="col-md-3 footer-grid"> -->
<!-- 					<div class="footer-grid-heading"> -->
<!-- 						<h4>社群</h4> -->
<!-- 					</div> -->
<!-- 					<div class="social"> -->
<!-- 						<ul> -->
<!-- 							<li><a href="https://www.facebook.com/InstaBuy.tw/"><i -->
<!-- 									class="fab fa-facebook"></i></a></li> -->
<!-- 							<li><a href="https://www.instagram.com/"><i -->
<!-- 									class="fab fa-instagram"></i></a></li> -->
<!-- 							<li><a href="#"><i class="fab fa-line"></i></a></li> -->
<!-- 						</ul> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				<div class="col-md-3 footer-grid"> -->
<!-- 					<div class="footer-grid-heading"> -->
<!-- 						<h4>訂閱電子報</h4> -->
<!-- 					</div> -->
<!-- 					<div class="footer-grid-info"> -->
<!-- 						<form action="#" method="post"> -->
<!-- 							<input type="email" id="mc4wp_email" name="EMAIL" -->
<!-- 								placeholder="請輸入您的Email" required> <input -->
<!-- 								type="submit" value="訂閱"> -->
<!-- 						</form> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				<div class="clearfix"></div> -->
<!-- 			</div> -->
<!-- 			<div class="copyright"> -->
<!-- 				<p> -->
<!-- 					Copyright &copy; 2018 All rights reserved <a href="<%=request.getContextPath()%>/front_end/index.jsp" -->
<!-- 						target="_blank" title="TravelMaker">TravelMaker</a> -->
<!-- 				</p> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
	<!-- //footer -->
</body>

<!-- =========================================以下為 datetimepicker 之相關設定========================================== -->

<%
	java.sql.Date trip_startDay = null;
	try {
		trip_startDay = tripVO.getTrip_startDay();
	} catch (Exception e) {
		trip_startDay = new java.sql.Date(System.currentTimeMillis());
	}
%>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/trip/datetimepicker/jquery.datetimepicker.css" />
<script
	src="<%=request.getContextPath()%>/front_end/trip/datetimepicker/jquery.js"></script>
<script
	src="<%=request.getContextPath()%>/front_end/trip/datetimepicker/jquery.datetimepicker.full.js"></script>

<style>
.xdsoft_datetimepicker .xdsoft_datepicker {
	width: 300px; /* width:  300px; */
}

.xdsoft_datetimepicker .xdsoft_timepicker .xdsoft_time_box {
	height: 151px; /* height:  151px; */
}
</style>

<script>
        $.datetimepicker.setLocale('zh');
        $('#f_date1').datetimepicker({
	       theme: '',              //theme: 'dark',
	       timepicker:false,       //timepicker:true,
	       step: 1,                //step: 60 (這是timepicker的預設間隔60分鐘)
	       format:'Y-m-d',         //format:'Y-m-d H:i:s',
		   value: '<%=trip_startDay%>',
	// value:   new Date(),
	//disabledDates:        ['2017/06/08','2017/06/09','2017/06/10'], // 去除特定不含
	//startDate:	            '2017/07/10',  // 起始日
	//minDate:               '-1970-01-01', // 去除今日(不含)之前
	//maxDate:               '+1970-01-01'  // 去除今日(不含)之後
	});

	// ----------------------------------------------------------以下用來排定無法選擇的日期-----------------------------------------------------------

	//      1.以下為某一天之前的日期無法選擇
	//      var somedate1 = new Date('2017-06-15');
	//      $('#f_date1').datetimepicker({
	//          beforeShowDay: function(date) {
	//        	  if (  date.getYear() <  somedate1.getYear() || 
	//		           (date.getYear() == somedate1.getYear() && date.getMonth() <  somedate1.getMonth()) || 
	//		           (date.getYear() == somedate1.getYear() && date.getMonth() == somedate1.getMonth() && date.getDate() < somedate1.getDate())
	//              ) {
	//                   return [false, ""]
	//              }
	//              return [true, ""];
	//      }});

	//      2.以下為某一天之後的日期無法選擇
	//      var somedate2 = new Date('2017-06-15');
	//      $('#f_date1').datetimepicker({
	//          beforeShowDay: function(date) {
	//        	  if (  date.getYear() >  somedate2.getYear() || 
	//		           (date.getYear() == somedate2.getYear() && date.getMonth() >  somedate2.getMonth()) || 
	//		           (date.getYear() == somedate2.getYear() && date.getMonth() == somedate2.getMonth() && date.getDate() > somedate2.getDate())
	//              ) {
	//                   return [false, ""]
	//              }
	//              return [true, ""];
	//      }});

	//      3.以下為兩個日期之外的日期無法選擇 (也可按需要換成其他日期)
	//      var somedate1 = new Date('2017-06-15');
	//      var somedate2 = new Date('2017-06-25');
	//      $('#f_date1').datetimepicker({
	//          beforeShowDay: function(date) {
	//        	  if (  date.getYear() <  somedate1.getYear() || 
	//		           (date.getYear() == somedate1.getYear() && date.getMonth() <  somedate1.getMonth()) || 
	//		           (date.getYear() == somedate1.getYear() && date.getMonth() == somedate1.getMonth() && date.getDate() < somedate1.getDate())
	//		             ||
	//		            date.getYear() >  somedate2.getYear() || 
	//		           (date.getYear() == somedate2.getYear() && date.getMonth() >  somedate2.getMonth()) || 
	//		           (date.getYear() == somedate2.getYear() && date.getMonth() == somedate2.getMonth() && date.getDate() > somedate2.getDate())
	//              ) {
	//                   return [false, ""]
	//              }
	//              return [true, ""];
	//      }});
</script>

<!--gMap使用 -->
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC_J7BIoAiflRgiGrns3ins84UVvBXHC10&callback=initMap"></script>


<script type="text/javascript">
	$(document).ready(function() {
		//註冊 行程日期 change事件
		$("#f_date1").change(function() {
			this.form.submit();
		});
		//註冊 滾動事件
		<%int countGM = 1; %>
		<c:forEach var="tripDaysVO" items="${tdList}">
		$("#btn_${tripDaysVO.tripDay_days}").click(function() {
			$("html,body").animate({
				scrollTop : $("#card_${tripDaysVO.tripDay_days}").offset().top
			}, 500);
			waypts.length = 0;
			<% attTripList = new ArrayList<>(); %>
			<% detailListGM = tripDayMap.get(countGM++);%>
			<% for(Object detail : detailListGM){
					if(detail instanceof AttractionsTripVO){
						attTripList.add((AttractionsTripVO)detail);
					}
				}
			%>
			<% pageContext.setAttribute("attTripList", attTripList);%>
			<%System.out.println(attTripList.size()); %>
			<%if(attTripList==null){%>
			<%}else if(attTripList.size()>2){%>
				<c:forEach var="attTripGMap" items="${attTripList}" begin="1" end="${attTripList.size()-1}" varStatus="s">
					waypts.push({
		                location: new google.maps.LatLng(${attSvc.getOneAttByPK(attTripGMap.att_no).att_lat}, ${attSvc.getOneAttByPK(attTripGMap.att_no).att_lon}),
		                stopover: true
		            });
	            </c:forEach>
	            
	            displayRoute(
	            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lon()%>),
	            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(attTripList.size()-1).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(attTripList.size()-1).getAtt_no()).getAtt_lon()%>),
	            		directionsService,
	            		directionsDisplay);
        	<%}else if(attTripList.size()==2){%>
        		displayRoute(
            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lon()%>),
            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(attTripList.size()-1).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(attTripList.size()-1).getAtt_no()).getAtt_lon()%>),
            		directionsService,
            		directionsDisplay);
        	<%}else if(attTripList.size()==1){%>
            	displayRoute(
	            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lon()%>),
	            		new google.maps.LatLng(<%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lat()%>, <%= attSvc.getOneAttByPK(attTripList.get(0).getAtt_no()).getAtt_lon()%>),
	            		directionsService,
	            		directionsDisplay);
        	<%}%>
						
		});
		</c:forEach>
		
        $("#attList").hover(function(){
            $(this).stop().animate({right: "0"});
       	}, function(){
	    	 $(this).stop().animate({right: "-38.5rem"});
	    }, 800);
	});
</script>
</html>