package com.grp.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.mem.model.MemberVO;


public class GrpService {
	private GrpDAO_interface dao;
	
	public GrpService() {
		dao = new GrpDAO();
	}
	
	
	public GrpVO insertGrp
	(String grp_Id, String mem_Id,Timestamp grp_Start,Timestamp grp_End
	,Integer grp_Cnt,Integer grp_Acpt,String trip_No,Timestamp trip_Start,Timestamp trip_End
	,String trip_Locale,String trip_Details,byte[] grp_Photo,Integer grp_Status,String chatroom_Id,String grp_Title,String grp_Price) {	
		
		GrpVO grpVO = new GrpVO();
		grpVO.setGrp_Id(grp_Id);
		grpVO.setMem_Id(mem_Id);
		grpVO.setGrp_Start(grp_Start);
		grpVO.setGrp_End(grp_End);
		grpVO.setGrp_Cnt(grp_Cnt);
		grpVO.setGrp_Acpt(grp_Acpt);
		grpVO.setTrip_No(trip_No);
		grpVO.setTrip_Start(trip_Start);
		grpVO.setTrip_End(trip_End);
		grpVO.setTrip_Locale(trip_Locale);
		grpVO.setTrip_Details(trip_Details);
		grpVO.setGrp_Photo(grp_Photo);
		grpVO.setGrp_Status(grp_Status);
		grpVO.setChatroom_Id(chatroom_Id);
		grpVO.setGrp_Title(grp_Title);
		grpVO.setGrp_Price(grp_Price);

		dao.insert(grpVO);

		return grpVO;
	}
	
	//新增揪團
	 public GrpVO addGrp
	 (String mem_Id,String grp_Title,String trip_Locale,Timestamp grp_End,Timestamp trip_Start,Timestamp trip_End
	 ,Integer grp_Status,String grp_Price,Integer grp_Cnt,Integer grp_Acpt,byte[] grp_Photo) {
		 
		 System.out.println("123");
		 
	  GrpVO grpVO = new GrpVO();
	  
		 System.out.println("grpVO");

	  grpVO.setMem_Id(mem_Id);
	  grpVO.setGrp_Title(grp_Title);
	  grpVO.setTrip_Locale(trip_Locale);
	  grpVO.setGrp_End(grp_End);
	  grpVO.setTrip_Start(trip_Start);
	  grpVO.setTrip_End(trip_End);
	  grpVO.setGrp_Status(grp_Status);
	  grpVO.setGrp_Price(grp_Price);
	  grpVO.setGrp_Cnt(grp_Cnt);
	  grpVO.setGrp_Acpt(grp_Acpt);
	  grpVO.setGrp_Photo(grp_Photo);


		 System.out.println("INSERT前");

	  dao.insert(grpVO);
		 System.out.println("過INSERT");

	  return grpVO;
	 }
	 //更新揪團
	 public GrpVO updateGrp(		 
			 String grp_Id,
			 String grp_Title,
			 String trip_Locale,
			 String grp_Price,
			 Integer grp_Cnt,
			 Integer grp_Acpt,
			 Timestamp grp_End,
			 Timestamp trip_Start,
			 Timestamp trip_End,
			 String trip_Details,
			 byte[] pic
	 		 ){
		 
		 System.out.println("update123");
		 
	  GrpVO grpVO = new GrpVO();
	  
		 System.out.println("update-grpVO");
	  
	  grpVO.setGrp_Id(grp_Id);
	  grpVO.setGrp_Title(grp_Title);
	  grpVO.setTrip_Locale(trip_Locale);
	  grpVO.setGrp_Price(grp_Price);
	  grpVO.setGrp_Cnt(grp_Cnt);
	  grpVO.setGrp_Acpt(grp_Acpt);
	  grpVO.setGrp_End(grp_End);
	  grpVO.setTrip_Start(trip_Start);
	  grpVO.setTrip_End(trip_End);
	  grpVO.setTrip_Details(trip_Details);
	  grpVO.setGrp_Photo(pic);

		 System.out.println("update前");

	  dao.update(grpVO);
		 System.out.println("過update");

	  return grpVO;
	 }
	
	//取得所有揪團
	public List<GrpVO> getAll(){
		return dao.getAll();
	}
	
	//用ID取得揪團
	public GrpVO findByPrimaryKey(String grp_Id) {
		return dao.findByPrimaryKey(grp_Id);
		}
	
	//萬用複合查詢
	public List<GrpVO> getAll(Map<String, String[]> map) {
		return dao.getAll(map);
	}
	
	 //*******搜尋某會員所有開團資訊(不論狀況為0123)//
	public List<GrpVO> getAll_ByMemId(String mem_Id) {
		return dao.getAll_ByMemID(mem_Id);
	}
	
	
	//成團後更改揪團狀態(=2成團)
	 public GrpVO update_status(String grp_Id,Integer grp_Status){
		 			 
	  GrpVO grpVO = new GrpVO();

	  grpVO.setGrp_Id(grp_Id);
	  grpVO.setGrp_Status(grp_Status);

	  dao.update_status(grpVO);

	  return grpVO;
	 }
	 
	// 揪團可報名人數、接受人數 確定參加會減少
	 public GrpVO update_mem_less(String grp_Id, Integer grp_Cnt){
			 
		  GrpVO grpVO = new GrpVO();

		  grpVO.setGrp_Id(grp_Id);
		  grpVO.setGrp_Cnt(grp_Cnt);
		  
		  dao.update_mem_less(grpVO);

		  return grpVO;
		 }
	 
	// 揪團可報名人數、接受人數 取消參加會增加	 
	 public GrpVO update_mem_plus(String grp_Id, Integer grp_Cnt){
		 
		  GrpVO grpVO = new GrpVO();

		  grpVO.setGrp_Id(grp_Id);
		  grpVO.setGrp_Cnt(grp_Cnt);

		  dao.update_mem_plus(grpVO);

		  return grpVO;
		 }
	
}
