package com.attEdit.model;

import java.util.List;

public interface AttractionsEditDAO_interface {
	public int insert(AttractionsEditVO attEditVO);
	public int update(AttractionsEditVO attEditVO);
	public int delete(String attEdit_no);
	public AttractionsEditVO findByPrimaryKey(String attEdit_no);
	public List<AttractionsEditVO> getAll();
	public List<AttractionsEditVO> getAllOrderByDate();
	public int att_update(AttractionsEditVO attEditVO);
}
