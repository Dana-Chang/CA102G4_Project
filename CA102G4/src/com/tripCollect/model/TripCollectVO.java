package com.tripCollect.model;

import java.io.Serializable;

public class TripCollectVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String trip_no;
	private String mem_id;
	
	public TripCollectVO() {}

	public TripCollectVO(String trip_no, String mem_id) {
		super();
		this.trip_no = trip_no;
		this.mem_id = mem_id;
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

	@Override
	public String toString() {
		return "行程編號："+trip_no+"\n"
				+"會員編號："+mem_id+"\n";
	}
	
}
