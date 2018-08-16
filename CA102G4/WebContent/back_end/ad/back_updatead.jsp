<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	//**********************管理者登入身分驗證********************************//

	AdVO advo=(AdVO) request.getAttribute("adVO");
%>


<!DOCTYPE HTML>
<html>
    <head>
        <title>Travel Maker 後台</title>
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
        
        <style>
        	.breadcrumb {
        		font-size:2em;
        		background-color:inherit;
        	}
        	
        	.row table{
        		margin-left:20px;
        	}
        	.row tr{
        		height:60px;
        	}
        	
        	.row th{
        		font-size:1em;
        	}
        	.row th > span{
        		color:red;
        	}
        	
        	input[type="text"]{
        		width:100%;
        	}
			textarea{
				resize: none;
			}
			
			#showpic > img{
				width:50%;
				height:50%;
			}
        </style>

		<!-- 日期時間選擇 -->
		<link rel="stylesheet" href="<%=request.getContextPath()%>/back_end/css/ad/jquery.datetimepicker.css">
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-datetimepicker/2.5.20/jquery.datetimepicker.full.min.js"></script>
		
		<script>

		$(document).ready(function(){			
			$('#datetimepicker_AddTime').datetimepicker({
// 				value:new Date(),
				format:'Y-m-d H:i:00',
// 				theme:'dark',
				minDate:0,
				minTime:0,
				step:5,
			});
			$('#datetimepicker_OffTime').datetimepicker({
				format:'Y-m-d H:i:00',
				minDate:0,
				minTime:0,
				step:15,		
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
                        <nav aria-label="breadcrumb">
						  <ol class="breadcrumb page-header" >
						    <li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/back_end/ad/back_ad.jsp">專欄廣告管理</a></li>
						    <li class="breadcrumb-item active" aria-current="page">修改廣告</li>
						  </ol>
						</nav>
                    </div>
                </div>
                
                
                <div class="row">
                	<div class="col-lg-4" style="position: static;">
					<form method="post" action="<%=request.getContextPath()%>/ad.do" name="form1" enctype="multipart/form-data">
						<table>
							<tr>
								<th><span>*</span>廣告編號：</th>
								<td>
									<%=(advo == null ) ? "" : advo.getAd_ID() %>
								</td>
							</tr>
							<tr>
								<th><span>*</span>廣告標題：</th>
								<td>
									<input type="text" name="ad_Title" class="form-control" value="<%=(advo == null ) ? "" : advo.getAd_Title() %>"/>
								</td>
							</tr>
							<tr>
								<th><span>*</span>廣告簡介：</th>
								<td>
									<textarea name="ad_Text" rows="5" cols="50" class="form-control"><%=(advo == null) ? "" : advo.getAd_Text() %></textarea>
								</td>
							</tr>
							<tr>
								<th><span>*</span>廣告連結：</th>
								<td>
									<input type="text" name="ad_Link" class="form-control" value="<%=(advo == null) ? "":advo.getAd_Link() %>"/>
								</td>
							</tr>
							<tr>
								<th><span>*</span>廣告圖片：</th>
								<td>
       								<input type="button" id="btn" value="選擇要更新的圖片" onclick="$('#fileinp').click();">
       								<input type="file" id="fileinp" name="ad_Pic" accept="image/*" style="display: none; visibility: hidden;">
       								<span style="color:green" id="fileText">目前資料庫已有圖片</span>
								</td>
							</tr>
							<tr>
								<th><span>*</span>預計上架時間：</th>
								<td>
									<input type="text" id="datetimepicker_AddTime"  name="ad_PreAddTime" 
										readonly value="<%=(advo == null) ? "":advo.getAd_PreAddTime() %>">
								</td>
							</tr>
							<tr>
								<th>預計下架時間：</th>
								<td>
									<input type="text" id="datetimepicker_OffTime"  name="ad_PreOffTime" 
										readonly value="<%=(advo == null) ? "":(advo.getAd_PreOffTime()==null ? "" : advo.getAd_PreOffTime()) %>">
								</td>
							</tr>
							<tr>
								<td>
									<input type="hidden" name="action" value="update">
									<input type="hidden" name="ad_ID" value="<%=(advo == null ) ? "" : advo.getAd_ID() %>">
									<input type="submit" value="送出" class="btn btn-success">
								</td>
							</tr>
						</table>
					</form>
					
					<%-- 錯誤表列 --%>
					<c:if test="${not empty errorMsgs}">
						<div class="modal fade" id="errorModal">
						    <div class="modal-dialog modal-sm" role="dialog">
						      <div class="modal-content">
						        <div class="modal-header">
						          <i class="fas fa-exclamation-triangle"></i>
						          <span class="modal-title"><h4>請修正以下錯誤:</h4></span>
						        </div>
						        <div class="modal-body">
									<c:forEach var="message" items="${errorMsgs}">
										<li style="color:red" type="square">${message}</li>
									</c:forEach>
						        </div>
						        <div class="modal-footer">
						          <button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
						        </div>
						      </div>
						    </div>
						 </div>
					</c:if>
					<%-- 錯誤表列 --%>
					
					</div>
					<div class="col-lg-8" id="showpic">
						<!-- 顯示上傳圖片的區塊 -->
						<img src="<%=request.getContextPath()%>/ADPicReader?AD_ID=<%=advo.getAd_ID()%>" id='0' >
					</div>
                </div>

                
            </div>
        </div>
           <script type="text/javascript">
           /*若有錯誤訊息時，就會跳出視窗.......*/
          	$('#errorModal').modal();
       	
           /*為了圖片的顯示................*/
        	function $id(id){
        		return document.getElementById(id);
        	}
        	document.getElementsByName("ad_Pic")[0].onchange=function(){readURL(this,0)};
        	
       	   function readURL(input,id){
       	        var parent = $id("showpic");
       	        var child  = $id(id);
       	        
       	        if(input.files && input.files[0]){ //確認是否有檔案
       	            var reader = new FileReader();
       	            reader.onload=function(e){
       	            	if(!parent.contains(child)){
       	                	$id("showpic").innerHTML+= "<img src='"+e.target.result+"' id="+id+">";
       	            	}else{
       	            		parent.removeChild(child);
       	            		$id("showpic").innerHTML+= "<img src='"+e.target.result+"' id="+id+">";
       	            	}
       	            }
       	            reader.readAsDataURL(input.files[0]);
       	        }else{
       	            parent.removeChild(child); //必須藉由父節點才能刪除底下的子節點
       	        }
       	    }
       	   
       	    //修改廣告時，更改上傳檔案的名稱
	       	$("#fileinp").change(function () {
	       		if($("#fileinp").val() == ""){
	       			$("#fileText").css("color","green");
	       			$("#fileText").html("不更新資料庫圖片喔!");
	       			$id("showpic").innerHTML+= "<img src='<%=request.getContextPath()%>/ADPicReader?AD_ID=<%=advo.getAd_ID()%>' id='0' >";
	       		}else{
	       			$("#fileText").css("color","red");
	       			$("#fileText").html("圖片路徑更新："+$("#fileinp").val());
	       		}
	            
	        })
	        
	        
        	
        </script>
    </body>
</html>