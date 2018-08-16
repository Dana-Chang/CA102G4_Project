package com.grp_mem.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.grp.model.GrpService;
import com.grp.model.GrpVO;
import com.grp_mem.model.Grp_memService;
import com.grp_mem.model.Grp_memVO;
import com.mem.model.MemberService;
import com.mem.model.MemberVO;
import com.mysql.fabric.xmlrpc.base.Value;
import com.photo_wall_like.model.photo_wall_likeService;

public class Grp_memServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
	    res.setContentType("text/html; charset=UTF-8");
	    String action = req.getParameter("action");
	    PrintWriter out = res.getWriter();
	   	HttpSession session = req.getSession();
	   	
	   		
	   	System.out.println("有進來報名揪團嗎");

	   	if("insert".equals(action)) {
			
		   	System.out.println("有人來報名揪團喔");

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs",errorMsgs);

			try {
				/*******************第一步：錯誤處理*******************/

			String grp_Id = req.getParameter("grp_Id");
			
			System.out.println(grp_Id);
			
			String mem_Id = req.getParameter("mem_Id");
			
			System.out.println(mem_Id);

			String grp_Leader = req.getParameter("grp_Leader");
			
			System.out.println(grp_Leader);
				
			Grp_memVO grp_memVO = new Grp_memVO();
				
  			grp_memVO.setGrp_Id(grp_Id);
			grp_memVO.setMem_Id(mem_Id);
  			grp_memVO.setGrp_Leader(grp_Leader);

				//若以上驗證有錯誤訊息的話，將會於頁面顯示出錯誤部分
				if(!errorMsgs.isEmpty()) {
					req.setAttribute("grp_memVO", grp_memVO);
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/grp/grp_oneview.jsp");
					failureView.forward(req, res);
					return;
				}
				
				//************************第二步：新增資料**************************//
				Grp_memService grp_memSvc = new Grp_memService();
				grp_memSvc.insert(grp_Id,mem_Id,grp_Leader);

				//************************第三步：新增完成，準備提交**************************
				System.out.println("有轉交嗎");

				String url = "/front_end/grp/personal_grp_review.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
	
			}catch(Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher filureView = req.getRequestDispatcher("/front_end/grp/grpIndex.jsp");
				filureView.forward(req, res);
			}
			
		}
	   	
	   	
	   	
	   	/*************************** 參加揪團 ********************************/
		if ("collect".equals(action)) {
			System.out.println("==================================");
			res.setContentType("text/html;charset=UTF-8");
			List<String> errorMsgs = new LinkedList<String>();

			try {
	
				/*************************** 1.接收請求參數 ***************************************/

				String grp_Id = req.getParameter("grp_Id");
				// 這裡要從session取得登入會員的ID
				String mem_Id = req.getParameter("mem_Id");
				
				String grp_Leader = req.getParameter("grp_Leader");
				
				//從前台接可接受報名人數
				Integer grp_Cnt = Integer.parseInt(req.getParameter("grp_Cnt"));
				
				//從前台接AJAX的joinOrNot
				String joinOrNot = req.getParameter("joinOrNot");
				
				
				/*************************** 2.資料庫GRP_MEM增加一筆 ***************************************/

				Grp_memService grp_memSvc = new Grp_memService();
				
				Grp_memVO cnt = grp_memSvc.findByPrimaryKey(grp_Id, mem_Id);
				
				System.out.println("grp_Id1="+grp_Id);
				System.out.println("mem_Id2="+mem_Id);
				System.out.println("grp_Cnt="+grp_Cnt);

				GrpService grpSvc = new GrpService();

				GrpVO grpVO = grpSvc.findByPrimaryKey(grp_Id);
				
//				
//				// 揪團可報名人數、接受人數 確定參加會減少
//
				GrpService grpSvc_less = new GrpService();
				Integer less = grpSvc_less.update_mem_less(grp_Id,grp_Cnt).getGrp_Cnt();
//				
//				
//				// 揪團可報名人數、接受人數 取消參加會增加	 
				GrpService grpSvc_plus = new GrpService();
				Integer plus = grpSvc_plus.update_mem_plus(grp_Id,grp_Cnt).getGrp_Cnt();
				
				
				if(joinOrNot.equals("取消參加")) {
					grpSvc_less.update_mem_less(grp_Id,grp_Cnt);
					grp_Cnt -= 1;
					System.out.println("grp_Cnt2="+grp_Cnt);

					grpSvc_less.update_mem_less(grp_Id,grp_Cnt);
					Integer mem_less = grpVO.getGrp_Cnt();
					
					out.print(grp_Cnt);
					
				}
				
				if(joinOrNot.equals("我要參加")){
					grpSvc_plus.update_mem_plus(grp_Id,grp_Cnt);
//					grp_Cnt += 1;
					System.out.println("grp_Cnt3="+grp_Cnt);
					grpSvc_plus.update_mem_plus(grp_Id,grp_Cnt);
					Integer mem_plus = grpVO.getGrp_Cnt();
					out.print(grp_Cnt);
					
				}
				
				
				System.out.println("找麻煩?");
			    if (cnt == null) {
					grp_memSvc.joingrp(grp_Id, mem_Id,grp_Leader);
					String thank = "感謝您的報名";
					out.print(thank);
					
					
				} else {
					grp_memSvc.delete(grp_Id, mem_Id);
					String cancel = "已取消報名";
					out.print(cancel);
					
					
				}
			    

//				System.out.println("cnt="+cnt.getMem_Id());
//				
//				if(mem_Id.equals(cnt.getMem_Id())) {
//				     errorMsgs.add("請不要檢舉自己的文章!");
//				     errorMsgs.add("你是不是在找麻煩!");
//				    }
//				    
//			    if (!errorMsgs.isEmpty()) {
//			     RequestDispatcher failureView = req
//			       .getRequestDispatcher("/front_end/grp/personal_area_grp.jsp");
//			     failureView.forward(req, res);
//			     return;
//			    }
			    
				/*************************** 3.收藏完成，準備轉交(Send the Success view) ***********/

				// 此為AJAX寫法，不用轉交

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
				out.print("請先登入在參加揪團喔");
			}
		}
		
		
		
		
		if ("delete".equals(action)) { 

			   List<String> errorMsgs = new LinkedList<String>();
			   req.setAttribute("errorMsgs", errorMsgs);
			 
			   try {
			    /***************************1.接收請求參數***************************************/
			    String grp_Id = req.getParameter("grp_Id");
			    String mem_Id = req.getParameter("mem_Id");

			    /***************************2.開始刪除資料***************************************/
			    Grp_memService articleSvc = new Grp_memService();
			    articleSvc.delete(grp_Id,mem_Id); 
			    System.out.println("刪除成功");
			    /***************************3.刪除完成,準備轉交(Send the Success view)***********/        
			    String url = "/front_end/grp/personal_grp_review.jsp";
			    RequestDispatcher successView = req.getRequestDispatcher(url);
			    successView.forward(req, res);
			    
			    /***************************其他可能的錯誤處理**********************************/
			   } catch (Exception e) {
			    errorMsgs.add("刪除資料失敗:"+e.getMessage());
			    RequestDispatcher failureView = req
			      .getRequestDispatcher("/front_end/grp_mem/personal_grp_review.jsp");
			    failureView.forward(req, res);
			   }
			  }
		
		
		if ("update".equals(action)) { 
        	System.out.println("有人來報名耶");
			List<String> errorMsgs = new LinkedList<String>();
		
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				
				String grp_Id = req.getParameter("grp_Id");
				
				System.out.println("grp_Id="+grp_Id);
				
				String mem_Id = req.getParameter("mem_Id");
				
				System.out.println("mem_Id="+mem_Id);

				String grp_Leader = req.getParameter("grp_Leader");
				
				System.out.println("grp_Leader="+grp_Leader);
					
				
				Grp_memVO grp_memVO = new Grp_memVO();
				
	  			grp_memVO.setGrp_Id(grp_Id);
	  			
				grp_memVO.setMem_Id(mem_Id);
				
	  			grp_memVO.setGrp_Leader(grp_Leader);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("grp_memVO", grp_memVO); 
					System.out.println("參加失敗囉");
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/grp/personal_grp_review.jsp");
					failureView.forward(req, res);
					return; //程式中斷
					
				}
				/***************************2.開始修改資料*****************************************/
				
				System.out.println("修改狀態快要成功");
				Grp_memService grp_memSvc = new Grp_memService();
				grp_memSvc.update(grp_Id,mem_Id,grp_Leader);;
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				if(grp_Leader.equals("1")) {
					req.setAttribute("display_tabs", "display");
				}
				
				req.setAttribute("grp_memVO", grp_memVO); 
				String url = "/front_end/grp/personal_grp_review.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res);
				System.out.println("大師兄來參加了");
				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/grp/personal_grp_review.jsp");
				failureView.forward(req, res);
			}
		}
		
		
	}
}
