<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.admin.model.*, com.photo_report.model.*"%>
<%@ page import="com.mem.model.*"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
		session.setAttribute("location_Backend",request.getRequestURI());
		 response.sendRedirect(request.getContextPath()+"/back_end/admin/back_login.jsp");
		return;
	}
	
	photo_reportService photo_reportSvc = new photo_reportService();
	List<Photo_reportVO> reportList = photo_reportSvc.getAll();
	pageContext.setAttribute("reportList", reportList);
	
	MemberService memberSvc = new MemberService();
	List<MemberVO> list = memberSvc.getAll();
	pageContext.setAttribute("list", list);
	
	List<AdminVO> list_one = (List)request.getAttribute("list");
	request.setAttribute("list_one",list_one);
	
	String tab = request.getParameter("tab");
	request.setAttribute("tab", tab);
	if(tab==null)
		tab="";
	
	// 	//付款方式處理
	//     HashMap mem_State = new HashMap();
	//     mem_State.put(1,"正常");
	//     mem_State.put(2,"停權");
	//     mem_State.put(3,"審核中");
	//     pageContext.setAttribute("mem_State",mem_State);
%>
<jsp:useBean id="memSvc" scope="page" class="com.mem.model.MemberService"/>
<jsp:useBean id="photo_wallSvc" scope="page" class="com.photo_wall.model.Photo_wallService"/>


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

<style>
#showpic>img {
	width: 150px;
	height: 150px;
}



</style>

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
					</ul></li>

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
						<li><a href="<%=request.getContextPath()%>/back_end/photo_wall/photo_report.jsp">照片牆檢舉</a></li>
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
					<h1 class="page-header"></h1>
				</div>
			</div>

			<!-- /.row -->
						<div class="row">
			
				<div class="col-lg-12 col-md-12 col-sm-12 col-12">
				<h1 class="h2">會員檢舉審核</h1>
				<div class="btn-toolbar mb-2 mb-md-0">
					<div role="tabpanel">

						<div class="tab-content">
					<!--顯示檢舉照片牆 -->
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-12">
						
								<div role="tabpanel" class="tab-pane" id="tab1">

									<table class="table table-hover ">
								
									<thead>
										<tr class="active">
										<th>會員編號</th>
										<th>會員姓名</th>
										<th>檢舉時間</th>
										<th>檢舉原因</th>
										<th>審核</th>
										</tr>	
									</thead>
									<tbody>
										<c:forEach var="Photo_reportVO" items="${reportList}">
										<c:if test="${Photo_reportVO.pho_Rep_Stats == 2 }">
											<tr>
												<td>${Photo_reportVO.photo_No}</td>
												<td>${memSvc.findByPrimaryKey(Photo_reportVO.mem_Id).mem_Name}(${Photo_reportVO.mem_Id})</td>
												<td><fmt:formatDate pattern="MM月dd日  HH:mm" value="${Photo_reportVO.report_Time}" /></td>										
												<td></td>
												<td>
													<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/photo_wall.do" style="margin-bottom: 0px;">
												<input type="submit" class="btn btn-warning" value="審核">
												<input type="hidden" name="photo_No" value="${Photo_reportVO.photo_No}"> 
												<input type="hidden" name="mem_Id" value="${Photo_reportVO.mem_Id}"> 
												<input type="hidden" name="action" value="review">
											</FORM>
												</td>
												

											</tr>
											</c:if>
										</c:forEach>
									</tbody>
								</table>

							</div>
</div>
							
						</div>
					</div>


				</div>


			</div>
		</div>
	</div>

	<!-- /.row -->


</body>
</html>