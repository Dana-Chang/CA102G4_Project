<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.product.model.*"%>
<%@ page import="com.productWishlist.model.*"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.shopping.model.*"%>

<%
	
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
	
	//若登入狀態為是true
	/***************取出登入者會員資訊******************/
	if( login_state == true){
		String memId = ((MemberVO)session.getAttribute("memberVO")).getMem_Id();
		pageContext.setAttribute("memId",memId);
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
<jsp:useBean id="productWishlistSvc" scope="page"
	class="com.productWishlist.model.ProductWishlistService" />
<jsp:useBean id="productCategorySvc" scope="page"
	class="com.productCategory.model.ProductCategoryService" />
<jsp:useBean id="cart" scope="session"
	class="com.shopping.model.CartBean" />
<%
	CartBean cartBean = (CartBean)session.getAttribute("cart");
	List<CartItem> CartItemList = cartBean.getCartItems();
	List<String> sellerList = new ArrayList<String>();
	for(CartItem c : CartItemList){
		String memId = c.getProduct_mem_id();
		if(!sellerList.contains(memId)) sellerList.add(memId);
	}

	pageContext.setAttribute("sellerList", sellerList);
	
	
	
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
<script
	src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
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
	href="<%=request.getContextPath()%>/front_end/css/store/util.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/main.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/store_cart.css"
	media="all" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/store_header.css"
	media="all" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/front_end/css/store/store_footer.css"
	media="all" />

<!-- //store 自定義的css -->

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
    	$(document).ready(function(){
    		
        	/*若有錯誤訊息時，就會跳出視窗.......*/
      		$("#errorModal").modal('show');
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

 	<%-- 錯誤表列 --%>
	<c:if test="${not empty errorMsgs}">
		<div class="modal fade" id="errorModal">
		    <div class="modal-dialog modal-sm" role="dialog">
		      <div class="modal-content">
		        <div class="modal-header">
		          <i class="fas fa-exclamation-triangle"></i>
		          <span class="modal-title"><h4>請修正以下錯誤:</h4></span>
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
		<!-- cart banner -->
		<section class="overlay bg-title-page p-t-40 p-b-50 flex-col-c-m"
			style="background-image: url('<%=request.getContextPath()%>/front_end/images/store/header.jpg');opacity: 0.8;filter: grayscale(40%);">
		<h2 class="l-text2 t-center">購物車</h2>
		</section>
		<!-- //cart banner -->
		<div class="container">
			<div class="row p-t-100 p-b-50">
				<c:if test="${cart.lineItemCount==0}">
					<div class="col-sm-12 col-md-10 col-md-offset-1" style="text-align: center">
						<font size="5" face="Verdana, Arial, Helvetica, sans-serif">-
							目前購物車尚無商品 -</font><br />
					</div>
				</c:if>
				<c:if test="${cart.lineItemCount!=0}">
					<div class="col-sm-12 col-md-10 col-md-offset-1">
						<!-- 購物車最上方表頭 -->
						<form action="shopping.do" method="post">
						<div>
							<table class="table table-hover">
								<thead>
									<tr>
										<th class="col-sm-1 col-md-1"> </th>
										<th class="col-sm-6 col-md-4">商品</th>
										<th class="col-sm-1 col-md-1">數量</th>
										<th class="col-sm-1 col-md-1 text-center">單價</th>
										<th class="col-sm-1 col-md-1 text-center">總計</th>
										<th class="col-sm-1 col-md-1 text-center"> </th>
									</tr>
								</thead>
							</table>
						</div>
						<!-- //購物車最上方表頭 end -->
						
						<c:forEach var="sellerId" items="${sellerList}">
						<!-- 一個賣家的購物車商品table -->
						
							<div class="p-b-50">
								<!-- 賣家名稱 -->
								<div class="cart-store-header">
									<i class="fas fa-store-alt p-l-20"></i><a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_public_sell.jsp?uId=${sellerId}"
										class="s-text3 p-l-10">${memSvc.getOneMember(sellerId).mem_Name}</a>
								</div>
								<!-- //賣家名稱 end -->
								<table class="table table-hover p-t-100">
									<tbody>
								<c:forEach var="cartItem" items="${cart.cartItems}" varStatus="counter">
								 <c:if test="${cartItem.product_mem_id eq sellerId }">
										<tr>
											<td class="col-sm-1 col-md-1">
												<input type="checkbox" name="productId" class="order-list-check form-check-input check-${cartItem.product_id}" value="${cartItem.product_id}" onchange="checkOrder(this,${cartItem.product_id})">
												<input type="checkbox" name="productQty" class="check-${cartItem.product_id}" value="${cartItem.quantity}" id="check-qty-${cartItem.product_id}" style="display:none">
												<input type="checkbox" name="productName" class="check-${cartItem.product_id}" value="${cartItem.product_name}"  style="display:none">
												<input type="checkbox" name="productMemId" class="check-${cartItem.product_id}" value="${cartItem.product_mem_id}"  style="display:none">
												<input type="checkbox" name="productPrice" class="check-${cartItem.product_id}" value="${cartItem.product_price}"  style="display:none">
												<input type="checkbox" name="productTotalPrice" class="check-${cartItem.product_id}" value="${cartItem.total_price}" id="check-total-${cartItem.product_id}" style="display:none">
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
											<td class="prod_total_qty col-sm-1 col-md-1" style="text-align: center">
												<input name="quantity" type="number" class="form-control"
												onchange="updateById(this, ${cartItem.product_id},${productSvc.getOneProduct(cartItem.product_id).product_stock},${cartItem.quantity})" step="1" min="1"
												max="${productSvc.getOneProduct(cartItem.product_id).product_stock}"
												value="${cartItem.quantity}"
												id="qty_${cartItem.product_id}">
											</td>
											<td class="col-sm-1 col-md-1 text-center">$
												<span id="prod_price_${cartItem.product_id}">${cartItem.product_price}</span>
											</td>
											<td class="prod_total_price col-sm-1 col-md-1 text-center">$
												<span id="total_price_${cartItem.product_id}">${cartItem.total_price}</span>
											</td>
											<td class="col-sm-1 col-md-1">
												<button type="button" class="btn remove-btn"
													value="DELETE" onclick="deleteById(this, ${cartItem.product_id})">
													<i class="fas fa-trash-alt"></i>
												</button>
											</td>
										</tr>
									
									<!-- 一個賣家的購物車商品table end -->
									</c:if>
								</c:forEach>
						  		 </tbody>
							</table>
						</div>
						</c:forEach>
						<input type="hidden" name="action" value="CHECKOUT"/>
						<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
							<div>
								<div class="p-b-50">
									<div class="cart-total" style="float: right; display: block;">
										購買總金額 (<span id="order-qty">0</span>個商品):$<span id="order-total">0</span>
									</div>
								</div>
								<div
									class="box-footer d-flex justify-content-between align-items-center"
									style="display: block">
									<div class="left-col">
										<a href="shop-category.html" class="btn btn-secondary mt-0">
											<i class="fa fa-chevron-left"></i> 繼續購買
										</a>
									</div>
									<div class="right-col">
										<a href="<%=request.getContextPath()%>/front_end/store/store_cart.jsp"><button class="btn btn-secondary">
											<i class="fas fa-sync-alt"></i> 刷新購物車
										</button></a>
										<button type="submit" class="btn btn-template-outlined">
											去買單 <i class="fa fa-chevron-right"></i>
										</button>
									</div>
								</div>
							</div>
						</form>
					</div>
				</c:if>
			</div>
		</div>
	</div>
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
<!-- 更新購物車商品數量 -->
<script>
		function updateById(e, prod_id,max,originQty){
			var action = "UPDATE";
			var quantity = parseInt($(e).val());
			var price = $(e).parent().next().find("span").eq(0).html();
			var updateEle = $(e).parent().next().next().find("span").eq(0);
			var updatecheckEle = $(e).parent().parent().find("td").eq(0).find(".order-list-check").eq(0);

			if(quantity>max){
				window.alert("數量大於庫存! 庫存數量為"+max);
				$(e).val(originQty);
			}else if(quantity <= 0){
				window.alert("商品數量最少 1 個，若要刪除請點右側刪除符號");
				$(e).val(originQty);
			}else{
				$.ajax({
					url:"shopping.do",
					method:"POST",
					data:{action:action,itemIndex:prod_id,quantity:quantity},
					success:function(data){
						alert("數量更新成功!");
						updateEle.html(quantity*price);	
						$('#check-qty-'+prod_id).val(quantity);
						$('#check-total-'+prod_id).val(quantity*price);
						$('.badge').text(data);
					}
				})
			}
		}

</script>

<!--===============================================================================================-->
	<!-- 刪除購物車商品 -->
	
<script type="text/javascript">
	function deleteById(e, prod_id){
		var action = "DELETE";
		var removeEle = $(e).parent().parent();
		
		$.ajax({
			url:"shopping.do",
			method:"POST",
			async: false,
			data:{action:action,itemIndex:prod_id},
			success:function(data){
				console.log(removeEle.html());
					if($(removeEle).parent().parent().find("tr").eq(0).is(':last-child'))
					{  
						removeEle.parent().parent().parent().remove();
					}else{
						removeEle.remove();
	 				}
					$('.badge').text(data);
			}
		})
	}
</script>
	
	
<!--===============================================================================================-->
<!-- 勾選要結帳的商品 -->
<script>
	  	function checkOrder(e,prodId){
	  		var checkEle = $(".check-"+prodId);
	  		 if($(e).is(':checked')){
	  			 $(".check-"+prodId).prop("checked",true);
	  			var checkedValue = $(".check-"+prodId).is(':checked');
	  			console.log(checkedValue);
	 		}else{
 				console.log($(e).is(':checked') == true);
 				checkEle.prop("checked",false);
	 		}
	  	}
	  	
</script>	
<!--===============================================================================================-->
<!-- 勾選商品金額顯示 -->
<script>
$(document).ready(function()
		{
		    setInterval( function() 
		    {

		    	var order_total = 0;
		    	var qty_total = 0;
		    	
		    	$('input.order-list-check:checkbox:checked').each(function () {
		    	    var prodId = $(this).val();
		    	    var prod_total = parseInt($('#total_price_'+prodId).html());
		    	    var prod_qty = parseInt($('#qty_'+prodId).val());
		    	    order_total = order_total+prod_total;
		    	    qty_total = qty_total+prod_qty;
		    	});
	
		    	$("#order-total").html(order_total);
	    		$("#order-qty").html(qty_total);
		    }, 500);
});


</script>

</body>
</html>