<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.product.model.*"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.shopping.model.*"%>

<%
		//確認登錄狀態
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

	//若登入狀態為是true
	/***************取出登入者會員資訊******************/
	if (login_state == true) {
		String memId = ((MemberVO) session.getAttribute("memberVO")).getMem_Id();
		pageContext.setAttribute("memId", memId);
		pageContext.setAttribute("memberVO", memberVO);
		
	}
	
	//若登入狀態為不是true，紀錄當前頁面並重導到登入畫面。
		if( login_state != true){
			session.setAttribute("location", request.getRequestURI());
			response.sendRedirect(request.getContextPath()+"/front_end/member/mem_login.jsp");
			return;
		}

	//為了join(寫法有servlet3.0限制)
	MemberService memSvc = new MemberService();
	pageContext.setAttribute("memSvc", memSvc);

	ProductService productSvc = new ProductService();
	pageContext.setAttribute("productSvc", productSvc);
	
	//取得購物車商品數量
	Object total_items_temp = session.getAttribute("total_items");
	int total_items = 0;
	if(total_items_temp != null ){
		total_items= (Integer) total_items_temp;
	}
	pageContext.setAttribute("total_items",total_items);
	
%>


<%

	List<CartItem> orderItemList = (List<CartItem>)request.getAttribute("orderItemList");
	List<String> sellerList = new ArrayList<String>();
	for(CartItem c : orderItemList){
		String memId = c.getProduct_mem_id();
		if(!sellerList.contains(memId)) sellerList.add(memId);
	}

	pageContext.setAttribute("sellerList", sellerList);
	pageContext.setAttribute("orderItemList", orderItemList);

	String[] productIdList = (String[])request.getAttribute("productIdList");
	String productIdListStr = Arrays.toString(productIdList);
	pageContext.setAttribute("productIdListStr", productIdListStr);
	
	String sellerListStr = sellerList.toString();
	pageContext.setAttribute("sellerListStr", sellerListStr);
	
	
	//給下訂單按鈕用的token
	String token = (String)session.getAttribute("token");
	pageContext.setAttribute("token", token);
%>

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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<link
	href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<!-- //bootstrap JS檔案 往後放 -->
<!-- //bootstrap-css -->

<!-- 套首頁herder和footer css -->
<link
	href="<%=request.getContextPath()%>/front_end/css/all/index_style_header_footer.css"
	rel="stylesheet" type="text/css" media="all" />
<!-- //套首頁herder和footer css -->

<!-- font-awesome icons -->
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
	integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp"
	crossorigin="anonymous">
<!-- //font-awesome icons -->

<!-- font字體 -->
<link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Pacifico'
	rel='stylesheet' type='text/css'>
<!-- //font字體 -->

<!--store 自定義的css -->
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/store_step.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/util.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/main.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/store_checkout.css">	
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/store_header.css"
	media="all" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/store_footer.css"
	media="all" />
<link rel="stylesheet" type="text/css"
href="<%=request.getContextPath()%>/front_end/css/store/store_cart.css"
media="all" />

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/store_checkout2.css">	

<!-- //store 自定義的css -->


<!-- store 自定義的js -->

<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<!-- //store 自定義的js -->

	
	<!-- 聊天相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css">
    <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script>
    <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script>
    <%@ include file="/front_end/personal_area/chatModal_JS.file" %>
    <!-- //聊天相關CSS及JS -->
	

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

	<!-- banner -->
	<div class="banner about-bg" id="myHeader">
		<div class="top-banner about-top-banner">
			<div class="container">
				<div class="top-banner-left">
					<ul>
						<li><i class="fa fa-phone" aria-hidden="true"></i> <a
							href="tel:034257387"> 03-4257387</a></li>
						<li><a href="mailto:TravelMaker@gmail.com"><i
								class="fa fa-envelope" aria-hidden="true"></i>
								TravelMaker@gmail.com</a></li>
					</ul>
				</div>
				<div class="top-banner-right">
					<ul>				
						<li><a href="<%= request.getContextPath()%>/front_end/member/member.do?action=logout"><span class=" top_banner"><i class=" fas fa-sign-out-alt" aria-hidden="true"></i></span></a></li>
						<li><a class="top_banner" href="<%=request.getContextPath()%>/front_end/personal_area_home.html"><i class="fa fa-user" aria-hidden="true"></i></a></li>
						<li><a class="top_banner" href="<%=request.getContextPath()%>/front_end/store/store_cart.jsp"><i class="fa fa-shopping-cart shopping-cart" aria-hidden="true"></i><span class="badge">${total_items}</span></a></li>
                        <li><a class="top_banner" href="#"><i class="fa fa-envelope" aria-hidden="true"></i></a></li>
					</ul>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
		<div class="header">
			<div class="container">
				<div class="logo">
					<h1>
						<a href="<%=request.getContextPath()%>/front_end/index.jsp">Travel
							Maker</a>
					</h1>
				</div>
				<div class="top-nav">
					<!-- 當網頁寬度太小時，導覽列會縮成一個按鈕 -->
					<nav class="navbar navbar-default">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">Menu
					</button>
					<!-- //當網頁寬度太小時，導覽列會縮成一個按鈕 --> <!-- Collect the nav links, forms, and other content for toggling -->
					<div class="collapse navbar-collapse"
						id="bs-example-navbar-collapse-1">
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

							<div class="clearfix"></div>
						</ul>
					</div>
					</nav>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>
	<!-- //banner -->
