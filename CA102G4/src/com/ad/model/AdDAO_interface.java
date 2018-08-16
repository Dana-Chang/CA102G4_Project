package com.ad.model;

import java.util.List;

public interface AdDAO_interface {
	
	//新增廣告(後台)
	int addAD(AdVO ad);
	
	//修改廣告資訊(後台)
	int updateAD(AdVO ad);
	
	//修改廣告狀態(上架/下架也會更新實際上下架時間)
	int updateAD(String id,Integer stat,AdVO advo);
	
	//修改廣告點擊率
	int updateAD_Click(String id);
	
	//刪除廣告(後台)
	int delectAD(String id);
	
	//查詢已上架熱門廣告
	List<AdVO> findHotAD();
	
	//查詢已上架的最新廣告
	List<AdVO> findNewAD();
	
	//查詢所有廣告(後台)
	List<AdVO> findAllAD();
	
	//查詢某一支廣告(後台)
	AdVO findByIdAD(String id);

}
