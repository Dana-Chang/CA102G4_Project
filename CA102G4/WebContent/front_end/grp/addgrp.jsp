<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ page import="com.photo_wall.model.*"%>
<%@ page import="com.photo_tag.model.*"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.grp.model.*"%>
<%@ page import="java.util.*"%>

<%	
//清快取
	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-store"); 
	response.setDateHeader("Expires", 0);
	
	MemberVO memberVO = (MemberVO) session.getAttribute("memberVO"); 
	if(memberVO == null){
		memberVO = (MemberVO)session.getAttribute("memberVO");
	}	
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
			
	//若登入狀態為不是true，紀錄當前頁面並重導到登入畫面。
	if( login_state != true){
		session.setAttribute("location", request.getRequestURI());
		response.sendRedirect(request.getContextPath()+"/front_end/member/mem_login.jsp");
		return;
	}
	
	//從memberVO取會員ID
	
	GrpService grpSvc = new GrpService();
	List<GrpVO> list = grpSvc.getAll();
	pageContext.setAttribute("list", list);
	
	
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
<html>

<jsp:useBean id="memberSvc" scope="page" class="com.mem.model.MemberService" />

<head>
    <!-- 網頁title -->
    <title>Travel Maker</title>
    <!-- //網頁title -->
    <!-- 指定螢幕寬度為裝置寬度，畫面載入初始縮放比例 100% -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- //指定螢幕寬度為裝置寬度，畫面載入初始縮放比例 100% -->
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!-- 設定網頁keywords -->
    <meta name="keywords" content="TravelMaker,Travelmaker,自助旅行,照片牆" />
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
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <script src="https://code.jquery.com/jquery.js"></script>
    <!-- //JQUERY -->
    
    <!-- Bootstrap -->
	<link href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css" rel="stylesheet" type="text/css" media="all" />
	<script src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
    
    <!-- font字體 -->
    <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
    <!-- //font字體 -->
   

    <!-- 套首頁herder和footer css -->
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_style_header_footer.css" rel="stylesheet" type="text/css" media="all" />
    <link href="<%=request.getContextPath()%>/front_end/css/blog/blog.css" rel="stylesheet" type="text/css" media="all" />
    <!-- //套首頁herder和footer css -->

    <!-- grp 自定義的css -->
    <link href="<%=request.getContextPath()%>/front_end/css/grp/AD_semantic.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/grp/group.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/grp/group_fix.css" rel="stylesheet" type="text/css">
    <!-- //grp 自定義的css -->

    <!-- font-awesome icons -->
    <link href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" rel="stylesheet" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- //font-awesome icons -->

    <!-- font字體 -->
    <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
    <!-- //font字體 -->
    
    <!--  datepicker js  -->
    <script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
    <!--//datepicker js  -->
    
    

	<style>
.footer{
	background-color:#1b1c1d; 
	}
	</style>

</head>

