package com.attEdit.model;

import java.io.Serializable;
import java.sql.*;


public class AttractionsEditVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String attEdit_no;
	private String mem_id;
	private Timestamp attEdit_date;
	private String att_no;
	private String att_name;
	private Double att_lat;
	private Double att_lon;
	private String country;
	private String administrative_area;
	private String att_information;
	private byte[] att_picture;
	private String att_address;

	public AttractionsEditVO() {
	}

	public AttractionsEditVO(String attEdit_no, String mem_id, Timestamp attEdit_date, String att_no, String att_name,
			Double att_lat, Double att_lon, String country, String administrative_area, String att_information,
			byte[] att_picture, String att_address) {
		super();
		this.attEdit_no = attEdit_no;
		this.mem_id = mem_id;
		this.attEdit_date = attEdit_date;
		this.att_no = att_no;
		this.att_name = att_name;
		this.att_lat = att_lat;
		this.att_lon = att_lon;
		this.country = country;
		this.administrative_area = administrative_area;
		this.att_information = att_information;
		this.att_picture = att_picture;
		this.att_address = att_address;
	}

	public String getAdministrative_area() {
		return administrative_area;
	}

	public void setAdministrative_area(String administrative_area) {
		this.administrative_area = administrative_area;
	}

	public String getAttEdit_no() {
		return attEdit_no;
	}

	public void setAttEdit_no(String attEdit_no) {
		this.attEdit_no = attEdit_no;
	}

	public String getMem_id() {
		return mem_id;
	}

	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

	public Timestamp getAttEdit_date() {
		return attEdit_date;
	}

	public void setAttEdit_date(Timestamp attEdit_date) {
		this.attEdit_date = attEdit_date;
	}

	public String getAtt_no() {
		return att_no;
	}

	public void setAtt_no(String att_no) {
		this.att_no = att_no;
	}

	public String getAtt_name() {
		return att_name;
	}

	public void setAtt_name(String att_name) {
		this.att_name = att_name;
	}

	public Double getAtt_lat() {
		return att_lat;
	}

	public void setAtt_lat(Double att_lat) {
		this.att_lat = att_lat;
	}

	public Double getAtt_lon() {
		return att_lon;
	}

	public void setAtt_lon(Double att_lon) {
		this.att_lon = att_lon;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAtt_information() {
		return att_information;
	}

	public void setAtt_information(String att_information) {
		this.att_information = att_information;
	}

	public byte[] getAtt_picture() {
		return att_picture;
	}

	public void setAtt_picture(byte[] att_picture) {
		this.att_picture = att_picture;
	}

	public String getAtt_address() {
		return att_address;
	}

	public void setAtt_address(String att_address) {
		this.att_address = att_address;
	}

	@Override
	public String toString() {
		return "景點共同編輯編號："+attEdit_no+"\n"
				+"編輯會員編號"+mem_id+"\n"
				+"編輯時間"+attEdit_date+"\n"
				+"景點編號："+att_no+"\n"
				+"景點名稱："+att_name+"\n"
				+"景點緯度："+att_lat+"\n"
				+"景點經度："+att_lon+"\n"
				+"所屬國家："+country+"\n"
				+"行政劃分："+administrative_area+"\n"
				+"景點資訊："+att_information+"\n"
				+"景點地址："+att_address+"\n";
	}
}
