<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="javax.servlet.http.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.fri.model.*" %>
<%@ page import="com.mem.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.product.model.*"%>
<%@ page import="com.productWishlist.model.*"%>
<%@ page import="com.ord.model.*"%>
<%@ page import="com.orderDetails.model.*"%>
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
		
	/***************取出登入者會員資訊******************/
	String memId = ((MemberVO)session.getAttribute("memberVO")).getMem_Id();
	
	//為了join(寫法有servlet3.0限制)
	MemberService memSvc = new MemberService();
	pageContext.setAttribute("memSvc",memSvc); 
	
	
	/***************取出登入會員銷售商品******************/
	ProductService productSvc = new ProductService();
    Set<ProductVO> list = productSvc.getSellerProducts(memId);
    pageContext.setAttribute("list",list);
    
    
  //取得購物車商品數量
  	Object total_items_temp = session.getAttribute("total_items");
  	int total_items = 0;
  	if(total_items_temp != null ){
  		total_items= (Integer) total_items_temp;
  	}
  	pageContext.setAttribute("total_items",total_items);
  	
    //取得賣家購買清單
  	OrdService ordSvc = new OrdService();

	List<OrdVO> sellList = ordSvc.getForAllSell(memId);
	
	//待出貨賣家訂單
    List<OrdVO> PSIList = new ArrayList<OrdVO>();
	
	//待收貨賣家訂單 Pending receipt
	List<OrdVO> PRList = new ArrayList<OrdVO>();
	
	//已完成 賣家訂單 complete
	List<OrdVO> COMList = new ArrayList<OrdVO>();

	//取消  賣家訂單 cancel
	List<OrdVO> CLList = new ArrayList<OrdVO>();
	
	//有買家評價的  賣家訂單
	List<OrdVO> ratingList = new ArrayList<OrdVO>();
	
    for(int i = 0 ;i<sellList.size();i++){
    	if(sellList.get(i).getBtos_rating()!=0){
    		ratingList.add(sellList.get(i));
    	}
    	
    	if(sellList.get(i).getShipment_status()==1 && sellList.get(i).getOrder_status()==1){
    		PSIList.add(sellList.get(i));
    	}else if((sellList.get(i).getShipment_status()==2 || sellList.get(i).getShipment_status()== 3 )&& sellList.get(i).getOrder_status()==2){
    		PRList.add(sellList.get(i));
    	}else if(sellList.get(i).getOrder_status()==3){
    		COMList.add(sellList.get(i));
    	}else if(sellList.get(i).getOrder_status()==4){
    		CLList.add(sellList.get(i));
    	}
    }
    pageContext.setAttribute("ordSvc",ordSvc); 
    pageContext.setAttribute("PSIList",PSIList); 
    pageContext.setAttribute("PRList",PRList); 
    pageContext.setAttribute("COMList",COMList); 
    pageContext.setAttribute("CLList",CLList); 
    pageContext.setAttribute("ratingList",ratingList);
    ProductService prodSvc = new ProductService();
    pageContext.setAttribute("prodSvc",prodSvc); 
    
    OrderDetailsService ordDetailsSvc = new OrderDetailsService();
    pageContext.setAttribute("ordDetailsSvc",ordDetailsSvc); 
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
    <link href="<%=request.getContextPath()%>/front_end/css/ad/ad_semantic.min.css" rel="stylesheet" type="text/css">
    <!-- //semantic css -->
    
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
    
    <!-- AD_Page相關CSS及JS -->
    <link href="<%=request.getContextPath()%>/front_end/css/ad/ad_page.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/personal/personal_area_home.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/personal/personal_area_fri.css" rel="stylesheet" type="text/css">
    <!-- //AD_Page相關CSS及JS -->
    
    <!-- 賣場相關CSS及JS -->
   	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/util.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/main.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/seller_prod_mgt.css">
   
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/store_tab.css">	   
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/front_end/css/store/store_order_mgt.css">	   
    <!-- //賣場相關CSS及JS -->
    
     <!-- LogoIcon -->
    <link href="<%=request.getContextPath()%>/front_end/images/all/Logo_Black_use.png" rel="icon" type="image/png">
    <!-- //LogoIcon -->
   
   <style>
	   a:hover, a:focus {
		    color: #aaa;
		    text-decoration: none;
		}
	  .wish-add-btn{
	  		color: #111;
	  }
	  
	  .mem_ind_name > p {
    	 line-height: 1.4285em;
    	 color: #333;
    	 font-family: 'Oswald','Noto Sans TC', sans-serif !important;
	  }
	  
	  .mem_ind_topbar {
		   height: 0;
		}
		.nav-tabs {
		    margin-bottom: 1em;
		}
		 #rating-area .selected {
		    color: #ffcc00 !important;
		}
   </style>
    <script>
    	$(document).ready(function(){
    		
        	/*若有錯誤訊息時，就會跳出視窗.......*/
      		$('#errorModal').modal();
    	});

    </script>
    
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
    
    <!--container-->
    <div class="ui container">
        <!--會員個人頁面標頭-->
        <div class="mem_ind_topbar">
            <!--會員封面-->
            <div class="mem_ind_banner">
                <img src="<%=request.getContextPath()%>/front_end/images/all/person_bar.jpg">
            </div>
            <!--會員訊息--> 
            <div class="mem_ind_info"> 
                <div class="mem_ind_img">
                    <img src="<%=request.getContextPath()%>/front_end/readPic?action=member&id=${memberVO.mem_Id}">
                </div>
                <div class="mem_ind_name">
                    <p>${memberVO.mem_Name}</p>
                    <p class="text-truncate" style="font-size:0.9em;padding-top:10px;max-height:110px">
					   ${memberVO.mem_Profile}
                    </p>
                </div>
            </div> 
        </div>
        <!--會員個人頁面-首頁內容-->
        <div class="mem_ind_content">
          <!-- 頁籤項目 -->
          <ul class="nav nav-tabs" role="tablist">
            <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_home.jsp">
                  <i class="fas fa-home"></i>首頁
              </a>
            </li>
            <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_friend.html">
                  <i class="fas fa-user-friends"></i>好友
              </a>
            </li>
            <li class="nav-item">
              <a href="personal_area_blog.html">
                  <i class="fab fa-blogger"></i>旅遊記
              </a>
            </li>
            <li class="nav-item">
              <a href="#trip">
                  <i class="fas fa-map"></i>行程
              </a>
            </li>
            <li class="nav-item">
              <a href="#together">
                  <i class="fas fa-bullhorn"></i>揪團
              </a>
            </li>
            <li class="nav-item">
              <a href="#question">
                  <i class="question circle icon"></i>問答
              </a>
            </li>
            <li class="nav-item">
              <a href="#gallery">
                  <i class="image icon"></i>相片
              </a>
            </li>
            
             <li class="nav-item active">
              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_sell.jsp">
                  <i class="money bill alternate icon"></i>銷售
              </a>
            </li>

             <li class="nav-item">
              <a href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_buy.jsp">
                  <i class="shopping cart icon"></i>購買
              </a>
            </li>

            <li class="nav-item" style="float: right">
              <a href="<%=request.getContextPath()%>/front_end/member/member.do?action=getOne_For_Display&mem_Id=${memberVO.mem_Id}">
                  <i class="cog icon"></i>設置
              </a>
            </li>
          </ul>
          <!-- //頁籤項目 -->
          <!-- 頁籤項目-銷售管理內容 -->
          <div class="tab-content" style="float:left;width:75%">
            <!--首頁左半邊-銷售管理-->
            <div id="#" class="container tab-pane active">
                <div class="u_title">
                    <strong>我的銷售</strong>
                </div>
                <br>
                <div id="sell_management">
                   <div style="width: 70%;float: left">
                       <ul class="nav nav-tabs" id="sell_tab">
                          <li class="active"><a data-toggle="tab" href="#store_mgt">賣場管理</a></li>
                          <li><a data-toggle="tab" href="#orderlist_sell">銷售清單</a></li>
                          <li><a data-toggle="tab" href="#rating_sell">銷售評價</a></li>
                        </ul>
                   </div>
                </div>
                
                <div class="tab-content">
                  <!--管理賣場-->
                  <div id="store_mgt" class="tab-pane fade in active"> 
                   <!--內容-->  
                    <div class="main" style="min-height: 1px;">
                    	<section class="bgwhite p-t-55 p-b-65">
							<div class="container">
								<div class="row">
							<div class="col-sm-6 col-md-8 col-lg-11 p-b-50">
								<div class="row">
									<div class="col-sm-10 col-md-10 col-lg-12">
										<%@ include file="page1.file" %>
										<div class="page-top flex-sb-m flex-w p-b-35 p-t-40" style="display: inline-block;">
											<span class="s-text8 p-t-5 p-b-5">
												第<%=whichPage%>/<%=pageNumber%>頁  共<%=rowNumber%>筆
											</span>
										</div>
										<div style="text-align: right;display: inline-block;float:right">
											<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/front_end/store/store_add_product.jsp">
													<input type="hidden" name="mem_Id"  value="${memberVO.mem_Id}">
													<button type="submit" style="border: 0; background: transparent">
													    <img id="add-product" src="<%=request.getContextPath()%>/front_end/images/store/add_circle_grey_96x96.png" width="80" height="80" alt="submit" onmouseover="this.src='<%=request.getContextPath()%>/front_end/images/store/add_circle_black_96x96.png'" onmouseout="this.src='<%=request.getContextPath()%>/front_end/images/store/add_circle_grey_96x96.png'" />
													</button>
											</FORM>
										</div>
									</div>
								</div>
								<!-- Product -->
								<jsp:useBean id="productWishlistSvc" scope="page" class="com.productWishlist.model.ProductWishlistService" />
								<div class="row">
								<c:forEach var="productVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
										<div class="col-sm-12 col-md-6 col-lg-4 p-b-50" id="block-${productVO.product_id}">
											<!-- Block2 -->
											<div class="block2">
												<div class="block2-img wrap-pic-w of-hidden pos-relative ${productVO.product_status == 2? 'block2-labelnew' : '' } ${productVO.product_status == 3? 'block2-labelsale' : '' }" style="height:16rem;width: 100%;">
													<img class="prod-img" src="data:image/jpeg;base64,${productVO.product_photo_1_base}" onerror="this.src='<%=request.getContextPath()%>/front_end/images/store/no-image-icon-15.png'"  alt="IMG-PRODUCT">
													<div class="block2-overlay trans-0-4" style="width: 100%;">
														<div class="block2-btn-addcart w-size1 trans-0-4">
															<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/front_end/store/product.do">
																 <input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
															     <input type="hidden" name="product_id"  value="${productVO.product_id}">
															     <input type="hidden" name="action" value="getOne_For_Update">
																<input type="hidden" name="whichPage"  value="<%=request.getParameter("whichPage")%>"> <!--送出當前是第幾頁給Controller-->
																<button type="submit" class="edit-prod-btn flex-c-m size1 bg4 hov7 s-text1 trans-0-4" id="edit-${productVO.product_id}">
																	<i class="fas fa-pencil-alt p-r-5"></i>編輯商品
																</button>
															</FORM>			
														     <button type="button" onclick="deleteById(this, ${productVO.product_id})" class="delete-prod-btn flex-c-m size1 bg4 hov1 s-text1 trans-0-4 m-t-10" id="delete-${productVO.product_id}">
																<i class="fas fa-trash-alt p-r-5"></i>刪除商品
															 </button>
														</div>
													</div>
												</div>
				
												<div class="block2-txt p-t-20">
													<a href="<%=request.getContextPath()%>/front_end/store/store_product.jsp?prod_id=${productVO.product_id}" class="block2-name dis-block s-text3 p-b-5 prod-title">
														${productVO.product_name}
													</a>
													<div class="p-t-10">
														<span class="wish-add m-text6 p-r-5 p-l-5" style="float: lefft;">
															<span class="wish-add-btn">
															<i class="far fa-heart" aria-hidden="true"></i>
															<i class="fas fa-heart dis-none" aria-hidden="true"></i></span>
														</span>
														<span class="wish-like-text m-text6 p-r-5" style="float: lefft;">
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
								
								<div class="row">
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
                  <!--//管理賣場-->
                  
                  <!--銷售清單列表-->
                  <div id="orderlist_sell" class="tab-pane fade">
                   <div class="container">
						<div class="row">	
							<div class="col-md-10 col-lg-10"  style="padding-top:20px;padding-bottom: 100px;">
								<div class="tabbable-panel">
									<div class="tabbable-line">
									<ul class="nav nav-tabs ">
											<li class="active">
												<a href="#tab_1" data-toggle="tab">
													待出貨 </a>
											</li>
											<li>
												<a href="#tab_2" data-toggle="tab">
													待收貨 </a>
											</li>
											<li>
												<a href="#tab_3" data-toggle="tab">
													完成 </a>
											</li>
											<li>
												<a href="#tab_4" data-toggle="tab">
													取消</a>
											</li>
										</ul>
										<div class="tab-content">
										<!-- 待出貨  tab1-->
											<div class="tab-pane active" id="tab_1">
												<c:forEach var="ordVO" items="${PSIList}">
												<div id="ord_block_${ordVO.order_id}">
													<div class="host">
														<a href="#" target="_blank" class="photo" style="background-image: url(<%=request.getContextPath()%>/front_end/readPic?action=member&id=${ordVO.seller_mem_id})">
														</a>
														<span class="#" style="display:inline-block">	
															<a href="#" target="_blank" style="display:inline-block">${memSvc.getOneMember(ordVO.seller_mem_id).mem_Name}</a>	
														</span>
													</div>
													<div class="text order_id">訂單編號： ${ordVO.order_id}</div>
													<hr class="divider-w pt-20">
													<c:forEach var="ordDetailsVO" items="${ordDetailsSvc.getOrderDetialsByOrdId(ordVO.order_id)}">
														<div class="order-content">
															<div class="order-content-product">
																<div class="order-content-img" style="display:inline-block">
																	<div class="prod-img-wrap">
																		<div class="prod-img-content" style="background-image: url(data:image/jpeg;base64,${prodSvc.getOneProduct(ordDetailsVO.details_product_id).product_photo_1_base})">
																		</div>
																	</div>
																</div>
																<div class="order-content-detail" style="display:inline-block">
																	<div class="order-content-prod-name">${prodSvc.getOneProduct(ordDetailsVO.details_product_id).product_name}</div>
																	<div class="order-content-prod-qty">x ${ordDetailsVO.details_order_qty}</div>
																</div>
																<div class="order-content-prod-price" style="display:inline-block;float:right">
																		<div class="order-content-prod-price-text" style="display:inline-block">
																			$ ${ordDetailsVO.details_order_total}
																		</div>
																</div>
															</div>
														</div>
													</c:forEach>
													<hr class="divider-w pt-20">
													<div class="order-list-review" align="right">
														<div class="order-list-review-title" style="display:inline-block">訂單金額 (${ordVO.order_item} 商品):</div>
														<div class="order-list-review-price" style="display:inline-block">$ ${ordVO.order_total}</div>
													</div>
													<p class="form-submit p-b-20">
														<button type="button" class="btn cancel-order" data-id="${ordVO.order_id}" data-toggle="modal" data-target="#cancelOrderModal">取消訂單</button> 
														<button type="button" class="btn ship-order" id="ship-order-${ordVO.order_id}" data-toggle="modal" data-target="#shipOrderModal" onclick="confirmShip('${ordVO.order_id}')">確認出貨</button>
													</p>
													<hr class="divider-w p-t-10" style="border-color:#313438">
													</div>
												</c:forEach>
											</div>
										<!-- 待出貨  tab1 END-->	
										
										<!-- 待收貨  tab2 -->	
											<div class="tab-pane" id="tab_2">
											<c:forEach var="ordVO" items="${PRList}">
											<div class="host">
												<a href="agnes" target="_blank" class="photo" style="background-image: url(<%=request.getContextPath()%>/front_end/readPic?action=member&id=${ordVO.seller_mem_id})">
												</a>
												<span class="text" style="display:inline-block">	
													<a href="agnes" target="_blank" style="display:inline-block">${memSvc.getOneMember(ordVO.seller_mem_id).mem_Name}</a>	
												</span>
											</div>
												<div class="text order_id">訂單編號： ${ordVO.order_id}</div>
												<hr class="divider-w pt-20">
												<c:forEach var="ordDetailsVO" items="${ordDetailsSvc.getOrderDetialsByOrdId(ordVO.order_id)}">	
													<div class="order-content">
														<div class="order-content-product">
															<div class="order-content-img" style="display:inline-block">
																<div class="prod-img-wrap">
																	<div class="prod-img-content" style="background-image: url(data:image/jpeg;base64,${prodSvc.getOneProduct(ordDetailsVO.details_product_id).product_photo_1_base})">
																	</div>
																</div>
															</div>
															<div class="order-content-detail" style="display:inline-block">
																<div class="order-content-prod-name">${prodSvc.getOneProduct(ordDetailsVO.details_product_id).product_name}</div>
																<div class="order-content-prod-qty">x ${ordDetailsVO.details_order_qty}</div>
															</div>
															<div class="order-content-prod-price" style="display:inline-block;float:right">
																<div class="order-content-prod-price-text" style="display:inline-block">
																	$ ${ordDetailsVO.details_order_total}
																</div>
															</div>
														</div>
													</div>
												</c:forEach>
												<hr class="divider-w pt-20">
												<div class="order-list-review" align="right">
													<div class="order-list-review-title" style="display:inline-block">訂單金額 (${ordVO.order_item} 商品):</div>
													<div class="order-list-review-price" style="display:inline-block">$ ${ordVO.order_total}</div>
												</div>
												<p class="form-submit p-b-20">  
													<button type="button" class="btn confirm-delivery" id="confirm-delivery" data-toggle="modal" data-target="#confirmDeliveryModal" onclick="confirmDelivery('${ordVO.order_id}')">確認到貨</button>
												</p>
												<hr class="divider-w p-t-10" style="border-color:#313438">
												</c:forEach>
											</div>
										<!-- 待收貨  tab2 END-->	
										
										
										<!-- 已完成  tab3 -->	
											<div class="tab-pane" id="tab_3">
											<c:forEach var="ordVO" items="${COMList}">
												<div class="host">
													<a href="#" target="_blank" class="photo" style="background-image: url(<%=request.getContextPath()%>/front_end/readPic?action=member&id=${ordVO.seller_mem_id})">
													</a>
													<span class="text" style="display:inline-block">	
														<a href="#" target="_blank" style="display:inline-block">${memSvc.getOneMember(ordVO.seller_mem_id).mem_Name}</a>	
													</span>
												</div>
													<div class="text order_id">訂單編號： ${ordVO.order_id}</div>
													<hr class="divider-w pt-20">
													<c:forEach var="ordDetailsVO" items="${ordDetailsSvc.getOrderDetialsByOrdId(ordVO.order_id)}">	
														<div class="order-content">
															<div class="order-content-product">
																<div class="order-content-img" style="display:inline-block">
																	<div class="prod-img-wrap">
																		<div class="prod-img-content" style="background-image: url(data:image/jpeg;base64,${prodSvc.getOneProduct(ordDetailsVO.details_product_id).product_photo_1_base})">
																		</div>
																	</div>
																</div>
																<div class="order-content-detail" style="display:inline-block">
																	<div class="order-content-prod-name">${prodSvc.getOneProduct(ordDetailsVO.details_product_id).product_name}</div>
																	<div class="order-content-prod-qty">x ${ordDetailsVO.details_order_qty}</div>
																</div>
																<div class="order-content-prod-price" style="display:inline-block;float:right">
																	<div class="order-content-prod-price-text" style="display:inline-block">
																		$ ${ordDetailsVO.details_order_total}
																	</div>
																</div>
															</div>
														</div>
													</c:forEach>
													<hr class="divider-w pt-20">
													<div class="order-list-review" align="right">
														<div class="order-list-review-title" style="display:inline-block">訂單金額 (${ordVO.order_item} 商品):</div>
														<div class="order-list-review-price" style="display:inline-block">$ ${ordVO.order_total}</div>
													</div>
													<p class="form-submit p-b-20">  
														<button type="button" class="btn rating-star ${ordVO.stob_rating != 0? 'disabled' : '' }" id="rating-star-${ordVO.order_id}" data-toggle="modal" data-target="#ratingModal" onclick="ratingOrder('${ordVO.order_id}','${memSvc.getOneMember(ordVO.buyer_mem_id).mem_Name}','${ordVO.buyer_mem_id}')">${ordVO.stob_rating != 0? '已評價' : '評價' }</button>
													</p>
													<hr class="divider-w p-t-10" style="border-color:#313438">
												</c:forEach>
											</div>
										<!-- 已完成  tab3 END-->	
											
										<!-- 取消 tab4 -->		
											<div class="tab-pane" id="tab_4">
											<c:forEach var="ordVO" items="${CLList}">
												<div class="host">
													<a href="#" target="_blank" class="photo" style="background-image: url(<%=request.getContextPath()%>/front_end/readPic?action=member&id=${ordVO.seller_mem_id})">
													</a>
													<span class="text" style="display:inline-block">	
														<a href="#" target="_blank" style="display:inline-block">${memSvc.getOneMember(ordVO.seller_mem_id).mem_Name}</a>	
													</span>
												</div>
													<div class="text order_id">訂單編號： ${ordVO.order_id}</div>
													<hr class="divider-w pt-20">
													<c:forEach var="ordDetailsVO" items="${ordDetailsSvc.getOrderDetialsByOrdId(ordVO.order_id)}">
														<div class="order-content">
															<div class="order-content-product">
																<div class="order-content-img" style="display:inline-block">
																	<div class="prod-img-wrap">
																		<div class="prod-img-content" style="background-image: url(data:image/jpeg;base64,${prodSvc.getOneProduct(ordDetailsVO.details_product_id).product_photo_1_base})">
																		</div>
																	</div>
																</div>
																<div class="order-content-detail" style="display:inline-block">
																	<div class="order-content-prod-name">${prodSvc.getOneProduct(ordDetailsVO.details_product_id).product_name}</div>
																	<div class="order-content-prod-qty">x ${ordDetailsVO.details_order_qty}</div>
																</div>
																<div class="order-content-prod-price" style="display:inline-block;float:right">
																	<div class="order-content-prod-price-text" style="display:inline-block">
																		$ ${ordDetailsVO.details_order_total}
																	</div>
																</div>
															</div>
														</div>
													</c:forEach>
													<hr class="divider-w pt-20">
													<div class="order-list-review" align="right">
														<div class="order-list-review-title" style="display:inline-block">訂單金額 (${ordVO.order_item} 商品):</div>
														<div class="order-list-review-price" style="display:inline-block">$ ${ordVO.order_total}</div>
													</div>
													<p class="form-submit p-b-20">  
														<button type="button" class="btn cancel-details" id="cancel-details-${ordVO.order_id}" data-toggle="modal" data-target="#cancelDetailsModal" onclick="cancelDetails('${ordVO.order_id}','${ordVO.cancel_reason}','${ordVO.order_date}')">查看詳情</button>
													</p>
												</c:forEach>
											</div>
										<!-- 取消 tab4 END-->	
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
                  </div>
                  <!--//銷售清單列表-->
                  
                  <!--我的評價-->
                  <div id="rating_sell" class="tab-pane fade">
                  		<div class="container">
						<div class="row">	
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
                  <!--//我的評價-->
                </div>
            </div>
            <!--//首頁左半邊-銷售管理-->
          </div>
          <!--頁籤項目-銷售管理內容-->
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

