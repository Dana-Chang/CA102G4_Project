package com.tripCollect.model;

import java.util.List;

public interface TripCollectDAO_interface {
	public int insert(TripCollectVO tripCollectVO);
	public int update(TripCollectVO tripCollectVO_old,TripCollectVO tripCollectVO_new);
	public int delete(String trip_no,String mem_id);
	public TripCollectVO findByPrimaryKey(String trip_no,String mem_id);
	public List<TripCollectVO> getAll();
	
	public List<TripCollectVO> getByMem_id(String mem_id);
}