<!-- store -->
	<div class="main" style="min-height: 1px;">
		<div class="container">
			<div class="row p-b-70">
				<section>
					<div class="wizard">
						<!-- 上方 Tab-->
						<div class="wizard-inner">
							<div class="connecting-line"></div>
							<ul class="nav nav-tabs" role="tablist">
								<!-- Step 1 tab-->
								<li role="presentation" class="active">
									<a href="#step1" data-toggle="tab" aria-controls="step1" role="tab" title="Step 1">
										<span class="round-tab">
			                                <i class="fa fa-truck" style="line-height:70px"></i>
			                            </span>
									</a>
								</li>
								<!-- Step 2 tab-->
								<li role="presentation" class="disabled">
									<a href="#step2" data-toggle="tab" aria-controls="step2" role="tab" title="Step 2">
										<span class="round-tab">
			                                <i class="fa fa-credit-card" style="line-height:70px"></i>
			                            </span>
									</a>
								</li>
								<!-- Step 3 tab-->
								<li role="presentation" class="disabled">
									<a href="#complete" data-toggle="tab" aria-controls="complete" role="tab" title="Complete">
										<span class="round-tab">
			                                <i class="glyphicon glyphicon-ok" style="line-height:70px"></i>
			                            </span>
									</a>
								</li>
							</ul>
						</div>
						<!-- //上方 Tab END-->
						
						<form role="form" action="shopping.do" method="post">
							<div class="tab-content">
								<!-- Step 1 tab 內容-->
								<div class="tab-pane active" role="tabpanel" id="step1">
									<section class="page-module-content module module-cart-bottom">
										<div class="container">
											<div class="row">
												<!-- Content column start -->
												<div class="col-sm-12 col-md-10 col-md-offset-1">
													<!-- Step 1 tab 內容表頭-->
												
													<div>
											            <table class="table table-hover">
											                <thead>
											                    <tr>
											                        <th class="col-sm-1 col-md-1"> </th>
											                        <th class="col-sm-6 col-md-4">訂單
											                        	商品</th>
											                        <th class="col-sm-1 col-md-1 text-center">數量</th>
											                        <th class="col-sm-1 col-md-1 text-center">單價</th>
											                        <th class="col-sm-1 col-md-1 text-center">總計</th>
											                        <th class="col-sm-1 col-md-1 text-center"> </th>
											                    </tr>
											                </thead>
											            </table>
										            </div>
										            <!-- //Step 1 tab 內容表頭 END-->
										            <!-- Step 1 tab 訂單內容-->
												<c:forEach var="sellerId" items="${sellerList}">
													<!-- 一個賣家的購物車商品table -->
													
														<div class="p-b-50">
															<!-- 賣家名稱 -->
															<div class="cart-store-header">
																<i class="fas fa-store-alt p-l-20"></i><a href="#"
																	class="s-text3 p-l-10">${memSvc.getOneMember(sellerId).mem_Name}</a>
															</div>
															<!-- //賣家名稱 end -->
															<table class="table table-hover p-t-100">
																<tbody>
															<c:forEach var="cartItem" items="${orderItemList}" varStatus="counter">
															 <c:if test="${cartItem.product_mem_id eq sellerId }">
																	<tr>
																		<td class="col-sm-1 col-md-1">
																		</td>
																		<td class="col-sm-6 col-md-4">
																			<div class="media">
																				<div class="pull-left m-b-10 m-t-10" href="#">
																					<img class="media-object"
																						src="data:image/jpeg;base64,${productSvc.getOneProduct(cartItem.product_id).product_photo_1_base}"
																						style="width: 72px; height: 72px;">
																				</div>
																				<div class="media-body">
																					<h4 class="media-heading p-l-20 p-t-40 s-text3">
																						<a
																							href="<%=request.getContextPath()%>/front_end/store/store_product.jsp?prod_id=${cartItem.product_id}"
																							target="_blank" class="s-text3">${cartItem.product_name}</a>
																					</h4>
																				</div>
																			</div>
																		</td>
																		<td class="col-sm-1 col-md-1" style="text-align: center">
																			${cartItem.quantity}
																		</td>
																		<td class="col-sm-1 col-md-1 text-center">$
																			<span id="prod_price_${cartItem.product_id}">${cartItem.product_price}</span>
																		</td>
																		<td class="col-sm-1 col-md-1 text-center">$
																			<span id="total_price_${cartItem.product_id}">${cartItem.total_price}</span>
																		</td>
																		<td class="col-sm-1 col-md-1">
																		</td>
																	</tr>
																
																<!-- 一個賣家的購物車商品table end -->
																</c:if>
															</c:forEach>
													  		</tbody>
														</table>
													</div>
													</c:forEach>
											         <!-- //Step 1 tab 訂單內容-->
													<hr class="divider-w pt-20">
													 <!-- Step 1 tab 物流選擇-->
													<div class="checkout-shipping">
														<div class="checkout-shipping-title">寄送資訊
															<div class="checkout-shipping-price">$ 0</div>
														</div>
														<div class="checkout-shipping-details" style="display:inline-block">
															<div class="checkout-shipping-details-selected-method" style="display:inline-block" id="checkout-ship-method">宅配</div>
															<div class="checkout-shipping-details-selected-address">
																<div class="checkout-store-name" id="checkout-store-name"></div>
																<div class="checkout-shipping-details-name" style="display:inline-block">${memberVO.mem_Name}</div>
																<div class="checkout-shipping-details-phone" style="display:inline-block">${memberVO.mem_Phone}</div>
																<div class="checkout-shipping-details-address" style="display:inline-block" id="checkout-addr">${memberVO.mem_Address}</div>
															</div>
															<div class="checkout-shipping-change" style="padding-top: 20px;">
																<button type="button" class="btn btn-lg btn-change-shipping" data-toggle="modal" data-target="#711Modal">變更</button>
															</div>
														</div>
													</div>
														
													 <!-- //Step 1 tab 物流選擇-->
													<hr class="divider-w pt-20">
													<div class="checkout-review" align="right">
														<div class="checkout-review-title" style="display:inline-block">訂單金額 (${ord_total_items} 商品):</div>
														<div class="checkout-review-price" style="display:inline-block">$ ${ord_total_prie}</div>
													</div>
												</div>
											</div>
										</div>
									</section>
									
										<ul class="list-inline pull-right">
											<li><button type="submit" id="place-order" class="btn next-step place-order">下訂單</button></li>
										</ul>
									<input type="hidden" name="judgeDuplicate" value="${token}"/>
									<input type="hidden" name="productIdListStr" value="${productIdListStr}"/>
									<input type="hidden" name="sellerListStr" value="${sellerListStr}"/>	
									<input type="hidden" name="shipMethod" id='ship-input' value=""/>	
									<input type="hidden" name="storeName" id='store-input' value=""/>
									<input type="hidden" name="addr" id='addr-input' value=""/>	
									<input type="hidden" name="action" value="CHECKOUT_TO_PAY"/>
									<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
								</div>
								<!-- //Step 1 tab 內容 END-->
								
							</div>
						</form>
					</div>
				</section>
			</div>
		</div>
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
							<li><a href="https://www.facebook.com/InstaBuy.tw/"><i
									class="fab fa-facebook"></i></a></li>
							<li><a href="https://www.instagram.com/"><i
									class="fab fa-instagram"></i></a></li>
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
							<input type="email" id="mc4wp_email" name="EMAIL"
								placeholder="請輸入您的Email" required=""> <input
								type="submit" value="訂閱">
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
	
