package com.grp_com.model;

import java.util.List;

public interface Grp_comDAO_interface {
	public void insert(Grp_comVO grp_comVO);
	public void update(Grp_comVO grp_comVO);
	public void delete(String grp_Com_Id);
	public Grp_comVO findByPrimaryKey(String grp_Com_Id);
	public List<Grp_comVO> getAll() ;

}
