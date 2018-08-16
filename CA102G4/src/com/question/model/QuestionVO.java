package com.question.model;

import java.io.Serializable;
import java.sql.Date;

public class QuestionVO implements Serializable{	
	private	String	question_id;					
	private	String	mem_id;
	private	String	question_content;
	private	Date	build_date;
	private Integer q_state;
 	
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
	public String getQuestion_content() {
		return question_content;
	}
	public void setQuestion_content(String question_content) {
		this.question_content = question_content;
	}
	public Date getBuild_date() {
		return build_date;
	}
	public void setBuild_date(Date build_date) {
		this.build_date = build_date;
	}
	public Integer getQ_state() {
		return q_state;
	}
	public void setQ_state(Integer q_state) {
		this.q_state = q_state;
	}
	
	
}
