package com.blog_tag_name.model;

import java.util.List;

public class blogTagNameService {
	private blog_tag_nameDAO_interface dao;

	public blogTagNameService() {
		dao = new blog_tag_nameDAO();
	}

	public blog_tag_nameVO addblogTagName(String btn_class, String btn_name) {
		blog_tag_nameVO blog_tag_nameVO = new blog_tag_nameVO();

		blog_tag_nameVO.setBtn_class(btn_class);
		blog_tag_nameVO.setBtn_name(btn_name);
		dao.insert(blog_tag_nameVO);

		return blog_tag_nameVO;
	}

	public blog_tag_nameVO updateBlogTagName(String btn_class, String btn_name, String btn_id) {
		blog_tag_nameVO blog_tag_nameVO = new blog_tag_nameVO();

		blog_tag_nameVO.setBtn_class(btn_class);
		blog_tag_nameVO.setBtn_name(btn_name);
		blog_tag_nameVO.setBtn_id(btn_id);
		dao.update(blog_tag_nameVO);

		return blog_tag_nameVO;
	}

	public void deleteBlogTagName(String btn_id) {
		dao.delete(btn_id);
	}

	public List<blog_tag_nameVO> getAllBytagClass(String keyword) {
		return dao.getAllBytagClass(keyword);
	}
	
	public List<blog_tag_nameVO> getAll() {
		return dao.getAll();
	}
	
	public blog_tag_nameVO findByBtn_id(String btn_id) {
		return dao.findByBtn_id(btn_id);
	}
}
