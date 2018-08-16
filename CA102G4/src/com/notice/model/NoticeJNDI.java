package com.notice.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class NoticeJNDI implements NoticeDAO_interface{

	
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = "Insert into NOTICE (NOTICE_ID,MEM_IDFROM,MEM_IDTO,NOTICE_TITLE,NOTICE_CON,NOTICE_STATE)"
			+ " values ('NT'||LPAD(to_char(NOTICE_SEQ.NEXTVAL), 9, '0'),?,?,?,?,?)";
	
	private static final String UPDATE_STMT = "UPDATE NOTICE SET MEM_IDFROM=? MEM_IDTO=? NOTICE_TITLE=? NOTICE_CON=? NOTICE_STATE=? WHERE NOTICE_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM NOTICE WHERE NOTICE_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM NOTICE WHERE NOTICE_ID = ?";
	private static final String GET_ALL = "SELECT * FROM NOTICE";
	@Override
	public int insert(NoticeVO NoticeVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, NoticeVO.getMem_idfrom());
			pstmt.setString(2, NoticeVO.getMem_idto());
			pstmt.setString(3, NoticeVO.getNotice_title());
			pstmt.setString(4, NoticeVO.getNotice_con());
			
			updateCount = pstmt.executeUpdate();

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
	public int update(NoticeVO NoticeVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);
			

			pstmt.setString(1, NoticeVO.getMem_idfrom());
			pstmt.setString(2, NoticeVO.getMem_idto());
			pstmt.setString(3, NoticeVO.getNotice_title());
			pstmt.setString(4, NoticeVO.getNotice_con());
			pstmt.setString(5, NoticeVO.getNotice_id());

			
			updateCount = pstmt.executeUpdate();

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
	public int delete(String notice_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1,notice_id);

			updateCount = pstmt.executeUpdate();

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
	public NoticeVO findByPrimaryKey(String notice_id) {
		NoticeVO Notice = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {


			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK);
			
			pstmt.setString(1, notice_id);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			Notice = new NoticeVO();
			System.out.println(rs.getString("notice_id"));
			Notice.setNotice_id(rs.getString("notice_id"));
			Notice.setMem_idfrom(rs.getString("mem_idfrom"));
			Notice.setMem_idto(rs.getString("mem_idto"));
			Notice.setNotice_title(rs.getString("notice_title"));
			Notice.setNotice_con(rs.getString("notice_con"));
			Notice.setNotice_state(rs.getInt("notice_state"));
			
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
		return Notice;

	}
	@Override
	public List<NoticeVO> getAll() {
		List<NoticeVO> list = new ArrayList<NoticeVO>();
		NoticeVO Notice = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Notice = new NoticeVO();
				Notice.setNotice_id(rs.getString("notice_id"));
				Notice.setMem_idfrom(rs.getString("mem_idfrom"));
				Notice.setMem_idto(rs.getString("mem_idto"));
				Notice.setNotice_title(rs.getString("notice_title"));
				Notice.setNotice_con(rs.getString("notice_con"));
				Notice.setNotice_state(rs.getInt("notice_state"));
				list.add(Notice);
			}
			
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
}
