<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.admin.model.*"%>
<%@ page import="com.mem.model.*"%>



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
	 session.setAttribute("location_Backend","/CA102G4/back_end/back_index.jsp");
	 response.sendRedirect(request.getContextPath()+"/back_end/admin/back_login.jsp");
	}

	MemberService memberSvc = new MemberService();
	List<MemberVO> list = memberSvc.getAll();
	pageContext.setAttribute("list", list);
	
	List<AdminVO> list_one = (List)request.getAttribute("list");
	request.setAttribute("list_one",list_one);
	
	String tab = request.getParameter("tab");
	request.setAttribute("tab", tab);

	
	// 	//付款方式處理
	//     HashMap mem_State = new HashMap();
	//     mem_State.put(1,"正常");
	//     mem_State.put(2,"停權");
	//     mem_State.put(3,"審核中");
	//     pageContext.setAttribute("mem_State",mem_State);
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
			<div
				class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
				<h1 class="h2">會員管理</h1>
				<div class="btn-toolbar mb-2 mb-md-0">
					<div role="tabpanel">
						<!-- 標籤面板：標籤區 -->
						<ul class="nav nav-tabs" role="tablist">

							<li class="active" onclick="location.href = '<%=request.getContextPath()%>/back_end/admin/get_one_member.jsp'" role="presentation"><a aria-controls="tab1"
								role="tab" data-toggle="tab">查詢會員</a></li>
								
							<li onclick="location.href = '<%=request.getContextPath()%>/back_end/admin/get_all_member.jsp'" role="presentation"><a aria-controls="tab2"
								role="tab" data-toggle="tab">顯示所有會員</a></li>
						</ul>

						<!-- 標籤面板：內容區 -->
						<div class="tab-content">
						
						<!-- 查詢所有會員 -->
							<div role="tabpanel" class="tab-pane active" id="tab1">
								<form method="post" action="<%=request.getContextPath() + "/front_end/member/member.do"%>">
									<div class="input-group">
										<input type="text" class="form-control" name="mem_Name" placeholder="搜尋會員 ..." style="height: 46px" />
										<div class="input-group-btn">
											<button class="btn btn-danger btn-lg" type="submit">
												<span class="fas fa-search"></span>
											</button>
											<input type="hidden" name="action" value="getAll_member">
											
										</div>
									</div>
									<jsp:include page="get_one_member.jsp" flush="true" />
								</form>
							</div>
						<!-- 顯示所有會員 -->
							<div role="tabpanel" class="tab-pane" id="tab2"  style="display:none">

								<table class="table">
									<thead>
										<th>會員編號</th>
										<th>會員大頭貼</th>
										<th>會員帳號</th>
										<th>會員姓名</th>
										<th>會員狀態</th>
										<th>會員生日</th>
										<th>會員電話</th>
										<th>會員簡介</th>
									</thead>
									<tbody>
										<c:forEach var="memberVO" items="${list}">
											<tr>
												<td>${memberVO.mem_Id}</td>
												<td id="showpic"><img
													src='<%=request.getContextPath()%>/front_end/readPic?action=member&id=${memberVO.mem_Id}'
													id='0'></td>
												<td>${memberVO.mem_Account}</td>
												<td>${memberVO.mem_Name}</td>
												<td>
													<form action="/CA102G4/front_end/member/member.do" method="get">

														<c:choose>
															<c:when test="${memberVO.mem_State == 2}">
																<input type="hidden" name="mem_State" value="1">
																<button type="submit" class="btn btn-warning">
																	<i class="fas fa-bell"></i>正常
																</button>
															</c:when>
															<c:otherwise>
																<input type="hidden" name="mem_State" value="2">
																<button type="submit" class="btn btn-success">
																	<i class="fas fa-arrow-down"></i>停權
																</button>
															</c:otherwise>
														</c:choose>

														<input type="hidden" name="mem_Id" value="${memberVO.mem_Id}"> 
														<input type="hidden" name="action" value="update_State">
														<input type="hidden" name="tab" value="all_mem">
													</form>
												</td>
												<td>${memberVO.mem_Birthday}</td>
												<td>${memberVO.mem_Phone}</td>
												<td>${memberVO.mem_Profile}</td>

											</tr>
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

	<!-- /.row -->



	<script>
		<script type="text/javascript">
		function $id(id) {
			return document.getElementById(id);
		}
		document.getElementsByName("member_Photo")[0].onchange = function() {
			readURL(this, 0)
		};

		function readURL(input, id) {
			var parent = $id("showpic");
			var child = $id(id);

			if (input.files && input.files[0]) { //確認是否有檔案
				var reader = new FileReader();
				reader.onload = function(e) {
					if (!parent.contains(child)) {
						$id("showpic").innerHTML += "<img src='"+e.target.result+"' id="+id+">";
					} else {
						parent.removeChild(child);
						$id("showpic").innerHTML += "<img src='"+e.target.result+"' id="+id+">";
					}
				}
				reader.readAsDataURL(input.files[0]);
			} else {
				parent.removeChild(child); //必須藉由父節點才能刪除底下的子節點
			}
		}
	</script>
</body>
</html>