<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ page import="com.mem.model.*" %>
<%@ page import="com.photo_wall.model.*" %>
<%@ page import="com.blog.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.trip.model.*" %>
<%@ page import="com.grp.model.*" %>
<%@ page import="com.question.model.*" %>
<jsp:useBean id="tripSvc" scope="page" class="com.trip.model.TripService"></jsp:useBean>
<jsp:useBean id="grpSvc" scope="page" class="com.grp.model.GrpService"></jsp:useBean>
<jsp:useBean id="qaSvc" scope="page" class="com.question.model.QuestionService"></jsp:useBean>
<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService"></jsp:useBean>

<%	 
	//因為沒有登入也可以查看他人的個人頁面，但無法顯示加入好友的按鈕
	MemberVO memberVO = (MemberVO)session.getAttribute("memberVO");
	String memId = null;
	
	//取得送過來查詢某位會員的ID的參數，以便取得他的相關個人資料
	String uId = request.getParameter("uId");

	if(uId == null){
		//如果沒取到某會員的ID，先讓他導向到首頁
		response.sendRedirect(request.getContextPath()+"/front_end/index.jsp");
		return;
	}
	
	//假設登入者跟傳入的uId參數一致時要導向...他自己的個人管理頁面
	if(memberVO != null){
		memId = memberVO.getMem_Id();
		if(uId.equals(memId)){
			response.sendRedirect(request.getContextPath()+"/front_end/personal_area/personal_area_home.jsp");
			return;
		}
	}

	//若有登入，可以看到登出按鈕
	//MemberVO memberVO = (MemberVO)session.getAttribute("memberVO");
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
	
	
	//取出該uI會員資料
	MemberService memSvc = new MemberService();
	MemberVO otherUser_memVO = memSvc.getOneMember(uId); 
	pageContext.setAttribute("otherUser_memVO",otherUser_memVO);
	
	//取出該uI照片牆的照片且狀態為1(未被檢舉 OK)
	Photo_wallService photoSvc = new Photo_wallService();
	List<Photo_wallVO> photoList=photoSvc.getByMem_id(uId);
	pageContext.setAttribute("photoList", photoList);
	
	//取出該uId部落格文章且狀態為0顯示(未被檢舉 OK)
	blogService blogSvc = new blogService();
	List<blogVO> blogList=blogSvc.findByMemId(uId);//動態從session取得會員ID
	pageContext.setAttribute("blogList", blogList);
	
	//取出uId的行程且狀態不為0的 -----  又分1未發表 2已發表(0814在改)
	List<TripVO> alluTripList =tripSvc.getByMem_id(uId);
	List<TripVO> uTripList = new ArrayList<>();
	for(TripVO tripVO : alluTripList){
		if(tripVO.getTrip_status() == 2){
			uTripList.add(tripVO);		
		}
	}
	pageContext.setAttribute("uTripList",uTripList);
	
	/***************取出uId的發起的揪團(僅撈出1成團中的)******************/
	List<GrpVO> allGrpList = grpSvc.getAll_ByMemId(uId);
	List<GrpVO> uGrpList = new ArrayList<>();
	for(GrpVO grpVO : allGrpList){
		if(grpVO.getGrp_Status() == 1){
			uGrpList.add(grpVO);		
		}
	}
	pageContext.setAttribute("uGrpList",uGrpList);
	
	/***************取出uId的發起的問答(顯示為0)******************/
	List<QuestionVO> allQAList = qaSvc.getAll();
	List<QuestionVO> uQAList = new ArrayList<>();
	for(QuestionVO qaVO : allQAList){
		if(qaVO.getMem_id().equals(uId) && qaVO.getQ_state() == 0){
			uQAList.add(qaVO);		
		}
	}
	pageContext.setAttribute("uQAList",uQAList);
