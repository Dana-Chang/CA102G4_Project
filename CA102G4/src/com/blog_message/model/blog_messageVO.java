package com.blog_message.model;

import java.sql.Timestamp;

public class blog_messageVO implements java.io.Serializable {
	private String message_id;
	private String blog_id;
	private String mem_id;
	private String blog_message;
	private Timestamp bm_time;
	private Integer bm_status;
	
	public blog_messageVO() {
	
	}

	public Timestamp getBm_time() {
		return bm_time;
	}

	public void setBm_time(Timestamp bm_time) {
		this.bm_time = bm_time;
	}

	public Integer getBm_status() {
		return bm_status;
	}

	public void setBm_status(Integer bm_status) {
		this.bm_status = bm_status;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public String getBlog_id() {
		return blog_id;
	}

	public void setBlog_id(String blog_id) {
		this.blog_id = blog_id;
	}

	public String getMem_id() {
		return mem_id;
	}

	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

	public String getBlog_message() {
		return blog_message;
	}

	public void setBlog_message(String blog_message) {
		this.blog_message = blog_message;
	}
	
}
