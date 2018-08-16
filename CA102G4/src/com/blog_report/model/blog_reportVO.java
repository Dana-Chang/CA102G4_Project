package com.blog_report.model;

import java.sql.Timestamp;

public class blog_reportVO implements java.io.Serializable {
	private String blog_id;
	private String mem_id;
	private String br_reason;
	private Timestamp br_time;
	private Integer br_status;
	
	public blog_reportVO() {
	
	}

	public String getBr_reason() {
		return br_reason;
	}

	public void setBr_reason(String br_reason) {
		this.br_reason = br_reason;
	}

	public Timestamp getBr_time() {
		return br_time;
	}

	public void setBr_time(Timestamp br_time) {
		this.br_time = br_time;
	}

	public void setBlog_id(String blog_id) {
		this.blog_id = blog_id;
	}

	public String getBlog_id() {
		return blog_id;
	}

	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

	public String getMem_id() {
		return mem_id;
	}

	public Integer getBr_status() {
		return br_status;
	}

	public void setBr_status(Integer br_status) {
		this.br_status = br_status;
	}

}
