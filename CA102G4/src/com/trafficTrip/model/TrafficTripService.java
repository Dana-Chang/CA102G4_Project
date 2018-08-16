package com.trafficTrip.model;

import java.util.List;

public class TrafficTripService {
	private TrafficTripDAO_interface dao;
	
	public TrafficTripService() {
		dao = new TrafficTripDAO();
	}
	
	public List<TrafficTripVO> getByTripDays_no(String tripDays_no){
		return dao.getByTripDays_no(tripDays_no);
	}
}
