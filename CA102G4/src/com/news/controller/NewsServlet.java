package com.news.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.news.model.NewsService;
import com.news.model.NewsVO;
import com.qa_list.model.Qa_listService;
import com.question.model.QuestionService;
import com.question.model.QuestionVO;

public class NewsServlet extends HttpServlet {

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
				String news_name = req.getParameter("news_name");
				if (news_name == null || news_name.trim().length() == 0) {
					errorMsgs.add("消息名稱請勿空白");
				}
				
				java.sql.Date news_date = null;
				try {
					news_date = java.sql.Date.valueOf(req.getParameter("news_date").trim());
				} catch (IllegalArgumentException e) {
						errorMsgs.add("請輸入日期!");
				} catch (NullPointerException e) {
					errorMsgs.add("請輸入日期!");
				}
				
				String news_con = req.getParameter("news_con");
				if (news_con == null || news_con.trim().length() == 0) {
					errorMsgs.add("消息內容請勿空白");
				}
				
				NewsVO newsVO = new NewsVO();
				
				newsVO.setNews_name(news_name);
				newsVO.setNews_date(news_date);
				newsVO.setNews_con(news_con);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("newsVO", newsVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/news/news.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始新增資料***************************************/
				NewsService newsSvc = new NewsService();
				newsVO = newsSvc.addNews(news_name,news_date,news_con);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/news/news.jsp"); 
				successView.forward(req, res);				
				
				/***************************其他可能的錯誤處理**********************************/
				} catch (Exception e) {
					errorMsgs.add(e.getMessage());
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/news/news.jsp");
					failureView.forward(req, res);
				}
			}
		
			if ("update".equals(action)) { // 來自update_emp_input.jsp的請求
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String news_id = req.getParameter("news_id");
				
				String news_name = req.getParameter("news_name");
				if (news_name == null || news_name.trim().length() == 0) {
					errorMsgs.add("消息名稱請勿空白");
				}
				
				java.sql.Date news_date = null;
				try {
					news_date = java.sql.Date.valueOf(req.getParameter("news_date").trim());
				} catch (IllegalArgumentException e) {
					news_date=new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請輸入想要日期!");
				}
				
				String news_con = req.getParameter("news_con");
				if (news_con == null || news_con.trim().length() == 0) {
					errorMsgs.add("消息內容請勿空白");
				}
				
				NewsVO newsVO = new NewsVO();
				newsVO.setNews_id(news_id);
				newsVO.setNews_name(news_name);
				newsVO.setNews_date(news_date);
				newsVO.setNews_con(news_con);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("newsVO", newsVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/news/update_news.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				NewsService newsSvc = new NewsService();
				newsVO = newsSvc.updateNews(news_id,news_name,news_date,news_con);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("newsVO", newsVO); 
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/news/news.jsp"); 
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/news/news.jsp");
				failureView.forward(req, res);
			}
		}
			
			if ("getOne_For_Update".equals(action)) { 

				List<String> errorMsgs = new LinkedList<String>();
				req.setAttribute("errorMsgs", errorMsgs);
				
				try {
					/***************************1.接收請求參數****************************************/
					String news_id = req.getParameter("news_id");
					
					/***************************2.開始查詢資料****************************************/
					NewsService newsSvc = new NewsService();
					NewsVO newsVO = newsSvc.getOneNews(news_id);
									
					/***************************3.查詢完成,準備轉交(Send the Success view)************/
					req.setAttribute("newsVO", newsVO);
					RequestDispatcher successView = req.getRequestDispatcher("/back_end/news/update_news.jsp");
					successView.forward(req, res);

					/***************************其他可能的錯誤處理**********************************/
				} catch (Exception e) {
					errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/news/update_news.jsp");
					failureView.forward(req, res);
				}
			}
			
			if ("delete".equals(action)) {

				List<String> errorMsgs = new LinkedList<String>();
				req.setAttribute("errorMsgs", errorMsgs);
		
				try {
					/***************************1.接收請求參數***************************************/
					String news_id = req.getParameter("news_id");
					/***************************2.開始刪除資料***************************************/
					NewsService newsSvc = new NewsService();
					newsSvc.deleteNews(news_id);
					
					/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
					RequestDispatcher successView = req.getRequestDispatcher("/back_end/news/news.jsp");// 刪除成功後,轉交回送出刪除的來源網頁
					successView.forward(req, res);
					
					/***************************其他可能的錯誤處理**********************************/
				} catch (Exception e) {
					errorMsgs.add("刪除資料失敗:"+e.getMessage());
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back_end/news/news.jsp");
					failureView.forward(req, res);
				}
			}
		}
	}
			
