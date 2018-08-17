<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.photo_wall.model.*"%>
<%@ page import="com.photo_tag.model.*"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.grp.model.*"%>
<%@ page import="java.util.*"%>

<%	
	//清快取
	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-store"); 
	response.setDateHeader("Expires", 0);
	
	MemberVO memberVO = (MemberVO) session.getAttribute("memberVO"); 
	if(memberVO == null){
		memberVO = (MemberVO)session.getAttribute("memberVO");
	}	
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
	//從memberVO取會員ID
	
	GrpService grpSvc = new GrpService();
 	List<GrpVO> list = grpSvc.getAll();
 	pageContext.setAttribute("list", list);
 	
	
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

<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService" />


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
    <script src="https://code.jquery.com/jquery.js"></script>
    <!-- //JQUERY -->
    
    <!-- Bootstrap -->
	<link href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css" rel="stylesheet" type="text/css" media="all" />
	<script src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
    
    <!-- font字體 -->
    <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
    <!-- //font字體 -->
   

    <!-- 套首頁herder和footer css -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_style_header_footer.css" rel="stylesheet" type="text/css" media="all" />
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog.css" rel="stylesheet" type="text/css" media="all" />
    <!-- //套首頁herder和footer css -->

    <!-- grp 自定義的css -->
    <link href="<%=request.getContextPath()%>/front_end/css/member/login_style.css" rel="stylesheet" type="text/css" media="all" />
   	<link href="<%=request.getContextPath()%>/front_end/css/member/modal.css" rel="stylesheet" >
    <link href="<%=request.getContextPath()%>/front_end/css/grp/AD_semantic.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/grp/group.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/grp/group_fix.css" rel="stylesheet" type="text/css">
    <!-- //grp 自定義的css -->

    <!-- font-awesome icons -->
    <link href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" rel="stylesheet" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- //font-awesome icons -->

    <!-- font字體 -->
    <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
    <!-- //font字體 -->
    
    <!--  datepicker js  -->
    <script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
    <!--//datepicker js  -->
    
	<!--燈箱效果 -->
	<script src="<%=request.getContextPath()%>/front_end/js/member/bootstrap.min.js"></script>
	<!--//燈箱效果 -->

	<style>
	
.footer{
	background-color:#1b1c1d; 
	}
