<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.product.model.*"%>
<%@ page import="com.productWishlist.model.*"%>
<%@ page import="com.productCategory.model.*"%>
<%@ page import="com.mem.model.*" %>
<%@ page import="com.ord.model.*" %>

<%
    ProductService productSvc = new ProductService();

	Integer prod_id = null;
	if(request.getParameter("prod_id")!=null){
   	   prod_id = new Integer(request.getParameter("prod_id"));
    }else if(request.getAttribute("prod_id")!=null){
    	String s = (String)(request.getAttribute("prod_id"));
    	prod_id = Integer.parseInt(s);
    } 
    ProductVO productVO =  productSvc.getOneProduct(prod_id);
    pageContext.setAttribute("productVO",productVO);
    //查看照片欄位的照片，有就加到list
    List<String> list = new ArrayList<String>();
    if(productVO.getProduct_photo_1() != null && productVO.getProduct_photo_1().length>0){
    	list.add(productVO.getProduct_photo_1_base());
    }
    if(productVO.getProduct_photo_2() != null && productVO.getProduct_photo_2().length>0){
    	list.add(productVO.getProduct_photo_2_base());
    }
    if(productVO.getProduct_photo_3()!= null && productVO.getProduct_photo_3().length>0){
    	list.add(productVO.getProduct_photo_3_base());
    }
    if(productVO.getProduct_photo_4()!= null && productVO.getProduct_photo_4().length>0){
    	list.add(productVO.getProduct_photo_4_base());
    }
    if(productVO.getProduct_photo_5() != null && productVO.getProduct_photo_5().length>0){
    	list.add(productVO.getProduct_photo_5_base());
    }
    pageContext.setAttribute("list",list);
	
	//確認登錄狀態
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

		pageContext.setAttribute("login_state",login_state);
		
	
 
	//若登入狀態為是true(育萱把memId拿出來，因為我下面程式會用到)
	String memId = "null";
	/***************取出登入者會員資訊******************/
	if( login_state == true){
		memId = ((MemberVO)session.getAttribute("memberVO")).getMem_Id();
		pageContext.setAttribute("memId",memId);
	}

	//為了join(寫法有servlet3.0限制)
	MemberService memSvc = new MemberService();
	pageContext.setAttribute("memSvc",memSvc); 
	
    //取出會員名稱
	session.setAttribute("location", request.getRequestURI()+"?prod_id="+prod_id);
	
	//取得購物車商品數量
		Object total_items_temp = session.getAttribute("total_items");
		int total_items = 0;
		if(total_items_temp != null ){
			total_items= (Integer) total_items_temp;
		}
		pageContext.setAttribute("total_items",total_items);
%>