<!--===============================================================================================-->	
<!-- Shipping Modal -->
<jsp:useBean id="store711CitySvc" scope="page" class="com.store711City.model.Store711CityService" />
<jsp:useBean id="streetSvc" scope="page" class="com.street.model.StreetService" />
  <div class="modal fade" id="711Modal" role="dialog">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">選擇寄送方式</h4>
        </div>
        <div class="modal-body">
        	<!--Radio group-->
			<div class="form-check">
			<div id="store-block">
			    <input name="ship" type="radio" class="form-check-input with-gap" id="radio-store" value='store'>超商取貨
			    <c:if test="${not empty memberVO.STORE_NAME_1}">
				    <div class="shipping-store" id="store-block-1">
				    	<input name="store" type="radio" class="shipping-store-input" value='store-1'><span class="p-r-10" >${memberVO.mem_Name}</span>7-11 <span class="p-r-10" id='store-name-1'>${memberVO.STORE_NAME_1}</span><span class="p-r-10">${memberVO.mem_Phone}</span><span class="p-r-10" id='store-addr-1'>${memberVO.STORE_ADDR_1}</span>
						<span class="remove-btn" style="cursor: pointer;" id="delete_store1" onclick="deleteStoreByCol(this,'1')">
							<i class="fas fa-trash-alt"></i>
						</span>
					</div>
				</c:if>
				<c:if test="${not empty memberVO.STORE_NAME_2}">
				    <div class="shipping-store" id="store-block-2">
				    	<input name="store" type="radio" class="shipping-store-input" value='store-2'><span class="p-r-10" >${memberVO.mem_Name}</span>7-11 <span class="p-r-10" id='store-name-2'>${memberVO.STORE_NAME_2}</span><span class="p-r-10">${memberVO.mem_Phone}</span><span class="p-r-10" id='store-addr-2'>${memberVO.STORE_ADDR_2}</span>
						<span class="remove-btn" style="cursor: pointer;" id="delete_store2" onclick="deleteStoreByCol(this,'2')">
							<i class="fas fa-trash-alt"></i>
						</span>
					</div>
				</c:if>
				<c:if test="${not empty memberVO.STORE_NAME_3}">
				    <div class="shipping-store" id="store-block-3">
				    	<input name="store" type="radio" class="shipping-store-input" value='store-3'><span class="p-r-10" >${memberVO.mem_Name}</span>7-11 <span class="p-r-10" id='store-name-3'>${memberVO.STORE_NAME_3}</span><span class="p-r-10">${memberVO.mem_Phone}</span><span class="p-r-10" id='store-addr-3'>${memberVO.STORE_ADDR_3}</span>
						<span class="remove-btn" style="cursor: pointer;" id="delete_store3" onclick="deleteStoreByCol(this,'3')">
							<i class="fas fa-trash-alt"></i>
						</span>
					</div>
				</c:if>
			</div>	
					<div class="p-t-20">
						<button type="button" class="btn add-store" onclick="showStoreAdd(this)"><i class="fas fa-plus"></i>新增店鋪</button>
					</div>
				
				<div class="p-t-20 p-b-50" id="storeDiv" style="display:none">
					<div class="pull-right" onclick="hideStoreAdd(this)" style="cursor: pointer;"><i class="fas fa-times"></i></div>
					<div class="dis-block p-t-35">
						<div>
							<span class="p-r-10">
								 <label>市區 :</label>
								 <select size="1" name="store_711_city_id" class="city" onchange="city_change()">
								 	<option selected="selected">--選擇市區--</option>
							         <c:forEach var="store711CityVO" items="${store711CitySvc.all}" > 
							          	<option value="${store711CityVO.store_711_city_id}">${store711CityVO.store_711_city_name}
							         </c:forEach>   
						      	 </select>
							 </span>
							<span class="p-r-10">
								 <label>行政區 :</label> 
								 <select size="1" name="store_711_dist_id" class="dist" onchange="dist_change()">
								  	<option selected="selected">--選擇行政區--</option>
						      	 </select>
							</span>
							 <span class="p-r-10">
								 <label>711地址 :</label> 
									 <select class="store" name="store_711_id">
								 	<option selected="selected">--選擇711地址--</option>
								 </select>
							 </span>
							 <div class="p-t-20">
							 	<button type="button" class="btn confirm-store-add pull-right btn-info" onclick="">確定新增</button>
							 </div>
						 </div>
					</div>
				</div>
			</div>
			<hr class="divider-w pt-20">
			<div class="form-check">
			<div id ="home-block">
			    <input name="ship" type="radio" class="form-check-input with-gap" id="radio-home" value='home' checked>賣家宅配
			    <c:if test="${not empty memberVO.delivery_Address_1}">
				    <div class="shipping-home" id='home-block-1'>
				    	<input name="home" type="radio" class="shipping-home-input" value='home-1'><span class="p-r-10" >${memberVO.mem_Name}</span><span class="p-r-10" >${memberVO.mem_Phone}</span><span class="p-r-10" id='home-addr-1'>${memberVO.delivery_Address_1}</span>
					    <span class="remove-btn" style="cursor: pointer;" id="delete_store1" onclick="deleteHomeByCol(this,'1')">
							<i class="fas fa-trash-alt"></i>
						</span>
					</div>
				</c:if>
				<c:if test="${not empty memberVO.delivery_Address_2}">
				    <div class="shipping-home" id='home-block-2'>
				    	<input name="home" type="radio" class="shipping-home-input" value='home-2'><span class="p-r-10" >${memberVO.mem_Name}</span><span class="p-r-10">${memberVO.mem_Phone}</span><span class="p-r-10" id='home-addr-2'>${memberVO.delivery_Address_2}</span>
						<span class="remove-btn" style="cursor: pointer;" id="delete_store2" onclick="deleteHomeByCol(this,'2')">
							<i class="fas fa-trash-alt"></i>
						</span>
					</div>
				</c:if>
				<c:if test="${not empty memberVO.delivery_Address_3}">
				    <div class="shipping-home" id='home-block-3'>
				    	<input name="home" type="radio" class="shipping-home-input" value='home-3'><span class="p-r-10" >${memberVO.mem_Name}</span><span class="p-r-10" >${memberVO.mem_Phone}</span><span class="p-r-10" id='home-addr-3'>${memberVO.delivery_Address_3}</span>
						<span class="remove-btn" style="cursor: pointer;" id="delete_home3" onclick="deleteHomeByCol(this,'3')">
							<i class="fas fa-trash-alt"></i>
						</span>
					</div>
				</c:if>
			 </div>
				
					<div class="p-t-20">
						<button type="button" class="btn add-home-addr" onclick="showHomeAdd(this)"><i class="fas fa-plus"></i>新增地址</button>
					</div>
				
				
				<div class="p-t-20 p-b-50" id="homeDiv" style="display:none">
					<div class="pull-right" onclick="hideHomeAdd(this)" style="cursor: pointer;"><i class="fas fa-times"></i></div>
					<div class="dis-block p-t-35">
						<div>
							<span class="p-r-10">
								 <label>市區 :</label>
								 <select size="1" name="home_city" class="city-home" onchange="home_city_change()">
								 	<option selected="selected">--選擇市區--</option>
							         <c:forEach var="city" items="${streetSvc.allCity}" > 
							          	<option value="${city}">${city}
							         </c:forEach>   
						      	 </select>
							 </span>
							<span class="p-r-10">
								 <label>行政區 :</label> 
								 <select size="1" name="home_dist" class="dist-home" onchange="home_dist_change()">
								  	<option selected="selected">--選擇行政區--</option>
						      	 </select>
							</span>
							 <span class="p-r-10">
								 <label>街道 :</label> 
									 <select class="home-street" name="home_street">
								 	<option selected="selected">--選擇街道--</option>
								 </select>
							 </span>
							 <div class="p-b-10 p-t-10">
							 	<label> 地址:</label> <input type="text" id="home_addr" name="home_addr" placeholder="請輸入地址" size="100">
							 </div>
							 
							 <div class="p-t-20">
							 	<button type="button" class="btn confirm-home-add pull-right btn-info" onclick="">確定新增</button>
							 </div>
						 </div>
					</div>
				</div>
				
				
			</div>
			
			<!--Radio group-->

        </div>
        
        <div class="modal-footer">
           <button type="button" class="btn btn-close" data-dismiss="modal">取消</button>
  		   <button type="button" class="btn btn-done confirm-addr" data-dismiss="modal">完成</button>
        </div>
      </div>
    </div>
  </div>
