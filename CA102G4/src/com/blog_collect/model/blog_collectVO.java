package com.blog_collect.model;

public class blog_collectVO implements java.io.Serializable {
	private String mem_id;
	private String blog_id;

	public blog_collectVO() {

	}

	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

	public String getMem_id() {
		return mem_id;
	}

	public void setBlog_id(String blog_id) {
		this.blog_id = blog_id;
	}

	public String getBlog_id() {
		return blog_id;
	}
}
