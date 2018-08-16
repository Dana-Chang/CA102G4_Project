package com.grp.model;

import java.util.List;
import java.util.Map;

import com.grp_mem.model.Grp_memVO;

public interface GrpDAO_interface {
	public void insert(GrpVO grpVO);
	public void delete(String grp_Id);
	public GrpVO findByPrimaryKey(String grp_Id);
	public List<GrpVO> getAll();
	
	//萬用複合查詢(傳入參數型態Map)(回傳 List)
    public List<GrpVO> getAll(Map<String, String[]> map);
    
    //修改揪團內容
	public void update(GrpVO grpVO);
	
	//取得我參加揪團的內容
	List<GrpVO> getAll_join_grp(String mem_Id);

	//取得我開揪團的內容(不論狀態0123)
	List<GrpVO> getAll_ByMemID(String mem_Id);
	
	//成團後更改揪團狀態(=2成團)
	public void update_status(GrpVO grpVO);
	
	// 揪團可報名人數、接受人數 確定參加會減少
	public void update_mem_less(GrpVO grpVO);
	
	// 揪團可報名人數、接受人數 確定參加會減少
	public void update_mem_plus(GrpVO grpVO);

}
