package com.photo_tag.model;

import java.util.List;

public interface Photo_tagDAO_interface {
	
	//透過標籤編號找標籤內容
	public Photo_tagVO findByPrimaryKey(String photo_Tag_No);
	
	//找照片標籤內容(地點)
	public List<Photo_tagVO> get_Keyword(String tag_Content);
	
	public void insert(Photo_tagVO photo_tagVO);
	public void update(Photo_tagVO photo_tagVO);
	public void delete(String photo_Tag_No);
	public List<Photo_tagVO> getAll() ;
}