<jsp:useBean id="productWishlistSvc" scope="page" class="com.productWishlist.model.ProductWishlistService" />
<jsp:useBean id="productCategorySvc" scope="page" class="com.productCategory.model.ProductCategoryService" />
<jsp:useBean id="ordSvc" scope="page" class="com.ord.model.OrdService" />


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
	    
	    <!-- bootstrap css、JS檔案 -->
	    <link href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css" rel="stylesheet" type="text/css" media="all" />
	    <script src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
	    <!-- //bootstrap-css -->
	    
	    <!-- 套首頁herder和footer css -->
	    <link href="<%=request.getContextPath()%>/front_end/css/all/index_style_header_footer.css" rel="stylesheet" type="text/css" media="all" />
	    <!-- //套首頁herder和footer css -->
	    
	    <!-- font-awesome icons -->
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" 
		integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
		<!-- //font-awesome icons -->
		
		<!-- font字體 -->
		<link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
		<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
		<!-- //font字體 -->
	
		<!--store 自定義的css -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/util.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/main.css">
	    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/store_banner.css" media="all" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/store_header.css" media="all" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/store_footer.css" media="all" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/store_product.css" media="all" />

		 <!-- //store 自定義的css -->
	    <!-- LogoIcon -->
	    <link href="<%=request.getContextPath()%>/front_end/images/all/Logo_Black_use.png" rel="icon" type="image/png">
	    <!-- //LogoIcon -->
		<style>
			p{
				color:#555;
			}
			
			.carousel-inner > .item > img,
			.carousel-inner > .item > a > img {
			  height: 370px;   
			  margin: auto;
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
		<div class="banner about-bg" id="myHeader">
			<div class="top-banner about-top-banner">
				<div class="container">
					<div class="top-banner-left">
						<ul>
							<li><i class="fa fa-phone" aria-hidden="true"></i>
								<a href="tel:034257387"> 03-4257387</a>
							</li>
							<li>
								<a href="mailto:TravelMaker@gmail.com"><i class="fa fa-envelope" aria-hidden="true"></i> TravelMaker@gmail.com</a>
							</li>
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
								<a class="top_banner" href="<%=request.getContextPath()%>/front_end/store/store_cart.jsp"><i class="fa fa-shopping-cart shopping-cart" aria-hidden="true"></i><span class="badge">${total_items}</span></a>
							</li>
							<li>
								<a class="top_banner" href="#"><i class="fa fa-envelope" aria-hidden="true"></i></a>
							</li>
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
									<li>
										<a href="<%=request.getContextPath()%>/front_end/news/news.jsp">最新消息</a>
									</li>
									<li>
										<a href="<%=request.getContextPath()%>/front_end/attractions/att.jsp">景點介紹</a>
									</li>
									<li>
										<a href="<%=request.getContextPath()%>/front_end/trip/trip.jsp">行程規劃</a>
									</li>
									<li>
										<a href="<%=request.getContextPath()%>/blog.index">旅遊記</a>
									</li>
									<li>
										<a href="<%=request.getContextPath()%>/front_end/question/question.jsp">問答區</a>
									</li>
									<li>
										<a href="<%=request.getContextPath()%>/front_end/photowall/photo_wall.jsp">照片牆</a>
									</li>
									<li>
										<a href="<%=request.getContextPath()%>/front_end/grp/grpIndex.jsp">揪團</a>
									</li>
									<li>
										<a href="<%=request.getContextPath()%>/front_end/store/store.jsp">交易平台</a>
									</li>
									<li>
										<a href="<%=request.getContextPath()%>/front_end/ad/ad.jsp">專欄</a>
									</li>

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
		<!-- store -->
		<div class="main" style="min-height: 1px;">
			<section class="module-small module-small-shop">
				<div class="container">
					<nav class="prod-breadcrumb mt-0">
							<a href="<%=request.getContextPath()%>/front_end/index.jsp">首頁</a>&nbsp;/&nbsp;<a href="<%=request.getContextPath()%>/front_end/store/store.jsp">交易平台</a>&nbsp;/&nbsp;${productCategorySvc.getOneProductCategory(productVO.product_category_id).product_category_name}&nbsp;/&nbsp;${productVO.product_name}
					</nav>
				</div>
			</section>
			
			<section class="bgwhite p-t-55 p-b-65">
				<div class="container">
					<div class="row">
						<div class="col-sm-6 col-md-6 col-lg-6 p-b-50">
							<div id="myCarousel" class="carousel slide" data-ride="carousel">
								  <!-- Indicators -->
								  <ol class="carousel-indicators">
								    	<c:forEach var="pic_base_str" items="${list}" varStatus="status">
					   			    	 
								    		<c:if test="${status.index == 0}"> 
												<li data-target="#myCarousel" data-slide-to="${status.index}" class="active"></li>
											</c:if>
											<c:if test="${status.index != 0}"> 
												<li data-target="#myCarousel" data-slide-to="${status.index}"></li>
											</c:if>
										</c:forEach>
								  </ol>
								
								  <!-- Wrapper for slides -->
								  <div class="carousel-inner">
									  <c:forEach var="pic_base_str" items="${list}" varStatus="status">
								    		
											<c:if test="${status.index== 0}">  
												<div class="item active">
								     			 	<img src="data:image/jpeg;base64,${pic_base_str}" onerror="this.src='<%=request.getContextPath()%>/front_end/images/store/no-image-icon-15.png'">
												 </div>
										    </c:if>
											<c:if test="${status.index!= 0}"> 
												<div class="item">
								     			 	<img src="data:image/jpeg;base64,${pic_base_str}" onerror="this.src='<%=request.getContextPath()%>/front_end/images/store/no-image-icon-15.png'">
												 </div>
											 </c:if>
											
										</c:forEach>
								  </div>
								
								  <!-- Left and right controls -->
								  <a class="left carousel-control" href="#myCarousel"  role="button" data-slide="prev">
								    <span class="glyphicon glyphicon-chevron-left"></span>
								    <span class="sr-only">Previous</span>
								  </a>
								  <a class="right carousel-control" href="#myCarousel"  role="button" data-slide="next">
								    <span class="glyphicon glyphicon-chevron-right"></span>
								    <span class="sr-only">Next</span>
								  </a>
								</div>
						</div>
						
						<div class="col-sm-6 col-md-6 col-lg-6 w-size14 respon5 p-l-50">
							<h4 class="product-detail-name m-text16 p-b-20" style="min-height: 130px;">
								${productVO.product_name}
							</h4>
						
							<span class="m-text17">
								$ ${productVO.product_price}
							</span>
							<div style="display:inline-block;float: right;">
							<!-- 用登入會員id+商品id findpk，若有回傳商品vo，表示此會員有收藏此商品，則讓愛心為紅色，class wish-add-btn 加上 added && 此商品賣家會員id不等於登入會員id-->
							   <c:if test="${productWishlistSvc.getOneProductWishlist(productVO.product_id,memId)!=null && productVO.product_mem_id != memId}">
									<span class="wish-add m-text6 p-r-5 p-l-5" style="font-size: 22px;">
												<a href="#" class="wish-add-btn added" data-login_state="${login_state}" data-memId="${memId}" data-prodId="${productVO.product_id}"> 
												<i class="far fa-heart" aria-hidden="true"></i>
												<i class="fas fa-heart dis-none" aria-hidden="true"></i></a>
									</span>
									<span class="wish-like-text m-text6 p-r-5" id="wish-${productVO.product_id}" style="font-size: 22px;">
										${productWishlistSvc.getLikesByProductid(productVO.product_id).size()}
									</span>
								</c:if>
								<!-- 用登入會員id+商品id findpk，若沒有回傳商品vo，表示此會員沒有收藏此商品，則讓愛心為白色 && 此商品賣家會員id不等於登入會員id-->
								<c:if test="${productWishlistSvc.getOneProductWishlist(productVO.product_id,memId)==null && productVO.product_mem_id != memId}">
									<span class="wish-add m-text6 p-r-5 p-l-5" style="font-size: 22px;">
												<a href="#" class="wish-add-btn" data-login_state="${login_state}" data-memId="${memId}" data-prodId="${productVO.product_id}"> 
												<i class="far fa-heart" aria-hidden="true"></i>
												<i class="fas fa-heart dis-none" aria-hidden="true"></i></a>
									</span>
									<span class="wish-like-text m-text6 p-r-5" id="wish-${productVO.product_id}" style="font-size: 22px;">
										${productWishlistSvc.getLikesByProductid(productVO.product_id).size()}
									</span>
								</c:if>
								<!-- 此商品賣家會員id等於登入會員id，表示是自己販賣的商品，不能收藏自己的商品，在class 加上 disabled -->
								<c:if test="${productVO.product_mem_id == memId}">
									<span class="wish-add m-text6 p-r-5 p-l-5" style="font-size: 22px;">
										<a href="#" class="wish-add-btn disabled" data-login_state="${login_state}" data-memId="${memId}" data-prodId="${productVO.product_id}">
										<i class="far fa-heart" aria-hidden="true"></i>
										<i class="fas fa-heart dis-none" aria-hidden="true"></i></a>
									</span>
									<span class="wish-like-text m-text6 p-r-5" id="wish-${productVO.product_id}" style="font-size: 22px;">
										${productWishlistSvc.getLikesByProductid(productVO.product_id).size()}
									</span>
								</c:if>
							</div>
							<div class="host p-t-30">
									<a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public_sell.jsp?uId=${productVO.product_mem_id}" target="_blank" class="photo" style="background-image:url(<%=request.getContextPath()%>/front_end/readPic?action=member&id=${productVO.product_mem_id})">
									</a>
									<span class="text" style="display:inline-block;height: 10px;position: absolute;bottom: 160px;right: 310px;letter-spacing: 2px;">	
										<a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public_sell.jsp?uId=${productVO.product_mem_id}" target="_blank" style="display:inline-block">${memSvc.getOneMember(productVO.product_mem_id).mem_Name}</a>	
									</span>
									<span class="star" style="display:inline-block ;position: absolute;height: 10px;bottom: 135px;right: 265px;">	
										<ul class="list-inline" data-rating="0" title="Average Rating -0">
											<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) >= 1}">
												<li title="1" id="6-1" data-index="1" data-business_id="6" data-rating="0" class="rating" style="color: rgb(255, 204, 0); font-size: 16px;">★</li>
											</c:if>
											<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) < 1}">
												<li title="1" id="6-1" data-index="1" data-business_id="6" data-rating="0" class="rating" style="color: rgb(204, 204, 204); font-size: 16px;">★</li>
											</c:if>
											
											<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) >= 2}">
												<li title="2" id="6-2" data-index="2" data-business_id="6" data-rating="0" class="rating" style="color: rgb(255, 204, 0); font-size: 16px;">★</li>
											</c:if>
											<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) < 2}">
												<li title="2" id="6-2" data-index="2" data-business_id="6" data-rating="0" class="rating" style="color: rgb(204, 204, 204); font-size: 16px;">★</li>
											</c:if>
											<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) >= 3}">
												<li title="3" id="6-3" data-index="3" data-business_id="6" data-rating="0" class="rating" style="color: rgb(255, 204, 0); font-size: 16px;">★</li>
											</c:if>
											<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) < 3}">
												<li title="3" id="6-3" data-index="3" data-business_id="6" data-rating="0" class="rating" style="color: rgb(204, 204, 204); font-size: 16px;">★</li>
											</c:if>
												<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) >= 4}">
												<li title="4" id="6-4" data-index="4" data-business_id="6" data-rating="0" class="rating" style="color: rgb(255, 204, 0); font-size: 16px;">★</li>
											</c:if>
											<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) < 4}">
												<li title="4" id="6-4" data-index="4" data-business_id="6" data-rating="0" class="rating" style="color: rgb(204, 204, 204); font-size: 16px;">★</li>
											</c:if>
												<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) >= 5}">
												<li title="5" id="6-5" data-index="5" data-business_id="6" data-rating="0" class="rating" style="color: rgb(255, 204, 0); font-size: 16px;">★</li>
											</c:if>
											<c:if test="${ordSvc.getRatingBySellerId(productVO.product_mem_id) < 5}">
												<li title="5" id="6-5" data-index="5" data-business_id="6" data-rating="0" class="rating" style="color: rgb(204, 204, 204); font-size: 16px;">★</li>
											</c:if>
										</ul>
									</span>
							</div>
							<c:if test="${productVO.product_mem_id != memId}">
								<div class="cart p-t-30">
									<div class="quantity"><input type="number" id="qty_${productVO.product_id}" class="input-text qty" step="1" min="1" max="${productVO.product_stock}" name="quantity" value="1" title="Qty" size="4"></div>
									<button type="button"  onclick="addById(this,'${productVO.product_id}','${productVO.product_name}','${productVO.product_mem_id}','${productVO.product_price}','${login_state}','${productVO.product_stock}')" name="add-to-cart" class="flex-c-m size1 bg4 hov4 s-text1 trans-0-4 add-to-cart-btn">加入購物車</button>
								</div>
							</c:if>
							<c:if test="${productVO.product_mem_id == memId}">
								<form class="cart p-t-30" action="<%=request.getContextPath()%>/front_end/store/product.do" method="post">
									<div class="quantity"><input type="number" id="qty_${productVO.product_id}" style="visibility: hidden;" class="input-text qty" step="1" min="1" max="${productVO.product_stock}" name="quantity" value="1" title="Qty" size="4"></div>
									<button type="submit" name="edit-prod" class="flex-c-m size1 bg4 hov4 s-text1 trans-0-4 add-to-cart-btn">編輯商品</button>				
									<input type="hidden" name="product_id"  value="${productVO.product_id}">
								    <input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
								    <input type="hidden" name="action" value="getOne_For_Update">
								</form>
							</c:if>
							<c:if test="${productVO.product_mem_id != memId}">
							    <!-- 如果不是登入者自己賣的商品時，會出現聊聊跟檢舉按鈕 -->
								<form class="chat p-t-50" method="post"  onSubmit="return checkIsLogin('${memId}','${productVO.product_mem_id}');">
									<button type="submit" name="chat-to-seller" class="flex-c-m size1 bg4 hov4 s-text1 trans-0-4 chat-to-seller-btn"><i class="fa fa-comments" aria-hidden="true"></i>聊聊</button>
								</form>
								<div class="report p-t-50" style='display:inline;'> 
									<button type="button" name="report-prod" class="flex-c-m size1 bg4 hov4 s-text1 trans-0-4 report-prod-btn" data-toggle="modal" data-target="#reportModal" onclick="reportProd('${productVO.product_id}','${memId}','${login_state}')"><i class="fa fa-flag" aria-hidden="true"></i>檢舉</button>
								</div>
							</c:if>
							<c:if test="${productVO.product_mem_id == memId}">
								<form class="chat p-t-50" action="" method="post" enctype="multipart/form-data">
									<button type="submit" name="chat-to-seller" style="visibility: hidden;" class="flex-c-m size1 bg4 hov4 s-text1 trans-0-4 chat-to-seller-btn"><i class="fa fa-comments" aria-hidden="true"></i>聊聊</button>
								</form>
								<form class="report p-t-50" action="" method="post" enctype="multipart/form-data" style='display:inline;'> 
									<button type="submit" name="report-prod" style="visibility: hidden;" class="flex-c-m size1 bg4 hov4 s-text1 trans-0-4 report-prod-btn"><i class="fa fa-flag" aria-hidden="true"></i>檢舉</button>
								</form>
							</c:if>
							
							
						</div>
						
					</div>	
					
					
					<div class="row">
						<div class="col-sm-12 col-md-12 col-lg-12 p-b-50">
							<ul class="nav nav-tabs">
							  <li class="active"><a data-toggle="tab" href="#home">商品詳情</a></li> 
							</ul>
							
							<div class="tab-content">
							  <div id="home" class="tab-pane fade in active p-l-50 p-t-50">
							    <p class="fs-20">${productVO.product_descr}</p>
							  </div>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>

	
		<!-- store -->
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
								<li>
									<a href="about.html">關於Travel Maker</a>
								</li>
								<li>
									<a href="about.html">聯絡我們</a>
								</li>
								<li>
									<a href="about.html">常見問題</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="col-md-3 footer-grid">
						<div class="footer-grid-heading">
							<h4>網站條款</h4>
						</div>
						<div class="footer-grid-info">
							<ul>
								<li>
									<a href="about.html">服務條款</a>
								</li>
								<li>
									<a href="services.html">隱私權條款</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="col-md-3 footer-grid">
						<div class="footer-grid-heading">
							<h4>社群</h4>
						</div>
						<div class="social">
							<ul>
								<li>
									<a href="https://www.facebook.com/InstaBuy.tw/"><i class="fab fa-facebook"></i></a>
								</li>
								<li>
									<a href="https://www.instagram.com/"><i class="fab fa-instagram"></i></a>
								</li>
								<li>
									<a href="#"><i class="fab fa-line"></i></a>
								</li>
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


