<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.*"%>
<%@ page import="com.admin.model.*"%>
<%@ page import="com.mem.model.*"%>
<%@ page import="com.photo_report.model.*, com.photo_wall.model.*"%>


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
	 session.setAttribute("location_Backend", request.getRequestURI());
	 response.sendRedirect(request.getContextPath()+"/back_end/admin/back_login.jsp");
	}
	
	String photo_No = request.getParameter("photo_No");
	String mem_Id = request.getParameter("mem_Id");
	
	Photo_wallService photo_wall_reviewSvc = new Photo_wallService();
	Photo_wallVO photo_wallVO = photo_wall_reviewSvc.findByPrimaryKey(photo_No);
	pageContext.setAttribute("photo_wallVO", photo_wallVO);
	
	MemberService memberSvc = new MemberService();
	MemberVO memberVO = memberSvc.findByPrimaryKey(mem_Id);
	pageContext.setAttribute("memberVO", memberVO);
	
	
// 	photo_reportService photo_reportSvc = new photo_reportService();
// 	List<Photo_reportVO> reportList = photo_reportSvc.getAll();
// 	pageContext.setAttribute("reportList", reportList);


	List<AdminVO> list_one = (List) request.getAttribute("list");
	request.setAttribute("list_one", list_one);

	// 	//付款方式處理
	//     HashMap mem_State = new HashMap();
	//     mem_State.put(1,"正常");
	//     mem_State.put(2,"停權");
	//     mem_State.put(3,"審核中");
	//     pageContext.setAttribute("mem_State",mem_State);
%>
<jsp:useBean id="memSvc" scope="page" class="com.mem.model.MemberService" />
<jsp:useBean id="photo_wallSvc" scope="page" class="com.photo_wall.model.Photo_wallService" />
<jsp:useBean id="photo_reportSvc" scope="page" class="com.photo_report.model.photo_reportService" />


<head>
<title>Travel Maker 後台</title>
<meta charset="UTF-8">
<!-- Bootstrap CSS v3.3.4 -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/all/bootstrap.css">

<!-- 自定義的CSS 包含sidebar與Content-->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/all/back_all.css">
	
	<!-- photowall 自定義的css -->
<link
	href="<%=request.getContextPath()%>/front_end/css/photo_wall/photowall.css" rel="stylesheet" type="text/css">
<!-- //photowall 自定義的css -->
	
	<!-- view_photowall 自定義的css -->
<link
	href="<%=request.getContextPath()%>/front_end/css/photo_wall/view_photowall.css" rel="stylesheet" type="text/css">
<!-- //view_photowall 自定義的css -->

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

.reason_content{
    width: auto;
    height: 122px;
}

.mem_photo{
	margin:0px;
	border-radius:50%;
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
					href="<%=request.getContextPath()%>/back_end/back_index.jsp"> 
					<i class="fas fa-home"></i> 回首頁
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
					<c:choose>
							<c:when test="<%=login_state_backEnd%>">
								<a href="<%=request.getContextPath()%>/admin.do?action=logout"><span
									class=" top_banner btn btn-info"><i
										class=" fas fa-sign-out-alt" aria-hidden="true"></i></span></a>
							</c:when>
							<c:otherwise>
								<a href="<%=request.getContextPath()%>/admin_login.jsp"><span
									class="top_banner btn btn-info"><i class=" fa fa-user"
										aria-hidden="true"></i></span></a>
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
			
			
			<!--審核區 -->
			<form action="<%= request.getContextPath()%>/photo_wall.do" method="post">
				<div class="modal fade" id="editCommit" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								審核通過
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body" align="center">
								<h4>確認通過?</h4>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
								<button type="submit" class="btn btn-danger">確定</button>
							</div>
						</div>
					</div>
				</div>
<!-- 			審核通過 照片牆的狀態(photo_Sta)會改成1(正常) 照片牆的審核狀態(pho_Rep_Stats)會改成1(已審核) -->
				<input type="hidden" name="mem_Id" value="${memberVO.mem_Id}"> 
				<input type="hidden" name="photo_No" value="${photo_wallVO.photo_No}"> 
				<input type="hidden" name="photo_Sta" value="1">
				<input type="hidden" name="pho_Rep_Stats" value="1">
				<input type="hidden" name="action" value="update_State">
			</form>
			
			<form action="<%= request.getContextPath()%>/photo_wall.do" method="post">
				<div class="modal fade" id="editDelete" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								審核不通過
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body" align="center">
								<h4>確定不通過?</h4>
							</div>
							<div class="modal-footer">
		
								<button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
								<button type="submit" class="btn btn-danger">確定</button>
							</div>
						</div>
					</div>
				</div>
<!--			審核不通過 照片牆的狀態(photo_Sta)會改成2(隱藏) 照片牆的審核狀態(pho_Rep_Stats)會改成1(已審核) -->
				<input type="hidden" name="mem_Id" value="${memberVO.mem_Id}"> 
				<input type="hidden" name="photo_No" value="${photo_wallVO.photo_No}">
				<input type="hidden" name="photo_Sta" value="2">
				<input type="hidden" name="pho_Rep_Stats" value="1">
				<input type="hidden" name="action" value="update_State">
				
			</form>
			<!--審核區 -->
			
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-12">
					<h1>
						照片牆更新審核
					</h1>
				</div>
			</div>
			<div class="row" style="margin-left: 1px; margin-top: 10px;">
			
				<div style="display: inline-block">
					<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#editCommit">
						<i class="fas fa-edit"></i> 通過
					</button>
				</div>
				
				<div style="display: inline-block">
					<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#editDelete">
						<i class="fas fa-edit"></i> 不通過
					</button>
				</div>
			</div>
			
			
			<!-- /.row -->
			<div class="row">
				<div class="container">
					<div class="card">
						<div class="container-fliud">
							<div class="wrapper row">
								<div class="preview col-md-6">
									<div class="preview-pic tab-content">

										<div class="preview-pic tab-content">
											<img src="data:image/*;base64,${photo_wallVO.encoded}">
										</div>

									</div>
								</div>
								<div class="details col-md-6">
									<div class="profile_all">
										<div class="profile_photo">
											<img class="mem_photo" src="data:image/*;base64,${memberVO.encoded}" width="30"
												height="30">

										</div>
										<div class="profile_photo_hr">
											<a href="#" class="product-title">${memberVO.mem_Name}</a>
										</div>
									</div>
									<hr class="hr_setting">
									<div class="rating">
										<div class="icon_all">
										</div>
										<div>
										<div class="reason_content">
										
											<table>
											<tr><th>我的標籤</th></tr>	
												<tr>
													<td style="max-width:330px;"><br>${photo_wallVO.photo_Content}</td>
												</tr>
											</table>
											</div>
											<hr class="hr_setting">
											<div class="reason_content">
											<table>
											<tr><th>檢舉原因</th></tr>	
												<tr>
													<td style="max-width:330px;"><br>${photo_reportVO.report_Reason}</td>
												</tr>								
											</table>
											</div>
											</div>
											<hr class="hr_setting">											
										</div>
									</div>
									<div class="action"></div>
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