package com.chat.model;

import java.io.Serializable;

public class ChatRoomVO implements Serializable{
	private String chatRoom_ID;
	private String chatRoom_Name;
	private String chatRec;
	//存放原本聊天對話初始人數，判別是群組聊天還是個人
	private Integer chatRoom_InitCNT; 
	
	public Integer getChatRoom_InitCNT() {
		return chatRoom_InitCNT;
	}

	public void setChatRoom_InitCNT(Integer chatRoom_InitCNT) {
		this.chatRoom_InitCNT = chatRoom_InitCNT;
	}

	public ChatRoomVO() {}
	
	public void setChatRoom_ID(String chatRoom_ID) {
		this.chatRoom_ID=chatRoom_ID;
	}
	
	public String getChatRoom_ID() {
		return chatRoom_ID;
	}
	
	public String getChatRoom_Name() {
		return chatRoom_Name;
	}

	public void setChatRoom_Name(String chatRoom_Name) {
		this.chatRoom_Name = chatRoom_Name;
	}

	public String getChatRec() {
		return chatRec;
	}

	public void setChatRec(String chatRec) {
		this.chatRec = chatRec;
	}
	
	

}
