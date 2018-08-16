<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ page import="java.util.*"%>
<%@ page import="com.blog.model.*"%>
<%@ page import="com.blog_message.model.*"%>
<%@ page import="com.blog_collect.model.*"%>
<%@ page import="com.mem.model.*"%>
<%
	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-store"); 
	response.setDateHeader("Expires", 0);

	// 紀錄捲動軸位置
	String scroll = request.getParameter("scroll");
 
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
	
	long token=System.currentTimeMillis();
	session.setAttribute("token", token);
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
<jsp:useBean id="Mytag" scope="page" class="com.blog_tag.model.blogTagService"></jsp:useBean>
<jsp:useBean id="tagName" scope="page" class="com.blog_tag_name.model.blogTagNameService"></jsp:useBean>
<jsp:useBean id="blogCollectSvc" scope="page" class="com.blog_collect.model.blogCollectService"></jsp:useBean>
<jsp:useBean id="blogMessageSvc" scope="page" class="com.blog_message.model.blogMessageService"></jsp:useBean>
<jsp:useBean id="blogSvc" scope="page" class="com.blog.model.blogService"></jsp:useBean>
<jsp:useBean id="memSvc" scope="page" class="com.mem.model.MemberService"></jsp:useBean>

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
    <!-- blog 自定義的js -->
    <script src="<%=request.getContextPath()%>/front_end/js/blog/blog.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/blog/blog_semantic.min.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/blog/blog_article.js"></script>
    <!-- //blog 自定義的js -->    
    <!-- bootstrap css、JS檔案 -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <script src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_grid.css" rel="stylesheet">
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
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog_atricle.css" rel="stylesheet">
    <!-- //blog 自定義的css -->

    <!-- LogoIcon -->
    <link href="<%=request.getContextPath()%>/front_end/images/all/Logo_Black_use.png" rel="icon" type="image/png">
    <!-- //LogoIcon -->
    <script src="<%=request.getContextPath()%>/front_end/blog_ckeditor/ckeditor.js"></script>
    <script type="text/javascript">
	    $(document).ready(function () {
		    $(".ui.button.collect").click(function(){
		    	var action = "collect";
		    	var blog_id = "${param.blogID}";
		    	var mem_id = "${memberVO.mem_Id}";
		    	var blog_id_Owner = "${blogSvc.findByPrimaryKey(param.blogID).mem_id}";
				var collectMessage = document.getElementById("collectMessage");
		    	$.ajax({
		    		url:"<%=request.getContextPath()%>/blog.do",
		    		method:"POST",
		    		data:{action:action,blog_id:blog_id,mem_id:mem_id},
		    		async: false, 
		    		success:function(msg){
		    			collectMessage.innerHTML=msg;
		    		},
		    		error:function(msg){
		    			collectMessage.innerHTML=msg;
		    		}
		    	});
		    });
	    });

		document.addEventListener('DOMContentLoaded', function() {
		    setTimeout(function() {
		        window.scrollTo(0, "${param.scroll}");
		    }, 30);
		});
		
