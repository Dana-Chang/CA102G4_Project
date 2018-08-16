package com.question.model;

import java.sql.Date;
import java.util.List;

import com.qa_classification.model.Qa_classificationVO;



public class QuestionService {
	
	private	QuestionDAO_interface dao;

	public	QuestionService() {
		dao = new QuestionDAO();
	}
	
	public	QuestionVO addQuestion(String mem_id, String question_content, Date build_date,Integer q_state) {
		
		QuestionVO QuestionVO = new QuestionVO();
		

		QuestionVO.setMem_id(mem_id);
		QuestionVO.setQuestion_content(question_content);
		QuestionVO.setBuild_date(build_date);
		QuestionVO.setQ_state(q_state);
		
		dao.insert(QuestionVO);
		
		return QuestionVO;
	}
	
	public	QuestionVO updateQuestion(String question_id, String mem_id, String question_content, Date build_date,Integer q_state) {
		
		QuestionVO QuestionVO = new QuestionVO();
		
		QuestionVO.setQuestion_id(question_id);
		QuestionVO.setMem_id(mem_id);
		QuestionVO.setQuestion_content(question_content);
		QuestionVO.setBuild_date(build_date);
		QuestionVO.setQ_state(q_state);
		
		dao.update(QuestionVO);
		
		return QuestionVO;
	}
	
	public void deleteQuestion(String question_id) {
		dao.delete(question_id);
	}
	
	public QuestionVO getOneQuestion(String question_id) {
		return dao.findByPrimaryKey(question_id);
	}

	public List<QuestionVO> getAll() {
		return dao.getAll();
	}

	public void insertQuestionAndQa_class(QuestionVO questionVO,List<Qa_classificationVO> list) {
		dao.insertQ(questionVO, list);
	}
	
	
	public List<QuestionVO> getOneState() {
		return dao.find_by_State();
	}
	
	public	QuestionVO updateQ(String question_id,Integer q_state) {
		
		QuestionVO QuestionVO = new QuestionVO();
		
		QuestionVO.setQuestion_id(question_id);
		QuestionVO.setQ_state(q_state);
		
		dao.updateQ(QuestionVO);
		
		return QuestionVO;
	}
	
	public List<QuestionVO> findByKeyword(String keyword) {
		return dao.findByKeyword(keyword);
	}
}	
