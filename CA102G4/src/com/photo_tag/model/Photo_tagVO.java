package com.photo_tag.model;

public class Photo_tagVO implements java.io.Serializable{
	private String photo_Tag_No;
	private String photo_No;
	private String tag_Content;
	private byte[] photo;
	private String encoded;
	
	public String getPhoto_No() {
		return photo_No;
	}

	public void setPhoto_No(String photo_No) {
		this.photo_No = photo_No;
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

	
	public Photo_tagVO() {
		
	}

	public String getPhoto_Tag_No() {
		return photo_Tag_No;
	}

	public void setPhoto_Tag_No(String photo_Tag_No) {
		this.photo_Tag_No = photo_Tag_No;
	}

	public String getTag_Content() {
		return tag_Content;
	}

	public void setTag_Content(String tag_Content) {
		this.tag_Content = tag_Content;
	}

	
	
}
