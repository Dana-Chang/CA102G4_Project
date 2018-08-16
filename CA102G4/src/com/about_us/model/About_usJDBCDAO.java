package com.about_us.model;

import java.util.*;
import java.sql.*;

public class About_usJDBCDAO implements About_usDAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "Insert into ABOUT_US (ABOUT_US_ID,ABOUT_CONTENT)"
			+ " values ('AB'||LPAD(to_char(ABOUT_US_SEQ.NEXTVAL), 9, '0'),?)";
	private static final String UPDATE_STMT = "UPDATE ABOUT_US SET ABOUT_CONTENT=? WHERE ABOUT_US_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM About_us WHERE ABOUT_US_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM About_us WHERE ABOUT_US_ID = ?";
	private static final String GET_ALL = "SELECT * FROM About_us";
	
	@Override
	public int insert(About_usVO About_usVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, About_usVO.getAbout_content());
			
			pstmt.executeUpdate();

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
	public int update(About_usVO About_usVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			
			pstmt.setString(1, About_usVO.getAbout_content());
			pstmt.setString(2, About_usVO.getAbout_us_id());

			
			pstmt.executeUpdate();

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
	public int delete(String about_us_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1, about_us_id);

			pstmt.executeUpdate();

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
	public About_usVO findByPrimaryKey(String about_us_id) {
		
		About_usVO About_us = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);
			
			pstmt.setString(1, about_us_id);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
			About_us = new About_usVO();
			System.out.println(rs.getString("about_us_id"));
			About_us.setAbout_us_id(rs.getString("about_us_id"));
			About_us.setAbout_content(rs.getString("About_content"));
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
		return About_us;

	}
	@Override
	public List<About_usVO> getAll() {
			List<About_usVO> list = new ArrayList<About_usVO>();
			About_usVO About_us = null;
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {

				Class.forName(DRIVER);
				con = DriverManager.getConnection(URL, USER, PASSWORD);
				pstmt = con.prepareStatement(GET_ALL);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					About_us = new About_usVO();
					About_us.setAbout_us_id(rs.getString("About_us_id"));
					About_us.setAbout_content(rs.getString("About_content"));
					list.add(About_us);
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
		About_usJDBCDAO dao = new About_usJDBCDAO();
		
		// 新增
		
		About_usVO about_as1 = new About_usVO();
		about_as1.setAbout_content("每個人都有一顆探險的心！透過「旅行」看見新世界，感受不同的風景，透過「旅行」發掘新事物，體驗不同的生活。\r\n" + 
				"我們提供豐富的旅遊資訊，幫助旅行者更容易規劃行程，更方便到達夢想之地，同時能結識志同道合的旅伴，勇敢探索未知的世界，無論是繁華大都會、秘境小村落，藝術跟古蹟，或遠離塵囂的愜意慢行！讓旅行者彼此互助與分擔，共享旅途風景。希望每個人，都能成為自由而且不孤單的旅行家，而不是只能跟著走的遊客。\r\n" + 
				"TRAVEL MAKER 不只希望和旅行者一起勇敢走出去，我們也期望透過我們的平台，協助被旅行的地方。協助偏遠資訊不發達的地方，可以有更多機會跟世界接軌，讓生活更好。我們注重環保更珍惜地球的資源，這也是我們推動旅遊共享的原因之一，串連起各地的旅遊同好，彼此交流、互相解答諮詢旅遊大小事，旅途中分享衣食住行的資源，共享的精神，不多浪費地球資源，讓未來的人也都可以享受你們看過的美景。>TRAVEL MAKER的辦公環境跟員工，也力求永續環保，由自身做起。\r\n" + 
				"");
		
		dao.insert(about_as1);
				
		// 修改
			
//		About_usVO about_as2 = new About_usVO();
//		about_as2.setAbout_content("TRAVEL MAKER 不只希望和旅行者一起勇敢走出去，我們也期望透過我們的平台，協助被旅行的地方。");
//		about_as2.setAbout_us_id("AB000000003");
//     	dao.update(about_as2);

				
				
     // 刪除
//		dao.delete("AB000000003");
				
     // 查詢
				
//		About_usVO about_as3 = new About_usVO();
//		about_as3 = dao.findByPrimaryKey("AB000000001");
//		System.out.println(about_as3.getAbout_us_id());
//		System.out.println(about_as3.getAbout_content());


     // 查詢
//		List<About_usVO> list = new ArrayList<About_usVO>();
//		list = dao.getAll();
//		for (About_usVO about_as4 : list){
//			System.out.println(about_as4.getAbout_us_id());
//		}
		
		
	}
}
