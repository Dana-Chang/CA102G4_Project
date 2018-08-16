package com.news.model;

import java.io.Serializable;
import java.sql.Date;

public class NewsVO implements Serializable{
	private	String	news_id;					
	private	String	news_name;
	private	Date	news_date;
	private	String	news_con;
	
	public String getNews_id() {
		return news_id;
	}
	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}
	public String getNews_name() {
		return news_name;
	}
	public void setNews_name(String news_name) {
		this.news_name = news_name;
	}
	public Date getNews_date() {
		return news_date;
	}
	public void setNews_date(Date news_date) {
		this.news_date = news_date;
	}
	public String getNews_con() {
		return news_con;
	}
	public void setNews_con(String news_con) {
		this.news_con = news_con;
	}
	
	
	
	
}
