package com.photo_tag.model;

import java.sql.Timestamp;

public class photo_allVO implements java.io.Serializable{
	private String photo_No;
	private Timestamp po_Time; 
	private byte[] photo;
	private String photo_Content;
	private String photo_Tag_No;

	public String getPhoto_Tag_No() {
		return photo_Tag_No;
	}

	public void setPhoto_Tag_No(String photo_Tag_No) {
		this.photo_Tag_No = photo_Tag_No;
	}

	private String encoded;

	public photo_allVO() {
			
		}
	
	public String getPhoto_Content() {
		return photo_Content;
	}

	public void setPhoto_Content(String photo_Content) {
		this.photo_Content = photo_Content;
	}


	public String getPhoto_No() {
		return photo_No;
	}

	public void setPhoto_No(String photo_No) {
		this.photo_No = photo_No;
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



	public String getEncoded() {
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}
}
