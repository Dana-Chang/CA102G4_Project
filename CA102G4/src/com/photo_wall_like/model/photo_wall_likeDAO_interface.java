package com.photo_wall_like.model;

public interface photo_wall_likeDAO_interface {
	
	public int insert(photo_wall_likeVO photo_wall_likeVO);

	public int delete(photo_wall_likeVO photo_wall_likeVO);

	public int findByPrimaryKey(photo_wall_likeVO photo_wall_likeVO);

}
