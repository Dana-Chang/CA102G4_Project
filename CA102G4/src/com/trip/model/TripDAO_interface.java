package com.trip.model;

import java.util.*;

import com.tripDays.model.TripDaysVO;

public interface TripDAO_interface {
	public int insert(TripVO tripVO);
	public int update(TripVO tripVO);
	public int delete(String trip_no);
	public TripVO findByPrimaryKey(String trip_no);
	public List<TripVO> getAll();
	public void addTrip(TripVO tripVO);
	
	public void insertOneTrip(TripVO tripVO,List<TripDaysVO> tdList,Map<Integer,List<Object>> tripDayMap);
	
	public void update2(TripVO tripVO,List<TripDaysVO> tdList,Map<Integer,List<Object>> tripDayMap);
	
	public List<TripVO> getPublish();
	
	public List<TripVO> getByMem_id(String mem_id);
	
	public int deleteOnline(String trip_no);
	
	public List<TripVO> getPublishOrderViews();
	
	public List<TripVO> getAll(Map<String,String[]> map);
}
