package com.rp_report.model;

import java.util.List;


public class Rp_reportService {
	private Rp_reportDAO_interface dao;
	
	public	Rp_reportService()	{
		dao = new Rp_reportDAO();
	}
	
	public	Rp_reportVO addRp_report(String reply_id, String mem_id, Integer rp_state) {
		
		Rp_reportVO Rp_reportVO = new Rp_reportVO();
		
		Rp_reportVO.setReply_id(reply_id);
		Rp_reportVO.setMem_id(mem_id);
		Rp_reportVO.setRp_state(rp_state);
		
		dao.insert(Rp_reportVO);
		
		return Rp_reportVO;
	}
	
	public	Rp_reportVO updateRp_report(String reply_id, String mem_id, Integer rp_state) {
		
		Rp_reportVO Rp_reportVO = new Rp_reportVO();
		
		Rp_reportVO.setReply_id(reply_id);
		Rp_reportVO.setMem_id(mem_id);
		Rp_reportVO.setRp_state(rp_state);
		
		dao.update(Rp_reportVO);
		
		return Rp_reportVO;
	}
	
	public void deleteRp_reportVO(String reply_id,String mem_id) {
		dao.delete(reply_id,mem_id);
	}
	
	public Rp_reportVO getOneRp_report(String reply_id) {
		return dao.findByPrimaryKey(reply_id);
	}

	public List<Rp_reportVO> getAll() {
		return dao.getAll();
	}
}	
