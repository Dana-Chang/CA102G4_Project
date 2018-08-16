package com.ord.controller;


import java.io.IOException;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ord.model.OrdService;
import com.ord.model.OrdVO;



public class OrdServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		if ("insert".equals(action)) { // 來自addProduct.jsp的請求  
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				
				String buyer_mem_id = req.getParameter("buyer_mem_id");
				String mem_idReg = "^M(0){5}[1-9]$";
				if (buyer_mem_id == null || buyer_mem_id.trim().length() == 0) {
					errorMsgs.add("買家編號: 請勿空白");
				}else if(!buyer_mem_id.trim().matches(mem_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("買家編號: 只能是M00000開頭, 尾數1~9");
	            }
				
				
				String seller_mem_id = req.getParameter("seller_mem_id");
				if (seller_mem_id == null || seller_mem_id.trim().length() == 0) {
					errorMsgs.add("賣家編號: 請勿空白");
				}else if(!seller_mem_id.trim().matches(mem_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("賣家編號: 只能是M00000開頭, 尾數1~9");
	            }

				String order_address = req.getParameter("order_address");
				
				if (order_address == null || order_address.trim().length() == 0) {
					errorMsgs.add("訂單地址");
				}
				
				
				String str = req.getParameter("payment_status");
				Integer payment_status = new Integer(str);
				
				String str2 = req.getParameter("payment_method");
				Integer payment_method = new Integer(str2);
		
				String str3 = req.getParameter("shipment_status");
				Integer shipment_status = new Integer(str3);
				
				
				java.sql.Timestamp order_date = null;
				try {
					order_date = java.sql.Timestamp.valueOf(req.getParameter("order_date").trim());
				} catch (IllegalArgumentException e) {
					order_date = new java.sql.Timestamp(System.currentTimeMillis());
					errorMsgs.add("請輸入日期!");
				}
				
				String str4 = req.getParameter("order_status");
				Integer order_status = new Integer(str4);
				
				String str5 = req.getParameter("order_total");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入訂單總金額");
				}
				Integer order_total = null;
				try {
					order_total = new Integer(str5);
				} catch (Exception e) {
					errorMsgs.add("訂單總金額格式不正確");
				}
				
				String str6 = req.getParameter("order_item");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入訂單項目數量");
				}
				Integer order_item = null;
				try {
					order_item = new Integer(str6);
				} catch (Exception e) {
					errorMsgs.add("訂單項目數量格式不正確");
				}
				
				String str7 = req.getParameter("shipment_method");
				Integer shipment_method = new Integer(str7);
				
				String ord_store_711_name = req.getParameter("ord_store_711_name");

				
				OrdVO ordVO = new OrdVO();

				ordVO.setBuyer_mem_id(buyer_mem_id);
				ordVO.setSeller_mem_id(seller_mem_id);
				ordVO.setOrder_address(order_address);
				ordVO.setPayment_status(payment_status);
				ordVO.setPayment_method(payment_method);
				ordVO.setShipment_status(shipment_status);
				ordVO.setOrder_date(order_date);
				ordVO.setOrder_status(order_status);
				ordVO.setOrder_total(order_total);
				ordVO.setOrder_item(order_item);
				ordVO.setShipment_method(shipment_method);
				ordVO.setOrd_store_711_name(ord_store_711_name);
			
	
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("ordVO", ordVO); // 含有輸入格式錯誤的ordVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/ord/addOrd.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				OrdService ordSvc = new OrdService();
				ordVO = ordSvc.addOrd(buyer_mem_id,seller_mem_id,order_address,payment_status,payment_method,shipment_status,
						order_date,order_status,order_total,order_item,shipment_method,ord_store_711_name);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("ordVO", ordVO); // 資料庫update成功後,正確的的ordVO物件,存入req
				String url = "/ord/listAllOrd.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneOrd.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/ord/addOrd.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		if ("getOne_For_Update".equals(action)) { //  來自listAllOrd.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				String order_id = req.getParameter("order_id");
				
