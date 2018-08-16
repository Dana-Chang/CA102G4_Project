package com.trip.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trip.model.*;
import com.tripCollect.model.*;
import com.tripDays.model.*;
import com.trafficTrip.model.*;
import com.attTrip.model.*;
import com.attractions.model.AttractionsService;
import com.attractions.model.AttractionsVO;
import com.mem.model.*;

public class TripServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		LinkedHashMap<Integer, String> traTrip_type = new LinkedHashMap<Integer, String>();
		traTrip_type.put(0, "地鐵、電車");
		traTrip_type.put(1, "飛機");
		traTrip_type.put(2, "火車");
		traTrip_type.put(3, "公車");
		traTrip_type.put(4, "步行");
		traTrip_type.put(5, "計程車");
		traTrip_type.put(6, "其他交通");
		ServletContext sc = getServletContext();
		sc.setAttribute("traTrip_type", traTrip_type);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=Big5");
		req.setCharacterEncoding("UTF-8");

		String action = req.getParameter("action");
		System.out.println(action);
		
		if("newTrip".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				String mem_id = null;
				mem_id = req.getParameter("mem_id");
//				System.out.println(mem_id);
				String trip_name = req.getParameter("trip_name");
				if(trip_name==null||trip_name.trim().length()==0) {
					errorMsgs.put("trip_name","行程名稱: 請勿空白");
				}
				
				java.sql.Date trip_startDay = null;
				try {
					if(!(req.getParameter("trip_startDay").trim().length()==0)) {
						trip_startDay = java.sql.Date.valueOf(req.getParameter("trip_startDay").trim());
					}
				}catch(IllegalArgumentException e) {
					trip_startDay = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.put("trip_startDay","請填正確格式");
				}
				
				Integer trip_days = null;
				try {
					trip_days = Integer.parseInt(req.getParameter("trip_days").trim());
				}catch(NumberFormatException e) {
					trip_days = 1;
					errorMsgs.put("trip_days","請輸入行程天數");
				}
				
				TripVO tripVO = new TripVO();
				tripVO.setMem_id(mem_id);
				tripVO.setTrip_name(trip_name);
				tripVO.setTrip_startDay(new java.sql.Date(trip_startDay.getTime()));
				tripVO.setTrip_days(trip_days);
				tripVO.setTrip_views(0);
				tripVO.setTrip_status(2);

				if(!errorMsgs.isEmpty()) {
					req.setAttribute("tripVO", tripVO);
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/newTrip.jsp");
					failureView.forward(req, res);
					return;
				}
				/***************************2.開始新增資料***************************************/
				//存行程天與景點行程、交通行程的對應
				Map<Integer,List<Object>> tripDayMap = new LinkedHashMap<>();

				List<TripDaysVO> tdList = new ArrayList<>();
				for(int i = 1;i<= trip_days ;i++) {
					java.sql.Date trip_startDay_temp = null;
					if(trip_startDay!=null) {
						trip_startDay_temp = new java.sql.Date(trip_startDay.getTime());
						trip_startDay.setDate(trip_startDay.getDate()+1);
					}
					TripDaysVO tripDaysVO = new TripDaysVO();
					tripDaysVO.setTripDay_days(i);
					tripDaysVO.setTripDay_date(trip_startDay_temp);
//					System.out.println(tripDaysVO.getTripDay_date());
					tdList.add(tripDaysVO);
					tripDayMap.put(i, new ArrayList<Object>());
				}
//				for(TripDaysVO vo : tdList) {
//					System.out.println(vo.getTripDay_date());
//				}
				
				HttpSession session = req.getSession();
				//為了jsp能與servlet互傳物件，忍痛存session
				session.setAttribute("tripVO_edit", tripVO);
				session.setAttribute("tdList", tdList);
				session.setAttribute("tripDayMap", tripDayMap);
//				req.setAttribute("tripVO", tripVO);
//				req.setAttribute("tdList", tdList);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/newTrip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("changeName".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				String trip_name = req.getParameter("trip_name");
//				if(trip_name==null||trip_name.trim().length()==0) {
//					errorMsgs.put("trip_name","行程名稱: 請勿空白");
//				}
				TripVO tripVO = (TripVO)session.getAttribute("tripVO_edit");
				tripVO.setTrip_name(trip_name);
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
					failureView.forward(req, res);
					return;
				}
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				session.setAttribute("tripVO_edit", tripVO);
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("changeDate".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				
				java.sql.Date trip_startDay = null;
				try {
					if(!(req.getParameter("trip_startDay").trim().length()==0)) {
						trip_startDay = java.sql.Date.valueOf(req.getParameter("trip_startDay").trim());
					}
				}catch(IllegalArgumentException e) {
					trip_startDay = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.put("trip_startDay","請填正確格式");
				}
				
				TripVO tripVO = (TripVO)session.getAttribute("tripVO_edit");
				tripVO.setTrip_startDay(new java.sql.Date(trip_startDay.getTime()));
				
				List<TripDaysVO> tdList = (List<TripDaysVO>)session.getAttribute("tdList");
				
				int days = tdList.size();
				for(int i = 0;i< days ;i++) {
					java.sql.Date trip_startDay_temp = null;
					if(trip_startDay!=null) {
						trip_startDay_temp = new java.sql.Date(trip_startDay.getTime());
						trip_startDay.setDate(trip_startDay.getDate()+1);
					}
					tdList.get(i).setTripDay_date(trip_startDay_temp);
				}
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
					failureView.forward(req, res);
					return;
				}
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				session.setAttribute("tripVO_edit", tripVO);
				session.setAttribute("tdList", tdList);
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		//新增交通行程
		if("addTrafficTrip".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				Map<Integer,List<Object>> tripDayMap = (Map<Integer,List<Object>>)session.getAttribute("tripDayMap");
				
				Integer traTrip_type = Integer.parseInt(req.getParameter("traTrip_type"));
				
				String traTrip_name = req.getParameter("traTrip_name");
				
				
				int startHour = (req.getParameter("startHour")==null? 0:Integer.parseInt(req.getParameter("startHour")));
				int startMin = (req.getParameter("startMin")==null? 0:Integer.parseInt(req.getParameter("startMin")));
				int endHour = (req.getParameter("endHour")==null? 0:Integer.parseInt(req.getParameter("endHour")));
				int endMin = (req.getParameter("endMin")==null? 0:Integer.parseInt(req.getParameter("endMin")));
				
				Integer traTrip_start = startHour*60+startMin;
				Integer traTrip_end = endHour*60+endMin;
				
				Integer traTrip_cost = (req.getParameter("traTrip_cost").isEmpty()?0:Integer.parseInt(req.getParameter("traTrip_cost")));
				
				String traTrip_note = req.getParameter("traTrip_note");
				
//				System.out.println("traTrip_start:"+ traTrip_start );
//				System.out.println("traTrip_end:"+ traTrip_end );
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
					failureView.forward(req, res);
					return;
				}
				/***************************2.開始新增資料***************************************/
				//屬於第幾天的行程
				Integer belongDays = Integer.parseInt(req.getParameter("belongDays"));
				//拿到那一天的行程list
				List<Object> detailList = tripDayMap.get(belongDays);
				//包裝資料
				TrafficTripVO traTripVO = new TrafficTripVO();
				traTripVO.setTraTrip_type(traTrip_type);
				traTripVO.setTraTrip_name(traTrip_name);
				traTripVO.setTraTrip_start(traTrip_start);
				traTripVO.setTraTrip_end(traTrip_end);
				traTripVO.setTraTrip_cost(traTrip_cost);
				traTripVO.setTraTrip_note(traTrip_note);
				traTripVO.setTrip_order(detailList.size()+1);
				//放入list
				detailList.add(traTripVO);
				//list放到map
				tripDayMap.put(belongDays, detailList);
