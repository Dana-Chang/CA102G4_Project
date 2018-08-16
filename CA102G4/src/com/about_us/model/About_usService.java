package com.about_us.model;

import java.util.List;

public class About_usService {
	
	private About_usDAO_interface dao;
	
	public About_usService() {
		dao = new About_usDAO();
		
	}
	
	public About_usVO addAbout_us(String about_us_id,String	about_content) {
		
		About_usVO About_usVO = new About_usVO();
		
		About_usVO.setAbout_us_id(about_us_id);
		About_usVO.setAbout_content(about_content);
		
		dao.insert(About_usVO);
		
		return About_usVO;
	}
	
	public About_usVO updateAbout_us(String	about_us_id,String	about_content) {
		
		About_usVO About_usVO = new About_usVO();
		
		About_usVO.setAbout_us_id(about_us_id);
		About_usVO.setAbout_content(about_content);
		
		dao.update(About_usVO);
		
		return About_usVO;
		}
	
	public void deleteAbout_us(String about_us_id) {
		dao.delete(about_us_id);
	}
	
	public About_usVO getOneAbout_us(String about_us_id) {
		return dao.findByPrimaryKey(about_us_id);
	}

	public List<About_usVO> getAll() {
		return dao.getAll();
	}
}	
