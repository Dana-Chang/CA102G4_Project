package com.qa_report.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qa_report.model.Qa_reportService;
import com.qa_report.model.Qa_reportVO;
import com.question.model.QuestionService;
import com.question.model.QuestionVO;

public class Qa_reportServlet extends HttpServlet {

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
				
				String	question_id	= req.getParameter("question_id");
				if(question_id==null||"".equals(question_id)) {
					errorMsgs.add("問題編號請勿留空");
				}
				
				String mem_id = req.getParameter("mem_Id");
				
				Integer	qa_state= 0;
				
				Qa_reportVO qa_reportVO = new Qa_reportVO();
				qa_reportVO.setQuestion_id(question_id);
				qa_reportVO.setMem_id(mem_id);
				qa_reportVO.setQa_state(qa_state);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("qa_reportVO", qa_reportVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/qa_reply/qa_reply.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				/***************************2.開始新增資料***************************************/
				Qa_reportService qa_reportSvc = new Qa_reportService();
				qa_reportVO = qa_reportSvc.addQa_report(question_id,mem_id,qa_state);
				
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
				String question_id = req.getParameter("question_id");
				String mem_id = req.getParameter("mem_id");
				
				/***************************2.開始刪除資料***************************************/
				Qa_reportService qa_reportSvc = new Qa_reportService();
				qa_reportSvc.deleteQa_report(question_id,mem_id);
				
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
				String question_id = req.getParameter("question_id");
	
				Integer q_state = 1;
				

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back_end/qa_report/qa_report.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				QuestionService questionSvc = new QuestionService();
				questionSvc.updateQ(question_id,q_state);
				
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