//				int count=0;
//				for(Map.Entry<Integer, List<Object>> entry : tripDayMap.entrySet()) {
//					System.out.println("第"+(++count)+"天有"+entry.getValue().size()+"個行程");
//				}
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				session.setAttribute("tripDayMap", tripDayMap);
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/newTrip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		//新增景點行程
		if("addAttTrip".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				Map<Integer,List<Object>> tripDayMap = (Map<Integer,List<Object>>)session.getAttribute("tripDayMap");
				
				int startHour = (req.getParameter("startHour")==null? 0:Integer.parseInt(req.getParameter("startHour")));
				int startMin = (req.getParameter("startMin")==null? 0:Integer.parseInt(req.getParameter("startMin")));
				int endHour = (req.getParameter("endHour")==null? 0:Integer.parseInt(req.getParameter("endHour")));
				int endMin = (req.getParameter("endMin")==null? 0:Integer.parseInt(req.getParameter("endMin")));
				
				Integer attTrip_start = startHour*60+startMin;
				Integer attTrip_end = endHour*60+endMin;
				
//				System.out.println("attTrip_start:"+ attTrip_start );
//				System.out.println("attTrip_end:"+ attTrip_end );
				
				Integer attTrip_cost = (req.getParameter("attTrip_cost").isEmpty()?0:Integer.parseInt(req.getParameter("attTrip_cost")));
				
				String attTrip_note = req.getParameter("attTrip_note");
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
					failureView.forward(req, res);
					return;
				}
				/***************************2.開始新增資料***************************************/
				//屬於第幾天的行程
				Integer belongDays = Integer.parseInt(req.getParameter("belongDays"));
				//拿到那一天的行程list
				List<Object> detailList = tripDayMap.get(belongDays);
				//包裝資料
				AttractionsTripVO attTripVO = new AttractionsTripVO();
				attTripVO.setAtt_no(req.getParameter("att_no"));
				attTripVO.setAttTrip_cost(attTrip_cost);
				attTripVO.setAttTrip_start(attTrip_start);
				attTripVO.setAttTrip_end(attTrip_end);
				attTripVO.setAttTrip_note(attTrip_note);
				attTripVO.setTrip_order(detailList.size()+1);
				//放入list
				detailList.add(attTripVO);
				//list放到map
				tripDayMap.put(belongDays, detailList);
				
