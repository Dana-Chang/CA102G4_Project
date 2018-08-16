package com.shopping.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;


import com.ord.model.OrdService;
import com.ord.model.OrdVO;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.productReport.model.ProductReportService;
import com.shopping.model.CartBean;
import com.shopping.model.CartItem;


public class ShoppingServlet extends HttpServlet {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

		System.out.println(req.getRequestURI());
		System.out.println(action);

		boolean login_state = Boolean.valueOf(req.getParameter("login_state"));
		HttpSession session = req.getSession();

		if (action != null && !action.equals("")) {
			if (action.equals("ADD")) {
				
				if (login_state == true) {
					int total_items = addToCart(req);
					PrintWriter w = res.getWriter();
					w.print(total_items);
					session.setAttribute("total_items", total_items);
				} else if (login_state != true) {
					PrintWriter w = res.getWriter();
					w.print("not log in");
				}
			} else if (action.equals("UPDATE")) {
				int total_items = updateCart(req);
				PrintWriter w = res.getWriter();
				w.print(total_items);
				session.setAttribute("total_items", total_items);
			} else if (action.equals("DELETE")) {
				int total_items = deleteCart(req);
				PrintWriter w = res.getWriter();
				w.print(total_items);
				session.setAttribute("total_items", total_items);
			} else if (action.equals("CHECKOUT")) {
				checkOutCart(req, res);
			} else if (action.equals("CHECKOUT_TO_PAY")) {
				checkOutPay(req, res);
			}else if(action.equals("confirmCredit")) {
				confirmCredit(req, res);
			}else if(action.equals("CHECKOUT_COMPLETE")) {
				checkOutComplete(req, res);
			}else if(action.equals("updateOrdCancel")) {
				cancelOrder(req, res);
			}else if(action.equals("confirmDelivery")) {
				confirmDelivery(req, res);
			}else if(action.equals("confirmDeliveryBySeller")) {
				confirmDeliveryBySeller(req, res);
			}else if(action.equals("confirmShip")) {
				confirmShip(req, res);
			}else if(action.equals("ratingOrder")) {
				ratingOrder(req, res);
			}else if(action.equals("reportProd")) {
				
					reportProd(req, res);
			
			}
			
		}
	}

	private void reportProd(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		try {
			Integer product_id = Integer.parseInt(req.getParameter("product_id"));
			String memId = req.getParameter("memId");
			String reportDescr = req.getParameter("reportDescr");
			
			ProductReportService productReportSvc = new ProductReportService();
			productReportSvc.addProductReport(product_id,memId,reportDescr,new Timestamp(System.currentTimeMillis()),1);
		}catch(Exception e) {
			res.setContentType("text/html;charset=Big5");
			PrintWriter w = res.getWriter();
			w.print("您已經檢舉過囉!");
		}
		

	}

	private void ratingOrder(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String ordId = req.getParameter("ordId");
		Integer bors = Integer.parseInt(req.getParameter("bors"));
		String ratingDescr = req.getParameter("ratingDescr");
		Integer rating = null;
	
		if(req.getParameter("rating").trim().length() != 0) {

			rating = Integer.parseInt(req.getParameter("rating"));
		}
		
		if(rating != null) {
			OrdService ordSvc = new OrdService();
			OrdVO ordVO = ordSvc.getOneOrd(ordId);
			
			//bors 1>買家給賣家評價 2>賣家給買家評價
			if(bors == 1) {
				ordVO.setBtos_rating(rating);
				ordVO.setBtos_rating_descr(ratingDescr);
			}else if(bors == 2) {
				ordVO.setStob_rating(rating);
				ordVO.setStob_rating_descr(ratingDescr);
			}
			ordSvc.updateOrd(ordVO);
		}else {
		
			PrintWriter w = res.getWriter();
			w.print("not null");
		}
		
		
	}

	private void confirmShip(HttpServletRequest req, HttpServletResponse res) {
		String ordId = req.getParameter("ordId");
		String shipId = req.getParameter("shipId");
		OrdService ordSvc = new OrdService();
		OrdVO ordVO = ordSvc.getOneOrd(ordId);
		ordVO.setShipment_id(shipId);
		ordVO.setShipment_status(2);
		ordVO.setOrder_status(2);
		ordSvc.updateOrd(ordVO);
		
	}

	private void confirmDeliveryBySeller(HttpServletRequest req, HttpServletResponse res) {
		String ordId = req.getParameter("ordId");

		OrdService ordSvc = new OrdService();
		OrdVO ordVO = ordSvc.getOneOrd(ordId);
		ordVO.setShipment_status(3);//3>已到貨
		ordSvc.updateOrd(ordVO);
		
	}

	private void confirmDelivery(HttpServletRequest req, HttpServletResponse res) {
		String ordId = req.getParameter("ordId");

		OrdService ordSvc = new OrdService();
		OrdVO ordVO = ordSvc.getOneOrd(ordId);
		ordVO.setPayment_status(3);//款項給賣家
		ordVO.setOrder_status(3);//已完成訂單
		ordSvc.updateOrd(ordVO);
	}

	private void cancelOrder(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String ordId = req.getParameter("ordId");
		Integer reason = Integer.parseInt(req.getParameter("reason"));
		
		OrdService ordSvc = new OrdService();
		OrdVO ordVO = ordSvc.getOneOrd(ordId);
		if(ordVO.getPayment_method()==1){
			ordVO.setPayment_status(4);//信用卡付款，取消訂單把付款狀態改為退款
		}
		ordVO.setCancel_reason(reason);
		ordVO.setOrder_date(new Timestamp(System.currentTimeMillis()));
		ordVO.setOrder_status(4);
		ordSvc.updateOrd(ordVO);

	}

	private void checkOutComplete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
			String requestURL = req.getParameter("requestURL");
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			//1>信用卡2>貨到付款
		    //1>待付款2>已付款(TM)
			Integer payment_method = new Integer(req.getParameter("payment"));
			Integer payment_status =null;

			String paymentByCard = req.getParameter("paymentByCard");
			
			String sellerListStr = req.getParameter("sellerListStr");
			String productIdListStr = req.getParameter("productIdListStr");
			
			String addr = req.getParameter("addr");
			String ord_store_711_name = req.getParameter("storeName");
			Integer shipment_method = Integer.parseInt(req.getParameter("shipMethod"));
			String BuyerMemId = req.getParameter("BuyerMemId");
			
			HttpSession session = req.getSession();
			CartBean cartBean = (CartBean)session.getAttribute("cart");
			List<CartItem> CartItemList = cartBean.getCartItems();
			List<CartItem> CheckOutItemList =new ArrayList<CartItem>();
			String token = (String)session.getAttribute("token");
			String judgeDuplicate = req.getParameter("judgeDuplicate");
			System.out.println(token);
			System.out.println(judgeDuplicate);
			if(StringUtils.isNotBlank(token) && token.equals(judgeDuplicate)) {
				try {
						System.out.println("付款方法"+payment_method);
	
						if(payment_method == 1 && paymentByCard==null) {
							errorMsgs.add("選擇信用卡付款請填寫信用卡資訊!");
						}
						
						if(payment_method == 1) {
							payment_status = 2;
						}else if(payment_method == 2) {
							payment_status = 1;
						}
						
						
						Map<String,List<CartItem>> orderMap = new HashMap<String,List<CartItem>>();
						List<String> sellerList = new ArrayList<String>(Arrays.asList(sellerListStr.substring(1,sellerListStr.indexOf("]")).split(", ")));
						List<String> productIdList = new ArrayList<String>(Arrays.asList(productIdListStr.substring(1,productIdListStr.indexOf("]")).split(", ")));
						
						for (String e : sellerList) { 
					         System.out.println("賣家:"+e);  
					         orderMap.put(e, new ArrayList<CartItem>());
					      }  
						for (String e : productIdList) { 
					         System.out.println("商品:"+e);  
					        
					      }  
						
						for(CartItem c : CartItemList){
							String prodId = String.valueOf(c.getProduct_id());
							if(productIdList.contains(prodId)) CheckOutItemList.add(c);
						}
	
						
						for(CartItem c : CheckOutItemList){
							String prodSellerId = String.valueOf(c.getProduct_mem_id());
							if(orderMap.containsKey(prodSellerId)) {
								ArrayList<CartItem> cartItemArray= (ArrayList<CartItem>) orderMap.get(prodSellerId);
								cartItemArray.add(c);
							}			
						}
						
						
						
						// Send the use back to the form, if there were errors
						if (!errorMsgs.isEmpty()) {
							RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
							failureView.forward(req, res);
							return; // 程式中斷
						}
					/***************************2.開始修改資料*****************************************/
						
						OrdService ordSvc = new OrdService();
						for(Map.Entry<String, List<CartItem>> entry : orderMap.entrySet()) {
						    String seller_mem_id = entry.getKey();
						    List<CartItem> list = entry.getValue();
						    int order_item = list.size();
						    int order_total = 0;
	
						    for(int i = 0;i<list.size();i++) {
						    	CartItem c=list.get(i);
						    	order_total = order_total + c.getTotal_price();
						    }
						    
						    OrdVO ordVO = new OrdVO();
						    ordVO = ordSvc.addOrdWithDetails(BuyerMemId,seller_mem_id,addr,payment_status,payment_method,1,
						    		new Timestamp(System.currentTimeMillis()),1,order_total,order_item,shipment_method,ord_store_711_name,list);
						   //從購物車移除結帳商品 
						    for(int i = 0;i<list.size();i++) {
						    	CartItem c=list.get(i);
						    	cartBean.deleteCartItemById(c.getProduct_id());
						    }
						    int total_items = cartBean.getOrderTotalItems();
						    session.setAttribute("total_items", total_items);
						}
								
					/*************************** 3.接收完成,準備轉交(Send the Success view) ***********/
					String url = "/front_end/store/store_checkout_3.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url);
					successView.forward(req, res);
					
					session.removeAttribute("token");
	
				} catch (Exception e) {
					e.printStackTrace();
					req.setAttribute("errorMsgs", errorMsgs);
					RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
					failureView.forward(req, res);
				}
			}else {
				String url = "/front_end/store/store_cart.jsp";
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req, res);
			}
			
	}

	private void confirmCredit(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		List<String> errorMsgs = new LinkedList<String>();
		req.setAttribute("errorMsgs", errorMsgs);
		
		String cardNum = req.getParameter("cardNum");
		String ccv = req.getParameter("ccv");
		String cardNumReg = "^4[0-9]{12}(?:[0-9]{3})?$";
		if (cardNum == null || cardNum.trim().length() == 0) {
			errorMsgs.add("卡號: 請勿空白");
		} else if(!cardNum.trim().matches(cardNumReg)) { //以下練習正則(規)表示式(regular-expression)
			errorMsgs.add("卡號有誤!");
        }
		
		if(ccv.trim().length() != 3) {
			errorMsgs.add("ccv 有誤");
		}
		
		
		if (!errorMsgs.isEmpty()) {
			res.setContentType("text/html;charset=Big5");
			PrintWriter w = res.getWriter();
			w.print(errorMsgs.toString());
		}else {
			res.setContentType("text/html;charset=utf-8");
			PrintWriter w = res.getWriter();
			w.print("ok");
		}
	
	}

	protected void checkOutCart(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("進入checkout!");
		HttpSession session = req.getSession();
		List<String> errorMsgs = new LinkedList<String>();
		req.setAttribute("errorMsgs", errorMsgs);
		List<CartItem> orderItemList = new Vector<>();
		String requestURL = req.getParameter("requestURL");
		int ord_total_prie = 0;
		int ord_total_items = 0;
		String[] productIdList = null;
		try {
			if (req.getParameterValues("productId") != null) {
				productIdList = req.getParameterValues("productId"); // 商品編號[1006,1026]
				String[] productQtyList = req.getParameterValues("productQty"); // 商品數量[2,3]
				String[] productNameList = req.getParameterValues("productName"); // 商品名稱
				String[] productMemIdList = req.getParameterValues("productMemId"); // 賣家編號
				String[] productPriceList = req.getParameterValues("productPrice"); // 商品售價
				String[] productTotalPrice = req.getParameterValues("productTotalPrice"); // 商品總金額

				for (int i = 0; i < productIdList.length; i++) {

					Integer prodId = Integer.parseInt(productIdList[i]);
					Integer prodQty = Integer.parseInt(productQtyList[i]);
					System.out.println(prodQty);
					String prodName = productNameList[i];
					String prodMemId = productMemIdList[i];
					Integer prodPrice = Integer.parseInt(productPriceList[i]);
					Integer prodTotalPrice = Integer.parseInt(productTotalPrice[i]);

					CartItem orderItem = new CartItem();
					orderItem.setProduct_id(prodId);
					orderItem.setQuantity(prodQty);
					orderItem.setProduct_name(prodName);
					orderItem.setProduct_mem_id(prodMemId);
					orderItem.setProduct_price(prodPrice);
					orderItem.setTotal_price(prodTotalPrice);

					orderItemList.add(orderItem);

					ord_total_prie += prodTotalPrice;
					ord_total_items += prodQty;
				}

			} else {
				errorMsgs.add("請至少選擇一個商品進行結帳!");
			}

			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
				return; // 程式中斷
			}

			/*************************** 3.接收完成,準備轉交(Send the Success view) ***********/
			req.setAttribute("orderItemList", orderItemList);
			req.setAttribute("ord_total_prie", ord_total_prie);
			req.setAttribute("ord_total_items", ord_total_items);
			req.setAttribute("productIdList", productIdList);
			
			UUID uuid = UUID.randomUUID();
		    session.setAttribute("token",uuid.toString());//給下訂單按鈕用的token
		     
		     
			JSONObject orderItemListJ = new JSONObject();
			orderItemListJ.put("orderItemList", orderItemList);
			String orderItemListStr = orderItemListJ.toString();
			req.setAttribute("orderItemListStr", orderItemListStr);

			String url = "/front_end/store/store_checkout_1.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);

		} catch (Exception e) {
			req.setAttribute("errorMsgs", errorMsgs);
			RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
			failureView.forward(req, res);
		}
	}

	protected void checkOutPay(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("進入checkout pay!");
		HttpSession session = req.getSession();
		List<String> errorMsgs = new LinkedList<String>();
		req.setAttribute("errorMsgs", errorMsgs);

		String requestURL = req.getParameter("requestURL");

		Integer shipMethod = new Integer(req.getParameter("shipMethod"));
		String storeName = req.getParameter("storeName");
		String addr = req.getParameter("addr");
		String sellerListStr = req.getParameter("sellerListStr");
		String productIdListStr = req.getParameter("productIdListStr");
		String judgeDuplicate = req.getParameter("judgeDuplicate");
		String token = (String)session.getAttribute("token");
		System.out.println(token);
		System.out.println(judgeDuplicate);
		if(StringUtils.isNotBlank(token) && token.equals(judgeDuplicate)) {
			try {
				System.out.println(sellerListStr);
				System.out.println(shipMethod);
				System.out.println(storeName);
				System.out.println(addr);
				System.out.println(productIdListStr);
	
			
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
					failureView.forward(req, res);
					return; // 程式中斷
				}
	
				/*************************** 3.接收完成,準備轉交(Send the Success view) ***********/
				
				req.setAttribute("storeName", storeName);
				req.setAttribute("addr", addr);
				req.setAttribute("shipMethod", shipMethod);
				req.setAttribute("productIdListStr", productIdListStr);
				req.setAttribute("sellerListStr", sellerListStr);
				
				session.removeAttribute("token");
	
				String url = "/front_end/store/store_checkout_2.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
	
			    
			} catch (Exception e) {
				e.printStackTrace();
				req.setAttribute("errorMsgs", errorMsgs);
				RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}else {
			String url = "/front_end/store/store_cart.jsp";
			RequestDispatcher failureView = req.getRequestDispatcher(url);
			failureView.forward(req, res);
		}
	}

	protected int deleteCart(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String itemIndex = req.getParameter("itemIndex");
		Integer prod_id = Integer.parseInt(itemIndex);

		CartBean cartBean = null;

		Object objCartBean = session.getAttribute("cart");

		cartBean = (CartBean) objCartBean;
		cartBean.deleteCartItemById(prod_id);
		int TotalItems = cartBean.getOrderTotalItems();
		return TotalItems;
	}

	protected int updateCart(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String quantity = req.getParameter("quantity");
		String itemIndex = req.getParameter("itemIndex");
		Integer prod_id = Integer.parseInt(itemIndex);

		CartBean cartBean = null;

		Object objCartBean = session.getAttribute("cart");
		cartBean = (CartBean) objCartBean;
		cartBean.updateCartItemById(prod_id, quantity);
		int TotalItems = cartBean.getOrderTotalItems();
		return TotalItems;
	}

	protected int addToCart(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String product_id = req.getParameter("product_id");
		String product_name = req.getParameter("product_name");
		String product_price = req.getParameter("product_price");
		String quantity = req.getParameter("quantity");
		String product_mem_id = req.getParameter("product_mem_id");
		CartBean cartBean = null;
		Object objCartBean = session.getAttribute("cart");

		if (objCartBean != null) {
			cartBean = (CartBean) objCartBean;

		} else {
			cartBean = new CartBean();
			session.setAttribute("cart", cartBean);

		}

		cartBean.addCartItem(product_id, product_name, product_price, quantity, product_mem_id);
		int TotalItems = cartBean.getOrderTotalItems();
		return TotalItems;
	}

}
