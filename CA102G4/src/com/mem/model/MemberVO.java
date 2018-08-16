package com.mem.model;
import java.sql.Date;


public class MemberVO implements java.io.Serializable {
	private String mem_Id ;
	private String mem_Account;
	private String mem_Password;
	
	private String mem_Name;
	
	private Integer mem_Sex;
	
	private String mem_Address;
	
	private Date mem_Birthday;
	
	private String mem_Phone;
	private String mem_Profile;
	
	private byte[] mem_Photo;
	
	private Integer mem_State;
	
	private String mem_Activecode;
	
	

	private String Delivery_Address_1;
	private String Delivery_Address_2;
	private String Delivery_Address_3;
	
	private String STORE_ADDR_1;
	private String STORE_ADDR_2;
	private String STORE_ADDR_3;
	
	private String STORE_NAME_1;
	private String STORE_NAME_2;
	private String STORE_NAME_3;
	
	private Integer STORE_NO_1;
	private Integer STORE_NO_2;
	private Integer STORE_NO_3;
	
	private Date mem_Reg_Date;
	
	public Date getMem_Reg_Date() {
		return mem_Reg_Date;
	}

	public void setMem_Reg_Date(Date mem_Reg_Date) {
		this.mem_Reg_Date = mem_Reg_Date;
	}

	private String encoded; 
	
	public String getEncoded() {
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}

	public MemberVO(){}

	public String getMem_Id() {
		return mem_Id;
	}

	public void setMem_Id(String mem_Id) {
		this.mem_Id = mem_Id;
	}

	public String getMem_Account() {
		return mem_Account;
	}

	public void setMem_Account(String mem_Account) {
		this.mem_Account = mem_Account;
	}

	public String getMem_Password() {
		return mem_Password;
	}

	public void setMem_Password(String mem_Password) {
		this.mem_Password = mem_Password;
	}

	public String getMem_Name() {
		return mem_Name;
	}

	public void setMem_Name(String mem_Name) {
		this.mem_Name = mem_Name;
	}

	public Integer getMem_Sex() {
		return mem_Sex;
	}

	public void setMem_Sex(Integer mem_Sex) {
		this.mem_Sex = mem_Sex;
	}

	public String getMem_Address() {
		return mem_Address;
	}

	public void setMem_Address(String mem_Address) {
		this.mem_Address = mem_Address;
	}

	public Date getMem_Birthday() {
		return mem_Birthday;
	}

	public void setMem_Birthday(Date mem_Birthday) {
		this.mem_Birthday = mem_Birthday;
	}

	public String getMem_Phone() {
		return mem_Phone;
	}

	public void setMem_Phone(String mem_Phone) {
		this.mem_Phone = mem_Phone;
	}

	public String getMem_Profile() {
		return mem_Profile;
	}

	public void setMem_Profile(String mem_Profile) {
		this.mem_Profile = mem_Profile;
	}

	public byte[] getMem_Photo() {
		return mem_Photo;
	}

	public void setMem_Photo(byte[] mem_Photo) {
		this.mem_Photo = mem_Photo;
	}

	public Integer getMem_State() {
		return mem_State;
	}

	public void setMem_State(Integer mem_State) {
		this.mem_State = mem_State;
	}
	
	public String getMem_Activecode() {
		return mem_Activecode;
	}

	public void setMem_Activecode(String mem_Activecode) {
		this.mem_Activecode = mem_Activecode;
	}

	public String getDelivery_Address_1() {
		return Delivery_Address_1;
	}

	public void setDelivery_Address_1(String delivery_Address_1) {
		Delivery_Address_1 = delivery_Address_1;
	}

	public String getDelivery_Address_2() {
		return Delivery_Address_2;
	}

	public void setDelivery_Address_2(String delivery_Address_2) {
		Delivery_Address_2 = delivery_Address_2;
	}

	public String getDelivery_Address_3() {
		return Delivery_Address_3;
	}

	public void setDelivery_Address_3(String delivery_Address_3) {
		Delivery_Address_3 = delivery_Address_3;
	}

	public String getSTORE_ADDR_1() {
		return STORE_ADDR_1;
	}

	public void setSTORE_ADDR_1(String sTORE_ADDR_1) {
		STORE_ADDR_1 = sTORE_ADDR_1;
	}

	public String getSTORE_ADDR_2() {
		return STORE_ADDR_2;
	}

	public void setSTORE_ADDR_2(String sTORE_ADDR_2) {
		STORE_ADDR_2 = sTORE_ADDR_2;
	}

	public String getSTORE_ADDR_3() {
		return STORE_ADDR_3;
	}

	public void setSTORE_ADDR_3(String sTORE_ADDR_3) {
		STORE_ADDR_3 = sTORE_ADDR_3;
	}

	public String getSTORE_NAME_1() {
		return STORE_NAME_1;
	}

	public void setSTORE_NAME_1(String sTORE_NAME_1) {
		STORE_NAME_1 = sTORE_NAME_1;
	}

	public String getSTORE_NAME_2() {
		return STORE_NAME_2;
	}

	public void setSTORE_NAME_2(String sTORE_NAME_2) {
		STORE_NAME_2 = sTORE_NAME_2;
	}

	public String getSTORE_NAME_3() {
		return STORE_NAME_3;
	}

	public void setSTORE_NAME_3(String sTORE_NAME_3) {
		STORE_NAME_3 = sTORE_NAME_3;
	}

	public Integer getSTORE_NO_1() {
		return STORE_NO_1;
	}

	public void setSTORE_NO_1(Integer sTORE_NO_1) {
		STORE_NO_1 = sTORE_NO_1;
	}

	public Integer getSTORE_NO_2() {
		return STORE_NO_2;
	}

	public void setSTORE_NO_2(Integer sTORE_NO_2) {
		STORE_NO_2 = sTORE_NO_2;
	}

	public Integer getSTORE_NO_3() {
		return STORE_NO_3;
	}

	public void setSTORE_NO_3(Integer sTORE_NO_3) {
		STORE_NO_3 = sTORE_NO_3;
	}

	public int getIs_manager() {
		return 0;
	}
	
	
}

	

