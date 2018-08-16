package com.productReport.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.productReport.model.ProductReportService;
import com.productReport.model.ProductReportVO;





public class ProductReportServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("insert".equals(action)) { // 來自addProductReport.jsp的請求  
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				Integer prod_report_product_id = new Integer(req.getParameter("prod_report_product_id").trim());
				String prod_report_mem_id = req.getParameter("prod_report_mem_id");
				String prod_report_mem_idReg = "^M(0){5}[1-9]$";
				if (prod_report_mem_id == null || prod_report_mem_id.trim().length() == 0) {
					errorMsgs.add("檢舉會員編號: 請勿空白");
				}else if(!prod_report_mem_id.trim().matches(prod_report_mem_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("檢舉會員編號: 只能是M00000開頭, 尾數1~9");
	            }
				
				

				String prod_report_reason = req.getParameter("prod_report_reason");
				
				if (prod_report_reason == null || prod_report_reason.trim().length() == 0) {
					errorMsgs.add("商品名稱: 請勿空白");
				}

				
				java.sql.Date prod_report_time = null;
				try {
					prod_report_time = java.sql.Date.valueOf(req.getParameter("prod_report_time").trim());
				} catch (IllegalArgumentException e) {
					prod_report_time = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請輸入日期!");
				}
				Timestamp ts=new Timestamp(prod_report_time.getTime());
				
				
				String str3 = req.getParameter("prod_report_status");
				Integer prod_report_status = new Integer(str3);
				
				ProductReportVO productReportVO = new ProductReportVO();
				
				productReportVO.setProd_report_product_id(prod_report_product_id);
				productReportVO.setProd_report_mem_id(prod_report_mem_id);
				productReportVO.setProd_report_reason(prod_report_reason);
				productReportVO.setProd_report_time(ts);
				productReportVO.setProd_report_status(prod_report_status);

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("productReportVO", productReportVO); // 含有輸入格式錯誤的productVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productReport/addProductReport.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				ProductReportService productReportSvc = new ProductReportService();
				productReportVO = productReportSvc.addProductReport(prod_report_product_id,prod_report_mem_id,prod_report_reason,
						ts,prod_report_status);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("productReportVO", productReportVO); // 資料庫update成功後,正確的的productVO物件,存入req
				String url = "/productReport/listAllProductReport.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneProduct.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/productReport/addProductReport.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("getOne_For_Update".equals(action)) { //  來自listAllProductReport.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				Integer prod_report_product_id = new Integer(req.getParameter("prod_report_product_id"));
				String prod_report_mem_id = req.getParameter("prod_report_mem_id");
				
				/***************************2.開始查詢資料****************************************/
				ProductReportService productReportSvc = new ProductReportService();
				ProductReportVO productReportVO = productReportSvc.getOneProductReport(prod_report_product_id, prod_report_mem_id);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("productReportVO", productReportVO);         // 資料庫取出的productReportVO物件,存入req
				String url = "/productReport/update_productReport_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_productReport_input.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/productReport/listAllProductReport.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("delete".equals(action)) { // 來自listAllProductReport.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
	
			try {
				/***************************1.接收請求參數***************************************/
				Integer prod_report_product_id = new Integer(req.getParameter("prod_report_product_id"));
				String prod_report_mem_id = req.getParameter("prod_report_mem_id");
				
				/***************************2.開始刪除資料***************************************/
				
				ProductReportService productReportSvc = new ProductReportService();
				productReportSvc.deleteProductReport(prod_report_product_id, prod_report_mem_id);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
				String url = "/productReport/listAllProductReport.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/productReport/listAllProductReport.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("update".equals(action)) { // 來自update_productReport_input.jsp的請求
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
		
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				Integer prod_report_product_id = new Integer(req.getParameter("prod_report_product_id").trim());
				String prod_report_mem_id = req.getParameter("prod_report_mem_id");
				String prod_report_reason = req.getParameter("prod_report_reason");
				java.sql.Timestamp prod_report_time = null;
				try {
					prod_report_time = java.sql.Timestamp.valueOf(req.getParameter("prod_report_time").trim());
				} catch (IllegalArgumentException e) {
					prod_report_time = new java.sql.Timestamp(System.currentTimeMillis());
					errorMsgs.add("請輸入日期!");
				}
				
				String str3= req.getParameter("prod_report_status");
				Integer prod_report_status = new Integer(str3);
				
				
				ProductReportVO productReportVO = new ProductReportVO();
				productReportVO.setProd_report_product_id(prod_report_product_id);
				productReportVO.setProd_report_mem_id(prod_report_mem_id);
				productReportVO.setProd_report_reason(prod_report_reason);
				productReportVO.setProd_report_time(prod_report_time);
				productReportVO.setProd_report_status(prod_report_status);
			
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("productReportVO", productReportVO); // 含有輸入格式錯誤的productReportVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productReport/update_productReport_input.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				ProductReportService productReportSvc = new ProductReportService();
				productReportVO = productReportSvc.updateProductReport(prod_report_product_id,prod_report_mem_id,prod_report_reason,
						prod_report_time,prod_report_status);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("productReportVO", productReportVO); // 資料庫update成功後,正確的的productReportVO物件,存入req
				String url = "/productReport/listOneProductReport.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneProductReport.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/productReport/update_productReport_input.jsp");
				failureView.forward(req, res);
			}
		}
		
			if ("updateByAjax".equals(action)) { 
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
		
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				Integer prod_report_product_id = new Integer(req.getParameter("prodId").trim());
				Integer prodStatus = new Integer(req.getParameter("prodStatus"));
				String memId = req.getParameter("memId");

				/***************************2.開始修改資料*****************************************/

				ProductService prodSvc = new ProductService();
				ProductVO prodVO = prodSvc.getOneProduct(prod_report_product_id);
				
				if(prodStatus==1) {
					res.setContentType("text/html;charset=Big5");
					PrintWriter w = res.getWriter();
					w.print("經過審核商品沒有問題，不需下架!");
				}else if(prodStatus==2) {
					
					prodVO.setProduct_status(2);//1>上架 2>下架
					prodSvc.updateProduct(prodVO);
					res.setContentType("text/html;charset=Big5");
					PrintWriter w = res.getWriter();
					w.print("經過審核商品有問題，暫時下架!");
				}else if(prodStatus==3) {
					
					prodSvc.deleteProduct(prod_report_product_id);
					res.setContentType("text/html;charset=Big5");
					PrintWriter w = res.getWriter();
					w.print("經過審核商品有嚴重問題，刪除商品!");
				}

				ProductReportService productReportSvc = new ProductReportService();
				ProductReportVO productReportVO =productReportSvc.getOneProductReport(prod_report_product_id, memId);
				productReportVO.setProd_report_status(2);//2 是已審核
				productReportSvc.updateProductReport(productReportVO);
				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				
			}
		}
		
		if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String str = req.getParameter("prod_report_product_id");
				String prod_report_product_idReg = "^10[1-9]{2}$";
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入檢舉商品編號");
				}else if(!str.trim().matches(prod_report_product_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("商品編號: 只能是10開頭, 尾數1~9重複兩次");
	            }
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productReport/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				Integer prod_report_product_id = null;
				try {
					prod_report_product_id = new Integer(str);
				} catch (Exception e) {
					errorMsgs.add("商品編號格式不正確");
				}
				
				
				String prod_report_mem_id = req.getParameter("prod_report_mem_id");
				String prod_report_mem_idReg = "^M(0){5}[1-9]$";
				if (prod_report_mem_id == null || prod_report_mem_id.trim().length() == 0) {
					errorMsgs.add("檢舉會員編號: 請勿空白");
				}else if(!prod_report_mem_id.trim().matches(prod_report_mem_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("檢舉會員編號: 只能是M00000開頭, 尾數1~9");
	            }
				
				
				
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productReport/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************2.開始查詢資料*****************************************/
				ProductReportService productReportSvc = new ProductReportService();
				ProductReportVO productReportVO = productReportSvc.getOneProductReport(prod_report_product_id,prod_report_mem_id);
				if (productReportVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productReport/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("productReportVO", productReportVO); // 資料庫取出的productReportVO物件,存入req
				String url = "/productReport/listOneProductReport.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneProductReport.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/productReport/select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
    

	}
}
