package com.chat.model;

import java.io.Serializable;

public class ChatRoom_JoinVO implements Serializable{
	private String chatRoom_ID;
	private String join_MemID;
	
	public ChatRoom_JoinVO() {}
	
	public void setChatRoom_ID(String chatRoom_ID) {
		this.chatRoom_ID=chatRoom_ID;
	}
	
	public String getChatRoom_ID() {
		return chatRoom_ID;
	}

	public String getJoin_MemID() {
		return join_MemID;
	}

	public void setJoin_MemID(String join_MemID) {
		this.join_MemID = join_MemID;
	}

	
}
