package com.rp_report.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Rp_reportDAO implements Rp_reportDAO_interface{

	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = "Insert into RP_REPORT (REPLY_ID,MEM_ID,RP_STATE)"
			+ " values (?,?,0)";
	
	private static final String UPDATE_STMT = "UPDATE RP_REPORT SET MEM_ID= ?, RP_STATE=? WHERE REPLY_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM RP_REPORT WHERE REPLY_ID = ? AND MEM_ID=?";
	private static final String FIND_BY_PK = "SELECT * FROM RP_REPORT WHERE REPLY_ID = ?";
	private static final String GET_ALL = "SELECT * FROM RP_REPORT";
	
	private static final String GET_ALL_STMT = "SELECT * FROM RP_REPORT ORDER BY RP_STATE";
	@Override
	public int insert(Rp_reportVO Rp_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {


			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, Rp_reportVO.getReply_id());
			pstmt.setString(2, Rp_reportVO.getMem_id());
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
	public int update(Rp_reportVO Rp_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			pstmt.setString(1, Rp_reportVO.getMem_id());
			pstmt.setInt(2, Rp_reportVO.getRp_state());
			pstmt.setString(3, Rp_reportVO.getReply_id());

			
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
	public int delete(String reply_id, String mem_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1,reply_id);
			pstmt.setString(2,mem_id);
			
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
	public Rp_reportVO findByPrimaryKey(String reply_id) {
		Rp_reportVO RP_REPORT = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {


			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK);
			
			pstmt.setString(1, reply_id);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			RP_REPORT = new Rp_reportVO();
			System.out.println(rs.getString("reply_id"));
			RP_REPORT.setReply_id(rs.getString("reply_id"));
			RP_REPORT.setMem_id(rs.getString("mem_id"));
			RP_REPORT.setRp_state(rs.getInt("rp_state"));

			
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
		return RP_REPORT;

	}
	@Override
	public List<Rp_reportVO> getAll1() {
		List<Rp_reportVO> list = new ArrayList<Rp_reportVO>();
		Rp_reportVO RP_REPORT = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				RP_REPORT = new Rp_reportVO();
				RP_REPORT.setReply_id(rs.getString("rply_id"));
				RP_REPORT.setMem_id(rs.getString("mem_id"));
				RP_REPORT.setRp_state(rs.getInt("rp_state"));
				list.add(RP_REPORT);
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
		
		@Override
		public List<Rp_reportVO> getAll() {
			List<Rp_reportVO> list = new ArrayList<Rp_reportVO>();
			Rp_reportVO rp_reportVO = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ALL_STMT);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					rp_reportVO = new Rp_reportVO();
					rp_reportVO.setReply_id(rs.getString("REPLY_ID"));
					rp_reportVO.setMem_id(rs.getString("MEM_ID"));
					rp_reportVO.setRp_state(rs.getInt("RP_STATE"));
					list.add(rp_reportVO);
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured." + se.getMessage());
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
	}

