package com.blog_message_report.model;

import java.util.List;

public class blogMessageReportService {
	private blog_message_reportDAO_interface dao;

	public blogMessageReportService() {
		dao = new blog_message_reportDAO();
	}

	public blog_message_reportVO addBlogMessageReport(String mem_id, String message_id,String bmr_reason) {
		blog_message_reportVO blog_message_reportVO = new blog_message_reportVO();

		blog_message_reportVO.setMem_id(mem_id);
		blog_message_reportVO.setMessage_id(message_id);
		blog_message_reportVO.setBmr_reason(bmr_reason);
		dao.insert(blog_message_reportVO);

		return blog_message_reportVO;
	}

	public blog_message_reportVO updateBlogMessageReport(Integer bmr_status, String mem_id, String message_id) {
		blog_message_reportVO blog_message_reportVO = new blog_message_reportVO();

		blog_message_reportVO.setBmr_status(bmr_status);
		blog_message_reportVO.setMem_id(mem_id);
		blog_message_reportVO.setMessage_id(message_id);
		dao.update(blog_message_reportVO);

		return blog_message_reportVO;
	}

	public List<blog_message_reportVO> getAll() {
		return dao.getAll();
	}
	
	public blog_message_reportVO getOne(String mem_id,String message_id) {
		return dao.getOne(mem_id, message_id);
	}
	//***********************取得已處理或未處理的部落格留言
	public List<blog_message_reportVO> getBlogMsgReport_ByStatus(Integer bmr_status) {
		return dao.getBlogMsgReport_ByStatus(bmr_status);
	}
	
	
	
}
