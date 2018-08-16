package com.grp_mem.model;

import java.util.List;

public interface Grp_memDAO_interface {
	public void insert(Grp_memVO grp_memVO);
	public void update(Grp_memVO grp_memVO);
	public void delete(String grp_Id,String mem_Id);
	public Grp_memVO findByPrimaryKey(String grp_Id,String mem_Id);
	public List<Grp_memVO> getAll() ;
	
	//取得參加揪團會員的資料
	List<Grp_memVO> getAll_join_mem(String grp_Id);
	
	//更改參加者的狀態(接受或拒絕)
	int update_State(String grp_Id, String mem_Id, String grp_Leader);

	//取得參加者(GRP_LEADER=1)的會員資料
	List<Grp_memVO> getAll_check_mem(String grp_Id,String grp_Leader);

	
}
