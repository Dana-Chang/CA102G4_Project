package com.photo_wall.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
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

import com.blog_report.model.blogReportService;
import com.blog_report.model.blog_reportVO;
import com.mem.model.MemberService;
import com.mem.model.MemberVO;
import com.photo_report.model.Photo_reportVO;
import com.photo_report.model.photo_reportService;
import com.photo_tag.model.Photo_tagService;
import com.photo_tag.model.Photo_tagVO;
import com.photo_wall.model.Photo_wallService;
import com.photo_wall.model.Photo_wallVO;
import com.photo_wall_like.model.photo_wall_likeService;

@MultipartConfig(maxFileSize = 50 * 1024 * 1024, maxRequestSize = 50 * 50 * 1024 * 1024)

public class Photo_wallServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		String action = req.getParameter("action");
		System.out.println("start");
		
		/*************************** 按讚照片 ********************************/
		if ("collect".equals(action)) {
			System.out.println("有進來喜歡嗎?");
			res.setContentType("text/html;charset=UTF-8");
			PrintWriter out = res.getWriter();
			try {

				/*************************** 1.接收請求參數 ***************************************/

				String photo_No = req.getParameter("photo_No");
				// 這裡要從session取得登入會員的ID
				String mem_Id = req.getParameter("mem_Id");

				/*************************** 2.開始增加喜歡次數 ***************************************/

				photo_wall_likeService photo_wallSvc = new photo_wall_likeService();
				
				int cnt = photo_wallSvc.findByPrimaryKey(mem_Id, photo_No);

				if (cnt == 0) {
					photo_wallSvc.addphoto_wall_like(mem_Id, photo_No);
					out.print(" ");
				} else {
					photo_wallSvc.deletephoto_wall_like(mem_Id, photo_No);
					out.print(" ");
				}

				/*************************** 3.收藏完成，準備轉交(Send the Success view) ***********/

				// 此為AJAX寫法，不用轉交

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				out.print("收藏失敗");
			}
		}
		
		
		if("reportPhoto".equals(action)) {

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String photo_No = null;
			/*************************** 2.接收請求參數  ***************************************/

			try {
				
			photo_No = req.getParameter("photo_No");	

			String mem_Id = req.getParameter("mem_Id");
		
			String report_Reason = req.getParameter("report_Reason");
				
			Timestamp report_Time = new Timestamp(System.currentTimeMillis());
						
			Integer pho_Rep_Stats = 2;

			
			/*************************** 2.開始新增檢舉資料  ***************************************/
			
			photo_reportService photo_reportSvc = new photo_reportService();
			Photo_reportVO photo_reportVO = photo_reportSvc.findByPrimaryKey(photo_No, mem_Id);
			
			System.out.println(photo_No);
			System.out.println(mem_Id);

			System.out.println("有新增嗎");
			
			if(photo_reportVO!=null) {
				errorMsgs.add("您已檢舉過此篇文章!");
			}else {
				photo_reportSvc.addphotoreport(photo_No, mem_Id,report_Reason,report_Time,pho_Rep_Stats);
				errorMsgs.add("提交檢舉成功!");
				System.out.println("提交檢舉成功!!");
			}

			// 直接轉交 errorMsgs 告知使用者提交成功或失敗
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/photowall/view_photowall.jsp");
				failureView.forward(req, res);
				return;
			}
			
			/*************************** 其他可能的錯誤處理 **********************************/
			} catch(Exception e) {
				errorMsgs.add("檢舉失敗:" + e.getMessage());
				RequestDispatcher failureView = req.
						getRequestDispatcher("/front_end/photowall/photo_wall.jsp");
				failureView.forward(req, res);
			}
		}
		
		System.out.println("審核有吃到嗎");
		if("review".equals(action)) {
			System.out.println("審核吃到");

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				String photo_No = req.getParameter("photo_No");
				System.out.println("審核照片編號="+photo_No);

				String mem_Id = req.getParameter("mem_Id");
				System.out.println("審核會員編號="+mem_Id);

				/***************************2.開始查詢資料*****************************************/
				photo_reportService photo_reportSvc = new photo_reportService();
				Photo_reportVO photo_reportVO = photo_reportSvc.findByPrimaryKey(photo_No,mem_Id);
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("photo_reportVO", photo_reportVO);
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/photo_wall/photo_report_review.jsp");
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch(Exception e) {
				errorMsgs.add("無法取得審核資料" + e.getMessage());
				RequestDispatcher failureView = req.
						getRequestDispatcher("/front_end/photowall/photo_wall.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		if ("update_State".equals(action)) { 
        	System.out.println("修改狀態有進來耶");
			List<String> errorMsgs = new LinkedList<String>();

			req.setAttribute("errorMsgs", errorMsgs);
			req.getParameter("tab");
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String mem_Id = req.getParameter("mem_Id");
				
				String photo_No = req.getParameter("photo_No");

				Integer photo_Sta = Integer.valueOf(req.getParameter("photo_Sta"));
			
				Integer pho_Rep_Stats = Integer.valueOf(req.getParameter("pho_Rep_Stats"));

				
				/***************************2.開始修改資料*****************************************/
				
				System.out.println("修改快要成功");
				
				Photo_wallService photo_wallSvc = new Photo_wallService();
				photo_wallSvc.update_State(mem_Id,photo_No,photo_Sta);
				
				photo_reportService photo_reportService = new photo_reportService();
				photo_reportService.update_review_State(mem_Id,photo_No,pho_Rep_Stats);
				
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/

				String url = "/back_end/photo_wall/photo_report.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
				successView.forward(req, res);
				System.out.println("大師兄的修改回來了");
				
				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/photo_wall/photo_report.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("get_Keyword".equals(action)) { 
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				String tag_Content = req.getParameter("tag_Content");
				if (tag_Content == null || (tag_Content.trim()).length() == 0) {
					errorMsgs.add("請輸入要搜尋的字");
				}
				

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("http://tw.yahoo.com");
					failureView.forward(req, res);
					return;// 程式中斷
				}

				/*************************** 2.開始查詢資料 *****************************************/
				Photo_tagService photo_tagSvc = new Photo_tagService();
				System.out.println("======================");
				List<Photo_tagVO> list = photo_tagSvc.get_Keyword(tag_Content);
				System.out.println("list size = " + list.size());
				System.out.println("tag = " + tag_Content);

//				if (list.isEmpty()) {
//					errorMsgs.add("查無資料");
//				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/photowall/失敗羅.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}
				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
				req.setAttribute("list", list);
				
				System.out.println(list);
				
				String url = "/front_end/photowall/get_photo_wall.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 manager_member.jsp
				successView.forward(req, res);
				System.out.println("轉交成功");
				return;
				
				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/photowall/photo_wall.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("update_review_State".equals(action)) { 
        	System.out.println("修改審核狀態有進來耶");
			List<String> errorMsgs = new LinkedList<String>();

			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String mem_Id = req.getParameter("mem_Id");
				
				String photo_No = req.getParameter("photo_No");

				Integer pho_Rep_Stats = Integer.valueOf(req.getParameter("pho_Rep_Stats"));
			
				
				Photo_reportVO photo_reportVO = new Photo_reportVO();
				
				photo_reportVO.setMem_Id(mem_Id);

				photo_reportVO.setPhoto_No(photo_No);

				photo_reportVO.setPho_Rep_Stats(pho_Rep_Stats);
				
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("photo_reportVO", photo_reportVO);
					System.out.println("修改審核狀態失敗囉");
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back_end/photo_wall/photo_report_review.jsp");
					failureView.forward(req, res);
					return; //程式中斷
					
				}
				/***************************2.開始修改資料*****************************************/
				
				System.out.println("修改審核狀態快要成功");
				photo_reportService photo_reportService = new photo_reportService();
				photo_reportService.update_review_State(mem_Id,photo_No,pho_Rep_Stats);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("Photo_reportVO", photo_reportVO); 
				String url = "/back_end/photo_wall/photo_report_review.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res);
				System.out.println("大師兄的修改審核狀態回來了");
				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/photo_wall/photo_report_review.jsp");
				failureView.forward(req, res);
			}
		}
		

	}
}