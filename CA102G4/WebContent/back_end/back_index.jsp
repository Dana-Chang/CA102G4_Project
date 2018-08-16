<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.blog_report.model.*,com.blog_message_report.model.*" %>
<%@ page import="com.admin.model.*,com.mem_report.model.*"%>
<%@ page import="com.qa_report.model.*,com.rp_report.model.*"%>
<%@ page import="com.attEdit.model.*"%>
<%@ page import="com.photo_report.model.*" %>
<%@ page import="com.productReport.model.*" %>
<%@ page import="java.util.*" %>
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
	
	//取得檢舉會員(2是未處理)---OK
	Member_reportService memReportSvc = new Member_reportService();
	List<Member_reportVO> memReportList = memReportSvc.getAll();
	int memReportCount = 0 ;
	for(Member_reportVO memReVO : memReportList){
		if(memReVO.getMem_Rep_Sta() == 2){
			memReportCount++;
		}
	}

	//取得商品檢舉(1是未處理)---OK
	ProductReportService productReportSvc = new ProductReportService();
	List<ProductReportVO> proReportList = productReportSvc.getAll();
	int proReportCount = 0 ;
	for(ProductReportVO proReVO : proReportList){
		if(proReVO.getProd_report_status() == 1){
			proReportCount++;
		}
	}
	
	//取得旅遊記檢舉及旅遊記留言檢舉未處理數---OK
	blogMessageReportService blogMRSvc = new blogMessageReportService();
	blogReportService blogRSvc = new blogReportService();
	int blogReportCount = ((blogMRSvc.getBlogMsgReport_ByStatus(0)).size())+(blogRSvc.getBR_BySTATUS(0)).size();
	
	
	//取得景點更新審核數---OK
	AttractionsEditService attrEditSvc = new AttractionsEditService();
	int attrEditCount =(attrEditSvc.getAll()).size();
	
	//取得問答檢舉及問答留言檢舉未處理數(狀態0為未處理)---OK
	int qaReportCount = 0;
	Qa_reportService qaReportSvc = new Qa_reportService();
	List<Qa_reportVO> qaReportList = qaReportSvc.getAll();
	for(Qa_reportVO qa_reportVO :qaReportList){
		if(qa_reportVO.getQa_state() == 0){
			qaReportCount++;
		}
	}
	
	Rp_reportService rpReportSvc = new Rp_reportService();
	List<Rp_reportVO> rpReportList = rpReportSvc.getAll();
	for(Rp_reportVO rp_reportVO :rpReportList){
		if(rp_reportVO.getRp_state() == 0){
			qaReportCount++;
		}
	}
	
	//取得照片牆檢舉未處理數(2未通過)---OK
	photo_reportService photoRSvc = new photo_reportService();
	List<Photo_reportVO> photoReportList = photoRSvc.getAll();
	int photoReportCount =0;
	for(Photo_reportVO photoRVO:photoReportList){
		if(photoRVO.getPho_Rep_Stats() == 2){
			photoReportCount++;
		}
	}

		
%>



<!DOCTYPE HTML>
<html>
    <head>
        <title>Travel Maker 後台</title>
        <meta charset="UTF-8">
        <!-- Bootstrap CSS v3.3.4 -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/back_end/css/all/bootstrap.css">

        <!-- 自定義的CSS 包含sidebar與Content-->
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
                <div class="row">
                    <div class="col-lg-4 col-md-6">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fas fa-user fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge"><%=memReportCount%></div>
                                        <div>會員檢舉</div>
                                    </div>
                                </div>
                            </div>
                            <a href="<%=request.getContextPath()%>/back_end/member/member_report.jsp">
                                <div class="panel-footer">
                                    <span class="pull-left">View Details</span>
                                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                    <div class="clearfix"></div>
                                </div>
                            </a>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="panel panel-success">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fab fa-blogger fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge"><%=blogReportCount%></div>
                                        <div>旅遊記檢舉(含留言)</div>
                                    </div>
                                </div>
                            </div>
                            <a href="<%=request.getContextPath()%>/blog.do?action=blogReportManage">
                                <div class="panel-footer">
                                    <span class="pull-left">View Details</span>
                                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                    <div class="clearfix"></div>
                                </div>
                            </a>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fas fa-question-circle fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge"><%=qaReportCount%></div>
                                        <div>問答區檢舉(含留言)</div>
                                    </div>
                                </div>
                            </div>
                            <a href="<%=request.getContextPath()%>/back_end/qa_report/qa_report.jsp">
                                <div class="panel-footer">
                                    <span class="pull-left">View Details</span>
                                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                    <div class="clearfix"></div>
                                </div>
                            </a>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="far fa-image fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge"><%=attrEditCount%></div>
                                        <div>景點更新</div>
                                    </div>
                                </div>
                            </div>
                            <a href="<%=request.getContextPath()%>/back_end/attEdit/back_attEdit.jsp">
                                <div class="panel-footer">
                                    <span class="pull-left">View Details</span>
                                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                    <div class="clearfix"></div>
                                </div>
                            </a>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="panel panel-success">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fas fa-camera fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge"><%=photoReportCount%></div>
                                        <div>照片牆檢舉</div>
                                    </div>
                                </div>
                            </div>
                            <a href="<%=request.getContextPath()%>/back_end/photo_wall/photo_report.jsp">
                                <div class="panel-footer">
                                    <span class="pull-left">View Details</span>
                                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                    <div class="clearfix"></div>
                                </div>
                            </a>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fas fa-shopping-bag fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge"><%=proReportCount%></div>
                                        <div>商品檢舉</div>
                                    </div>
                                </div>
                            </div>
                            <a href="<%=request.getContextPath()%>/back_end/store/product_report.jsp">
                                <div class="panel-footer">
                                    <span class="pull-left">View Details</span>
                                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                    <div class="clearfix"></div>
                                </div>
                            </a>
                        </div>
                    </div>
                </div>
                <!-- /.row -->

                
            </div>
        </div>
        
    </body>
</html>