package com.qa_collect.model;

import java.io.Serializable;

public class Qa_collectVO implements Serializable{
	private	String question_id; 
	private	String mem_id;
	
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

}
