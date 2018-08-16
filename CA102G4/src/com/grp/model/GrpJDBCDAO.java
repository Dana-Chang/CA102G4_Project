//package com.grp.model;
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
//import java.util.List;
//
//import com.mem.model.MemberVO;
//
//
//	
//	public class GrpJDBCDAO implements GrpDAO_interface {
//		String driver = "oracle.jdbc.driver.OracleDriver";
//		String url = "jdbc:oracle:thin:@localhost:1521:XE";
//		String userid = "CA102G4";
//		String passwd = "12345678";
//		
//		private static final String INSERT_STMT =
//				"Insert into GRP (GRP_ID,MEM_ID,GRP_START,GRP_END,GRP_CNT,GRP_ACPT,TRIP_NO,TRIP_START,TRIP_END,TRIP_LOCALE,TRIP_DETAILS,TRIP_PHOTO,GRP_STATUS,CHATROOM_ID) "
//				+ "VALUES ('GRP'||LPAD(to_char(GRP_seq.NEXTVAL), 6, '0'),?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		private static final String UPDATE =
//				"UPDATE GRP SET MEM_ID=?,GRP_START=?,GRP_END=?,GRP_CNT=?,GRP_ACPT=?,TRIP_NO=?,TRIP_START=?,TRIP_END=?,TRIP_LOCALE=?,TRIP_DETAILS=?,TRIP_PHOTO=?,GRP_STATUS=?,CHATROOM_ID=? WHERE GRP_ID =? ";
//		private static final String DELETE_GRP =
//				"DELETE FROM GRP WHERE GRP_ID = ?";
//		private static final String GET_ONE_STMT = 
//				"SELECT * FROM GRP WHERE GRP_ID = ?";
//		private static final String GET_ALL = 
//				"SELECT * FROM GRP ORDER BY GRP_ID";
//		
//		
//		@Override
//		public void insert(GrpVO grpVO) {
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			
//			try {
//				Class.forName(driver);
//				
//				con = DriverManager.getConnection(url, userid, passwd);
//				pstmt = con.prepareStatement(INSERT_STMT);
//				
//				pstmt.setString(1, grpVO.getMem_Id());
//				pstmt.setTimestamp(2, grpVO.getGrp_Start());
//				pstmt.setTimestamp(3, grpVO.getGrp_End());
//				pstmt.setInt(4, grpVO.getGrp_Cnt());
//				pstmt.setInt(5, grpVO.getGrp_Acpt());
//				pstmt.setString(6, grpVO.getTrip_No());
//				pstmt.setTimestamp(7, grpVO.getTrip_Start());
//				pstmt.setTimestamp(8, grpVO.getTrip_End());
//				pstmt.setString(9, grpVO.getTrip_Locale());
//				pstmt.setString(10, grpVO.getTrip_Details());
//				pstmt.setBytes(11, grpVO.getTrip_Photo());
//				pstmt.setInt(12, grpVO.getGrp_Status());
//				pstmt.setString(13, grpVO.getChatroom_Id());
//				
//				pstmt.executeUpdate();
//				
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException("Couldn't load database driver. "
//						+ e.getMessage());
//				// Handle any SQL errors
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. "
//						+ se.getMessage());
//				// Clean up JDBC resources
//			} finally {
//				if (pstmt != null) {
//					try {
//						pstmt.close();
//					} catch (SQLException se) {
//						se.printStackTrace(System.err);
//					}
//				}
//				if (con != null) {
//					try {
//						con.close();
//					} catch (Exception e) {
//						e.printStackTrace(System.err);
//					}
//				}
//			}
//		}
//		
//		@Override
//		public void update(GrpVO grpVO) {
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			
//			try {
//				Class.forName(driver);
//				
//				con = DriverManager.getConnection(url, userid, passwd);
//				pstmt = con.prepareStatement(UPDATE);
//				
//				pstmt.setString(1, grpVO.getMem_Id());
//				pstmt.setTimestamp(2, grpVO.getGrp_Start());
//				pstmt.setTimestamp(3, grpVO.getGrp_End());
//				pstmt.setInt(4, grpVO.getGrp_Cnt());
//				pstmt.setInt(5, grpVO.getGrp_Acpt());
//				pstmt.setString(6, grpVO.getTrip_No());
//				pstmt.setTimestamp(7, grpVO.getTrip_Start());
//				pstmt.setTimestamp(8, grpVO.getTrip_End());
//				pstmt.setString(9, grpVO.getTrip_Locale());
//				pstmt.setString(10, grpVO.getTrip_Details());
//				pstmt.setBytes(11, grpVO.getTrip_Photo());
//				pstmt.setInt(12, grpVO.getGrp_Status());
//				pstmt.setString(13, grpVO.getChatroom_Id());
//				pstmt.setString(14, grpVO.getGrp_Id());
//				pstmt.executeUpdate();
//				
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException("Couldn't load database driver. "
//						+ e.getMessage());
//				// Handle any SQL errors
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. "
//						+ se.getMessage());
//				// Clean up JDBC resources
//			} finally {
//				if (pstmt != null) {
//					try {
//						pstmt.close();
//					} catch (SQLException se) {
//						se.printStackTrace(System.err);
//					}
//				}
//				if (con != null) {
//					try {
//						con.close();
//					} catch (Exception e) {
//						e.printStackTrace(System.err);
//					}
//				}
//			}
//		}
//
//		@Override
//		public void delete(String grp_Id) {
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			
//			try {
//				Class.forName(driver);
//				
//				con = DriverManager.getConnection(url, userid, passwd);
//				
//				con.setAutoCommit(false);
//				
//				pstmt = con.prepareStatement(DELETE_GRP);
//
//				pstmt.setString(1, grp_Id);
//
//				pstmt.executeUpdate();
//
//				con.commit();
//				con.setAutoCommit(true);
//				
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//				// Handle any SQL errors
//			} catch (SQLException se) {
//				if (con != null) {
//					try {
//						con.rollback();
//					} catch (SQLException excep) {
//						throw new RuntimeException("rollback error occured. " + excep.getMessage());
//					}
//				}
//				throw new RuntimeException("A database error occured. " + se.getMessage());
//				// Clean up JDBC resources
//			} finally {
//				if (pstmt != null) {
//					try {
//						pstmt.close();
//					} catch (SQLException se) {
//						se.printStackTrace(System.err);
//					}
//				}
//				if (con != null) {
//					try {
//						con.close();
//					} catch (Exception e) {
//						e.printStackTrace(System.err);
//					}
//				}
//			}
//		}
//		
//		
//
//		@Override
//		public GrpVO findByPrimaryKey(String grp_Id) {
//			
//			GrpVO grpVO = null;
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//			
//			try {
//				Class.forName(driver);
//				con = DriverManager.getConnection(url, userid, passwd);
//				pstmt = con.prepareStatement(GET_ONE_STMT);
//
//				pstmt.setString(1, grp_Id);
//
//				rs = pstmt.executeQuery();
//
//				while (rs.next()) {
//					
//					grpVO = new GrpVO();
//					grpVO.setGrp_Id(rs.getString("GRP_ID"));
//					grpVO.setGrp_Start(rs.getTimestamp("GRP_START"));
//					grpVO.setGrp_End(rs.getTimestamp("GRP_END"));
//					grpVO.setGrp_Cnt(rs.getInt("GRP_CNT"));
//					grpVO.setGrp_Acpt(rs.getInt("GRP_ACPT"));
//					grpVO.setTrip_No(rs.getString("TRIP_NO"));
//					grpVO.setTrip_Start(rs.getTimestamp("TRIP_START"));
//					grpVO.setTrip_End(rs.getTimestamp("TRIP_END"));
//					grpVO.setTrip_Locale(rs.getString("TRIP_LOCALE"));
//					grpVO.setTrip_Details(rs.getString("TRIP_DETAILS"));
//					grpVO.setTrip_Photo(rs.getBytes("TRIP_PHOTO"));
//					grpVO.setGrp_Status(rs.getInt("GRP_STATUS"));
//					grpVO.setChatroom_Id(rs.getString("CHATROOM_ID"));
//				}
//				
//
//				// Handle any driver errors
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//				// Handle any SQL errors
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. " + se.getMessage());
//				// Clean up JDBC resources
//			} finally {
//				if (rs != null) {
//					try {
//						rs.close();
//					} catch (SQLException se) {
//						se.printStackTrace(System.err);
//					}
//				}
//				if (pstmt != null) {
//					try {
//						pstmt.close();
//					} catch (SQLException se) {
//						se.printStackTrace(System.err);
//					}
//				}
//				if (con != null) {
//					try {
//						con.close();
//					} catch (Exception e) {
//						e.printStackTrace(System.err);
//					}
//				}
//			}
//			return grpVO;
//		}
//
//		@Override
//		public List<GrpVO> getAll() {
//		List<GrpVO> list = new ArrayList<GrpVO>();
//		GrpVO grpVO = null;
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
//				grpVO = new GrpVO();
//				grpVO.setGrp_Id(rs.getString("GRP_ID"));
//				grpVO.setGrp_Start(rs.getTimestamp("GRP_START"));
//				grpVO.setGrp_End(rs.getTimestamp("GRP_END"));
//				grpVO.setGrp_Cnt(rs.getInt("GRP_CNT"));
//				grpVO.setGrp_Acpt(rs.getInt("GRP_ACPT"));
//				grpVO.setTrip_No(rs.getString("TRIP_NO"));
//				grpVO.setTrip_Start(rs.getTimestamp("TRIP_START"));
//				grpVO.setTrip_End(rs.getTimestamp("TRIP_END"));
//				grpVO.setTrip_Locale(rs.getString("TRIP_LOCALE"));
//				grpVO.setTrip_Details(rs.getString("TRIP_DETAILS"));
//				grpVO.setTrip_Photo(rs.getBytes("TRIP_PHOTO"));
//				grpVO.setGrp_Status(rs.getInt("GRP_STATUS"));
//				grpVO.setChatroom_Id(rs.getString("CHATROOM_ID"));
//				list.add(grpVO);
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
//		
//		
//		public Reader stringToReader(String text) {
//			return new StringReader(text);
//		}
//
//		public String readerToString(Reader reader) {
//			int i;
//			StringBuilder sb = new StringBuilder();
//			try {
//				while ((i = reader.read()) != -1) {
//					sb.append((char) i);
//				}
//				reader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return sb.toString();
//		}
//
//		public static byte[] getPictureByteArray(String path) throws IOException {
//			File file = new File(path);
//			FileInputStream fis = new FileInputStream(file);
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			byte[] buffer = new byte[8192];
//			int i;
//			while ((i = fis.read(buffer)) != -1) {
//				baos.write(buffer, 0, i);
//			}
//			baos.close();
//			fis.close();
//
//			return baos.toByteArray();
//		}
//		
//		public static void main(String[] args) throws IOException {
//
//		GrpJDBCDAO dao = new GrpJDBCDAO();
//		
//		//insert
////		GrpVO grpVO = new GrpVO();
////		grpVO.setMem_Id("M000001");
////		grpVO.setGrp_Start(Timestamp.valueOf("2017-8-26 10:15:30"));
////		grpVO.setGrp_End(Timestamp.valueOf("2017-8-27 12:55:40"));
////		grpVO.setGrp_Cnt(3);
////		grpVO.setGrp_Acpt(3);
////		grpVO.setTrip_No("T000000001");
////		grpVO.setTrip_Start(Timestamp.valueOf("2017-7-15 10:00:00"));
////		grpVO.setTrip_End(Timestamp.valueOf("2017-7-15 10:00:00"));
////		grpVO.setTrip_Locale("台北");
////		grpVO.setTrip_Details("基隆鐵支路好好玩");
////		grpVO.setTrip_Photo(getPictureByteArray("C:\\Users\\yuan\\Desktop\\pic\\orange_cat.jpg"));
////		grpVO.setGrp_Status(1);
////		grpVO.setChatroom_Id("CR000002");
////		dao.insert(grpVO);
//		
//		//update
////		GrpVO grpVO = new GrpVO();
////		grpVO.setMem_Id("M000010");
////		grpVO.setGrp_Start(Timestamp.valueOf("2018-3-26 10:15:30"));
////		grpVO.setGrp_End(Timestamp.valueOf("2018-3-27 12:55:40"));
////		grpVO.setGrp_Cnt(2);
////		grpVO.setGrp_Acpt(1);
////		grpVO.setTrip_No("T000000002");
////		grpVO.setTrip_Start(Timestamp.valueOf("2018-3-15 10:00:00"));
////		grpVO.setTrip_End(Timestamp.valueOf("2018-3-15 10:00:00"));
////		grpVO.setTrip_Locale("南投");
////		grpVO.setTrip_Details("南投鐵支路好好玩");
////		grpVO.setTrip_Photo(getPictureByteArray("C:\\Users\\yuan\\Desktop\\pic\\orange_cat.jpg"));
////		grpVO.setGrp_Status(2);
////		grpVO.setChatroom_Id("CR000001");
////		grpVO.setGrp_Id("GRP000005");
////		dao.update(grpVO);
//		
//		//delete
////		dao.delete("GRP000022");
//		
//		//findByPrimaryKey() 
////		 GrpVO grpVO = dao.findByPrimaryKey("GRP000005");
////		 System.out.print(grpVO.getGrp_Id() + ", ");
////		 System.out.print(grpVO.getGrp_Start() + ", ");
////		 System.out.print(grpVO.getGrp_End() +", ");
////		 System.out.print(grpVO.getGrp_Cnt() +", ");
////		 System.out.print(grpVO.getGrp_Acpt() +", ");
////		 System.out.print(grpVO.getTrip_No() +", ");
////		 System.out.print(grpVO.getTrip_Start() +", ");
////		 System.out.print(grpVO.getTrip_End() +", ");
////		 System.out.print(grpVO.getTrip_Locale() +", ");
////		 System.out.print(grpVO.getTrip_Details() +", ");
////		 System.out.print(grpVO.getTrip_Photo() +", ");
////		 System.out.print(grpVO.getGrp_Status() +", ");
////		 System.out.print(grpVO.getChatroom_Id() +", ");
//		
//		//getALL()
//		List<GrpVO> list = dao.getAll(); for (GrpVO agrpVO : list) {
//		 System.out.print(agrpVO.getGrp_Id() + ", ");
//		 System.out.print(agrpVO.getGrp_Start() + ", ");
//		 System.out.print(agrpVO.getGrp_End() +", ");
//		 System.out.print(agrpVO.getGrp_Cnt() +", ");
//		 System.out.print(agrpVO.getGrp_Acpt() +", ");
//		 System.out.print(agrpVO.getTrip_No() +", ");
//		 System.out.print(agrpVO.getTrip_Start() +", ");
//		 System.out.print(agrpVO.getTrip_End() +", ");
//		 System.out.print(agrpVO.getTrip_Locale() +", ");
//		 System.out.print(agrpVO.getTrip_Details() +", ");
//		 System.out.print(agrpVO.getTrip_Photo() +", ");
//		 System.out.print(agrpVO.getGrp_Status() +", ");
//		 System.out.print(agrpVO.getChatroom_Id() +", ");
//		}
//		 
//		System.out.println("OK");
//		}
//		
//		}
//
//