.btn-primary-grp{
	background-color: rgba(0,50,50,0.2);
    border-color: rgba(0,67,50,0.2);
}	
.btn-primary-grp:hover{
	color:unset;
	background:none !important;
}
.btn-primary-grp:focus{
	color:unset;
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
                        <li><i class="fa fa-phone" aria-hidden="true"></i>
                            <a href="tel:034257387"> 03-4257387</a></li>
                        <li><a href="mailto:TravelMaker@gmail.com"><i class="fa fa-envelope" aria-hidden="true"></i> TravelMaker@gmail.com</a></li>
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
                <div class="clearfix"> </div>
            </div>
        </div>
        <div class="header">
            <div class="container">
                <div class="logo">
                    <h1>
                        <a href="index.html">Travel Maker</a>
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
   

<!-- 主圖區 -->
    <div>
        <div class="carousel-inner">
            <div class="carousel-images">
                <img src="<%=request.getContextPath()%>/front_end/images/all/aegean_Sea_2.png" style="width:100% ;height:auto;" alt="">
                <div class="">
                    <div class="carousel-caption">
                        <h1>揪個合適的夥伴一起旅行</h1>
                        <p>你揪過了嗎？</p>						
                     <a href="<%=request.getContextPath()%>/front_end/grp/addgrp.jsp">
                     <button class="btn btn-lg btn-primary-grp" type="button" style="border:0;">
                      	 開始我的揪團</button>
                      </a>						
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <ol class="breadcrumb">
        <li>
            <a href="<%=request.getContextPath()%>/front_end/index.jsp">回到首頁</a>
        </li>

       <li class="active">瀏覽揪團</li>

    </ol>
    
<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/grp.do" name="form1">

        <!--  search  -->
    <div class="flight-engine">
        <div class="container">
            <div class="tabing">
                <ul>
                    <li>
                        <a class="active" href="#1"><i class="fa fa-plane" aria-hidden="true"></i> 找揪團</a>
                    </li>
                </ul>
                
                <div class="tab-content">
                    <div id="1" class="tab1 active">
                    
                           <div class="flight-tab row">
                            <div class="persent-one">
                                <i class="fa fa-map-marker" aria-hidden="true"></i>
                                <input type="text" name="TRIP_LOCALE" class="textboxstyle" id="arival" placeholder="請輸入地點或關鍵字">
                            </div>
                            <div class="persent-one less-per">
                                <i class="fa fa-calendar" aria-hidden="true"></i>
                                <input type="text" name="GRP_START"  class="textboxstyle" id="datepicker_1" placeholder="開始日期">
                            </div>
                            <div class="persent-one less-per">
                                <i class="fa fa-calendar" aria-hidden="true"></i>
                                <input type="text" name="GRP_END" class="textboxstyle" id="datepicker_2" placeholder="結束日期">
                            </div>
                            <div class="persent-one less-btn">
                                <input type="Submit" value="Search" class="btn cst-btn" id="srch">
                                <input type="hidden" name="action" value="listEmps_ByCompositeQuery">
                            </div>
                        </div>
                        
                    </div>

                </div>
            </div>

        </div>

    </div>
</FORM>    
    <div class="ui container">
            <div id="cards" class="ui four stackable cards">
                <c:forEach var="GrpVO" items="${list}">
				<c:if test="${GrpVO.grp_Status == 1 }">
				<a class="fluid card" href="<%=request.getContextPath()%>/front_end/grp/grp_oneview.jsp?grp_Id=${GrpVO.grp_Id}" style="margin-top:30px;">
					<div class="image">				
						<div class="ui image pic" style="height:220px; background-size:cover;">
						<img style="height:100%;width:100%" src="<%=request.getContextPath()%>/grpPicReader?grp_Id=${GrpVO.grp_Id}">
						</div>
					</div>
					<div class="content">
                        <div class="header">
                           <img id="user-group" src="<%=request.getContextPath()%>/front_end/readPic?action=member&id=${GrpVO.mem_Id}">
                            <span class="font-f">${memberSvc.findByPrimaryKey(GrpVO.mem_Id).mem_Name}</span>
                            <p class="font-f">${GrpVO.grp_Title}</p>
                        </div>
                    <hr class="card-hr">
                    <div class="con-group">
                    <p class="far fa-calendar-alt con-group font-f">                
                    <fmt:formatDate pattern="YYYY年MM月dd日  HH:mm" value="${GrpVO.grp_Start}" />
                    </p><br>                                       
                    <p class="fas fa-map-marker-alt con-group font-f">&nbsp;${GrpVO.trip_Locale}</p><br>
                    <img src="<%=request.getContextPath()%>/front_end/images/all/dollar-coin-money.svg" style="height:18px;width:18px;margin-bottom:4px;margin-right: 0px;margin-left:-2;margin-left:-3px;" alt="">
                    ${GrpVO.grp_Price}<br>
                    </div>
                    </div>
				</a>
				</c:if>
			</c:forEach>
            </div>    
    </div>

    <!--  //search  -->
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
                    <a href="index.html" target="_blank" title="TravelMaker">TravelMaker</a>
                </p>
            </div>
        </div>
    </div>	

<script>
  $(function() {
   $( "#datepicker_1" ).datepicker({
      showAnim: "slideDown",
      dateFormat : "yy-mm-dd"
    });
  });
 </script>
 
 <script>
  $(function() {
   $( "#datepicker_2" ).datepicker({
      showAnim: "slideDown",
      dateFormat : "yy-mm-dd"
    });
  });
 </script>

<!-- <!-- 燈箱 Register 開始 --> -->
<!-- <!-- Modal Register --> -->
<!-- <div class="modal fade" id="myModal" role="dialog"> -->
<!--     <div class="modal-dialog"> -->
    
<%--     <c:if test="${not empty errorMsgs}"> --%>
<!--     	<div id="myModal"> -->
<!--     		<div class="messageContent"> -->
<%--     			<c:forEach var="message" items="${errorMsgs}"> --%>
<%--     			${message} --%>
<%--     			</c:forEach> --%>
<!--     		</div> -->
<!--     	</div> -->
<%--     	</c:if> --%>
    
<!--         Modal Register content -->
<!--        <div background-color="lightblue;"> -->
<%--             <form METHOD="post" ACTION="<%=request.getContextPath()%>/grp.do" name="form1" class="fh5co-form animate-box-modal" data-animate-effect="fadeIn" onsubmit="return chk();"> --%>
<!--                 <h2>開始計畫新的揪團</h2> -->
<!--                 <div class="form-group"> -->
<!--                 	<th>幫揪團取個名字吧</th> -->
<!--                     <td><input name="grp_Title" size="45" type="text" class="form-control" placeholder="團名" autocomplete="off"> -->
<!--                 	</td>                	 -->
<!--                 </div> -->
<!--                 <div class="form-group"> -->
<!--                 	<table> -->
<!--                 	<th style="font-weight:normal;">去哪裡呢&nbsp;</th> -->
<!-- 					<td><input name="trip_Locale" size="20" type="text" class="form-control"  placeholder="地點" autocomplete="off" > -->
<!--                 	</td> -->
<!--                 	<th style="font-weight:normal;">大概預算&nbsp;</th> -->
<!-- 					<td><input name="grp_Price" size="20" type="text" class="form-control"  placeholder="金額" autocomplete="off" > -->
<!--                 	</td> -->
<!--                 	</table> -->
<!--                 </div> -->
<!--                 <div class="form-group"> -->
<!--                 	<table> -->
<!--                 	<th style="font-weight:normal;">報名人數&nbsp;</th> -->
<!--                     <td><input name="grp_Cnt" size="20" type="text" class="form-control"  placeholder="預計報名人數" autocomplete="off"> -->
<!--                 	</td> -->
<!--                 	<th style="font-weight:normal;">出團人數&nbsp;</th> -->
<!--                     <td><input name="grp_Acpt" size="20" type="text" class="form-control"  placeholder="成團出發人數" autocomplete="off"> -->
<!--                 	</td> -->
<!--                 	</table> -->
<!--                 </div> -->
<!--                 <div class="form-group"> -->
<!--                     <th>揪團結束的時間</th> -->
<!--                     <label for="start" class="sr-only"></label> -->
<!--                     <input name="grp_End"  size="45" type="text" class="form-control" id="datepicker_grp_end"  placeholder="請選擇揪團結束時間" autocomplete="off"> -->
<!--                 </div> -->
<!--                 <div class="form-group"> -->
<!--                     <table> -->
<!--                     <th style="font-weight:normal;">啟程時間&nbsp;</th> -->
<!--                    	<td><input name="trip_Strat"  size="20" type="text" class="form-control" id="datepicker_trip_start"  placeholder="請選擇行程起程時間" autocomplete="off"> -->
<!--                    	</td> -->
<!--                     <th style="font-weight:normal;">回程時間&nbsp;</th> -->
<!--                     <td><input name="trip_End"    size="20" type="text" class="form-control" id="datepicker_trip_end"  placeholder="請選擇行程結束時間" autocomplete="off"> -->
<!--                 	</td> -->
<!--                 	</table> -->
<!--                 </div> -->

<!--                 <div class="form_btn">                -->
<!--                     </div> -->
<!--                 <div class="form-group"> -->
<%-- 					<input type="hidden" name="mem_Id" value="${memberVO.mem_Id}">  --%>
<!--                 	<input type="hidden" name="action" value="insert" class="btn btn-primary"> -->
<!--                     <input type="submit" value="送出" class="btn btn-primary">   -->
<!--                     <input type="button" value="取消" onclick="javascript:location.href='grpIndex.jsp'" class="btn btn-primary" > -->
<!--                 </div> -->
<!--             </form> -->
            
<!--         </div> -->
<!--         //Modal Register content -->

<!--     </div> -->

<!-- </div> -->
<!-- <!-- 燈箱 Register 結束 --> -->
<!-- <script> -->
//   $(function() {
//    $( "#datepicker_grp_end" ).datepicker({
//       showAnim: "slideDown",
//       dateFormat : "yy-mm-dd"
//     });
//   });
<!--  </script> -->
<!-- <script> -->
//   $(function() {
//    $( "#datepicker_trip_start" ).datepicker({
//       showAnim: "slideDown",
//       dateFormat : "yy-mm-dd"
//     });
//   });
<!--  </script> -->
<!--  <script> -->
//   $(function() {
//    $( "#datepicker_trip_end" ).datepicker({
//       showAnim: "slideDown",
//       dateFormat : "yy-mm-dd"
//     });
//   });
<!--  </script> -->

</body>

</html>

