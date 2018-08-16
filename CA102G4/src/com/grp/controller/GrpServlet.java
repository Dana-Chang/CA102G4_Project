package com.grp.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import com.grp.model.*;
import com.grp_mem.model.Grp_memService;
import com.grp_mem.model.Grp_memVO;
import com.mem.model.MemberService;
import com.mem.model.MemberVO;
import com.tools.JavaMailSender;


@MultipartConfig(maxFileSize=-1,maxRequestSize=-1)
public class GrpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
	    res.setContentType("text/html; charset=UTF-8");
		String grp_PhotoReg = "^(jpeg|jpg|bmp|png|gif)$";
	    String action = req.getParameter("action");
	    PrintWriter out = res.getWriter();
	   	HttpSession session = req.getSession();
		SimpleDateFormat time_format = new SimpleDateFormat("yyyy-mm-dd");

	   	
  		if("insert".equals(action)) {
  			
  		   	System.out.println("有進來新增喔");

  			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
  			req.setAttribute("errorMsgs",errorMsgs);
  			
  			Timestamp pre_grp_End = null;
			Timestamp pre_trip_Strat = null;
			Timestamp pre_trip_End = null;

  			try {
  				/*******************第一步：錯誤處理*******************/
				String mem_Id = req.getParameter("mem_Id");
				
				System.out.println(mem_Id);

  				String grp_Title = req.getParameter("grp_Title");
  				
  				if(grp_Title == null || grp_Title.trim().length() == 0) {
  					errorMsgs.put("grp_Title","團名：請勿空白。");
  				}else if(grp_Title.trim().length()<2||grp_Title.trim().length()>30){
  					errorMsgs.put("grp_Title","團名：請輸入2~6個字。");
  				}
				System.out.println(grp_Title);

  				String trip_Locale = req.getParameter("trip_Locale");
  				if(trip_Locale == null || trip_Locale.trim().length() == 0) {
  					errorMsgs.put("trip_Locale","地點：請勿空白。");
  				}
				System.out.println(trip_Locale);

				
  				String grp_End = req.getParameter("grp_End");
  				
				System.out.println("我在這1"+grp_End);

				if(grp_End == null || grp_End.trim().length() == 0){
					errorMsgs.put("grp_End","揪團結束時間，請勿空白");
					
					System.out.println("我在這2");

				}else {
					System.out.println("我在這3");

					Date temp_grp_End = time_format.parse(grp_End);
					System.out.println("我在這4");

					System.out.println("temp_grp_End"+temp_grp_End);
					System.out.println("time_format"+time_format);
					System.out.println("grp_End"+grp_End);

					pre_grp_End = new Timestamp(temp_grp_End.getTime());
				}
				
				System.out.println("pre_grp_End"+pre_grp_End);
				
				String trip_Strat = req.getParameter("trip_Strat");
				if(trip_Strat == null || trip_Strat.trim().length() == 0){
					errorMsgs.put("trip_Strat","行程開始時間，請勿空白");
				}else {
					Date temp_trip_Start = time_format.parse(trip_Strat);
					pre_trip_Strat = new Timestamp(temp_trip_Start.getTime());
				}
				System.out.println("pre_trip_Strat"+pre_trip_Strat);

				
				String trip_End = req.getParameter("trip_End");
				if(trip_End == null || trip_End.trim().length() == 0){
					errorMsgs.put("trip_End","行程結束時間，請勿空白");
				}else {
					Date temp_trip_End = time_format.parse(trip_End);
					pre_trip_End = new Timestamp(temp_trip_End.getTime());
				}
				System.out.println("pre_trip_End"+pre_trip_End);

				Integer grp_Status = 1 ;
				
				String grp_Price = req.getParameter("grp_Price");
				if(grp_Price == null || grp_Price.trim().length() == 0){
					errorMsgs.put("grp_Price","預算，請勿空白");
				}
  				
  				Integer grp_Cnt = null;
  				try {
  					grp_Cnt = Integer.parseInt(req.getParameter("grp_Cnt").trim());
				}catch(NumberFormatException e) {
					grp_Cnt = 1;
					errorMsgs.put("grp_Cnt","請輸入報名人數");
				}
  				
  				Integer grp_Acpt = null;
  				try {
  					grp_Acpt = Integer.parseInt(req.getParameter("grp_Acpt").trim());
				}catch(NumberFormatException e) {
					grp_Acpt = 1;
					errorMsgs.put("grp_Acpt","請輸入出團人數");
				}
  				
  				File grpPhoto = new File(req.getRealPath("/front_end/images/all/aegean_Sea_2.png"));
			    BufferedImage profilepicImage = ImageIO.read(grpPhoto);
			    ByteArrayOutputStream profilepicBaos = new ByteArrayOutputStream();
			    ImageIO.write(profilepicImage, "png", profilepicBaos);
			    profilepicBaos.flush();
			    byte[] grp_Photo = profilepicBaos.toByteArray();
			    System.out.println(grp_Photo);
			    profilepicBaos.close();
  				
  				GrpVO grpVO = new GrpVO();
  				
  				grpVO.setMem_Id(mem_Id);
	  			grpVO.setGrp_Title(grp_Title);
	  			grpVO.setTrip_Locale(trip_Locale);
	  			grpVO.setGrp_Status(grp_Status);
	  			grpVO.setGrp_Price(grp_Price);
	  			grpVO.setGrp_Cnt(grp_Cnt);
	  			grpVO.setGrp_Acpt(grp_Acpt);
	  			grpVO.setGrp_Photo(grp_Photo);

  				//若以上驗證有錯誤訊息的話，將會於頁面顯示出錯誤部分
  				if(!errorMsgs.isEmpty()) {
  					req.setAttribute("grpVO", grpVO);
  					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/grp/addgrp.jsp");
  					failureView.forward(req, res);
  					return;
  				}
  				
  				//************************第二步：新增資料**************************//
  				GrpService grpSvc = new GrpService();
  				grpSvc.addGrp(mem_Id,grp_Title,trip_Locale,pre_grp_End,pre_trip_Strat,pre_trip_End,grp_Status,grp_Price,grp_Cnt,grp_Acpt,grp_Photo);

  				//************************第三步：新增完成，準備提交**************************
  				System.out.println("有轉交嗎");

  				String url = "/front_end/grp/grpIndex.jsp";
  				RequestDispatcher successView = req.getRequestDispatcher(url);
  				successView.forward(req, res);
  	
  			}catch(Exception e) {
				errorMsgs.put("errorMsgs",e.getMessage());
  				RequestDispatcher filureView = req.getRequestDispatcher("/front_end/grp/addgrp.jsp");
  				filureView.forward(req, res);
  				System.out.println(e.getMessage());
				e.printStackTrace();
  			}
  			
  		}
  		
  		if ("update".equals(action)) { 
        	System.out.println("修改揪團有進來");
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			byte[] pic = null;
			ByteArrayOutputStream baos=null;
			Timestamp pre_grp_End = null;
			Timestamp pre_trip_Strat = null;
			Timestamp pre_trip_End = null;
			
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String grp_Id = req.getParameter("grp_Id");
				System.out.println("grp_Id="+grp_Id);

				String grp_Title = req.getParameter("grp_Title");
				System.out.println("grp_Title="+grp_Title);

				if (grp_Title == null || grp_Title.trim().length() == 0) {
					errorMsgs.add("團名: 請勿空白");
				}
				System.out.println("團名有");
				
				String trip_Locale = req.getParameter("trip_Locale");
				System.out.println("trip_Locale="+trip_Locale);
				
				if (trip_Locale == null || trip_Locale.trim().length() == 0) {
					errorMsgs.add("地點: 請勿空白");
				}else if(trip_Locale.trim().length()<2||trip_Locale.trim().length()>8){
					errorMsgs.add("地點：請輸入2~8個字。");
				} 
				System.out.println("地點有");

				String grp_Price = req.getParameter("grp_Price");
				if (grp_Price == null || grp_Price.trim().length() == 0) {
					errorMsgs.add("預算: 請勿空白");
				} 
				System.out.println("預算有="+grp_Price);
				
				Integer grp_Cnt = Integer.valueOf(req.getParameter("grp_Cnt"));
				System.out.println("報名人數="+grp_Cnt);
				
				Integer grp_Acpt = Integer.valueOf(req.getParameter("grp_Acpt"));
				System.out.println("出團人數="+grp_Acpt);
				
				
				String grp_End = req.getParameter("grp_End");
  				
				System.out.println("揪團結束"+grp_End);

				if(grp_End == null || grp_End.trim().length() == 0){
					errorMsgs.add("揪團結束時間，請勿空白");		
				}else {

					Date temp_grp_End = time_format.parse(grp_End);
					pre_grp_End = new Timestamp(temp_grp_End.getTime());
				}
				
				System.out.println("pre_grp_End="+pre_grp_End);
				
				
				String trip_Strat = req.getParameter("trip_Start");
				if(trip_Strat == null || trip_Strat.trim().length() == 0){
					errorMsgs.add("行程開始時間，請勿空白");
				}else {
					Date temp_trip_Start = time_format.parse(trip_Strat);
					pre_trip_Strat = new Timestamp(temp_trip_Start.getTime());
					System.out.println(pre_trip_Strat);
				}
				System.out.println("pre_trip_Strat="+pre_trip_Strat);

				
				String trip_End = req.getParameter("trip_End");
				if(trip_End == null || trip_End.trim().length() == 0){
					errorMsgs.add("行程結束時間，請勿空白");
				}else {
					Date temp_trip_End = time_format.parse(trip_End);
					pre_trip_End = new Timestamp(temp_trip_End.getTime());
				}
				System.out.println("pre_trip_End="+pre_trip_End);
				
				
				String trip_Details = req.getParameter("trip_Details");
				if (trip_Details == null || trip_Details.trim().length() == 0) {
					errorMsgs.add("行程內容: 請勿空白");
					}
				System.out.println("行程內容有");
				

				Part grp_Photo = req.getPart("grp_Photo");
				System.out.println("grp_Photo222======="+grp_Photo);

				if(getFileNameFromPart(grp_Photo) == null) {
					GrpService grpSvc = new GrpService();
					GrpVO grpVO_DB = grpSvc.findByPrimaryKey(grp_Id);
					pic = grpVO_DB.getGrp_Photo();
				}else if(!getFileNameFromPart(grp_Photo).matches(grp_PhotoReg)) {
					errorMsgs.add("圖片格式不符(.jpg/jpeg/bmp/gif/png)。");
				}
				
				System.out.println("圖片是grp_Photo="+pic);			
						
				GrpVO grpVO = new GrpVO();
				
				grpVO.setGrp_Id(grp_Id);
				grpVO.setGrp_Title(grp_Title);
				grpVO.setTrip_Locale(trip_Locale);
				grpVO.setGrp_Price(grp_Price);
				grpVO.setGrp_Cnt(grp_Cnt);
				grpVO.setGrp_Acpt(grp_Acpt);
				grpVO.setTrip_Details(trip_Details);
				grpVO.setGrp_Photo(pic);

				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("grpVO", grpVO); // 含有輸入格式錯誤的empVO物件,也存入req
					System.out.println("失敗囉");
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/grp/grp_update.jsp");
					failureView.forward(req, res);
					return; //程式中斷
					
				}
				
				/***************************2.開始修改資料*****************************************/
				if(getFileNameFromPart(grp_Photo) != null) {
					InputStream is = grp_Photo.getInputStream();
					baos = new ByteArrayOutputStream();
					byte[] buf = new byte[is.available()];
					
					int len = 0 ;
					while((len = is.read(buf))!=-1) {
						baos.write(buf,0,len);
					}
					baos.close();
					is.close();
					
					pic = baos.toByteArray();
//					System.out.println("圖片修改成功");
				}
