package com.android.controller;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import com.admin.model.AdminService;
//import com.admin.model.AdminVO;
import com.mem.model.MemberDAO;
import com.mem.model.MemberService;
import com.mem.model.MemberVO;
import com.mem_report.model.*;
import com.store711.model.Store711Service;
import com.store711.model.Store711VO;
import com.tools.JavaMailSender;

@MultipartConfig()
@WebServlet("/android/MemberServlet")
public class MemberServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// 【檢查使用者輸入的帳號(account) 密碼(password)是否有效】
	// 【實際上應至資料庫搜尋比對】
	private MemberVO allowUser(String mem_Account, String mem_Password) {

		MemberDAO memberdao = new MemberDAO();
		MemberVO memberVO = memberdao.login_Member(mem_Account, mem_Password);
		if (memberVO != null) {
			return memberVO;
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

		// 登出開始
		HttpSession session = req.getSession();

		if ("logout".equals(action)) {
			// session.invalidate();
			session.removeAttribute("login_state");
			session.removeAttribute("memberVO");
			session.removeAttribute("token");
			res.sendRedirect(req.getContextPath() + "/front_end/index.jsp");
		}
		// 登出結束
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		String mem_PhotoReg = "^(jpeg|jpg|bmp|png|gif)$";
		PrintWriter out = res.getWriter();

		Gson gson = new Gson();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("input: " + jsonIn);
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		
		
		
		
		/*
		 * Enumeration enums = req.getParameterNames(); while(enums.hasMoreElements()) {
		 * System.out.println(enums.nextElement()); }
		 */

		if ("login".equals(action)) {
			System.out.println("test123");

			// 【取得使用者 帳號(account) 密碼(password)】
			String mem_Account = jsonObject.get("userId").getAsString();
			String mem_Password = jsonObject.get("password").getAsString();
			Map<String, String> errorMsgs = new HashMap<String, String>();
			System.out.println("test456");

			// 【檢查該帳號 , 密碼是否有效】
			MemberVO memberVO = allowUser(mem_Account, mem_Password);
			if (memberVO == null) {
				// 【帳號 , 密碼無效時】
				writeText(res, "");

			} else {
				// 【帳號 , 密碼有效時, 才做以下工作】
				writeText(res, new Gson().toJson(memberVO));
			}
		}

		if ("insert".equals(action)) { // 來自addMember.jsp的請求
			
			try {
				/*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
				JsonObject member = gson.fromJson(jsonObject.get("member").getAsString(), JsonObject.class);
				String mem_Name = member.get("mem_Name").getAsString();
				System.out.println("接收新增請求");
				// String mem_Account = req.getParameter("mem_id");
				// String mem_idReg = "^[a-zA-Z0-9_]{3,10}$";

				String mem_Account = member.get("mem_Account").getAsString();
				String mem_AccountReg = "[a-zA-Z0-9._%-]+@[a-zA-Z0-9._%-]+.[a-zA-Z]{2,4}";
				MemberDAO dao = new MemberDAO();

				String mem_Password = member.get("mem_Password").getAsString(); // 自動產生亂數密碼寄到信箱
				int count = 0;

				String mem_activecode = UUID.randomUUID().toString().replace("-", "")
						+ UUID.randomUUID().toString().replace("-", "");

				Integer mem_State = 3;
				
				Date mem_Reg_Date = new Date(System.currentTimeMillis());

				File memPhoto = new File(req.getRealPath("/front_end/images/all/puppuy.jpg"));
				BufferedImage profilepicImage = ImageIO.read(memPhoto);
				ByteArrayOutputStream profilepicBaos = new ByteArrayOutputStream();
				ImageIO.write(profilepicImage, "png", profilepicBaos);
				profilepicBaos.flush();
				byte[] mem_Photo = profilepicBaos.toByteArray();
				System.out.println(mem_Photo);
				profilepicBaos.close();

				System.out.println("test2");

				/*************************** 2.開始新增資料 ***************************************/

				MemberService memberSvc = new MemberService();
				memberSvc.addMember(mem_Account, mem_Password, mem_Name, mem_State, mem_Reg_Date, mem_activecode,
						mem_Photo);
				System.out.println("新增成功");
				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				MemberVO memberVO = allowUser(mem_Account, mem_Password);	
				writeText(res, new Gson().toJson(memberVO));
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				
			}
		}

		if ("UPDATE_MEMBER".equals(action)) {
			System.out.println("修改有進來");
			List<String> errorMsgs = new LinkedList<String>();

			req.setAttribute("errorMsgs", errorMsgs);
			byte[] mem_Photo = null;
			ByteArrayOutputStream baos = null;

			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				String mem_Id = req.getParameter("mem_Id");

				String mem_Name = req.getParameter("mem_Name");
				System.out.println("mem_Name" + mem_Name);
				if (mem_Name == null || mem_Name.trim().length() == 0) {
					errorMsgs.add("會員姓名: 請勿空白");
				} else if (mem_Name.trim().length() < 2 || mem_Name.trim().length() > 8) {
					errorMsgs.add("會員姓名：請輸入2~8個字。");
				}
				System.out.println("姓名有");

				String mem_Phone = req.getParameter("mem_Phone");
				// String mem_PhoneReg = "/^09\\d{8}$/";
				if (mem_Phone == null || mem_Phone.trim().length() == 0) {
					errorMsgs.add("會員手機: 請勿空白");
				}
				// else if(!mem_Phone.trim().matches(mem_PhoneReg)) {
				// //以下練習正則(規)表示式(regular-expression)
				// errorMsgs.add("會員手機: 只能是數字, 且長度必需10碼");
				// }
				System.out.println("手機有");

				Integer mem_Sex = Integer.valueOf(req.getParameter("mem_Sex"));
				System.out.println("性別有");

				String mem_Profile = req.getParameter("mem_Profile");
				if (mem_Profile == null || mem_Profile.trim().length() == 0) {
					errorMsgs.add("自我介紹: 請勿空白");
				} else if (mem_Profile.trim().length() > 60) {
					errorMsgs.add("自我介紹：請勿超過60個字。");
				}
				System.out.println("自我介紹有");

				java.sql.Date mem_Birthday = null;
				try {
					mem_Birthday = java.sql.Date.valueOf(req.getParameter("mem_Birthday").trim());
				} catch (IllegalArgumentException e) {
					mem_Birthday = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請選擇日期!");
				}
				System.out.println("生日有");

				Part member_Photo = req.getPart("member_Photo");
				if (getFileNameFromPart(member_Photo) == null) {
					MemberService memberSvc = new MemberService();
					MemberVO memberVO_DB = memberSvc.getOneMember(mem_Id);
					mem_Photo = memberVO_DB.getMem_Photo();
				} else if (!getFileNameFromPart(member_Photo).matches(mem_PhotoReg)) {
					errorMsgs.add("圖片格式不符(.jpg/jpeg/bmp/gif/png)。");
				}
				System.out.println("圖片有");
				System.out.println("mem_Birthday" + mem_Birthday);

				MemberVO memberVO = new MemberVO();

				memberVO.setMem_Id(mem_Id);

				memberVO.setMem_Name(mem_Name);

				memberVO.setMem_Phone(mem_Phone);
				memberVO.setMem_Sex(mem_Sex);
				memberVO.setMem_Birthday(mem_Birthday);
				memberVO.setMem_Profile(mem_Profile);
				memberVO.setMem_Photo(mem_Photo);
				System.out.println("沒改...." + mem_Photo);

				if (!errorMsgs.isEmpty()) {
					req.setAttribute("memberVO", memberVO); // 含有輸入格式錯誤的empVO物件,也存入req
					System.out.println("失敗囉");
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/member/update_mem_profile.jsp");
					failureView.forward(req, res);
					return; // 程式中斷

				}

				/*************************** 2.開始修改資料 *****************************************/
				if (getFileNameFromPart(member_Photo) != null) {
					InputStream is = member_Photo.getInputStream();
					baos = new ByteArrayOutputStream();
					byte[] buf = new byte[is.available()];

					int len = 0;
					while ((len = is.read(buf)) != -1) {
						baos.write(buf, 0, len);
					}
					baos.close();
					is.close();

					mem_Photo = baos.toByteArray();
					System.out.println("圖片修改成功");
				}
				System.out.println("修改快要成功");
				MemberService memberSvc = new MemberService();
				memberSvc.updateMember(mem_Id, mem_Name, mem_Phone, mem_Sex, mem_Birthday, mem_Photo, mem_Profile);

				/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
				System.out.println("****資料庫新增成功 ......後");
				req.setAttribute("memberVO", memberVO); // 資料庫update成功後,正確的的memberVO物件,存入req
				System.out.println(memberVO);
				String url = "/front_end/member/update_mem_profile.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
				successView.forward(req, res);
				System.out.println("大師兄回來了");
				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/member/update_mem_profile.jsp");
				failureView.forward(req, res);
			}
		}

		if ("UPDATE_PASSWORD".equals(action)) { // 來自update_mem_password.jsp的請求
			System.out.println("修改密碼有進來耶");
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				String mem_Account = req.getParameter("mem_Account");

				String mem_Password = req.getParameter("mem_Password");
				if (mem_Password == null || mem_Password.trim().length() == 0) {
					errorMsgs.add("密碼: 請勿空白");
				}

				MemberVO memberVO = new MemberVO();

				memberVO.setMem_Account(mem_Account);

				memberVO.setMem_Password(mem_Password);

				if (!errorMsgs.isEmpty()) {
					req.setAttribute("memberVO", memberVO); // 含有輸入格式錯誤的empVO物件,也存入req
					System.out.println("失敗囉");
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/member/update_mem_password.jsp");
					failureView.forward(req, res);
					return; // 程式中斷

				}
				/*************************** 2.開始修改資料 *****************************************/

				System.out.println("修改快要成功");
				MemberService memberSvc = new MemberService();
				memberSvc.mem_Update_Password(mem_Account, mem_Password);

				/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
				req.setAttribute("MemberVO", memberVO); // 資料庫update成功後,正確的的memberVO物件,存入req
				String url = "/front_end/index.jsp";
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/member/mem_login.jsp"); // 修改成功後,轉交listOneEmp.jsp
				successView.forward(req, res);
				System.out.println("大師兄的密碼回來了");
				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/member/mem_login.jsp");
				failureView.forward(req, res);
			}
		}

		if ("update_State".equals(action)) { // 來自update_mem_password.jsp的請求
			System.out.println("修改狀態有進來耶");
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			req.getParameter("tab");
			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				String mem_Id = req.getParameter("mem_Id");

				Integer mem_State = Integer.valueOf(req.getParameter("mem_State"));

				MemberVO memberVO = new MemberVO();

				memberVO.setMem_Account(mem_Id);

				memberVO.setMem_State(mem_State);

				if (!errorMsgs.isEmpty()) {
					req.setAttribute("memberVO", memberVO); // 含有輸入格式錯誤的empVO物件,也存入req
					System.out.println("失敗囉");
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/manager_member.jsp");
					failureView.forward(req, res);
					return; // 程式中斷

				}
				/*************************** 2.開始修改資料 *****************************************/

				System.out.println("修改快要成功");
				MemberService memberSvc = new MemberService();
				memberSvc.update_State(mem_Id, mem_State);

				/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
				req.setAttribute("MemberVO", memberVO); // 資料庫update成功後,正確的的memberVO物件,存入req
				String url = "/back_end/admin/manager_member.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
				successView.forward(req, res);
				System.out.println("大師兄的密碼回來了");
				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/manager_member.jsp");
				failureView.forward(req, res);
			}
		}

		if ("getAll_member".equals(action)) { // 來自manager_admin.jsp的請求
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				String mem_Name = req.getParameter("mem_Name");
				if (mem_Name == null || (mem_Name.trim()).length() == 0) {
					errorMsgs.add("請輸入要搜尋姓名");
				}

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/manager_admin.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}

				/*************************** 2.開始查詢資料 *****************************************/
				MemberService memberSvc = new MemberService();
				List<MemberVO> list = memberSvc.getAll_member(mem_Name);

				if (list.isEmpty()) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/manager_member.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}
				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
				req.setAttribute("list", list);

				String url = "/back_end/admin/manager_member.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 manager_member.jsp
				successView.forward(req, res);
				System.out.println("轉交成功");
				return;

				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back_end/admin/manager_member.jsp");
				failureView.forward(req, res);
			}
		}

		//////////////////////////////////////// 新增會員檢舉資料以及審核會員資格////////////////////////////////////
		if ("reportMember".equals(action)) {

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String mem_Id_reported = null;
			/*************************** 2.接收請求參數 ***************************************/

			try {
				// 檢舉人MemId
				String mem_Id_report = req.getParameter("mem_Id_report");
				if (mem_Id_report == null || (mem_Id_report.trim().length()) == 0) {
					errorMsgs.add("未取到檢舉人的MemId");
				}

				// 被檢舉人MemId
				mem_Id_reported = req.getParameter("mem_Id_reported");
				if (mem_Id_reported == null || (mem_Id_reported.trim().length()) == 0) {
					errorMsgs.add("未取到被檢舉人的MemId");
				}

				// 被檢舉理由
				String report_Reason = req.getParameter("report_Reason");
				if (report_Reason == null || (report_Reason.trim().length()) == 0) {
					errorMsgs.add("檢舉理由：請勿空白");
				}

				// 設定送出的會員檢舉處理狀態，2為未處理
				Integer mem_Rep_Sta = 2;

				// 先擋下萬一檢舉理由或檢舉人或被檢舉人是空值的狀況
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher(
							"/front_end/personal_area/personal_area_public.jsp?uId=" + mem_Id_reported);
					failureView.forward(req, res);
					return;
				}

				/***************************
				 * 2.開始新增檢舉資料(先檢查是否已有檢舉過，因為不能重複檢舉
				 ****************************/

				Member_reportService member_reportSvc = new Member_reportService();
				Member_reportVO Member_reportVO = member_reportSvc.findByPrimaryKey(mem_Id_report, mem_Id_reported);

				System.out.println("mem_Id_report" + mem_Id_report);
				System.out.println("mem_Id_reported" + mem_Id_reported);

				System.out.println("有新增嗎");

				if (Member_reportVO != null) {
					errorMsgs.add("您已檢舉過此會員，請勿重複檢舉!");
				} else {
					member_reportSvc.addmemberreport(mem_Id_report, mem_Id_reported, report_Reason, mem_Rep_Sta);
					errorMsgs.add("提交檢舉成功!");
					System.out.println("提交檢舉成功!!");
				}

				// 直接轉交 errorMsgs 告知使用者提交成功或失敗
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher(
							"/front_end/personal_area/personal_area_public.jsp?uId=" + mem_Id_reported);
					failureView.forward(req, res);
					return;
				}

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.add("檢舉失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(
						"/front_end/personal_area/personal_area_public.jsp?uId=" + mem_Id_reported);
				failureView.forward(req, res);
			}
		}

		System.out.println("審核有吃到嗎");

		if ("review".equals(action)) {
			System.out.println("審核吃到");

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.接收請求參數 ****************************************/
				String mem_Id_reported = req.getParameter("mem_Id_reported");

				System.out.println("審核照片編號=" + mem_Id_reported);

				String mem_Id_report = req.getParameter("mem_Id_report");

				System.out.println("審核會員編號=" + mem_Id_report);

				/*************************** 2.開始查詢資料 *****************************************/
				Member_reportService member_reportSvc = new Member_reportService();
				Member_reportVO Member_reportVO = member_reportSvc.findByPrimaryKey(mem_Id_reported, mem_Id_report);

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
				req.setAttribute("Member_reportVO", Member_reportVO);
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/member/member_report.jsp");
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得審核資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/photowall/photo_wall.jsp");
				failureView.forward(req, res);
			}
		}

	}
	// 為了判別是否有上傳圖片，故取出檔案名稱

	public String getFileNameFromPart(Part part) {
		String header = part.getHeader("content-disposition");
		System.out.println("header=" + header); // 測試用
		String filename = new File(header.substring(header.lastIndexOf("=") + 2, header.length() - 1)).getName();
		System.out.println("filename=" + filename); // 測試用
		// 取出副檔名
		String fnameExt = filename.substring(filename.lastIndexOf(".") + 1, filename.length()).toLowerCase();
		System.out.println("fnameExt=" + fnameExt); // 測試用
		if (filename.length() == 0) {
			return null;
		}
		return fnameExt;
	}

	private void writeText(HttpServletResponse res, String outText) throws IOException {
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		out.print(outText);
		out.close();
		System.out.println("outText: " + outText);

	}
}
