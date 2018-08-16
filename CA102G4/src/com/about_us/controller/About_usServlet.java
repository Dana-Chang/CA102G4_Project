package com.about_us.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.about_us.model.About_usService;
import com.about_us.model.About_usVO;

public class About_usServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String action = req.getParameter("action");

		if ("update".equals(action)) { 
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String about_us_id = req.getParameter("about_us_id");
				
				String about_content = req.getParameter("about_content");
				if (about_content == null || about_content.trim().length() == 0) {
					errorMsgs.add("內容請勿空白");
				}
				
				About_usVO about_usVO = new About_usVO();
				about_usVO.setAbout_us_id(about_us_id);
				about_usVO.setAbout_content(about_content);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("about_usVO", about_usVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/back_end/about_us/update_about_us.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				About_usService about_usSvc = new About_usService();
				about_usVO = about_usSvc.updateAbout_us(about_us_id,about_content);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("about_usVO", about_usVO); 
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/about_us/about_us.jsp"); 
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/about_us/about_us.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("getOne_For_Update".equals(action)) { 

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				String about_us_id = req.getParameter("about_us_id");
	
				/***************************2.開始查詢資料****************************************/
				About_usService about_usSvc = new About_usService();
				About_usVO about_usVO = about_usSvc.getOneAbout_us(about_us_id);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				;
				req.setAttribute("about_usVO", about_usVO);
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/about_us/update_about_us.jsp");
				successView.forward(req, res);

				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back_end/about_us/update_about_us.jsp");
				failureView.forward(req, res);
			}
		}
	}
}
