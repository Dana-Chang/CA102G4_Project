package com.about_us.model;

import java.io.Serializable;

public class About_usVO implements Serializable{
	
	private	String	about_us_id;
	private	String	about_content;
	
	public String getAbout_us_id() {
		return about_us_id;
	}
	public void setAbout_us_id(String about_us_id) {
		this.about_us_id = about_us_id;
	}
	public String getAbout_content() {
		return about_content;
	}
	public void setAbout_content(String about_content) {
		this.about_content = about_content;
	}
	
}