<!--===============================================================================================-->

<!-- 已完成 評價 Modal -->
  <div class="modal fade" id="ratingModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- 已完成 評價  Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">評價此商品買家</h4>
        </div>
        <div class="modal-body">
          <div class="host">
			<a href="#" target="_blank" class="photo" id="buyerPic" style="background-image: url(https://img.triplisher.com/USERIMG/133.jpg)">
			</a>
			<span class="text">	
				<a href="#" target="_blank" id="buyerName"></a>	
			</span>
			</div>
			<hr class="divider-w pt-20" style="margin-top: 30px;">
			<div id="rating-area" class="p-b-20">
				<input type="hidden" name="rating" id="ratingStar" value="" />
				<ul class="list-inline" data-rating="0" title="Average Rating -0">
					<li title="1" id="6-1" data-index="1" data-business_id="6" data-rating="0" class="rating" style="cursor: pointer; color: rgb(204, 204, 204); font-size: 28px;" onClick="addRating(this)">★</li>
					<li title="2" id="6-2" data-index="2" data-business_id="6" data-rating="0" class="rating" style="cursor: pointer; color: rgb(204, 204, 204); font-size: 28px;" onClick="addRating(this)">★</li>
					<li title="3" id="6-3" data-index="3" data-business_id="6" data-rating="0" class="rating" style="cursor: pointer; color: rgb(204, 204, 204); font-size: 28px;" onClick="addRating(this)">★</li>
					<li title="4" id="6-4" data-index="4" data-business_id="6" data-rating="0" class="rating" style="cursor: pointer; color: rgb(204, 204, 204); font-size: 28px;" onClick="addRating(this)">★</li>
					<li title="5" id="6-5" data-index="5" data-business_id="6" data-rating="0" class="rating" style="cursor: pointer; color: rgb(204, 204, 204); font-size: 28px;" onClick="addRating(this)">★</li>
				</ul>
			</div>
			<div>
				<textarea rows="4" cols="50" id='ratingDescr'>
	
				</textarea>
			</div>
			<div class="p-t-20 p-b-10">跟我們分享你的經驗吧!</div>
        </div>
        <div class="modal-footer">
      	  <input type="hidden" name="ratingOrdId" id="ratingOrdId" value=""/>
          <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          <button type="button" class="btn btn-info" data-dismiss="modal" onclick="ratingById()">完成</button>
        </div>
      </div>
   <!-- Modal 已完成 評價 content END-->    
    </div>
  </div>   
