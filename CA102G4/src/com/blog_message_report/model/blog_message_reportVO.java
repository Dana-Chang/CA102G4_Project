package com.blog_message_report.model;

import java.sql.Timestamp;
 
public class blog_message_reportVO implements java.io.Serializable {
	private String mem_id;
	private String message_id;
	private String bmr_reason;
	private Integer bmr_status;
	private Timestamp bmr_time;

	public blog_message_reportVO() {
	
	}

	public String getBmr_reason() {
		return bmr_reason;
	}

	public void setBmr_reason(String bmr_reason) {
		this.bmr_reason = bmr_reason;
	}

	public Timestamp getBmr_time() {
		return bmr_time;
	}

	public void setBmr_time(Timestamp bmr_time) {
		this.bmr_time = bmr_time;
	}

	public String getMem_id() {
		return mem_id;
	}

	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public Integer getBmr_status() {
		return bmr_status;
	}

	public void setBmr_status(Integer bmr_status) {
		this.bmr_status = bmr_status;
	}

}
