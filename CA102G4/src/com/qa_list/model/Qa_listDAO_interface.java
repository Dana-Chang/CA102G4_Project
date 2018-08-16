package com.qa_list.model;

import java.util.List;

import com.qa_list.model.Qa_listVO;

public interface Qa_listDAO_interface {
	public int insert(Qa_listVO Qa_listVO);
	public int update(Qa_listVO Qa_listVO);
	public int delete (String list_id);
	public Qa_listVO findByPrimaryKey(String list_id);
	public List <Qa_listVO> getAll();
}
