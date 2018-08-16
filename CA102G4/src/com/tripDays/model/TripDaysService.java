package com.tripDays.model;

import java.util.List;

public class TripDaysService {
	private TripDaysDAO_interface dao;
	
	public TripDaysService() {
		dao = new TripDaysDAO();
	}
	
	public List<TripDaysVO> getByTrip_no(String trip_no){
		return dao.getByTrip_no(trip_no);
	}
	
	public int update(TripDaysVO tripDaysVO) {
		return dao.update(tripDaysVO);
	}
}