//				int count=0;
//				for(Map.Entry<Integer, List<Object>> entry : tripDayMap.entrySet()) {
//					System.out.println("第"+(++count)+"天有"+entry.getValue().size()+"個行程");
//				}
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				session.setAttribute("tripDayMap", tripDayMap);
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/newTrip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		//刪除景點行程或交通行程
		if("delectDetailTrip".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數******************************************/
				HttpSession session = req.getSession();
				Map<Integer,List<Object>> tripDayMap = (Map<Integer,List<Object>>)session.getAttribute("tripDayMap");
				
				//屬於第幾天的行程
				Integer belongDays = Integer.parseInt(req.getParameter("belongDays"));
				Integer order = Integer.parseInt(req.getParameter("order"));
				
				//拿到那一天的行程list
				List<Object> detailList = tripDayMap.get(belongDays);
				
				/***************************2.開始刪除資料***************************************/
				
				detailList.remove(order-1);
				for(int i = (order-1);i<detailList.size();i++) {
					if(detailList.get(i) instanceof AttractionsTripVO) {
						AttractionsTripVO attTripVO = (AttractionsTripVO)detailList.get(i);
						attTripVO.setTrip_order(i+1);
						detailList.set(i, attTripVO);
					}else if(detailList.get(i) instanceof TrafficTripVO){
						TrafficTripVO traTripVO = (TrafficTripVO)detailList.get(i);
						traTripVO.setTrip_order(i+1);
						detailList.set(i, traTripVO);
					}
				}
				//list放到map
				tripDayMap.put(belongDays, detailList);
				
//				int count=0;
//				for(Map.Entry<Integer, List<Object>> entry : tripDayMap.entrySet()) {
//					System.out.println("第"+(++count)+"天有"+entry.getValue().size()+"個行程");
//				}
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/
				session.setAttribute("tripDayMap", tripDayMap);
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/newTrip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("editTraTrip".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				Map<Integer,List<Object>> tripDayMap = (Map<Integer,List<Object>>)session.getAttribute("tripDayMap");
				
				Integer traTrip_type = Integer.parseInt(req.getParameter("traTrip_type"));
				
				String traTrip_name = req.getParameter("traTrip_name");
				
				
				int startHour = (req.getParameter("startHour")==null? 0:Integer.parseInt(req.getParameter("startHour")));
				int startMin = (req.getParameter("startMin")==null? 0:Integer.parseInt(req.getParameter("startMin")));
				int endHour = (req.getParameter("endHour")==null? 0:Integer.parseInt(req.getParameter("endHour")));
				int endMin = (req.getParameter("endMin")==null? 0:Integer.parseInt(req.getParameter("endMin")));
				
				Integer traTrip_start = startHour*60+startMin;
				Integer traTrip_end = endHour*60+endMin;
				
				Integer traTrip_cost = (req.getParameter("traTrip_cost").isEmpty()?0:Integer.parseInt(req.getParameter("traTrip_cost")));
				
				String traTrip_note = req.getParameter("traTrip_note");
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
					failureView.forward(req, res);
					return;
				}
				/***************************2.開始修改資料***************************************/
				//屬於第幾天的行程
				Integer belongDays = Integer.parseInt(req.getParameter("belongDays"));
				//取得順序
				Integer order = Integer.parseInt(req.getParameter("order"));
				//拿到那一天的行程list
				List<Object> detailList = tripDayMap.get(belongDays);
				//修改list內的資料
				TrafficTripVO traTripVO = (TrafficTripVO)detailList.get(order-1);
				traTripVO.setTraTrip_type(traTrip_type);
				traTripVO.setTraTrip_name(traTrip_name);
				traTripVO.setTraTrip_start(traTrip_start);
				traTripVO.setTraTrip_end(traTrip_end);
				traTripVO.setTraTrip_cost(traTrip_cost);
				traTripVO.setTraTrip_note(traTrip_note);

				//list放到map
				tripDayMap.put(belongDays, detailList);
				
//				int count=0;
//				for(Map.Entry<Integer, List<Object>> entry : tripDayMap.entrySet()) {
//					System.out.println("第"+(++count)+"天有"+entry.getValue().size()+"個行程");
//				}
				/***************************3.修改完成,準備轉交(Send the Success view)***********/
				session.setAttribute("tripDayMap", tripDayMap);
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/newTrip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("editAttTrip".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				Map<Integer,List<Object>> tripDayMap = (Map<Integer,List<Object>>)session.getAttribute("tripDayMap");
				
				int startHour = (req.getParameter("startHour")==null? 0:Integer.parseInt(req.getParameter("startHour")));
				int startMin = (req.getParameter("startMin")==null? 0:Integer.parseInt(req.getParameter("startMin")));
				int endHour = (req.getParameter("endHour")==null? 0:Integer.parseInt(req.getParameter("endHour")));
				int endMin = (req.getParameter("endMin")==null? 0:Integer.parseInt(req.getParameter("endMin")));
				
				Integer attTrip_start = startHour*60+startMin;
				Integer attTrip_end = endHour*60+endMin;
				
				
				Integer attTrip_cost = (req.getParameter("attTrip_cost").isEmpty()?0:Integer.parseInt(req.getParameter("attTrip_cost")));
				
				String attTrip_note = req.getParameter("attTrip_note");
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
					failureView.forward(req, res);
					return;
				}
				/***************************2.開始修改資料***************************************/
				//屬於第幾天的行程
				Integer belongDays = Integer.parseInt(req.getParameter("belongDays"));
				//取得順序
				Integer order = Integer.parseInt(req.getParameter("order"));
				//拿到那一天的行程list
				List<Object> detailList = tripDayMap.get(belongDays);
				//修改list內的資料
				AttractionsTripVO attTripVO = (AttractionsTripVO)detailList.get(order-1);
				attTripVO.setAttTrip_cost(attTrip_cost);
				attTripVO.setAttTrip_start(attTrip_start);
				attTripVO.setAttTrip_end(attTrip_end);
				attTripVO.setAttTrip_note(attTrip_note);
				
				//list放到map
				tripDayMap.put(belongDays, detailList);
//				int count=0;
//				for(Map.Entry<Integer, List<Object>> entry : tripDayMap.entrySet()) {
//					System.out.println("第"+(++count)+"天有"+entry.getValue().size()+"個行程");
//				}
				/***************************3.修改完成,準備轉交(Send the Success view)***********/
				session.setAttribute("tripDayMap", tripDayMap);
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/newTrip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("editHotel".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				List<TripDaysVO> tdList = (List<TripDaysVO>) session.getAttribute("tdList");
				
				String tripDay_hotelName = "";
				if(req.getParameter("tripDay_hotelName")!=null) {
					tripDay_hotelName = req.getParameter("tripDay_hotelName").trim();
				}
				
				int startHour = (req.getParameter("startHour")==null? 0:Integer.parseInt(req.getParameter("startHour")));
				int startMin = (req.getParameter("startMin")==null? 0:Integer.parseInt(req.getParameter("startMin")));
				
				Integer tripDay_hotelStart = startHour*60+startMin;
				
				Integer tripDay_hotelCost = (req.getParameter("tripDay_hotelCost").isEmpty()?0:Integer.parseInt(req.getParameter("tripDay_hotelCost")));
				
				String tripDay_hotelNote = req.getParameter("tripDay_hotelNote");
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
					failureView.forward(req, res);
					return;
				}
				/***************************2.開始修改資料***************************************/
				//屬於第幾天的行程
				int index = Integer.parseInt(req.getParameter("index"));
				//取得行程天VO
				TripDaysVO tripDaysVO =  tdList.get(index);
				tripDaysVO.setTripDay_hotelCost(tripDay_hotelCost);
				tripDaysVO.setTripDay_hotelStart(tripDay_hotelStart);
				tripDaysVO.setTripDay_hotelName(tripDay_hotelName);
				tripDaysVO.setTripDay_hotelNote(tripDay_hotelNote);

				/***************************3.修改完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/newTrip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("commitEdit".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				TripVO tripVO = (TripVO) session.getAttribute("tripVO_edit");
				List<TripDaysVO> tdList = (List<TripDaysVO>) session.getAttribute("tdList");
				Map<Integer,List<Object>> tripDayMap = (Map<Integer,List<Object>>)session.getAttribute("tripDayMap");

				/***************************2.開始新增資料***************************************/
				TripService tripSvc = new TripService();
				//如果沒有trip_no代表為新建立的行程
				if(tripVO.getTrip_no()==null) {
					tripSvc.insertOneTrip(tripVO, tdList, tripDayMap);
					
					//如果有trip_no代表為編輯修改的行程
				}else {
					tripSvc.updateByEdit(tripVO, tdList, tripDayMap);
				}
				
				
				/***************************3.新增或修改完成,準備重導***********/
				session.removeAttribute("tripVO_edit");
				session.removeAttribute("tdList");
				session.removeAttribute("tripDayMap");
				res.sendRedirect(req.getContextPath()+"/front_end/trip/personal_area_trip.jsp");
				return;
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				failureView.forward(req, res);
//				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("deleteTrip".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				String trip_no = req.getParameter("trip_no");
				
				/***************************2.開始修改資料***************************************/
				TripService tripSvc = new TripService();
				tripSvc.deleteOnline(trip_no);
				/***************************3.修改完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/personal_area_trip.jsp");
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/personal_area_trip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("collectTrip".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			req.setAttribute("errorMsgs", errorMsgs);
			PrintWriter out = res.getWriter();
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				String trip_no = req.getParameter("trip_no");
				if(session.getAttribute("memberVO")==null) {
					out.print("fail");
					return;
				}
				MemberVO memVO = (MemberVO)session.getAttribute("memberVO");
				String mem_id = memVO.getMem_Id();
//				System.out.println(mem_id+":"+trip_no);
				TripCollectVO tripCollectVO = new TripCollectVO();
				tripCollectVO.setMem_id(mem_id);
				tripCollectVO.setTrip_no(trip_no);
				TripCollectService tcSvc = new TripCollectService();
				if(tcSvc.findByPrimaryKey(trip_no, mem_id)!=null) {
					out.print("fail2");
					return;
				}
				
				/***************************2.開始修改資料***************************************/
				tcSvc.insert(tripCollectVO);
				
				/***************************3.修改完成,準備轉交(Send the Success view)***********/
				out.print("ok");
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/personal_area_trip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("cancelCollect".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			req.setAttribute("errorMsgs", errorMsgs);
			PrintWriter out = res.getWriter();
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				HttpSession session = req.getSession();
				String trip_no = req.getParameter("trip_no");
				if(session.getAttribute("memberVO")==null) {
					session.setAttribute("location", req.getContextPath()+"/front_end/trip/personal_area_trip.jsp");
					out.print("fail");
					return;
				}
				MemberVO memVO = (MemberVO)session.getAttribute("memberVO");
				String mem_id = memVO.getMem_Id();
				System.out.println(mem_id+":"+trip_no);
				TripCollectVO tripCollectVO = new TripCollectVO();
				tripCollectVO.setMem_id(mem_id);
				tripCollectVO.setTrip_no(trip_no);
				TripCollectService tcSvc = new TripCollectService();
				int updateCount = tcSvc.delete(trip_no, mem_id);
				System.out.println(updateCount);
//				if(updateCount==0) {
//					out.write("fail2");
//					return;
//				}
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/
				out.print(updateCount);
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/personal_area_trip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("editTrip".equals(action)){
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				String trip_no = req.getParameter("trip_no");

				/***************************2.開始包裝資料***************************************/
				TripService tripSvc = new TripService();
				TripDaysService tdSvc = new TripDaysService();
				TrafficTripService ttSvc = new TrafficTripService();
				AttractionsTripService atSvc = new AttractionsTripService();
				
				TripVO tripVO = tripSvc.getOneTripByPK(trip_no);
				
				List<TripDaysVO> tdList = tdSvc.getByTrip_no(trip_no);
				
				Map<Integer,List<Object>> tripDayMap = new LinkedHashMap<>();
//				Map<Integer,List<Object>> tripDayMapOrigin = new LinkedHashMap<>();
				
				for(TripDaysVO tdVO : tdList){
					List<AttractionsTripVO> atList = atSvc.getByTripDays_no(tdVO.getTripDay_no());
					List<TrafficTripVO> ttList = ttSvc.getByTripDays_no(tdVO.getTripDay_no());
					int detailSize = atList.size() + ttList.size();
					List<Object> detailList = new ArrayList<>();
					for(int i = 1;i<= detailSize;i++){
						for(AttractionsTripVO atVO : atList){
							if(atVO.getTrip_order() == i){
								detailList.add(atVO);
								continue;
							}
						}
						for(TrafficTripVO ttVO : ttList){
							if(ttVO.getTrip_order() == i){
								detailList.add(ttVO);
								continue;
							}
						}
					}
					tripDayMap.put(tdVO.getTripDay_days(), detailList);
//					tripDayMapOrigin.put(tdVO.getTripDay_days(), detailList);
				}
				
				HttpSession session = req.getSession();
				//編輯頁面用
				session.setAttribute("tripVO_edit", tripVO);
				session.setAttribute("tdList", tdList);
				session.setAttribute("tripDayMap", tripDayMap);
				
				//保存原本細節
//				session.setAttribute("tripDayMapOrigin", tripDayMapOrigin);
				/***************************3.包裝完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/tripEdit.jsp");
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/trip/personal_area_trip.jsp");
				failureView.forward(req, res);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		if("search".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
			/***************************1.將輸入資料轉為Map**********************************/ 
			HttpSession session = req.getSession();
			Map<String, String[]> map =(Map<String, String[]>) session.getAttribute("tripMap");
			String keyword = null;
			if(req.getParameter("keyword")!=null&&!req.getParameter("keyword").trim().isEmpty()) {
				keyword = req.getParameter("keyword").trim();
//				System.out.println("keyword:"+keyword);
			}
			if(keyword!=null&&!keyword.isEmpty()) {
				HashMap<String, String[]> map1 = new HashMap<String, String[]>(req.getParameterMap());
				map1.put("trip_name", new String[] {keyword});
				session.setAttribute("tripMap",map1);
				map = map1;
			}
				
			/***************************2.開始複合查詢***************************************/
			TripService tripSvc = new TripService();
			List<TripVO> list;
			if(map==null) {
				list = tripSvc.getPublish();
			}else {
				list = tripSvc.getAll(map);
			}
			/***************************3.查詢完成,準備轉交(Send the Success view)************/
			req.setAttribute("list", list);
			RequestDispatcher successView = req.getRequestDispatcher("/front_end/trip/trip.jsp");
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