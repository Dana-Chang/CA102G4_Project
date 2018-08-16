package com.rp_report.model;

import java.util.List;



public interface Rp_reportDAO_interface {
	public int insert(Rp_reportVO Rp_reportVO);
	public int update(Rp_reportVO Rp_reportVO);
	public int delete (String reply_id,String mem_id);
	public Rp_reportVO findByPrimaryKey(String reply_id);
	public List <Rp_reportVO> getAll();
	public List<Rp_reportVO> getAll1();
}
