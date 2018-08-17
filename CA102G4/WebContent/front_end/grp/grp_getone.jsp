<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.photo_wall.model.*"%>
<%@ page import="com.photo_tag.model.*"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.grp.model.*"%>
<%@ page import="java.util.*"%>

<%	
	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-store"); 
	response.setDateHeader("Expires", 0);	

	MemberVO memberVO = (MemberVO)request.getAttribute("memberVO");
	
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
	
// 	//我主動叫SERVICE去呼叫DAO去資料庫撈資料出來
// 	GrpService grpSvc = new GrpService();
//  	List<GrpVO> list = grpSvc.getAll();
//  	pageContext.setAttribute("list", list);
 	
 	//我請controller叫service去呼叫dao去撈資料
 	List<GrpVO> list_All = (List)request.getAttribute("listEmps_ByCompositeQuery");
	//左邊的KEY是給前端頁面用的變數名稱 右邊是上面是 list_All
 	pageContext.setAttribute("list_All", list_All);
	for(GrpVO i : list_All){
		System.out.println("找我麻煩********"+i.getGrp_Id());
	}
	for(int i = 0 ; i < list_All.size() ; i++){
		System.out.println("找我麻煩484"+list_All);
	}
 	System.out.print("list_ALL="+list_All);
 	
	
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
                        <h1>揪個合適的夥伴一起旅行</h1>
                        <p>你揪過了嗎？</p>
                        <p><a class="btn btn-lg btn-primary" href="#" role="button">開始我的揪團</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>

<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/grp.do" name="form1">
        <!--  search  -->
    <div class="flight-engine">
        <div class="container">
            <div class="tabing">
                <ul>
                    <li>
                        <a class="active" href="#1"><i class="fa fa-plane" aria-hidden="true"></i> 找揪團</a>
                    </li>
                </ul>
                
                <div class="tab-content">
                    <div id="1" class="tab1 active">
                         <div class="flight-tab row">
                            <div class="persent-one">
                                <i class="fa fa-map-marker" aria-hidden="true"></i>
                                <input type="text" name="TRIP_LOCALE" class="textboxstyle" id="arival" placeholder="請輸入地點或關鍵字">
                            </div>
                            <div class="persent-one less-per">
                                <i class="fa fa-calendar" aria-hidden="true"></i>
                                <input type="text" name="GRP_START"  class="textboxstyle" id="datepicker_trip_start" placeholder="揪團開始日期">
                            </div>
                            <div class="persent-one less-per">
                                <i class="fa fa-calendar" aria-hidden="true"></i>
                                <input type="text" name="GRP_END" class="textboxstyle " id="datepicker_trip_end" placeholder="揪團結束日期">
                            </div>
                            <div class="persent-one less-btn">
                                <input type="Submit" value="Search" class="btn cst-btn" id="srch">
                                <input type="hidden" name="action" value="listEmps_ByCompositeQuery">
                            </div>
                        </div>
                    </div>

                </div>
            </div>

        </div>

    </div>
</FORM>
    
    <div class="ui container">

            <div id="cards" class="ui four stackable cards"  >
                <c:forEach var="GrpVO" items="${list_All}">
				<c:if test="${GrpVO.grp_Status == 1 }">
				<a class="fluid card" href="<%=request.getContextPath()%>/front_end/grp/grp_oneview.jsp?grp_Id=${GrpVO.grp_Id}" style="margin-top:30px;">
					<div class="image">
						<div class="ui image pic" style="height:220px; background-size:cover;">
						<img style="height:100%;width:100%" src="<%=request.getContextPath()%>/grpPicReader?grp_Id=${GrpVO.grp_Id}">
						</div>
					</div>
					<div class="content">
                        <div class="header">
                           <img id="user-group" src="<%=request.getContextPath()%>/front_end/readPic?action=member&id=${GrpVO.mem_Id}">
                            <span class="font-f">${memberSvc.findByPrimaryKey(GrpVO.mem_Id).mem_Name}</span>
                            <p class="font-f">${GrpVO.grp_Title}</p>
                        </div>
                    <hr class="card-hr">
                    <div class="con-group">
                    <p class="far fa-calendar-alt con-group font-f">                
                    <fmt:formatDate pattern="YYYY年MM月dd日  HH:mm" value="${GrpVO.grp_Start}" />
                    </p><br>                                       
                    <p class="fas fa-map-marker-alt con-group font-f">${GrpVO.trip_Locale}</p><br>
                    <p class="fas fa-dollar-sign con-group font-f">&nbsp;${GrpVO.grp_Price}</p><br>
                    </div>
                    </div>
				</a>
				</c:if>
			</c:forEach>
                
              
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

