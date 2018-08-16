package com.ad.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class AdVO implements Serializable{
	private String ad_ID;
	private String ad_Title;
	private String ad_Text;
	private String ad_Link;
	private byte[] ad_Pic;
	private Timestamp ad_PreAddTime;
	private Timestamp ad_PreOffTime;
	private Timestamp ad_ActAddTime;
	private Timestamp ad_ActOffTime;
	private Integer ad_Stat;
	private Integer clickCount;
	
	public AdVO() {}

	public String getAd_ID() {
		return ad_ID;
	}

	public void setAd_ID(String ad_ID) {
		this.ad_ID = ad_ID;
	}

	public String getAd_Title() {
		return ad_Title;
	}

	public void setAd_Title(String ad_Title) {
		this.ad_Title = ad_Title;
	}

	public String getAd_Text() {
		return ad_Text;
	}

	public void setAd_Text(String ad_Text) {
		this.ad_Text = ad_Text;
	}

	public String getAd_Link() {
		return ad_Link;
	}

	public void setAd_Link(String ad_Link) {
		this.ad_Link = ad_Link;
	}

	public byte[] getAd_Pic() {
		return ad_Pic;
	}

	public void setAd_Pic(byte[] ad_Pic) {
		this.ad_Pic = ad_Pic;
	}

	public Timestamp getAd_PreAddTime() {
		return ad_PreAddTime;
	}

	public void setAd_PreAddTime(Timestamp ad_PreAddTime) {
		this.ad_PreAddTime = ad_PreAddTime;
	}

	public Timestamp getAd_PreOffTime() {
		return ad_PreOffTime;
	}

	public void setAd_PreOffTime(Timestamp ad_PreOffTime) {
		this.ad_PreOffTime = ad_PreOffTime;
	}

	public Timestamp getAd_ActAddTime() {
		return ad_ActAddTime;
	}

	public void setAd_ActAddTime(Timestamp ad_ActAddTime) {
		this.ad_ActAddTime = ad_ActAddTime;
	}

	public Timestamp getAd_ActOffTime() {
		return ad_ActOffTime;
	}

	public void setAd_ActOffTime(Timestamp ad_ActOffTime) {
		this.ad_ActOffTime = ad_ActOffTime;
	}

	public Integer getAd_Stat() {
		return ad_Stat;
	}

	public void setAd_Stat(Integer ad_Stat) {
		this.ad_Stat = ad_Stat;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}
	
	
	
	
}
