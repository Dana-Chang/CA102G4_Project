package com.notice.model;

import java.util.List;

import com.notice.model.NoticeVO;

public interface NoticeDAO_interface {
	public int insert(NoticeVO NoticeVO);
	public int update(NoticeVO NoticeVO);
	public int delete (String notice_id);
	public NoticeVO findByPrimaryKey(String notice_id);
	public List <NoticeVO> getAll();
}
