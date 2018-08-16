package com.qa_collect.model;

import java.util.List;




public class Qa_collectService {

private	Qa_collectDAO_interface dao;
	
	public	Qa_collectService() {
		dao = new Qa_collectDAO();
	}
	
	public	Qa_collectVO addQa_collect(String question_id, String mem_id) {
		
		Qa_collectVO Qa_collectVO = new Qa_collectVO();
		
		Qa_collectVO.setQuestion_id(question_id);
		Qa_collectVO.setMem_id(mem_id);
		
		dao.insert(Qa_collectVO);
		
		return Qa_collectVO;
	}
	
	public	Qa_collectVO updateNews(String question_id, String mem_id) {
		
		Qa_collectVO Qa_collectVO = new Qa_collectVO();
		
		Qa_collectVO.setQuestion_id(question_id);
		Qa_collectVO.setMem_id(mem_id);
		
		dao.update(Qa_collectVO);
		
		return Qa_collectVO;
	}
	
	public void deleteQa_collect(String question_id) {
		dao.delete(question_id);
	}
	
	public Qa_collectVO getOneQa_collect(String question_id) {
		return dao.findByPrimaryKey(question_id);
	}

	public List<Qa_collectVO> getAll() {
		return dao.getAll();
	}
	
	public List<Qa_collectVO> getAllByMem_id(String mem_id) {
		return dao.getAllByMem_id(mem_id);
	}
}	

