package com.qa_classification.model;

import java.util.List;

import com.qa_classification.model. Qa_classificationVO;

public interface Qa_classificationDAO_interface {
	public int insert(Qa_classificationVO Qa_classificationVO);
	public int update(Qa_classificationVO Qa_classificationVO);
	public int delete (String question_id);
	public List<Qa_classificationVO> findByQuestion_id(String question_id);
	public List<Qa_classificationVO> findByList_id(String list_id);
	public List <Qa_classificationVO> getAll();
}
