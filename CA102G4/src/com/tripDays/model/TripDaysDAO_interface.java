package com.tripDays.model;

import java.util.List;


public interface TripDaysDAO_interface {
	public int insert(TripDaysVO tripDaysVO);
	public int update(TripDaysVO tripDaysVO);
	public int delete(String tripDay_no);
	public TripDaysVO findByPrimaryKey(String tripDay_no);
	public List<TripDaysVO> getAll();
	
	public List<TripDaysVO> getByTrip_no(String trip_no);
	
	//行程天VO，行程細節List，連線物件
	public void insert2(TripDaysVO tripDaysVO,List<Object> detailList,java.sql.Connection con);
	
	public void update2(TripDaysVO tripDaysVO,List<Object> detailList,java.sql.Connection con);
}
