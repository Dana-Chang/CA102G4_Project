package com.question.controller;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.qa_classification.model.Qa_classificationService;
import com.qa_classification.model.Qa_classificationVO;
import com.question.model.*;

public class QuestionServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String action = req.getParameter("action");
		
		if ("getOne_For_Display".equals(action)) { 

			List<String> errorMsgs = new LinkedList<String>();

			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String str = req.getParameter("question_id");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入問題編號");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/question/select_page.jsp");
					failureView.forward(req, res);
					return;
				}
				
				String question_id = str;

				String question_idReg = "^QU{1}[0-9]{9}$";	
				if(!str.trim().matches(question_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("問題編號只能是QU開頭加後面9個數字");
	            }
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/question/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}		
				
				/***************************2.開始查詢資料*****************************************/
				QuestionService questionSvc = new QuestionService();
				QuestionVO questionVO = questionSvc.getOneQuestion(question_id);
				if (questionVO == null) {
					errorMsgs.add("查無資料");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/question/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("questionVO", questionVO);
				RequestDispatcher successView = req.getRequestDispatcher("/question/listOneQuestion.jsp");
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/question/select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		if ("getOne_For_Update".equals(action)) { 

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				String question_id = req.getParameter("question_id");
				
				/***************************2.開始查詢資料****************************************/
				QuestionService questionSvc = new QuestionService();
				QuestionVO questionVO = questionSvc.getOneQuestion(question_id);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("questionVO", questionVO);
				RequestDispatcher successView = req.getRequestDispatcher("/question/update_question_input.jsp");
				successView.forward(req, res);

				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/question/listAllQuestion.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		if ("update".equals(action)) { 
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String question_id = req.getParameter("question_id");
				
				String mem_id = req.getParameter("mem_id");
				String mem_idReg = "^M{1}[0-9]{6}$";
				if (mem_id == null || mem_id.trim().length() == 0) {
					errorMsgs.add("會員編號: 請勿空白");
				} else if(!mem_id.trim().matches(mem_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("會員編號: 會員編號只能是M開頭加後面6個數字");
	            }

				String question_content = req.getParameter("question_content");
				if (question_content == null || question_content.trim().length() == 0) {
					errorMsgs.add("問題內容:請勿空白");
				}	
				
				java.sql.Date build_date = null;
				try {
					build_date = java.sql.Date.valueOf(req.getParameter("build_date").trim());
				} catch (IllegalArgumentException e) {
					build_date=new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請輸入日期!");
				}
				
				Integer q_state = 0;
				
				QuestionVO questionVO = new QuestionVO();
				questionVO.setQuestion_id(question_id);
				questionVO.setMem_id(mem_id);
				questionVO.setBuild_date(build_date);
				questionVO.setQuestion_content(question_content);
				questionVO.setQ_state(q_state);

				if (!errorMsgs.isEmpty()) {
					req.setAttribute("questionVO", questionVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/question/update_question_input.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				QuestionService questionSvc = new QuestionService();
				questionVO = questionSvc.updateQuestion(question_id, mem_id, question_content,build_date,q_state);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("questionVO", questionVO); 
				RequestDispatcher successView = req.getRequestDispatcher("/question/listOneQuestion.jsp"); 
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/question/update_question_input.jsp");
				failureView.forward(req, res);
			}
		}
		
		 if ("insert".equals(action)) {   
				List<String> errorMsgs = new LinkedList<String>();

				req.setAttribute("errorMsgs", errorMsgs);

				try {
					/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
					String mem_id = req.getParameter("mem_Id");
					
					String question_content = req.getParameter("question_content");
					if (question_content == null || question_content.trim().length() == 0) {
						errorMsgs.add("問題內容請勿空白");
					}	
					
					java.sql.Date build_date = new java.sql.Date(System.currentTimeMillis());
					
					Integer q_state =0;
					
					QuestionVO questionVO = new QuestionVO();
					questionVO.setMem_id(mem_id);
					questionVO.setBuild_date(build_date);
					questionVO.setQuestion_content(question_content);
					questionVO.setQ_state(q_state);
					
					String[] list_id = req.getParameterValues("list_id");
					if (list_id == null || list_id.length == 0) {
						errorMsgs.add("請勾選標籤");
					}	
					
					if (!errorMsgs.isEmpty()) {
						req.setAttribute("questionVO", questionVO); 
						RequestDispatcher failureView = req.getRequestDispatcher("/front_end/ask/ask.jsp");
						failureView.forward(req, res);
						return; //程式中斷
					}
					
					List<Qa_classificationVO> list = new ArrayList<>();
			        for(String list_one_id : list_id){
			        	Qa_classificationVO qa_classVO = new Qa_classificationVO();
			        	qa_classVO.setList_id(list_one_id);
			        	list.add(qa_classVO);
			        }
			        
			        
			        
					if (!errorMsgs.isEmpty()) {
						req.setAttribute("questionVO", questionVO); 
						RequestDispatcher failureView = req.getRequestDispatcher("/front_end/ask/ask.jsp");
						failureView.forward(req, res);
						return; //程式中斷
					}
					/***************************2.開始新增資料***************************************/
					QuestionService questionSvc = new QuestionService();
					questionSvc.insertQuestionAndQa_class(questionVO, list);
	
					/***************************3.新增完成,準備轉交(Send the Success view)***********/
					RequestDispatcher successView = req.getRequestDispatcher("/front_end/question/question.jsp"); 
					successView.forward(req, res);				
					/***************************其他可能的錯誤處理**********************************/
				} catch (Exception e) {
					errorMsgs.add(e.getMessage());
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/ask/ask.jsp");
					failureView.forward(req, res);
				}
			}
			
			
			if ("delete".equals(action)) { // 來自listAllEmp.jsp

				List<String> errorMsgs = new LinkedList<String>();
				req.setAttribute("errorMsgs", errorMsgs);
		
				try {
					/***************************1.接收請求參數***************************************/
					String question_id = req.getParameter("question_id");
					/***************************2.開始刪除資料***************************************/
					QuestionService questionSvc = new QuestionService();
					questionSvc.deleteQuestion(question_id);
					
					/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
					RequestDispatcher successView = req.getRequestDispatcher("/question/listAllQuestion.jsp");// 刪除成功後,轉交回送出刪除的來源網頁
					successView.forward(req, res);
					
					/***************************其他可能的錯誤處理**********************************/
				} catch (Exception e) {
					errorMsgs.add("刪除資料失敗:"+e.getMessage());
					RequestDispatcher failureView = req
							.getRequestDispatcher("/question/listAllQuestion.jsp");
					failureView.forward(req, res);
				}
			}
		}
	}
