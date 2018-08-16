package com.qa_classification.model;

import java.util.List;


public class Qa_classificationService {

private	Qa_classificationDAO_interface dao;
	
	public	Qa_classificationService() {
		dao = new Qa_classificationDAO();
	}
	
	public	Qa_classificationVO addQa_classification(String question_id, String list_id) {
		
		Qa_classificationVO Qa_classificationVO = new Qa_classificationVO();
		
		Qa_classificationVO.setQuestion_id(question_id);
		Qa_classificationVO.setList_id(list_id);
		
		dao.insert(Qa_classificationVO);
		
		return Qa_classificationVO;
	}
		
	public	Qa_classificationVO updateQa_classification(String question_id, String list_id) {
		
		Qa_classificationVO Qa_classificationVO = new Qa_classificationVO();
		
		Qa_classificationVO.setQuestion_id(question_id);
		Qa_classificationVO.setList_id(list_id);
		
		dao.insert(Qa_classificationVO);
		
		return Qa_classificationVO;
	}
	
	public void deleteQa_classification(String question_id) {
		dao.delete(question_id);
	}
	
	public List<Qa_classificationVO> getOneNoticeByQuestion_id(String question_id) {
		return dao.findByQuestion_id(question_id);
	}
	
	public List<Qa_classificationVO> getOneNoticeByList_id(String list_id) {
		return dao.findByList_id(list_id);
	}
	
	public List<Qa_classificationVO> getAll() {
		return dao.getAll();
	}
}	

