package com.blog_tag.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class blog_tagVO implements java.io.Serializable {
	private String blog_id;
	private String btn_id;

	public blog_tagVO() {

	}

	public blog_tagVO(String blog_id, String btn_id) {
		super();
		this.blog_id = blog_id;
		this.btn_id = btn_id;
	}

	public void setBlog_id(String blog_id) {
		this.blog_id = blog_id;
	}

	public String getBlog_id() {
		return blog_id;
	}

	public void setBtn_id(String btn_id) {
		this.btn_id = btn_id;
	}

	public String getBtn_id() {
		return btn_id;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj != null && getClass() == obj.getClass()) {
			if (obj instanceof blog_tagVO) {
				blog_tagVO e = (blog_tagVO) obj;
				if (this.blog_id.equals(e.blog_id))
					return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return this.blog_id.hashCode();
	}

}