<!--===============================================================================================-->
<!--===============================================================================================-->
<script type="text/javascript">
	        //tab js
			$(document).ready(function() {
				//Initialize tooltips
				$('.nav-tabs > li a[title]').tooltip();

				//Wizard
				$('a[data-toggle="tab"]').on('show.bs.tab', function(e) {

					var $target = $(e.target);

					if($target.parent().hasClass('disabled')) {
						return false;
					}
				});

				$(".next-step").click(function(e) {

					var $active = $('.wizard .nav-tabs li.active');
					$active.next().removeClass('disabled');
					nextTab($active);

				});
				$(".prev-step").click(function(e) {

					var $active = $('.wizard .nav-tabs li.active');
					prevTab($active);

				});
				
				$("#order-checkout").click(function(e) {

					var $active = $('.wizard .nav-tabs li.active');
					$active.prev().addClass('disabled');
					$active.prev().prev().addClass('disabled');
					nextTab($active);
				});
				
				$("#place-order").click(function(e) {

					var $active = $('.wizard .nav-tabs li.active');
					$active.prev().addClass('disabled');
					nextTab($active);
				});
			});

			function nextTab(elem) {
				$(elem).next().find('a[data-toggle="tab"]').click();
			}

			function prevTab(elem) {
				$(elem).prev().find('a[data-toggle="tab"]').click();
			}
			
			//payment radio
			$(document).ready(function () {
			
			    $('input[type="radio"]').click(function () {
					if ($(this).attr("id") == "cash") {
			            $(".credit-row").hide('slow');
			        }
			  
			          if ($(this).attr("id") == "credit") {
			            $(".credit-row").show('slow');
			
			        }
			    });
			
			    $('input[type="radio"]').trigger('click');  // trigger the event
			});
			
			//payment radio end
			
			
			//shipping radio
			$(document).ready(function () {

			    $('input[type="radio"]').click(function () {
			    	var size_store = $('#store-block').find('.shipping-store').length;
					console.log("store size"+size_store);
					var size_home = $('#home-block').find('.shipping-home').length;
					console.log("home size"+size_home);
			    	
			    	
					if ($(this).attr("id") == "radio-store") {
			            $(".shipping-home").hide('slow');
			            $(".add-home-addr").hide('slow');
			            $("#homeDiv").hide('slow');
			        }
			        if ($(this).attr("id") == "radio-store") {
			            $(".shipping-store").show('slow');
			           
			             if(size_store>=3){
								$('.add-store').css("display", "none");
							}else{
								$('.add-store').show('slow');
							}
			
			        }
			         if ($(this).attr("id") == "radio-home") {
			            $(".shipping-store").hide('slow');
			             $(".add-store").hide('slow');
			            $("#storeDiv").hide('slow');
			        }
			          if ($(this).attr("id") == "radio-home") {
			            $(".shipping-home").show('slow');
			            //$(".add-home-addr").show('slow');
			            if(size_home>=3){
							$('.add-home-addr').css("display", "none");

						}else{
							$('.add-home-addr').show('slow');
						}
			        }
			    });
			
			    $('input[type="radio"]').trigger('click');  // trigger the event
			});
			
			//shipping radio end
			
