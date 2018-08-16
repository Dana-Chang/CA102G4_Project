package com.qa_collect.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qa_collect.model.Qa_collectVO;

public class Qa_collectJDBCDAO implements Qa_collectDAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "INSERT INTO QA_COLLECT (QUESTION_ID,MEM_ID)"
			+ " values (?,?)";
	
	private static final String UPDATE_STMT = "UPDATE QA_COLLECT SET MEM_ID=? WHERE QUESTION_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM QA_COLLECT WHERE QUESTION_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM QA_COLLECT WHERE QUESTION_ID = ?";
	private static final String GET_ALL = "SELECT * FROM QA_COLLECT";
	@Override
	public int insert(Qa_collectVO Qa_collectVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, Qa_collectVO.getQuestion_id());
			pstmt.setString(2, Qa_collectVO.getMem_id());
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
	public int update(Qa_collectVO Qa_collectVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			pstmt.setString(1, Qa_collectVO.getMem_id());
			pstmt.setString(2, Qa_collectVO.getQuestion_id());;

			
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
	public int delete(String question_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1,question_id);

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
	public Qa_collectVO findByPrimaryKey(String question_id) {
		Qa_collectVO Qa_collect = null;
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
			Qa_collect = new Qa_collectVO();
			System.out.println(rs.getString("question_id"));
			Qa_collect.setQuestion_id(rs.getString("question_id"));
			Qa_collect.setMem_id(rs.getString("mem_id"));


			
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
		return Qa_collect;

	}
	@Override
	public List<Qa_collectVO> getAll() {
		List<Qa_collectVO> list = new ArrayList<Qa_collectVO>();
		Qa_collectVO Qa_collect = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Qa_collect = new Qa_collectVO();
				Qa_collect.setQuestion_id(rs.getString("question_id"));
				Qa_collect.setMem_id(rs.getString("mem_id"));
				list.add(Qa_collect);
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
		Qa_collectJDBCDAO dao = new Qa_collectJDBCDAO();
		//新增
		
//		Qa_collectVO qa_collect1 = new Qa_collectVO();
//		qa_collect1.setQuestion_id("QU000000003");	
//		qa_collect1.setMem_id("M000002");	
//		dao.insert(qa_collect1);
				
		// 修改
			
//		Qa_collectVO qa_collect2 = new Qa_collectVO();
//		qa_collect2.setMem_id("M000004");
//		qa_collect2.setQuestion_id("QU000000001");
//     	dao.update(qa_collect2);

				
				
     	// 刪除
//		dao.delete("QU000000003");
				
     	// 查詢
				
		Qa_collectVO qa_collect3 = new Qa_collectVO();
		qa_collect3 = dao.findByPrimaryKey("QU000000001");
		System.out.println(qa_collect3.getQuestion_id());
		System.out.println(qa_collect3.getMem_id());


		// 查詢
		List<Qa_collectVO> list = new ArrayList<Qa_collectVO>();
		list = dao.getAll();
		for (Qa_collectVO qa_collect4 : list){
			System.out.println(qa_collect4.getQuestion_id());
		}
		
		
	}
	@Override
	public List<Qa_collectVO> getAllByMem_id(String mem_id) {
		// TODO Auto-generated method stub
		return null;
	}


}

