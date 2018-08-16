package com.faq.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.faq.model.FaqService;
import com.faq.model.FaqVO;

public class FaqServlet extends HttpServlet {

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
				String faq_qa = req.getParameter("faq_qa");
				if (faq_qa == null || faq_qa.trim().length() == 0) {
					errorMsgs.add("FAQ標題請勿空白");
				}
				
				String faq_content = req.getParameter("faq_content");
				if (faq_content == null || faq_content.trim().length() == 0) {
					errorMsgs.add("FAQ內容請勿空白");
				}
				
				FaqVO faqVO = new FaqVO();
				
				faqVO.setFaq_qa(faq_qa);
				faqVO.setFaq_content(faq_content);

				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("faqVO", faqVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/faq/faq.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始新增資料***************************************/
				FaqService faqSvc = new FaqService();
				faqVO = faqSvc.addFaq(faq_qa,faq_content);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/faq/faq.jsp"); 
				successView.forward(req, res);				
				
				/***************************其他可能的錯誤處理**********************************/
				} catch (Exception e) {
					errorMsgs.add(e.getMessage());
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/faq/faq.jsp");
					failureView.forward(req, res);
				}
			}
		
			if ("update".equals(action)) { 
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String faq_id = req.getParameter("faq_id");
				
				String faq_qa = req.getParameter("faq_qa");
				if (faq_qa == null || faq_qa.trim().length() == 0) {
					errorMsgs.add("FAQ標題請勿空白");
				}
				
				String faq_content = req.getParameter("faq_content");
				if (faq_content == null || faq_content.trim().length() == 0) {
					errorMsgs.add("FAQ內容請勿空白");
				}
				
				FaqVO faqVO = new FaqVO();
				faqVO.setFaq_id(faq_id);
				faqVO.setFaq_qa(faq_qa);
				faqVO.setFaq_content(faq_content);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("faqVO", faqVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/faq/update_faq.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				FaqService faqSvc = new FaqService();
				faqVO = faqSvc.updateFaq(faq_id,faq_qa,faq_content);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("faqVO", faqVO); 
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/faq/faq.jsp"); 
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/faq/faq.jsp");
				failureView.forward(req, res);
			}
		}
			
			if ("getOne_For_Update".equals(action)) { 

				List<String> errorMsgs = new LinkedList<String>();
				req.setAttribute("errorMsgs", errorMsgs);
				
				try {
					/***************************1.接收請求參數****************************************/
					String faq_id = req.getParameter("faq_id");
					
					/***************************2.開始查詢資料****************************************/
					FaqService faqSvc = new FaqService();
					FaqVO faqVO = faqSvc.getOneFaq(faq_id);
									
					/***************************3.查詢完成,準備轉交(Send the Success view)************/
					req.setAttribute("faqVO", faqVO);
					RequestDispatcher successView = req.getRequestDispatcher("/back_end/faq/update_faq.jsp");
					successView.forward(req, res);

					/***************************其他可能的錯誤處理**********************************/
				} catch (Exception e) {
					errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/faq/update_faq.jsp");
					failureView.forward(req, res);
				}
			}
	}
}
