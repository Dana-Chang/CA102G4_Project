package com.attTrip.model;

import java.io.Serializable;

public class AttractionsTripVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String attTrip_no;
	private String att_no;
	private String tripDay_no;
	private Integer attTrip_cost;
	private Integer attTrip_start;
	private Integer attTrip_end;
	private Integer trip_order;
	private String attTrip_note;

	public AttractionsTripVO() {
	}

	public AttractionsTripVO(String attTrip_no, String att_no, String tripDay_no, Integer attTrip_cost,
			Integer attTrip_start, Integer attTrip_end, Integer trip_order, String attTrip_note) {
		super();
		this.attTrip_no = attTrip_no;
		this.att_no = att_no;
		this.tripDay_no = tripDay_no;
		this.attTrip_cost = attTrip_cost;
		this.attTrip_start = attTrip_start;
		this.attTrip_end = attTrip_end;
		this.trip_order = trip_order;
		this.attTrip_note = attTrip_note;
	}

	public String getAttTrip_no() {
		return attTrip_no;
	}

	public void setAttTrip_no(String attTrip_no) {
		this.attTrip_no = attTrip_no;
	}

	public String getAtt_no() {
		return att_no;
	}

	public void setAtt_no(String att_no) {
		this.att_no = att_no;
	}

	public String getTripDay_no() {
		return tripDay_no;
	}

	public void setTripDay_no(String tripDay_no) {
		this.tripDay_no = tripDay_no;
	}

	public Integer getAttTrip_cost() {
		return attTrip_cost;
	}

	public void setAttTrip_cost(Integer attTrip_cost) {
		this.attTrip_cost = attTrip_cost;
	}

	public Integer getAttTrip_start() {
		return attTrip_start;
	}

	public void setAttTrip_start(Integer attTrip_start) {
		this.attTrip_start = attTrip_start;
	}

	public Integer getAttTrip_end() {
		return attTrip_end;
	}

	public void setAttTrip_end(Integer attTrip_end) {
		this.attTrip_end = attTrip_end;
	}

	public Integer getTrip_order() {
		return trip_order;
	}

	public void setTrip_order(Integer trip_order) {
		this.trip_order = trip_order;
	}

	public String getAttTrip_note() {
		return attTrip_note;
	}

	public void setAttTrip_note(String attTrip_note) {
		this.attTrip_note = attTrip_note;
	}
	
	@Override
	public String toString() {
		return "景點行程編號："+attTrip_no+"\n"
				+"景點編號："+att_no+"\n"
				+"所屬行程天編號："+tripDay_no+"\n"
				+"景點花費："+attTrip_cost+"\n"
				+"景點開始時間："+attTrip_start+"\n"
				+"景點結束時間："+attTrip_end+"\n"
				+"行程順序："+trip_order+"\n"
				+"景點備註："+attTrip_note+"\n";
	}
}
