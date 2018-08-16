package com.fri.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.fri.model.*;
import com.mem.model.*;




public class FriServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req,res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		res.setContentType("text/plain;charset=UTF-8");
		
		//送出好友邀請時
		if("insertFri".equals(action)) {
			List<String> errorMsgs_Ailee = new LinkedList<String>();
			req.setAttribute("errorMsgs_Ailee", errorMsgs_Ailee);
			String meId =null;
			String friId=null;
			
			try {
				/****************1.接受請求參數,錯誤驗證**********************/
				meId = req.getParameter("meId");
				friId  = req.getParameter("friId");
				
				if(meId == null || meId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者會員ID null!");
				}
				
				if(friId == null || friId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者好友的會員ID null!");
				}
				
				/***************新增前置作業，確認是是否重複新增???**********************/
				FriendService friSvc = new FriendService();
				Friend relationship = friSvc.findRelationship(meId, friId);
				
				if(relationship != null || (!errorMsgs_Ailee.isEmpty())) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
					failureView.forward(req, res);
					return;
				}
				/****************2.確認資料無誤，開始新增好友關係(狀態要設成待確認)**********************/		
				
				friSvc.insertFri(meId,friId,1);
				
				/****************3.準備轉交**********************/
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
				successView.forward(req, res);
				
			}catch(Exception e) {
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
				failureView.forward(req, res);
			}
	
		}
		
		//確認對方發起的交友邀請
		if("becomeFri".equals(action)) {
			List<String> errorMsgs_Ailee = new LinkedList<String>();
			req.setAttribute("errorMsgs_Ailee", errorMsgs_Ailee);
			String meId =null;
			String friId=null;
			try {
				/****************1.接受請求參數,錯誤驗證**********************/
				meId = req.getParameter("meId");
				friId  = req.getParameter("friId");
				
				if(meId == null || meId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者會員ID null!");
				}
				
				if(friId == null || friId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者好友的會員ID null!");
				}
				
				if(!errorMsgs_Ailee.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
					failureView.forward(req, res);
					return;
				}
			
				/**********2.確認資料無誤，開始新增好友關係及更改對方的好友狀態***********/
				FriendService friSvc = new FriendService();
				
				//先確認資料庫是否有資料???免得因為重新整理重新新增
				if(friSvc.findRelationship(meId, friId) != null) {
					errorMsgs_Ailee.add("錯誤：已經成為好友了");
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
					failureView.forward(req, res);
					return;
				}
				
				//因為好友編號是自增主鍵，所以有可能因為重新整理導致重新新增狀況。
				friSvc.becomeFri(meId, friId);
				
				/**********2.成功加入並成功修改狀態，開始轉交***********/
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
				successView.forward(req, res);
				
			}catch(Exception e) {
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
				failureView.forward(req, res);

			}
			
		}
		//刪除對方發起的交友邀請
		if("reject".equals(action)) {
			List<String> errorMsgs_Ailee = new LinkedList<String>();
			req.setAttribute("errorMsgs_Ailee", errorMsgs_Ailee);
			String meId =null;
			String friId=null;
			try {
				/****************1.接受請求參數,錯誤驗證**********************/
				meId = req.getParameter("meId");
				friId  = req.getParameter("friId");
				
				if(meId == null || meId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者會員ID null!");
				}
				
				if(friId == null || friId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者好友的會員ID null!");
				}
				
				if(!errorMsgs_Ailee.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
					failureView.forward(req, res);
					return;
				}

				/**********2.確認資料無誤，刪除他人發送的好友邀請***********/
				FriendService friSvc = new FriendService();
				friSvc.rejectFri(meId, friId);
				
				/**********2.成功加入並成功修改狀態，開始轉交***********/
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
				successView.forward(req, res);
				
			}catch(Exception e) {
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_public.jsp?uId="+friId);
				failureView.forward(req, res);

			}
		}
		
		
		//解除好友關係時
		if("deleteFri".equals(action)) {
			List<String> errorMsgs_Ailee = new LinkedList<String>();
			req.setAttribute("errorMsgs_Ailee", errorMsgs_Ailee);
			
			try {
				/****************1.接受請求參數,錯誤驗證**********************/
				String meId = req.getParameter("meId");
				String friId  = req.getParameter("friId");

				
				if(meId == null || meId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者會員ID null!");
				}
				
				if(friId == null || friId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者好友的會員ID null!");
				}
				
				if(!errorMsgs_Ailee.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_friend.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/****************2.確認資料無誤，即開始解除好友關係**********************/
				FriendService friSvc = new FriendService();
				 //因為成為好友後，會存兩筆，故要執行deleteFri()請DAO一次刪除兩筆(已處理交易問題)
				friSvc.deleteFri(meId, friId);
				
				/****************3.刪除完畢，開始轉交**********************/
				String url = "/front_end/personal_area/personal_area_friend.jsp";
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/personal_area/personal_area_friend.jsp");
				successView.forward(req, res);
				
				
			}catch(Exception e) {
				errorMsgs_Ailee.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_friend.jsp");
				failureView.forward(req, res);
			}
		}
		
		//封鎖好友時，收到personal_area_friend.jsp請求
		if("blockFri".equals(action)) {
			List<String> errorMsgs_Ailee = new LinkedList<String>();
			req.setAttribute("errorMsgs_Ailee",errorMsgs_Ailee);
			
			try {
				/********************1.接受參數，錯誤驗證****************************/
				String meId = req.getParameter("meId");
				String friId = req.getParameter("friId");
				
				if(meId == null || meId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者會員ID null!");
				}
				
				if(friId == null || friId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：登入者好友的會員ID null!");
				}
				
				if(!errorMsgs_Ailee.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_friend.jsp");
					failureView.forward(req, res);
					return;
				}
				
				
				/********************2.完成錯誤驗證，準備封鎖朋友****************************/
				FriendService friSvc = new FriendService();
				friSvc.updateFriStat_Block(meId, friId, 3);
				
				/********************3.準備提交****************************************/
				String url = "/front_end/personal_area/personal_area_friend.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
				
				
			}catch(Exception e) {
				errorMsgs_Ailee.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_friend.jsp");
				failureView.forward(req, res);
			}
			
			
			
		}
		
		//對某位好友解除封鎖時
		if("unBlockFri".equals(action)) {
			
			List<String> errorMsgs_Ailee = new LinkedList<String>();
			req.setAttribute("errorMsgs_Ailee",errorMsgs_Ailee);
			
			try {
				String meId = req.getParameter("meId");
				String friId = req.getParameter("friId");
				String local = req.getParameter("local");
				
				/*****************1.取得參數，錯誤處理*************************/
				if(meId == null || meId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤:未取得登入者ID");
				}
				
				if(friId == null || friId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤:未取得登入者的好友ID");
				}
				
				if(!errorMsgs_Ailee.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_friend.jsp");
					failureView.forward(req, res);
				}
				
				
				/*****************2.錯誤驗證無誤，進行解除封鎖********************/
				FriendService friSvc = new FriendService();
				friSvc.updateFriStat_Block(meId,friId,2);
				
				/*****************3.解除封鎖成功，準備提交********************/
				String url = null ;
				//若有取到來自personal_area_public的請求時，會有存到送出需求頁面網址
				if("public_area".equals(local)) {
					url="/front_end/personal_area/personal_area_public.jsp?uId="+friId;
				}else {
					url = "/front_end/personal_area/personal_area_friend.jsp";
				}
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req,res);
				
				
			}catch(Exception e) {
				errorMsgs_Ailee.add("發生exception"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/personal_area/personal_area_friend.jsp");
				failureView.forward(req, res);
			}
			
			
		}
		
		//確認之間的好友關係是 封鎖?好友?非好友? 來自於personal_area_public.jsp的請求
		if("checkFri".equals(action)) {
			List<String> errorMsgs_Ailee = new LinkedList<String>();
			req.setAttribute("errorMsgs_Ailee", errorMsgs_Ailee);
			PrintWriter out = res.getWriter() ;  //回傳資料給Ajax
			
			try {
				/************1.取得參數，錯誤驗證*******************/
				String meId = req.getParameter("meId");
				String uId = req.getParameter("uId");
				if(meId == null || meId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：未取到登入者ID");
				}
				
				if(uId == null || uId.trim().length() == 0) {
					errorMsgs_Ailee.add("錯誤：未取到查看他人個人頁面的ID");
				}
	
				if(!errorMsgs_Ailee.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/index.jsp"); // 到時要改!!!
					failureView.forward(req, res);
				}
				
				/************2.錯誤驗證結束，開始確認好友之間關係*******************/
				FriendService friSvc = new FriendService();
				Friend myfriend = friSvc.findRelationship(meId,uId); //判斷我跟他的關係
				Friend uIdfriend = friSvc.findRelationship(uId,meId);//在判斷他跟我的關西，可能有寄發交友邀請
				
				/*如果給的參數，沒有這位人的話 .... 回傳404*/
				MemberService memSvc = new MemberService();
				MemberVO uId_memvo=memSvc.getOneMember(uId);
				
				if(uId_memvo == null) {
					out.println(404);
				}else {
					if(myfriend == null) {
						if(uIdfriend != null) {
							out.print("他加我好友");
							return;
						}
						out.println(0);  //我跟他並不是好友
					}else if(myfriend.getFri_Stat() == 1 ) {
						out.println(1);  //我已向他發起交友邀請
					}else if(myfriend.getFri_Stat() == 2) {
						out.println(2);  //我跟他是好友
					}else if(myfriend.getFri_Stat() == 3) {
						out.println(3);  //我已封鎖他
					}
				}

				/************3.準備轉交*******************/
				
			}catch(Exception e) {
				errorMsgs_Ailee.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/index.jsp"); // 到時要改!!!
				failureView.forward(req, res);
			}
			
		}
		
		
		
		
	}

}
