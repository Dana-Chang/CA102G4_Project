package com.product.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringUtils;

import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.productCategory.model.ProductCategoryService;
import com.productCategory.model.ProductCategoryVO;




@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
public class ProductServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		 if ("insert".equals(action)) { // 來自 front_end/store/store_add_product.jsp的請求  
			 HttpSession session = req.getSession();
			 String judgeDuplicate = req.getParameter("judgeDuplicate");
			 String token = (String) session.getAttribute("token");
			if(StringUtils.isNotBlank(token) && token.equals(judgeDuplicate)) {
			 	Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
				// Store this set in the request scope, in case we need to
				// send the ErrorPage view.
				req.setAttribute("errorMsgs", errorMsgs);
				String requestURL = req.getParameter("requestURL"); //送出刪除的來源網頁路徑:可能為 product/addProduct.jsp or front_end/store/store_add_product.jsp
				
				try {
					/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
					Integer product_category_id = new Integer(req.getParameter("product_category_id").trim());
					String product_mem_id = req.getParameter("product_mem_id");
					String product_name = req.getParameter("product_name");
					if (product_name == null || product_name.trim().length() == 0) {
						errorMsgs.put("product_name","商品名稱: 請勿空白");
					}
					String str = req.getParameter("product_price");
					Integer product_price = null;
					if (str == null || (str.trim()).length() == 0) {
						errorMsgs.put("product_price","請輸入商品價格");
					}else {
					try {
						product_price = new Integer(str);
					} catch (Exception e) {
						errorMsgs.put("product_price","商品價格格式不正確");
					}}
	
					String product_descr = req.getParameter("product_descr");
					if (product_descr == null || product_descr.trim().length() == 0) {
						errorMsgs.put("product_descr","商品描述: 請勿空白");
					} 

					String str2 = req.getParameter("product_stock");
					Integer product_stock = null;
					if (str2 == null || (str2.trim()).length() == 0) {
						errorMsgs.put("product_stock","請輸入商品庫存");
					}else {
					try {
						product_stock = new Integer(str2);
					} catch (Exception e) {
						errorMsgs.put("product_stock","商品庫存格式不正確");
					}}

					Integer product_status = new Integer(req.getParameter("product_status").trim());
					
					java.sql.Timestamp product_date = null;
					try {
						product_date = java.sql.Timestamp.valueOf(req.getParameter("product_date").trim());
					} catch (IllegalArgumentException e) {
						product_date = new java.sql.Timestamp(System.currentTimeMillis());
						errorMsgs.put("product_date","請輸入日期");
					}

					byte[] product_photo_1 =null;
					Part filePart = req.getPart("product_photo_1");
				//把filePart印出來，其實不是null
					
					if(filePart != null && filePart.getSize() > 0) {
						InputStream fileContent = filePart.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        byte[] buffer = new byte[1024];
				        int readBytes = -1;
		
				        while((readBytes = fileContent.read(buffer)) > 1){
				            baos.write(buffer,0,readBytes);
				        }
				        
				        baos.flush();
				        baos.close();
				        fileContent.close();
		
				        product_photo_1 = baos.toByteArray();
			        }else { 
			        	
			        	if(req.getParameter("product_photo_1_base")!=null && !req.getParameter("product_photo_1_base").equals("null")) {
			        		final Base64.Decoder decoder = Base64.getDecoder();
			        		product_photo_1 = decoder.decode(req.getParameter("product_photo_1_base"));
			        	}else {
			        	product_photo_1 = null;
			        	}
			        }
					
					
					byte[] product_photo_2 =null;
					Part filePart2 = req.getPart("product_photo_2");
					if(filePart2 != null && filePart2.getSize() > 0) {
						InputStream fileContent = filePart2.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        byte[] buffer = new byte[1024];
				        int readBytes = -1;
		
				        while((readBytes = fileContent.read(buffer)) > 1){
				            baos.write(buffer,0,readBytes);
				        }
				        baos.flush();
				        baos.close();
				        fileContent.close();
				        product_photo_2 = baos.toByteArray();
			        }else {
			        	
			        	if(req.getParameter("product_photo_2_base")!=null && !req.getParameter("product_photo_2_base").equals("null")) {
			        		final Base64.Decoder decoder = Base64.getDecoder();
			        		product_photo_2 = decoder.decode(req.getParameter("product_photo_2_base"));
			        	}else {
			        		product_photo_2 = null;
			        	}
			        }
					
					
					byte[] product_photo_3 =null;
					Part filePart3 = req.getPart("product_photo_3");
					if(filePart3 != null && filePart3.getSize() > 0) {
						InputStream fileContent = filePart3.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        byte[] buffer = new byte[1024];
				        int readBytes = -1;
		
				        while((readBytes = fileContent.read(buffer)) > 1){
				            baos.write(buffer,0,readBytes);
				        }
				        baos.flush();
				        baos.close();
				        fileContent.close();
				        product_photo_3 = baos.toByteArray();
			        }else {
			        	
			        	if(req.getParameter("product_photo_3_base")!=null && !req.getParameter("product_photo_3_base").equals("null")) {
			        		final Base64.Decoder decoder = Base64.getDecoder();
			        		product_photo_3 = decoder.decode(req.getParameter("product_photo_3_base"));
			        	}else {
			        	product_photo_3 = null;
			        	}
			        
			        }
					
					
					byte[] product_photo_4 =null;
					Part filePart4 = req.getPart("product_photo_4");
					if(filePart4 != null && filePart4.getSize() > 0) {
						InputStream fileContent = filePart4.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        byte[] buffer = new byte[1024];
				        int readBytes = -1;
		
				        while((readBytes = fileContent.read(buffer)) > 1){
				            baos.write(buffer,0,readBytes);
				        }
				        baos.flush();
				        baos.close();
				        fileContent.close();
		
				        product_photo_4 = baos.toByteArray();
			        }else {
			        	
			        	if(req.getParameter("product_photo_4_base")!=null && !req.getParameter("product_photo_4_base").equals("null")) {
			        		final Base64.Decoder decoder = Base64.getDecoder();
			        		product_photo_4 = decoder.decode(req.getParameter("product_photo_4_base"));
			        	}else {
			        	product_photo_4 = null;
			        	}
			        	
			        }
					
					
					byte[] product_photo_5 =null;
					Part filePart5 = req.getPart("product_photo_5");
					if(filePart5 != null && filePart5.getSize() > 0) {
						InputStream fileContent = filePart5.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        byte[] buffer = new byte[1024];
				        int readBytes = -1;
		
				        while((readBytes = fileContent.read(buffer)) > 1){
				            baos.write(buffer,0,readBytes);
				        }
				        baos.flush();
				        baos.close();
				        fileContent.close();
				        product_photo_5 = baos.toByteArray();
			        }else {     
			        	
			        	if(req.getParameter("product_photo_5_base")!=null && !req.getParameter("product_photo_5_base").equals("null")) {
			        		final Base64.Decoder decoder = Base64.getDecoder();
			        		product_photo_5 = decoder.decode(req.getParameter("product_photo_5_base"));
			        	}else {
			        		product_photo_5 = null;
			        	}
			        	
			        }
					
					if(product_photo_1 == null && product_photo_2 == null && product_photo_3 == null && product_photo_4 == null && product_photo_5 == null) {
						errorMsgs.put("product_photo","請上傳至少一張照片");
					}
					
					ProductVO productVO = new ProductVO();
					
					productVO.setProduct_category_id(product_category_id);
					productVO.setProduct_mem_id(product_mem_id);
					productVO.setProduct_name(product_name);
					productVO.setProduct_price(product_price);
					productVO.setProduct_descr(product_descr);
					productVO.setProduct_stock(product_stock);
					productVO.setProduct_status(product_status);
					productVO.setProduct_date(product_date);
					productVO.setProduct_photo_1(product_photo_1);
					productVO.setProduct_photo_2(product_photo_2);
					productVO.setProduct_photo_3(product_photo_3);
					productVO.setProduct_photo_4(product_photo_4);
					productVO.setProduct_photo_5(product_photo_5);

					// Send the use back to the form, if there were errors
					if (!errorMsgs.isEmpty()) {
						req.setAttribute("productVO", productVO); // 含有輸入格式錯誤的productVO物件,也存入req
						RequestDispatcher failureView = req
								.getRequestDispatcher("/front_end/store/store_add_product.jsp");
						failureView.forward(req, res);
						return; //程式中斷
					}
					
					/***************************2.開始修改資料*****************************************/
					ProductService productSvc = new ProductService();
					productVO = productSvc.addProduct(product_category_id,product_mem_id,product_name,product_price,product_descr,product_stock,product_status,product_date,product_photo_1,product_photo_2,product_photo_3,product_photo_4,product_photo_5);
					
					/***************************3.修改完成,準備轉交(Send the Success view)*************/
					req.setAttribute("productVO", productVO); // 資料庫update成功後,正確的的productVO物件,存入req
					if(requestURL.equals("/front_end/store/store_add_product.jsp")) {
						String url = "/front_end/personal_area/personal_area_sell.jsp";
						RequestDispatcher successView = req.getRequestDispatcher(url);
						successView.forward(req, res);
					}
					session.removeAttribute("token");
					/***************************其他可能的錯誤處理*************************************/
				} catch (Exception e) {
					e.printStackTrace();
					errorMsgs.put("修改資料失敗:",e.getMessage());
					RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
					failureView.forward(req, res);
				}}else {
					String url = "/front_end/personal_area/personal_area_sell.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url);
					successView.forward(req, res);
				}
			}
		
		if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			String requestURL = req.getParameter("requestURL");
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String str = req.getParameter("product_id");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入商品編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/product/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				Integer product_id = null;
				try {
					product_id = new Integer(str);
				} catch (Exception e) {
					errorMsgs.add("商品編號格式不正確");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/product/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************2.開始查詢資料*****************************************/
				ProductService productSvc = new ProductService();
				ProductVO productVO = productSvc.getOneProduct(product_id);
				if (productVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/product/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("productVO", productVO); // 資料庫取出的productVO物件,存入req
				String url = "/product/listOneProduct.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneProduct.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}
		
		if ("deleteByAjax".equals(action)) { // 來自listAllProduct.jsp or /front_end/personal_area/personal_area_sell.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
	
			String requestURL = req.getParameter("requestURL"); //送出刪除的來源網頁路徑:可能為 product/listAllProduct.jsp or /front_end/personal_area/personal_area_sell.jsp
			
			try {
				/***************************1.接收請求參數***************************************/
				Integer product_id = new Integer(req.getParameter("product_id"));
				
				/***************************2.開始刪除資料***************************************/
				ProductService productSvc = new ProductService();
				ProductVO productVO = productSvc.getOneProduct(product_id);
				productVO.setProduct_status(4);
				productSvc.updateProduct(productVO);
	
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
			}
		}
		
		if ("delete".equals(action)) { // 來自listAllProduct.jsp or /front_end/personal_area/personal_area_sell.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
	
			String requestURL = req.getParameter("requestURL"); //送出刪除的來源網頁路徑:可能為 product/listAllProduct.jsp or /front_end/personal_area/personal_area_sell.jsp
			
			try {
				/***************************1.接收請求參數***************************************/
				Integer product_id = new Integer(req.getParameter("product_id"));
				
				/***************************2.開始刪除資料***************************************/
				ProductService productSvc = new ProductService();
				productSvc.deleteProduct(product_id);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/
				
				String url = requestURL;
				RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}
		if ("getOne_For_Update".equals(action)) { //  來自listAllProduct.jsp or /front_end/personal_area/personal_area_sell.jsp
			System.out.println("get into getOne_For_Update");
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			String requestURL = req.getParameter("requestURL");
			System.out.println(requestURL);
			try {
				/***************************1.接收請求參數****************************************/
			Integer product_id = new Integer(req.getParameter("product_id"));

				/***************************2.開始查詢資料****************************************/
				ProductService productSvc = new ProductService();
				ProductVO productVO = productSvc.getOneProduct(product_id);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("productVO", productVO);         // 資料庫取出的productVO物件,存入req
				if(requestURL.equals("/front_end/personal_area/personal_area_sell.jsp")) {
					String url = "/front_end/store/store_update_product.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // 資料庫取出的list物件,存入request
					successView.forward(req, res);
				}
				
				if(requestURL.equals("/front_end/store/store_product.jsp")) {
					String url = "/front_end/store/store_update_product.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // 資料庫取出的list物件,存入request
					successView.forward(req, res);
				}

				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
		
				errorMsgs.put("無法取得要修改的資料:", e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}
		
		
		if ("update".equals(action)) { // 來自update_product_input.jsp的請求
			
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			String requestURL = req.getParameter("requestURL");
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				Integer product_id = new Integer(req.getParameter("product_id").trim());
				Integer product_category_id = new Integer(req.getParameter("product_category_id").trim());
				String product_mem_id = req.getParameter("product_mem_id");
				String product_name = req.getParameter("product_name");
				if (product_name == null || product_name.trim().length() == 0) {
					errorMsgs.put("product_name","商品名稱: 請勿空白");
				}
				String str = req.getParameter("product_price");
				Integer product_price = null;
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.put("product_price","請輸入商品價格");
				}else {
				try {
					product_price = new Integer(str);
				} catch (Exception e) {
					errorMsgs.put("product_price","商品價格格式不正確");
				}}

				String product_descr = req.getParameter("product_descr");
				if (product_descr == null || product_descr.trim().length() == 0) {
					errorMsgs.put("product_descr","商品描述: 請勿空白");
				} 

				String str2 = req.getParameter("product_stock");
				Integer product_stock = null;
				if (str2 == null || (str2.trim()).length() == 0) {
					errorMsgs.put("product_stock","請輸入商品庫存");
				}else {
				try {
					product_stock = new Integer(str2);
				} catch (Exception e) {
					errorMsgs.put("product_stock","商品庫存格式不正確");
				}}

				Integer product_status = new Integer(req.getParameter("product_status").trim());
				
				java.sql.Timestamp product_date = null;
				try {
					product_date = java.sql.Timestamp.valueOf(req.getParameter("product_date").trim());
				} catch (IllegalArgumentException e) {
					product_date = new java.sql.Timestamp(System.currentTimeMillis());
					errorMsgs.put("product_date","請輸入日期");
				}
				
				byte[] product_photo_1 =null;
				Part filePart = req.getPart("product_photo_1");
				
				if(filePart != null && filePart.getSize() > 0) {
					InputStream fileContent = filePart.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        byte[] buffer = new byte[1024];
			        int readBytes = -1;
	
			        while((readBytes = fileContent.read(buffer)) > 1){
			            baos.write(buffer,0,readBytes);
			        }
			        
			        baos.flush();
			        baos.close();
			        fileContent.close();
	
			        product_photo_1 = baos.toByteArray();
		        }else {
		        	//若input沒有圖，先檢查本來的欄位有沒有圖，有圖就保留本來欄位的圖
		        	ProductService productSvc = new ProductService();
		        	ProductVO productVO = productSvc.getOneProduct(product_id);
		        	if(productVO.getProduct_photo_1()!=null&&productVO.getProduct_photo_1().length>0) {
		        		product_photo_1 = productVO.getProduct_photo_1();
		        	}else {
		        	  product_photo_1 = null;
		        	}
		        }
				
				
				byte[] product_photo_2 =null;
				Part filePart2 = req.getPart("product_photo_2");
				if(filePart2 != null && filePart2.getSize() > 0) {
					InputStream fileContent = filePart2.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        byte[] buffer = new byte[1024];
			        int readBytes = -1;
	
			        while((readBytes = fileContent.read(buffer)) > 1){
			            baos.write(buffer,0,readBytes);
			        }
			        baos.flush();
			        baos.close();
			        fileContent.close();
			        product_photo_2 = baos.toByteArray();
		        }else {
		        	//若input沒有圖，先檢查本來的欄位有沒有圖，有圖就保留本來欄位的圖
		        	ProductService productSvc = new ProductService();
		        	ProductVO productVO = productSvc.getOneProduct(product_id);
		        	if(productVO.getProduct_photo_2()!=null&&productVO.getProduct_photo_2().length>0) {
		        		product_photo_2 = productVO.getProduct_photo_2();
		        	}else {
		        	  product_photo_2 = null;
		        	}
		        }
				
				
				byte[] product_photo_3 =null;
				Part filePart3 = req.getPart("product_photo_3");
				if(filePart3 != null && filePart3.getSize() > 0) {
					InputStream fileContent = filePart3.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        byte[] buffer = new byte[1024];
			        int readBytes = -1;
	
			        while((readBytes = fileContent.read(buffer)) > 1){
			            baos.write(buffer,0,readBytes);
			        }
			        baos.flush();
			        baos.close();
			        fileContent.close();
			        product_photo_3 = baos.toByteArray();
		        }else {
		        	//若input沒有圖，先檢查本來的欄位有沒有圖，有圖就保留本來欄位的圖
		        	ProductService productSvc = new ProductService();
		        	ProductVO productVO = productSvc.getOneProduct(product_id);
		        	if(productVO.getProduct_photo_3()!=null&&productVO.getProduct_photo_3().length>0) {
		        		product_photo_3 = productVO.getProduct_photo_3();
		        	}else {
		        	  product_photo_3 = null;
		        	}
		        }
				
				
				byte[] product_photo_4 =null;
				Part filePart4 = req.getPart("product_photo_4");
				if(filePart4 != null && filePart4.getSize() > 0) {
					InputStream fileContent = filePart4.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        byte[] buffer = new byte[1024];
			        int readBytes = -1;
	
			        while((readBytes = fileContent.read(buffer)) > 1){
			            baos.write(buffer,0,readBytes);
			        }
			        baos.flush();
			        baos.close();
			        fileContent.close();
	
			        product_photo_4 = baos.toByteArray();
		        }else {
		        	//若input沒有圖，先檢查本來的欄位有沒有圖，有圖就保留本來欄位的圖
		         	ProductService productSvc = new ProductService();
		        	ProductVO productVO = productSvc.getOneProduct(product_id);
		        	if(productVO.getProduct_photo_4()!=null&&productVO.getProduct_photo_4().length>0) {
		        		product_photo_4 = productVO.getProduct_photo_4();
		        	}else {
		        	  product_photo_4 = null;
		        	}
		        }
				
				
				byte[] product_photo_5 =null;
				Part filePart5 = req.getPart("product_photo_5");
				if(filePart5 != null && filePart5.getSize() > 0) {
					InputStream fileContent = filePart5.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        byte[] buffer = new byte[1024];
			        int readBytes = -1;
	
			        while((readBytes = fileContent.read(buffer)) > 1){
			            baos.write(buffer,0,readBytes);
			        }
			        baos.flush();
			        baos.close();
			        fileContent.close();
			        product_photo_5 = baos.toByteArray();
		        }else {
		        	//若input沒有圖，先檢查本來的欄位有沒有圖，有圖就保留本來欄位的圖
		        	ProductService productSvc = new ProductService();
		        	ProductVO productVO = productSvc.getOneProduct(product_id);
		        	if(productVO.getProduct_photo_5()!=null&&productVO.getProduct_photo_5().length>0) {
		        		product_photo_5 = productVO.getProduct_photo_5();
		        	}else {
		        	  product_photo_5 = null;
		        	}
		        }
				
				ProductVO productVO = new ProductVO();
				
				productVO.setProduct_category_id(product_category_id);
				productVO.setProduct_mem_id(product_mem_id);
				productVO.setProduct_name(product_name);
				productVO.setProduct_price(product_price);
				productVO.setProduct_descr(product_descr);
				productVO.setProduct_stock(product_stock);
				productVO.setProduct_status(product_status);
				productVO.setProduct_date(product_date);
				productVO.setProduct_photo_1(product_photo_1);
				productVO.setProduct_photo_2(product_photo_2);
				productVO.setProduct_photo_3(product_photo_3);
				productVO.setProduct_photo_4(product_photo_4);
				productVO.setProduct_photo_5(product_photo_5);
				productVO.setProduct_id(product_id);
				
				
			
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("productVO", productVO); // 含有輸入格式錯誤的productVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/store/store_update_product.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				ProductService productSvc = new ProductService();
				productVO = productSvc.updateProduct(product_category_id,product_mem_id,product_name,product_price,product_descr,product_stock,product_status,product_date,product_photo_1,product_photo_2,product_photo_3,product_photo_4,product_photo_5,product_id);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("productVO", productVO); // 資料庫update成功後,正確的的productVO物件,存入req
			
				if(requestURL.equals("/front_end/store/store_update_product.jsp")) {
					String url = "/front_end/personal_area/personal_area_sell.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // 資料庫取出的list物件,存入request
					successView.forward(req, res);
				}else if(requestURL.equals("/product/update_product_input.js")) {
					String url = "/product/listOneProduct.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneProduct.jsp
					successView.forward(req, res);
				}

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.put("修改資料失敗:",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}

		if ("listProducts_ByCompositeQuery".equals(action)) { // 來自select_page.jsp的複合查詢請求
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			System.out.println("進入複合查詢");
			try {
				
				/***************************1.將輸入資料轉為Map**********************************/ 
				//採用Map<String,String[]> getParameterMap()的方法 
				//注意:an immutable java.util.Map 
				//Map<String, String[]> map = req.getParameterMap();
				HttpSession session = req.getSession();
				
				String orderby = null;
				if(req.getParameter("orderby")!=null) {
					orderby = req.getParameter("orderby");
				}
				
				Integer PRODUCT_CATEGORY_ID=null;
				if(req.getParameter("PRODUCT_CATEGORY_ID")!=null) {
					PRODUCT_CATEGORY_ID = Integer.parseInt(req.getParameter("PRODUCT_CATEGORY_ID"));
				}
				System.out.println("進入複合查詢2");
				Map<String, String[]> map = (Map<String, String[]>)session.getAttribute("map");
				if (req.getParameter("whichPage") == null){
					HashMap<String, String[]> map1 = new HashMap<String, String[]>(req.getParameterMap());
					session.setAttribute("map",map1);
					map = map1;
				} 
				
				/***************************2.開始複合查詢***************************************/
				ProductService prodSvc = new ProductService();
				List<ProductVO> list  = prodSvc.getAll(map);
				
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				session.setAttribute("queryList", list); // 資料庫取出的list物件,存入request
				if(PRODUCT_CATEGORY_ID != null) {
					req.setAttribute("PRODUCT_CATEGORY_ID", PRODUCT_CATEGORY_ID);
				}
				
				if(orderby != null) {
					req.setAttribute("orderby", orderby);
				}
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/store/listProducts_ByCompositeQuery.jsp"); // 成功轉交listProducts_ByCompositeQuery.jsp
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		
	}		
}
