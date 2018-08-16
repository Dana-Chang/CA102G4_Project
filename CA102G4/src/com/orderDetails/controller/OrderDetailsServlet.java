package com.orderDetails.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orderDetails.model.OrderDetailsService;
import com.orderDetails.model.OrderDetailsVO;

public class OrderDetailsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		if ("insert".equals(action)) { // 來自addOrderDetails.jsp的請求  
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				
				String str = req.getParameter("details_product_id");
				Integer details_product_id = new Integer(str);
				
				String str2 = req.getParameter("details_order_qty");
				
				Integer details_order_qty = null;
				try {
					details_order_qty = new Integer(str2);
				} catch (Exception e) {
					errorMsgs.add("商品編號格式不正確");
				}

				String str3 = req.getParameter("details_order_total");
				
				Integer details_order_total = null;
				try {
					details_order_total = new Integer(str3);
				} catch (Exception e) {
					errorMsgs.add("商品編號格式不正確");
				}
				
				OrderDetailsVO orderDetailsVO = new OrderDetailsVO();
				
				orderDetailsVO.setDetails_product_id(details_product_id);
				orderDetailsVO.setDetails_order_qty(details_order_qty);
				orderDetailsVO.setDetails_order_total(details_order_total);
				

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("orderDetailsVO", orderDetailsVO); // 含有輸入格式錯誤的orderDetailsVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/orderDetails/addOrderDetails.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				OrderDetailsService orderDetailsSvc = new OrderDetailsService();
				orderDetailsVO = orderDetailsSvc.addOrderDetails(details_product_id,details_order_qty,details_order_total);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("orderDetailsVO", orderDetailsVO); // 資料庫update成功後,正確的的orderDetailsVO物件,存入req
				String url = "/orderDetails/listAllOrderDetails.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneOrderDetails.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/orderDetails/addOrderDetails.jsp");
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
				String details_order_id = req.getParameter("details_order_id");
			    //20180724-000002
				String order_idReg = "^20[0-2]\\d[0-1]\\d[0-3]\\d-\\d{6}$";
				if (details_order_id == null || (details_order_id.trim()).length() == 0) {
					errorMsgs.add("請輸入訂單編號");
				}else if(!details_order_id.trim().matches(order_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("訂單編號: 只能是20180724-000002");
	            }
				
				String str = req.getParameter("details_product_id");
				String product_idReg = "^10[1-9]{2}$";
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入檢舉商品編號");
				}else if(!str.trim().matches(product_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("商品編號: 只能是10開頭, 尾數1~9重複兩次");
	            }
				
				
				Integer details_product_id = null;
				try {
					details_product_id = new Integer(str);
				} catch (Exception e) {
					errorMsgs.add("商品編號格式不正確");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/orderDetails/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				
				/***************************2.開始查詢資料*****************************************/
				OrderDetailsService orderDetailsSvc = new OrderDetailsService();
				OrderDetailsVO orderDetailsVO = orderDetailsSvc.getOneOrderDetails(details_order_id,details_product_id);
				if (orderDetailsVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/orderDetails/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("orderDetailsVO", orderDetailsVO); // 資料庫取出的orderDetailsVO物件,存入req
				String url = "/orderDetails/listOneOrderDetails.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneOrderDetails.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/orderDetails/select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
	}
}
