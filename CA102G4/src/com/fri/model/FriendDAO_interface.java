package com.fri.model;

import java.util.List;

public interface FriendDAO_interface {
	
	//新增好友邀請
	int addFri(Friend friend);
	
	//更新好友狀態(成為好友設定狀態及設定成為好友時間)
	int updateFriStat(Friend friend);
	
	//更新好友狀態(封鎖或解除封鎖時)
	int updateFriBlock(Friend friend);
	
	//刪除好友(處理交易問題)
	int deleteFri(String memID_self,String memID_Fri);
	
	//拒絕他人好友邀請
	int rejectFri(String memID_self,String memID_Fri);
	
	//接受他人好友邀請時(處理交易問題)
	int becomeFri(String memID_self,String memID_Fri);
	
	//查詢好友名單搭配好友狀態(封鎖或為好友關係)
	List<Friend> findMyFri(String memID_self,Integer status);
	
	//查詢好友名單(最近新增)
	List<Friend> findMyNewFri(String memID_self);
	
	//查詢好友名單(當月壽星)
	List<Friend> findMyBirFri(String memID_self);
	
	//查詢好友之間的關係(一筆)
	Friend findRelation(String memID_self,String memID_Fri);
	
}
