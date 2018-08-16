package com.qa_collect.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qa_collect.model.Qa_collectService;
import com.qa_collect.model.Qa_collectVO;
import com.qa_list.model.Qa_listService;
import com.question.model.QuestionService;

public class Qa_collectServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String action = req.getParameter("action");
		
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
				
				Qa_collectVO qa_collectVO = new Qa_collectVO();
				qa_collectVO.setQuestion_id(question_id);
				qa_collectVO.setMem_id(mem_id);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("qa_collectVO", qa_collectVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/qa_reply/qa_reply.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				/***************************2.開始新增資料***************************************/
				Qa_collectService qa_collectSvc = new Qa_collectService();
				qa_collectVO = qa_collectSvc.addQa_collect(question_id,mem_id);
				
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
				/***************************2.開始刪除資料***************************************/
				Qa_collectService qa_collectSvc = new Qa_collectService();
				System.out.println("question_id="+question_id);
				qa_collectSvc.deleteQa_collect(question_id);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/personal/personal_area_question.jsp");// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/personal/personal_area_question.jsp");
				failureView.forward(req, res);
			}
		}
		
	}
}