</script>	
	
<!--===============================================================================================-->
<!-- 新增店鋪按鈕 -->

<script>
function showStoreAdd(e){
	$('#storeDiv').css("display", "block");
}	

function hideStoreAdd(e){
	$('#storeDiv').css("display", "none");
}


function showHomeAdd(e){
	$('#homeDiv').css("display", "block");
}

function hideHomeAdd(e){
	$('#homeDiv').css("display", "none");
}

</script>	
<!-- 711 選擇 -->
<script type="text/javascript">
function city_change()
{
    var city = $(".city").val();
 
    $.ajax({
        type: "POST",
        url: "dist.jsp",
        data: "store_711_city_id="+city,
        cache: false,
        success: function(response)
        {
            $(".dist").html(response);
        }
    });
}
function dist_change()
{
    var dist = $(".dist").val();
 
    $.ajax({
        type: "POST",
        url: "711store.jsp",
        data: "store_711_dist_id="+dist,
        cache: false,
        success: function(response)
        {
            $(".store").html(response);
        }
    });
}



//宅配 選擇 
function home_dist_change()
{
    var country = $(".dist-home").val();
 	var action = "getRoadByCountry";
    
    $.ajax({
        type: "POST",
        url: "street.do",
        data:{action:action,country:country},
        cache: false,
        success: function(result)
        {
        	for(var i=0;i<result.length;i++)
        	{           
        		$(".home-street").append("<option value="+result[i]+">"+result[i]);
        	}
        	
        }
    });
}