<!-- Modal 已完成 評價 END -->

<!-- 待收貨 確認到貨 Modal  -->
  <div class="modal fade" id="confirmDeliveryModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- 確認到貨 content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">確認到貨</h4>
        </div>
        <div class="modal-body">
          <div>確認商品到貨了嗎?</div>
          <div>尚未到貨請點選取消，確認到貨請點選完成!</div>
        </div>
        <div class="modal-footer">
          <input type="hidden" name="confirmOrdId" id="confirmOrdId" value=""/>
          <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          <button type="button" class="btn btn-info" data-dismiss="modal" onclick="confirmDeliveryById()">完成</button>
        </div>
      </div>
<!-- 待收貨 確認到貨 Modal CONTENT END -->      
    </div>
  </div>
<!-- 待收貨 確認到貨 Modal END -->  

<!--待出貨 取消訂單  Modal -->
  <div class="modal fade" id="cancelOrderModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">取消原因</h4>
        </div>
        <div class="modal-body">
        	<div>請選擇取消原因</div>
         <select class="selectpicker" id="cancelOrdSelect">
		  <option value="21">買家要求取消</option>
		  <option value="22">缺貨</option>
		  <option value="23">收件地址不在運送範圍內</option>
		  <option value="24">其他(例如 不想賣了)</option>
		 </select>

        </div>
        <div class="modal-footer">
          <input type="hidden" name="ordId" id="ordId" value=""/>
          <button type="button" class="btn btn-default" data-dismiss="modal">離開</button>
          <button type="button" class="btn btn-info" data-dismiss="modal" onclick="cancelOrderById()">取消訂單</button>
        </div>
      </div>
