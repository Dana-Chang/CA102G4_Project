<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.product.model.*"%>
<%@ page import="com.productWishlist.model.*"%>
<%@ page import="com.productCategory.model.*"%>
<%@ page import="com.mem.model.*" %>
<%
	String memId = request.getParameter("mem_Id");
	pageContext.setAttribute("memId",memId);
	
	ProductVO  productVO = ( ProductVO) request.getAttribute("productVO");
	
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
	
	 UUID uuid = UUID.randomUUID();
	 session.setAttribute("token",uuid.toString());
%>

<jsp:useBean id="productCategorySvc" scope="page" class="com.productCategory.model.ProductCategoryService" />

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
	
		<!--add product 自定義的css -->

		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/store_header.css" media="all" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/store_footer.css" media="all" />
	    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/add_prod.css" media="all" />
	    <!-- //add product 自定義的css -->

	
	    <!-- add product 自定義的js -->
 	    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
 	    <script src="<%=request.getContextPath()%>/front_end/store_ckeditor/ckeditor.js"></script>
	    <!-- //add product 自定義的js -->
	    
	    <style>
	    .img-thumbnail {
	    	width: 300px; 
	    	height:200px;
	    }
	    
		<!-- 聊天相關CSS及JS -->
	    <link href="<%=request.getContextPath()%>/front_end/css/chat/chat_style.css" rel="stylesheet" type="text/css">
	    <script src="<%=request.getContextPath()%>/front_end/js/chat/vjUI_fileUpload.js"></script>
	    <script src="<%=request.getContextPath()%>/front_end/js/chat/chat.js"></script>
	    <%@ include file="/front_end/personal_area/chatModal_JS.file" %>
	    <!-- //聊天相關CSS及JS -->
			
	    </style>
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
							<li><a href="<%= request.getContextPath()%>/front_end/member/member.do?action=logout"><span class=" top_banner"><i class=" fas fa-sign-out-alt" aria-hidden="true"></i></span></a></li>
							<li><a class="top_banner" href="<%=request.getContextPath()%>/front_end/personal_area_home.html"><i class="fa fa-user" aria-hidden="true"></i></a></li>
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
		<!-- 新增商品 -->
		<div class="container" style="margin-top: 50px;">
			<div class="col-md-12">
			  <form name = 'form' action ='product.do' method='post' enctype='multipart/form-data'>
			  	<div>
			  		商品名稱
			  	</div>
			  	<input type="text" name="product_name" size="20" maxlength="30" id="product-name" placeholder="請輸入商品名稱" value="${param.product_name}"/><span style="color:red;padding-left:10px;">${errorMsgs.product_name}</span>
			  	<!--商品照片-->
			  	
			  	
				<div class="row">
			        <div class="col-md-12 col-lg-12 ">
			       			<div style="color:red;padding-bottom:10px;">${errorMsgs.product_photo}</div>
			            	<div class="col-md-6 col-lg-4" style="padding-bottom:20px">
							商品照片1:
							<input type="FILE" accept="image/*" id="product_photo_1" name="product_photo_1" value="<%=(productVO==null)? null:productVO.getProduct_photo_1_base()%>" />
							<c:set var="imgData1" scope="page" value="${productVO.product_photo_1_base}"/>
							<c:if test="${not empty imgData1}">
								<img id="preview-product_photo_1" src="data:image/jpeg;base64,${imgData1}" height="200px" width="300px" class="img-thumbnail"/>
							</c:if>
							<c:if test="${empty imgData1}">
								<img id="preview-product_photo_1" src="<%=request.getContextPath()%>/front_end/images/store/no-image-icon-15.png" height="200px" width="300px" class="img-thumbnail"/>
							</c:if>
							</div>
							<div class="col-md-6 col-lg-4" style="padding-bottom:20px">
							商品照片2:
							<input type="FILE" accept="image/*" id="product_photo_2" name="product_photo_2" value="<%=(productVO==null)? null:productVO.getProduct_photo_2_base()%>" />
							<c:set var="imgData2" scope="page" value="${productVO.product_photo_2_base}"/>
							<c:if test="${not empty imgData2}"> <img id="preview-product_photo_2" src="data:image/jpeg;base64,${imgData2}" height="200px" width="300px" class="img-thumbnail"/></c:if><c:if test="${empty imgData2}"><img id="preview-product_photo_2" src="<%=request.getContextPath()%>/front_end/images/store/no-image-icon-15.png" height="200px" width="300px" class="img-thumbnail"/></c:if>
							</div>
							<div class="col-md-6 col-lg-4" style="padding-bottom:20px">
							商品照片3:
							<input type="FILE" accept="image/*" id="product_photo_3" name="product_photo_3" value="<%=(productVO==null)? null:productVO.getProduct_photo_3_base()%>" />
							<c:set var="imgData3" scope="page" value="${productVO.product_photo_3_base}"/>
							<c:if test="${not empty imgData3}"> <img id="preview-product_photo_3" src="data:image/jpeg;base64,${imgData3}" height="200px" width="300px" class="img-thumbnail"/></c:if><c:if test="${empty imgData3}"><img id="preview-product_photo_3" src="<%=request.getContextPath()%>/front_end/images/store/no-image-icon-15.png" height="200px" width="300px" class="img-thumbnail"/></c:if>
							</div>
							<div class="col-md-6 col-lg-4" style="padding-bottom:20px">
							商品照片4:
							<input type="FILE" accept="image/*" id="product_photo_4" name="product_photo_4" value="<%=(productVO==null)? null:productVO.getProduct_photo_4_base()%>"/>
							<c:set var="imgData4" scope="page" value="${productVO.product_photo_4_base}"/>
							<c:if test="${not empty imgData4}"> <img id="preview-product_photo_4" src="data:image/jpeg;base64,${imgData4}" height="200px" width="300px" class="img-thumbnail"/></c:if><c:if test="${empty imgData4}"><img id="preview-product_photo_4" src="<%=request.getContextPath()%>/front_end/images/store/no-image-icon-15.png" height="200px" width="300px" class="img-thumbnail"/></c:if>
							</div>
							<div class="col-md-6 col-lg-4" style="padding-bottom:20px">
							商品照片5:
							<input type="FILE" accept="image/*" id="product_photo_5" name="product_photo_5" value="<%=(productVO==null)? null:productVO.getProduct_photo_5_base()%>" />
							<c:set var="imgData5" scope="page" value="${productVO.product_photo_5_base}"/>
							<c:if test="${not empty imgData5}"> <img id="preview-product_photo_5" src="data:image/jpeg;base64,${imgData5}" height="200px" width="300px" class="img-thumbnail"/></c:if><c:if test="${empty imgData5}"><img id="preview-product_photo_5" src="<%=request.getContextPath()%>/front_end/images/store/no-image-icon-15.png" height="200px" width="300px" class="img-thumbnail"/></c:if>
			      			</div>
			        </div>
				</div>	
			  	<!--商品照片上傳-->
			  	<br>商品價格<input min="1" style="width: 120px;margin-left: 10px;" type="number" name="product_price" id="product-price" placeholder="請輸入商品價格" value="<c:if test="${not empty productVO}"><%= productVO.getProduct_price()%></c:if>"/><span style="color:red;padding-left:10px;">${errorMsgs.product_price}</span>
			  	<br>商品庫存<input min="1" style="width: 120px;margin-left: 10px;" type="number" name="product_stock" id="product-unit" placeholder="請輸入商品庫存" value="<c:if test="${not empty productVO}"><%= productVO.getProduct_stock()%></c:if>" /><span style="color:red;padding-left:10px;">${errorMsgs.product_stock}</span><br>
			  		<div style="display:inline-block; margin-right: 10px;">請選擇商品分類</div>
				  		<select size="1" name="product_category_id" class="selectpicker" id = "product-category">
				         	<c:forEach var="productCategoryVO" items="${productCategorySvc.all}" > 
				          		<option value="${productCategoryVO.product_category_id}" ${(productVO.product_category_id==productCategoryVO.product_category_id)?'selected':''}>${productCategoryVO.product_category_name}
				         	</c:forEach>   
				        </select> 
				<br>
				<div style="padding-bottom:10px">
			  		請輸入商品描述 <span style="color:red;padding-left:10px;">${errorMsgs.product_descr}</span>
			  	</div>
	            <textarea name="product_descr" id="product_descr" rows="10" cols="80"><c:if test="${not empty productVO}"><%= productVO.getProduct_descr()%></c:if></textarea>
		            <script>
						CKEDITOR.replace( 'product_descr', {  width: 1100,height: 500,});
					</script>
	            <div style = "height: 100px; margin-top: 20px;">
	            <input type="hidden" name="judgeDuplicate" size="30" value="${token}" />
	               <input type="hidden" name="product_date" size="30" value="<%= new java.sql.Timestamp(System.currentTimeMillis()) %>" />
	               <input type="hidden" name="product_status" size="30" value="1" />
	               <input type="hidden" name="product_mem_id" size="30" value="<%=(productVO==null)? memId:productVO.getProduct_mem_id()%>" />
	               <input type="hidden" name="product_photo_1_base" value="<%=(productVO==null)? null:productVO.getProduct_photo_1_base()%>" />
					<input type="hidden" name="product_photo_2_base" value="<%=(productVO==null)? null:productVO.getProduct_photo_2_base()%>" />
					<input type="hidden" name="product_photo_3_base" value="<%=(productVO==null)? null:productVO.getProduct_photo_3_base()%>" />
					<input type="hidden" name="product_photo_4_base" value="<%=(productVO==null)? null:productVO.getProduct_photo_4_base()%>" />
					<input type="hidden" name="product_photo_5_base" value="<%=(productVO==null)? null:productVO.getProduct_photo_5_base()%>" />
		         	<input type="hidden" name="action" value="insert">
		         	<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
	         	   <input id = "submit-product-add" type = 'submit' class = "btn pull-right" value = '上架'>
	            </div>
	          </form>
           </div>
        </div>
		<!-- //新增商品 -->
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
<!-- 照片預覽 -->
<script>
function readURL(input) {

	  if (input.files && input.files[0]) {
		    var reader = new FileReader();
		
		    reader.onload = function(e) {
		    	 imgId = '#preview-'+$(input).attr('id');
	             $(imgId).attr('src', e.target.result);
		    }
		
		    reader.readAsDataURL(input.files[0]);
	  }
}

$("input[type='file']").change(function() {
  readURL(this);
});

</script>
</body>
</html>