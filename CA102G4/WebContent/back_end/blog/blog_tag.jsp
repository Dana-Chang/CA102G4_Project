<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ page import="java.util.*" %>
<%@ page import="com.blog_tag_name.model.*"%>
<%@ page import="com.admin.model.*"%>
<% 
	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-store"); 
	response.setDateHeader("Expires", 0);
	request.setCharacterEncoding("UTF-8");
	
	blogTagNameService blogTagNameSvc = new blogTagNameService();
	List<blog_tag_nameVO> list = blogTagNameSvc.getAll();
	request.setAttribute("list",list);
	
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
	
	long token=System.currentTimeMillis();
	session.setAttribute("token", token);
%>

<!DOCTYPE HTML>
<html> 
    <head>
        <title>Travel Maker 後台   - 旅遊記標籤管理</title>
        <meta charset="UTF-8">
        <!-- Bootstrap CSS v3.3.4 -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/back_end/css/all/bootstrap.css">

        <!-- 自定義的CSS -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/back_end/css/all/back_all.css">

        <!-- Font Awesome JS -->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">


        <!-- jQuery CDN - Slim version (=without AJAX) -->
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>

        <!-- Bootstrap JS v3.3.4-->
        <script src="<%=request.getContextPath()%>/back_end/js/all/bootstrap.js"></script>
        
        <!-- AD自己定義的JS檔案-->
        <script src="<%=request.getContextPath()%>/back_end/js/all/back_all.js"></script>
        <!-- //AD自己定義的JS檔案-->
        
        <!-- font字體 -->
        <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
        <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
        <!-- //font字體 -->

		<!-- blogTag頁面自己定義的CSS-->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/back_end/css/blog/blog_tag.css">
        <!-- //blogTag頁面自己定義的CSS-->
        
        <!-- blogTag自己定義的JS檔案-->
        <script src="<%=request.getContextPath()%>/back_end/js/blog/blog_tag.js"></script>
        <!-- //blogTag自己定義的JS檔案-->
        
        <!-- blog使用到的jQuery Dialog -->
    	<link rel="stylesheet" href="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.css">
    	<script src="<%=request.getContextPath()%>/front_end/jquery-ui-1.12.1/jquery-ui.js"></script>
    	<!-- //blog使用到的jQuery Dialog -->
    	
    	<!-- semantic-->
    	<link rel="stylesheet" href="<%=request.getContextPath()%>/back_end/js/blog/UI-Menu-master/menu.min.css">
        <!-- //semantic-->
    
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
			     <span style="font-size:1.5em;margin-right:10px;vertical-align:sub;">
			         Welcome！${adminVO.admin_Name}
			     </span>
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
                        <h1 class="page-header">
                        	旅遊記標籤管理
                        	<button class="btn btn-primary insertTagBtn">
                        		<i class="fas fa-plus"></i> 新增標籤
                        	</button>
                        </h1>
                        
                    </div>
                </div>
                             
                <div class="row">
                    <div class="col-lg-12">
                    		<div id="adTabContent" class="tab-content  ui piled segment">
                    			<div id="offContent" class="tab-pane fade active in">
			                   		<br>
			                        <table class="table">
			                        	<thead>
			                        		<th>編號</th>
			                        		<th>類別</th>
			                        		<th>名稱</th>
			                        		<th>修改</th>
			                        		<th>刪除</th>
			                        	</thead>
			                        	
			                        	<%@ include file="page1.file"%>
			                        	<c:forEach var="blogTagNameVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
                						
                                    
                                    	<tbody>
                                            <tr>
                                                <td><p class="tagID">${blogTagNameVO.btn_id}</p></td>
                                                <td><p class="tagClass">${blogTagNameVO.btn_class}</p></td>
                                                <td><p class="tagName">${blogTagNameVO.btn_name}</p></td>	
                                                <td>
                                                    <FORM action="<%=request.getContextPath()%>/blog.do" method="post">
                                                        <input type="hidden" name="btn_id" value="${blogTagNameVO.btn_id}">
                                                        <input type="hidden" name="action" value="updateTagName">
                                                        <input type="hidden" class="btn_class" name="btn_class" value="${blogTagNameVO.btn_class}">
                                                        <input type="hidden" class="btn_name" name="btn_name" value="${blogTagNameVO.btn_name}">
                                                        <button type='button' class='btn btn-primary updateTagBtn' value='updateTag'>修改</button>
                                                    </FORM>
                                                </td>
                                                <td>
		                                            <FORM class="deleteTagForm" METHOD="post" ACTION="<%=request.getContextPath()%>/blog.do">
                                                        <input type="hidden" name="btn_id" value="${blogTagNameVO.btn_id}">
                                                        <input type="hidden" name="action" value="delTagName">
    													<input type="hidden" name="whichPage" value="${param.whichPage}">
                                                        <button type='button' class='btn btn-danger' value='deleteTag'>刪除</button>
                                                    </FORM>
                                                </td>
                                            </tr>	

                                        </tbody>
                                    
                                    
										</c:forEach>
                                	
			                        </table> 
                    			</div>
							</div>
						</div>
					</div>
					
						<!-- 上一頁button -->
						<%@ include file="page2.file"%>
						<!-- //下一頁button -->
						<!-- 頁數資訊 -->
						<div class="page_info">
							顯示第<%= whichPage %>頁，共<%= pageNumber %>頁
						</div>
						<!-- //頁數資訊 -->

               
                
            </div>
        </div>
        
        <div id="updateTagDialog">
    		<div class="updateContent">
    		<div class="updateContentTitle">修改標籤類別/名稱：</div>
    			<form class="ui updateContent form" METHOD="POST" ACTION="<%=request.getContextPath()%>/blog.do">
    			<div class="updateFormContent">編號：<p class="btn_id"></p></div>
    			<div class="updateFormContent">類別：<input type="text" id="btn_class" name="blogTagNameClass" value="" maxlength="15"></div>
    			<div class="updateFormContent">名稱：<input type="text" id="btn_name" name="blogTagName" value=""  maxlength="15"></div>
    			<input type="hidden" name="action" value="updateTag">
    			<input type="hidden" id="btn_id" name="blogTagNameID" value="">
    			<input type="hidden" name="whichPage" value="${param.whichPage}">
    			</form>
    		</div>
    	</div>
    	
    	<div id="deleteTagData">
			<div>你確定要刪除標籤嗎?</div>
		</div>
		
		<div id="insertTagDialog">
    		<div class="insertContent">
    		<div class="insertContentTitle">新增標籤：</div>
    			<form class="ui insertContent form" METHOD="POST" ACTION="<%=request.getContextPath()%>/blog.do">
    			<div class="insertFormContent">類別：<input type="text" id="insert_btn_class" name="blogTagNameClass" value="" maxlength="15"></div>
    			<div class="insertFormContent">名稱：<input type="text" id="insert_btn_name" name="blogTagName" value=""  maxlength="15"></div>
    			<input type="hidden" name="action" value="insertTag">
    			<input type="hidden" name="whichPage" value="${param.whichPage}">
    			<input type="hidden" name="token" value="<%=token%>">
    			</form>
    		</div>
    	</div>
    	
    </body>
</html>