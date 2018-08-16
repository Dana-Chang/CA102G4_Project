package com.qa_reply.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qa_reply.model.Qa_replyService;
import com.qa_reply.model.Qa_replyVO;
import com.mem.model.*;

public class Qa_replyServlet extends HttpServlet {

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

				String	reply_content = req.getParameter("reply_content");
				if(reply_content==null||"".equals(reply_content)) {
					errorMsgs.add("回覆內容請勿留空");
				}
				java.sql.Date reply_date = new java.sql.Date(System.currentTimeMillis());
				
				Integer r_state =0;
				
				Qa_replyVO qa_replyVO = new Qa_replyVO();
				qa_replyVO.setQuestion_id(question_id);
				qa_replyVO.setMem_id(mem_id);
				qa_replyVO.setReply_content(reply_content);
				qa_replyVO.setReply_date(reply_date);
				qa_replyVO.setR_state(r_state);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("qa_replyVO", qa_replyVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/qa_reply/qa_reply.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				/***************************2.開始新增資料***************************************/
				Qa_replyService qa_replySvc = new Qa_replyService();
				qa_replyVO = qa_replySvc.addQa_reply(question_id,mem_id,reply_content,reply_date,r_state);
				
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
		
		
	}
}
