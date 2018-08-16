package com.productWishlist.controller;

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
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.productWishlist.model.ProductWishlistVO;
import com.productReport.model.ProductReportService;
import com.productReport.model.ProductReportVO;
import com.productWishlist.model.ProductWishlistService;

public class ProductWishlistServlet extends HttpServlet{
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
				String requestURL = req.getParameter("requestURL");
				try {
					/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
					Integer wishlist_product_id = new Integer(req.getParameter("wishlist_product_id").trim());
					String wishlist_mem_id = req.getParameter("wishlist_mem_id");
					String wishlist_mem_idReg = "^M(0){5}[1-9]$";
					if (wishlist_mem_id == null || wishlist_mem_id.trim().length() == 0) {
						errorMsgs.add("收藏會員編號: 請勿空白");
					}else if(!wishlist_mem_id.trim().matches(wishlist_mem_idReg)) { //以下練習正則(規)表示式(regular-expression)
						errorMsgs.add("收藏會員編號: 只能是M00000開頭, 尾數1~9");
		            }
	
					java.sql.Timestamp product_wishlist_time = new java.sql.Timestamp(System.currentTimeMillis());
	
					ProductWishlistVO productWishlistVO = new ProductWishlistVO();
					
					productWishlistVO.setWishlist_product_id(wishlist_product_id);
					productWishlistVO.setWishlist_mem_id(wishlist_mem_id);
					productWishlistVO.setProduct_wishlist_time(product_wishlist_time);
	
					// Send the use back to the form, if there were errors
					if (!errorMsgs.isEmpty()) {
						req.setAttribute("productWishlistVO", productWishlistVO); // 含有輸入格式錯誤的productWishlistVO物件,也存入req
						RequestDispatcher failureView = req
								.getRequestDispatcher("/productWishlist/addProductWishlist.jsp");
						failureView.forward(req, res);
						return; //程式中斷
					}
					
					/***************************2.開始修改資料*****************************************/
					ProductWishlistService productWishlistSvc = new ProductWishlistService();
					productWishlistVO = productWishlistSvc.addProductWishlist(wishlist_product_id,wishlist_mem_id,product_wishlist_time);

					int wishlikesize = productWishlistSvc.getLikesByProductid(wishlist_product_id).size();
					/***************************3.修改完成,準備轉交(Send the Success view)*************/
					req.setAttribute("productWishlistVO", productWishlistVO); // 資料庫update成功後,正確的的productWishlistVO物件,存入req
					res.setContentType("application/json");
			        PrintWriter out = res.getWriter();
			        JSONObject obj = new JSONObject();
			        obj.put("wishlikesize", wishlikesize);
			        out.print(obj);
					
				//	RequestDispatcher successView = req.getRequestDispatcher(requestURL); // 修改成功後,轉交listOneProductWishlist.jsp
				//	successView.forward(req, res);
	
					/***************************其他可能的錯誤處理*************************************/
				} catch (Exception e) {
					e.printStackTrace();
					errorMsgs.add("修改資料失敗:"+e.getMessage());
					RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
					failureView.forward(req, res);
				}
			
		}
		
		if ("delete".equals(action)) { // 來自listAllProductWishlist.jsp
			
				List<String> errorMsgs = new LinkedList<String>();
				// Store this set in the request scope, in case we need to
				// send the ErrorPage view.
				req.setAttribute("errorMsgs", errorMsgs);
				String requestURL = req.getParameter("requestURL");
		
				try {
					/***************************1.接收請求參數***************************************/
					Integer wishlist_product_id = new Integer(req.getParameter("wishlist_product_id"));
					String wishlist_mem_id = req.getParameter("wishlist_mem_id");
					
					/***************************2.開始刪除資料***************************************/
					
					ProductWishlistService productWishlistSvc = new ProductWishlistService();
					productWishlistSvc.deleteProductWishlist(wishlist_product_id, wishlist_mem_id);
					int wishlikesize = productWishlistSvc.getLikesByProductid(wishlist_product_id).size();
					/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
					res.setContentType("application/json");
			        PrintWriter out = res.getWriter();
			        JSONObject obj = new JSONObject();
			        obj.put("wishlikesize", wishlikesize);
			        out.print(obj);
			        
					//RequestDispatcher successView = req.getRequestDispatcher(requestURL);// 刪除成功後,轉交回送出刪除的來源網頁
					//successView.forward(req, res);
					
					/***************************其他可能的錯誤處理**********************************/
				} catch (Exception e) {
					errorMsgs.add("刪除資料失敗:"+e.getMessage());
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productWishlist/listAllProductWishlist.jsp");
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
				String str = req.getParameter("wishlist_product_id");
				String wishlist_product_idReg = "^10([0-9]){2}$";
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入收藏商品編號");
				}else if(!str.trim().matches(wishlist_product_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("商品編號: 只能是10開頭, 尾數1~9重複兩次");
	            }
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productWishlist/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				Integer wishlist_product_id = null;
				try {
					wishlist_product_id = new Integer(str);
				} catch (Exception e) {
					errorMsgs.add("商品編號格式不正確");
				}
				
				
				String wishlist_mem_id = req.getParameter("wishlist_mem_id");
				String wishlist_mem_idReg = "^M(0){5}[1-9]$";
				if (wishlist_mem_id == null || wishlist_mem_id.trim().length() == 0) {
					errorMsgs.add("檢舉會員編號: 請勿空白");
				}else if(!wishlist_mem_id.trim().matches(wishlist_mem_idReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("檢舉會員編號: 只能是M00000開頭, 尾數1~9");
	            }
				
				
				
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productWishlist/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************2.開始查詢資料*****************************************/
				ProductWishlistService productWishlistSvc = new ProductWishlistService();
				ProductWishlistVO productWishlistVO = productWishlistSvc.getOneProductWishlist(wishlist_product_id,wishlist_mem_id);
				if (productWishlistVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/productWishlist/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("productWishlistVO", productWishlistVO); // 資料庫取出的productWishlistVO物件,存入req
				String url = "/productWishlist/listOneProductWishlist.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneProductWishlist.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/productWishlist/select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
	}  
}
