package com.qa_list.model;

import java.io.Serializable;

public class Qa_listVO implements Serializable{
	private	String list_id;
	private	String list_name;
	
	public String getList_id() {
		return list_id;
	}
	public void setList_id(String list_id) {
		this.list_id = list_id;
	}
	public String getList_name() {
		return list_name;
	}
	public void setList_name(String list_name) {
		this.list_name = list_name;
	}
	
	
	
}
