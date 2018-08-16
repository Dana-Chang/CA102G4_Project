package com.news.model;

import java.sql.Date;
import java.util.List;


public class NewsService {

private	NewsDAO_interface dao;
	
	public	NewsService() {
		dao = new NewsDAO();
	}
	
	public	NewsVO	addNews(String news_name, Date news_date, String news_con) {
		
		NewsVO NewsVO = new NewsVO();
		
		NewsVO.setNews_name(news_name);
		NewsVO.setNews_date(news_date);
		NewsVO.setNews_con(news_con);
		
		dao.insert(NewsVO);
		
		return NewsVO;
	}
	
	public	NewsVO	updateNews(String news_id, String news_name, Date news_date, String news_con) {
		
		NewsVO NewsVO = new NewsVO();
		
		NewsVO.setNews_id(news_id);
		NewsVO.setNews_name(news_name);
		NewsVO.setNews_date(news_date);
		NewsVO.setNews_con(news_con);
		
		dao.update(NewsVO);
		
		return NewsVO;
	}
	
	public void deleteNews(String news_id) {
		dao.delete(news_id);
	}
	
	public NewsVO getOneNews(String news_id) {
		return dao.findByPrimaryKey(news_id);
	}

	public List<NewsVO> getAll() {
		return dao.getAll();
	}
}	