<!--待出貨 取消訂單  Modal CONTENT END -->       
    </div>
  </div>
<!--待出貨 取消訂單  Modal END -->   

<!--取消 取消詳情 Modal -->
  <div class="modal fade" id="cancelDetailsModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">取消訂單詳情</h4>
        </div>
        <div class="modal-body">
          <div>取消人<span class="p-l-10" id="cancelSide">買家</span></div>
          <div>取消時間 <span class="p-l-10" id="cancelDate">2018-07-02</span></div>
          <div>取消原因 <span class="p-l-10" id="cancelReason">其他(例如 不想買了)</span></div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">離開</button>
        </div>
      </div>
 <!--取消 取消詳情 Modal CONTENT END-->    
    </div>
  </div>
 <!--取消 取消詳情 Modal END-->   

<!-- 待出貨 確認商品出貨 Modal -->
  <div class="modal fade" id="shipOrderModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">確認商品出貨</h4>
        </div>
        <div class="modal-body">
          <div>寄件編號<span class="p-l-10">
          	<input type="text" name="shipping-id" id="shipping-id" style="width: 300px;" placeholder="請輸入寄件編號"></span>
          </div>
        </div>
        <div class="modal-footer">
           <input type="hidden" name="shipOrdId" id="shipOrdId" value=""/>
          <button type="button" class="btn btn-default" data-dismiss="modal">離開</button>
           <button type="button" class="btn btn-info" data-dismiss="modal" onclick="shipById()">完成</button>
        </div>
      </div>
