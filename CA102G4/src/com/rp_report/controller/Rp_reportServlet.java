package com.rp_report.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qa_reply.model.Qa_replyService;
import com.rp_report.model.Rp_reportService;
import com.rp_report.model.Rp_reportVO;



public class Rp_reportServlet  extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String action = req.getParameter("action");
		System.out.println(action);
		
		if ("insert".equals(action)) {  
			List<String> errorMsgs = new LinkedList<String>();

			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				
				String	reply_id	= req.getParameter("reply_id");
				if(reply_id==null||"".equals(reply_id)) {
					errorMsgs.add("回覆編號請勿留空");
				}
				String question_id = req.getParameter("question_id");
				String mem_id = req.getParameter("mem_Id");
				
				Integer	rp_state= 0;
				
				Rp_reportVO rp_reportVO = new Rp_reportVO();
				rp_reportVO.setReply_id(reply_id);
				rp_reportVO.setMem_id(mem_id);
				rp_reportVO.setRp_state(rp_state);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("rp_reportVO", rp_reportVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/qa_reply/qa_reply.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				/***************************2.開始新增資料***************************************/
				Rp_reportService rp_reportSvc = new Rp_reportService();
				rp_reportVO = rp_reportSvc.addRp_report(reply_id,mem_id,rp_state);
				req.setAttribute("question_id", question_id);
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/qa_reply/qa_reply.jsp"); 
				successView.forward(req, res);				
				
				/***************************其他可能的錯誤處理**********************************/
				} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/qa_reply/qa_reply.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("delete".equals(action)) {

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
	
			try {
				/***************************1.接收請求參數***************************************/
				String reply_id = req.getParameter("reply_id");
				String mem_id = req.getParameter("mem_id");

				/***************************2.開始刪除資料***************************************/
				Rp_reportService rp_reportSvc = new Rp_reportService();
				rp_reportSvc.deleteRp_reportVO(reply_id,mem_id);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/qa_report/qa_report.jsp");// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/qa_report/qa_report.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("update".equals(action)) { 
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String reply_id = req.getParameter("reply_id");
	
				Integer r_state = 1;
				

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back_end/qa_report/qa_report.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				Qa_replyService qa_replySvc = new Qa_replyService();
				qa_replySvc.updateR(reply_id,r_state);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/qa_report/qa_report.jsp"); 
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/qa_report/qa_report.jsp");
				failureView.forward(req, res);
			}
		}
		
	}
}