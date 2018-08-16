package com.admin.model;

public class AdminVO implements java.io.Serializable{
	private String admin_Id;
	private String admin_Account;
	private String admin_Password;
	private String admin_Name;
	private String admin_Mail;
	private String admin_Phone;
	
	public AdminVO() {
		
	}

	public String getAdmin_Id() {
		return admin_Id;
	}

	public void setAdmin_Id(String admin_Id) {
		this.admin_Id = admin_Id;
	}

	public String getAdmin_Account() {
		return admin_Account;
	}

	public void setAdmin_Account(String admin_Account) {
		this.admin_Account = admin_Account;
	}

	public String getAdmin_Password() {
		return admin_Password;
	}

	public void setAdmin_Password(String admin_Password) {
		this.admin_Password = admin_Password;
	}

	public String getAdmin_Name() {
		return admin_Name;
	}

	public void setAdmin_Name(String admin_Name) {
		this.admin_Name = admin_Name;
	}

	public String getAdmin_Mail() {
		return admin_Mail;
	}

	public void setAdmin_Mail(String admin_Mail) {
		this.admin_Mail = admin_Mail;
	}

	public String getAdmin_Phone() {
		return admin_Phone;
	}

	public void setAdmin_Phone(String admin_Phone) {
		this.admin_Phone = admin_Phone;
	}
	
}
