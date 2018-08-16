package com.notice.model;

import java.io.Serializable;

public class NoticeVO implements Serializable{
	private	String notice_id;
	private	String mem_idfrom;
	private	String mem_idto;
	private String notice_title;
	private String notice_con;
	private Integer notice_state;
	
	public String getNotice_id() {
		return notice_id;
	}
	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}
	public String getMem_idfrom() {
		return mem_idfrom;
	}
	public void setMem_idfrom(String mem_idfrom) {
		this.mem_idfrom = mem_idfrom;
	}
	public String getMem_idto() {
		return mem_idto;
	}
	public void setMem_idto(String mem_idto) {
		this.mem_idto = mem_idto;
	}
	public String getNotice_title() {
		return notice_title;
	}
	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}
	public String getNotice_con() {
		return notice_con;
	}
	public void setNotice_con(String notice_con) {
		this.notice_con = notice_con;
	}
	public Integer getNotice_state() {
		return notice_state;
	}
	public void setNotice_state(Integer notice_state) {
		this.notice_state = notice_state;
	}

}
