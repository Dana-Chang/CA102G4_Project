package com.qa_collect.model;

import java.util.List;

import com.qa_collect.model.Qa_collectVO;

public interface Qa_collectDAO_interface {
	public int insert(Qa_collectVO Qa_collectVO);
	public int update(Qa_collectVO Qa_collectVO);
	public int delete (String question_id);
	public Qa_collectVO findByPrimaryKey(String question_id);
	public List <Qa_collectVO> getAll();
	
	public List<Qa_collectVO> getAllByMem_id(String mem_id);
}

