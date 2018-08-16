package com.photo_report.model;

import java.util.List;

public interface Photo_reportDAO_interface {
	
	public void insert(Photo_reportVO photo_reportVO);
	
	public void update(Photo_reportVO photo_reportVO);
	
	public void delete(String photo_Id,String mem_Id);
	
	public Photo_reportVO findByPrimaryKey(String photo_No,String mem_Id);
	
	public List<Photo_reportVO> getAll();
	
	//更改照片牆審核狀態
	int update_review_State(String mem_Id,String photo_No,Integer pho_Rep_Stats);
		
}
