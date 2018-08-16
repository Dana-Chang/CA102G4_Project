package com.qa_list.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.qa_list.model.Qa_listService;
import com.qa_list.model.Qa_listVO;
import com.question.model.QuestionService;

public class Qa_listServlet extends HttpServlet {

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

				String list_name = req.getParameter("list_name");
				String list_nameReg = "^[(\u4e00-\u9fa5)]{2,10}$";
				if (list_name == null || list_name.trim().length() == 0) {
					errorMsgs.add("標籤名稱請勿空白");
				}else if(!list_name.trim().matches(list_nameReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("員工姓名: 只能是中文, 且長度必需在2到10之間");
	            }	
				
				Qa_listVO qa_listVO = new Qa_listVO();
				qa_listVO.setList_name(list_name);

				if (!errorMsgs.isEmpty()) {
					req.setAttribute("qa_listVO", qa_listVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/qa_list/qa_list.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始新增資料***************************************/
				Qa_listService qa_listSvc = new Qa_listService();
				qa_listVO = qa_listSvc.addQa_list(list_name);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/qa_list/qa_list.jsp"); 
				successView.forward(req, res);				
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back_end/qa_list/qa_list.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		if ("delete".equals(action)) {

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
	
			try {
				/***************************1.接收請求參數***************************************/
				String list_id = req.getParameter("list_id");
				/***************************2.開始刪除資料***************************************/
				Qa_listService qa_listSvc = new Qa_listService();
				qa_listSvc.deleteQa_list(list_id);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/qa_list/qa_list.jsp");// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/qa_list/qa_list.jsp");
				failureView.forward(req, res);
			}
		}
	}
}
