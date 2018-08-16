<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ page import="com.mem.model.*" %>
<%@ page import="com.photo_wall.model.*" %>
<%@ page import="com.blog.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.fri.model.*" %>
<%@ page import="com.trip.model.*" %>
<%@ page import="com.grp.model.*" %>
<%@ page import="com.question.model.*" %>
<%@ page import="com.product.model.*" %>
<%@ page import="com.ord.model.*"%>
<jsp:useBean id="friSvc" scope="page" class="com.fri.model.FriendService"></jsp:useBean>
<jsp:useBean id="tripSvc" scope="page" class="com.trip.model.TripService"></jsp:useBean>
<jsp:useBean id="grpSvc" scope="page" class="com.grp.model.GrpService"></jsp:useBean>
<jsp:useBean id="qaSvc" scope="page" class="com.question.model.QuestionService"></jsp:useBean>
<jsp:useBean id="prodSvc" scope="page" class="com.product.model.ProductService"></jsp:useBean>
<jsp:useBean id="productWishlistSvc" scope="page" class="com.productWishlist.model.ProductWishlistService" />
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
	
	//取出uId的行程且狀態不為0的
	List<TripVO> uTripList =tripSvc.getByMem_id(uId);
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
	
	/***************取出查詢會員銷售商品******************/
	ProductService productSvc = new ProductService();
    Set<ProductVO> list2 = productSvc.getSellerProducts(uId);
    List<ProductVO> list = new ArrayList<ProductVO>();
    for(ProductVO productVO:list2){
    	if(productVO.getProduct_status() == 1){
    		list.add(productVO);
    	}
    }
    pageContext.setAttribute("list",list);
    pageContext.setAttribute("login_state",login_state);
	pageContext.setAttribute("memId",uId);
	session.setAttribute("location", request.getRequestURI()+"?uId="+uId);
	System.out.println(request.getRequestURI()+"?uId="+uId);
	
	//取得賣家購買清單
  	OrdService ordSvc = new OrdService();

	List<OrdVO> sellList = ordSvc.getForAllSell(uId);
	
	//有買家評價的  賣家訂單
	List<OrdVO> ratingList = new ArrayList<OrdVO>();
	
	 for(int i = 0 ;i<sellList.size();i++){
		 
	    	if(sellList.get(i).getBtos_rating()!=0){
	    		ratingList.add(sellList.get(i));
	    	}

	 }

	pageContext.setAttribute("memSvc",memSvc); 
	pageContext.setAttribute("ratingList",ratingList);
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
    <script src="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.js"></script>
    <!-- //會員檢舉使用到的jQuery Dialog -->
    
    
    <!-- 聊天相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css">
    <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script>
    <!-- //聊天相關CSS及JS -->

    <!-- 賣場相關CSS及JS -->
   	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/util.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/main.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/seller_prod_mgt.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/store_order_mgt.css">
	
	<!-- LogoIcon -->
    <link href="<%=request.getContextPath()%>/front_end/images/all/Logo_Black_use.png" rel="icon" type="image/png">
    <!-- //LogoIcon -->
	
	<style>
		.mem_ind_topbar {
		    height: 17.2718%;
		    width: 100%;
		}
		
		p {
		    font-family: 'Oswald','Noto Sans TC', sans-serif !important;
		    color: #333;
		}

		.block2-img {
				width: 270px;
				height: 270px;
				max-width: 100%;
				display: flex;
				align-items: center;
				justify-content: center;
			}
			
			.row:not(.sell){
				margin-top: unset;
			    height: unset;
			    margin-bottom: unset;
			    margin: unset;
			    position: unset;
    			z-index: unset;
			}

			a {
			    color: #111;
			    text-decoration: none;
			}
			
			a:hover, a:focus {
			    color: #aaa;
			    text-decoration: none;
			}

	</style>
    
