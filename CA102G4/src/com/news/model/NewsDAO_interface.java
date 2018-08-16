package com.news.model;

import java.util.List;

public interface NewsDAO_interface {
    public int insert(NewsVO NewsVO);
    public int update(NewsVO NewsVO);
    public int delete(String  news_id);
    public NewsVO findByPrimaryKey(String news_id);
    public List<NewsVO> getAll();
}
