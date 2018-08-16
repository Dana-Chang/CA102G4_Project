package com.trip.model;

import java.sql.Date;
import java.util.*;

import com.mysql.fabric.xmlrpc.base.Array;
import com.tripCollect.model.*;
import com.tripDays.model.TripDaysVO;

public class TripService {
	
	private TripDAO_interface dao;
	
	public TripService() {
		dao = new TripDAO();
	}
	
	public TripVO addTrip(TripVO tripVO) {
		dao.addTrip(tripVO);
		return tripVO;
	}
	
	public TripVO updateTrip(String trip_no,String mem_id, String trip_name,
			Date trip_startDay, Integer trip_days, Integer trip_views, Integer trip_status) {
		TripVO tripVO = new TripVO();
		
		tripVO.setTrip_no(trip_no);
		tripVO.setMem_id(mem_id);
		tripVO.setTrip_name(trip_name);
		tripVO.setTrip_startDay(trip_startDay);
		tripVO.setTrip_days(trip_days);
		tripVO.setTrip_views(trip_views);
		tripVO.setTrip_status(trip_status);
		dao.update(tripVO);
		
		return tripVO;
	}
	
	public void deleteTrip(String trip_no) {
		dao.delete(trip_no);
	}
	
	public TripVO getOneTripByPK(String trip_no) {
		return dao.findByPrimaryKey(trip_no);
	}
	
	public List<TripVO> getAll(){
		return dao.getAll();
	}
	
	public void insertOneTrip(TripVO tripVO, List<TripDaysVO> tdList, Map<Integer, List<Object>> tripDayMap) {
		dao.insertOneTrip(tripVO, tdList, tripDayMap);
	}
	
	public List<TripVO> getByMem_id(String mem_id){
		return dao.getByMem_id(mem_id);
	}
	
	public List<TripVO> getOneMemCollection(String mem_id){
		List<TripVO> list = new ArrayList<>();
		TripCollectService tripColSvc = new TripCollectService();
		List<TripCollectVO> tripColList = tripColSvc.getByMem_id(mem_id);
		for(TripCollectVO tcVO : tripColList) {
			TripVO tripVO = new TripVO();
			tripVO = getOneTripByPK(tcVO.getTrip_no());
			list.add(tripVO);
		}
		return list;
	}
	
	public List<TripVO> getPublish() {
		return dao.getPublish();
	}
	
	public int deleteOnline(String trip_no) {
		return dao.deleteOnline(trip_no);
	}
	
	public List<TripVO> getPublishOrderViews() {
		return dao.getPublishOrderViews();
	}
	
	public void updateByEdit(TripVO tripVO, List<TripDaysVO> tdList, Map<Integer, List<Object>> tripDayMap) {
		dao.update2(tripVO, tdList, tripDayMap);
	}
	
	public List<TripVO> getAll(Map<String, String[]> map){
		return dao.getAll(map);
	}
}
