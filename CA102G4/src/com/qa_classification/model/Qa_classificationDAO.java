package com.qa_classification.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Qa_classificationDAO implements Qa_classificationDAO_interface{

	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = "Insert into QA_CLASSIFICATION (LIST_ID,QUESTION_ID)"
			+ " values (?,?)";
	
	private static final String UPDATE_STMT = "UPDATE QA_CLASSIFICATION SET QUESTION_ID = ? WHERE LIST_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM QA_CLASSIFICATION WHERE LIST_ID = ?";
	private static final String FIND_BY_PK1 = "SELECT * FROM QA_CLASSIFICATION WHERE LIST_ID = ?";
	private static final String FIND_BY_PK2 = "SELECT * FROM QA_CLASSIFICATION WHERE QUESTION_ID = ?";
	private static final String GET_ALL = "SELECT * FROM QA_CLASSIFICATION";
	
	@Override
	public int insert(Qa_classificationVO Qa_classificationVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, Qa_classificationVO.getList_id());
			pstmt.setString(2, Qa_classificationVO.getQuestion_id());

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
	public int update(Qa_classificationVO Qa_classificationVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);
			

			pstmt.setString(1, Qa_classificationVO.getQuestion_id());
			pstmt.setString(2, Qa_classificationVO.getList_id());

			
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
	public int delete(String question_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1,question_id);

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
	public List<Qa_classificationVO> findByQuestion_id(String question_id) {
		List<Qa_classificationVO> list = new ArrayList<>();
		Qa_classificationVO Qa_classification = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK2);
			
			pstmt.setString(1, question_id);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Qa_classification = new Qa_classificationVO();
				System.out.println(rs.getString("list_id"));
				Qa_classification.setList_id(rs.getString("list_id"));
				Qa_classification.setQuestion_id(rs.getString("question_id"));
				list.add(Qa_classification);
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
	public List<Qa_classificationVO> findByList_id(String list_id) {
		List<Qa_classificationVO> list = new ArrayList<>();
		Qa_classificationVO Qa_classification = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK1);
			
			pstmt.setString(1, list_id);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Qa_classification = new Qa_classificationVO();
				System.out.println(rs.getString("question_id"));
				Qa_classification.setList_id(rs.getString("list_id"));
				Qa_classification.setQuestion_id(rs.getString("question_id"));
				list.add(Qa_classification);
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
	public List<Qa_classificationVO> getAll() {
		List<Qa_classificationVO> list = new ArrayList<Qa_classificationVO>();
		Qa_classificationVO Qa_classification = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Qa_classification = new Qa_classificationVO();
				Qa_classification.setQuestion_id(rs.getString("question_id"));
				Qa_classification.setList_id(rs.getString("list_id"));
				list.add(Qa_classification);
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
