package com.photo_wall_like.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class photo_wall_likeDAO implements photo_wall_likeDAO_interface {
	
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	// 新增喜歡照片牆
	private static final String INSERT_STMT = "INSERT INTO PHOTO_WALL_LIKE(MEM_ID,PHOTO_NO) VALUES (?,?)";
	// 刪除喜歡照片牆
	private static final String DELETE_STMT = "DELETE FROM PHOTO_WALL_LIKE WHERE MEM_ID = ? AND PHOTO_NO = ?";	
	// 取得某會員是否喜歡過照片牆
	private static final String GET_CNT_BY_PRIMARYKEY_STMT = "SELECT COUNT(*) AS CNT FROM PHOTO_WALL_LIKE WHERE MEM_ID = ? AND PHOTO_NO = ?";
	
	
	
	@Override
	public int insert(photo_wall_likeVO photo_wall_likeVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, photo_wall_likeVO.getMem_Id());
			pstmt.setString(2, photo_wall_likeVO.getPhoto_No());

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
	public int delete(photo_wall_likeVO photo_wall_likeVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);

			pstmt.setString(1, photo_wall_likeVO.getMem_Id());
			pstmt.setString(2, photo_wall_likeVO.getPhoto_No());

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
	public int findByPrimaryKey(photo_wall_likeVO photo_wall_likeVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_CNT_BY_PRIMARYKEY_STMT);

			pstmt.setString(1, photo_wall_likeVO.getMem_Id());
			pstmt.setString(2, photo_wall_likeVO.getPhoto_No());

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
	
}
