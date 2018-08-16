package com.blog.model;

import java.io.*;
import java.sql.*;
import java.util.*;

public class blogJDBCDAO implements blogDAO_interface {
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";

	// 新增一個部落格文章
	private static final String INSERT_STMT = "INSERT INTO BLOG(BLOG_ID,TRIP_NO,MEM_ID,BLOG_DATE,BLOG_TITLE,BLOG_CONTENT,TRAVEL_DATE,BLOG_VIEWS,BLOG_STATUS,BLOG_COVERIMAGE)"
			+ "VALUES(to_char('B'||LPAD(to_char(BLOG_SEQ.NEXTVAL), 6, '0')),?,?,SYSDATE,?,?,?,?,0,?)";
	// 修改部落格文章
	private static final String UPDATE_STMT = "UPDATE BLOG SET BLOG_DATE = SYSDATE,BLOG_TITLE = ?,BLOG_CONTENT = ?, TRAVEL_DATE = ?,TRIP_NO = ? ,BLOG_COVERIMAGE = ? WHERE BLOG_ID = ?";
	// 刪除一個部落格文章
	private static final String DELETE_STMT = "DELETE FROM BLOG WHERE BLOG_ID = ?";
	// 取得所有部落格文章，根據發文時間由新到舊排列
	private static final String GET_ALL_BY_NEW_STMT = "SELECT * FROM BLOG WHERE BLOG_STATUS = 0 ORDER BY BLOG_DATE DESC";
	// 取得所有部落格文章，根據瀏覽次數由多到少排列
	private static final String GET_ALL_BY_HOT_STMT = "SELECT * FROM BLOG WHERE BLOG_STATUS = 0 ORDER BY BLOG_VIEWS DESC";
	// 會員取得他發過的部落格文章，根據發文時間由新到舊排列
	private static final String FIND_BY_MEMID_STMT = "SELECT * FROM BLOG WHERE MEM_ID = ? AND BLOG_STATUS = 0 ORDER BY BLOG_DATE DESC";
	// 根據標題或內容搜尋，根據發文時間由新到舊排列
	private static final String GET_ALL_BY_KEYWORD_ORDER_BY_DATE = "SELECT * FROM BLOG WHERE (BLOG_STATUS = 0 AND UPPER(BLOG.BLOG_TITLE) LIKE UPPER(?)) OR (BLOG_STATUS = 0 AND UPPER(BLOG.BLOG_CONTENT) LIKE UPPER(?)) ORDER BY BLOG_DATE DESC";
	// 根據標題或內容搜尋，根據發文時間由新到舊排列
	private static final String GET_ALL_BY_KEYWORD_ORDER_BY_VIEWS = "SELECT * FROM BLOG WHERE (BLOG_STATUS = 0 AND UPPER(BLOG.BLOG_TITLE) LIKE UPPER(?)) OR (BLOG_STATUS = 0 AND UPPER(BLOG.BLOG_CONTENT) LIKE UPPER(?)) ORDER BY BLOG_DATE DESC";
	// 取得4個發文日期最新的部落格文章
	private static final String GET_ALL_BY_NEW_FOUR_STMT = "SELECT * FROM (SELECT * FROM BLOG WHERE BLOG_STATUS = 0 ORDER BY BLOG_DATE DESC )WHERE ROWNUM <=4";
	// 取得4個瀏覽次數最多的部落格文章
	private static final String GET_ALL_BY_HOT_FOUR_STMT = "SELECT * FROM (SELECT * FROM BLOG WHERE BLOG_STATUS = 0 ORDER BY BLOG_VIEWS DESC)WHERE ROWNUM <=4";
	// 取得單筆部落格文章
	private static final String GET_ONE_STMT = "SELECT * FROM BLOG WHERE BLOG_ID = ? AND BLOG_STATUS = 0";
	// 取得會員除了當筆文章的其他最近三筆文章
	private static final String GET_LASTED_BLOG_BY_MEMID = "SELECT * FROM (SELECT * FROM BLOG WHERE BLOG_STATUS = 0 AND MEM_ID=? AND NOT BLOG_ID=? ORDER BY BLOG_DATE DESC )WHERE ROWNUM <=3";
	// 修改部落格文章狀態-隱藏或顯示
	private static final String UPDATE_STATUS_STMT = "UPDATE BLOG SET BLOG_STATUS = ? WHERE BLOG_ID = ?";
	// 部落格瀏覽數++
	private static final String UPDATE_BLOG_VIEWS_STMT = "UPDATE BLOG SET BLOG_VIEWS = BLOG_VIEWS + 1 WHERE BLOG_ID = ?";
	
