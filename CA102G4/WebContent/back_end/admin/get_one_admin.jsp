<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.admin.model.*"%>


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
	 session.setAttribute("location_Backend","/CA102G4/back_end/admin/manager_admin.jsp");
	 response.sendRedirect(request.getContextPath()+"/back_end/admin/back_login.jsp");
	}

	List<AdminVO> list = (List) request.getAttribute("list");
	request.setAttribute("list", list);
%>

<head>

<div role="tabpanel" class="tab-pane">
	<table class="table">
		<thead>
		<tr>
			<th>員工編號</th>
			<th>姓名</th>
			<th>信箱</th>
			<th>電話</th>
		</tr>
		</thead>
		<tbody>
		<c:if test="${not empty list_one}">
			<c:forEach var="adminVO" items="${list}">
				<tr>
					<td>${adminVO.admin_Id}</td>
					<td>${adminVO.admin_Name}</td>
					<td>${adminVO.admin_Mail}</td>
					<td>${adminVO.admin_Phone}</td>
				</tr>
			</c:forEach>
		</c:if>
		</tbody>
	</table>

</div>
</body>
</html>