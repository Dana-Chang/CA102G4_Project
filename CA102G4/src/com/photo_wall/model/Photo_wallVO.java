package com.photo_wall.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Photo_wallVO implements java.io.Serializable{
	private String photo_No;
	private String mem_Id; 
	private Timestamp po_Time; 
	private byte[] photo;
	private Integer photo_Sta;
	private String photo_Content;
	private String encoded;


	public String getPhoto_Content() {
		return photo_Content;
	}

	public void setPhoto_Content(String photo_Content) {
		this.photo_Content = photo_Content;
	}

	
	public Photo_wallVO() {
		
	}

	public String getPhoto_No() {
		return photo_No;
	}

	public void setPhoto_No(String photo_No) {
		this.photo_No = photo_No;
	}

	public String getMem_Id() {
		return mem_Id;
	}

	public void setMem_Id(String mem_Id) {
		this.mem_Id = mem_Id;
	}

	public Timestamp getPo_Time() {
		return po_Time;
	}

	public void setPo_Time(Timestamp po_Time) {
		this.po_Time = po_Time;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public Integer getPhoto_Sta() {
		return photo_Sta;
	}

	public void setPhoto_Sta(Integer photo_Sta) {
		this.photo_Sta = photo_Sta;
	}

	public String getEncoded() {
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}

	
	
	
}
