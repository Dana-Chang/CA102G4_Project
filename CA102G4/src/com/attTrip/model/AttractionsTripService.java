package com.attTrip.model;

import java.util.List;

public class AttractionsTripService {
	private AttractionsTripDAO_interface dao;
	
	public AttractionsTripService() {
		dao = new AttractionsTripDAO();
	}
	
	public List<AttractionsTripVO> getByTripDays_no(String tripDays_no){
		return dao.getByTripDays_no(tripDays_no);
	}
}
