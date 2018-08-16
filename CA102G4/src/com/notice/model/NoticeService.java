package com.notice.model;

import java.util.List;


public class NoticeService {

private	NoticeDAO_interface dao;
	
	public	NoticeService() {
		dao = new NoticeDAO();
	}
	
	public	NoticeVO addNews(String notice_id, String mem_idfrom , String mem_idto , String notice_title, String notice_con, Integer notice_state) {
		
		NoticeVO NoticeVO = new NoticeVO();
		
		NoticeVO.setNotice_id(notice_id);
		NoticeVO.setMem_idfrom(mem_idfrom);
		NoticeVO.setMem_idto(mem_idto);
		NoticeVO.setNotice_title(notice_title);
		NoticeVO.setNotice_con(notice_con);
		NoticeVO.setNotice_state(notice_state);
		
		dao.insert(NoticeVO);
		
		return NoticeVO;
	}
	
	public	NoticeVO updateNews(String notice_id, String mem_idfrom , String mem_idto , String notice_title, String notice_con, Integer notice_state) {
		
		NoticeVO NoticeVO = new NoticeVO();
		
		NoticeVO.setNotice_id(notice_id);
		NoticeVO.setMem_idfrom(mem_idfrom);
		NoticeVO.setMem_idto(mem_idto);
		NoticeVO.setNotice_title(notice_title);
		NoticeVO.setNotice_con(notice_con);
		NoticeVO.setNotice_state(notice_state);
		
		dao.update(NoticeVO);
		
		return NoticeVO;
	}
	
	public void deleteNotice(String notice_id) {
		dao.delete(notice_id);
	}
	
	public NoticeVO getOneNotice(String notice_id) {
		return dao.findByPrimaryKey(notice_id);
	}

	public List<NoticeVO> getAll() {
		return dao.getAll();
	}
}	