//     	$('html,body').animate({
//             scrollTop: "${param.scroll}"
//         });

    </script>
    
    <!-- blog使用到的jQuery Dialog -->
    <link href="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.js"></script>
    <!-- //blog使用到的jQuery Dialog -->
    
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
                          <c:when test="<%= login_state %>">
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
    <!-- 我是隔板 -->
    <div class="ui hidden divider"></div>
    <!-- //我是隔板 -->
    <!-- 內容 -->
    <div class="container" id="container">

        <div class="row">
            <!-- 左邊內容 -->
            <div class="col-8">
                <!-- 左邊文章標題 -->
                <h1 class="article_title">
                   ${blogSvc.findByPrimaryKey(param.blogID).blog_title}
                </h1>
                <!-- //左邊文章標題 -->
                <!-- 左邊文章標題下方統計數字 -->
                <div class="article_brief_info">
                    <ul class="helper-clear">
                        <li class="brief_info author far fa-user"><a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogSvc.findByPrimaryKey(param.blogID).mem_id}">&nbsp; 
                        ${memSvc.findByPrimaryKey(blogSvc.findByPrimaryKey(param.blogID).mem_id).mem_Name}
                        </a></li>
                        <li class="brief_info_divider"></li>
                        <li class="brief_info date_icon release_date far fa-calendar-alt">
                            <label>&nbsp;旅行日期：</label> ${blogSvc.findByPrimaryKey(param.blogID).travel_date}</li>
                        <li class="brief_info_divider"></li>
                        <li class="brief_info view_cnt far fa-eye">&nbsp; ${blogSvc.findByPrimaryKey(param.blogID).blog_views}&nbsp;次瀏覽</li>
                        <li class="brief_info_gap"></li>
                        <li class="brief_info reply_cnt far fa-comment">&nbsp;${blogMessageSvc.findByBlogId(param.blogID).size()}&nbsp;個留言</li>
                        <li class="brief_info_gap"></li>
                        <li class="brief_info like_cnt far fa-heart" data-like_cnt="0">&nbsp;${blogCollectSvc.getAllByBlogId(param.blogID).size()}&nbsp;人收藏</li>
                    </ul>
                </div>
                <!-- //左邊文章標題下方統計數字 -->
                <!-- 我是隔板 -->
                <div class="ui hidden divider"></div>
                <!-- //我是隔板 -->
                <!-- 遊記內容 -->
                <div class="article_content">
                     ${blogSvc.findByPrimaryKey(param.blogID).blog_content}
                </div>
                <!-- //遊記內容 -->
                <!-- 我是隔板 -->
                <div class="ui hidden divider"></div>
                <!-- //我是隔板 -->
                <c:forEach var="blog_tagVO" items='${Mytag.getAllByABlog(param.blogID)}'>
                	<c:forEach var="blog_tag_nameVO" items="${tagName.all}">
                		<c:if test="${blog_tagVO.btn_id==blog_tag_nameVO.btn_id}">
                			<a class="ui basic label tagLabel" href="<%=request.getContextPath()%>/blog.do?orderby=recent&keyword=${blog_tag_nameVO.btn_name}&item=tag&action=keyword" target="_blank">
                			${blog_tag_nameVO.btn_name}
                			</a>
                		</c:if>
                	</c:forEach>
                </c:forEach>
                <!-- 留言區 -->
                <div class="ui threaded comments">

                    <h3 class="ui dividing header">
                        <font style="vertical-align: inherit;">
                            <font style="vertical-align: inherit;">${blogMessageSvc.findByBlogId(param.blogID).size()}則留言</font>
                        </font>
                    </h3>
				<c:forEach var="blogMessageList" items="${blogMessageSvc.findByBlogId(param.blogID)}">
                    <div class="comment">
                        <a class="avatar" href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogMessageList.mem_id}">
                        	<img src="<%=request.getContextPath()%>/front_end/readPic?action=member&id=${memSvc.findByPrimaryKey(blogMessageList.mem_id).mem_Id}">
                        </a>
                        <div class="content">
                            <a class="author" href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogMessageList.mem_id}">
                                <font style="vertical-align: inherit;">
                                    <font style="vertical-align: inherit;">${memSvc.findByPrimaryKey(blogMessageList.mem_id).mem_Name}</font>
                                </font>
                            </a>
                            <div class="metadata">
                                <span class="date">
                                    <font style="vertical-align: inherit;">
                                        <font style="vertical-align: inherit;">
                                            <fmt:formatDate value="${blogMessageList.bm_time}" type="both"/>
                                        </font>
                                    </font>
                                </span>
                            </div>
                            <div class="text">
                                <font style="vertical-align: inherit;">
                                    <font style="vertical-align: inherit;">
                                        ${blogMessageList.blog_message}
                                    </font>
                                </font>
                            </div>
                            <div class="actions">
							<!-- 如果登入者的id==留言者的id 則顯示刪除的按鈕 -->
                            <c:if test="${memberVO.mem_Id==blogMessageList.mem_id}">
                                <a class="delete">
                                <form class="ui deleteMessage form" METHOD="POST" ACTION="<%=request.getContextPath()%>/blog.do">
                                    <font style="vertical-align: inherit;font-size:.875em">
                                        <font style="vertical-align: inherit;">刪除</font>
                                        <input type="hidden" name="action" value="deleteMessage">
    									<input type="hidden" name="message_id" value="${blogMessageList.message_id}">
                                    	<input type="hidden" name="mem_id" value="${memberVO.mem_Id}">
                                        <input type="hidden" name="scroll" value="">
                                        <input type="hidden" name="blog_id" value="${param.blogID}">
                                    </font>
                                </form>
                                </a>
                            </c:if>
                            <!-- 如果登入者的id!=留言者的id 則顯示檢舉的按鈕，並且確認是登入狀態才顯示 -->
                            <c:if test="${login_state!=null}">
	                            <c:if test="${memberVO.mem_Id!=blogMessageList.mem_id}">
	                                <a class="report message">
	                                    <font style="vertical-align: inherit;">
	                                        <font style="vertical-align: inherit;">檢舉</font>
	                                        <input type="hidden" name="message_id" value="${blogMessageList.message_id}">            	
	                                    </font>
	                                </a>
	                            </c:if>
                            </c:if>
                            </div>
                        </div>
                    </div>
				</c:forEach>
                    <div class="comment">
                        <!-- 留言區-看更多 -->
                        <li class="comment_next_page_btn" data-next_page="1">
                            <i class="loader"></i>看更多
                        </li>
                        <!-- //留言區-看更多 -->
                        <!-- 留言區輸入內容的地方 -->
                        <form class="ui reply form" METHOD="POST" ACTION="<%=request.getContextPath()%>/blog.do">
                            <div class="field">
                                <a name="textareaField">
                                    <textarea style="margin-top: 0px; margin-bottom: 0px; height: 160px;resize:none" name="blog_message"></textarea>
                                </a>
                            </div>
                            <c:if test="${login_state!=null}">
	                            <div class="ui blue labeled submit icon button">
	                                <i class="icon edit"></i>
	                                <font style="vertical-align: inherit;">
	                                    <font style="vertical-align: inherit;"> 留言
	                                    </font>
	                                </font>
	                            </div>
                            </c:if>
                            <input type="hidden" name="action" value="message">
                            <input type="hidden" name="blog_id" value="${param.blogID}">
                            <input type="hidden" name="mem_id" value="${memberVO.mem_Id}">
                            <input type="hidden" name="scroll" value="">
                            <input type="hidden" name="token" value="<%=token%>">
                        </form>
                        <!-- //留言區輸入內容的地方 -->
                    </div>
                    <!-- //留言區 -->
                    <!-- 我是隔板 -->
                    <div class="ui hidden divider"></div>
                    <!-- //我是隔板 -->
                </div>
            </div>
            <!-- //左邊內容 -->
            <!-- 右邊內容 -->
            <div class="col-4">
                <!-- 檢舉、收藏、留言按鈕 -->
                <c:if test="${login_state!=null}">
	                <div class="ui basic buttons">
	                    <button class='ui button report ${memberVO.mem_Id==blogSvc.findByPrimaryKey(param.blogID).mem_id?"disabled":""}'><i class="fas fa-ban"></i> 檢舉</button>
	                    <button class='ui button collect ${memberVO.mem_Id==blogSvc.findByPrimaryKey(param.blogID).mem_id?"disabled":""}' id="collect"><i class="collection far fa-heart" style='font-weight:${blogCollectSvc.findByPrimaryKey(memberVO.mem_Id,param.blogID)==0?"400":"900"};color:${blogCollectSvc.findByPrimaryKey(memberVO.mem_Id,param.blogID)==0?"black":"red"}'></i> 收藏</button>
	                    <button class="ui button reply"><i class="far fa-comment-dots"></i> 留言</button>
	                </div>

	                <!-- //檢舉、收藏、留言按鈕 -->
	                <!-- 我是隔板 -->
	                <div class="ui hidden divider"></div>
	                <!-- //我是隔板 -->
                </c:if>
                <!-- 作者區 -->
                <!-- 作者區如果沒有設定背景的話，預設在css裡設定 -->
                <div class="article_author_block">
                    <!-- //作者區如果沒有設定背景的話，預設在css裡設定 -->
                    <!-- 作者區 -->
                    <!-- 作者區自行設定的背景 -->
                    <div class="article_cover">
                        <!-- //作者區自行設定的背景 -->
                        <!-- 作者區的大頭貼 -->
                        <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogSvc.findByPrimaryKey(param.blogID).mem_id}">
                            <div class="article_author_avatar" style='background-image:url(<%=request.getContextPath()%>/front_end/readPic?action=member&id=${blogSvc.findByPrimaryKey(param.blogID).mem_id});'></div>
                        </a>
                        <!-- //作者區的大頭貼 -->
                        <!-- 作者區下半部 -->
                        <div class="article_author_info">
                            <div class="article_author">
                                <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogSvc.findByPrimaryKey(param.blogID).mem_id}">
                                ${memSvc.findByPrimaryKey(blogSvc.findByPrimaryKey(param.blogID).mem_id).mem_Name}的文章遊記</a>
                            </div>
                            <ul class="article_author_count">
                                <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogSvc.findByPrimaryKey(param.blogID).mem_id}#trip">
                                    <li>
                                        <span class="count">0</span>
                                        <label class="cntlabel">行程</label>
                                    </li>
                                </a>
                                <li class="list_divider"></li>
                                <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogSvc.findByPrimaryKey(param.blogID).mem_id}#blog">
                                    <li>
                                        <span class="count">${blogSvc.findByMemId(blogSvc.findByPrimaryKey(param.blogID).mem_id).size()}</span>
                                        <label class="cntlabel">文章</label>
                                    </li>
                                </a>
                                <li class="list_divider"></li>
                                <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogSvc.findByPrimaryKey(param.blogID).mem_id}#gallery">
                                    <li>
                                        <span class="count">0</span>
                                        <label class="cntlabel">照片</label>
                                    </li>
                                </a>
                            </ul>
                        </div>
                        <!-- //作者區下半部 -->
                    </div>
                </div>
                <!-- 作者區 -->
                <!-- 我是隔板 -->
                <div class="ui hidden divider"></div>
                <!-- //我是隔板 -->
                <!-- 作者的最近文章 -->
                <c:if test="${not empty blogSvc.getThreeByMem_id(blogSvc.findByPrimaryKey(param.blogID).mem_id,param.blogID)}">
                
                <div class="widgetcontainerheader">
                    ${memSvc.findByPrimaryKey(blogSvc.findByPrimaryKey(param.blogID).mem_id).mem_Name}的最近文章
                </div>
                
                <div class="widgetcontainer">
                    <ul class="helper-clear sidebar_list">
                    
					<c:forEach var="blogVO" items="${blogSvc.getThreeByMem_id(blogSvc.findByPrimaryKey(param.blogID).mem_id,param.blogID)}">
					
                        <li class="article journal">
                            <div class="cover_border">
                                <a href="<%=request.getContextPath()%>/blog.do?action=article&blogID=${blogVO.blog_id}">
                                    <div class="article_cover" style="background-image:url(<%=request.getContextPath()%>/blogPicReader?blog_id=${blogVO.blog_id});"></div>
                                </a>
                            </div>
                            <div class="article_body">
                                <div class="article_title right">
                                    <a href="<%=request.getContextPath()%>/blog.do?action=article&blogID=${blogVO.blog_id}" title="${blogVO.blog_title}">${blogVO.blog_title}</a>
                                </div>
                                <div class="article_info_container helper-clear">
                                    <div class="brief_info author">
                                        <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${blogVO.mem_id}">&nbsp;${memSvc.findByPrimaryKey(blogSvc.findByPrimaryKey(param.blogID).mem_id).mem_Name}</a>
                                    </div>
                                </div>
                                <div class="article_info_container helper-clear">
                                    <div class="brief_info view_cnt">${blogVO.blog_views}&nbsp;次瀏覽</div>
                                    <div class="brief_info_divider_right"></div>
                                    <div class="brief_info like_cnt">&nbsp;${blogCollectSvc.getAllByBlogId(param.blogID).size()}人收藏</div>
                                </div>
                            </div>
                        </li>
                        
					</c:forEach>

                    </ul>
                </div>
                <!-- //作者的最近文章 -->
                </c:if>
            </div>
            <!-- //右邊內容 -->
        </div>
        <!-- 右邊的Go to top Button -->
        <button id="myBtn" title="Go to top">
            <i class="fas fa-chevron-up"></i>
        </button>
        <!-- //右邊的Go to top Button -->
    </div>

    <!-- //內容 -->
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
                <div class="clearfix"></div>
            </div>
            <div class="copyright">
                <p>Copyright &copy; 2018 All rights reserved
                    <a href="<%=request.getContextPath()%>/front_end/index.jsp" target="_blank" title="TravelMaker">TravelMaker</a>
                </p>
            </div>
        </div>
    </div>
    <!-- //footer -->
    	<div id="collectMessage"></div>
    	
    	<div id="reportBlogDialog">
    		<div class="reportContent">
    		<div class="reportContentTitle">檢舉理由：</div>
    			<form class="ui report form" METHOD="POST" ACTION="<%=request.getContextPath()%>/blog.do">
    			<textarea class="reportReasonContent" name="br_reason" maxlength="90"></textarea>
    			<input type="hidden" name="action" value="reportBlog">
    			<input type="hidden" name="blog_id" value="${param.blogID}">
    			<input type="hidden" name="mem_id" value="${memberVO.mem_Id}">
    			<input type="hidden" name="blog_id_Owner" value="${blogSvc.findByPrimaryKey(param.blogID).mem_id}">
    			<input type="hidden" name="token" value="<%=token%>">
    			</form>
    		</div>
    	</div>
    	
    	<c:if test="${not empty errorMsgs}">
    	<div id="reportDialogConfirm">
    		<div class="messageContent">
    			<c:forEach var="message" items="${errorMsgs}">
    			${message}
    			</c:forEach>
    		</div>
    	</div>
    	</c:if>
    	
    	 <div id="reportMessageDialog">
    		<div class="reportContent">
    		<div class="reportContentTitle">檢舉理由：</div>
    			<form class="ui reportMessage form" METHOD="POST" ACTION="<%=request.getContextPath()%>/blog.do">
    			<textarea class="reportReasonContent" name="bmr_reason" maxlength="90"></textarea>
    			<input type="hidden" name="action" value="reportMessage">
    			<input type="hidden" name="message_id" id="reportBlogMessageId" value="">
    			<input type="hidden" name="mem_id" value="${memberVO.mem_Id}">
    			<input type="hidden" name="blog_id" value="${param.blogID}">
    			<input type="hidden" name="token" value="<%=token%>">
    			</form>
    		</div>
    	</div>
    	
    <div id="deleteMyMessage">
		<div>你確定要刪除留言嗎?</div>
	</div>

</body>

</html>
