package com.qa_reply.model;

import java.io.Serializable;
import java.sql.Date;

public class Qa_replyVO implements Serializable{
	private	String reply_id;
	private	String question_id;
	private	String mem_id;
	private	String reply_content;
	private	Date reply_date;
	private Integer r_state;
	
	public String getReply_id() {
		return reply_id;
	}
	public void setReply_id(String reply_id) {
		this.reply_id = reply_id;
	}
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getReply_content() {
		return reply_content;
	}
	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}
	public Date getReply_date() {
		return reply_date;
	}
	public void setReply_date(Date reply_date) {
		this.reply_date = reply_date;
	}
	public Integer getR_state() {
		return r_state;
	}
	public void setR_state(Integer r_state) {
		this.r_state = r_state;
	}
	
	
	
}
