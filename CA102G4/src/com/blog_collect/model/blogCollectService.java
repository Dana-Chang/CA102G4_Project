package com.blog_collect.model;

import java.util.List;

public class blogCollectService {
	private blog_collectDAO_interface dao;

	public blogCollectService() {
		dao = new blog_collectDAO();
	}

	public blog_collectVO addBlogCollect(String mem_id, String blog_id) {
		blog_collectVO blog_collectVO = new blog_collectVO();

		blog_collectVO.setMem_id(mem_id);
		blog_collectVO.setBlog_id(blog_id);
		dao.insert(blog_collectVO);

		return blog_collectVO;
	}

	public void deleteBlogCollect(String mem_id, String blog_id) {
		blog_collectVO blog_collectVO = new blog_collectVO();

		blog_collectVO.setMem_id(mem_id);
		blog_collectVO.setBlog_id(blog_id);
		dao.delete(blog_collectVO);

	}

	public List<blog_collectVO> getAllByMem_id(String mem_id) {
		return dao.getAllByMem_id(mem_id);
	}
	
	public List<blog_collectVO> getAllByBlogId(String blog_id) {
		return dao.getAllByBlogId(blog_id);
	}
	
	public int findByPrimaryKey(String mem_id,String blog_id) {
		blog_collectVO blog_collectVO = new blog_collectVO();
		
		blog_collectVO.setMem_id(mem_id);
		blog_collectVO.setBlog_id(blog_id);
		int cnt = dao.findByPrimaryKey(blog_collectVO);
		return cnt;
	}
	
	public void deleteAll(String blog_id) {
		dao.deleteAll(blog_id);
	}
}
