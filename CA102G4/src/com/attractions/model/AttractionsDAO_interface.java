package com.attractions.model;

import java.util.*;

public interface AttractionsDAO_interface {
	public int insert(AttractionsVO attVO);
	public int update(AttractionsVO attVO);
	public int delete(String att_no);
	public AttractionsVO findByPrimaryKey(String att_no);
	public List<AttractionsVO> getAll();
	public byte[] getAttPicture(String att_no);
	public List<AttractionsVO> getAll(Map<String,String[]> map);
	//世銘打的
	public List<AttractionsVO> getAllRandom();
}
