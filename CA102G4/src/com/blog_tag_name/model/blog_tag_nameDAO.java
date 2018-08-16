package com.blog_tag_name.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.blog.model.blogVO;

public class blog_tag_nameDAO implements blog_tag_nameDAO_interface {
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, blog_tag_nameVO.getBtn_class());
			pstmt.setString(2, blog_tag_nameVO.getBtn_name());

			updateCount = pstmt.executeUpdate();
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setString(1, blog_tag_nameVO.getBtn_class());
			pstmt.setString(2, blog_tag_nameVO.getBtn_name());
			pstmt.setString(3, blog_tag_nameVO.getBtn_id());

			updateCount = pstmt.executeUpdate();
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);

			pstmt.setString(1, btn_id);

			updateCount = pstmt.executeUpdate();
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
			con = ds.getConnection();
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_tag_nameVO = new blog_tag_nameVO();
				blog_tag_nameVO.setBtn_id(rs.getString("BTN_ID"));
				blog_tag_nameVO.setBtn_class(rs.getString("BTN_CLASS"));
				blog_tag_nameVO.setBtn_name(rs.getString("BTN_NAME"));
				list.add(blog_tag_nameVO);
			}
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
	public blog_tag_nameVO findByBtn_id(String btn_id) {
		blog_tag_nameVO blog_tag_nameVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, btn_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_tag_nameVO = new blog_tag_nameVO();
				blog_tag_nameVO.setBtn_id(rs.getString("BTN_ID"));
				blog_tag_nameVO.setBtn_class(rs.getString("BTN_CLASS"));
				blog_tag_nameVO.setBtn_name(rs.getString("BTN_NAME"));			
			}
			
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
}