function home_city_change()
{
    var city = $(".city-home").val();
 	var action = "getCountryByCity";
    
    $.ajax({
        type: "POST",
        url: "street.do",
        data:{action:action,city:city},
        cache: false,
        success: function(result)
        {
        	for(var i=0;i<result.length;i++)
        	{           
        		$(".dist-home").append("<option value="+result[i]+">"+result[i]);
        	}
           
        }
    });
}


</script>	


<script>
// 711 store 確定新增，送資料庫新增
$('.confirm-store-add').on('click', function(e) {
	e.preventDefault();
	var action = "update_store_addr";
	var memId = "${memId}";
	var storeNo = $(".store").val();
	$.ajax({
		url:"${pageContext.request.contextPath}/front_end/member/member.do",
		method:"POST",
		async: false,
		data:{action:action,memId:memId,storeNo:storeNo},
		success:function(result){
			console.log("新增成功");
			console.log(result.storeName);
			console.log(result.storeAddr);
			console.log(result.col);
			var appendEle = $('#store-block');
			if(result.col ==1){
				appendEle.append("<div class='shipping-store' id='store-block-1'>"+
		    	"<input name='store' type='radio' class='shipping-store-input' value='store-1' checked><span class='p-r-10'>${memberVO.mem_Name}</span>7-11 <span class='p-r-10' id='store-name-1'>"+result.storeName+"</span><span class='p-r-10'>${memberVO.mem_Phone}</span><span class='p-r-10' id='store-addr-1'>"+result.storeAddr+"</span>"+
				"<span class='remove-btn' style='cursor: pointer;' id='delete_store1' onclick='deleteStoreByCol(this,\"1\")'>"+
				"<i class='fas fa-trash-alt'></i></span></div>");
				  
			}else if(result.col ==2){
				appendEle.append("<div class='shipping-store' id='store-block-2'>"+
				    	"<input name='store' type='radio' class='shipping-store-input' value='store-2' checked><span class='p-r-10'>${memberVO.mem_Name}</span>7-11 <span class='p-r-10' id='store-name-2'>"+result.storeName+"</span><span class='p-r-10'>${memberVO.mem_Phone}</span><span class='p-r-10' id='store-addr-2'>"+result.storeAddr+"</span>"+
						"<span class='remove-btn' style='cursor: pointer;' id='delete_store2' onclick='deleteStoreByCol(this,\"2\")'>"+
						"<i class='fas fa-trash-alt'></i></span></div>");
			}else if(result.col ==3){
				appendEle.append("<div class='shipping-store' id='store-block-3'>"+
				    	"<input name='store' type='radio' class='shipping-store-input' value='store-3' checked><span class='p-r-10'>${memberVO.mem_Name}</span>7-11 <span class='p-r-10' id='store-name-3'>"+result.storeName+"</span><span class='p-r-10'>${memberVO.mem_Phone}</span><span class='p-r-10' id='store-addr-3'>"+result.storeAddr+"</span>"+
						"<span class='remove-btn' style='cursor: pointer;' id='delete_store3' onclick='deleteStoreByCol(this,\"3\")'>"+
						"<i class='fas fa-trash-alt'></i></span></div>");
			}else{
				alert("數量已達3個，請先刪除，再新增!")
			}
			
			$('#storeDiv').css("display", "none");
	
			var size_store = $('#store-block').find('.shipping-store').length;
			if(size_store>=3){
				$('.add-store').css("display", "none");
			}else{
				$('.add-store').css("display", "block");
			}
			
		}
	})

});

