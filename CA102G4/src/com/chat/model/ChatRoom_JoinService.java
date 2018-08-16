package com.chat.model;

import java.util.List;

public class ChatRoom_JoinService {
	
	private ChatRoom_JoinDAO_Interface dao ;
	
	public ChatRoom_JoinService() {
		dao = new ChatRoom_JoinDAO();
	}
	
	//新增用戶到聊天對話
	public int addMemInCR(String chatRoom_id,String join_Memid) {
		ChatRoom_JoinVO crj = new ChatRoom_JoinVO();
		crj.setChatRoom_ID(chatRoom_id);
		crj.setJoin_MemID(join_Memid);
		
		return dao.addMemInCR(crj);
	}
	
	//將用戶退出聊天對話
	public int delMemOutCR(String chatRoom_id,String join_Memid) {
		ChatRoom_JoinVO crj = new ChatRoom_JoinVO();
		crj.setChatRoom_ID(chatRoom_id);
		crj.setJoin_MemID(join_Memid);
		return dao.delMemOutCR(crj);
	}
	
	//查詢我所有參與的聊天室
	public List<ChatRoom_JoinVO> getMyChatRoom(String memid){
		return dao.findMyChatRoom(memid);
	}
	
	//查詢某聊天對話，所有參與的人員
	public List<ChatRoom_JoinVO> getJoinMem_ByChatRoom(String chatRoom_id){
		return dao.getJoinMem_ByChatRoom(chatRoom_id);
	}

}
