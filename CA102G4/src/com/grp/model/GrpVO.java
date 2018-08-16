package com.grp.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

public class GrpVO implements java.io.Serializable{
	private String grp_Id;
	private String mem_Id;
	private Timestamp grp_Start;
	private Timestamp grp_End;
	private Integer grp_Cnt;
	private Integer grp_Acpt;
	private String trip_No;
	private Timestamp trip_Start;
	private Timestamp trip_End;
	private String trip_Locale;
	private String trip_Details;
	private byte[] grp_Photo;
	private Integer grp_Status;
	private String chatroom_Id;
	private String grp_Title;
	private String grp_Price;
	
	
	public GrpVO() {
		
	}

	public String getGrp_Id() {
		return grp_Id;
	}

	public void setGrp_Id(String grp_Id) {
		this.grp_Id = grp_Id;
	}

	public String getMem_Id() {
		return mem_Id;
	}

	public void setMem_Id(String mem_Id) {
		this.mem_Id = mem_Id;
	}

	public Timestamp getGrp_Start() {
		return grp_Start;
	}

	public void setGrp_Start(Timestamp grp_Start) {
		this.grp_Start = grp_Start;
	}

	public Timestamp getGrp_End() {
		return grp_End;
	}

	public void setGrp_End(Timestamp grp_End) {
		this.grp_End = grp_End;
	}

	public Integer getGrp_Cnt() {
		return grp_Cnt;
	}

	public void setGrp_Cnt(Integer grp_Cnt) {
		this.grp_Cnt = grp_Cnt;
	}

	public Integer getGrp_Acpt() {
		return grp_Acpt;
	}

	public void setGrp_Acpt(Integer grp_Acpt) {
		this.grp_Acpt = grp_Acpt;
	}

	public String getTrip_No() {
		return trip_No;
	}

	public void setTrip_No(String trip_No) {
		this.trip_No = trip_No;
	}

	public Timestamp getTrip_Start() {
		return trip_Start;
	}

	public void setTrip_Start(Timestamp trip_Start) {
		this.trip_Start = trip_Start;
	}

	public Timestamp getTrip_End() {
		return trip_End;
	}

	public void setTrip_End(Timestamp trip_End) {
		this.trip_End = trip_End;
	}

	public String getTrip_Locale() {
		return trip_Locale;
	}

	public void setTrip_Locale(String trip_Locale) {
		this.trip_Locale = trip_Locale;
	}

	public String getTrip_Details() {
		return trip_Details;
	}

	public void setTrip_Details(String trip_Details) {
		this.trip_Details = trip_Details;
	}

	public byte[] getGrp_Photo() {
		return grp_Photo;
	}

	public void setGrp_Photo(byte[] grp_Photo) {
		this.grp_Photo = grp_Photo;
	}

	public Integer getGrp_Status() {
		return grp_Status;
	}

	public void setGrp_Status(Integer grp_Status) {
		this.grp_Status = grp_Status;
	}

	public String getChatroom_Id() {
		return chatroom_Id;
	}

	public void setChatroom_Id(String chatroom_Id) {
		this.chatroom_Id = chatroom_Id;
	}
	
	public String getGrp_Title() {
		return grp_Title;
	}

	public void setGrp_Title(String grp_Title) {
		this.grp_Title = grp_Title;
	}

	public String getGrp_Price() {
		return grp_Price;
	}

	public void setGrp_Price(String grp_Price) {
		this.grp_Price = grp_Price;
	}


	
	
}
