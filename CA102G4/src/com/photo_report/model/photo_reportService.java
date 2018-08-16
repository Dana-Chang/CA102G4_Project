package com.photo_report.model;

import java.sql.Timestamp;
import java.util.List;

public class photo_reportService {
	private Photo_reportDAO dao;
	
	public photo_reportService(){
		dao = new Photo_reportDAO();
	}
	
	public Photo_reportVO addphotoreport(String photo_No,String mem_Id,String report_Reason,Timestamp report_Time,Integer pho_Rep_Stats) {
		Photo_reportVO photo_reportVO = new Photo_reportVO();
		
		photo_reportVO.setPhoto_No(photo_No);
		photo_reportVO.setMem_Id(mem_Id);
		photo_reportVO.setReport_Reason(report_Reason);
		photo_reportVO.setReport_Time(report_Time);
		photo_reportVO.setPho_Rep_Stats(pho_Rep_Stats);

		dao.insert(photo_reportVO);

		return photo_reportVO;
	}
	
	public void update_review_State(String mem_Id,String photo_No,Integer pho_Rep_Stats) {
		
		dao.update_review_State(mem_Id,photo_No,pho_Rep_Stats);

	}
	
	public List<Photo_reportVO> getAll(){
		return dao.getAll();
	}
	
	public Photo_reportVO findByPrimaryKey(String photo_No,String mem_Id) {
		
		return dao.findByPrimaryKey(photo_No, mem_Id);

	}

}