	@Override
	public String insert(blogVO blogVO) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String blog_id = null;
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			
			String[] blogIdCol = {"BLOG_ID"};
			pstmt = con.prepareStatement(INSERT_STMT,blogIdCol);

			pstmt.setString(1, blogVO.getTrip_no());
			pstmt.setString(2, blogVO.getMem_id());
			pstmt.setString(3, blogVO.getBlog_title());
			pstmt.setString(4, blogVO.getBlog_content());
			pstmt.setDate(5, blogVO.getTravel_date());
			pstmt.setInt(6, blogVO.getBlog_views());
			pstmt.setBytes(7, blogVO.getBlog_coverimage());

			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			
			rs.next();
			blog_id = rs.getString(1);
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return blog_id;
	}

	@Override
	public int update(blogVO blogVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setString(1, blogVO.getBlog_title());
			pstmt.setString(2, blogVO.getBlog_content());
			pstmt.setDate(3, blogVO.getTravel_date());
			pstmt.setString(4, blogVO.getTrip_no());
			pstmt.setBytes(5, blogVO.getBlog_coverimage());
			pstmt.setString(6, blogVO.getBlog_id());

			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public int delete(String blog_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);

			pstmt.setString(1, blog_id);

			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public List<blogVO> getAllByNew() {
		List<blogVO> list = new ArrayList<blogVO>();
		blogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_BY_NEW_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blogVO = new blogVO();
				blogVO.setMem_id(rs.getString("MEM_ID"));
				blogVO.setBlog_date(rs.getTimestamp("BLOG_DATE"));
				blogVO.setBlog_coverimage(rs.getBytes("BLOG_COVERIMAGE"));
				blogVO.setBlog_title(rs.getString("BLOG_TITLE"));
				blogVO.setBlog_content(rs.getString("BLOG_CONTENT"));
				blogVO.setTravel_date(rs.getDate("TRAVEL_DATE"));
				blogVO.setBlog_views(rs.getInt("BLOG_VIEWS"));
				list.add(blogVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

	@Override
	public List<blogVO> getAllByHot() {
		List<blogVO> list = new ArrayList<blogVO>();
		blogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_BY_HOT_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blogVO = new blogVO();
				blogVO.setMem_id(rs.getString("MEM_ID"));
				blogVO.setBlog_date(rs.getTimestamp("BLOG_DATE"));
				blogVO.setBlog_coverimage(rs.getBytes("BLOG_COVERIMAGE"));
				blogVO.setBlog_title(rs.getString("BLOG_TITLE"));
				blogVO.setBlog_content(rs.getString("BLOG_CONTENT"));
				blogVO.setTravel_date(rs.getDate("TRAVEL_DATE"));
				blogVO.setBlog_views(rs.getInt("BLOG_VIEWS"));
				list.add(blogVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

	@Override
	public List<blogVO> getAllByNewFour() {
		List<blogVO> list = new ArrayList<blogVO>();
		blogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_BY_NEW_FOUR_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blogVO = new blogVO();
				blogVO.setMem_id(rs.getString("MEM_ID"));
				blogVO.setBlog_date(rs.getTimestamp("BLOG_DATE"));
				blogVO.setBlog_coverimage(rs.getBytes("BLOG_COVERIMAGE"));
				blogVO.setBlog_title(rs.getString("BLOG_TITLE"));
				blogVO.setBlog_content(rs.getString("BLOG_CONTENT"));
				blogVO.setTravel_date(rs.getDate("TRAVEL_DATE"));
				blogVO.setBlog_views(rs.getInt("BLOG_VIEWS"));
				list.add(blogVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

	@Override
	public List<blogVO> findByMemId(String mem_id) {
		List<blogVO> list = new ArrayList<blogVO>();
		blogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_MEMID_STMT);

			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blogVO = new blogVO();
				blogVO.setMem_id(rs.getString("MEM_ID"));
				blogVO.setBlog_date(rs.getTimestamp("BLOG_DATE"));
				blogVO.setBlog_coverimage(rs.getBytes("BLOG_COVERIMAGE"));
				blogVO.setBlog_title(rs.getString("BLOG_TITLE"));
				blogVO.setBlog_content(rs.getString("BLOG_CONTENT"));
				blogVO.setTravel_date(rs.getDate("TRAVEL_DATE"));
				blogVO.setTrip_no(rs.getString("TRIP_NO"));
				blogVO.setBlog_views(rs.getInt("BLOG_VIEWS"));
				list.add(blogVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

	@Override
	public List<blogVO> getAllByHotFour() {
		List<blogVO> list = new ArrayList<blogVO>();
		blogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_BY_HOT_FOUR_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blogVO = new blogVO();
				blogVO.setMem_id(rs.getString("MEM_ID"));
				blogVO.setBlog_date(rs.getTimestamp("BLOG_DATE"));
				blogVO.setBlog_coverimage(rs.getBytes("BLOG_COVERIMAGE"));
				blogVO.setBlog_title(rs.getString("BLOG_TITLE"));
				blogVO.setBlog_content(rs.getString("BLOG_CONTENT"));
				blogVO.setTravel_date(rs.getDate("TRAVEL_DATE"));
				blogVO.setBlog_views(rs.getInt("BLOG_VIEWS"));
				list.add(blogVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

	@Override
	public List<blogVO> getAllByKeywordOrderByDate(String keyword) {
		List<blogVO> list = new ArrayList<blogVO>();
		blogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_BY_KEYWORD_ORDER_BY_DATE);
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setString(2, "%" + keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blogVO = new blogVO();
				blogVO.setMem_id(rs.getString("MEM_ID"));
				blogVO.setBlog_date(rs.getTimestamp("BLOG_DATE"));
				blogVO.setBlog_coverimage(rs.getBytes("BLOG_COVERIMAGE"));
				blogVO.setBlog_title(rs.getString("BLOG_TITLE"));
				blogVO.setBlog_content(rs.getString("BLOG_CONTENT"));
				blogVO.setTravel_date(rs.getDate("TRAVEL_DATE"));
				blogVO.setBlog_views(rs.getInt("BLOG_VIEWS"));
				list.add(blogVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

	@Override
	public List<blogVO> getAllByKeywordOrderByViews(String keyword) {
		List<blogVO> list = new ArrayList<blogVO>();
		blogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_BY_KEYWORD_ORDER_BY_VIEWS);
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setString(2, "%" + keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blogVO = new blogVO();
				blogVO.setMem_id(rs.getString("MEM_ID"));
				blogVO.setBlog_date(rs.getTimestamp("BLOG_DATE"));
				blogVO.setBlog_coverimage(rs.getBytes("BLOG_COVERIMAGE"));
				blogVO.setBlog_title(rs.getString("BLOG_TITLE"));
				blogVO.setBlog_content(rs.getString("BLOG_CONTENT"));
				blogVO.setTravel_date(rs.getDate("TRAVEL_DATE"));
				blogVO.setBlog_views(rs.getInt("BLOG_VIEWS"));
				list.add(blogVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	
	@Override
	public blogVO findByPrimaryKey(String blog_id) {
		blogVO blogVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, blog_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				blogVO = new blogVO();
				blogVO.setBlog_id(rs.getString("BLOG_ID"));
				blogVO.setMem_id(rs.getString("MEM_ID"));
				blogVO.setBlog_date(rs.getTimestamp("BLOG_DATE"));
				blogVO.setBlog_coverimage(rs.getBytes("BLOG_COVERIMAGE"));
				blogVO.setBlog_title(rs.getString("BLOG_TITLE"));
				blogVO.setBlog_content(rs.getString("BLOG_CONTENT"));
				blogVO.setTravel_date(rs.getDate("TRAVEL_DATE"));
				blogVO.setTrip_no(rs.getString("TRIP_NO"));
				blogVO.setBlog_views(rs.getInt("BLOG_VIEWS"));
				blogVO.setBlog_status(rs.getInt("BLOG_STATUS"));
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return blogVO;
	}

	@Override
	public List<blogVO> getThreeByMem_id(String mem_id, String blog_id) {
		List<blogVO> list = new ArrayList<blogVO>();
		blogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_LASTED_BLOG_BY_MEMID);
			pstmt.setString(1, mem_id);
			pstmt.setString(2, blog_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blogVO = new blogVO();
				blogVO.setBlog_id(rs.getString("BLOG_ID"));
				blogVO.setMem_id(rs.getString("MEM_ID"));
				blogVO.setBlog_date(rs.getTimestamp("BLOG_DATE"));
				blogVO.setBlog_coverimage(rs.getBytes("BLOG_COVERIMAGE"));
				blogVO.setBlog_title(rs.getString("BLOG_TITLE"));
				blogVO.setBlog_content(rs.getString("BLOG_CONTENT"));
				blogVO.setTravel_date(rs.getDate("TRAVEL_DATE"));
				blogVO.setTrip_no(rs.getString("TRIP_NO"));
				blogVO.setBlog_views(rs.getInt("BLOG_VIEWS"));
				blogVO.setBlog_status(rs.getInt("BLOG_STATUS"));
				list.add(blogVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

	@Override
	public int updateStatus(Integer blog_status, String blog_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STATUS_STMT);

			pstmt.setInt(1, blog_status);
			pstmt.setString(2, blog_id);

			updateCount = pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

	public int updateViews(String blog_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_BLOG_VIEWS_STMT);

			pstmt.setString(1, blog_id);

			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	
	public static void main(String args[]) throws IOException {
		blogJDBCDAO dao = new blogJDBCDAO();

		 // 取得所有部落格文章，根據發文時間由新到舊排列
//		 List<blogVO> list = dao.getAllByNew();
//		 for (blogVO blog : list) {
//		 System.out.println(blog.getMem_id());
//		 System.out.println(blog.getBlog_date());
//		 System.out.println(blog.getBlog_coverimage());
//		 System.out.println(blog.getBlog_title());
//		 System.out.println(blog.getBlog_content());
//		 System.out.println(blog.getTravel_date());
//		 System.out.println(blog.getBlog_views());
//		 }

		// 取得所有部落格文章，根據瀏覽次數由多到少排列
		// List<blogVO> list2 = dao.getAllByHot();
		// for (blogVO blog : list2) {
		// System.out.println(blog.getMem_id());
		// System.out.println(blog.getBlog_date());
		// System.out.println(blog.getBlog_coverimage());
		// System.out.println(blog.getBlog_title());
		// System.out.println(blog.getBlog_content());
		// System.out.println(blog.getTravel_date());
		// System.out.println(blog.getBlog_views());
		// }

		// 取得4個發文日期最新的部落格文章
		// List<blogVO> list3 = dao.getAllByNewFour();
		// for (blogVO blog : list3) {
		// System.out.println(blog.getMem_id());
		// System.out.println(blog.getBlog_date());
		// System.out.println(blog.getBlog_coverimage());
		// System.out.println(blog.getBlog_title());
		// System.out.println(blog.getBlog_content());
		// System.out.println(blog.getTravel_date());
		// System.out.println(blog.getBlog_views());
		// }

		// 取得4個瀏覽次數最多的部落格文章
//		 List<blogVO> list4 = dao.getAllByHotFour();
//		 for (blogVO blog : list4) {
//		 System.out.println(blog.getMem_id());
//		 System.out.println(blog.getBlog_date());
//		 System.out.println(blog.getBlog_coverimage());
//		 System.out.println(blog.getBlog_title());
//		 System.out.println(blog.getBlog_content());
//		 System.out.println(blog.getTravel_date());
//		 System.out.println(blog.getBlog_views());
//		 }

		// 新增一個部落格文章
		 blogVO blogVO = new blogVO();
		 byte[] pic =
		 getPictureByteArray("D:\\GitHub\\CA102-G4\\CA102G4\\images\\g4.jpg");
		 blogVO.setTrip_no("T000000001");
		 blogVO.setMem_id("M000007");
		 blogVO.setBlog_title("JDBC TEST");
		 blogVO.setBlog_content("JDBC TEST");
		 blogVO.setTravel_date(java.sql.Date.valueOf("2018-08-30"));
		 blogVO.setBlog_views(80);
		 blogVO.setBlog_coverimage(pic);
		 String updateCount_insert = dao.insert(blogVO);
		 System.out.println(updateCount_insert);

		// 修改部落格文章
//		blogVO blogVO2 = new blogVO();
//		byte[] pic2 = getPictureByteArray("D:\\GitHub\\CA102-G4\\CA102-G4\\images\\g6.jpg");
//		blogVO2.setBlog_title("修改過的內容");
//		blogVO2.setBlog_content("123");
//		blogVO2.setTravel_date(java.sql.Date.valueOf("2018-07-05"));
//		blogVO2.setTrip_no("T000000001");
//		blogVO2.setBlog_coverimage(pic2);
//		blogVO2.setBlog_id("B000002");
//		int updateCount_update = dao.update(blogVO2);
//		System.out.println(updateCount_update);

		// 刪除一個部落格文章
//		 int updateCount_delete = dao.delete("B000063");
//		 System.out.println(updateCount_delete);
		
//		int updateBlogViews = dao.updateViews("B000062");
//		System.out.println(updateBlogViews);
		// 會員取得他發過的部落格文章
		// List<blogVO> list5 = dao.findByMemId("M000002");
		// for (blogVO blog : list5) {
		// System.out.println(blog.getMem_id());
		// System.out.println(blog.getBlog_date());
		// System.out.println(blog.getBlog_coverimage());
		// System.out.println(blog.getBlog_title());
		// System.out.println(blog.getBlog_content());
		// System.out.println(blog.getTravel_date());
		// System.out.println(blog.getTrip_no());
		// System.out.println(blog.getBlog_views());
		// }

		// 根據標題或內容搜尋
		// List<blogVO> list6 = dao.getAllByKeyword("WIKI");
		// for (blogVO blog : list6) {
		// System.out.println(blog.getMem_id());
		// System.out.println(blog.getBlog_date());
		// System.out.println(blog.getBlog_coverimage());
		// System.out.println(blog.getBlog_title());
		// System.out.println(blog.getBlog_content());
		// System.out.println(blog.getTravel_date());
		// System.out.println(blog.getTrip_no());
		// System.out.println(blog.getBlog_views());
		// }
	}

	public static byte[] getPictureByteArray(String path) throws IOException {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int i;
		while ((i = fis.read(buffer)) != -1) {
			baos.write(buffer, 0, i);
		}
		baos.close();
		fis.close();

		return baos.toByteArray();
	}
}
