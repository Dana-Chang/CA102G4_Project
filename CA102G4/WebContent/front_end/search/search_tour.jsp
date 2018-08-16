<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
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

    <!-- JQUERY -->
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <!-- //JQUERY -->

    <!-- bootstrap css、JS檔案 -->
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

    <!-- blog 自定義的css -->
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_semantic.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog.css" rel="stylesheet" type="text/css" media="all">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_divider.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_button.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_icon.css" rel="stylesheet" type="text/css">
    <!-- //blog 自定義的css -->
    
    <!-- search 自定義的css -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/front_end/css/search/search.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/front_end/css/search/search_blog.css">
    <!-- //search 自定義的css -->
    
    <!-- blog 自定義的js -->
<%--     <script src="<%=request.getContextPath()%>/front_end/js/blog/blog_semantic.min.js"></script> --%>
    <script src="<%=request.getContextPath()%>/front_end/js/search/search.js"></script>
    <!-- //blog 自定義的js -->

    <!-- 旋轉木馬 -->
    <link href="<%=request.getContextPath()%>/front_end/swiper-4.3.3/dist/css/swiper.min.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/front_end/swiper-4.3.3/dist/js/swiper.min.js"></script>
    <!-- 旋轉木馬 -->
    
    <!-- LogoIcon -->
    <link href="<%=request.getContextPath()%>/front_end/images/all/Logo_Black_use.png" rel="icon" type="image/png">
    <!--    <link rel="icon" href="images/Logo_Black_use.ico" type="image/x-icon">-->
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
    <!-- 內容 -->
    <div class="ui container" id="container">
        <!-- 我是隔板 -->
        <div class="ui hidden divider"></div>
        <!-- //我是隔板 -->
        <!-- 搜尋BAR -->
         <FORM class="keywordForm" METHOD="GET" ACTION="<%=request.getContextPath()%>/blog.do">
	        <div class="ui fluid action input">
	            <input type="text" name="keyword" placeholder="搜尋遊記、揪團、問答、會員、景點" value="${param.keyword}">
	            <input type="hidden" name="action" value="searchTour">
	            <div class="ui button submitKeyword">搜尋</div>
	        </div>
        </FORM>
        <!-- 搜尋BAR -->
        <!-- 我是隔板 -->
        <div class="ui hidden divider"></div>
        <!-- //我是隔板 -->
        <!-- Menu -->
	  	<div class="ui tabular menu">
            <a class='item ${param.action=="searchAll"?"active":""}' href="<%=request.getContextPath()%>/blog.do?action=searchAll&keyword=${param.keyword}">
                最佳
            </a>
            <a class='item ${param.action=="searchBlog"?"active":""}' href="<%=request.getContextPath()%>/blog.do?action=searchBlog&keyword=${param.keyword}">
                旅遊記
            </a>
            <a class='item ${param.action=="searchMember"?"active":""}' href="<%=request.getContextPath()%>/blog.do?action=searchMember&keyword=${param.keyword}">
                會員
            </a>
            <a class='item ${param.action=="searchAsk"?"active":""}' href="<%=request.getContextPath()%>/blog.do?action=searchAsk&keyword=${param.keyword}">
                問答
            </a>
            <a class='item ${param.action=="searchTogether"?"active":""}' href="<%=request.getContextPath()%>/blog.do?action=searchTogether&keyword=${param.keyword}">
                揪團
            </a>
            <a class='item ${(param.action=="searchTour") || (param.action == null)?"active":""}' href="<%=request.getContextPath()%>/blog.do?action=searchTour&keyword=${param.keyword}">
                景點
            </a>
        </div>
        <!-- //Menu -->

        <!-- 我是隔板 -->
        <div class="ui hidden divider"></div>
        <!-- //我是隔板 -->
        <!-- 搜尋時間 -->
 		<div class="time">
        	<div class="load"><span id="resultCount">共找到 ${AttractionsList.size()} 個結果</span>，花費 <span id="time"> 秒</span></div>
        </div>
        <!-- //搜尋時間 -->
        <!-- 下面搜尋結果 -->
        <div class="row">
            <c:if test="${not empty AttractionsList}">
	            <table class="table table-hover">
	                <tbody>
	                	<%@ include file="page3.file"%>
	                	<c:forEach var="attractionsVO" items="${AttractionsList}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
		                    <tr>
		                        <td>
		                            <div class="item">
		                                <div class="content">
		                                    <a class="ui header title" target="_blank" href="<%=request.getContextPath()%>/front_end/attractions/attDetail.jsp?att_no=${attractionsVO.att_no}">
		                                    ${attractionsVO.att_name}
		                                	</a>
		                                    <div class="description text-truncate attractionsArea">
		                                    ${attractionsVO.country}${attractionsVO.administrative_area==null?"":" - "}${attractionsVO.administrative_area}
		                                    </div>
		                                    <div class="extra attractionsExtra">
			                                    <c:set var="att_information" value="${attractionsVO.att_information}"/> 
			                                    <%= ((String)pageContext.getAttribute("att_information")).replaceAll("<[^>]*>","").trim()%>
		                                    </div>
		                                </div>
		                            </div>
		                        </td>
		                    </tr>
						</c:forEach>
	                </tbody>
	            </table>

		      	<!-- 上一頁button -->
				<%@ include file="page2.file"%>
				<!-- //下一頁button -->
				<!-- 頁數資訊 -->
				<div class="page_info">
					顯示第<%= whichPage %>頁，共<%= pageNumber %>頁
				</div>
				<!-- //頁數資訊 -->
			</c:if>
			<c:if test="${empty AttractionsList}">
				<div class="ui left aligned search_results empty" style="text-align:center;height:300px;border:1px solid lightgray">
					<p style="color:darkgray;height:100%;line-height:300px;font-size:40px">沒有相關關鍵字的景點!!</p>
				</div>
			</c:if>


        </div>
        <!-- //下面搜尋結果 -->
        


        <!-- 我是隔板 -->
        <div class="ui hidden divider"></div>
        <!-- //我是隔板 -->
    </div>
    <!-- //內容 -->

    <!-- //blog -->
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
    
     <script>
		var start = Date.now();
		var elapsedMs;
		// run the clock and stop when all the images load
		var timeEl = document.getElementById('time');
		var clockUpdate;

		function setElapsedTimeDisplay() {
			elapsedMs = (Date.now() - start);
			timeEl.firstChild.data = (elapsedMs / 1000).toFixed(3) + ' 秒';
		}

		clockUpdate = window.setInterval(function() {
			setElapsedTimeDisplay();
		}, 17);

		window.onload = function() {
			if (console)
				console.log("window.onload 載入完成");
			setElapsedTimeDisplay();
			clearInterval(clockUpdate);
		}
	</script>
</body>

</html>
