package com.question.model;

import java.util.List;

import com.qa_classification.model.Qa_classificationVO;


public interface QuestionDAO_interface {
		
	public int insert(QuestionVO questionVO);
	public void insertQ(QuestionVO questionVO,List<Qa_classificationVO> list);
    public int update(QuestionVO questionVO);
    public int delete(String  question_id);
    public QuestionVO findByPrimaryKey(String question_id);
    public List<QuestionVO> getAll();    
    public List<QuestionVO> find_by_State();
    
    public int updateQ(QuestionVO questionVO);
    
  	// ¥@»Ê¥´ªº
  	public List<QuestionVO> findByKeyword(String keyword);
}
