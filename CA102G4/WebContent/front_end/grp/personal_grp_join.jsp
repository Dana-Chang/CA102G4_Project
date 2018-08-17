<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="javax.servlet.http.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.mem.model.*" %>
<%@ page import="com.photo_wall.model.*" %>
<%@ page import="com.blog.model.*" %>
<%@ page import="com.chat.model.*" %>
<%@ page import="com.fri.model.*" %>
<%@ page import="com.grp.model.*" %>
<%@ page import="com.grp_mem.model.*" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService"></jsp:useBean>

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


	boolean login_state = false ;
	Object login_state_temp = session.getAttribute("login_state");
	
	//確認登錄狀態
	if(login_state_temp != null ){
		login_state= (boolean) login_state_temp ;
	}
	
// 	List<String> grp_Id = request.getParameter("grp_Id");
	
// 	System.out.println("grp_Id="+grp_Id);
	System.out.println("mem_Id="+memberVO.getMem_Id());
	
	//若登入狀態為不是true，紀錄當前頁面並重導到登入畫面。
	if( login_state != true){
		session.setAttribute("location", request.getRequestURI());
		response.sendRedirect(request.getContextPath()+"/front_end/member/mem_login.jsp");
		return;
	}

	//取得登錄者的照片牆
	Photo_wallDAO photoSvc = new Photo_wallDAO(); 
	List<Photo_wallVO> photoList=photoSvc.getAll_ByMemID(memberVO.getMem_Id());
	pageContext.setAttribute("photoList", photoList);
	
	//取得登錄者的部落格
	blogService blogSvc = new blogService();
	List<blogVO> blogList=blogSvc.findByMemId(memberVO.getMem_Id());
	pageContext.setAttribute("blogList", blogList);
	
	//取得登錄者開的揪團
	GrpDAO grpSvc = new GrpDAO();
	List<GrpVO> grpList = grpSvc.getAll_ByMemID(memberVO.getMem_Id());
	pageContext.setAttribute("grpList", grpList);
	
	//取得登錄者參加的揪團
	GrpDAO grp_memSvc = new GrpDAO();
	List<GrpVO> grp_memList = grp_memSvc.getAll_join_grp(memberVO.getMem_Id());
	pageContext.setAttribute("grp_memList", grp_memList);
	
	

	
	/***************取出登入者的行程******************/
// 	List<TripVO> allTripList = tripSvc.getAll();
// 	List<TripVO> myTripList = new ArrayList<>();
// 	for(TripVO tripvo : allTripList){
// 		if(tripvo.getMem_id().equals(memberVO.getMem_Id())){
// 			myTripList.add(tripvo);		
// 		}
// 	}

// 	pageContext.setAttribute("myTripList",myTripList);
	
// 	//取得登錄者所參與的群組聊天*******
// 	List<ChatRoom_JoinVO> myCRList =chatRoomJoinSvc.getMyChatRoom(memberVO.getMem_Id());
// 	Set<ChatRoom_JoinVO> myCRGroup = new HashSet<>(); //裝著我參與的聊天對話為群組聊天時
	
// 	for(ChatRoom_JoinVO myRoom : myCRList){
// 		//查詢我參與的那間聊天對話，初始人數是否大於2?? 因為這樣一定就是群組聊天
// 		int initJoinCount = chatRoomSvc.getOne_ByChatRoomID(myRoom.getChatRoom_ID()).getChatRoom_InitCNT();
// 		if(initJoinCount > 2){
// 			myCRGroup.add(myRoom);
// 		}
// 	}
// 	pageContext.setAttribute("myCRList", myCRGroup);
	
// 	/***************取出會員的好友******************/
// 	FriendService friSvc = new FriendService();
// 	List<Friend> myFri = friSvc.findMyFri(memberVO.getMem_Id(),2); //互相為好友的狀態
// 	pageContext.setAttribute("myFri",myFri);

	
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
<%@ page import="com.fri.model.*" %>
<%@ page import="com.chat.model.*" %>
<jsp:useBean id="chatRoomSvc" scope="page" class="com.chat.model.ChatRoomService"></jsp:useBean>
<jsp:useBean id="chatRoomJoinSvc" scope="page" class="com.chat.model.ChatRoom_JoinService"></jsp:useBean>

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
    
    <!-- AD_Page相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/ad/ad_page.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/personal/personal_area_home.css" rel="stylesheet" type="text/css">
    <!-- //AD_Page相關CSS及JS -->
    
	<!-- 聊天相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css">
    <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script>
    <%@ include file="/front_end/personal_area/chatModal_JS.file" %>
    <!-- //聊天相關CSS及JS -->
    

    