<!--===============================================================================================-->
<!-- reportModal -->
<div id="reportModal" class="modal fade" role="dialog">
  <div class="modal-dialog modal-sm">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">檢舉原因</h4>
      </div>
      <div class="modal-body">
        <textarea rows="4" cols="40" id='reportDescr'></textarea>
      </div>
      <div class="modal-footer">
      	 <input type="hidden" name="memId" id="memId" value=""/>
      	 <input type="hidden" name="prodId" id="prodId" value=""/>
         <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
         <button type="button" class="btn btn-info" data-dismiss="modal" onclick="reportById()">完成</button>
      </div>
    </div>

  </div>
</div>

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
					 url:"productWishlist.do",
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
					 url:"productWishlist.do",
					 method:"POST",
					 data:{wishlist_mem_id:memId, action:action,wishlist_product_id:prodId,login_state:login_state},
					 success:function(data){
						 //alert("新增成功!");
						 $("#wish-"+prodId).html(data.wishlikesize);
						 console.log(data.wishlikesize);
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
<!--===============================================================================================-->
<!--加入購物車-->
<script>
		function addById(e, product_id,product_name,product_mem_id,product_price,login_state,product_stock){
			var action = "ADD";
			var quantity = $("#qty_"+product_id).val();
			if(quantity>product_stock){
				window.alert("商品數量超過商品庫存! 商品庫存為"+product_stock);
				$("#qty_"+product_id).val(1);
			}else if(quantity <= 0){
				window.alert("加入數量最少 1 個");
				$("#qty_"+product_id).val(1);
			}else{
				$.ajax({ 
					url:"shopping.do",
					method:"POST",
					data:{action:action,product_id:product_id,product_name:product_name,product_mem_id:product_mem_id,product_price:product_price,login_state:login_state,quantity:quantity},
					success:function(data){
						if(data === 'not log in'){
							console.log("轉跳!");
							window.location.replace("${pageContext.request.contextPath}/front_end/member/mem_login.jsp");
						}else{
							alert("添加成功!");
							$('.badge').text(data);
						}
						
					} 
				})
			}

		}
  
</script>	

<!--檢舉商品-->
<script>
		function reportProd(product_id,memId,login_state){
			if(login_state != 'true'){
				window.location.replace("${pageContext.request.contextPath}/front_end/member/mem_login.jsp");
			}else{
				$('#prodId').val(product_id);
				$('#memId').val(memId);
			}

		}
		
		function reportById(){
			var action = "reportProd";
			var product_id = $('#prodId').val();
			var memId = $('#memId').val();
			var reportDescr = $('#reportDescr').val();
			$.ajax({ 
				url:"shopping.do",
				method:"POST",
				data:{action:action,product_id:product_id,memId:memId,reportDescr:reportDescr},
				success:function(data){
					if(data == null){
						window.alert('Travel Maker 收到您的檢舉囉!');
					}else{
						window.alert(data);
					}
					
				} 
			})

		}
  
</script>


<!--聊聊-->
<script>
//還要排除封鎖狀態
function checkIsLogin(memid,seller){

	if(memid == undefined || memid == "" || memid.trim().lenth == 0){
		window.location.replace("${pageContext.request.contextPath}/front_end/member/mem_login.jsp");
		return false;
	}else if($("#"+seller+"status").val() != undefined){
		//在判斷目前這個賣家是否已經為好友且無封鎖狀態,就可直接開啟與他人的聊天視窗
		$("#"+seller+"status").trigger("click");
		return false;
	}else if("${friSvc.findRelationship(memId, productVO.getProduct_mem_id()).getFri_Stat()}" == "3"){
		alert("您已封鎖該位賣家!");
		return false;
	}
	//若我沒封鎖他，也不是好友的話，要去新增聊天對話並開啟聊天對話openChatRoom(chatRoom_id,chatRoom_Name,cnt)
	$.ajax({
			url:"/CA102G4/chatRoom.do",
			type:"POST",
			data:{action:"addNewCR_seller",meId:memid,sellerId:seller},
			success:function(data){
				console.log(data);
				if(data.toString().trim() == "錯誤：未取到登入者的會員ID。" || data.toString().trim() == "錯誤：未取到好友的會員ID。"
						|| data.toString().trim() == "錯誤：發生Exception。"){
					alert("回傳後，發生問題");
					return;
				}
				var jsonObject = JSON.parse(data);
				openChatRoom(jsonObject.CRID,jsonObject.crName,jsonObject.cnt);
			},
			error:function(){
				alert("失敗,未呼叫到fri.do");
			}
			
	});/**Ajax end**/
	alert("有登入");
	return false;
}


		

  
</script>

</body>
</html>