</head>

    <body>
    
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
		    						"<a class='ui inverted green button mini' href='<%=request.getContextPath()%>/fri.do?action=insertFri&meId=<%=(String)memId%>&friId=<%=uId%>&local=public_area'>"+
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
			    						"<a class='ui green button mini' href='<%=request.getContextPath()%>/fri.do?action=becomeFri&meId=<%=(String)memId%>&friId=<%=uId%>&local=public_area' onclick=''>"+
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
                        	${otherUser_memVO.mem_Sex == 1 ? "<i class='fas fa-male' style='color:#4E9EE2'></i>" : "<i class='fas fa-female' style='color:#EC7555'></i>"}
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
            <div class="mem_ind_content" id="mem_ind_content">
              <!-- 頁籤項目 -->
              <ul class="nav nav-tabs">
                <li class="nav-item">
                  <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${memId}">
                      <i class="fas fa-home"></i>首頁
                  </a>
                </li>
                <li class="nav-item">
                  <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${memId}">
                      <i class="fab fa-blogger"></i>旅遊記
                  </a>
                </li>
                <li class="nav-item">
                  <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${memId}">
                      <i class="fas fa-map"></i>行程
                  </a>
                </li>
                <li class="nav-item">
                  <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${memId}">
                      <i class="fas fa-bullhorn"></i>揪團
                  </a>
                </li>
                <li class="nav-item">
                  <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${memId}">
                      <i class="question circle icon"></i>問答
                  </a>
                </li>
                <li class="nav-item">
                  <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public.jsp?uId=${memId}">
                      <i class="image icon"></i>相片
                  </a>
                </li>
                
                <li class="nav-item active">
	              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public_sell.jsp?uId=${memId}">
	                  <i class="money bill alternate icon"></i>銷售
	              </a>
	            </li>
              </ul>
              <!-- //頁籤項目 -->
              <!-- 頁籤項目-首頁內容 -->
              <div class="tab-content">
				<div id="sell_management" style="height:50px">
                   <div style="width: 70%;float: left">
                       <ul class="nav nav-tabs" id="sell_tab" style="float: left;padding-left: 30px;">
                          <li id="store_public" class="active"><a data-toggle="tab" href="#store_mgt" onclick="checkActiveTab();">賣場</a></li>
                          <li id="store_rating"><a data-toggle="tab" href="#rating_sell" onclick="checkActiveTab();">賣場評價</a></li>
                        </ul>
                   </div>
                </div>
                
                <div class="tab-content">
                  <!--賣場-->
                 <div id="store_mgt" class="tab-pane fade in active"> 
                	<div class='row sell'>
                	 <!--內容-->  
                    <div class="main" style="min-height: 1px;">
                    	<section class="bgwhite">
							<div class="container">
								<div class="row sell"  style="margin-bottom:0px">
							<div class="col-sm-6 col-md-8 col-lg-11 p-b-50">
								<div class="row"  style="margin-bottom:0px">
									<div class="col-sm-10 col-md-10 col-lg-12">
										<%@ include file="page1.file" %>
										<div class="page-top flex-sb-m flex-w p-b-35 p-t-40" style="display: inline-block;">
											<span class="s-text8 p-t-5 p-b-5">
												第<%=whichPage%>/<%=pageNumber%>頁  共<span id="rowno"><%=rowNumber%></span>筆
											</span>
										</div>
									</div>
								</div>
								<!-- Product -->
								<div class="row sell">
								<c:forEach var="productVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
										<div class="col-sm-12 col-md-6 col-lg-4 p-b-50">
											<!-- Block2 -->
											<div class="block2">
												<div class="block2-img wrap-pic-w of-hidden pos-relative" style="height:16rem;width: 100%;">
													<img class="prod-img" src="data:image/jpeg;base64,${productVO.product_photo_1_base}" onerror="this.src='<%=request.getContextPath()%>/front_end/images/store/no-image-icon-15.png'"  alt="IMG-PRODUCT">
													<div class="block2-overlay trans-0-4" style="width: 100%;">
														<div class="block2-btn-addcart w-size1 trans-0-4">
															<button type="button" onclick="addById(this,'${productVO.product_id}','${productVO.product_name}','${productVO.product_mem_id}','${productVO.product_price}','${login_state}')" class="add-to-cart add-prod-btn flex-c-m size1 bg4 hov1 s-text1 trans-0-4">
																加入購物車
															</button>											
														</div>
													</div>
												</div>
				
												<div class="block2-txt p-t-20">
													<a href="<%=request.getContextPath()%>/front_end/store/store_product.jsp?prod_id=${productVO.product_id}" class="block2-name dis-block s-text3 p-b-5 prod-title">
														${productVO.product_name}
													</a>
													<div class="p-t-10">
													<!-- 用登入會員id+商品id findpk，若有回傳商品vo，表示此會員有收藏此商品，則讓愛心為紅色，class wish-add-btn 加上 added && 此商品賣家會員id不等於登入會員id-->
													<c:if test="${productWishlistSvc.getOneProductWishlist(productVO.product_id,memId)!=null && productVO.product_mem_id != memId}">
														<span class="wish-add m-text6 p-r-5 p-l-5" style="float: lefft;">
															<a href="#" class="wish-add-btn added" data-login_state="${login_state}" data-memId="${memId}" data-prodId="${productVO.product_id}">
															<i class="far fa-heart" aria-hidden="true"></i>
															<i class="fas fa-heart dis-none" aria-hidden="true"></i></a>
														</span>
													</c:if>
													<!-- 用登入會員id+商品id findpk，若沒有回傳商品vo，表示此會員沒有收藏此商品，則讓愛心為白色 && 此商品賣家會員id不等於登入會員id-->
													<c:if test="${productWishlistSvc.getOneProductWishlist(productVO.product_id,memId)==null && productVO.product_mem_id != memId}">
														<span class="wish-add m-text6 p-r-5 p-l-5" style="float: lefft;">
															<a href="#" class="wish-add-btn" data-login_state="${login_state}" data-memId="${memId}" data-prodId="${productVO.product_id}">
															<i class="far fa-heart" aria-hidden="true"></i>
															<i class="fas fa-heart dis-none" aria-hidden="true"></i></a>
														</span>
													</c:if>
													<!-- 登入會員無法收藏自己的商品 class 添加 disabled-->
													<c:if test="${productVO.product_mem_id == memId}">
														<span class="wish-add m-text6 p-r-5 p-l-5" style="float: lefft;">
															<a href="#" class="wish-add-btn disabled" data-login_state="${login_state}" data-memId="${memId}" data-prodId="${productVO.product_id}">
															<i class="far fa-heart" aria-hidden="true"></i>
															<i class="fas fa-heart dis-none" aria-hidden="true"></i></a>
														</span>
													</c:if>
														<span class="wish-like-text m-text6 p-r-5" id="wish-${productVO.product_id}" style="float: lefft;">
															${productWishlistSvc.getLikesByProductid(productVO.product_id).size()}
														</span>
														<span class="block2-price m-text6 p-r-5" style="float: right;">
															$ ${productVO.product_price}
														</span>
													</div>
												</div>
											</div>
										</div>
									</c:forEach>
								</div>
								<!-- //Product -->
								
								<div class="row sell" style="margin-bottom: 0px">
									<div class="col-sm-12 col-md-6 col-lg-12 p-b-50">
										<nav aria-label="Page navigation">
											  <ul class="pagination" name="whichPage">
											  <%if (rowsPerPage<rowNumber) {%>
											      <%if(pageIndex>=rowsPerPage){%> 
											       <li><a href="<%=request.getRequestURI()%>?whichPage=<%=whichPage-1%>" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
											    <%}%>
											      <%}%> 
											  
											    
										  		  <%if (pageNumber>=1) {%>
											         <%for (int i=1; i<=pageNumber; i++){%>
													         <%if(pageIndex==(i*rowsPerPage-rowsPerPage)){%>
													          <li class="active"><a href="<%=request.getRequestURI()%>?whichPage=<%=i%>"><%=i%></a></li>
													         <%}else{%>
												            	<li><a href="<%=request.getRequestURI()%>?whichPage=<%=i%>"><%=i%></a></li>
												         	<%}%> 
											         	 <%}%> 
													  <%}%>
													  
												 <%if (rowsPerPage<rowNumber) {%>
													 
													    <%if(pageIndex<pageIndexArray[pageNumber-1]){%>
													        <li><a href="<%=request.getRequestURI()%>?whichPage=<%=whichPage+1%>" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>
													    <%}%>
												  <%}%>  
											  </ul>
										</nav>
								</div>
							</div>
							</div>
						</div>
					 </div>
					</section>
                    </div>
                    <!--內容--> 
	 				</div>
                </div>
                <!--//賣場-->
                
                 <!--賣場的評價-->
                  <div id="rating_sell" class="tab-pane fade">
                  		<div class="container">
						<div class="row sell" style="height: 470px;">	
							<div class="col-md-10 col-lg-10"  style="padding-top:20px;padding-bottom: 100px;">
							<c:forEach var="ordVO" items="${ratingList}">
							<div class="host p-t-20">
								<a href="#" target="_blank" class="photo" style="background-image: url(<%=request.getContextPath()%>/front_end/readPic?action=member&id=${ordVO.buyer_mem_id})">
								</a>
								<span class="text" style="display:inline-block">	
									<a href="#" target="_blank" style="display:inline-block">${memSvc.getOneMember(ordVO.buyer_mem_id).mem_Name}</a>	
								</span>
							</div>
							<div class="text order_id">訂單編號： ${ordVO.order_id}</div>
							<hr class="divider-w pt-20">
								<div class="star">	
										<ul class="list-inline" data-rating="0" title="Average Rating -0">
											<c:if test="${ordVO.btos_rating >= 1}">
												<li title="1" id="6-1" data-index="1" data-business_id="6" data-rating="0" style="color: rgb(255, 204, 0); font-size: 28px;">★</li>
											</c:if>
											<c:if test="${ordVO.btos_rating < 1}">
												<li title="1" id="6-1" data-index="1" data-business_id="6" data-rating="0" style="color: rgb(204, 204, 204); font-size: 28px;">★</li>
											</c:if>
											
											<c:if test="${ordVO.btos_rating >= 2}">
												<li title="2" id="6-2" data-index="2" data-business_id="6" data-rating="0" style="color: rgb(255, 204, 0); font-size: 28px;">★</li>
											</c:if>
											<c:if test="${ordVO.btos_rating < 2}">
												<li title="2" id="6-2" data-index="2" data-business_id="6" data-rating="0" style="color: rgb(204, 204, 204); font-size: 28px;">★</li>
											</c:if>
											<c:if test="${ordVO.btos_rating >= 3}">
												<li title="3" id="6-3" data-index="3" data-business_id="6" data-rating="0" style="color: rgb(255, 204, 0); font-size: 28px;">★</li>
											</c:if>
											<c:if test="${ordVO.btos_rating < 3}">
												<li title="3" id="6-3" data-index="3" data-business_id="6" data-rating="0" style="color: rgb(204, 204, 204); font-size: 28px;">★</li>
											</c:if>
												<c:if test="${ordVO.btos_rating >= 4}">
												<li title="4" id="6-4" data-index="4" data-business_id="6" data-rating="0" style="color: rgb(255, 204, 0); font-size: 28px;">★</li>
											</c:if>
											<c:if test="${ordVO.btos_rating < 4}">
												<li title="4" id="6-4" data-index="4" data-business_id="6" data-rating="0" style="color: rgb(204, 204, 204); font-size: 28px;">★</li>
											</c:if>
												<c:if test="${ordVO.btos_rating >= 5}">
												<li title="5" id="6-5" data-index="5" data-business_id="6" data-rating="0" style="color: rgb(255, 204, 0); font-size: 28px;">★</li>
											</c:if>
											<c:if test="${ordVO.btos_rating < 5}">
												<li title="5" id="6-5" data-index="5" data-business_id="6" data-rating="0" style="color: rgb(204, 204, 204); font-size: 28px;">★</li>
											</c:if>
										</ul>
									</div>
									<div class="p-b-30 p-t-30">${ordVO.btos_rating_descr}</div>
							<hr class="divider-w p-t-10" style="border-color:#313438">
							</c:forEach>
							</div>
						</div>
					</div>
                  </div>
                  <!--//賣場的評價-->
                
         		 </div>
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
<!--========================收藏商品=======================================================================-->