%>
<%
	//取得購物車商品數量
	Object total_items_temp = session.getAttribute("total_items");
	int total_items = 0;
	if(total_items_temp != null ){
		total_items= (Integer) total_items_temp;
	}
	pageContext.setAttribute("total_items",total_items);
	pageContext.setAttribute("uId",uId);
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
    <title>Travel Maker他人個人頁面</title>
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
    <link href="<%=request.getContextPath()%>/front_end/css/personal/semantic.min.css" rel="stylesheet" type="text/css">
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
        
    <!-- AD_Page/personal_area_public相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/ad/ad_page.css" rel="stylesheet" type="text/css"><!--共用頁籤及頁尾style-->
    <link href="<%=request.getContextPath()%>/front_end/css/personal/personal_area_home.css" rel="stylesheet" type="text/css"><!--共用個人首頁上方會員資訊塊style-->
    <link href="<%=request.getContextPath()%>/front_end/css/personal/personal_area_public.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/front_end/js/personal/personal_area_public.js"></script>
    <!-- //AD_Page相關CSS及JS -->
    
	<!-- 會員檢舉使用到的jQuery Dialog -->
    <link href="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/front_end/js/personal/jquery-ui.js"></script>
    <!-- //會員檢舉使用到的jQuery Dialog -->
    
    <!-- 聊天相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css">
    <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script>
    <!-- //聊天相關CSS及JS -->
    
	<!-- 登入才會有的功能(檢舉、送出或接受交友邀請通知)-->
	<c:if test="${memberVO != null}">
		<script>
			/**再送出好友邀請時，先比對是否傳遞的參數都有出去**/
			function checkSendFriendMessage(me_id,fri_id){
				var checkRelationShip = "<%=friSvc.findRelationship(memId,uId)%>";
				
				if(me_id.trim() == "" || fri_id.trim() == "" || checkRelationShip != "null"){
					alert("未取到登入者或對方的會員ID");
					return false;
				}
				
				var jsonObj = {
					"title"		:"好友邀請",
				  	"sender"	: me_id,
				 	"receiver"	: fri_id,
				 	"message"	:"您有一筆好友邀請待確認",
				 	"link"	:"need"
				};
				webSocket.send(JSON.stringify(jsonObj));
				
				return true;
			}
			
			/**再送出好友邀請時，先比對是否傳遞的參數都有出去**/
			function comfirmBeFriMessage(me_id,fri_id){
				console.log("有送出確認好友邀請");
				if(me_id.trim() == "" || fri_id.trim() == ""){
					alert("未取到登入者或對方的會員ID");
					return false;
				}
				
				var jsonObj = {
					"title"		:"好友關係確認",
				  	"sender"	: me_id,
				 	"receiver"	: fri_id,
				 	"message"	:"您與${memberVO.mem_Name}已成為好友"
				};
				webSocket.send(JSON.stringify(jsonObj));
				
				return true;
			}
		</script>
		
		<%@ include file="/front_end/personal_area/chatModal_JS.file" %>
		
	</c:if>
	  
    
    
	<script>

    	$(document).ready(function(){
    		/*若沒登錄，根本不用去確認與該會員是否為好友?*/
    		if(<%=memId != null%>){
	    		$.ajax({
	    			url:"/CA102G4/fri.do",
	    			type:"POST",
	    			data:{action:"checkFri",meId:"<%=(String)memId%>",uId:"<%=uId%>"},
	    			success:function(data){
	    				console.log(data);//得到取得的好友關係狀態碼時，給予對應的btn
	    				
	    				if(data == 0){
	    					$("#mem_ind_name_friBtn").html(
		    						"<a class='ui inverted green button mini' onclick='return checkSendFriendMessage(\"<%=(String)memId%>\",\"<%=uId%>\");' href='<%=request.getContextPath()%>/fri.do?action=insertFri&meId=<%=(String)memId%>&friId=<%=uId%>&local=public_area'>"+
		    							"<i class='icon plus'></i>加入好友"+
		    						"</a>");	
	    				}else if(data == 1){
	    					//我有送好友邀請給他了，但他尚未確認，送出時，按鈕disabled掉，不給退已送的好友邀請
	    					$("#mem_ind_name_friBtn").html(
		    						"<button class='ui blue button mini' disabled>"+
		    						"<i class='fas fa-user-plus'></i>&nbsp好友邀請確認中</button>");
	    				}else if (data == 2){
	    					//他跟我是好友，按下後，會倒到自己的好友管理頁面
	    					$("#mem_ind_name_friBtn").html(
	    						"<a href='<%=request.getContextPath()%>/front_end/personal_area/personal_area_friend.jsp' class='ui inverted pink button mini'>"+
	    						"<i class='fas fa-check'></i>&nbsp好友</a>");
	    				}else if (data == 3){
	    					//他跟我是好友，但是她被我封鎖了
	    					$("#mem_ind_name_friBtn").html(
		    						"<a class='ui inverted orange button mini' href='<%=request.getContextPath()%>/fri.do?action=unBlockFri&meId=<%=(String)memId%>&friId=<%=uId%>&local=public_area' onclick='return confirm(\"確定要解除封鎖嗎?\");'>"+
		    						"<i class='fas fa-user-slash'></i>&nbsp解除封鎖</a>");
	    				}else if (data == 404){
	    					window.location.href="<%=request.getContextPath()%>/front_end/index.jsp";
	    				}else if(data.trim() == "他加我好友"){
	    					//對方有送給我好友邀請，要顯示確認或刪除邀請
	    					$("#mem_ind_name_friBtn").html(
		    						"&nbsp;&nbsp;<div class='ui buttons mini'>"+
			    						"<a class='ui green button mini' href='<%=request.getContextPath()%>/fri.do?action=becomeFri&meId=<%=(String)memId%>&friId=<%=uId%>&local=public_area' onclick='return comfirmBeFriMessage(\"<%=(String)memId%>\",\"<%=uId%>\");'>"+
			    						"<i class='fas fa-check-circle'></i>&nbsp確認</a>"+
		    						  "<div class='or'></div>"+
			    						"<a class='ui button mini' href='<%=request.getContextPath()%>/fri.do?action=reject&meId=<%=(String)memId%>&friId=<%=uId%>&local=public_area' onclick=''>"+
			    						"<i class='fas fa-trash-alt'></i>&nbsp刪除邀請</a>"+
		    						"</div>");
	    				}
	    			},
	    			error:function(){
	    				alert("失敗,未呼叫到fri.do");
	    			}
	    			
	    		});/**Ajax end**/
    		}else{
    			
    		}

    	});/**document read end**/
    	
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
                        <li>
							<a class="top_banner" href="<%=request.getContextPath()%>/front_end/store/store_cart.jsp">
								<i class="fa fa-shopping-cart shopping-cart" aria-hidden="true"></i><span class="badge">${total_items}</span>
							</a>
						</li>
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
                    <img src="<%=request.getContextPath()%>/front_end/images/all/person_public_bar.jpg">
                </div>
                <!--會員訊息--> 
                <div class="mem_ind_info"> 
                    <div class="mem_ind_img">
                    	<c:choose>
                    		<c:when test="${otherUser_memVO.mem_Photo == null}">
                    			<img src='<%=request.getContextPath()%>/front_end/images/all/mem_nopic.jpg'>
                    		</c:when>
                    		<c:otherwise>
                    			<img src='<%=request.getContextPath()%>/front_end/readPic?action=member&id=${otherUser_memVO.mem_Id}'>
                    		</c:otherwise>
                    	</c:choose>
                    </div>
                    <div class="mem_ind_name">
                        <p>${otherUser_memVO.mem_Name}
                        	<c:if test="${otherUser_memVO.mem_Sex == 1}">
	       						<i class='fas fa-male' style='color:#4E9EE2'></i>
	      					</c:if>
	      					<c:if test="${otherUser_memVO.mem_Sex == 2}">
	       						<i class='fas fa-female' style='color:#EC7555'></i>
	     					</c:if>
                        	
							<span id='mem_ind_name_friBtn'>
								<!-- 這邊要放關係確認的結果??? -->
							</span>
                        	<span id='mem_report' style='float:right'>
                        	    <!-- 有登入才能檢舉 -->
                        		<c:if test="${memberVO != null}">
                        			<button type='button' class='ui inverted red button mini _Memreport'>
                        				<i class="fas fa-exclamation-triangle"></i>&nbsp;檢舉
                        			</button>
                        		</c:if>
                        	</span>	 	
                        </p>
                        <p class="text-truncate" style="font-size:0.9em;padding-top:10px;max-height:110px">
                           ${otherUser_memVO.mem_Profile}
                        </p>
                    </div>
                </div> 
            </div>
            <!--//會員個人頁面標頭-->
            <!--會員個人頁面-首頁內容-->
            <div class="mem_ind_content">
              <!-- 頁籤項目 -->
              <ul class="nav nav-tabs">
                <li class="nav-item active">
                  <a href="#home_right" data-toggle="tab">
                      <i class="fas fa-home"></i>首頁
                  </a>
                </li>
                <li class="nav-item">
                  <a href="#blog"  data-toggle="tab">
                      <i class="fab fa-blogger"></i>旅遊記
                  </a>
                </li>
                <li class="nav-item">
                  <a href="#trip" data-toggle="tab">
                      <i class="fas fa-map"></i>行程
                  </a>
                </li>
                <li class="nav-item">
                  <a href="#grp" data-toggle="tab">
                      <i class="fas fa-bullhorn"></i>揪團
                  </a>
                </li>
                <li class="nav-item">
                  <a href="#question" data-toggle="tab">
                      <i class="question circle icon"></i>問答
                  </a>
                </li>
                <li class="nav-item">
                  <a href="#gallery" data-toggle="tab">
                      <i class="image icon"></i>相片
                  </a>
                </li>
                 <li class="nav-item">
                  <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public_sell.jsp?uId=${uId}">
                      <i class="image icon"></i>銷售
                  </a>
                </li>
              </ul>
              <!-- //頁籤項目 -->
              <!-- 頁籤項目-首頁內容 -->
              <div class="tab-content">
                <!--首頁右半邊-個人首頁-->
                <div id="home_right" class="tab-pane fade in active">
                   <!--首頁我的照片列表-->
                   <div class="u_ind_item">
                      <div class="u_title">
                          <strong>${otherUser_memVO.mem_Name}的照片</strong>
                          <a href="#gallery"  data-toggle="tab">
                          	<i class="angle double right icon"></i>更多
                          </a>
                          <div>
                              <span>${photoList.size()}</span>
                              <span>張照片</span>
                          </div>
                      </div>
                      <div class="mem_ind_item_photo">
						  <c:choose>
		                  	<c:when test="${not empty photoList}">
		                      <div class="flex">
								<c:forEach var="photovo" items="${photoList}" begin="0" end="4">
									<div class="item">
								    	<a href="<%=request.getContextPath()%>/front_end/photowall/view_photowall.jsp?photo_No=${photovo.photo_No}">
								        	<img src="<%=request.getContextPath()%>/front_end/readPic?action=photowall&id=${photovo.photo_No}">
								    	</a>
									</div>
								</c:forEach>
		                      </div>
		                     </c:when>
		                     <c:otherwise>
		                     	<img src="<%=request.getContextPath()%>/front_end/images/all/nothing.png" class="nothing">&nbsp; 尚未發表
		                     </c:otherwise>
		                  </c:choose>
                      </div>
					  <!--  <br>  -->
                   </div>
                   <hr>
                   <!--首頁我的旅遊記列表-->
                   <div class="u_ind_item">
                      <div class="u_title">
                          <strong>${otherUser_memVO.mem_Name}的旅遊記</strong>
                          <a href="#blog" data-toggle="tab"><i class="angle double right icon"></i>更多</a>
                          <div>
                              <span>${blogList.size()}</span>
                              <span>篇遊記</span>
                          </div>
                      </div>
                      <div class="mem_ind_item_blog">
                         <div class="ui items">
	                        <c:choose>
	                     	  <c:when test="${not empty blogList}">
	                     		<c:forEach var="blogvo" items="${blogList}" begin="0" end="2">
		                      	  <!-- 部落格區塊 -->
			                      <div class="item col-12">
			                        <div class="ui small image ">
			                          <a href="<%=request.getContextPath()%>/blog.do?action=article&blogID=${blogvo.blog_id}">
			                              <img src="<%=request.getContextPath()%>/blogPicReader?blog_id=${blogvo.blog_id}"/>
			                          </a>
			                        </div>
			                        <div class="content ">
			                          <div class="text-truncate header">${blogvo.blog_title}</div>
			                          <div class="meta">
			                            <span class="stay">
			                            	<i class="fas fa-calendar-alt"></i>
			                            		${blogvo.travel_date}
			                            </span>
			                          </div>
			                          <div class="description">
			                          	<i class="fas fa-align-justify"></i>
			                            <span class="_shortText">
			                                <i class="fas fa-map-marker-alt"></i>
			                                <c:set var="blog_content" value="${blogvo.blog_content}"/>
											<%= ((String)pageContext.getAttribute("blog_content")).replaceAll("<[^>]*>","").trim()%>
			                            </span>
			                          </div>
			                        </div>
			                      </div>
			                    </c:forEach>
		                      	  <!-- 部落格區塊 -->
		                       </c:when>
		                       <c:otherwise>
		                       		<img src="<%=request.getContextPath()%>/front_end/images/all/nothing.png" class="nothing">&nbsp;尚未發表
		                       </c:otherwise>
		                     </c:choose>
                         </div>  
                      </div>
                   </div>
                   <hr>
                   <!--首頁我的行程列表-->
	               <div class="u_ind_item">
	                  <div class="u_title">
	                      <strong>我的行程</strong>
	                      <a href="#trip" data-toggle="tab"><i class="angle double right icon"></i>更多</a>
	                      <div>
	                          <span>${uTripList.size()}</span>
	                          <span>篇行程</span>
	                      </div>
	                  </div>
	                  <div class="mem_ind_item_plan">
	                     <div class="ui items">
	                      <c:choose>
		                      <c:when test="${not empty uTripList}">
			                      <c:forEach var="tripvo" items="${uTripList}" begin="0" end="2">
				                      <div class="item">
				                        <div class="ui small image">
				                         <a href="<%=request.getContextPath()%>/front_end/trip/tripDetail.jsp?trip_no=${tripvo.trip_no}">
				                         	<img src="<%=request.getContextPath()%>/front_end/readPic?action=trip&id=${tripvo.trip_no}">
				                         </a>
				                        </div>
				                        <div class="content">
				                          <div class="header">${tripvo.trip_name}</div>
				                          <div class="meta">
				                            <span class="stay"><i class="fas fa-play-circle"></i>出發日期：${tripvo.trip_startDay}</span>
				                          </div>
				                          <div class="description">
				                          	<span class="stay"><i class="fas fa-clock"></i>&nbsp;${tripvo.trip_days}天</span>
				                            <p>
												<!-- 本來要放行程有幾個景點<i class="fas fa-map-marker-alt"></i> -->
				                            </p>
				                          </div>
				                        </div>
				                      </div>
			                      </c:forEach>
		                      </c:when>
	 						  <c:otherwise>
								<img src="<%=request.getContextPath()%>/front_end/images/all/nothing.png" class="nothing">尚未發表
						      </c:otherwise>
	                      </c:choose>
	                     </div>  
	                  </div>
	               </div>
                   <br>
                </div>
                <!--//首頁右半邊-個人首頁-->
       
                <!--//相片頁面-->
                <div id="gallery" class="tab-pane fade">
                	<div class='row'>
                	
                		 <c:choose>
		                  	<c:when test="${not empty photoList}">
			                  	<div class="ui three stackable cards">
									<c:forEach var="photovo" items="${photoList}">
									<div class="card">
										<div class="image">
										<a href="<%=request.getContextPath()%>/front_end/photowall/view_photowall.jsp?photo_No=${photovo.photo_No}">
									    	<img src="<%=request.getContextPath()%>/front_end/readPic?action=photowall&id=${photovo.photo_No}">
									    </a>	
										</div>
									</div>
									</c:forEach>
								 </div>
		                     </c:when>
		                     <c:otherwise>
			                     <div style="text-align:center">
			                     	<img src="<%=request.getContextPath()%>/front_end/images/all/nothing.png" class="nothing">&nbsp; 尚未發表
			                     </div>	
		                     </c:otherwise>
		                  </c:choose>
	 				
	 				</div>
                </div>
                <!--//相片頁面-->
                
                <!--部落格頁面-->
                <div id="blog" class="tab-pane fade">
                	<div class='row'>
	 				
	 				  <c:choose>
		               <c:when test="${not empty blogList}">
		               <div class="ui link cards">
						<c:forEach var="blogvo" items="${blogList}">
							<div class="ui card">
							  <div class="image">
						 		<a href="<%=request.getContextPath()%>/blog.do?action=article&blogID=${blogvo.blog_id}">
							    	<img src="<%=request.getContextPath()%>/blogPicReader?blog_id=${blogvo.blog_id}">
								</a>
							  </div>
						  	  <div class="content">
								<a href="<%=request.getContextPath()%>/blog.do?action=article&blogID=${blogvo.blog_id}" class="header">
									${blogvo.blog_title}
								</a>
								<div class="meta">
									<span class="date">
										旅行日期：${blogvo.travel_date}
									</span>
								</div>
								<div class="description">
								 <!-- 這裡可以看內文 -->	
								</div>
						  	  </div>
						  	  <div class="extra content">
						   		<a>
						      	  <i class="eye icon"></i>${blogvo.blog_views}
						    	</a>
						  	  </div>
						  	 </div>
					  	</c:forEach>
					  	</div>
		               </c:when>
                       <c:otherwise>
                       	<div style="text-align:center">
                     	 	<img src="<%=request.getContextPath()%>/front_end/images/all/nothing.png" class="nothing">&nbsp; 尚未發表
                     	</div>
                       </c:otherwise>
		              </c:choose>

	 				</div>
                </div>
                <!--部落格頁面-->
                
                <!--行程頁面-->
                <div id="trip" class="tab-pane fade">
                	<div class='row'>
	 				
	 				  <c:choose>
		               <c:when test="${not empty uTripList}">
		               <div class="ui link cards">
						<c:forEach var="tripvo" items="${uTripList}">
							<div class="ui card">
							  <div class="image">
						 		<a href="<%=request.getContextPath()%>/front_end/trip/tripDetail.jsp?trip_no=${tripvo.trip_no}">
							    	<img src="<%=request.getContextPath()%>/front_end/readPic?action=trip&id=${tripvo.trip_no}">
								</a>
							  </div>
						  	  <div class="content">
								<a href="<%=request.getContextPath()%>/front_end/trip/tripDetail.jsp?trip_no=${tripvo.trip_no}" class="header">
									${tripvo.trip_name}
								</a>
								<div class="meta">
									<span class="date">
										出發日期：${tripvo.trip_startDay}
									</span>
								</div>
								<div class="description">
								 <!-- 這裡可以看內文 -->	
								</div>
						  	  </div>
						  	  <div class="extra content">
						   		<a>
						      	  <i class="fas fa-clock"></i>${tripvo.trip_days}天
						    	</a>
						  	  </div>
						  	 </div>
					  	</c:forEach>
					  	</div>
		               </c:when>
                       <c:otherwise>
                       	<div style="text-align:center">
                     	 	<img src="<%=request.getContextPath()%>/front_end/images/all/nothing.png" class="nothing">&nbsp; 尚未發表
                     	</div>
                       </c:otherwise>
		              </c:choose>

	 				</div>
                </div>
                <!--行程頁面-->
                     
                <!--揪團頁面-->
                <div id="grp" class="tab-pane fade">
                	<div class='row'>
	 				
	 				  <c:choose>
		               <c:when test="${not empty uGrpList}">
		               <div class="ui link cards">
						<c:forEach var="grpvo" items="${uGrpList}">
							<div class="ui card">
							  <div class="image">
						 		<a href="<%=request.getContextPath()%>/front_end/grp/grp_oneview.jsp?grp_Id=${grpvo.grp_Id}">
							    	<img src="<%=request.getContextPath()%>/front_end/readPic?action=grp&id=${grpvo.grp_Id}">
								</a>
							  </div>
						  	  <div class="content">
								<a href="<%=request.getContextPath()%>/front_end/grp/grp_oneview.jsp?grp_Id=${grpvo.grp_Id}" class="header">
									${grpvo.grp_Title}
									<c:if test="${grpvo.grp_Status == 1}">
										<br><span style="color:red">(揪團中)</span>
									</c:if>
								</a>
								<div class="meta">
									<span class="date">
										揪團開始日期：<fmt:formatDate value="${grpvo.grp_Start}" pattern="yyyy-MM-dd kk:mm"/><br>
										揪團結束日期：<fmt:formatDate value="${grpvo.grp_End}" pattern="yyyy-MM-dd kk:mm"/>
									</span>
								</div>
								<div class="description">
								 <!-- 這裡可以看內文 -->	
								</div>
						  	  </div>
						  	  <div class="extra content">
						   		<a>
						      	  <i class="fas fa-map-marker-alt"></i>${grpvo.trip_Locale}
						    	</a>
						  	  </div>
						  	 </div>
					  	</c:forEach>
					  	</div>
		               </c:when>
                       <c:otherwise>
                       	<div style="text-align:center">
                     	 	<img src="<%=request.getContextPath()%>/front_end/images/all/nothing.png" class="nothing">&nbsp; 尚未發表
                     	</div>
                       </c:otherwise>
		              </c:choose>

	 				</div>
                </div>
                <!--部落格頁面-->
                
                
                <!--問答頁面-->
                <div id="question" class="tab-pane fade">
                	<div class='row'>
                	 <strong style="font-size: 1.5em;">發起的問答</strong>
	 				  <c:choose>
		               <c:when test="${not empty uQAList}">
		               <div class="ui link cards">
						<c:forEach var="qavo" items="${uQAList}">
							<div class="ui link card">
							  <div class="content">
							    <div class="header">
							      <a href="<%=request.getContextPath()%>/front_end/qa_reply/qa_reply.jsp?question_id=${qavo.question_id}">
							      	<p>${qavo.question_content}</p>
							      </a>
							    </div>
							  </div>
							  <div class="extra content">
							    <div class="right floated author">
							      <i class="far fa-calendar-alt"></i>發文日期：${qavo.build_date}
							    </div>
							  </div>
							</div>
					  	</c:forEach>
					  	</div>
		               </c:when>
                       <c:otherwise>
                       	<div style="text-align:center">
                     	 	<img src="<%=request.getContextPath()%>/front_end/images/all/nothing.png" class="nothing">&nbsp; 尚未發表
                     	</div>
                       </c:otherwise>
		              </c:choose>

	 				</div>
                </div>
                <!--部落格頁面-->
                
                
              </div>
              <!--頁籤項目-首頁內容-->
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
		
		<!-- 按下檢舉會員，會出現的輸入框 -->
		<div id="reportMemberDialog">
    		<div class="reportContent">
    		<div class="reportContentTitle">檢舉理由：</div>
    			<form class="ui report form" METHOD="POST" ACTION="<%=request.getContextPath()%>/front_end/member/member.do">
    				<textarea class="reportReasonContent" name="report_Reason" maxlength="90"></textarea>
    				<input type="hidden" name="action" value="reportMember">
    				<input type="hidden" name="mem_Id_report" value="${memberVO.mem_Id}">
    				<input type="hidden" name="mem_Id_reported" value="<%=uId%>">
    			</form>
    		</div>
    	</div>
		
		
		
		
		
    </body>

</html>
