package com.tripCollect.model;

import java.util.List;

public class TripCollectService {
	
	private TripCollectDAO_interface dao;
	
	public TripCollectService() {
		dao = new TripCollectDAO();
	}
	
	public List<TripCollectVO> getByMem_id(String mem_id){
		return dao.getByMem_id(mem_id);
	}
	
	public int insert(TripCollectVO tripCollectVO) {
		return dao.insert(tripCollectVO);
	}
	
	public TripCollectVO findByPrimaryKey(String trip_no, String mem_id) {
		return dao.findByPrimaryKey(trip_no, mem_id);
	}
	
	public int delete(String trip_no, String mem_id) {
		return dao.delete(trip_no, mem_id);
	}
}