<script>

	  	$('.wish-add-btn').on('click', function(e) {
		  e.preventDefault();
			var memId = $(this).attr("data-memId");
			var prodId = $(this).attr("data-prodId");
			var login_state = $(this).attr("data-login_state");
			console.log(login_state=="true");
			
	     if(login_state=="true"){
			 if($(this).hasClass('added')){
				 $(this).removeClass('added');
				 var action = "delete";
				 $.ajax({
					 url:"${pageContext.request.contextPath}/front_end/store/productWishlist.do",
					 method:"POST",
					 data:{wishlist_mem_id:memId, action:action,wishlist_product_id:prodId,login_state:login_state},
					 success:function(data){
						// alert("刪除成功!");
						$("#wish-"+prodId).html(data.wishlikesize);
						console.log(data.wishlikesize);
					 }
				 })
				}else if($(this).hasClass('disabled')!= true){
				 $(this).addClass('added');
				 var action = "insert";
				 $.ajax({
					 url:"${pageContext.request.contextPath}/front_end/store/productWishlist.do",
					 method:"POST",
					 data:{wishlist_mem_id:memId, action:action,wishlist_product_id:prodId,login_state:login_state},
					 success:function(data){
						 //alert("新增成功!");
						 $("#wish-"+prodId).html(data.wishlikesize);
						 console.log(data.wishlikesize);
						 var oldrowno = $('#rowno').html();
							$('#rowno').html(oldrowno-1);
					 }
				 })
			 }else{
				//do nothing;
				 
			 }
			}else{
				 window.location = '<%=request.getContextPath()%>/front_end/member/mem_login.jsp';
			}
		});