$(document).ready(function () {
var size_store = $('#store-block').find('.shipping-store').length;
console.log("store size"+size_store);
var size_home = $('#home-block').find('.shipping-home').length;
console.log("home size"+size_home);

if(size_store>=3){
	$('.add-store').css("display", "none");
}else{
	$('.add-store').css("display", "block");
}


if(size_home>=3){
	$('.add-home-addr').css("display", "none");

}else{
	$('.add-home-addr').css("display", "block");
}

})

//711 store 刪除
function deleteStoreByCol(e,col){
	var action = "delete_store_addr";
	var memId = "${memId}";
	$.ajax({
		url:"${pageContext.request.contextPath}/front_end/member/member.do",
		method:"POST",
		async: false,
		data:{action:action,memId:memId,col:col},
		success:function(data){
			console.log("刪除成功");
			console.log(data);
			
			if(data == 1){
				$('#store-block-1').remove();
			}else if(data == 2){
				$('#store-block-2').remove();
			}else if(data == 3){
				$('#store-block-3').remove();
			}
			$('.add-store').css("display", "block");
		}
	})

}



//宅配地址 確定新增，送資料庫新增

$('.confirm-home-add').on('click', function(e) {
	e.preventDefault();
	var action = "update_home_addr";
	var memId = "${memId}";
	var city = $(".city-home").val();
	var dist = $(".dist-home").val();
	var road = $(".home-street").val();
	var street = $('#home_addr').val();
	var addr = city+dist+road+street;
	
	$.ajax({
		url:"${pageContext.request.contextPath}/front_end/member/member.do",
		method:"POST",
		async: false,
		data:{action:action,memId:memId,addr:addr},
		success:function(data){
			console.log("新增成功");
			console.log(data);
			var appendEle = $('#home-block');
			if(data ==1){
				appendEle.append("<div class='shipping-home' id='home-block-1'>"+
		    	"<input name='home' type='radio' class='shipping-home-input' value='home-1' checked><span class='p-r-10'>${memberVO.mem_Name}</span><span class='p-r-10'>${memberVO.mem_Phone}</span><span class='p-r-10' id='home-addr-1'>"+addr+"</span>"+
				"<span class='remove-btn' style='cursor: pointer;' id='delete_home1' onclick='deleteHomeByCol(this,\"1\")'>"+
				"<i class='fas fa-trash-alt'></i></span></div>");
			}else if(data ==2){
				appendEle.append("<div class='shipping-home' id='home-block-2'>"+
		    	"<input name='home' type='radio' class='shipping-home-input' value='home-2' checked><span class='p-r-10'>${memberVO.mem_Name}</span><span class='p-r-10'>${memberVO.mem_Phone}</span><span class='p-r-10' id='home-addr-2'>"+addr+"</span>"+
				"<span class='remove-btn' style='cursor: pointer;' id='delete_home2' onclick='deleteHomeByCol(this,\"2\")'>"+
				"<i class='fas fa-trash-alt'></i></span></div>");
			}else if(data ==3){
				appendEle.append("<div class='shipping-home' id='home-block-3'>"+
		    	"<input name='home' type='radio' class='shipping-home-input' value='home-3' checked><span class='p-r-10'>${memberVO.mem_Name}</span><span class='p-r-10'>${memberVO.mem_Phone}</span><span class='p-r-10' id='home-addr-3'>"+addr+"</span>"+
				"<span class='remove-btn' style='cursor: pointer;' id='delete_home3' onclick='deleteHomeByCol(this,\"3\")'>"+
				"<i class='fas fa-trash-alt'></i></span></div>");
			}else{
				alert("數量已達3個，請先刪除，再新增!")
			}
			
			$('#homeDiv').css("display", "none");
		
			var size_home = $('#home-block').find('.shipping-home').length;
			if(size_home>=3){
				$('.add-home-addr').css("display", "none");

			}else{
				$('.add-home-addr').css("display", "block");
			}
		}
	})

});


