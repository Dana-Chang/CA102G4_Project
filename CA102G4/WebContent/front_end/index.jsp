<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ page import="java.util.*"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.blog.model.*"%>
<%@ page import="com.mem.model.*"%>
<%
	request.setCharacterEncoding("UTF-8");
	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-store"); 
	response.setDateHeader("Expires", 0);
	 
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

<!DOCTYPE html>
<html>
<jsp:useBean id="blogSvc" scope="page" class="com.blog.model.blogService"></jsp:useBean>
<jsp:useBean id="memSvc" scope="page" class="com.mem.model.MemberService"></jsp:useBean>
<jsp:useBean id="adSvc" scope="page" class="com.ad.model.AdService"></jsp:useBean>
<jsp:useBean id="grpSvc" scope="page" class="com.grp.model.GrpService"></jsp:useBean>
<jsp:useBean id="attSvc" scope="page" class="com.attractions.model.AttractionsService"></jsp:useBean>


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

    <!-- JQUERY -->
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <!-- //JQUERY -->

    <!-- bootstrap css及JS檔案 -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <script src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
    <!-- //bootstrap css及JS檔案 -->

    <!-- css -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_style.css" rel="stylesheet" type="text/css" media="all" />
    <!-- //css -->

    <!-- js -->
    <script src="<%=request.getContextPath()%>/front_end/js/all/index.js"></script>
    <!-- //js -->
    
    <!-- 幻燈片css -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_ken_burns.css" rel="stylesheet">
    <!-- //幻燈片css -->

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
    <div class="banner" style="width: 100%;left:0;">
        <div class="top-banner" style="border-bottom: 0;">
            <div class="container">
                <div class="top-banner-left">
                    <ul>
                        <li><i class="fa fa-phone" aria-hidden="true"></i>
                            <a href="tel:034257387"> 03-4257387</a></li>
                        <li><a href="mailto:TravelMaker@gmail.com"><i class="fa fa-envelope" aria-hidden="true"></i> TravelMaker@gmail.com</a></li>
                    </ul>
                </div>
                <div class="top-banner-right">
                		
                		<!-- 暫時登出用的 -->
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
        <hr style="width: 1200px;border-top: 1px solid #eee;margin-top: 0px;margin-bottom: 0px;">
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
								<div class="clearfix"></div>
							</ul>
                        </div>
                    </nav>
                </div>
                <div class="clearfix"> </div>
            </div>
        </div>
    </div>
    <!-- //banner -->
    <div id="kb" class="carousel kb_elastic animate_text kb_wrapper" data-ride="carousel" data-interval="6000" data-pause="hover">
        <!-- 搜尋 -->
        <FORM METHOD="GET" ACTION="<%=request.getContextPath()%>/blog.do">
	        <div class="row" style="position: absolute">
	            <div class="col-sm-6 col-sm-offset-3">
	                <div class="input-group" id="search_place">
	                    <input type="text" name="keyword" class="form-control" placeholder="搜尋遊記、揪團、問答、會員、景點">
	                    <input type="hidden" name="action" value="searchAll">
	                    <span class="input-group-btn">
	                <button class="btn btn-default submit" type="submit">
	                    <i class="fa fa-search"></i>
	                </button>
	            </span>
	                </div>
	            </div>
	        </div>
        </form>
        <!-- //搜尋 -->
        <!-- Wrapper for Slides 幻燈片 熱門景點 -->
        <div class="carousel-inner" role="listbox">

            <!-- First Slide 第一張幻燈片 -->
            <div class="item active">
                <div class="slider">
                    <a href="#">
                        <div class="carousel-caption kb_caption slider-grid" style="padding:7.2em 0 0 0;">
                            <h3>Singapore</h3>
<!--                             <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p> -->
                        </div>
                    </a>
                </div>
            </div>

            <!-- Second Slide 第二張幻燈片 -->
            <div class="item">
                <div class="slider slider1">
                    <a href="#">
                        <div class="carousel-caption kb_caption kb_caption_right slider-grid" style="padding:7.2em 0 0 0;">
                            <h3>Hawaii</h3>
<!--                             <p>Vivamus vel nulla venenatis, tincidunt mi vel, consequat erat.</p> -->
                        </div>
                    </a>
                </div>
            </div>

            <!-- Third Slide 第三張幻燈片 -->
            <div class="item">
                <div class="slider slider2">
                    <a href="#">
                        <div class="carousel-caption kb_caption kb_caption_center slider-grid" style="padding:7.2em 0 0 0;">
                            <h3>Hong Kong</h3>
