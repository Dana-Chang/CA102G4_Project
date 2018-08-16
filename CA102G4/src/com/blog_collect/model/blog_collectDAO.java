package com.blog_collect.model;

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

public class blog_collectDAO implements blog_collectDAO_interface {
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	// 新增收藏部落格文章
	private static final String INSERT_STMT = "INSERT INTO BLOG_COLLECT(MEM_ID,BLOG_ID) VALUES (?,?)";
	// 刪除收藏部落格文章
	private static final String DELETE_STMT = "DELETE FROM BLOG_COLLECT WHERE MEM_ID = ? AND BLOG_ID = ?";
	// 取得某個會員的收藏列表
	private static final String GET_ALL_BY_MEMID_STMT = "SELECT BLOG_ID FROM BLOG_COLLECT WHERE MEM_ID = ?";
	// 取得某篇文章的收藏次數
	private static final String GET_ALL_BY_BLOGID_STMT = "SELECT * FROM BLOG_COLLECT WHERE BLOG_ID = ?";
	// 取得某會員是否收藏過此篇文章
	private static final String GET_CNT_BY_PRIMARYKEY_STMT = "SELECT COUNT(*) AS CNT FROM BLOG_COLLECT WHERE MEM_ID = ? AND BLOG_ID = ?";
	// 刪除多筆收藏部落格資料
	private static final String DELETE_ALL_STMT = "DELETE FROM BLOG_COLLECT WHERE BLOG_ID = ?";

	@Override
	public int insert(blog_collectVO blog_collectVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, blog_collectVO.getMem_id());
			pstmt.setString(2, blog_collectVO.getBlog_id());

			updateCount = pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured." + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public int delete(blog_collectVO blog_collectVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);

			pstmt.setString(1, blog_collectVO.getMem_id());
			pstmt.setString(2, blog_collectVO.getBlog_id());

			updateCount = pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public List<blog_collectVO> getAllByMem_id(String mem_id) {
		List<blog_collectVO> list = new ArrayList<blog_collectVO>();
		blog_collectVO blog_collectVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_BY_MEMID_STMT);

			pstmt.setString(1, mem_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_collectVO = new blog_collectVO();
				blog_collectVO.setBlog_id(rs.getString("BLOG_ID"));
				list.add(blog_collectVO);
			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	@Override
	public List<blog_collectVO> getAllByBlogId(String blog_id) {
		List<blog_collectVO> list = new ArrayList<blog_collectVO>();
		blog_collectVO blog_collectVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_BY_BLOGID_STMT);

			pstmt.setString(1, blog_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_collectVO = new blog_collectVO();
				blog_collectVO.setBlog_id(rs.getString("BLOG_ID"));
				blog_collectVO.setMem_id(rs.getString("MEM_ID"));
				list.add(blog_collectVO);
			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	@Override
	public int findByPrimaryKey(blog_collectVO blog_collectVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_CNT_BY_PRIMARYKEY_STMT);

			pstmt.setString(1, blog_collectVO.getMem_id());
			pstmt.setString(2, blog_collectVO.getBlog_id());

			rs = pstmt.executeQuery();
			rs.next();
			cnt = rs.getInt("CNT");
			
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
		return cnt;
	}
	
	@Override
	public int deleteAll(String blog_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_ALL_STMT);

			pstmt.setString(1, blog_id);

			updateCount = pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}
	
}
