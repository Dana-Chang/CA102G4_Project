package com.qa_report.model;

import java.util.List;


public class Qa_reportService {
	private	Qa_reportDAO_interface dao;

	public	Qa_reportService() {
		dao = new Qa_reportDAO();
	}
	
	public	Qa_reportVO addQa_report(String question_id, String mem_id, Integer qa_state) {
		
		Qa_reportVO Qa_reportVO = new Qa_reportVO();
		
		Qa_reportVO.setQuestion_id(question_id);
		Qa_reportVO.setMem_id(question_id);
		Qa_reportVO.setMem_id(mem_id);
		Qa_reportVO.setQa_state(qa_state);
		
		dao.insert(Qa_reportVO);
		
		return Qa_reportVO;
	}
	
	public	Qa_reportVO updateQa_report(String question_id, String mem_id, Integer qa_state) {
		
		Qa_reportVO Qa_reportVO = new Qa_reportVO();
		
		Qa_reportVO.setQuestion_id(question_id);
		Qa_reportVO.setMem_id(question_id);
		Qa_reportVO.setMem_id(mem_id);
		Qa_reportVO.setQa_state(qa_state);
		
		dao.update(Qa_reportVO);
		
		return Qa_reportVO;
	}
	
	public void deleteQa_report(String question_id,String mem_id) {
		dao.delete(question_id,mem_id);
	}
	
	public Qa_reportVO getOneQa_report(String question_id) {
		return dao.findByPrimaryKey(question_id);
	}

	public List<Qa_reportVO> getAll() {
		return dao.getAll();
	}
	
}	


