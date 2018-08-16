<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.grp.model.*"%>

<html>

<%
// 	MemberVO memberVO = (MemberVO) session.getAttribute("memberVO"); 

	boolean login_state = false;
	Object login_state_temp = session.getAttribute("login_state");
	if(login_state_temp!=null){
		login_state=(boolean)login_state_temp;
	}
	
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
    
<!--  fb  -->
 <!-- FB -->
    <script>
        //初始化設定載入Facebook SDK
        (function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) return;
            js = d.createElement(s); js.id = id;
            js.src = 'https://connect.facebook.net/zh_TW/sdk.js#xfbml=1&version=v3.0&appId=196108454564197';
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));

        //登入按鈕 //取得授權並登入應用程式
        function fbLogin() {
            FB.login(function(response) {
                statusChangeCallback(response);
            }, {scope: 'public_profile,email'});
        }
        //登出按鈕 //把FB真的登出
        function fbLogout() {
            FB.logout(function(response) {
                statusChangeCallback(response);
            });
        }
        //檢查登入狀態並取得資料
        function statusChangeCallback(response) {
            if (response.status === 'connected') {
                console.log('Facebook已登入')
                FB.api('/me?fields=id,name,picture,email', function(response) {
                    document.getElementById('com').innerText='FACEBOOK'
                    document.getElementById('uid').innerText=response.id
                    document.getElementById('name').innerText=response.name
                    document.getElementById('email').innerText=response.email
                    document.getElementById('picture').src=response.picture.data.url
                });
            } else {
                console.log('Facebook未登入')
            }
        }
        
    </script>
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
                        <li><a class="top_banner" href="#"><i class="fa fa-user" aria-hidden="true"></i></a></li>
                        <li><a class="top_banner" href="#"><i class="fa fa-shopping-cart" aria-hidden="true"></i></a></li>
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
                        <a href="index.html">Travel Maker</a>
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

	<%-- 錯誤表列 --%>
	<c:if test="${not empty errorMsgs}">
		<font color='red'>請修正以下錯誤:
			<ul>
				<c:forEach var="message" items="${errorMsgs}">
					<li>${message}</li>
				</c:forEach>
			</ul>
		</font>
	</c:if>

        <!-- Start Sign In Form -->
        <div class="row_login">
            <form method="post" action="<%=request.getContextPath()%>/front_end/member/member.do" class="fh5co-form animate-box" data-animate-effect="fadeIn">
                <h2>Welcome</h2>
                
                <div class="form-group">
                    <label id="username" class="sr-only">Email</label>
                    <input type="text" name="mem_Account" value="" required="required" class="form-control" id="username" placeholder="Email" autocomplete="off" />
                </div>
                <div class="form-group">
                    <label id="password" class="sr-only">Password</label>
                    <input type="password" name="mem_Password" value="" required="required" class="form-control" id="password" placeholder="Password" autocomplete="off" />
                </div>

                <div id="modal-setting">

                    <button type="button" data-toggle="modal" data-target="#myModal" style="border:0;">Register</button>

<!--                     <button type="button" data-toggle="modal" data-target="#myModal_foget" style="border:0;">Forgot Password?</button> -->

                    <!--
            <a href="sign-up.html">
                <font size="4px">Register</font></a>&nbsp;&nbsp;&nbsp;
                Forgot Password? -->

                </div>


                <div class="form-group sign-group">
                	<input type="hidden" name="action" value="login">
                    <input type="submit" value="Sign In" class="btn btn-primary">

                </div>

                <p></p>

                <hr style="border: 0.5px solid gery">
                <div class="hr-more">New&nbsp;&nbsp;to&nbsp;&nbsp;Travel&nbsp;&nbsp;Maker</div>

                <div class="three-login">


                    <button id="btn-fb"  class="ui facebook button btn3" onclick="fbLogin();">
          <i class="fab fa-facebook-square"></i></button>

         <button id="btn-google" class="ui google button btn3"> 
           <i class="fab fa-google-plus-square"></i></button>


                </div>
                               
                <!--
        <div class="form-group">
            <a href="sign-up.html">Create your account</a>
        </div>
-->


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

    <script>
		function show() {
		 var inputs = document.getElementsByTagName('input');
		 
		document.form1.mem_Name.value="李柴柴";
		document.form1.mem_Account.value="ca102g4@gmail.com.tw";
		document.form1.mem_Password.value="a12345678";

		}
		
		
		function chk(){
			if(!document.form1.name.value){
				alert('test!');
				return false;
			}else if(!document.form1.modal-password.value){
				alert('mima ?');
				return false;
			}
		}
	</script>
	

