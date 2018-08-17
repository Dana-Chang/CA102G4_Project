<%@page import="java.sql.Timestamp"%>
<%@page import="java.sql.Time"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.attEdit.model.*"%>
<%@ page import="com.trip.model.*"%>
<%@ page import="com.admin.model.*"%>
<%@ page import="java.util.*"%>
 
<%
//**********************管理者登入身分驗證********************************//
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
//**********************管理者登入身分驗證********************************//
	
	AttractionsEditService attEditSvc = new AttractionsEditService();
	List<AttractionsEditVO> list = attEditSvc.getAllOrderByDate();
	pageContext.setAttribute("list", list);
%>

<jsp:useBean id="memSvc" scope="page"
	class="com.mem.model.MemberService" />

<!DOCTYPE HTML>
<html>
<head>
<title>Travel Maker 後台 - 景點更新</title>
<meta charset="UTF-8">
<!-- Bootstrap CSS v3.3.4 -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/all/bootstrap.css">

<!-- 自定義的CSS 包含sidebar與Content-->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/all/back_all.css">

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/back_end/css/attEdit/back_attEdit.css">

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
					<h1 class="page-header">景點更新</h1>
				</div>
			</div>
			<!-- /.row -->

			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-12">
					<div class="table-responsive">
						<table class="table table-hover ">
							<thead>
								<tr class="active">
									<th>景點名稱</th>
									<th>編輯者</th>
									<th>編輯時間(年/月/日 時:分:秒)</th>
									<th>審核</th>
								</tr>
							</thead>
							<tbody>
								<%
									int index = 0;
								%>
								<%
									Timestamp attEdit_date;
								%>
								<%@ include file="/back_end/attEdit/include/page1.file"%>
								<c:forEach var="attEditVO" items="${list}"
									begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
									<%
										AttractionsEditVO attEditVO = list.get(index++);
											attEdit_date = new Timestamp(attEditVO.getAttEdit_date().getTime());
											SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
									%>
									<tr>
										<td>${attEditVO.att_name}</td>
										<td>${memSvc.getOneMember(attEditVO.mem_id).mem_Name}(${attEditVO.mem_id})</td>
										<td><%=sdf.format(attEdit_date)%></td>

										<td>
											<FORM METHOD="post"
												ACTION="<%=request.getContextPath()%>/attEdit/attEdit.do"
												style="margin-bottom: 0px;">
												<input type="submit" class="btn btn-warning" value="審核">
												<input type="hidden" name="attEdit_no"
													value="${attEditVO.attEdit_no}"> <input
													type="hidden" name="action" value="reviewOne">
											</FORM>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<%@ include file="/back_end/attEdit/include/page2.file"%>
					</div>
				</div>
			</div>

			<!-- /.row -->

		</div>
	</div>
</body>
</html>