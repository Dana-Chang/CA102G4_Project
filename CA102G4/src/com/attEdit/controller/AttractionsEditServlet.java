package com.attEdit.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.attEdit.model.*;
import com.attractions.model.*;

@MultipartConfig
public class AttractionsEditServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=Big5");
		req.setCharacterEncoding("UTF-8");

		String action = req.getParameter("action");
		
		if("reviewOne".equals(action)) {

			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				String attEdit_no = req.getParameter("attEdit_no");
				
				/***************************2.開始查詢資料*****************************************/
				AttractionsEditService attEditSvc = new AttractionsEditService();
				AttractionsEditVO attEditVO = attEditSvc.getOneAttEditByPK(attEdit_no);
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("attEditVO", attEditVO);
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/attEdit/back_attEditReview.jsp");
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			}catch (Exception e) {
				errorMsgs.put("Exception","無法取得要審核的資料"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back_end/attEdit/back_attEdit.jsp");
				failureView.forward(req, res);
			}
		}
		
		if("updataCommit".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				String attEdit_no = req.getParameter("attEdit_no");
				
				/***************************2.開始修改資料*****************************************/
				AttractionsEditService attEditSvc = new AttractionsEditService();
				AttractionsEditVO attEditVO = attEditSvc.getOneAttEditByPK(attEdit_no);
				attEditSvc.att_update(attEditVO);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/attEdit/back_attEdit.jsp");
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			}catch (Exception e) {
				errorMsgs.put("Exception","無法修改資料"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back_end/attEdit/back_attEdit.jsp");
				failureView.forward(req, res);
			}
		}
		if("updataDelete".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數***************************************/
				String attEdit_no = req.getParameter("attEdit_no");
				/***************************2.開始刪除資料***************************************/
				AttractionsEditService attEditSvc = new AttractionsEditService();
				attEditSvc.delete(attEdit_no);
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/back_end/attEdit/back_attEdit.jsp");
				successView.forward(req, res);
			}catch (Exception e) {
				errorMsgs.put("Exception","無法刪除資料"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back_end/attEdit/back_attEdit.jsp");
				failureView.forward(req, res);
			}
			
		}
		
		if("userEdit".equals(action)){
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				String att_no = req.getParameter("att_no");
				
				/***************************2.開始查詢資料*****************************************/
				AttractionsService attSvc = new AttractionsService();
				AttractionsVO attVO = attSvc.getOneAttByPK(att_no);
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("attVO", attVO);
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/attractions/attEdit.jsp");
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			}catch (Exception e) {
				errorMsgs.put("Exception","無法取得要編輯的資料"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/attractions/attDetail.jsp");
				failureView.forward(req, res);
			}
		}
		
		if("userEditCommit".equals(action)) {
			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				String att_no = req.getParameter("att_no");
				
//				String mem_id = String.valueOf(req.getSession().getAttribute("mem_id"));
				String mem_id = req.getParameter("mem_id");
				
				String att_name = req.getParameter("att_name");
				if (att_name == null || att_name.trim().length() == 0) {
					errorMsgs.put("att_name","(請勿空白)");
				}
				
				Part att_picture_part = req.getPart("att_picture");
				String fileName = getFileNameFromPart(att_picture_part);
				boolean att_picture_trigger;;
				//有使用檔案上傳
				if(fileName!=null) {
					//用tomcat判斷檔案類型
					String mimeType = getServletContext().getMimeType(fileName);
					att_picture_trigger = true;
					if(mimeType.substring(0, mimeType.indexOf("/"))!="image") {
						errorMsgs.put("att_picture","(請上傳圖片類型檔案)");
					}
				}else {
					att_picture_trigger = false;
				}
				
				String country = req.getParameter("country");
				if (country == null || country.trim().length() == 0) {
					errorMsgs.put("country","(請勿空白)");
				}
				
				String administrative_area = req.getParameter("administrative_area");
				if (administrative_area == null || administrative_area.trim().length() == 0) {
					errorMsgs.put("administrative_area","(請勿空白)");
				}
				
				Double att_lat = null;
				try {
					att_lat = new Double(req.getParameter("att_lat").trim());
				} catch (NumberFormatException e) {
					errorMsgs.put("att_lat","(請填數字)");
				}
				
				Double att_lon = null;
				try {
					att_lon = new Double(req.getParameter("att_lon").trim());
				} catch (NumberFormatException e) {
					errorMsgs.put("att_lon","(請填數字)");
				}
				
				String att_address = req.getParameter("att_address");
				if (att_address == null || att_address.trim().length() == 0) {
					errorMsgs.put("att_address","(請勿空白)");
				}
				
				String att_information = req.getParameter("att_information");
				if (att_information == null || att_information.trim().length() == 0) {
					errorMsgs.put("att_information","(請勿空白)");
				}
				
				AttractionsVO attVO = new AttractionsVO();
				attVO.setAtt_no(att_no);
				attVO.setAtt_name(att_name);
				attVO.setAtt_lat(att_lat);
				attVO.setAtt_lon(att_lon);
				attVO.setCountry(country);
				attVO.setAtt_address(att_address);
				attVO.setAtt_information(att_information);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("attVO", attVO);
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/attractions/attEdit.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.開始新增資料***************************************/
				AttractionsEditService attEditSvc = new AttractionsEditService();
				//att_picture_trigger (true=有上傳檔案, false=無上傳檔案)
				byte[] att_picture=null;
				if(att_picture_trigger) {
					InputStream in = att_picture_part.getInputStream();
					att_picture = new byte[in.available()];
					in.read(att_picture);
					in.close();
				}else {
					AttractionsService attSvc = new AttractionsService();
					att_picture = attSvc.getPicture(att_no);
				}
				AttractionsEditVO attEditVO = attEditSvc.addAttEdit(mem_id, att_no, att_name, att_lat, att_lon, country, administrative_area,att_information, att_picture, att_address);
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				RequestDispatcher successView = req.getRequestDispatcher("/front_end/attractions/attDetail.jsp");
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			}catch(Exception e) {
				errorMsgs.put("Exception",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/attractions/attEdit.jsp");
				failureView.forward(req, res);
				e.printStackTrace();
			}
		}
	}
	
	public String getFileNameFromPart(Part part) {
		String header = part.getHeader("content-disposition");
//		System.out.println(header);
		// header=form-data; name="upfile1";
		// filename="C:\JavaEE_Workspace\IBM_Servlet4\WebContent\images\tomcat.gif"
		// substring beginIndex - 起始索引（包括）。endIndex - 結束索引（不包括）。
		String filename = new File(header.substring(header.lastIndexOf("=") + 2, header.length() - 1)).getName();
		if (filename.length() == 0) {
			return null;
		}
		return filename;
	}
}
