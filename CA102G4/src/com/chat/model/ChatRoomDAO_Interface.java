package com.chat.model;

import java.util.List;

public interface ChatRoomDAO_Interface {
	//新增聊天對話
	String addChatRoom(ChatRoomVO cr,String[] addPeople,String loginMemId);
	//修改聊天對話(名子或聊天紀錄)
	int updateChatRoom(ChatRoomVO cr);
	//刪除聊天對話(當最後一個人離開聊天對話時)
	int delChatRoom(String chatRoom_id);
	//所有聊天對話
	List<ChatRoomVO> getAllChatRoom();
	
	//查詢某個聊天對話
	ChatRoomVO getOne_ByChatRoomID(String chatRoom_id);
}
