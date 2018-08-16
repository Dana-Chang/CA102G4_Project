<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.admin.model.*"%>


<!DOCTYPE HTML>
<html>

<%
	AdminVO adminVO = (AdminVO)session.getAttribute("adminVO");
	if(adminVO == null){
	 adminVO = (AdminVO)session.getAttribute("adminVO");
	}
	
	boolean login_state_backEnd = false;
	Object login_state_temp = session.getAttribute("login_state_backEnd");
	if(login_state_temp!=null){
	 login_state_backEnd=(boolean)login_state_temp;
	}
	
	if(login_state_backEnd!=true){
	 session.setAttribute("location_Backend","/CA102G4/back_end/admin/add_admin.jsp");
	 response.sendRedirect(request.getContextPath()+"/back_end/admin/back_login.jsp");
	}

	AdminService adminSvc = new AdminService();
	List<AdminVO> list = adminSvc.getAll();
	pageContext.setAttribute("list", list);
%>

<head>
<title>Travel Maker 後台</title>
<meta charset="UTF-8">
<!-- Bootstrap CSS v3.3.4 -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/all/bootstrap.css">

<!-- 自定義的CSS 包含sidebar與Content-->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/all/back_all.css">

<!-- Font Awesome JS -->
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
	integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp"
	crossorigin="anonymous">


<!-- jQuery CDN - Slim version (=without AJAX) -->
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>

<!-- Bootstrap JS v3.3.4-->
<script src="<%=request.getContextPath()%>/back_end/js/all/bootstrap.js"></script>

<!-- 自己定義的JS檔案-->
<script src="<%=request.getContextPath()%>/back_end/js/all/back_all.js"></script>

<!-- font字體 -->
<link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300'
	rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Pacifico'
	rel='stylesheet' type='text/css'>
<!-- //font字體 -->

</head>
<body>
	<div class="wrapper">
		<!-- Sidebar  -->
		<nav id="sidebar" class="navbar-fixed-left">
			<div class="sidebar-header">
				<h3>Travel Maker</h3>
				<strong>TM</strong>
			</div>

			<ul class="list-unstyled components">
				<li class="active"><a
					href="<%=request.getContextPath()%>/back_end/back_index.jsp"> <i
						class="fas fa-home"></i> 回首頁
				</a></li>

				<li>
				<li class="dropdown"><a href="#auth_Submenu"
					data-toggle="collapse" aria-expanded="false"
					class="dropdown-toggle"> <i class="fas fa-users"></i> 權限管理
				</a>

					<ul class="dropdown-menu" id="auth_Submenu">
						<li><a
							href="<%=request.getContextPath()%>/back_end/admin/manager_admin.jsp">管理員</a>
						</li>
						<li><a
							href="<%=request.getContextPath()%>/back_end/admin/manager_member.jsp">會員</a>
						</li>
					</ul>
					
					</li>

				<li><a href="#"> <i class="fas fa-newspaper"></i> 最新消息管理
				</a></li>

				<li><a href="#"> <i class="fas fa-image"></i> 景點管理
				</a></li>

				<li class="dropdown"><a href="#category_Submenu"
					data-toggle="collapse" aria-expanded="false"
					class="dropdown-toggle"> <i class="fas fa-tag"></i> 標籤管理
				</a>
					<ul class="dropdown-menu" id="category_Submenu">
						<li><a href="Back_TagBlog.html">旅遊記</a></li>
						<li><a href="#">問答區</a></li>
					</ul></li>

				<li class="dropdown"><a href="#report_Submenu"
					data-toggle="collapse" aria-expanded="false"
					class="dropdown-toggle" aria-haspopup="true"> <i
						class="fas fa-comment-dots"></i> 檢舉管理
				</a>
					<ul class="dropdown-menu" id="report_Submenu">
						<li><a href="#">會員檢舉</a></li>
						<li><a href="Back_ReportBlog.html">旅遊記檢舉</a></li>
						<li><a href="#">問答區檢舉</a></li>
						<li><a href="#">照片牆檢舉</a></li>
						<li><a href="#">揪團檢舉</a></li>
						<li><a href="#">商品檢舉</a></li>
					</ul></li>

				<li><a href="#"> <i class="fas fa-shopping-cart"></i>
						交易款項管理
				</a></li>

				<li><a
					href="<%=request.getContextPath()%>/back_end/ad/back_ad.jsp"> <i
						class="fas fa-audio-description"></i> 專欄廣告管理
				</a></li>

				<li class="dropdown"><a href="#aboutUS_Submenu"
					data-toggle="collapse" aria-expanded="false"
					class="dropdown-toggle"> <i class="fas fa-briefcase"></i>
						關於我們管理
				</a>
					<ul class="dropdown-menu" id="aboutUS_Submenu">
						<li><a href="#">關於我們</a></li>
						<li><a href="#">FAQ</a></li>
					</ul></li>

				</li>
			</ul>

		</nav>

		<!-- Page Content  -->
		<div id="content">

			<nav class="navbar">
				<!--navbar-expand-lg navbar-light bg-light-->
				<div class="container-fluid">
					<!--側邊欄按鈕-->
					<button type="button" id="sidebarCollapse" class="btn btn-info">
						<i class="fas fa-align-left"></i>
					</button>
					<span style="float: right"> 
					<c:choose>
                          <c:when test="<%=login_state_backEnd %>">
                           <a href="<%= request.getContextPath()%>/admin.do?action=logout"><span class=" top_banner btn btn-info"><i class=" fas fa-sign-out-alt" aria-hidden="true"></i></span></a>
                          </c:when>
                          <c:otherwise>
                           <a href="<%= request.getContextPath()%>/admin_login.jsp"><span class="top_banner btn btn-info"><i class=" fa fa-user" aria-hidden="true"></i></span></a>
                          </c:otherwise>
                    </c:choose>
					</span>
				</div>
			</nav>

			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Welcome ${adminVO.admin_Name}!</h1>
				</div>
			</div>

			<!-- /.row -->
			<div
				class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
				<h1 class="h2">員工帳戶管理</h1>
				<div class="btn-toolbar mb-2 mb-md-0">
					<div role="tabpanel">
