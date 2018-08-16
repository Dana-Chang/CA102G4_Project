package com.admin.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.admin.model.AdminDAO;
import com.admin.model.AdminService;
import com.admin.model.AdminVO;



@MultipartConfig()
public class AdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// 管理員登入去資料庫搜尋
	private AdminVO allowUser_Admin(String admin_Account, String admin_Password) {

		AdminDAO admindao = new AdminDAO();
		AdminVO adminVO = admindao.login_Admin(admin_Account, admin_Password);
		if (adminVO != null) {
			return adminVO;
		} else {
			return null;
		}

	}
	// 結束

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
		String action = req.getParameter("action");
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");

		HttpSession session = req.getSession();

		if ("logout".equals(action)) {
//			session.invalidate();
			session.removeAttribute("login_state_backEnd");
			session.removeAttribute("adminVO");
			res.sendRedirect(req.getContextPath() + "/back_end/admin/back_login.jsp");
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		String action = req.getParameter("action");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();
		System.out.println("有進來嗎");

		if ("login_Admin".equals(action)) {
			System.out.println("login_Admin");
			String admin_Account = req.getParameter("admin_Account");
			String admin_Password = req.getParameter("admin_Password");
			Map<String, String> errorMsgs = new HashMap<String, String>();
			System.out.println("test456");

			if ("".equals(admin_Account)) {
				errorMsgs.put("請輸入你的帳號", "test");
			}
			if ("".equals(admin_Password)) {
				errorMsgs.put("請輸入你的密碼", "test");
			}

			System.out.println("test2");

			if (!errorMsgs.isEmpty()) {
				req.setAttribute("errorMsgs", errorMsgs);
				String url = "/back_end/admin/back_login.jsp";
				RequestDispatcher rd = req.getRequestDispatcher(url);
				rd.forward(req, res);
			}

			AdminVO adminVO = allowUser_Admin(admin_Account, admin_Password);
			if (adminVO == null) {
				// 【帳號 , 密碼無效時】
				out.println("<HTML><HEAD><TITLE>Access Denied</TITLE></HEAD>");
				out.println("<BODY>你的帳號 , 密碼無效!<BR>");
				out.println("請按此重新登入</A>");
				out.println("</BODY></HTML>");
			} else {
				// 【帳號 , 密碼有效時, 才做以下工作】
				String admin_Id = adminVO.getAdmin_Id();
				String session_id = session.getId();
				session.setAttribute(session_id, admin_Id); // *工作1: 才在session內做已經登入過的標識

				session.setAttribute("login_state_backEnd", true); // 員工登入的狀態為true

				session.setAttribute("adminVO", adminVO); // session包著員工資料走
				try {
					String location_Backend = (String) session.getAttribute("location_Backend");
					if (location_Backend != null) {
						session.removeAttribute("location_Backend"); // *工作2: 看看有無來源網頁 (-->如有來源網頁:則重導至來源網頁)
						res.sendRedirect(location_Backend);
						return;
					}
				} catch (Exception ignored) {

				}
				res.sendRedirect(req.getContextPath() + "/back_end/back_index.jsp"); // *工作3: (-->如無來源網頁:則重導至首頁)
			}

		}
		//查詢員工
		if ("getAll_Keyword".equals(action)) { // 來自manager_admin.jsp的請求
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			System.out.println("123");
			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				String admin_Name = req.getParameter("admin_Name");
				if (admin_Name == null || (admin_Name.trim()).length() == 0) {
					errorMsgs.add("請輸入要搜尋姓名");
				}

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/manager_admin.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}

				/*************************** 2.開始查詢資料 *****************************************/
				AdminService adminSvc = new AdminService();
				List<AdminVO> list = adminSvc.getAll_keyword(admin_Name);

				if (list.isEmpty()) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/manager_admin.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
				req.setAttribute("list", list);

				String url = "/back_end/admin/manager_admin.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 manger_admin.jsp
				successView.forward(req, res);
				return;
				
				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/manager_admin.jsp");
				failureView.forward(req, res);
			}
		}

		if ("insert_Admin".equals(action)) { // 來自addadmin.jsp的請求

			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/

				String admin_Account = req.getParameter("admin_Account");
				if (admin_Account == null || admin_Account.trim().length() == 0) {
					errorMsgs.put("admin_Account", "帳號: 請勿空白");
				}

				String admin_Password = req.getParameter("admin_Password");
				String admin_PasswordReg = "^(?=.*\\d)(?=.*[a-z]).{6,10}$";
				if (admin_Password == null || admin_Password.trim().length() == 0) {
					errorMsgs.put("admin_Password", "密碼: 請勿空白");
				}

				String admin_Name = req.getParameter("admin_Name");
				String admin_NameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
				if (admin_Name == null || admin_Name.trim().length() == 0) {
					errorMsgs.put("admin_Name", "姓名: 請勿空白");
				}
				
				String admin_Mail = req.getParameter("admin_Mail");
				String admin_MailReg = "[\\w-.]+@[\\w-]+(.[\\w_-]+)+";
				if (admin_Mail == null || admin_Mail.trim().length() == 0) {
					errorMsgs.put("admin_Mail", "信箱請勿空白");
				} else if (!admin_Mail.trim().matches(admin_MailReg)) {
					errorMsgs.put("admin_Mail", "請輸入Email正確格式");
				}
				String admin_Phone = req.getParameter("admin_Phone");
				if (admin_Phone == null || admin_Phone.trim().length() == 0) {
					errorMsgs.put("admin_Phone", "手機請勿空白");
				}

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/add_admin.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 2.開始新增資料 ***************************************/
				AdminService adminSvc = new AdminService();
				adminSvc.insert_Admin(admin_Account, admin_Password, admin_Name, admin_Mail, admin_Phone);
				System.out.println("新增成功");
				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				String url = "/back_end/admin/add_admin.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交到網站首頁
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.put("Exception", e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/add_admin.jsp");
				failureView.forward(req, res);
			}
		}
		//修改員工個人資料
		if ("update_Admin".equals(action)) { // 來自update_admin.jsp的請求
        	System.out.println("修改有進來");
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String admin_Id = req.getParameter("admin_Id");

				String admin_Password = req.getParameter("admin_Password");
				if (admin_Password == null || admin_Password.trim().length() == 0) {
					errorMsgs.add("密碼請勿空白");
				}
				
				String admin_Name = req.getParameter("admin_Name");
				if (admin_Name == null || admin_Name.trim().length() == 0) {
					errorMsgs.add("姓名請勿空白");
				}else if(admin_Name.trim().length()<2||admin_Name.trim().length()>8){
					errorMsgs.add("姓名：請輸入2~8個字。");
				} 
				System.out.println("姓名有");
				
				String admin_Mail = req.getParameter("admin_Mail");
				if (admin_Mail == null || admin_Mail.trim().length() == 0) {
					errorMsgs.add("信箱請勿空白");
				} 
				String admin_Phone = req.getParameter("admin_Phone");
				if (admin_Phone == null || admin_Phone.trim().length() == 0) {
					errorMsgs.add("手機請勿空白");
				} 

				AdminVO adminVO = new AdminVO();
				adminVO.setAdmin_Id(admin_Id);
				adminVO.setAdmin_Password(admin_Password);
				adminVO.setAdmin_Name(admin_Name);
				adminVO.setAdmin_Mail(admin_Mail);
				adminVO.setAdmin_Phone(admin_Phone);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("adminVO", adminVO); // 含有輸入格式錯誤的adminVO物件,也存入req
					System.out.println("失敗囉");
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back_end/admin/update_admin.jsp");
					failureView.forward(req, res);
					return; //程式中斷
					
				}
				
				/***************************2.開始修改資料*****************************************/
				
				System.out.println("修改快要成功");
				AdminService adminSvc = new AdminService();
				adminSvc.upadte_Admin(admin_Id,admin_Password,admin_Name,admin_Mail,admin_Phone);
				
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				System.out.println("****資料庫新增成功 ......後");
				req.setAttribute("adminVO", adminVO); // 資料庫update成功後,正確的的adminVO物件,存入req
				System.out.println(adminVO);
				String url = "/back_end/admin/update_admin.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
				successView.forward(req, res);
				System.out.println("大師兄回來了");
				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/admin/update_admin.jsp");
				failureView.forward(req, res);
			}
		}

	}

}
