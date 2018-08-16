//package com.photo_wall.model;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.Reader;
//import java.io.StringReader;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//
//import com.grp_mem.model.Grp_memVO;
//
//public class Photo_wallJDBCDAO implements Photo_wallDAO_interface {
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String userid = "CA102G4";
//	String passwd = "12345678";
//	
//	private static final String INSERT_STMT =
//			"Insert into PHOTO_WALL (PHOTO_NO,MEM_ID,PO_TIME,PHOTO,PHOTO_STA) VALUES (?,?,?,?,?)";
//	private static final String UPDATE = 
//			"UPDATE PHOTO_WALL SET MEM_ID=?,PO_TIME=?,PHOTO=?,PHOTO_STA=? WHERE PHOTO_NO = ?";
//	private static final String DELETE_PHOTO_WALL = 
//			"DELETE FROM PHOTO_WALL WHERE PHOTO_NO = ?";
//	private static final String GET_ONE =
//			"SELECT * FROM PHOTO_WALL WHERE PHOTO_NO = ?";
//	private static final String GET_ALL =
//			"SELECT * FROM PHOTO_WALL ORDER BY PHOTO_NO";
//	
//	
//	@Override
//	public void insert(Photo_wallVO photo_wallVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(INSERT_STMT);
//			
//			pstmt.setString(1, photo_wallVO.getPhoto_No());
//			pstmt.setString(2, photo_wallVO.getMem_Id());
//			pstmt.setTimestamp(3, photo_wallVO.getPo_Time());
//			pstmt.setBytes(4, photo_wallVO.getPhoto());
//			pstmt.setInt(5, photo_wallVO.getPhoto_Sta());
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
//	
//	@Override
//	public void update(Photo_wallVO photo_wallVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(UPDATE);
//			
//			pstmt.setString(1, photo_wallVO.getMem_Id());
//			pstmt.setTimestamp(2, photo_wallVO.getPo_Time());
//			pstmt.setBytes(3, photo_wallVO.getPhoto());
//			pstmt.setInt(4, photo_wallVO.getPhoto_Sta());
//			pstmt.setString(5, photo_wallVO.getPhoto_No());
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
//	
//	@Override
//	public void delete(String photo_No) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//
//			pstmt = con.prepareStatement(DELETE_PHOTO_WALL);
//
//			pstmt.setString(1, photo_No);
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
//	public Photo_wallVO findByPrimaryKey(String photo_No) {
//		Photo_wallVO photo_wallVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ONE);
//
//			pstmt.setString(1, photo_No);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				photo_wallVO = new Photo_wallVO();
//				photo_wallVO.setPhoto_No(rs.getString("PHOTO_NO"));
//				photo_wallVO.setMem_Id(rs.getString("MEM_ID"));
//				photo_wallVO.setPo_Time(rs.getTimestamp("PO_TIME"));
//				photo_wallVO.setPhoto(rs.getBytes("PHOTO"));
//				photo_wallVO.setPhoto_Sta(rs.getInt("PHOTO_STA"));
//
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
//		return photo_wallVO;
//	}
//	@Override
//	public List<Photo_wallVO> getAll() {
//		List<Photo_wallVO> list = new ArrayList<Photo_wallVO>();
//		Photo_wallVO photo_wallVO = null;
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
//				photo_wallVO = new Photo_wallVO();
//				photo_wallVO.setPhoto_No(rs.getString("PHOTO_NO"));
//				photo_wallVO.setMem_Id(rs.getString("MEM_ID"));
//				photo_wallVO.setPo_Time(rs.getTimestamp("PO_TIME"));
//				photo_wallVO.setPhoto(rs.getBytes("PHOTO"));
//				photo_wallVO.setPhoto_Sta(rs.getInt("PHOTO_STA"));
//				String photoEncoded = Base64.getEncoder().encodeToString(rs.getBytes("PHOTO"));
//				photo_wallVO.setEncoded(photoEncoded);
//				list.add(photo_wallVO);
//
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
//		return list;
//	}
//	
//	public Reader stringToReader(String text) {
//		return new StringReader(text);
//	}
//
//	public String readerToString(Reader reader) {
//		int i;
//		StringBuilder sb = new StringBuilder();
//		try {
//			while ((i = reader.read()) != -1) {
//				sb.append((char) i);
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return sb.toString();
//	}
//
//	public static byte[] getPictureByteArray(String path) throws IOException {
//		File file = new File(path);
//		FileInputStream fis = new FileInputStream(file);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		byte[] buffer = new byte[8192];
//		int i;
//		while ((i = fis.read(buffer)) != -1) {
//			baos.write(buffer, 0, i);
//		}
//		baos.close();
//		fis.close();
//
//		return baos.toByteArray();
//	}
//	
//	
//	public static void main(String[] args) throws IOException {
//
//		Photo_wallJDBCDAO dao = new Photo_wallJDBCDAO();
//		//insert
////		Photo_WallVO photo_wallVO = new Photo_WallVO();
////		photo_wallVO.setPhoto_No("P000011");
////		photo_wallVO.setMem_Id("M000010");
////		photo_wallVO.setPo_Time(Timestamp.valueOf("2018-8-20 10:10:00"));
////		photo_wallVO.setPhoto(getPictureByteArray("C:\\Users\\yuan\\Desktop\\pic\\Standing_cat.jpg"));
////		photo_wallVO.setPhoto_Sta(1);
////		dao.insert(photo_wallVO);
//		
//		//update
////		Photo_WallVO photo_wallVO = new Photo_WallVO();
////		photo_wallVO.setPhoto_No("P000022");
////		photo_wallVO.setMem_Id("M000008");
////		photo_wallVO.setPo_Time(Timestamp.valueOf("2018-8-09 12:13:14"));
////		photo_wallVO.setPhoto(getPictureByteArray("C:\\Users\\yuan\\Desktop\\pic\\Standing_cat.jpg"));
////		photo_wallVO.setPhoto_Sta(2);
////		dao.update(photo_wallVO);
//		
//		//delete()
////		dao.delete("P000011");
//		
//		//findByPrimaryKey() 
////		Photo_wallVO photo_wallVO = dao.findByPrimaryKey("P000001");
////		System.out.print(photo_wallVO.getPhoto_No() + ", ");
////		System.out.print(photo_wallVO.getMem_Id() + ", ");
////		System.out.print(photo_wallVO.getPo_Time() +", ");
////		System.out.print(photo_wallVO.getPhoto() +", ");
////		System.out.print(photo_wallVO.getPhoto_Sta() +", ");
//		
//		
//		//getALL()
//		List<Photo_wallVO> list = dao.getAll(); for (Photo_wallVO aphoto_wallVO : list) {
////		System.out.print(aphoto_wallVO.getPhoto_No() + ", ");
////		System.out.print(aphoto_wallVO.getMem_Id() + ", ");
////		System.out.print(aphoto_wallVO.getPo_Time() +", ");
////		System.out.print(aphoto_wallVO.getPhoto() +", ");
////		System.out.print(aphoto_wallVO.getPhoto_Sta() +", ");
//		System.out.println(aphoto_wallVO.getEncoded());
//				}
//		
//		
//		System.out.println("ok");
//
//	}
//
//
//}
