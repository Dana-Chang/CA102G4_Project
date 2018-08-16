//package com.grp_mem.model;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class Grp_memJDBCDAO implements Grp_memDAO_interface {
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String userid = "CA102G4";
//	String passwd = "12345678";
//	
//	private static final String INSERT_STMT =
//			"Insert into GRP_MEM (GRP_ID,MEM_ID,GRP_LEADER) VALUES (?,?,?)";
//	private static final String UPDATE = 
//			"UPDATE GRP_MEM SET GRP_LEADER= ? WHERE GRP_ID =? AND MEM_ID = ?";
//	private static final String DELETE_GRP_MEM = 
//			"DELETE FROM GRP_MEM WHERE GRP_ID =? AND MEM_ID = ?";
//	private static final String GET_ONE = 
//			"SELECT * FROM GRP_MEM WHERE GRP_ID = ? AND MEM_ID=?";
//	private static final String GET_ALL = 
//			"SELECT * FROM GRP_MEM ORDER BY GRP_ID,MEM_ID";
//	
//	
//	@Override
//	public void insert(Grp_memVO grp_memVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(INSERT_STMT);
//			
//			pstmt.setString(1, grp_memVO.getGrp_Id());
//			pstmt.setString(2, grp_memVO.getMem_Id());
//			pstmt.setString(3, grp_memVO.getGrp_Leader());
//			
//			pstmt.executeUpdate();
//			
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. "
//					+ e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. "
//					+ se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//	}
//	@Override
//	public void update(Grp_memVO grp_memVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(UPDATE);
//			
//			pstmt.setString(1, grp_memVO.getGrp_Leader());
//			pstmt.setString(2, grp_memVO.getGrp_Id());
//			pstmt.setString(3, grp_memVO.getMem_Id());
//			
//			pstmt.executeUpdate();
//
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//	}
//	@Override
//	public void delete(String grp_Id, String mem_Id) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//
//			pstmt = con.prepareStatement(DELETE_GRP_MEM);
//
//			pstmt.setString(1, grp_Id);
//			pstmt.setString(2, mem_Id);
//
//			pstmt.executeUpdate();
//
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			if (con != null) {
//				try {
//					con.rollback();
//				} catch (SQLException excep) {
//					throw new RuntimeException("rollback error occured. " + excep.getMessage());
//				}
//			}
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//	}
//	@Override
//	public Grp_memVO findByPrimaryKey(String grp_Id, String mem_Id) {
//
//		Grp_memVO grp_memVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ONE);
//
//			pstmt.setString(1, grp_Id);
//			pstmt.setString(2, mem_Id);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				grp_memVO = new Grp_memVO();
//				grp_memVO.setGrp_Id(rs.getString("GRP_ID"));
//				grp_memVO.setMem_Id(rs.getString("MEM_ID"));
//				grp_memVO.setGrp_Leader(rs.getString("GRP_LEADER"));	
//			}
//
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//		return grp_memVO;
//	}
//		
//
//	@Override
//	public List<Grp_memVO> getAll() {
//		List<Grp_memVO> list = new ArrayList<Grp_memVO>();
//		Grp_memVO grp_memVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ALL);
//			
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				
//				grp_memVO = new Grp_memVO();
//				grp_memVO.setGrp_Id(rs.getString("GRP_ID"));
//				grp_memVO.setMem_Id(rs.getString("MEM_ID"));
//				grp_memVO.setGrp_Leader(rs.getString("GRP_LEADER"));
//				list.add(grp_memVO);
//			}
//			
//
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//		return list;
//	}
//	
//	public static void main(String[] args) throws IOException {
//
//		Grp_memJDBCDAO dao = new Grp_memJDBCDAO();
//		//insert
////		Grp_memVO grp_memVO = new Grp_memVO();
////		grp_memVO.setGrp_Id("GRP000005");
////		grp_memVO.setMem_Id("M000006");
////		grp_memVO.setGrp_Leader("1");
////		dao.insert(grp_memVO);
//		
//		//update
////		Grp_memVO grp_memVO = new Grp_memVO();
////		grp_memVO.setGrp_Id("GRP000005");
////		grp_memVO.setMem_Id("M000003");
////		grp_memVO.setGrp_Leader("1");
////		dao.update(grp_memVO);
//		
//		//delete()
////		dao.delete("GRP000005","M000003");
//		
//		//findByPrimaryKey() 
////		Grp_memVO grp_memVO = dao.findByPrimaryKey("GRP000002","M000002");
////		System.out.print(grp_memVO.getGrp_Id() + ", ");
////		System.out.print(grp_memVO.getMem_Id() + ", ");
////		System.out.print(grp_memVO.getGrp_Leader() +", ");
//		 
//		 //getALL()
//		List<Grp_memVO> list = dao.getAll(); for (Grp_memVO agrp_memVO : list) {
//		System.out.print(agrp_memVO.getGrp_Id() + ", ");
//		System.out.print(agrp_memVO.getMem_Id() + ", ");
//		System.out.print(agrp_memVO.getGrp_Leader() +", ");
//		}
//		
//		System.out.println("ok");
//	}
//}
