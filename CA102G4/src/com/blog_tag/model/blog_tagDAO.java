package com.blog_tag.model;

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

public class blog_tagDAO implements blog_tagDAO_interface {
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	// 一個部落格文章增加一個標籤
	private static final String INSERT_STMT = "INSERT INTO BLOG_TAG(BLOG_ID,BTN_ID) VALUES(?,?)";
	// 一個部落格文章移除一個標籤
	private static final String DELETEONE_STMT = "DELETE FROM BLOG_TAG WHERE BLOG_ID = ? AND BTN_ID = ?";
	// 一個部落格文章移除全部標籤
	private static final String DELETEALL_STMT = "DELETE FROM BLOG_TAG WHERE BLOG_ID = ?";
	// 查詢一個部落格文章有哪些標籤
	private static final String GET_All_BY_A_BLOG = "SELECT * FROM BLOG_TAG WHERE BLOG_ID = ?";
	// 查詢全部
	private static final String GET_ALL_STMT = "SELECT * FROM BLOG_TAG";
	// 刪除id為xxxx的所有標籤
	private static final String DELETE_ALL_BY_BTNID_STMT = "DELETE FROM BLOG_TAG WHERE BTN_ID = ?";
	
	@Override
	public int insert(blog_tagVO blog_tagVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, blog_tagVO.getBlog_id());
			pstmt.setString(2, blog_tagVO.getBtn_id());

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
	public int deleteOne(blog_tagVO blog_tagVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETEONE_STMT);

			pstmt.setString(1, blog_tagVO.getBlog_id());
			pstmt.setString(2, blog_tagVO.getBtn_id());

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
	public int deleteAll(String blog_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETEALL_STMT);

			pstmt.setString(1, blog_id);

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
	public List<blog_tagVO> getAllByABlog(String blog_id) {
		List<blog_tagVO> list = new ArrayList<blog_tagVO>();
		blog_tagVO blog_tagVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_All_BY_A_BLOG);

			pstmt.setString(1, blog_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_tagVO = new blog_tagVO();
				blog_tagVO.setBlog_id(rs.getString("BLOG_ID"));
				blog_tagVO.setBtn_id(rs.getString("BTN_ID"));
				list.add(blog_tagVO);
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
	
	public List<blog_tagVO> getAll() {
		List<blog_tagVO> list = new ArrayList<blog_tagVO>();
		blog_tagVO blog_tagVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				blog_tagVO = new blog_tagVO();
				blog_tagVO.setBlog_id(rs.getString("BLOG_ID"));
				blog_tagVO.setBtn_id(rs.getString("BTN_ID"));
				list.add(blog_tagVO);
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
	public int deleteAllByBtnID(String btn_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_ALL_BY_BTNID_STMT);

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
	
}