<!-- 待出貨 確認商品出貨 Modal CONTENT END-->      
    </div>
  </div>
<!-- 待出貨 確認商品出貨 Modal END--> 

      
<!--===============================================================================================-->

<!-- 評價 Script -->
 <script>
 			$(document).on('mouseover', '.rating', function(e) {
				var index = $(this).data("index");
				var business_id = $(this).data('business_id');
				remove_background(business_id);
				for (var count = 1; count <= index; count++) {
					$('#' + business_id + '-' + count).css('color', '#ffcc00');
					$('#' + business_id + '-' + count).removeClass('selected');
				}

			})
			
	
 			
			function addRating(e) {
				$('#rating-area li').each(function(index) {
					$(this).addClass('selected');
					$('#rating-area #ratingStar').val((index + 1));
					if (index == $('#rating-area li').index(e)) {
						return false;
					}
				})
 			}
			

			function remove_background(business_id) {
				for (var count = 1; count <= 5; count++) {
					$('#' + business_id + '-' + count).css('color', '#ccc');
				}
			}

			$(document).on('mouseout', '.rating', function() {
				var index = $(this).data("index");
				var business_id = $(this).data('business_id');
				var rating = $(this).data("rating");
				remove_background(business_id);
				//alert(rating);
				for (var count = 1; count <= rating; count++) {
					$('#' + business_id + '-' + count).css('color', '#ffcc00');
					$('#' + business_id + '-' + count).removeClass('selected');
				}
			});
			
			
 </script> 

