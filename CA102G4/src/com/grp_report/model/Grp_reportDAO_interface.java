package com.grp_report.model;

import java.util.List;

public interface Grp_reportDAO_interface {
	public void insert(Grp_reportVO grp_reportVO);
	public void update(Grp_reportVO grp_reportVO);
	public void delete(String grp_Id,String mem_Id);
	public Grp_reportVO findByPrimaryKey(String grp_Id,String mem_Id);
	public List<Grp_reportVO> getAll() ;

}
