package com.fri.model;

import java.io.Serializable;
import java.sql.Date;

//需實作Serializable
public class Friend implements Serializable{
	
	private String fri_ID;
	private String memID_Self;
	private String memID_Fri;
	private Date fri_Time;
	private Integer fri_Stat;
	
	//不帶參數的建構子
	public Friend(){}

	//針對每一個private欄位設計一組getter-setter
	public String getFri_ID() {
		return fri_ID;
	}

	public void setFri_ID(String fri_ID) {
		this.fri_ID = fri_ID;
	}

	public String getMemID_Self() {
		return memID_Self;
	}

	public void setMemID_Self(String memID_Self) {
		this.memID_Self = memID_Self;
	}

	public String getMemID_Fri() {
		return memID_Fri;
	}

	public void setMemID_Fri(String memID_Fri) {
		this.memID_Fri = memID_Fri;
	}

	public Date getFri_Time() {
		return fri_Time;
	}

	public void setFri_Time(Date fri_Time) {
		this.fri_Time = fri_Time;
	}

	public Integer getFri_Stat() {
		return fri_Stat;
	}

	public void setFri_Stat(Integer fri_Stat) {
		this.fri_Stat = fri_Stat;
	}
	
	
	
	

}
