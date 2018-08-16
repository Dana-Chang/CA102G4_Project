package com.productCategory.controller;


import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.productCategory.model.ProductCategoryService;
import com.productCategory.model.ProductCategoryVO;

public class ProductCategoryServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		

        if ("insert".equals(action)) { // 來自addProductCategory.jsp的請求  
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				String product_category_name = req.getParameter("product_category_name");
				String product_category_nameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{1,20}$";
				if (product_category_name == null || product_category_name.trim().length() == 0) {
					errorMsgs.add("類別名稱: 請勿空白");
				} else if(!product_category_name.trim().matches(product_category_nameReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("類別名稱: 只能是中、英文字母、數字和_ , 且長度必需在1到20之間");
	            }
				
				ProductCategoryVO productCategoryVO = new ProductCategoryVO();
				productCategoryVO.setProduct_category_name(product_category_name);
				

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("productCategoryVO", productCategoryVO); // 含有輸入格式錯誤的productCategoryVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productCategory/addProductCategory.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.開始新增資料***************************************/
				ProductCategoryService productCategorySvc = new ProductCategoryService();
				productCategoryVO = productCategorySvc.addProductCategory(product_category_name);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				String url = "/productCategory/listAllProductCategory.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllProductCategory.jsp
				successView.forward(req, res);				
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/productCategory/addProductCategory.jsp");
				failureView.forward(req, res);
			}
		}
		
        
    	
		if ("delete".equals(action)) { // 來自listAllProductCategory.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
	
			try {
				/***************************1.接收請求參數***************************************/
				Integer product_category_id = new Integer(req.getParameter("product_category_id"));
				
				/***************************2.開始刪除資料***************************************/
				ProductCategoryService productCategorySvc = new ProductCategoryService();
				productCategorySvc.deleteProductCategory(product_category_id);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
				String url = "/productCategory/listAllProductCategory.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/productCategory/listAllProductCategory.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		if ("getOne_For_Update".equals(action)) { //  來自listAllProductCategory.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				Integer product_category_id = new Integer(req.getParameter("product_category_id"));
				
				/***************************2.開始查詢資料****************************************/
				ProductCategoryService productCategorySvc = new ProductCategoryService();
				ProductCategoryVO productCategoryVO = productCategorySvc.getOneProductCategory(product_category_id);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("productCategoryVO", productCategoryVO);         // 資料庫取出的productCategoryVO物件,存入req
				String url = "/productCategory/update_productCategory_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_productCategory_input.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/productCategory/listAllProductCategory.jsp");
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
				String str = req.getParameter("product_category_id");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入商品類別編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productCategory/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				Integer product_category_id = null;
				try {
					product_category_id = new Integer(str);
				} catch (Exception e) {
					errorMsgs.add("商品類別編號格式不正確");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productCategory/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************2.開始查詢資料*****************************************/
				ProductCategoryService productCategorySvc = new ProductCategoryService();
				ProductCategoryVO productCategoryVO = productCategorySvc.getOneProductCategory(product_category_id);
				if (productCategoryVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productCategory/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("productCategoryVO", productCategoryVO); // 資料庫取出的productCategoryVO物件,存入req
				String url = "/productCategory/listOneProductCategory.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneProductCategory.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/productCategory/select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
	
		if ("update".equals(action)) { // 來自update_productCategory_input.jsp的請求
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
		
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				Integer product_category_id = new Integer(req.getParameter("product_category_id").trim());
				
				String product_category_name = req.getParameter("product_category_name");
				String product_category_nameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{1,20}$";
				if (product_category_name == null || product_category_name.trim().length() == 0) {
					errorMsgs.add("類別名稱: 請勿空白");
				} else if(!product_category_name.trim().matches(product_category_nameReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("類別名稱: 只能是中、英文字母、數字和_ , 且長度必需在1到20之間");
	            }
				
				ProductCategoryVO productCategoryVO = new ProductCategoryVO();
				productCategoryVO.setProduct_category_id(product_category_id);
				productCategoryVO.setProduct_category_name(product_category_name);
			
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("productCategoryVO", productCategoryVO); // 含有輸入格式錯誤的productCategoryVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productCategory/update_productCategory_input.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				ProductCategoryService productCategorySvc = new ProductCategoryService();
				productCategoryVO = productCategorySvc.updateProductCategory(product_category_id, product_category_name);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("productCategoryVO", productCategoryVO); // 資料庫update成功後,正確的的productCategoryVO物件,存入req
				String url = "/productCategory/listOneProductCategory.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneProductCategory.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/productCategory/update_productCategory_input.jsp");
				failureView.forward(req, res);
			}
		}
		
		
	}
}
