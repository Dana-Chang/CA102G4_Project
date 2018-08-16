package com.street.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.street.model.StreetService;
import com.street.model.StreetVO;

public class StreetServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		if ("getAllCity".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
			
				
				/***************************2.開始查詢資料*****************************************/
				StreetService streetSvc = new StreetService();
				List<String> listAllCity = streetSvc.getAllCity();
			
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				
				res.setContentType("application/json");
				//OutputStream outputStream= res.getOutputStream();
				PrintWriter pw = res.getWriter();
				Gson gson=new Gson();       
				pw.write(gson.toJson(listAllCity));
				pw.flush();

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				
			}
		}
		
		if ("getCountryByCity".equals(action)) { 

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String city = req.getParameter("city");
				
				/***************************2.開始查詢資料*****************************************/
				StreetService streetSvc = new StreetService();
				List<String> listCountry = streetSvc.getCountryByCity(city);
			
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				
				res.setContentType("application/json;charset=utf-8");
				//OutputStream outputStream= res.getOutputStream();
				PrintWriter pw = res.getWriter();
				Gson gson=new Gson();       
				pw.write(gson.toJson(listCountry));
				pw.flush();

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				
			}
		}
		
		
		if ("getRoadByCountry".equals(action)) { 

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String country = req.getParameter("country");
				
				/***************************2.開始查詢資料*****************************************/
				StreetService streetSvc = new StreetService();
				List<String> listRoad = streetSvc.getRoadByCountry(country);
			
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				
				res.setContentType("application/json;charset=utf-8");
				//OutputStream outputStream= res.getOutputStream();
				PrintWriter pw = res.getWriter();
				Gson gson=new Gson();       
				pw.write(gson.toJson(listRoad));
				pw.flush();

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				
			}
		}
		
		
		
	}
}
