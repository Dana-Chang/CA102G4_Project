package com.qa_report.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qa_report.model.Qa_reportVO;

public class Qa_reportJDBCDAO implements Qa_reportDAO_interface{
	
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "Insert into QA_REPORT (QUESTION_ID,MEM_ID,QA_STATE)"
			+ " values (?,?,?)";
	
	private static final String UPDATE_STMT = "UPDATE QA_REPORT SET MEM_ID= ?, QA_STATE=? WHERE QUESTION_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM QA_REPORT WHERE QUESTION_ID AND MEM_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM QA_REPORT WHERE QUESTION_ID = ?";
	private static final String GET_ALL = "SELECT * FROM QA_REPORT";
	@Override
	public int insert(Qa_reportVO Qa_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, Qa_reportVO.getQuestion_id());
			pstmt.setString(2, Qa_reportVO.getMem_id());
			pstmt.setInt(3, Qa_reportVO.getQa_state());
			updateCount = pstmt.executeUpdate();

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
	public int update(Qa_reportVO Qa_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			pstmt.setString(1, Qa_reportVO.getMem_id());
			pstmt.setInt(2, Qa_reportVO.getQa_state());
			pstmt.setString(3, Qa_reportVO.getQuestion_id());

			
			updateCount = pstmt.executeUpdate();

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
	public int delete(String question_id,String mem_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1,question_id);
			pstmt.setString(2,mem_id);
			updateCount = pstmt.executeUpdate();

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
	public Qa_reportVO findByPrimaryKey(String question_id) {
		Qa_reportVO Qa_report = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);
			
			pstmt.setString(1, question_id);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			Qa_report = new Qa_reportVO();
			System.out.println(rs.getString("question_id"));
			Qa_report.setQuestion_id(rs.getString("question_id"));
			Qa_report.setMem_id(rs.getString("mem_id"));
			Qa_report.setQa_state(rs.getInt("qa_state"));

			
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

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Qa_report = new Qa_reportVO();
				Qa_report.setQuestion_id(rs.getString("question_id"));
				Qa_report.setMem_id(rs.getString("mem_id"));
				Qa_report.setQa_state(rs.getInt("qa_state"));
				list.add(Qa_report);
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
		Qa_reportJDBCDAO dao = new Qa_reportJDBCDAO();
		//新增
		
//		Qa_reportVO qa_report1 = new Qa_reportVO();
//		qa_report1.setQuestion_id("QU000000003");
//		qa_report1.setMem_id("M000001");
//		qa_report1.setqa_state("0");	
//		dao.insert(qa_report1);
							
		// 修改
						
//		Qa_reportVO qa_report2 = new Qa_reportVO();
//		qa_report2.setMem_id("M000002");
//		qa_report2.setQa_state("1");
//		qa_report2.setQuestion_id("QU000000001");
//		dao.update(qa_report2);

							
							
		// 刪除
//		dao.delete("QU000000003");
							
		// 查詢
							
//		Qa_reportVO qa_report3 = new Qa_reportVO();
//		qa_report3 = dao.findByPrimaryKey("QU000000001");
//		System.out.println(qa_report3.getQuestion_id());
//		System.out.println(qa_report3.getMem_id());
//		System.out.println(qa_report3.getqa_state());


		// 查詢
//		List<Qa_reportVO> list = new ArrayList<Qa_reportVO>();
//		list = dao.getAll();
//		for (Qa_reportVO qa_report4 : list){
//			System.out.println(qa_report4.getQuestion_id());
//			}
					
				
	}

}

