package com.blog_tag.model;

import java.util.List;

public interface blog_tagDAO_interface {
	
	public int insert(blog_tagVO blog_tagVO);
	
	public int deleteOne(blog_tagVO blog_tagVO);
	
	public int deleteAll(String blog_id);
	
	public List<blog_tagVO> getAllByABlog(String blog_id);
	
	public List<blog_tagVO> getAll();
	
	public int deleteAllByBtnID(String btn_id);
}
