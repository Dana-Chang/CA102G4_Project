<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.admin.model.*"%>
<%@ page import="com.mem.model.*"%>


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
	 session.setAttribute("location_Backend","/CA102G4/back_end/admin/manager_member.jsp");
	 response.sendRedirect(request.getContextPath()+"/back_end/admin/back_login.jsp");
	}

	List<MemberVO> list = (List) request.getAttribute("list");
	request.setAttribute("list", list);
	
	MemberService memberSvc = new MemberService();
	pageContext.setAttribute("list", list);
%>

<head>

<div role="tabpanel" class="tab-pane">
	<table class="table">
		<thead>
			<tr>
			<th nowrap="nowrap">會員編號</th>
			<th nowrap="nowrap">會員大頭貼</th>
			<th nowrap="nowrap">會員帳號</th>
			<th nowrap="nowrap">會員姓名</th>
			<th nowrap="nowrap">會員狀態</th>
			<th nowrap="nowrap">會員生日</th>
			<th nowrap="nowrap">會員電話</th>
			<th nowrap="nowrap">會員簡介</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="memberVO" items="${list}">
				<tr>
					<td nowrap="nowrap">${memberVO.mem_Id}</td>
					<td id="showpic"><img src='<%=request.getContextPath()%>/front_end/readPic?action=member&id=${memberVO.mem_Id}'
						id='0'></td>
					<td nowrap="nowrap">${memberVO.mem_Account}</td>
					<td nowrap="nowrap">${memberVO.mem_Name}</td>
					<td>
						<form action="/CA102G4/front_end/member/member.do" method="post">

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

						</form>
												</td>
					<td nowrap="nowrap">${memberVO.mem_Birthday}</td>
					<td nowrap="nowrap">${memberVO.mem_Phone}</td>
					<td>${memberVO.mem_Profile}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</div>
</body>
</html>