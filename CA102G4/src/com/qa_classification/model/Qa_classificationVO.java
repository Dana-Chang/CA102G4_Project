package com.qa_classification.model;

import java.io.Serializable;

public class Qa_classificationVO implements Serializable{
	private	String	question_id;
	private	String	list_id;
	
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getList_id() {
		return list_id;
	}
	public void setList_id(String list_id) {
		this.list_id = list_id;
	}
	
	
	
}
