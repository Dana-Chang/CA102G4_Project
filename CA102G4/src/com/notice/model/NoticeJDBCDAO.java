package com.notice.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.notice.model.NoticeVO;

public class NoticeJDBCDAO implements NoticeDAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "Insert into NOTICE (NOTICE_ID,MEM_IDFROM,MEM_IDTO,NOTICE_TITLE)"
			+ " values ('NT'||LPAD(to_char(NOTICE_SEQ.NEXTVAL), 9, '0'),?,?,?)";
	
	private static final String UPDATE_STMT = "UPDATE NOTICE SET MEM_IDFROM=? MEM_IDTO=? NOTICE_TITLE=? WHERE NOTICE_ID = ?";
	
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

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, NoticeVO.getMem_idfrom());
			pstmt.setString(2, NoticeVO.getMem_idto());
			pstmt.setString(3, NoticeVO.getNotice_title());
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
	public int update(NoticeVO NoticeVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			

			pstmt.setString(1, NoticeVO.getMem_idfrom());
			pstmt.setString(2, NoticeVO.getMem_idto());
			pstmt.setString(3, NoticeVO.getNotice_title());
			pstmt.setString(4, NoticeVO.getNotice_id());

			
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
	public int delete(String notice_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1,notice_id);

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
	public NoticeVO findByPrimaryKey(String notice_id) {
		NoticeVO Notice = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
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

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Notice = new NoticeVO();
				Notice.setNotice_id(rs.getString("notice_id"));
				Notice.setMem_idfrom(rs.getString("mem_idfrom"));
				Notice.setMem_idto(rs.getString("mem_idto"));
				Notice.setNotice_title(rs.getString("notice_title"));
				list.add(Notice);
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
//		NoticeJDBCDAO dao = new NoticeJDBCDAO();
		//新增
		
//		NoticeVO notice1 = new NoticeVO();
//		notice1.setMem_id("M000002");	
//		dao.insert(notice1);
				
		// 修改
			
//		NoticeVO notice2 = new NoticeVO();
//		notice2.setMem_id("M000001");
//		notice2.setNotice_id("NT000000002");
//     	dao.update(notice2);
			
     	// 刪除
//		dao.delete("NT000000003");
				
     	// 查詢
				
//		NoticeVO notice3 = new NoticeVO();
//		notice3 = dao.findByPrimaryKey("NT000000001");
//		System.out.println(notice3.getNotice_id());
//		System.out.println(notice3.getMem_id());


     	// 查詢
//		List<NoticeVO> list = new ArrayList<NoticeVO>();
//		list = dao.getAll();
//		for (NoticeVO notice4 : list){
//			System.out.println(notice4.getNotice_id());
//		}
		
		
	}
}
