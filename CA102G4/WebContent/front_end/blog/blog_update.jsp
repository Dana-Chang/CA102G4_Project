<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.blog.model.*"%>
<%@ page import="com.blog_tag_name.model.*" %>
<%@ page import="com.blog_tag.model.*" %>
<%@ page import="com.mem.model.*"%>
<%
	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-store"); 
	response.setDateHeader("Expires", 0);
	blogVO blogVO = (blogVO) request.getAttribute("blogVO");
	
	blogTagNameService blogTagNameSvc = new blogTagNameService();
	List<blog_tag_nameVO> list = blogTagNameSvc.getAll();
	request.setAttribute("list", list);
	 
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
	 	response.sendRedirect("/CA102G4/front_end/member/mem_login.jsp");
	 	return;
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
<jsp:useBean id="Mytag" scope="page" class="com.blog_tag.model.blogTagService"></jsp:useBean>

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
    <meta name="keywords" content="TravelMaker,Travelmaker,自助旅行,部落格,旅遊記" />
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
    <!-- JQUERY-->
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <!-- //JQUERY -->
    <!-- bootstrap css及 JS檔案 -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <script src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
    <!-- //bootstrap-css -->

    <!-- 套首頁herder和footer css -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_style_header_footer.css" rel="stylesheet" type="text/css" media="all" />
    <!-- //套首頁herder和footer css -->

    <!-- font-awesome icons -->
    <link href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" rel="stylesheet" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- //font-awesome icons -->

    <!-- font字體 -->
    <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
    <!-- //font字體 -->

    <!-- blog使用到的jQuery Dialog -->
    <link href="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.js"></script>
    <!-- //blog使用到的jQuery Dialog -->

    <!-- blog 自定義的css -->
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_divider.css" rel="stylesheet" type="text/css" media="all">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog.css" rel="stylesheet" type="text/css" media="all">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_icon.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_add_semantic.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_add.css" rel="stylesheet" type="text/css" media="all">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_add_button.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_semantic.min.css" rel="stylesheet" type="text/css">
    <!-- //blog 自定義的css -->

    <!-- blog 自定義的js-->
	<script src="<%=request.getContextPath()%>/front_end/js/blog/blog_popup.min.js"></script>
	<script src="<%=request.getContextPath()%>/back_end/js/blog/UI-Transition-master/transition.min.js"></script>
	<script src="<%=request.getContextPath()%>/back_end/js/blog/UI-Dropdown-master/dropdown.min.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/blog/blog_add.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/blog_ckeditor/ckeditor.js"></script>
    <!-- //blog 自定義的js -->
    <!-- LogoIcon -->
    <link href="<%=request.getContextPath()%>/front_end/images/all/Logo_Black_use.png" rel="icon" type="image/png">
    <!-- //LogoIcon -->
   	<!-- 聊天相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css">
    <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script>
    <%@ include file="/front_end/personal_area/chatModal_JS.file" %>
    <!-- //聊天相關CSS及JS -->
    
    
    <script>
		$(function() {
			$( "#datepicker" ).datepicker({
				  showAnim: "slideDown",
				  maxDate: "-1d",
				  dateFormat : "yy-mm-dd"
				});
		});
	</script>
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


	<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/blog.do" name="form2" enctype="multipart/form-data">
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
    <div class="container">
        <!-- 我是隔板 -->
        <div class="ui hidden divider"></div>
        <!-- //我是隔板 -->
        
        <input type="hidden" name="action" value="updating">
        <input type="hidden" name="blog_id" value="${blogVO.blog_id}">
        <form METHOD="post" ACTION="<%=request.getContextPath()%>/blog.do" name="form1">
            <!-- 文章標題區塊 -->
            <input type="text" placeholder="請輸入文章標題" name="title" maxlength="33" value="<%= (blogVO==null)? "" : blogVO.getBlog_title()%>"/>
            <!-- //文章標題區塊 -->
            <!-- 封面照片 -->
            <p>封面照片：<input type="file" name="travel_coverimage" accept="image/*" value="<%= (blogVO==null)? "" : blogVO.getBlog_coverimage()%>"></p>
            <!-- //封面照片 -->
           	<!-- 旅遊日期 -->
            <p>日期：<input type="text" id="datepicker" size="30" readonly name="travel_date" value="<%= (blogVO==null)? "" : blogVO.getTravel_date()%>"></p>
            <!-- //旅遊日期 -->
            <!-- 標籤 -->
			<div class="ui sub header">設定標籤可以增加文章的曝光率唷!</div>
			<div class="ui multiple selection dropdown">
				  <input name="blogTag" type="hidden" value='<c:forEach var="blog_tagVO" items="${Mytag.getAllByABlog(blogVO.blog_id)}">${blog_tagVO.btn_id},</c:forEach>'>
				  <i class="dropdown icon"></i>
				  <div class="default text">添加標籤</div>
				  <div class="menu">
				  <c:forEach var="blogTagNameVO" items="${list}">
				      <div class="item" data-value="${blogTagNameVO.btn_id}">${blogTagNameVO.btn_class} - ${blogTagNameVO.btn_name}</div>
				  </c:forEach>
				  </div>
			</div>				
			<!-- //標籤 -->
            <!-- 編輯器區塊 -->
            <textarea name="editor1">
            <%= (blogVO==null)? "" : blogVO.getBlog_content()%>
            </textarea>
            <!-- //編輯器區塊 -->
        </form>
    </div>
    </FORM>
            <!-- 右邊的Go to top Button -->
        <div class="ui vertical basic buttons">
        	<button id="submitButton" data-tooltip="發佈文章" data-position="top center">
	            <i class="fas fa-paper-plane"></i>
        	</button>
	        <button id="myBtn" data-tooltip="Go to top" data-position="bottom center">
	            <i class="fas fa-chevron-up"></i>
	        </button>
        </div>
        <!-- //右邊的Go to top Button -->
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
    <!-- //editorJS -->
    <script>
        CKEDITOR.replace('editor1', {
            extraPlugins: 'base64image',
            removePlugins: 'image',
            removePlugins: 'resize',
            height: 700,
            removeDialogTabs: 'image:advanced;link:advanced',
        });
        //            extraPlugins: 'autogrow',
        //            autoGrow_minHeight: 500,

    </script>
    <!-- //editorJS -->
        
       <%-- 錯誤表列 --%>
		<c:if test="${not empty errorMsgs}">
		  <div class="warning" id="warning">
		  	<div class="warning_content">
        	<ul>
				<c:forEach var="message" items="${errorMsgs}">
					<li>${message}</li>
				</c:forEach>
			</ul>
			</div>
       	 </div>
		</c:if>
</body>

</html>
