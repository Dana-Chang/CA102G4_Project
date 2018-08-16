package com.qa_report.model;

import java.util.List;

import com.qa_report.model.Qa_reportVO;

public interface Qa_reportDAO_interface {
	public int insert(Qa_reportVO Qa_reportVO);
	public int update(Qa_reportVO Qa_reportVO);
	public int delete (String question_id,String mem_id);
	public Qa_reportVO findByPrimaryKey(String question_id);
	public List <Qa_reportVO> getAll();
	
}
