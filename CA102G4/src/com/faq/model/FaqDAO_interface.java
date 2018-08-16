package com.faq.model;

import java.util.List;

public interface FaqDAO_interface {
	public int insert(FaqVO FaqVO);
	public int update(FaqVO FaqVO);
	public int delete (String faq_id);
	public FaqVO findByPrimaryKey(String faq_id);
	public List <FaqVO> getAll();
}