				/***************************2.開始查詢資料****************************************/
				OrdService ordSvc = new OrdService();
				OrdVO ordVO = ordSvc.getOneOrd(order_id);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("ordVO", ordVO);         // 資料庫取出的ordVO物件,存入req
				String url = "/ord/update_ord_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_ord_input.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/ord/listAllOrd.jsp");
				failureView.forward(req, res);
			}
		}
		

		if ("update".equals(action)) { // 來自update_ord_input.jsp的請求
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
		
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String order_id = req.getParameter("order_id");
				String buyer_mem_id = req.getParameter("buyer_mem_id");
				String seller_mem_id = req.getParameter("seller_mem_id");
				String order_address = req.getParameter("order_address");

				String str = req.getParameter("payment_status");
				Integer payment_status = null;
				try {
					payment_status = new Integer(str);
				} catch (Exception e) {
					errorMsgs.add("付款狀態格式不正確");
				}
				
				Integer payment_method = new Integer(req.getParameter("payment_method").trim());
				
				String str2 = req.getParameter("shipment_status");
				Integer shipment_status = null;
				try {
					shipment_status = new Integer(str2);
				} catch (Exception e) {
					errorMsgs.add("物流狀態格式不正確");
				}
				
				
				java.sql.Timestamp order_date = null;
				try {
					order_date = java.sql.Timestamp.valueOf(req.getParameter("order_date").trim());
				} catch (IllegalArgumentException e) {
					order_date = new java.sql.Timestamp(System.currentTimeMillis());
					errorMsgs.add("請輸入日期!");
				}
				
				
				
				String str3 = req.getParameter("order_status");
				Integer order_status = null;
				try {
					order_status = new Integer(str3);
				} catch (Exception e) {
					errorMsgs.add("訂單狀態格式不正確");
				}
				
				
				String str4 = req.getParameter("order_total");
				Integer order_total =new Integer(str4);
				
				
				String str5 = req.getParameter("order_item");
				Integer order_item =new Integer(str5);
				
				String str6 = req.getParameter("cancel_reason");
				Integer cancel_reason = null;
				try {
					order_status = new Integer(str6);
				} catch (Exception e) {
					cancel_reason = null;
				}
				
				String str7 = req.getParameter("stob_rating");
				Integer stob_rating = null;
				try {
					stob_rating = new Integer(str7);
				} catch (Exception e) {
					stob_rating = null;
				}
				
				String stob_rating_descr = req.getParameter("stob_rating_descr");
				
				String str8 = req.getParameter("btos_rating");
				Integer btos_rating = null;
				try {
					btos_rating = new Integer(str8);
				} catch (Exception e) {
					btos_rating = null;
				}
				
				String btos_rating_descr = req.getParameter("btos_rating_descr");
				
				String shipment_id = req.getParameter("shipment_id");
				
				
				String str9 = req.getParameter("shipment_method");
				Integer shipment_method =new Integer(str9);
				
				String ord_store_711_name = req.getParameter("ord_store_711_name");
				
				OrdVO ordVO = new OrdVO();
				
				ordVO.setOrder_id(order_id);
				ordVO.setBuyer_mem_id(buyer_mem_id);
				ordVO.setSeller_mem_id(seller_mem_id);
				ordVO.setOrder_address(order_address);
				ordVO.setPayment_status(payment_status);
				ordVO.setPayment_method(payment_method);
				ordVO.setShipment_status(shipment_status);
				ordVO.setOrder_date(order_date);
				ordVO.setOrder_status(order_status);
				ordVO.setOrder_total(order_total);
				ordVO.setOrder_item(order_item);
				ordVO.setCancel_reason(cancel_reason);
				ordVO.setStob_rating(stob_rating);
				ordVO.setStob_rating_descr(stob_rating_descr);
				ordVO.setBtos_rating(btos_rating);
				ordVO.setBtos_rating_descr(btos_rating_descr);
				ordVO.setShipment_id(shipment_id);
				ordVO.setShipment_method(shipment_method);
				ordVO.setOrd_store_711_name(ord_store_711_name);
			
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("ordVO", ordVO); // 含有輸入格式錯誤的ordVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/ord/update_ord_input.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				OrdService ordSvc = new OrdService();
				ordVO = ordSvc.updateOrd(payment_status,shipment_status,order_status,cancel_reason,stob_rating,
						stob_rating_descr,btos_rating,btos_rating_descr,shipment_id,order_id);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("ordVO", ordVO); // 資料庫update成功後,正確的的ordVO物件,存入req
				String url = "/ord/listOneOrd.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneProduct.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/ord/update_ord_input.jsp");
				failureView.forward(req, res);
			}
		}

		if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String order_id = req.getParameter("order_id");
			    //20180724-000002
				String order_idReg = "^20[0-2]\\d[0-1]\\d[0-3]\\d-\\d{6}$";
				if (order_id == null || (order_id.trim()).length() == 0) {
					errorMsgs.add("請輸入訂單編號");
				}else if(!order_id.trim().matches(order_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("訂單編號: 只能是20180724-000002");
	            }
				
				
				
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/ord/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				
				/***************************2.開始查詢資料*****************************************/
				OrdService ordSvc = new OrdService();
				OrdVO ordVO = ordSvc.getOneOrd(order_id);
				if (ordVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/ord/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("ordVO", ordVO); // 資料庫取出的ordVO物件,存入req
				String url = "/ord/listOneOrd.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneOrd.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/ord/select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		
	}
}