</script>
<!--========================購物車動畫=======================================================================-->
<script src="https://static.codepen.io/assets/common/stopExecutionOnTimeout-41c52890748cd7143004e05d3c5f786c66b19939c4500ce446314d1748483e13.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>

	<script>
	$(document).ready(function () {
	$('.add-to-cart').on('click', function () {
        var cart = $('.shopping-cart');
        console.log(cart.offset())
        var imgtodrag = $(this).parent().parent().parent().find("img").eq(0);
        if (imgtodrag) {
            var imgclone = imgtodrag.clone()
                .offset({
                top: imgtodrag.offset().top,
                left: imgtodrag.offset().left
            }).css({
					'opacity': '0.5',
                    'position': 'absolute',
                    'height': '150px',
                    'width': '150px',
                    'z-index': '100'
            }).appendTo($('body'))
            .animate({
               		'top': cart.offset().top + 10,
                    'left': cart.offset().left + 10,
                    'width': 75,
                    'height': 75
            }, 1000, 'easeInOutExpo');
            
            

            imgclone.animate({
                'width': 0,
                    'height': 0
            }, function () {
                $(this).detach()
            });
        }
    });
	
	});
</script>	
<!--===============================================================================================-->
<!--加入購物車-->
<script>
		function addById(e, product_id,product_name,product_mem_id,product_price,login_state){
			var action = "ADD";
			$.ajax({ 
				url:"${pageContext.request.contextPath}/front_end/store/shopping.do",
				method:"POST",
				data:{action:action,product_id:product_id,product_name:product_name,product_mem_id:product_mem_id,product_price:product_price,quantity:"1",login_state:login_state},
				success:function(data){
					if(data === 'not log in'){
						console.log("轉跳!");
						window.location.replace("${pageContext.request.contextPath}/front_end/member/mem_login.jsp");
					}else{
						console.log("添加成功!");
						$('.badge').text(data);
					}
					
				} 
			})
		
		}
  
</script>
<!--===============================================================================================-->
<script>
function checkActiveTab() {
	var store = $('#store_public');
	var rating = $('#store_rating');
	var content = $('#mem_ind_content');
	if(rating.hasClass("active")){
		console.log('store active:'+store.hasClass("active"));
		content.css("margin-top", "0px");
		
	}else if(store.hasClass("active")){
		console.log('rating active:'+rating.hasClass("active"));
		content.css("margin-top", "220px");
	}
}

</script>





    </body>

</html>
