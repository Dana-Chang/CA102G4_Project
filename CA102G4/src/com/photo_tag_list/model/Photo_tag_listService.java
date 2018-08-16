package com.photo_tag_list.model;

import java.util.List;

public class Photo_tag_listService {
	
private Photo_tag_listDAO_interface dao;
	
	public Photo_tag_listService() {
		dao = new Photo_tag_listDAO();
	}
	
	//用照片標籤編號找照片(編號)
	
	public List<Photo_tag_listVO> getAll_Photo_No(String photo_No){
		return dao.getAll_Photo_No(photo_No);
	}
	
	public Photo_tag_listVO findByPrimaryKey(String photo_Tag_No,String photo_No){
		return dao.findByPrimaryKey(photo_Tag_No,photo_No);
	}
	
}
