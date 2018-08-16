package com.mem_report.model;

import java.sql.Timestamp;
import java.util.List;

import com.photo_report.model.Photo_reportVO;

public class Member_reportService {
	private Member_reportDAO dao;
	
	public Member_reportService() {
		dao = new Member_reportDAO();
	}
	
	public Member_reportVO addmemberreport(String mem_Id_Report,String mem_Id_Reported,String report_Reason,Integer mem_Rep_Sta) {
		Member_reportVO member_reportVO = new Member_reportVO();
		
		member_reportVO.setMem_Id_report(mem_Id_Report);
		member_reportVO.setMem_Id_reported(mem_Id_Reported);
		member_reportVO.setReport_Reason(report_Reason);
		member_reportVO.setMem_Rep_Sta(mem_Rep_Sta);

		dao.insert(member_reportVO);

		return member_reportVO;
	}
	
	public void update_review_State(String mem_Id_Report,String mem_Id_Reported, Integer mem_Rep_Sta) {
		
		dao.update_review_State(mem_Id_Report,mem_Id_Reported,mem_Rep_Sta);

	}
	
	public Member_reportVO findByPrimaryKey(String mem_Id_Report,String mem_Id_Reported) {
			
			return dao.findByPrimaryKey(mem_Id_Report,mem_Id_Reported);
	
	}
	
	//取得全部檢舉
	 public List<Member_reportVO> getAll(){
	  return dao.getAll();
	 }
}