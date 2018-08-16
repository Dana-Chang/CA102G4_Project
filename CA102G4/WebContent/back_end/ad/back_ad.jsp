<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ page import="java.util.*" %>
<%@ page import="com.ad.model.*" %>
<%@ page import="com.admin.model.*"%>
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

	AdService adSvc = new AdService();
	List<AdVO> list = adSvc.getAllAD();
	pageContext.setAttribute("list", list);
%>

<!DOCTYPE HTML>
<html>
    <head>
        <title>Travel Maker 後台   - 專欄廣告管理</title>
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
        
        <!-- 自己定義的JS檔案-->
        <script src="<%=request.getContextPath()%>/back_end/js/all/back_all.js"></script>
        
        
        <!-- font字體 -->
        <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
        <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
        <!-- //font字體 -->
		
		<!-- AD頁面自己定義的CSS-->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/back_end/css/ad/back_ad.css">
        <style>

        
        </style>
		<script type="text/javascript">
			//引入廣告簡介資訊，內容太多冗長
			$(function(){
			    var len = 30; // 超過100個字以"..."取代
			    $("tr td.ad_text").each(function(i){
			   	 var temptext=$(this).text().trim().replace('  ', '');
			        if(temptext.length>len){
			            $(this).attr("title",temptext);
			            var text=temptext.substring(0,len-1)+"...";
			            $(this).text(text);
			        }
			    });
			});
			
		</script>
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

                <nav class="navbar"> <!--navbar-expand-lg navbar-light bg-light-->
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
                        <h1 class="page-header">
                        	專欄廣告管理
                        	<button class="btn btn-primary" onclick="location.href='<%=request.getContextPath()%>/back_end/ad/back_addad.jsp'">
                        		<i class="fas fa-plus"></i> 新增廣告
                        	</button>
                        </h1>
                        
                    </div>
                </div>
                
                <div class="row">
                	<div class="col-lg-12" style="text-align:left">
                    	
                	</div>
                </div>
                
                
                <div class="row">
                    <div class="col-lg-12">
                    	<!-- AD頁籤  下架/上架-->
                    	<div class="bs-example-tabs">
                    		<!-- AD頁籤定義 -->
                    		<ul id="adTab" class="nav nav-tabs">
                    			<li class="<%=request.getAttribute("display_tabs") == null ? "active" :"" %>">
                        			<a href="#offContent" data-toggle="tab">
                        				<i class='fas fa-arrow-down'></i>未上架
                        			</a>
                   			 	</li>
                   			 	<li class="<%=request.getAttribute("display_tabs") == null ? "" :"active" %>">
                        			<a href="#onContent" data-toggle="tab">
                        				<i class='fas fa-bell'></i>已上架
                        			</a>
                   			 	</li>
                    		</ul>
                    		<!-- AD頁籤內容定義 -->
                    		<div id="adTabContent" class="tab-content  ui piled segment">
                    			<div id="offContent" class="tab-pane fade <%=request.getAttribute("display_tabs") == null ? "active in" :"" %>">
			                   	<!-- 這邊要放未上架的廣告 -->
			                   		<br>
			                        <table class="table">
			                        	<thead>
			                        		<th>編號</th>
			                        		<th>標題</th>
			                        		<th>簡介</th>
			                        		<th>連結</th>
			                        		<th>圖片</th>
			                        		<th>上架/下架時間</th>
			                        		<th>狀態</th>
			                        		<th>修改</th>
			                        		<th>刪除</th>
			                        	</thead>
			                        	<c:forEach var="advo" items="${list}">
											<c:if test="${advo.ad_Stat == 0}">
												<%@ include file="ad_Content.file" %>
											</c:if>
										</c:forEach>		
			                        </table> 
                    			</div>
                    			
                    			<!-- 已上架的頁籤內容 -->
                    			<div id="onContent" class="tab-pane fade <%=request.getAttribute("display_tabs") == null ? "" :"active in" %>">
                    			<!-- 這邊要放已上架的廣告 -->
			                   		<br>
			                        <table class="table">
			                        	<thead>
			                        		<th>編號</th>
			                        		<th>標題</th>
			                        		<th>簡介</th>
			                        		<th>連結</th>
			                        		<th>圖片</th>
			                        		<th>上架/下架時間</th>
			                        		<th>狀態</th>
			                        		<th>修改</th>
			                        		<th>刪除</th>
			                        	</thead>
			                        	<c:forEach var="advo" items="${list}">
											<c:if test="${advo.ad_Stat == 1}">
												<%@ include file="ad_Content.file" %>
											</c:if>
										</c:forEach>
			                        </table>
                    			</div>
                    		</div>
                    	</div>                   
                    </div>
                </div>

                
            </div>
        </div>
        
    </body>
</html>