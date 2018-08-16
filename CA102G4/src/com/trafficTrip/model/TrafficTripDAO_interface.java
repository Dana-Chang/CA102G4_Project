package com.trafficTrip.model;

import java.util.List;

public interface TrafficTripDAO_interface {
	public int insert(TrafficTripVO traTripVO);
	public int update(TrafficTripVO traTripVO);
	public int delete(String traTrip_no);
	public TrafficTripVO findByPrimaryKey(String traTrip_no);
	public List<TrafficTripVO> getAll();
	
	public void insert2(TrafficTripVO traTripVO,java.sql.Connection con);
	
	public List<TrafficTripVO> getByTripDays_no(String tripDays_no);
	
	public int deleteByTripDay(String tripDay_no,java.sql.Connection con);
}