<!--===============================================================================================-->
<script type="text/javascript">
	function deleteById(e, prod_id){
		var action = "deleteByAjax";
		var removeEle = $("#block-"+prod_id);
		 if(confirm('確定要刪除商品嗎?')){
			$.ajax({
				url:"${pageContext.request.contextPath}/front_end/store/product.do",
				method:"POST",
				async: false,
				data:{action:action,product_id:prod_id},
				success:function(data){
					console.log(removeEle.html());
					removeEle.remove();				
				}
			})
		 }
	}
</script>
	
<!--==============================================================================================-->
<!--管理的取消訂單按鈕-->   
<script>
$(document).on("click", ".cancel-order", function () {
    var ordId = $(this).data('id');
    $(".modal-footer #ordId").val( ordId );
   
});

function cancelOrderById(){
	var ordId =  $(".modal-footer #ordId").val();
	var action = "updateOrdCancel";
	var reason = $("#cancelOrdSelect").val();
	$.ajax({
		url:"${pageContext.request.contextPath}/front_end/store/shopping.do",
		method:"POST",
		async: false,
		data:{action:action,ordId:ordId,reason:reason},
		success:function(data){
			console.log("買家取消訂單成功");
			$('#ord_block_'+ordId).remove();
	
		}
	})
	
}
<!--=============================================================================================-->
<!--查看取消詳情按鈕-->  
function cancelDetails(ordId,reason,date){
	
	var cancelside;
	
	if(reason.slice(0, 1) == '1'){
		cancelside ="買家";	
		$('#cancelSide').html(cancelside);
	}else{
		cancelside ="賣家";
		$('#cancelSide').html(cancelside);
	}
	
	$('#cancelDate').html(date.slice(0, 19));
	
	var cancelReason;
	if(reason == '11'){
		cancelReason = "賣家不回應";
		$('#cancelReason').html(cancelReason);
	}else if(reason == '12'){
		cancelReason = "賣家要求取消訂單";
		$('#cancelReason').html(cancelReason);
	}else if(reason == '13'){
		cancelReason = "修改訂單內容(地址，姓名，電話等等)";
		$('#cancelReason').html(cancelReason);
	}else if(reason == '14'){
		cancelReason = "賣家出貨速度太慢";
		$('#cancelReason').html(cancelReason);
	}else if(reason == '15'){
		cancelReason = "對賣家行為有疑慮(例如要求私下交易)";
		$('#cancelReason').html(cancelReason);
	}else if(reason == '16'){
		cancelReason = "其他(例如 不想買了)";
		$('#cancelReason').html(cancelReason);
	}else if(reason == '21'){
		cancelReason = "買家要求取消";
		$('#cancelReason').html(cancelReason);
	}else if(reason == '22'){
		cancelReason = "缺貨";
		$('#cancelReason').html(cancelReason);
	}else if(reason == '23'){
		cancelReason = "收件地址不在運送範圍內";
		$('#cancelReason').html(cancelReason);
	}else if(reason == '24'){
		cancelReason = "其他(例如 不想賣了)";
		$('#cancelReason').html(cancelReason);
	}

}
</script>

