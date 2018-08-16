package com.blog_tag_name.model;

import java.util.List;

public interface blog_tag_nameDAO_interface {

	public int insert(blog_tag_nameVO blog_tag_nameVO);
	
	public int update(blog_tag_nameVO blog_tag_nameVO);
	
	public int delete(String btn_id);
	
	public List<blog_tag_nameVO> getAllBytagClass(String blog_tag_nameVO);
	
	public List<blog_tag_nameVO> getAll();
	
	public blog_tag_nameVO findByBtn_id(String btn_id);
}
