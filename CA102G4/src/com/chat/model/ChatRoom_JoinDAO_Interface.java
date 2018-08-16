package com.chat.model;

import java.util.*;

public interface ChatRoom_JoinDAO_Interface {
	
	//新增用戶到聊天對話
	int addMemInCR(ChatRoom_JoinVO crj);
	
	//將用戶退出聊天對話
	int delMemOutCR(ChatRoom_JoinVO crj);
	
	//查詢我所有參與的聊天室
	List<ChatRoom_JoinVO> findMyChatRoom(String memID);
	
	//查詢某對話聊天，所有參與的人員
	List<ChatRoom_JoinVO> getJoinMem_ByChatRoom(String chatRoom_id);
}
