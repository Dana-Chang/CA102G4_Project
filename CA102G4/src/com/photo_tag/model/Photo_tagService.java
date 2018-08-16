package com.photo_tag.model;

import java.util.List;

public class Photo_tagService {
	
private Photo_tagDAO_interface dao;
	
	public Photo_tagService() {
		dao = new Photo_tagDAO();
	}
	
	//用照片標籤編號找照片內容

	public Photo_tagVO findByPrimaryKey(String photo_Tag_No){
		return dao.findByPrimaryKey(photo_Tag_No);
	}
	
	//找照片標籤內容(地點)
	
	public List<Photo_tagVO> get_Keyword(String tag_Content){
		return dao.get_Keyword(tag_Content);

	}
	
	
	
	
	public List<Photo_tagVO> getAll() {
		return dao.getAll();
	}
}
