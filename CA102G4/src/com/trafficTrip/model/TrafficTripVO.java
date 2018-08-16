package com.trafficTrip.model;

import java.io.Serializable;

public class TrafficTripVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String traTrip_no;
	private String tripDay_no;
	private Integer traTrip_type;
	private Integer traTrip_cost;
	private Integer traTrip_start;
	private Integer traTrip_end;
	private Integer trip_order;
	private String traTrip_name;
	private String traTrip_note;

	public TrafficTripVO() {
	}

	public TrafficTripVO(String traTrip_no, String tripDay_no, Integer traTrip_type, Integer traTrip_cost,
			Integer traTrip_start, Integer traTrip_end, Integer trip_order, String traTrip_name, String traTrip_note) {
		super();
		this.traTrip_no = traTrip_no;
		this.tripDay_no = tripDay_no;
		this.traTrip_type = traTrip_type;
		this.traTrip_cost = traTrip_cost;
		this.traTrip_start = traTrip_start;
		this.traTrip_end = traTrip_end;
		this.trip_order = trip_order;
		this.traTrip_name = traTrip_name;
		this.traTrip_note = traTrip_note;
	}

	public String getTraTrip_no() {
		return traTrip_no;
	}

	public void setTraTrip_no(String traTrip_no) {
		this.traTrip_no = traTrip_no;
	}

	public String getTripDay_no() {
		return tripDay_no;
	}

	public void setTripDay_no(String tripDay_no) {
		this.tripDay_no = tripDay_no;
	}

	public Integer getTraTrip_type() {
		return traTrip_type;
	}

	public void setTraTrip_type(Integer traTrip_type) {
		this.traTrip_type = traTrip_type;
	}

	public Integer getTraTrip_cost() {
		return traTrip_cost;
	}

	public void setTraTrip_cost(Integer traTrip_cost) {
		this.traTrip_cost = traTrip_cost;
	}

	public Integer getTraTrip_start() {
		return traTrip_start;
	}

	public void setTraTrip_start(Integer traTrip_start) {
		this.traTrip_start = traTrip_start;
	}

	public Integer getTraTrip_end() {
		return traTrip_end;
	}

	public void setTraTrip_end(Integer traTrip_end) {
		this.traTrip_end = traTrip_end;
	}

	public Integer getTrip_order() {
		return trip_order;
	}

	public void setTrip_order(Integer trip_order) {
		this.trip_order = trip_order;
	}

	public String getTraTrip_name() {
		return traTrip_name;
	}

	public void setTraTrip_name(String traTrip_name) {
		this.traTrip_name = traTrip_name;
	}

	public String getTraTrip_note() {
		return traTrip_note;
	}

	public void setTraTrip_note(String traTrip_note) {
		this.traTrip_note = traTrip_note;
	}
	
	@Override
	public String toString() {
		return "交通行程編號："+traTrip_no+"\n"
				+"所屬行程天編號："+tripDay_no+"\n"
				+"交通類型："+traTrip_type+"\n"
				+"交通花費："+traTrip_cost+"\n"
				+"交通開始時間："+traTrip_start+"\n"
				+"交通結束時間："+traTrip_end+"\n"
				+"行程順序："+trip_order+"\n"
				+"交通名稱："+traTrip_name+"\n"
				+"交通備註："+traTrip_note+"\n";
	}
}
