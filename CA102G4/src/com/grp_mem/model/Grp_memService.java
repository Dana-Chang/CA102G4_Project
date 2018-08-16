package com.grp_mem.model;

import java.util.List;

public class Grp_memService {
	private Grp_memDAO_interface dao;
	
	public Grp_memService() {
		dao = new Grp_memDAO();
	}
	
	//新增揪團參加者
	public Grp_memVO insert(String grp_Id,String mem_Id,String grp_Leader) {
		
		Grp_memVO grp_memVO = new Grp_memVO();
		grp_memVO.setGrp_Id(grp_Id);
		grp_memVO.setMem_Id(mem_Id);
		grp_memVO.setGrp_Leader(grp_Leader);
		
		dao.insert(grp_memVO);
		
		return grp_memVO;

	}
	
	//更新揪團
		public Grp_memVO update(String grp_Id, String mem_Id, String grp_Leader) {
			
			Grp_memVO grp_memVO = new Grp_memVO();

			grp_memVO.setGrp_Id(grp_Id);
			grp_memVO.setMem_Id(mem_Id);
			grp_memVO.setGrp_Leader(grp_Leader);

			dao.update(grp_memVO);
			return grp_memVO;
		}
	
	//參加揪團
	public Grp_memVO joingrp(String grp_Id, String mem_Id, String grp_Leader) {
		
		Grp_memVO grp_memVO = new Grp_memVO();

		grp_memVO.setGrp_Id(grp_Id);
		grp_memVO.setMem_Id(mem_Id);
		grp_memVO.setGrp_Leader(grp_Leader);

		dao.insert(grp_memVO);
		return grp_memVO;
	}
	
	//取消參加的揪團
	
	public void delete(String grp_Id, String mem_Id) {
			
			dao.delete(grp_Id,mem_Id);
	
		}
		
	//用最一剛開始的FINDBYPK找VO
	public Grp_memVO findByPrimaryKey(String grp_Id, String mem_Id) {
		Grp_memVO grp_memVO = new Grp_memVO();
		
		grp_memVO.setGrp_Id(grp_Id);
		grp_memVO.setMem_Id(mem_Id);
		
		Grp_memVO cnt = dao.findByPrimaryKey(grp_Id,mem_Id);
		
		return cnt;
	}
	//更改參加者的會員狀態(grp_Leader=0 被婉拒 =1接受)
	public void update_State(String grp_Id, String mem_Id, String grp_Leader) {
		dao.update_State(grp_Id,mem_Id,grp_Leader);
	}
	
	//取得參加者(GRP_LEADER=1)的會員資料
	public List<Grp_memVO> getAll_check_mem(String grp_Id, String grp_Leader) {
		return dao.getAll_check_mem(grp_Id,grp_Leader);
	}
	

}
