package com.ad.model;

import java.sql.Timestamp;
import java.util.List;

public class AdService {
	private AdDAO_interface dao ;
	
	public AdService(){
		dao=new AdDAO();
	}
	
	//新增廣告
	public AdVO addAD(String title,String text,String link,byte[] pic,Timestamp ad_PreAddTime,Timestamp ad_PreOffTime) {
		AdVO advo=new AdVO();

		advo.setAd_Title(title);
		advo.setAd_Text(text);
		advo.setAd_Link(link);
		advo.setAd_Pic(pic);
		advo.setAd_PreAddTime(ad_PreAddTime);
		advo.setAd_PreOffTime(ad_PreOffTime);
		dao.addAD(advo);

		return advo; 
	}
	
	//修改廣告(必須是下架狀態)
	public AdVO updateAD(String id,String title,String text,String link,byte[] pic,Timestamp ad_PreAddTime,Timestamp ad_PreOffTime) {
		AdVO advo = new AdVO();
		advo.setAd_ID(id);
		advo.setAd_Title(title);
		advo.setAd_Text(text);
		advo.setAd_Link(link);
		advo.setAd_Pic(pic);
		advo.setAd_PreAddTime(ad_PreAddTime);
		advo.setAd_PreOffTime(ad_PreOffTime);

		dao.updateAD(advo);
		
		return advo;
	}
	
	//修改廣告狀態(上下架更新實際上下架時間)
	public void updateAD(String id,Integer stat,AdVO advo) {
		dao.updateAD(id, stat,advo);
	}
	
	//修改廣告點擊率
	public void updateAD_click(String id) {
		dao.updateAD_Click(id);
	}
	
	//刪除廣告
	public void delectAD(String id) {
		dao.delectAD(id);
	}
	
	//取得所有廣告(以熱門排序)
	public List<AdVO> getHotAD(){
		return dao.findHotAD();
	}
	
	//取得所有廣告(以最新排序)
	public List<AdVO> getNewAD(){
		return dao.findNewAD();
	}
	
	//取得所有廣告
	public List<AdVO> getAllAD(){
		return dao.findAllAD();
	}
	
	public AdVO getOne_ById(String id) {
		return dao.findByIdAD(id);
	}
}
