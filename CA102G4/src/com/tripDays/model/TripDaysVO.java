package com.tripDays.model;

import java.io.Serializable;
import java.sql.Date;

public class TripDaysVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String tripDay_no;
	private String trip_no;
	private Integer tripDay_days;
	private Date tripDay_date;
	private String tripDay_hotelName;
	private Integer tripDay_hotelStart;
	private String tripDay_hotelNote;
	private Integer tripDay_hotelCost;
	
	public TripDaysVO(){}

	public TripDaysVO(String tripDay_no, String trip_no, Integer tripDay_days, Date tripDay_date,
			String tripDay_hotelName, Integer tripDay_hotelStart, String tripDay_hotelNote, Integer tripDay_hotelCost) {
		this.tripDay_no = tripDay_no;
		this.trip_no = trip_no;
		this.tripDay_days = tripDay_days;
		this.tripDay_date = tripDay_date;
		this.tripDay_hotelName = tripDay_hotelName;
		this.tripDay_hotelStart = tripDay_hotelStart;
		this.tripDay_hotelNote = tripDay_hotelNote;
		this.tripDay_hotelCost = tripDay_hotelCost;
	}

	public String getTripDay_no() {
		return tripDay_no;
	}

	public void setTripDay_no(String tripDay_no) {
		this.tripDay_no = tripDay_no;
	}

	public String getTrip_no() {
		return trip_no;
	}

	public void setTrip_no(String trip_no) {
		this.trip_no = trip_no;
	}

	public Integer getTripDay_days() {
		return tripDay_days;
	}

	public void setTripDay_days(Integer tripDay_days) {
		this.tripDay_days = tripDay_days;
	}

	public Date getTripDay_date() {
		return tripDay_date;
	}

	public void setTripDay_date(Date tripDay_date) {
		this.tripDay_date = tripDay_date;
	}

	public String getTripDay_hotelName() {
		return tripDay_hotelName;
	}

	public void setTripDay_hotelName(String tripDay_hotelName) {
		this.tripDay_hotelName = tripDay_hotelName;
	}

	public Integer getTripDay_hotelStart() {
		return tripDay_hotelStart;
	}

	public void setTripDay_hotelStart(Integer tripDay_hotelStart) {
		this.tripDay_hotelStart = tripDay_hotelStart;
	}

	public String getTripDay_hotelNote() {
		return tripDay_hotelNote;
	}

	public void setTripDay_hotelNote(String tripDay_hotelNote) {
		this.tripDay_hotelNote = tripDay_hotelNote;
	}

	public Integer getTripDay_hotelCost() {
		return tripDay_hotelCost;
	}

	public void setTripDay_hotelCost(Integer tripDay_hotelCost) {
		this.tripDay_hotelCost = tripDay_hotelCost;
	}

	@Override
	public String toString() {
		return "行程天編號："+tripDay_no+"\n"
				+"所屬行程編號："+trip_no+"\n"
				+"行程第幾天："+tripDay_days+"\n"
				+"此行程天日期："+tripDay_date+"\n"
				+"住宿名稱："+tripDay_hotelName+"\n"
				+"住宿時間："+tripDay_hotelStart+"\n"
				+"住宿備註："+tripDay_hotelNote+"\n"
				+"住宿花費："+tripDay_hotelCost+"\n";
	}
	
}
