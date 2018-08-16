package com.faq.model;

import java.io.Serializable;

public class FaqVO implements Serializable{
	private	String	faq_id;
	private	String	faq_qa;
	private	String	faq_content;
	
	public String getFaq_id() {
		return faq_id;
	}
	public void setFaq_id(String faq_id) {
		this.faq_id = faq_id;
	}
	public String getFaq_qa() {
		return faq_qa;
	}
	public void setFaq_qa(String faq_qa) {
		this.faq_qa = faq_qa;
	}
	public String getFaq_content() {
		return faq_content;
	}
	public void setFaq_content(String faq_content) {
		this.faq_content = faq_content;
	}
	
	
		
	
}