<body>
   
    <!-- banner -->
    <div class="banner about-bg">
        <div class="top-banner about-top-banner">
            <div class="container">
                <div class="top-banner-left">
                    <ul>
                        <li><i class="fa fa-phone" aria-hidden="true"></i>
                            <a href="tel:034257387"> 03-4257387</a></li>
                        <li><a href="mailto:TravelMaker@gmail.com"><i class="fa fa-envelope" aria-hidden="true"></i> TravelMaker@gmail.com</a></li>
                    <ul>
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

	<!-- 主圖區 -->
    <div>
        <div class="carousel-inner">
            <div class="carousel-images">
                <img src="<%=request.getContextPath()%>/front_end/images/all/aegean_Sea_2.png" style="width:100% ;height:auto;" alt="">
                <div class="">
                    <div class="carousel-caption">											
                    </div>
                </div>
            </div>
        </div>
    </div>  
   
    <div class="container" style="height: 1500px; margin-top:50px">
    	<div class="row" style="width:530px;height:20px;margin:0 auto;">
			<div class="col-offset-6 col-md-10 col-md-offset-6" style="width:530px;height:20px;margin:0 auto;">
				<div class="panel panel-login" style="border: solid 0.5px;border-color:#C8C8C8;">
					<div class="panel-heading">
						<div class="row" >
							<h2 style="text-align:center;">開始計畫新的揪團</h2>
						</div>
						<hr>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
								<form action="<%=request.getContextPath()%>/grp.do" method="post" role="form" style="">
									<div class="form-group">
										<p><i class="fas fa-edit" style="color:gray;"></i>&nbsp;幫揪團取個名字吧</p>
										<font style="color:red">&nbsp;${errorMsgs.grp_Title}</font>
										<input type="text" name="grp_Title" class="form-control" placeholder="團名" value="${param.grp_Title}">
									</div>
									<div class="form-group">
										<p><i class="fas fa-map-marker-alt" style="color:red;"></i>&nbsp;想去哪裡呢</p>
										<font style="color:red">&nbsp;${errorMsgs.trip_Locale}</font>
										<input type="text" name="trip_Locale" class="form-control" placeholder="地點" value="${param.trip_Locale}">
									</div>
									<div class="form-group">
										<p><i class="fas fa-dollar-sign" style="color:red;"></i>&nbsp;大概預算</p>
										<font style="color:red">&nbsp;${errorMsgs.grp_Price}</font>
										<input type="text" name="grp_Price" class="form-control" placeholder="預算" value="${param.grp_Price}">
									</div>
									<div class="form-group">
										<p><i class="fas fa-users" style="color:#483D8B;"></i>&nbsp;報名人數</p>
										<font style="color:red">&nbsp;${errorMsgs.grp_Cnt}</font>
										<input type="number" name="grp_Cnt" value="${param.grp_Cnt}" min="1" max="99" size="5" tabindex="2" class="form-control" placeholder="預計報名人數">
									</div>
									<div class="form-group">
										<p><i class="fas fa-users" style="color:#483D8B;"></i>&nbsp;出團人數</p>
										<font style="color:red">&nbsp;${errorMsgs.grp_Acpt}</font>
										<input type="number" name="grp_Acpt" value="${param.grp_Acpt}" min="1" max="99" size="5" tabindex="2"  class="form-control" placeholder="成團出發人數">
									</div>
									<div class="form-group">
										<p><i class="fas fa-calendar-alt" style="color:#778899;"></i>&nbsp;揪團結束的日期</p>
										<font style="color:red">&nbsp;${errorMsgs.grp_End}</font>
										<input type="text" name="grp_End" value="${param.grp_End}" id="datepicker_grp_end" class="form-control" placeholder="請選擇揪團結束日期">
									</div>
									<div class="form-group">
										<p><i class="fas fa-calendar-alt" style="color:#778899;"></i>&nbsp;啟程日期</p>
										<font style="color:red">&nbsp;${errorMsgs.trip_Strat}</font>
										<input type="text" name="trip_Strat" value="${param.trip_Strat}" id="datepicker_trip_start" class="form-control" placeholder="請選擇行程起程日期">
									</div>
									<div class="form-group">
										<p><i class="fas fa-calendar-alt" style="color:#778899;"></i>&nbsp;回程日期</p>
										<font style="color:red">&nbsp;${errorMsgs.trip_End}</font>
										<input type="text" name="trip_End" value="${param.trip_End}" id="datepicker_trip_end" class="form-control" placeholder="請選擇行程結束日期">
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="hidden" name="mem_Id" value="${memberVO.mem_Id}"> 
                								<input type="hidden" name="action" value="insert" class="btn btn-primary">
												<input type="submit"  class="form-control btn btn" style="border-color:#C8C8C8;" value="創建揪團">
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

    <!--  //search  -->


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
                            <li><a href="https://www.facebook.com/InstaBuy.tw/"><i class="fab fa-facebook"></i></a></li>
                            <li><a href="https://www.instagram.com/"><i class="fab fa-instagram"></i></a></li>
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
                            <input type="email" id="mc4wp_email" name="EMAIL" placeholder="請輸入您的Email" required="">
                            <input type="submit" value="訂閱">
                        </form>
                    </div>
                </div>
                <div class="clearfix"> </div>
            </div>
            <div class="copyright">
                <p>Copyright &copy; 2018 All rights reserved
                    <a href="index.html" target="_blank" title="TravelMaker">TravelMaker</a>
                </p>
            </div>
        </div>
    </div>	
 
<script>
  $(function() {
   $( "#datepicker_grp_end" ).datepicker({
      showAnim: "slideDown",
      dateFormat : "yy-mm-dd"
    });
  });
 </script>
<script>
  $(function() {
   $( "#datepicker_trip_start" ).datepicker({
      showAnim: "slideDown",
      dateFormat : "yy-mm-dd"
    });
  });
 </script>
 <script>
  $(function() {
   $( "#datepicker_trip_end" ).datepicker({
      showAnim: "slideDown",
      dateFormat : "yy-mm-dd"
    });
  });
 </script>
</body>

</html>

