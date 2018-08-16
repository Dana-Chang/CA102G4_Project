package com.blog_collect.model;

import java.util.List;

public interface blog_collectDAO_interface {
	public int insert(blog_collectVO blog_collectVO);

	public int delete(blog_collectVO blog_collectVO);

	public List<blog_collectVO> getAllByMem_id(String mem_id);
	
	public List<blog_collectVO> getAllByBlogId(String blog_id);
	
	public int findByPrimaryKey(blog_collectVO blog_collectVO);
	
	public int deleteAll(String blog_id);
}
