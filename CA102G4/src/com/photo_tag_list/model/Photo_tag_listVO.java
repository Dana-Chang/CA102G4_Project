package com.photo_tag_list.model;

public class Photo_tag_listVO implements java.io.Serializable {
	private String photo_No;
	private String photo_Tag_No;
	
	public Photo_tag_listVO() {
			
		}
	
	public String getPhoto_Tag_No() {
		return photo_Tag_No;
	}

	public void setPhoto_Tag_No(String photo_Tag_No) {
		this.photo_Tag_No = photo_Tag_No;
	} 

	public String getPhoto_No() {
		return photo_No;
	}

	public void setPhoto_No(String photo_No) {
		this.photo_No = photo_No;
	}

	
	
}
