package com.attractions.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.attEdit.model.AttractionsEditService;
import com.attractions.model.AttractionsService;
import com.attractions.model.AttractionsVO;

public class AttractionsServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=Big5");
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action").trim();
//		System.out.println(action);
		
		if("search".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
			/***************************1.將輸入資料轉為Map**********************************/ 
			HttpSession session = req.getSession();
			Map<String, String[]> map =(Map<String, String[]>) session.getAttribute("attMap");
			String keyword = null;
			if(req.getParameter("keyword")!=null&&!req.getParameter("keyword").trim().isEmpty()) {
				keyword = req.getParameter("keyword").trim();
//				System.out.println("keyword:"+keyword);
			}
			if(keyword!=null&&!keyword.isEmpty()) {
				HashMap<String, String[]> map1 = new HashMap<String, String[]>(req.getParameterMap());
				map1.put("att_name", new String[] {keyword});
				map1.put("att_address", new String[] {keyword});
				map1.put("administrative_area", new String[] {keyword});
				session.setAttribute("attMap",map1);
				map = map1;
			}
				
			/***************************2.開始複合查詢***************************************/
			AttractionsService attSvc = new AttractionsService();
			List<AttractionsVO> list;
			if(map==null) {
				list = attSvc.getAll();
			}else {
				list = attSvc.getAll(map);
			}
			/***************************3.查詢完成,準備轉交(Send the Success view)************/
			req.setAttribute("list", list);
			RequestDispatcher successView = req.getRequestDispatcher("/front_end/attractions/att.jsp");
			successView.forward(req, res);
			/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.add(e.getMessage());
//				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/attractions/att.jsp");
//				failureView.forward(req, res);
				e.printStackTrace();
			}
		}
		
		if("tripEditSearch".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.將輸入資料轉為Map**********************************/ 
				HttpSession session = req.getSession();
				Map<String, String[]> map =(Map<String, String[]>) session.getAttribute("map_tripEdit");
				String keyword = null;
				if(req.getParameter("keyword")!=null&&!req.getParameter("keyword").trim().isEmpty()) {
					keyword = req.getParameter("keyword").trim();
//					System.out.println("keyword:"+keyword);
				}
				if(keyword!=null&&!keyword.isEmpty()) {
					HashMap<String, String[]> map1 = new HashMap<String, String[]>(req.getParameterMap());
					map1.put("att_name", new String[] {keyword});
					map1.put("att_address", new String[] {keyword});
					map1.put("administrative_area", new String[] {keyword});
					session.setAttribute("map_tripEdit",map1);
					map = map1;
				}
				
				/***************************2.開始複合查詢***************************************/
				AttractionsService attSvc = new AttractionsService();
				List<AttractionsVO> list;
				if(map==null) {
					list = attSvc.getAll();
				}else {
					list = attSvc.getAll(map);
				}
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("list", list);
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.add(e.getMessage());
//				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/attractions/att.jsp");
//				failureView.forward(req, res);
				e.printStackTrace();
			}
		}
	}
}
