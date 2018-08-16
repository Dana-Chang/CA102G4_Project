<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.attEdit.model.*"%>
<%@ page import="com.attractions.model.*"%>
<%@ page import="com.admin.model.*"%>
<%@ page import="java.util.*"%>
<% 
	AdminVO adminVO = (AdminVO) session.getAttribute("adminVO");
	if (adminVO == null) {
		adminVO = (AdminVO) session.getAttribute("adminVO");
	}

	boolean login_state = false;
	Object login_state_temp = session.getAttribute("login_state");
	if (login_state_temp != null) {
		login_state = (boolean) login_state_temp;
	}

	if (login_state != true) {
		session.setAttribute("location_Backend", request.getRequestURI());
		 response.sendRedirect(request.getContextPath()+"/back_end/admin/back_login.jsp");
		return;
	}
%>
<jsp:useBean id="attSvc" scope="page"
	class="com.attractions.model.AttractionsService" />

<!DOCTYPE HTML>
<html>
<head>
<title>Travel Maker 後台 - 景點更新審核</title>
<meta charset="UTF-8">
<!-- Bootstrap CSS v3.3.4 -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/all/bootstrap.css">

<!-- 自定義的CSS 包含sidebar與Content-->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/all/back_all.css">

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/attEdit/back_attEditReview.css">

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
                    <li class="active">
                        <a href="<%=request.getContextPath()%>/back_end/back_index.jsp">
                            <i class="fas fa-home"></i> 回首頁
                        </a>
                    </li>
                    
                    <li>
                        <li class="dropdown">
                            <a href="#auth_Submenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                                <i class="fas fa-users"></i>權限管理
                            </a>

                            <ul class="dropdown-menu" id="auth_Submenu">
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/admin/manager_admin.jsp">管理員</a>
                                </li>
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/admin/manager_member.jsp">會員</a>
                                </li>
                            </ul>

                        </li>
                        
                        <li>
                            <a href="<%=request.getContextPath()%>/back_end/news/news.jsp">
                                <i class="fas fa-newspaper"></i>最新消息管理
                            </a>
                        </li>
                        
                        <li>
                            <a href="<%=request.getContextPath()%>/back_end/attEdit/back_attEdit.jsp">
                                <i class="fas fa-image"></i>景點管理
                            </a>
                        </li>
                       
                        <li class="dropdown">
                            <a href="#category_Submenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                                <i class="fas fa-tag"></i>標籤管理
                            </a>
                            <ul class="dropdown-menu" id="category_Submenu">
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/blog/blog_tag.jsp">旅遊記</a>
                                </li>
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/qa_list/qa_list.jsp">問答區</a>
                                </li>
                            </ul>
                        </li>
                        
                        <li class="dropdown">
                            <a href="#report_Submenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle" aria-haspopup="true" >
                                <i class="fas fa-comment-dots"></i>檢舉管理
                            </a>
                            <ul class="dropdown-menu" id="report_Submenu">
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/member/member_report.jsp">會員檢舉</a>
                                </li>
                                <li>
                                    <a href="<%=request.getContextPath()%>/blog.do?action=blogReportManage">旅遊記檢舉</a>
                                </li>
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/qa_report/qa_report.jsp">問答區檢舉</a>
                                </li>
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/photo_wall/photo_report.jsp">照片牆檢舉</a>
                                </li>
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/store/product_report.jsp">商品檢舉</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="<%=request.getContextPath()%>/back_end/ad/back_ad.jsp">
                                <i class="fas fa-audio-description"></i>專欄廣告管理
                            </a>
                        </li>
                        
                        <li class="dropdown">
                            <a href="#aboutUS_Submenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                                <i class="fas fa-briefcase"></i>關於我們管理
                                </a>
                            <ul class="dropdown-menu" id="aboutUS_Submenu">
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/about_us/about_us.jsp">關於我們</a>
                                </li>
                                <li>
                                    <a href="<%=request.getContextPath()%>/back_end/faq/faq.jsp">FAQ</a>
                                </li>
                            </ul>
                        </li>

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
					<span style="font-size:1.5em;margin-right:10px;vertical-align:sub;">Welcome！${adminVO.admin_Name}</span>
						<%= (login_state)? 
                        	"<a href=\"/CA102G4/admin.do?action=logout\"><span class=\"btn btn-info\">登出<i class=\" fas fa-sign-out-alt\" aria-hidden=\"true\"></i></span></a>"
                        	:"<a href=\"/CA102G4/back_login.jsp\"><span class=\"btn btn-info\">登入<i class=\" fas fa-sign-out-alt\" aria-hidden=\"true\"></i></span></a>"%>
					</span>
				</div>
			</nav>

			<!-- Modal區塊  -->
			<form action="<%=request.getContextPath()%>/attEdit/attEdit.do"
				method="post">
				<div class="modal fade" id="editCommit" tabindex="-1" role="dialog"
					aria-labelledby="exampleModalLabel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								審核通過
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body" align="center">
								<h4>確認更新?</h4>
							</div>
							<div class="modal-footer">
								<div align="center">
									<button type="button" class="btn btn-secondary"
										data-dismiss="modal">取消</button>
									<button type="submit" class="btn btn-danger">確定</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" name="attEdit_no"
					value="${attEditVO.attEdit_no}"> <input type="hidden"
					name="action" value="updataCommit">
			</form>

			<form action="<%=request.getContextPath()%>/attEdit/attEdit.do"
				method="post">
				<div class="modal fade" id="editDelete" tabindex="-1" role="dialog"
					aria-labelledby="exampleModalLabel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								審核不通過
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body" align="center">
								<h4>確定刪除此編輯?</h4>
							</div>
							<div class="modal-footer" align="center">

								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">取消</button>
								<button type="submit" class="btn btn-danger">確定</button>
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" name="attEdit_no"
					value="${attEditVO.attEdit_no}"> <input type="hidden"
					name="action" value="updataDelete">
			</form>

			<!-- //Modal區塊  -->
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-12">
					<h1>景點更新審核</h1>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-2 col-2">
					<button type="button" class="btn btn-primary" data-toggle="modal"
						data-target="#editCommit">
						<i class="fas fa-edit"></i> 通過
					</button>
				</div>

				<div
					class="col-lg-2 col-md-2 col-sm-2 col-2 col-lg-offset-8 col-md-offset-8 col-sm-offset-8 col-offset-8">
					<button type="button" class="btn btn-danger" data-toggle="modal"
						data-target="#editDelete">
						<i class="fas fa-edit"></i> 不通過
					</button>
				</div>
			</div>

			<hr>
			<!-- /.row -->

			<h1>${attEditVO.att_name}</h1>
			<h4>(原名稱:${attSvc.getOneAttByPK(attEditVO.att_no).att_name})</h4>
			<div class="img-container">
				<div class="row">
					<div class="col-lg-6 col-md-6 col-sm-6 col-6">
						<div class="polaroid">
							<img
								src="<%= request.getContextPath()%>/trip/getPicture.do?att_no=${attEditVO.att_no}"
								alt="${attEditVO.att_name}" style="width: 100%">
						</div>
					</div>
					<div
						class="col-lg-4 col-md-4 col-sm-4 col-4 col-lg-offset-2 col-md-offset-2 col-sm-offset-2 col-offset-2">
						<div class="table-responsive">
							<table class="table table-bordered table-hover">
								<tr>
									<th>國家</th>
									<td>${attEditVO.country}</td>
								</tr>
								<tr>
									<th>區域</th>
									<td>${attEditVO.administrative_area}</td>
								</tr>
								<tr>
									<th>緯度</th>
									<td>${attEditVO.att_lat}</td>
								</tr>
								<tr>
									<th>經度</th>
									<td>${attEditVO.att_lon}</td>
								</tr>
								<tr>
									<th>地址</th>
									<td>${attEditVO.att_address}</td>
								</tr>
							</table>
						</div>

					</div>
				</div>
			</div>
			<h3>景點介紹</h3>
			<hr>
			<p>
				<c:out value="${attEditVO.att_information}" default="尚無內容"></c:out>
			</p>
			<hr>

			<!-- /.row -->

		</div>
	</div>
</body>
</html>