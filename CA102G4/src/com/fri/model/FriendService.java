package com.fri.model;

import java.sql.Date;
import java.util.List;

public class FriendService {
	private FriendDAO_interface  dao;
	
	
	public FriendService(){
		dao = new FriendDAO();
	}
	
	//新增好友邀請
	public Friend insertFri(String memID_self,String memID_Fri,Integer Stat) {
		Friend fri = new Friend();
		fri.setMemID_Self(memID_self);
		fri.setMemID_Fri(memID_Fri);
		fri.setFri_Stat(Stat);
		
		dao.addFri(fri);
		
		return fri;//回傳的時候，不是從資料庫拿
	}
	
	//更新好友狀態(成為好友設定狀態及設定成為好友時間)
	public Friend updateFriStat(String memID_self,String memID_Fri) {
		Friend fri = new Friend();
		fri.setMemID_Self(memID_self);
		fri.setMemID_Fri(memID_Fri);
		
		dao.updateFriStat(fri);
		
		return fri;
	}
	
	//更新好友狀態(封鎖或解除封鎖時)
	public Friend updateFriStat_Block(String memID_self,String memID_Fri,Integer stat) {
		Friend fri = new Friend();
		fri.setMemID_Self(memID_self);
		fri.setMemID_Fri(memID_Fri);
		fri.setFri_Stat(stat);

		dao.updateFriBlock(fri);
		
		return fri ;
	}
	
	//刪除好友時(處理交易問題)
	public void deleteFri(String memID_self,String memID_Fri) {
		dao.deleteFri(memID_self, memID_Fri);
	}
	
	//拒絕他人好友邀請
	public void rejectFri(String memID_self,String memID_Fri) {
		dao.rejectFri(memID_self, memID_Fri);
	}
	
	//查詢好友名單搭配好友狀態(封鎖或為好友關係)
	public List<Friend> findMyFri(String memID_self,Integer status){
		return dao.findMyFri(memID_self, status);
	}
	
	//查詢好友名單(最近新增)
	public List<Friend> findMyNewFri(String memID_self){
		return dao.findMyNewFri(memID_self);
	}
	
	//查詢好友名單(當月壽星)
	public List<Friend> findMyBirFri(String memID_self){
		return dao.findMyBirFri(memID_self);
	};
	
	//查詢好友之間的關係(一筆)
	public Friend findRelationship(String memID_self,String memID_Fri) {
		return dao.findRelation(memID_self, memID_Fri);
	}

	//當接受別人的好友邀請時(處理交易問題)
	public void becomeFri(String memID_self,String memID_Fri) {
		dao.becomeFri(memID_self, memID_Fri);
	}
}