//				System.out.println("修改快要成功");
				
				GrpService grpSvc = new GrpService();
  				grpSvc.updateGrp(grp_Id,grp_Title,trip_Locale,grp_Price,grp_Cnt,grp_Acpt,pre_grp_End,pre_trip_Strat,pre_trip_End,trip_Details,pic);

				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
//				System.out.println("****資料庫新增成功 ......後");
				req.setAttribute("grpVO", grpVO); // 資料庫update成功後,正確的的memberVO物件,存入req
//				System.out.println(grpVO);
				String url = "/front_end/grp/grp_update.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
				successView.forward(req, res);
//				System.out.println("大師兄回來了");
				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/grp/grp_update.jsp");
				failureView.forward(req, res);
			}
		}
  		
	   	
	   	//萬用複合查詢
	   	if ("listEmps_ByCompositeQuery".equals(action)) { // 來自select_page.jsp的複合查詢請求
		   	System.out.println("有進來嗎1");

	   		List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				
				/***************************1.將輸入資料轉為Map**********************************/ 
				//採用Map<String,String[]> getParameterMap()的方法 
				//注意:an immutable java.util.Map 
				//Map<String, String[]> map = req.getParameterMap();
				Map<String, String[]> map = (Map<String, String[]>)session.getAttribute("map");
				if (req.getParameter("whichPage") == null){
					HashMap<String, String[]> map1 = new HashMap<String, String[]>(req.getParameterMap());
					session.setAttribute("map",map1);
					map = map1;
					System.out.println("map1"+map);
				} 
				System.out.println("有在這嗎");
				/***************************2.開始複合查詢***************************************/
				Set list2 = map.keySet();
			    
			    Iterator it = list2.iterator();
			    while(it.hasNext()) {
			     Object tt = it.next();
			     String[] str = map.get(tt);
			     System.out.println(tt+":"+str[0]);
			    }
				
				GrpService grpSvc = new GrpService();

				List<GrpVO> list  = grpSvc.getAll(map);
				
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("listEmps_ByCompositeQuery", list); // 資料庫取出的list物件,存入request
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/grp/grp_getone.jsp"); // 成功轉交listEmps_ByCompositeQuery.jsp
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("123456");
				failureView.forward(req, res);
			}
		}
	   	

	//更改揪團狀態  成團(變成2)或失敗(被檢舉變成3)
	if ("update_grp_status".equals(action)) { 
    	System.out.println("出團囉別睡過頭");
		List<String> errorMsgs = new LinkedList<String>();
		req.setAttribute("errorMsgs", errorMsgs);

		try {
			/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
			String grp_Id = req.getParameter("grp_Id");
			System.out.println("grp_Id="+grp_Id);
			
			Integer grp_Status = Integer.valueOf(req.getParameter("grp_Status"));
	
			GrpVO grpVO = new GrpVO();
			
			grpVO.setGrp_Id(grp_Id);
			grpVO.setGrp_Status(grp_Status);
			
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("grpVO", grpVO); // 含有輸入格式錯誤的empVO物件,也存入req
				System.out.println("失敗囉");
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/grp/personal_grp_review.jsp");
				failureView.forward(req, res);
				return; //程式中斷			
			}
			
			/***************************2.開始修改資料*****************************************/
			
			GrpService grpSvc = new GrpService();
			grpSvc.update_status(grp_Id,grp_Status);
			System.out.println("確認出團");
			
			
			Grp_memService grp_memSvc = new Grp_memService();
			List<Grp_memVO> list = grp_memSvc.getAll_check_mem(grp_Id,"1");
			
			for(int i = 0 ; i < list.size(); i++) {
				
				//從GRPVO的LIST取出所有參加的人
				Grp_memVO mem = list.get(i);
				//從所有參加會員名單取出會員的資料(帳戶EMAIL)
				MemberService memsvc = new MemberService();
				MemberVO memberVO = memsvc.getOneMember(mem.getMem_Id());
				
				GrpService grpsvc = new GrpService();
				GrpVO grpVO_Title = grpsvc.findByPrimaryKey(grp_Id);
				//送EMAIL
				JavaMailSender mailSvc = new JavaMailSender();
				
				String mem_Account = memberVO.getMem_Account();
			    String subject = "Travel Maker的揪團通知";
			    String messageText = memberVO.getMem_Name() + "您好，您參加的揪團，揪團團名為：" + grpVO_Title.getGrp_Title() + "，已經成功出團" ;
			    mailSvc.sendMail(mem_Account, subject, messageText);
				
			    //送簡訊
				Send sendSvc = new Send();
				
				String[] tel = {memberVO.getMem_Phone()};
				String message = memberVO.getMem_Name() + "您好，您參加的揪團，揪團團名為：" + grpVO_Title.getGrp_Title() + "，已經成功出團" ;

				sendSvc.sendMessage(tel,message);
			   		    
			}
			System.out.println("簡訊已經寄出");			
			System.out.println("出團信件已經寄出");			
			
			
			/***************************3.修改完成,準備轉交(Send the Success view)*************/

			req.setAttribute("grpVO", grpVO); 
			String url = "/front_end/grp/personal_grp_review.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);
//			System.out.println("大師兄回來了");
			/***************************其他可能的錯誤處理*************************************/
		} catch (Exception e) {
			errorMsgs.add("修改資料失敗:"+e.getMessage());
			RequestDispatcher failureView = req
					.getRequestDispatcher("/front_end/grp/personal_grp_review.jsp");
			failureView.forward(req, res);
		}
	}
	
	}
	   	public String getFileNameFromPart(Part part) {
			String header = part.getHeader("content-disposition");
			// System.out.println("header=" + header); // 測試用
			String filename = new File(header.substring(header.lastIndexOf("=") + 2, header.length() - 1)).getName();
			// System.out.println("filename=" + filename); // 測試用
			// 取出副檔名
			String fnameExt = filename.substring(filename.lastIndexOf(".") + 1, filename.length()).toLowerCase();
			// System.out.println("fnameExt=" + fnameExt); // 測試用
			if (filename.length() == 0) {
				return null;
			}
			return fnameExt;
		}
}
