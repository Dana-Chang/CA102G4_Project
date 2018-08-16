package com.blog_message.model;

import java.util.List;

public interface blog_messageDAO_interface {
	public int insert(blog_messageVO blog_messageVO);

	public int update(blog_messageVO blog_messageVO);

	public int delete(String message_id);
	
	public List<blog_messageVO> findByBlogId(String blog_id);
	
	public int updateStatus(String message_id,String mem_id,Integer bm_status);
	
	public List<blog_messageVO> getAll();
	
	public int updateStatusForBackEnd(String message_id,Integer bm_status);
}
