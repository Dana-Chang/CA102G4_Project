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
	
	String trip_no = request.getParameter("trip_no");

	TripService tripSvc = new TripService();
	TripDaysService tdSvc = new TripDaysService();
	TrafficTripService ttSvc = new TrafficTripService();
	AttractionsTripService atSvc = new AttractionsTripService();
	AttractionsService attSvc = new AttractionsService();
	
	TripVO tripVO = tripSvc.getOneTripByPK(trip_no);
	
	List<TripDaysVO> tdList = tdSvc.getByTrip_no(trip_no);
	
	Map<Integer,List<Object>> tripDayMap = new LinkedHashMap<>();
	
	for(TripDaysVO tdVO : tdList){
		List<AttractionsTripVO> atList = atSvc.getByTripDays_no(tdVO.getTripDay_no());
		List<TrafficTripVO> ttList = ttSvc.getByTripDays_no(tdVO.getTripDay_no());
		int detailSize = atList.size() + ttList.size();
		List<Object> detailList = new ArrayList<>();
		for(int i = 1;i<= detailSize;i++){
			for(AttractionsTripVO atVO : atList){
				if(atVO.getTrip_order() == i){
					detailList.add(atVO);
					continue;
				}
			}
			for(TrafficTripVO ttVO : ttList){
				if(ttVO.getTrip_order() == i){
					detailList.add(ttVO);
					continue;
				}
			}
		}
		tripDayMap.put(tdVO.getTripDay_days(), detailList);
	}
	//測試用
// 	int count=0;
// 	for(Map.Entry<Integer, List<Object>> entry : tripDayMap.entrySet()) {
// 		System.out.println("第"+(++count)+"天有"+entry.getValue().size()+"個行程");
// 	}
	
	pageContext.setAttribute("tripVO", tripVO);
	pageContext.setAttribute("tdList", tdList);
	pageContext.setAttribute("tripDayMap", tripDayMap);
	pageContext.setAttribute("attSvc", attSvc);
%>

<jsp:useBean id="memSvc" scope="page" class="com.mem.model.MemberService"/>

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

<html>
<head>
<!-- 網頁title -->
<title>${tripVO.trip_name}</title>
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

<!-- LogoIcon -->
<link href="<%=request.getContextPath()%>/front_end/images/all/Logo_Black_use.png" rel="icon" type="image/png">
<!-- //LogoIcon -->

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

<!-- SweetAlert CDN -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<script
	src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>

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
								<!-- 判斷是否登入，若有登入將會出現登出按鈕 --> <c:choose>
									<c:when test="<%=login_state%>">
										<a href="<%=request.getContextPath()%>/front_end/member/member.do?action=logout"><span
											class=" top_banner"><i class=" fas fa-sign-out-alt"
												aria-hidden="true"></i></span></a>
									</c:when>
									<c:otherwise>
										<a href="<%=request.getContextPath()%>/front_end/member/mem_login.jsp"><span
											class="top_banner"><i class=" fa fa-user"
												aria-hidden="true"></i></span></a>
									</c:otherwise>
								</c:choose>
							</li>
							<li style="<%=logout%>"><a class="top_banner"
								href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_home.jsp"><i
									class="fa fa-user" aria-hidden="true"></i></a></li>
							<li><a class="top_banner" href="<%=request.getContextPath()%>/front_end/store/store_cart.jsp"><i class="fa fa-shopping-cart shopping-cart" aria-hidden="true"></i><span class="badge">${total_items}</span></a></li>
							<li><a class="top_banner" href="#"><i
									class="fa fa-envelope" aria-hidden="true"></i></a></li>
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
	
	<div class="container mt-3">
		<div class="row">
			<div class="col-lg-6 col-md-6 col-sm-6 col-6">
				<h2 class="m-0" id="tripName">${tripVO.trip_name}</h2>
				<div class="m-2">
				<img class="ui avatar image" style="width: 3em;height: 3em;border-radius: 500rem;"
				 src="<%= request.getContextPath()%>/trip/getPicture.do?mem_id=${tripVO.mem_id}">
					<a style="text-decoration: none;">${memSvc.getOneMember(tripVO.mem_id).mem_Name}</a>
				</div>
				<div>
					
				</div>
			</div>
				
			<div class="col-lg-6 col-md-6 col-sm-6 col-6">
				<div align="right">
					<button class="btn btn-success btn-collectTrip" onclick="my_function()"><i class="fas fa-edit"></i>&nbsp;收藏</button>
				</div>

			</div>
		</div>
	</div>
	<hr>
	<div class="container-fluid">
		<div class="row">
		<!--行程天數 -->
			<div class="col-lg-1 col-md-1 col-sm-1 col-1 p-0">
				<div class="card bg-light mx-0 my-2 w-100" style="overflow-y: auto;">
					<c:forEach var="tripDaysVO" items="${tdList}">
						<button id="btn_${tripDaysVO.tripDay_days}"
							class="btn btn-info m-2 p-2 w-auto">第${tripDaysVO.tripDay_days}天</button>
					</c:forEach>
				</div>
			</div>
			<!--//行程天數 -->
		
		<!--行程內容-->
			<div class="col-lg-5 col-md-5 col-sm-5 col-5">
				<div id="card_tripDay" class="card bg-light p-2 mx-0 my-2 w-100"
					style="overflow-y: auto;">
					<div class="card p-2">
						<div class="card-body p-2" style="text-align: center">
							<h4 class="card-title">共${tdList.size()}天</h4>
							<p class="card-text" style="font-size: 14px">
								<i class="far fa-calendar-alt"></i>&nbsp;<%= tdList.get(0).getTripDay_date()%> ~ <%=tdList.get(tdList.size()-1).getTripDay_date() %>
							</p>
						</div>
					</div>
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
						</div>
					</c:forEach>
				</div>
			</div>
			<!--//行程內容-->
			<div class="col-lg-6 col-md-6 col-sm-6 col-6">
				<div class="card bg-light mx-0 my-2" id="gmap-all" style="right: 1rem;width:46rem;">
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
		</div>
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
	
<!--gMap使用 -->
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC_J7BIoAiflRgiGrns3ins84UVvBXHC10&callback=initMap"></script>
	
	
	<script>
	function my_function(){
		$.ajax({
			url:"<%=request.getContextPath()%>/trip/trip.do",
			data:{
				'action':'collectTrip',
				'trip_no':'${tripVO.trip_no}'
				},
			type:'POST',
			success: function(result){
				if(result=="fail"){
<%-- 					window.location="<%= request.getContextPath()+"/front_end/member/mem_login.jsp"%>"; --%>
					swal('尚未登入', 'You clicked the button!', 'error');
				}else if(result=="fail2"){
					swal('已加入收藏', 'You clicked the button!', 'info');
				}else if(result=="ok"){
					swal('收藏成功', 'You clicked the button!', 'success');
				}
		}});
	}
		
	</script>
	
	<script type="text/javascript">
	$(document).ready(function() {
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
	});
</script>
	
</body>
</html>