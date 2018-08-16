package com.blog.model;

import java.util.List;

public class blogService {
	private blogDAO_interface dao;

	public blogService() {
		dao = new blogDAO();
	}

	public String addBlog(String trip_no, String mem_id, String blog_title, String blog_content,
			java.sql.Date travel_date, Integer blog_views, byte[] blog_coverimage) {
		blogVO blogVO = new blogVO();

		blogVO.setTrip_no(trip_no);
		blogVO.setMem_id(mem_id);
		blogVO.setBlog_title(blog_title);
		blogVO.setBlog_content(blog_content);
		blogVO.setTravel_date(travel_date);
		blogVO.setBlog_views(blog_views);
		blogVO.setBlog_coverimage(blog_coverimage);
		String blog_id = dao.insert(blogVO);

		return blog_id;
	}

	public blogVO updateBlog(String trip_no, String blog_title, String blog_content, java.sql.Date travel_date, byte[] blog_coverimage,String blog_id) {
		blogVO blogVO = new blogVO();

		blogVO.setTrip_no(trip_no);
		blogVO.setBlog_title(blog_title);
		blogVO.setBlog_content(blog_content);
		blogVO.setTravel_date(travel_date);
		blogVO.setBlog_coverimage(blog_coverimage);
		blogVO.setBlog_id(blog_id);
		dao.update(blogVO);

		return blogVO;
	}

	public void deleteBlog(String blog_id) {
		dao.delete(blog_id);
	}
	
	public void updateBlogViews(String blog_id) {
		dao.updateViews(blog_id);
	}
	
	public List<blogVO> getAllByNew() {
		return dao.getAllByNew();
	}

	public List<blogVO> getAllByHot() {
		return dao.getAllByHot();
	}

	public List<blogVO> getAllByNewFour() {
		return dao.getAllByNewFour();
	}

	public List<blogVO> getAllByHotFour() {
		return dao.getAllByHotFour();
	}

	public List<blogVO> findByMemId(String mem_id) {
		return dao.findByMemId(mem_id);
	}

	public List<blogVO> getAllByKeywordOrderByDate(String keyword) {
		return dao.getAllByKeywordOrderByDate(keyword);
	}
	public List<blogVO> getAllByKeywordOrderByViews(String keyword) {
		return dao.getAllByKeywordOrderByViews(keyword);
	}
	public blogVO findByPrimaryKey(String blog_id) {
		return dao.findByPrimaryKey(blog_id);
	}
	public List<blogVO> getThreeByMem_id(String mem_id,String blog_id) {
		return dao.getThreeByMem_id(mem_id, blog_id);
	}
	public void hideBlog(Integer blog_status,String blog_id) {
		dao.updateStatus(blog_status, blog_id);
	}
}