<!-- 						標籤面板：標籤區 -->
						<ul class="nav nav-tabs" role="tablist">
							
							<li onclick="location.href = '<%=request.getContextPath()%>/admin.do?action=getAll_Keyword'" role="presentation"><a aria-controls="tab1"
								role="tab" data-toggle="tab" class="active">查詢員工</a></li>
								
							<li onclick="location.href = '<%=request.getContextPath()%>/back_end/admin/get_all_admin.jsp'" role="presentation"><a aria-controls="tab2" 
								role="tab" data-toggle="tab">所有員工</a></li>
							
							<li onclick="location.href = '<%=request.getContextPath()%>/back_end/admin/add_admin.jsp'"	role="presentation"><a aria-controls="tab3"
								role="tab" data-toggle="tab">新增員工</a></li>
								
							<li onclick="location.href = '<%=request.getContextPath()%>/back_end/admin/update_admin.jsp'" role="presentation"><a aria-controls="tab4"
								role="tab" data-toggle="tab">修改個人資料</a></li>
						</ul>

						<!-- 標籤面板：內容區 -->
						
						
						
							<div role="tabpanel" class="tab-pane active" id="tab3">
								<%-- 錯誤表列 --%>
								<c:if test="${not empty errorMsgs}">
									<font style="color: red">請修正以下錯誤:</font>
									<ul>
										<c:forEach var="message" items="${errorMsgs}">
											<li style="color: red">${message.value}</li>
										</c:forEach>
									</ul>
								</c:if>

								<div background-color="lightblue;">
									<form METHOD="post" action="<%=request.getContextPath()+"/admin.do"%>" 
										name="form1"
										class="fh5co-form animate-box-modal"
										data-animate-effect="fadeIn">

										<div class="form-group">
											<label for="name" class="sr-only">Name</label> 
											<input type="text" name="admin_Name"  value="${param.admin_Name}" class="form-control" 
												placeholder="name" autocomplete="off">
										</div>
										<div class="form-group">
											<label for="account" class="sr-only">Account</label> 
											<input type="text" name="admin_Account" value="${param.admin_Account}" class="form-control" 
												placeholder="account" autocomplete="off">
										</div>
										<div class="form-group">
											<label for="password" class="sr-only">Password</label> 
											<input type="password" name="admin_Password" value="${param.admin_Password}" class="form-control" 
												placeholder="password" autocomplete="off">
										</div>
										<div class="form-group">
											<label for="Email" class="sr-only">Email</label> 
											<input type="text" name="admin_Mail" value="${param.admin_Mail}" class="form-control"
												placeholder="Email" autocomplete="off">
										</div>
										<div class="form-group">
											<label for="Phone" class="sr-only">Phone</label> 
											<input type="text" name="admin_Phone" value="${param.admin_Phone}" class="form-control"
												placeholder="Phone" autocomplete="off">
										</div>

										
										<div class="form_btn"></div>
										<div class="form-group">
											<input type="hidden" name="action" value="insert_Admin" class="btn btn-primary"> 
											<input type="submit" value="Confirm" class="btn btn-primary"> 
											<input type="button" value="Cancel" class="btn btn-primary" onclick="history.back()">
										</div>
										<input type="button" onclick="show()" style="background-color: pink;">
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<!-- /.row -->


		</div>
	</div>

</body>
</html>