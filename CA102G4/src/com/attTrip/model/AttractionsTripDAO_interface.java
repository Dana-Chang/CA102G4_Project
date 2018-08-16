package com.attTrip.model;

import java.util.List;

public interface AttractionsTripDAO_interface {
	public int insert(AttractionsTripVO attTripVO);
	public int update(AttractionsTripVO attTripVO);
	public int delete(String attTrip_no);
	public AttractionsTripVO findByPrimaryKey(String attTrip_no);
	public List<AttractionsTripVO> getAll();
	public void insert2(AttractionsTripVO attTripVO,java.sql.Connection con);
	
	public List<AttractionsTripVO> getByTripDays_no(String tripDays_no);
	
	public int deleteByTripDay(String tripDay_no,java.sql.Connection con);
}
