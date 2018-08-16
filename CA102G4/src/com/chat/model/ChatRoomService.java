package com.chat.model;

import java.util.List;

public class ChatRoomService {
	private ChatRoomDAO_Interface dao;
	
	public ChatRoomService() {
		dao = new ChatRoomDAO();
	}
	
	
	//新增聊天對話+參加名單
	public String addChatRoom(String chatRoom_Name,String chatRec,String[] addPeople,String loginMemid,Integer initCnt) {
		ChatRoomVO cr = new ChatRoomVO();

		cr.setChatRoom_Name(chatRoom_Name);
		cr.setChatRec(chatRec);
		cr.setChatRoom_InitCNT(initCnt);
		String pk = dao.addChatRoom(cr,addPeople,loginMemid); //拿回自增主建

		return pk;
	}
	
	//更新某個聊天對話
	public ChatRoomVO updateChatRoom(String chatRoom_id,String chatRoom_Name,String chatRec) {
		ChatRoomVO cr = new ChatRoomVO();
		
		cr.setChatRoom_ID(chatRoom_id);
		cr.setChatRoom_Name(chatRoom_Name);
		cr.setChatRec(chatRec);
		
		dao.updateChatRoom(cr);
		
		return cr ;
	}
	
	//刪除某個聊天對話
	public int delChatRoom(String chatRoom_id) {
		return dao.delChatRoom(chatRoom_id);
	}
	
	//查詢所有聊天對話
	public List<ChatRoomVO> getAllChatRoom(){
		return dao.getAllChatRoom();
	}
	 
	//查詢某個聊天對話
	public ChatRoomVO getOne_ByChatRoomID(String chatRoom_id) {
		return dao.getOne_ByChatRoomID(chatRoom_id);
	}
	
	
}
