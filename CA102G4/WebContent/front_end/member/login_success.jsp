<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.grp.model.*"%>

<html>

<%
// 	MemberVO memberVO = (MemberVO) session.getAttribute("memberVO"); 

// 	boolean login_state = false;
// 	Object login_state_temp = session.getAttribute("login_state");
// 	if(login_state_temp!=null){
// 		login_state=(boolean)login_state_temp;
// 	}
	
// 	String mem_Id = memberVO.getMem_Id();

// 	String grp_Id = request.getParameter("grp_Id");	
	
// 	GrpService grpsvc = new GrpService();
// 	GrpVO grpVO = grpsvc.findByPrimaryKey(grp_Id);
// 	pageContext.setAttribute("grpVO", grpVO);
	
// 	System.out.println("mem_Id="+mem_Id);
	
// 	System.out.println("grp_Id="+grp_Id);
		
// 	System.out.println("grpVO="+grpVO);
	
%>

<head>
    <title>Travel Maker</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="keywords" content="TravelMaker,Travelmaker,自助旅行,登入畫面" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link href="<%=request.getContextPath()%>/front_end/css/all/index_bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <script src="<%=request.getContextPath()%>/front_end/js/all/index_bootstrap.js"></script>
    <!-- font字體 -->
    <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
    <!-- //font字體 -->
    <link href="<%=request.getContextPath()%>/front_end/css/member/blog_semantic.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/front_end/css/member/google_icon.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/front_end/css/member/bootstrap.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/front_end/css/member/animate.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/front_end/css/member/login_style.css" type="text/css" media="all" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/front_end/css/member/style.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/front_end/css/member/login.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/front_end/css/member/modal.css">
    <script src="<%=request.getContextPath()%>/front_end/js/member/modernizr-2.6.2.min.js"></script>
    <link href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" rel="stylesheet" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">

	 <!-- jQuery -->
    <script src="<%=request.getContextPath()%>/front_end/js/member/jquery.min.js"></script>
    <!-- Bootstrap -->
    <script src="<%=request.getContextPath()%>/front_end/js/member/bootstrap.min.js"></script>
    <!-- Placeholder -->
    <script src="<%=request.getContextPath()%>/front_end/js/member/jquery.placeholder.min.js"></script>
    <!-- Waypoints -->
    <script src="<%=request.getContextPath()%>/front_end/js/member/jquery.waypoints.min.js"></script>
    <!-- Main JS -->
    <script src="<%=request.getContextPath()%>/front_end/js/member/main.js"></script>
    
		
<style>


body > div.banner.about-bg > div.row_login > form{
margin:80px auto;
}
.three-login{
    margin: 30px auto;
    text-align: center;
}
#btn-fb{
	width: 75px;
    height: 25px;
    border:0px;
}

#btn-google{
	width: 75px;
    height: 25px;
    background-color: red;
    border:0px;
}

#modal-setting{
	margin:0 auto;
	margin-left:138px;
	padding-bottom: 15px;
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
                    </ul>
                </div>
                <div class="top-banner-right">
                    <ul>

	                    	<li style=""><a class="top_banner" href="<%=request.getContextPath()%>/front_end/personal_area/personal_area_home.jsp"><i class="fa fa-user" aria-hidden="true"></i></a></li>          	
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

        <!-- //banner -->

        <!-- Start Sign In Form -->
        <div class="row_login">
            <form method="post" action="#" class="fh5co-form animate-box" data-animate-effect="fadeIn">
                <h2>Welcome</h2>
                	<p style="text-align:center;font-size:18px;color:black;">親愛的會員您好<br>
                	<p style="text-align:center;font-size:18px;color:black;">感謝您加入<br>
                	<p style="text-align:center;font-size:18px;color:black;">Travel Maker<br>
                	<p style="text-align:center;font-size:18px;color:black;">系統已發送「會員密碼」至您的帳號（電子信箱）<br>
                	<p style="text-align:center;font-size:18px;color:black;">成功登入後，修改成您熟悉的密碼，便利以後的登入<br>
                	<p style="text-align:center;font-size:18px;color:black;">如果您沒收到，請至垃圾郵件查看。<br>
                	<p style="text-align:center;font-size:18px;color:red;"><a style="color:red;text-decoration:none;" href="<%= request.getContextPath()%>/front_end/member/mem_login.jsp">請點擊此處回登入頁面</a><br>
                	
            </form>
         </div>
    </div>
    <!-- END Sign In Form -->


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

</body>

</html>