</body>

</html>

<!-- 燈箱 Register 開始 -->
<!-- Modal Register -->
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
    
    <c:if test="${not empty errorMsgs}">
	<font style="color:red">請修正以下錯誤:</font>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li style="color:red">${message.value}</li>
		</c:forEach>
	</ul>
</c:if>

        <!-- Modal Register content-->
       <div background-color="lightblue;">
            <form METHOD="post" ACTION="<%=request.getContextPath()%>/front_end/member/member.do" name="form1" class="fh5co-form animate-box-modal" data-animate-effect="fadeIn" onsubmit="return chk();">
                <h2>Sign Up</h2>

                <div class="form-group">
                    <label for="name" class="sr-only">Name</label>
                    <input name="mem_Name" size="45" value="${param.mem_Name}" type="text" class="form-control" id="name" placeholder="name" autocomplete="off">
                </div>
                <div class="form-group">
                    <label for="account" class="sr-only">Account</label>
                    <input name="mem_Account" size="45" value="${param.mem_Account}" type="text" class="form-control" id="account" placeholder="account" autocomplete="off">
                </div>
                <div class="form-group">
                    <label for="password" class="sr-only">Password</label>
                    <input name="mem_Password" size="45" value="${param.mem_Password}" type="password" class="form-control" id="modal-password" placeholder="password" autocomplete="off">
                </div>
                <div class="form-group">
                    <label for="re-password" class="sr-only">Re-type Password</label>
                    <input type="Password" class="form-control" id="re-password" placeholder="Re-type Password" autocomplete="off">
                </div>

                <div class="form-group">
                   
                    <label for="verification code" class="sr-only">verification code</label>
                    <input type="verification code" class="form-control" id="verification code" placeholder="verification code" autocomplete="off">
                </div>
                <div class="form_btn">
                
                    </div>

                <div class="form-group">
                	<input type="hidden" name="action" value="insert" class="btn btn-primary">
                    <input type="submit" value="Confirm" class="btn btn-primary">  
                            
                    <input type="button" value="Cancel" onclick="javascript:location.href='mem_login.jsp'" class="btn btn-primary" >
                </div>
                    <input type="button" onclick="show()" style="background-color:skyblue; float:right; border:0px;">
            </form>
            
        </div>
        <!-- //Modal Register content-->

    </div>

</div>
<!-- 燈箱 Register 結束 -->

<!-- 燈箱 Forgot Password  開始 -->

<!-- Modal_foget -->
<!-- <div class="modal fade" id="myModal_foget" role="dialog"> -->
<!--     <div class="modal-dialog"> -->

<!--         Modal_foget content -->
<!--         <div background-color="lightblue;"> -->
<!--             <form action="#" class="fh5co-form animate-box-modal" data-animate-effect="fadeIn"> -->
<!--                 <h2>Forgot Password ?</h2> -->

<!--                 <div class="form-group"> -->
<!--                     <label for="name" class="sr-only">Name</label> -->
<!--                     <input type="text" class="form-control" id="name" placeholder="Name" autocomplete="off"> -->
<!--                 </div> -->
<!--                 <div class="form-group"> -->
<!--                     <label for="email" class="sr-only">Email</label> -->
<!--                     <input type="email" class="form-control" id="email" placeholder="Email" autocomplete="off"> -->
<!--                 </div> -->

<!--                 <div class="form-group"> -->
<!--                     <label for="verification code" class="sr-only">verification code</label> -->
<!--                     <input type="verification code" class="form-control" id="verification code" placeholder="verification code" autocomplete="off"> -->
<!--                 </div> -->
                
<!--                 <div class=""> -->
                
<!--                     <input type="submit" value="Send" class="btn btn-primary"> -->
                
<!--                 </div> -->


<!--                 <div class="form-group"> -->
<!--                     <input type="button" value="Cancel" class="btn btn-primary" onclick="history.back()"> -->
<!--                     <input type="submit" value="Confirm" class="btn btn-primary"> -->
<!--                 </div> -->
<!--             </form> -->
<!--         </div> -->
<!--         //Modal_foget content -->

<!--     </div> -->

<!-- </div> -->
<!-- 燈箱 Forgot Password  結束 -->
