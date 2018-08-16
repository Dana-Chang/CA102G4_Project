package com.photo_wall.model;

import java.util.List;

public interface Photo_wallDAO_interface {
	//取得照片牆所有資訊
	public List<Photo_wallVO> getAll() ;
	
	//個人照片牆頁面
	public Photo_wallVO findByPrimaryKey(String photo_No);
	
	//更改照片牆狀態
	int update_State(String mem_Id,String photo_No,Integer mem_State);
	
	//搜尋照片牆
//	public List<Photo_wallVO> findBy ;
	
	//以下都沒用到
	public void insert(Photo_wallVO photo_wallVO);
	public void update(Photo_wallVO photo_wallVO);
	public void delete(String photo_No);

	//讀取特定會員的照片牆*****
		List getAll_ByMemID(String mem_Id);
}
