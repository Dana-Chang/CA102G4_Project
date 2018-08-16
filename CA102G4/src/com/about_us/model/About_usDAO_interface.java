package com.about_us.model;

import java.util.List;

public interface About_usDAO_interface {
	public int insert(About_usVO About_usVO);
	public int update(About_usVO About_usVO);
	public int delete(String about_us_id);
	public About_usVO findByPrimaryKey(String about_us_id);
	public List<About_usVO> getAll();
}
