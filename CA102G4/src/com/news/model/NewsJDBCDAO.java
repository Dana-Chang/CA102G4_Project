package com.news.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.news.model.NewsVO;

public class NewsJDBCDAO implements NewsDAO_interface  {
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "Insert into NEWS (NEWS_ID,NEWS_NAME,NEWS_DATE,NEWS_CON)"
			+ " values ('NE'||LPAD(to_char(NEWS_SEQ.NEXTVAL), 9, '0'),?,?,?)";
	
	private static final String UPDATE_STMT = "UPDATE NEWS SET NEWS_NAME=?, NEWS_DATE=?, NEWS_CON=? WHERE NEWS_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM NEWS WHERE NEWS_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM NEWS WHERE NEWS_ID = ?";
	private static final String GET_ALL = "SELECT * FROM NEWS";
	@Override
	public int insert(NewsVO news) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, news.getNews_name());
			pstmt.setDate(2, news.getNews_date());
			pstmt.setString(3, news.getNews_con());
			updateCount = pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException ce) {
			throw new RuntimeException("Couldn't load database driver. " + ce.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return updateCount;

	}
	@Override
	public int update(NewsVO NewsVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			
			pstmt.setString(1, NewsVO.getNews_name());
			pstmt.setDate(2, NewsVO.getNews_date());
			pstmt.setString(3, NewsVO.getNews_con());
			pstmt.setString(4, NewsVO.getNews_id());

			
			updateCount = pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException ce) {
			throw new RuntimeException("Couldn't load database driver. " + ce.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		}
		finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return updateCount;
	}
	@Override
	public int delete(String news_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1, news_id);

			updateCount = pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException ce) {
			throw new RuntimeException("Couldn't load database driver. " + ce.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		}
		finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return updateCount;
	}
	@Override
	public NewsVO findByPrimaryKey(String news_id) {
		NewsVO News = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);
			
			pstmt.setString(1, news_id);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			News = new NewsVO();
			System.out.println(rs.getString("news_id"));
			News.setNews_id(rs.getString("news_id"));
			News.setNews_name(rs.getString("news_name"));
			News.setNews_date(rs.getDate("news_date"));
			News.setNews_con(rs.getString("news_con"));

			
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return News;

	}
	@Override
	public List<NewsVO> getAll() {
		List<NewsVO> list = new ArrayList<NewsVO>();
		NewsVO News = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				News = new NewsVO();
				News.setNews_id(rs.getString("news_id"));
				News.setNews_name(rs.getString("news_name"));
				News.setNews_date(rs.getDate("news_date"));
				News.setNews_con(rs.getString("news_con"));
				list.add(News);
			}
			
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	public static void main(String[] args) {
		NewsJDBCDAO dao = new NewsJDBCDAO();
		
		//新增
		
//		NewsVO news1 = new NewsVO();
//		news1.setNews_name("TRAVEL_MAKER新增辦公室公告-台中公司");
//		news1.setNews_date(java.sql.Date.valueOf("2017-11-17"));
//		news1.setNews_con("親愛的客戶們，感謝大家對 TRAVEL_MAKER-台中公司的支持，為拓展業務及提供給大家更好的服務品質，台中公司於 2018/04/16 新增至：台中市西區大隆路20號10樓之11");
//
//		dao.insert(news1);
		
		// 修改
	
//		NewsVO news2 = new NewsVO();
//		news2.setNews_name("TRAVEL_MAKER新增辦公室公告-台中公司");
//		news2.setNews_date(java.sql.Date.valueOf("2017-11-17"));
//		news2.setNews_con("感謝大家對 TRAVEL_MAKER-台中公司的支持，為拓展業務及提供給大家更好的服務品質，台中公司於 2018/04/16 新增至：台中市西區大隆路20號10樓之11");
//		news2.setNews_id("NE000000003");
//     	dao.update(news2);
		
		
     	// 刪除
//		dao.delete("NE000000004");
		
     	// 查詢
		
		NewsVO news3 = new NewsVO();
		news3 = dao.findByPrimaryKey("NE000000001");
		System.out.println(news3.getNews_id());
		System.out.println(news3.getNews_name());
		System.out.println(news3.getNews_date());
		System.out.println(news3.getNews_con());

     	// 查詢
		
		List<NewsVO> list = new ArrayList<NewsVO>();
		list = dao.getAll();
		for (NewsVO news4 : list){
			System.out.println(news4.getNews_id());
		}
		
	}
}