<!--=============================================================================================-->
<!--確認到貨按鈕--> 

<script type="text/javascript">

function confirmDelivery(ordId){
	$(".modal-footer #confirmOrdId").val( ordId );
}

function confirmDeliveryById(){
	
	var ordId = $(".modal-footer #confirmOrdId").val();
	var action = "confirmDeliveryBySeller";

	$.ajax({
		url:"${pageContext.request.contextPath}/front_end/store/shopping.do",
		method:"POST",
		async: false,
		data:{action:action,ordId:ordId},
		success:function(data){
			console.log("賣家確認到貨");
		}
	})
	
}
</script>

<!--=============================================================================================-->
<!--確認出貨按鈕--> 
<script type="text/javascript">

function confirmShip(ordId){
	$(".modal-footer #shipOrdId").val( ordId );
}

function shipById(){
	var ordId = $(".modal-footer #shipOrdId").val();
	var action = "confirmShip";
	var shipId = $('#shipping-id').val();
	$.ajax({
		url:"${pageContext.request.contextPath}/front_end/store/shopping.do",
		method:"POST",
		async: false,
		data:{action:action,ordId:ordId,shipId:shipId},
		success:function(data){
			console.log("賣家確認出貨");
			$('#ord_block_'+ordId).remove();
		}
	})
	
}
</script>

<!--=============================================================================================-->
<!--評價按鈕--> 
<script>

function ratingOrder(ordId,memName,memId){
	$('#buyerName').html(memName);
	$(".modal-footer #ratingOrdId").val( ordId );
	$('#buyerPic').css('background-image', 'url(${pageContext.request.contextPath}/front_end/readPic?action=member&id=' + memId + ')');
}

function ratingById(){
	var ordId = $(".modal-footer #ratingOrdId").val();
	var action = "ratingOrder";
	var rating = $("#ratingStar").val();
	var ratingDescr = $("#ratingDescr").val();
	console.log(ratingDescr);
	$.ajax({
		url:"${pageContext.request.contextPath}/front_end/store/shopping.do",
		method:"POST",
		async: false,
		data:{action:action,ordId:ordId,bors:2,rating:rating,ratingDescr:ratingDescr},//bors 1>買家給賣家評價 2>賣家給買家評價
		success:function(data){
			console.log("買家送出評價");
			if(data=='not null'){
				window.alert("評價最低請給1顆星!");
			}else{
				$('#rating-star-'+ordId).html("已評價");
				$('#rating-star-'+ordId).prop('disabled', true);
			}
		}
	})
	
	
}
</script>

</body>

</html>
