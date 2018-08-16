package com.qa_list.model;

import java.util.List;

public class Qa_listService {

private	Qa_listDAO_interface dao;
	
	public	Qa_listService() {
		dao = new Qa_listDAO();
	}
	
	public	Qa_listVO addQa_list( String list_name) {
		
		Qa_listVO Qa_listVO = new Qa_listVO();
		
		Qa_listVO.setList_name(list_name);
		
		dao.insert(Qa_listVO);
		
		return Qa_listVO;
	}
	
	public	Qa_listVO updateQa_list(String list_id, String list_name) {
		
		Qa_listVO Qa_listVO = new Qa_listVO();
		
		Qa_listVO.setList_id(list_id);
		Qa_listVO.setList_name(list_name);
		
		dao.update(Qa_listVO);
		
		return Qa_listVO;
	}
	
	public void deleteQa_list(String list_id) {
		dao.delete(list_id);
	}
	
	public Qa_listVO getOneQa_list(String list_id) {
		return dao.findByPrimaryKey(list_id);
	}

	public List<Qa_listVO> getAll() {
		return dao.getAll();
	}
}	

