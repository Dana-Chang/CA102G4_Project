<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.photo_wall.model.*"%>
<%@ page import="com.photo_tag.model.*"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.grp.model.*"%>
<%@ page import="com.grp_mem.model.*"%>
<%@ page import="java.util.*"%>

<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService" />


<%	
	//清快取
	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-store"); 
	response.setDateHeader("Expires", 0);
	
	MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
	
	
	//宣告一個區域變數mem_Id先讓他等於null 要塞值的時候再給他值
	String mem_Id = null ;
	
	//取得從上個面頁傳送過來的會員ID參數(被點擊的揪團該團長ID)
// 	String mem_ID = request.getParameter("mem_Id");
	
// 	System.out.println(mem_ID);
	
	//假設開團的會員點進自己的揪團畫面會直接轉到該揪團的管理頁面
	
	
	
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
	
	//取得從上個面頁傳送過來的揪團ID參數(被點擊揪團的該ID)
	String grp_Id = request.getParameter("grp_Id");
	
	System.out.println("grp_Id="+grp_Id);

	GrpService grpSvc = new GrpService();
	GrpVO grpVO = grpSvc.findByPrimaryKey(grp_Id);
	pageContext.setAttribute("grpVO", grpVO);
	
	System.out.println("grpVO="+grpVO);
	
 	String mem_ID = request.getParameter("mem_Id");
	
	if(memberVO != null){
		
		mem_Id = memberVO.getMem_Id();
		
		System.out.println(mem_Id);

		if(mem_Id.equals(grpVO.getMem_Id())){
			response.sendRedirect(request.getContextPath()+"/front_end/grp/personal_area_grp.jsp");
			return;
		}
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
    <link href="<%=request.getContextPath()%>/front_end/css/grp/group_cart.css" rel="stylesheet" type="text/css">
    
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
    
	<ol class="breadcrumb">
        <li>
            <a href="<%=request.getContextPath()%>/front_end/index.jsp">回到首頁</a>
        </li>

       <li>
       		<a href="<%=request.getContextPath()%>/front_end/grp/grpIndex.jsp">瀏覽揪團</a>
	   </li>
	   <li style="<%= logout %>">
	   
	   <a href="<%=request.getContextPath()%>/front_end/grp/personal_grp_join.jsp"> 我參加的揪團</a>
	   
	   </li>
	   
	   
        <li class="active">${memberSvc.findByPrimaryKey(grpVO.mem_Id).mem_Name}的揪團</li>

    </ol>

 <div class="container-group" style="margin-top: 30px;margin-bottom:150px;padding:0 250px">
        <div class="row-group">
            <div class="col-xs-7" style="text-align:center">
                <div class="list-group-title">
                    <h2 style="color:black;">${grpVO.grp_Title}</h2>
                    <p class="far fa-calendar-alt con-group" style="color:black;"></p>
                    <span style="font-size:16px;">
                    <fmt:formatDate pattern="YYYY年MM月dd日  " value="${grpVO.trip_Start}" />－
                    <fmt:formatDate pattern="YYYY年MM月dd日  " value="${grpVO.trip_End}" />
                    </span>
                    <p class="fas fa-map-marker-alt con-group" style="font-size:20px; color: red;"></p>
                    <span style="font-size:20px; color:;">${grpVO.trip_Locale}</span><br>
                    <br>
                    </div>
                    <div class="list-group-images">
						<div class="ui image pic" style="height:220px; background-size:cover;">
						<img style="height:360px !important;width:500px !important;margin:0 auto" src="<%=request.getContextPath()%>/grpPicReader?grp_Id=${grpVO.grp_Id}">
						</div>                    
					</div>
                    <br>                
            </div>
            
			<!--文章的編號-->
            <div class="col-xs-5">
               <div class="list-group-title-right">
                   <ul class="icon-bth">
                   </ul>        
				</div>
               
                <div class="thumbnail">
                    <div class="caption">
                        <h2 style="font-size:24px; color:red; width: 100%;">可報名人數：<span id="joinCount">${grpVO.grp_Cnt}</span></h2>
<%--                         <h2 style="font-size:24px; color:red; width: 100%;">預計出團人數：${grpVO.grp_Acpt}</h2> --%>
                        
                        <p>結束報名時間：
                        <fmt:formatDate pattern="YYYY年MM月dd日 " value="${grpVO.grp_End}" />
                        </p>
                        <h3 class="group-item">行程內容</h3>
                        <p class="group-card-text" style="width:auto;">
                        ${grpVO.trip_Details}
                        </p>
                        <div class="group-amnt">
                        </div>
                        <div class="total-price">
                            <span class="font-total">總價</span>
                            <span class="font-price">
                            <span>NTD&nbsp;&nbsp;</span>
                            <span class="group-price">${grpVO.grp_Price}</span>
                            </span>
                        </div>
                        
                        <% 
                       	
                        if(memberVO != null){
                        
                        Grp_memService grp_memSvc = new Grp_memService();
                        	
                        mem_Id = memberVO.getMem_Id();
          
						Grp_memVO cnt = grp_memSvc.findByPrimaryKey(grp_Id, mem_Id);

                    	%>
                        
                        <!-- //參加揪團 -->
                        <button class="btn btn-info aaa" id="join_grp" style='font-weight:<%=(cnt == null) ? "1;" : "2;"%>${grpVO.grp_Status == 2 ? "display:none":" "}'><%=(cnt == null) ? "我要參加" : "取消參加"%></button>
                        
                        	<script type="text/javascript">
	                       	
 	                       	$("#join_grp").click(function () { 
 	                            if ($(".aaa").css('font-weight') == 1) { 
 	                            	$(".aaa").css('font-weight', '2') 
 	                            	$(".aaa").text('取消參加'); 
	                                
 	                            } else {
 	                            	$(".aaa").css('font-weight', '1') 
 	                            	$(".aaa").text('我要參加'); 
                            } 
 	                        }); 
	                       	
                        		</script> 
                        
                        <%}else{ %>
                        
                        <button class="btn btn-info aaa" id="join_grp" style='${grpVO.grp_Status == 2 ? "display:none":" "}'>我要參加</button>
                        
                        
                        <script type="text/javascript">
                        </script>
                        
                        <%}%>
                            
                    </div>
                </div>
            </div>
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
   $( ".datepicker" ).datepicker({
      showAnim: "slideDown",
      dateFormat : "yy-mm-dd"
    });
  });
 </script>


<script type="text/javascript">

	 	$(document).ready(function () {	 
	      $("#join_grp").on("click",function(){
	       var action = "collect";
	       var grp_Id = "${grpVO.grp_Id}";
	   	   var mem_Id = "${memberVO.mem_Id}";
		   var grp_Cnt = "${grpVO.grp_Cnt}";
		   if($("#join_grp").text() == "取消參加"){
		    	//.text的內容都會轉型成字串再用parseInt轉成整數在.text送到SERVLET
		    	  $("#joinCount").text( parseInt($("#joinCount").text())-1);
		    }else if($("#join_grp").text() == "我要參加"){
		 		 $("#joinCount").text( parseInt($("#joinCount").text())+1)
		     }
		  
	    $.ajax({
	     url : "<%=request.getContextPath()%>/grp_mem.do",
	     method : "POST",
	     data : {
	      action:action,
	      grp_Id:grp_Id,
	      mem_Id:mem_Id,
	      grp_Leader:0,
	      grp_Cnt:grp_Cnt,
	      joinOrNot:$("#join_grp").text()
	     },
	     success: function(response){
	    	 alert(response);
	     }
	    });  
	   });
	  });
</script>

</body>

</html>

