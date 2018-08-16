package com.trip.model;

import java.io.Serializable;
import java.sql.Date;

public class TripVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String trip_no;
	private String mem_id;
	private String trip_name;
	private Date trip_startDay;
	private Integer trip_days;
	private Integer trip_views;
	private Integer trip_status;
	
	public TripVO(){}

	public TripVO(String trip_no, String mem_id, String trip_name, Date trip_startDay, Integer trip_days,
			Integer trip_views, Integer trip_status) {
		super();
		this.trip_no = trip_no;
		this.mem_id = mem_id;
		this.trip_name = trip_name;
		this.trip_startDay = trip_startDay;
		this.trip_days = trip_days;
		this.trip_views = trip_views;
		this.trip_status = trip_status;
	}

	public String getTrip_no() {
		return trip_no;
	}

	public void setTrip_no(String trip_no) {
		this.trip_no = trip_no;
	}

	public String getMem_id() {
		return mem_id;
	}

	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

	public String getTrip_name() {
		return trip_name;
	}

	public void setTrip_name(String trip_name) {
		this.trip_name = trip_name;
	}

	public Date getTrip_startDay() {
		return trip_startDay;
	}

	public void setTrip_startDay(Date trip_startDay) {
		this.trip_startDay = trip_startDay;
	}

	public Integer getTrip_days() {
		return trip_days;
	}

	public void setTrip_days(Integer trip_days) {
		this.trip_days = trip_days;
	}

	public Integer getTrip_views() {
		return trip_views;
	}

	public void setTrip_views(Integer trip_views) {
		this.trip_views = trip_views;
	}

	public Integer getTrip_status() {
		return trip_status;
	}

	public void setTrip_status(Integer trip_status) {
		this.trip_status = trip_status;
	}

	@Override
	public String toString() {
		return "行程編號："+trip_no+"\n"
				+"會員編號："+mem_id+"\n"
				+"行程名稱："+trip_name+"\n"
				+"行程起始日期："+trip_startDay+"\n"
				+"行程天數："+trip_days+"\n"
				+"瀏覽次數："+trip_views+"\n"
				+"行程狀態："+trip_status+"\n";
	}
}
