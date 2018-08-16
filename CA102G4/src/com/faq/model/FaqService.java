package com.faq.model;

import java.util.List;

public class FaqService {

	private	FaqDAO_interface dao;
	
	public	FaqService() {
		dao = new FaqDAO();
	}
	
	public	FaqVO	addFaq(String faq_qa,String faq_content) {
		
		FaqVO FaqVO = new FaqVO();
		
		FaqVO.setFaq_qa(faq_qa);
		FaqVO.setFaq_content(faq_content);
		
		dao.insert(FaqVO);
		
		return FaqVO;
	}
	
	public FaqVO updateFaq(String faq_id,String faq_qa,String faq_content) {
		
		FaqVO FaqVO = new FaqVO();
		
		FaqVO.setFaq_id(faq_id);
		FaqVO.setFaq_qa(faq_qa);
		FaqVO.setFaq_content(faq_content);
		
		dao.update(FaqVO);
		
		return FaqVO;
	}
	
	public void deleteFaq(String faq_id) {
		dao.delete(faq_id);
	}
	
	public FaqVO getOneFaq(String faq_id) {
		return dao.findByPrimaryKey(faq_id);
	}

	public List<FaqVO> getAll() {
		return dao.getAll();
	}
}	

