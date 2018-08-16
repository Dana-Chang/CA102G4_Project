package com.rp_report.model;

import java.io.Serializable;

public class Rp_reportVO implements Serializable{
	private	String	reply_id;
	private	String	mem_id;
	private	Integer	rp_state;
	
	
	public String getReply_id() {
		return reply_id;
	}
	public void setReply_id(String reply_id) {
		this.reply_id = reply_id;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public Integer getRp_state() {
		return rp_state;
	}
	public void setRp_state(Integer rp_state) {
		this.rp_state = rp_state;
	}
	
}
