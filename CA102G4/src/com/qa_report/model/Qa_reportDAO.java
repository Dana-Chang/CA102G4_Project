package com.qa_report.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Qa_reportDAO implements Qa_reportDAO_interface{

	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = "Insert into QA_REPORT (QUESTION_ID,MEM_ID,QA_STATE)"
			+ " values (?,?,0)";
	
	private static final String UPDATE_STMT = "UPDATE QA_REPORT SET MEM_ID= ?, QA_STATE=? WHERE QUESTION_ID = ?";	
	private static final String DELETE_STMT = "DELETE FROM QA_REPORT WHERE QUESTION_ID=? AND MEM_ID=?";
	private static final String FIND_BY_PK = "SELECT * FROM QA_REPORT WHERE QUESTION_ID = ?";
	private static final String GET_ALL = "SELECT * FROM QA_REPORT";
	
	
	
	
	@Override
	public int insert(Qa_reportVO Qa_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {


			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, Qa_reportVO.getQuestion_id());
			pstmt.setString(2, Qa_reportVO.getMem_id());
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
	public int update(Qa_reportVO Qa_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			pstmt.setString(1, Qa_reportVO.getMem_id());
			pstmt.setInt(2, Qa_reportVO.getQa_state());
			pstmt.setString(3, Qa_reportVO.getQuestion_id());

			
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
	public int delete(String question_id, String mem_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1,question_id);
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
	public Qa_reportVO findByPrimaryKey(String question_id) {
		Qa_reportVO Qa_report = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {


			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK);
			
			pstmt.setString(1, question_id);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			Qa_report = new Qa_reportVO();
			Qa_report.setQuestion_id(rs.getString("question_id"));
			Qa_report.setMem_id(rs.getString("mem_id"));
			Qa_report.setQa_state(rs.getInt("Qa_state"));

			
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
		return Qa_report;

	}
	@Override
	public List<Qa_reportVO> getAll() {
		List<Qa_reportVO> list = new ArrayList<Qa_reportVO>();
		Qa_reportVO Qa_report = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Qa_report = new Qa_reportVO();
				Qa_report.setQuestion_id(rs.getString("question_id"));
				Qa_report.setMem_id(rs.getString("mem_id"));
				Qa_report.setQa_state(rs.getInt("Qa_state"));
				list.add(Qa_report);
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
