//package com.mem_login.controller;
//
//import java.io.*;
//import javax.servlet.*;
//import javax.servlet.http.*;
//import com.mem.model.*;
//
//
//import javax.servlet.annotation.WebServlet;
//
//@WebServlet("/member/LoginLogoutHandler")
//public class LoginLogoutHandler extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//   //【檢查使用者輸入的帳號(account) 密碼(password)是否有效】
//   //【實際上應至資料庫搜尋比對】
//  private MemberVO allowUser(String mem_Account, String mem_Password) {
//	  
//	  MemberDAO memberdao = new MemberDAO(); 
//	  MemberVO memberVO = memberdao.login_Member(mem_Account,mem_Password);
//    if (memberVO != null) {
//        return memberVO;
//      }else {
//      return null;
//      }
//  }
//  
//  public void doPost(HttpServletRequest req, HttpServletResponse res)
//                                throws ServletException, IOException {
//    req.setCharacterEncoding("UTF-8");
//    res.setContentType("text/html; charset=UTF-8");
//    PrintWriter out = res.getWriter();
//    
//    String action = req.getParameter("action");
//    HttpSession session = req.getSession();
//    // 【取得使用者 帳號(account) 密碼(password)】
//    String mem_Account = req.getParameter("mem_Account");
//    String mem_Password = req.getParameter("mem_Password");
//
//    // 【檢查該帳號 , 密碼是否有效】
//    MemberVO memberVO = allowUser(mem_Account,mem_Password);
//    if (memberVO == null) {          //【帳號 , 密碼無效時】
//      out.println("<HTML><HEAD><TITLE>Access Denied</TITLE></HEAD>");
//      out.println("<BODY>你的帳號 , 密碼無效!<BR>");
//      out.println("請按此重新登入 </member/mem_login.jsp>重新登入</A>");
//      out.println("</BODY></HTML>");
//    }else {		//【帳號 , 密碼有效時, 才做以下工作】
//      String member_Id = memberVO.getMem_Id();
////      int is_manager = memberVO.getIs_manager();
//      String session_id = session.getId();
//      session.setAttribute(session_id, member_Id);  //*工作1: 才在session內做已經登入過的標識
////      session.setAttribute("is_manager", is_manager); 
//      
//       try {                                                        
//         String location = (String) session.getAttribute("location");
//         if (location != null) {
//           session.removeAttribute("location");   //*工作2: 看看有無來源網頁 (-->如有來源網頁:則重導至來源網頁)
//           res.sendRedirect(location);            
//           return;
//         }
//       }catch (Exception ignored) { 
//    	   
//       }
////       res.sendRedirect("https://www.google.com/");
//      res.sendRedirect(req.getContextPath()+"/front_end/index.jsp");  //*工作3: (-->如無來源網頁:則重導至login_success.jsp)
//    }
//  }
////  if("logout".equals(action)) {
////	  session.invalidate();
////  }
////}
//}