//宅配地址 刪除
function deleteHomeByCol(e,col){
	var action = "delete_home_addr";
	var memId = "${memId}";
	$.ajax({
		url:"${pageContext.request.contextPath}/front_end/member/member.do",
		method:"POST",
		async: false,
		data:{action:action,memId:memId,col:col},
		success:function(data){
			console.log("刪除成功");
			console.log(data);
			
			if(data == 1){
				$('#home-block-1').remove();
			}else if(data == 2){
				$('#home-block-2').remove();
			}else if(data == 3){
				$('#home-block-3').remove();
			}
			$('.add-home-addr').css("display", "block");
		}
	})

}


</script>


<!-- 地址確認完成 -->
<script type="text/javascript">

$('.confirm-addr').on('click', function(e) {
	var shipmethod = $('input[name=ship]:checked').val();
	var home = $('input[name=home]:checked').val();
	var store = $('input[name=store]:checked').val();

	if(shipmethod == 'home'){
		$('.checkout-shipping-details-selected-method').html('宅配');
		$('#ship-input').val(1);
		if(home == 'home-1'){
			var home_addr = $('#home-addr-1').html();
			console.log(home_addr);	
			$('.checkout-shipping-details-address').html(home_addr);
		
		}else if(home == 'home-2'){
			var home_addr = $('#home-addr-2').html();
			console.log(home_addr);	
			$('.checkout-shipping-details-address').html(home_addr);
		
		}else if(home == 'home-3'){
			var home_addr = $('#home-addr-3').html();
			console.log(home_addr);	
			$('.checkout-shipping-details-address').html(home_addr);
	
		}
		
	}else if(shipmethod == 'store'){
		$('.checkout-shipping-details-selected-method').html('7-11');
		$('#ship-input').val(2);
		if(store == 'store-1'){
			var store_addr = $('#store-addr-1').html();
			var store_name = $('#store-name-1').html();
			console.log(store_name+" "+store_addr);
			$('.checkout-shipping-details-address').html(store_addr);
			$('.checkout-store-name').html(store_name+"店 ");
	
		}else if(store == 'store-2'){
			var store_addr = $('#store-addr-2').html();
			var store_name = $('#store-name-2').html();
			console.log(store_name+" "+store_addr);	
			$('.checkout-shipping-details-address').html(store_addr);
			$('.checkout-store-name').html(store_name+"店 ");
		
		}else if(store == 'store-3'){
			var store_addr = $('#store-addr-3').html();
			var store_name = $('#store-name-3').html();
			console.log(store_name+" "+store_addr);	
			$('.checkout-shipping-details-address').html(store_addr);
			$('.checkout-store-name').html(store_name+"店 "); 
			
		}
	}

})

</script>

<!--===============================================================================================-->
<!-- 塞值進input -->
<script>

$('#place-order').on('click',function(e){
	var shipMethod = $('#checkout-ship-method').html();
	var storeName = $('#checkout-store-name').html();
	var addr = $('#checkout-addr').html();

	console.log(shipMethod);
	console.log(storeName);
	console.log(addr);
	
if(shipMethod == '宅配'){
	$('#ship-input').val(1);
}else{
	$('#ship-input').val(2);
}

	$('#store-input').val(storeName);
	$('#addr-input').val(addr);
	
	console.log($('#ship-input').val());
	console.log($('#store-input').val());
	console.log($('#addr-input').val());
	
})

</script>

<script>
//阻止預設行為
function prDefault(evt){  /*IE*/  
	if($.browser.msie){ 
       evt.keyCode=0;  
       evt.returnValue=false;  
    }else{
    	evt.preventDefault();  
	}  
}  

/*禁止刷新頁面*/  
function noRefresh(event){  
/*F5刷新*/  
	if(event.keyCode==116){  
       prDefault(event);  
       return false ;
   }  
/*ctrl+r or ctrl+n 刷新 */  
	if(event.ctrlKey && (event.keyCode==78||event.keyCode==82)){
       prDefault(event);  
       return false;  
    }  
}  

</script>	

</body>
</html>