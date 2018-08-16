package com.blog_tag_name.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class blog_tag_nameJDBCDAO implements blog_tag_nameDAO_interface {

	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";

	// 新增TAG
	private static final String INSERT_STMT = "INSERT INTO BLOG_TAG_NAME(BTN_ID,BTN_CLASS,BTN_NAME) VALUES(TO_CHAR('BT'||LPAD(TO_CHAR(BTN_SEQ.NEXTVAL),6,'0')),?,?)";
	// 修改TAG
	private static final String UPDATE_STMT = "UPDATE BLOG_TAG_NAME SET BTN_CLASS = ?,BTN_NAME = ? WHERE BTN_ID = ?";
	// 刪除TAG
	private static final String DELETE_STMT = "DELETE FROM BLOG_TAG_NAME WHERE BTN_ID = ?";
	// 依照關鍵字搜尋 TAG'S CLASS OR NAME																	
	private static final String GET_ALL_BY_KEYWORD = "SELECT BTN_ID,BTN_CLASS,BTN_NAME FROM BLOG_TAG_NAME WHERE (UPPER(BLOG_TAG_NAME.BTN_CLASS) LIKE UPPER(?)) OR (UPPER(BLOG_TAG_NAME.BTN_NAME) LIKE UPPER(?))";
	// 取得全部
	private static final String GET_ALL_STMT = "SELECT * FROM BLOG_TAG_NAME ORDER BY BTN_ID";
	// getOne
	private static final String GET_ONE_STMT = "SELECT * FROM BLOG_TAG_NAME WHERE BTN_ID = ?";

	@Override
	public int insert(blog_tag_nameVO blog_tag_nameVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, blog_tag_nameVO.getBtn_class());
			pstmt.setString(2, blog_tag_nameVO.getBtn_name());

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
	public int update(blog_tag_nameVO blog_tag_nameVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setString(1, blog_tag_nameVO.getBtn_class());
			pstmt.setString(2, blog_tag_nameVO.getBtn_name());
			pstmt.setString(3, blog_tag_nameVO.getBtn_id());

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
	public int delete(String btn_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);

			pstmt.setString(1, btn_id);

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
	public List<blog_tag_nameVO> getAllBytagClass(String Keyword) {
		List<blog_tag_nameVO> list = new ArrayList<blog_tag_nameVO>();
		blog_tag_nameVO blog_tag_nameVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_BY_KEYWORD);

			pstmt.setString(1, "%" + Keyword + "%");
			pstmt.setString(2, "%" + Keyword + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_tag_nameVO = new blog_tag_nameVO();
				blog_tag_nameVO.setBtn_id(rs.getString("BTN_ID"));
				blog_tag_nameVO.setBtn_class(rs.getString("BTN_CLASS"));
				blog_tag_nameVO.setBtn_name(rs.getString("BTN_NAME"));
				list.add(blog_tag_nameVO);
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
	public List<blog_tag_nameVO> getAll() {
		List<blog_tag_nameVO> list = new ArrayList<blog_tag_nameVO>();
		blog_tag_nameVO blog_tag_nameVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_tag_nameVO = new blog_tag_nameVO();
				blog_tag_nameVO.setBtn_id(rs.getString("BTN_ID"));
				blog_tag_nameVO.setBtn_class(rs.getString("BTN_CLASS"));
				blog_tag_nameVO.setBtn_name(rs.getString("BTN_NAME"));
				list.add(blog_tag_nameVO);
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
	public blog_tag_nameVO findByBtn_id(String btn_id) {
		blog_tag_nameVO blog_tag_nameVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, btn_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_tag_nameVO = new blog_tag_nameVO();
				blog_tag_nameVO.setBtn_id(rs.getString("BTN_ID"));
				blog_tag_nameVO.setBtn_class(rs.getString("BTN_CLASS"));
				blog_tag_nameVO.setBtn_name(rs.getString("BTN_NAME"));			
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
		return blog_tag_nameVO;
	}
	
	public static void main(String args[]) {

		blog_tag_nameJDBCDAO dao = new blog_tag_nameJDBCDAO();

		// 新增標籤
//		blog_tag_nameVO blog_tag_nameVO = new blog_tag_nameVO();
//		blog_tag_nameVO.setBtn_class("旅遊關鍵字");
//		blog_tag_nameVO.setBtn_name("超值");
//		int updateCount_insert = dao.insert(blog_tag_nameVO);
//		System.out.println(updateCount_insert);

		// 修改標籤
//		blog_tag_nameVO blog_tag_nameVO2 = new blog_tag_nameVO();
//		blog_tag_nameVO2.setBtn_class("歐洲");
//		blog_tag_nameVO2.setBtn_name("瑞典");
//		blog_tag_nameVO2.setBtn_id("BT000004");
//		int updateCount_update = dao.update(blog_tag_nameVO2);
//		System.out.println(updateCount_update);

		// 刪除標籤
//		int updateCount_delete = dao.delete("BT000006");
//		System.out.println(updateCount_delete);

		// 依照關鍵字搜尋 TAG'S CLASS OR NAME
//		List<blog_tag_nameVO> list = dao.getAllBytagClass("一");
//		for (blog_tag_nameVO btn : list) {
//			System.out.print(btn.getBtn_id() + "\t");
//			System.out.print(btn.getBtn_class() + "\t");
//			System.out.println(btn.getBtn_name());
//		}
		
		// 取出全部
//		List<blog_tag_nameVO> list2 = dao.getAll();
//		for (blog_tag_nameVO btn : list2) {
//			System.out.print(btn.getBtn_id() + "\t");
//			System.out.print(btn.getBtn_class() + "\t");
//			System.out.println(btn.getBtn_name());
//		}
		
	}
}
