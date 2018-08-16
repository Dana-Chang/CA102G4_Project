package com.blog.model;

import java.sql.Date;
import java.sql.Timestamp;

public class blogVO implements java.io.Serializable {
	private String blog_id;
	private String mem_id;
	private Timestamp blog_date;
	private byte[] blog_coverimage;
	private String blog_title;
	private String blog_content;
	private Date travel_date;
	private String trip_no;
	private Integer blog_views;
	private Integer blog_status;

	public blogVO() {
	
	}

	public Integer getBlog_status() {
		return blog_status;
	}

	public void setBlog_status(Integer blog_status) {
		this.blog_status = blog_status;
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

	public Timestamp getBlog_date() {
		return blog_date;
	}

	public void setBlog_date(Timestamp timestamp) {
		this.blog_date = timestamp;
	}

	public byte[] getBlog_coverimage() {
		return blog_coverimage;
	}

	public void setBlog_coverimage(byte[] blog_coverimage) {
		this.blog_coverimage = blog_coverimage;
	}

	public String getBlog_title() {
		return blog_title;
	}

	public void setBlog_title(String blog_title) {
		this.blog_title = blog_title;
	}

	public String getBlog_content() {
		return blog_content;
	}

	public void setBlog_content(String blog_content) {
		this.blog_content = blog_content;
	}

	public Date getTravel_date() {
		return travel_date;
	}

	public void setTravel_date(Date travel_date) {
		this.travel_date = travel_date;
	}

	public String getTrip_no() {
		return trip_no;
	}

	public void setTrip_no(String trip_no) {
		this.trip_no = trip_no;
	}

	public Integer getBlog_views() {
		return blog_views;
	}

	public void setBlog_views(Integer blog_views) {
		this.blog_views = blog_views;
	}

}