</head>
<!-- onload="connect();" -->
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

	<%-- 錯誤表列 --%>
	<c:if test="${not empty errorMsgs}">
		<div class="modal fade" id="errorModal">
		    <div class="modal-dialog modal-sm" role="dialog">
		      <div class="modal-content">
		        <div class="modal-header">
		          <i class="fas fa-exclamation-triangle"></i>
		          <span class="modal-title"><h4>&nbsp;注意：</h4></span>
		        </div>
		        <div class="modal-body">
					<c:forEach var="message" items="${errorMsgs}">
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
    <div class="ui container">
        <!--會員個人頁面標頭-->
        <div class="mem_ind_topbar">
            <!--會員封面-->
            <div class="mem_ind_banner">
                <img src="<%=request.getContextPath()%>/front_end/images/all/person_bar.jpg">
            </div>
            <!--會員訊息--> 
            <div class="mem_ind_info"> 
                <div class="mem_ind_img">
                   	<c:choose>
                  		<c:when test="${memberVO.mem_Photo == null}">
                  			<img src='<%=request.getContextPath()%>/front_end/images/all/mem_nopic.jpg'>
                  		</c:when>
                  		<c:otherwise>
                  			<img src='<%=request.getContextPath()%>/front_end/readPic?action=member&id=${memberVO.mem_Id}'>
                  		</c:otherwise>
                  	</c:choose>
                </div>
                <div class="mem_ind_name">
                    <p>${memberVO.mem_Name}
 						<c:if test="${memberVO.mem_Sex == 1}">
       						<i class='fas fa-male' style='color:#4E9EE2'></i>
      					</c:if>
      					<c:if test="${memberVO.mem_Sex == 2}">
       						<i class='fas fa-female' style='color:#EC7555'></i>
     					</c:if>
 
                    </p> 
                    <p class="text-truncate" style="font-size:0.9em;padding-top:10px;max-height:110px">
					   ${memberVO.mem_Profile}
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
            <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/trip/personal_area_trip.jsp">
                  <i class="fas fa-map"></i>行程
              </a>
            </li>
            <li class="nav-item active">
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
            <div id="home" class="container tab-pane active">
            
            <ul class="nav nav-tabs">
				  <li role="presentation"><a href="<%=request.getContextPath()%>/front_end/grp/personal_area_grp.jsp">我的揪團</a></li>
				  <li role="presentation" class="active" ><a href="<%=request.getContextPath()%>/front_end/grp/personal_grp_join.jsp">我參加的揪團</a></li>
			  </ul>
               
               <div class="u_ind_item">
                  
                  <div class="mem_ind_item_blog">
                  	<!--我參加的揪團列表 start  -->
                     <div class="ui items">
                     		<%if(grp_memList.size() != 0){ %>
                            <c:forEach var="GrpVO" items="${grp_memList}">
	                      	  <!-- 揪團區塊 -->
		                      <div class="item col-12">
		                        <div class="ui small image ">
		                          <a href="<%=request.getContextPath()%>/front_end/grp/grp_oneview.jsp?grp_Id=${GrpVO.grp_Id}">
		                              <img src="<%=request.getContextPath()%>/grpPicReader?grp_Id=${GrpVO.grp_Id}"/>
		                          </a>
		                        </div>
		                        <div class="content">
		                          <div class="text-truncate header">${GrpVO.grp_Title}</div>
		                          <div class="meta">
		                            <span class="stay">
		                            	<i class="fas fa-calendar-alt">出發日期</i>
                   						 <fmt:formatDate pattern="YYYY年MM月dd日  HH:mm" value="${GrpVO.trip_Start}" />
		                            </span>
		                          </div>
		                          <div class="description">
		                          	<i class="fas fa-align-justify"></i>
		                            <span class="_shortText">${GrpVO.trip_Details}  															
		                            </span>
		                          </div>
		                        </div>
		                      </div>
		                     
		                    </c:forEach>
		                    <% }else{ %>
		                    <br>
	                    	<h3>快點參加一個揪團出去玩吧</h3>
	                    	<br>
		                    <% } %>
                     </div>
                     <br>  
                     <!--我的揪團列表 end  -->
                  </div>
               </div> 
            </div>
            <!--//首頁左半邊-個人首頁-->
          </div>
          <!--頁籤項目-首頁內容-->
          <!--首頁右半邊-->                
          
          <!--//首頁右半邊--> 
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
                    <a href="index.html" target="_blank" title="TravelMaker">TravelMaker</a>
                </p>
            </div>
        </div>
    </div>
    <!-- //footer -->

<script>
$(function(){
			    var len = 80; // 超過100個字以"..."取代
			    $("._shortText").each(function(i){
			   	 var temptext=$(this).text().trim().replace('  ', '');
			        if(temptext.length>len){
			            $(this).attr("title",temptext);
			            var text=temptext.substring(0,len-1)+"...";
			            $(this).text(text);
			        }
			    });
			});
</script>
    
</body>

</html>