<!--                             <p>Nunc turpis purus, vestibulum at quam ac, feugiat dignissim nunc</p> -->
                        </div>
                    </a>
                </div>
            </div>
            
        </div>
        <!-- //Wrapper for Slides 幻燈片 熱門景點 -->
        <!-- Left Button 切換幻燈片左按鈕 -->
        <a class="left carousel-control kb_control_left kb_icons" href="#kb" role="button" data-slide="prev">
            <span class="fas fa-angle-left" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <!-- Right Button 切換幻燈片右按鈕 -->
        <a class="right carousel-control kb_control_right kb_icons" href="#kb" role="button" data-slide="next">
            <span class="fas fa-angle-right" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>

    <!-- 熱門行程 -->
    <div class="about">
        <div class="container">
            <div class="w3l-about-heading">
                <h2>Top Tours</h2>
            </div>

            <div class="about-grids">
            
            <c:forEach var="attVO" items="${attSvc.allRandom}">
                <div class="col-md-3 about-grid">
                    <div class="about-grid-info effect-1" style="background: url('<%= request.getContextPath()%>/trip/getPicture.do?att_no=${attVO.att_no}') no-repeat 50% 50%;background-size:cover;border-radius:50%;overflow:hidden;white-space: nowrap;">
                        <a href="<%=request.getContextPath()%>/front_end/attractions/attDetail.jsp?att_no=${attVO.att_no}">
                            <h4 style="font-family:'Oswald','Noto Sans TC', sans-serif !important">${attVO.att_name}</h4>
                        </a>
                    </div>
                </div>
			</c:forEach>

                <div class="clearfix"> </div>
            </div>
        </div>
    </div>
    <!-- //熱門景點 -->

    <!-- 熱門旅遊記 -->
    <div class="news">
        <div class="container">
            <div class="news-heading">
                <h3>Popular Travels</h3>
            </div>
            <div class="news-grids" style="display:flex">
				<c:forEach var="blogVO" items="${blogSvc.allByHotFour}">
	                <div class="col-md-3 news-grid" style="display:flex">
	                    <div class="agile-news-grid-info" style="width:100%">
	                        <div class="news-grid-info-img">
	                            <a href="<%=request.getContextPath()%>/blog.do?action=article&blogID=${blogVO.blog_id}">
	                            	<img src="<%=request.getContextPath()%>/blogPicReader?blog_id=${blogVO.blog_id}" alt="${blogVO.blog_title}" />
	                            </a>
	                        </div>
	                        <div class="news-grid-info-bottom">
	                            <div class="date-grid">
	                                <div class="admin" style="line-height: 18px;">
	                                    <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogVO.mem_id}">
	                                    	<i class="fa fa-user" aria-hidden="true"></i> ${memSvc.findByPrimaryKey(blogVO.mem_id).mem_Name}</a>
	                                </div>
	                                <div class="time" style="float:unset">
	                                    <p><i class="fa fa-calendar" aria-hidden="true"></i> ${blogVO.travel_date}</p>
	                                </div>
	                                <div class="clearfix"> </div>
	                            </div>
	                            <div class="news-grid-info-bottom-text">
	                                <a href="<%=request.getContextPath()%>/blog.do?action=article&blogID=${blogVO.blog_id}">${blogVO.blog_title}</a>
	                                <c:set var="blog_content" value="${blogVO.blog_content}"/> 
	                                <p class="news-grid-info-bottom-text-content"><%= ((String)pageContext.getAttribute("blog_content")).replaceAll("<[^>]*>","").trim()%></p>
	                            </div>
	                        </div>
	                    </div>
	                </div>
				</c:forEach>
               	<div class="clearfix"> </div>
            </div>
        </div>
    </div>
    <!-- //熱門旅遊記 -->

    <!-- 最新旅遊記 -->
    <div class="news">
        <div class="container">
            <div class="news-heading">
                <h3>New Together</h3>
            </div>
            <div class="news-grids" style="display:flex">
				<c:forEach var="grpVO" items="${grpSvc.all}" begin="0" end="3">
	                <div class="col-md-3 news-grid" style="display:flex">
	                    <div class="agile-news-grid-info" style="width:100%">
	                        <div class="news-grid-info-img">
	                            <a href="<%=request.getContextPath()%>/front_end/grp/grp_oneview.jsp?grp_Id=${grpVO.grp_Id}">
	                            	<img src="<%=request.getContextPath()%>/grpPicReader?grp_Id=${grpVO.grp_Id}" alt="" style="height:179.250px;"/>
	                            </a>
	                        </div>
	                        <div class="news-grid-info-bottom">
	                            <div class="date-grid">
	                                <div class="admin" style="line-height: 18px;">
	                                    <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${grpVO.mem_Id}"><i class="fa fa-user" aria-hidden="true"></i> 
	                                    	${memSvc.findByPrimaryKey(grpVO.mem_Id).mem_Name}
	                                    </a>
	                                </div>
	                                <div class="time" style="float:unset">
	                                    <p><i class="fa fa-calendar" aria-hidden="true"></i>
	                                    <fmt:formatDate value="${grpVO.grp_Start}" type="date"/>
	                                    </p>
	                                </div>
	                                
	                           		<div class="clearfix"> </div>
	                           	</div>
	                            <div class="date-grid">
	                                <div class="admin" style="line-height: 18px;margin-top: 10px;">
	                                    <a href="<%=request.getContextPath()%>/front_end/grp/grp_oneview.jsp?grp_Id=${grpVO.grp_Id}"><i class="fas fa-map-marker-alt"></i>
	                                    	${grpVO.trip_Locale}
	                                    </a>
	                                </div>
	                                <div class="time" style="float:unset;line-height:35px">
	                                    <p><img class="price" src="<%=request.getContextPath()%>/front_end/fonts/all/dollar-coin-money.svg" style="width:18px;height:18px"></i>
	                                    	${grpVO.grp_Price}
	                                    </p>
	                                </div>
	                                
	                                <div class="clearfix"> </div>
	                            </div>
	                            <div class="news-grid-info-bottom-text">
	                                <a href="<%=request.getContextPath()%>/front_end/grp/grp_oneview.jsp?grp_Id=${grpVO.grp_Id}">
	                                ${grpVO.grp_Title}
	                                </a>
	                                <c:set var="trip_Details" value="${grpVO.trip_Details}"/>   
	                                <p class="news-grid-info-bottom-text-content"><%= ((String)pageContext.getAttribute("trip_Details")).replaceAll("<[^>]*>","").trim()%></p>
	                            </div>
	                        </div>
	                    </div>
	                </div>
				</c:forEach>
				
				
               		<div class="clearfix"> </div>
            </div>
        </div>
    </div>
    <!-- //最新旅遊記 -->
    
    
    <!-- 熱門促銷 -->
    <div id="kb2" class="carousel kb_elastic animate_text kb_wrapper" data-ride="carousel" data-interval="6000" data-pause="hover">
        <ol class="carousel-indicators">
        	<c:forEach var="adList" items="${adSvc.hotAD}" begin="0" end="0">
            	<li data-target="#kb2" class="active"></li>
            </c:forEach>
            <c:forEach var="adList" items="${adSvc.hotAD}" begin="1" end="${adSvc.hotAD.size()-1}">
            	<li data-target="#kb2"></li>
            </c:forEach>
        </ol>
        <div class="carousel-inner" role="listbox">
            <!-- 投影片 -->
            <c:forEach var="adVO" items="${adSvc.hotAD}" begin="0" end="0">
	            <div class="item active">
	                <div class="offer">
	                    <div class="container">
	                        <a href="${adVO.ad_Link}" target="_blank">
	                            <div class="wthree-offer-grid" style="background:url('<%=request.getContextPath()%>/ADPicReader?AD_ID=${adVO.ad_ID}') no-repeat center center;background-size:cover">
	                            </div>
	                        </a>
	                    </div>
	                </div>
	            </div>
            </c:forEach>
            <!-- //投影片 -->
            
			<!-- 第二張投影片 -->
            <c:forEach var="adVO" items="${adSvc.hotAD}" begin="1" end="${adSvc.hotAD.size()-1}">
	            <div class="item">
	                <div class="offer">
	                    <div class="container">
	                        <a href="${adVO.ad_Link}" target="_blank">
	                            <div class="wthree-offer-grid" style="background:url('<%=request.getContextPath()%>/ADPicReader?AD_ID=${adVO.ad_ID}') no-repeat center center;background-size:cover">
	                            </div>
	                        </a>
	                    </div>
	                </div>
	            </div>
            </c:forEach>
			<!-- //第二張投影片 -->
        </div>
        <!-- Left Button 切換幻燈片左按鈕 -->
        <a class="left carousel-control kb_control_left kb_icons" href="#kb2" role="button" data-slide="prev">
            <span class="fas fa-angle-left" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <!-- Right Button 切換幻燈片右按鈕 -->
        <a class="right carousel-control kb_control_right kb_icons" href="#kb2" role="button" data-slide="next">
            <span class="fas fa-angle-right" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>

    <!-- //熱門促銷 -->

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
    <script src="<%=request.getContextPath()%>/front_end/js/all/index_SmoothScroll.min.js"></script>
</body>

</html>
