//package com.mem.model;
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
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import com.grp.model.GrpVO;
//import com.grp_com.model.Grp_comVO;
//import com.grp_mem.model.Grp_memVO;
//import com.grp_report.model.Grp_reportVO;
//import com.photo.model.Photo_wallVO;
//import com.photo_report.model.Photo_reportVO;
//
//public class MemberJDBCDAO implements MemberDAO_interface {
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String userid = "CA102G4";
//	String passwd = "12345678";
//
//	// private static final String REGIGST_STMT =
//	// "Insert into MEMBER (MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME) "
//	// + "VALUES ('M'||LPAD(to_char(MEMBER_seq.NEXTVAL), 6, '0'),?,?,?)";
//
//	private static final String INSERT_STMT = "Insert into MEMBER (MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME,MEM_STATE) "
//			+ "VALUES ('M'||LPAD(to_char(MEMBER_seq.NEXTVAL), 6, '0'),?,?,?,?)";
//
//	private static final String UPDATE = "UPDATE MEMBER SET MEM_ACCOUNT=?,MEM_PASSWORD= ?, MEM_NAME= ? ,MEM_SEX= ?,MEM_ADDRESS= ?,MEM_BIRTHDAY= ?,MEM_PHONE= ?,MEM_PROFILE= ?,MEM_PHOTO= ?,MEM_STATE= ?,DELIVERY_ADDRESS_1= ?,DELIVERY_ADDRESS_2=?,DELIVERY_ADDRESS_3=?,STORE_ADDR_1=?,STORE_ADDR_2=?,STORE_ADDR_3=?,STORE_NAME_1=?,STORE_NAME_2=?,STORE_NAME_3=?,STORE_NO_1=?,STORE_NO_2=?,STORE_NO_3=? WHERE MEM_ID = ?";
//
//	private static final String GET_ALL_STMT = "SELECT MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME,MEM_SEX,MEM_ADDRESS,to_char(MEM_BIRTHDAY,'yyyy-mm-dd') MEM_BIRTHDAY,MEM_PHONE,MEM_PROFILE,MEM_PHOTO,MEM_STATE,DELIVERY_ADDRESS_1,DELIVERY_ADDRESS_2,DELIVERY_ADDRESS_3,STORE_ADDR_1,STORE_ADDR_2,STORE_ADDR_3,STORE_NAME_1,STORE_NAME_2,STORE_NAME_3,STORE_NO_1,STORE_NO_2,STORE_NO_3 FROM MEMBER ORDER BY MEM_ID";
//	private static final String GET_ONE_STMT = "SELECT MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME,MEM_SEX,MEM_ADDRESS,to_char(MEM_BIRTHDAY,'yyyy-mm-dd') MEM_BIRTHDAY,MEM_PHONE,MEM_PROFILE,MEM_PHOTO,MEM_STATE,DELIVERY_ADDRESS_1,DELIVERY_ADDRESS_2,DELIVERY_ADDRESS_3,STORE_ADDR_1,STORE_ADDR_2,STORE_ADDR_3,STORE_NAME_1,STORE_NAME_2,STORE_NAME_3,STORE_NO_1,STORE_NO_2,STORE_NO_3 FROM MEMBER WHERE MEM_ID = ?";
//	private static final String DELETE_MEMBER = "DELETE FROM MEMBER WHERE MEM_ID = ?";
//
//	private static final String login_Member = "SELECT * FROM MEMBER WHERE MEM_ACCOUNT= ? AND MEM_PASSWORD = ?";
//
//	private static final String update_Member = 
//			"UPDATE MEMBER SET MEM_NAME= ? , MEM_PHONE= ? ,MEM_SEX= ?,MEM_BIRTHDAY= ?,MEM_PHOTO= ?,MEM_PROFILE= ? WHERE MEM_ID = ?";
//
//	// SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
//	// java.util.Date date = new java.util.Date();
//	// String strDate = sdFormat.format(date);
//	// liveVO1.setFb_order_time(java.sql.Timestamp.valueOf(strDate));
//	// memberVO.setMem_Reg(java.sql.Date.valueOf(strDate));
//
//	@Override
//	public void insert(MemberVO memberVO) {
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			Class.forName(driver);
//
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(INSERT_STMT);
//
//			pstmt.setString(1, memberVO.getMem_Account());
//			pstmt.setString(2, memberVO.getMem_Password());
//			pstmt.setString(3, memberVO.getMem_Name());
//			pstmt.setInt(4, 3);
//			pstmt.executeUpdate();
//
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
//
//	@Override
//	public void update(MemberVO memberVO) {
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(UPDATE);
//
//			pstmt.setString(1, memberVO.getMem_Account());
//			pstmt.setString(2, memberVO.getMem_Password());
//			pstmt.setString(3, memberVO.getMem_Name());
//			pstmt.setInt(4, memberVO.getMem_Sex());
//			pstmt.setString(5, memberVO.getMem_Address());
//			pstmt.setDate(6, memberVO.getMem_Birthday());
//			pstmt.setString(7, memberVO.getMem_Phone());
//			pstmt.setString(8, memberVO.getMem_Profile());
//			pstmt.setBytes(9, memberVO.getMem_Photo());
//			pstmt.setInt(10, memberVO.getMem_State());
//			pstmt.setString(11, memberVO.getDelivery_Address_1());
//			pstmt.setString(12, memberVO.getDelivery_Address_2());
//			pstmt.setString(13, memberVO.getDelivery_Address_3());
//			pstmt.setString(14, memberVO.getSTORE_ADDR_1());
//			pstmt.setString(15, memberVO.getSTORE_ADDR_2());
//			pstmt.setString(16, memberVO.getSTORE_ADDR_3());
//			pstmt.setString(17, memberVO.getSTORE_NAME_1());
//			pstmt.setString(18, memberVO.getSTORE_NAME_2());
//			pstmt.setString(19, memberVO.getSTORE_NAME_3());
//			pstmt.setInt(20, memberVO.getSTORE_NO_1());
//			pstmt.setInt(21, memberVO.getSTORE_NO_2());
//			pstmt.setInt(22, memberVO.getSTORE_NO_3());
//			pstmt.setString(23, memberVO.getMem_Id());
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
//
//	@Override
//	public void update_Member(MemberVO memberVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(update_Member);
//
//			pstmt.setString(1, memberVO.getMem_Name());
//			pstmt.setString(2, memberVO.getMem_Phone());
//			pstmt.setInt(3, memberVO.getMem_Sex());
//			pstmt.setDate(4, memberVO.getMem_Birthday());
//			pstmt.setString(5, memberVO.getMem_Profile());
//			pstmt.setBytes(6, memberVO.getMem_Photo());
//			pstmt.setString(7, memberVO.getMem_Id());
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
//
//	@Override
//	public void delete(String mem_Id) {
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			Class.forName(driver);
//
//			con = DriverManager.getConnection(url, userid, passwd);
//
//			con.setAutoCommit(false);
//
//			pstmt = con.prepareStatement(DELETE_MEMBER);
//
//			pstmt.setString(1, mem_Id);
//
//			pstmt.executeUpdate();
//
//			con.commit();
//			con.setAutoCommit(true);
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
//
//	@Override
//	public MemberVO findByPrimaryKey(String mem_Id) {
//
//		MemberVO memberVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ONE_STMT);
//
//			pstmt.setString(1, mem_Id);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				memberVO = new MemberVO();
//				memberVO.setMem_Id(rs.getString("MEM_ID"));
//				memberVO.setMem_Account(rs.getString("MEM_ACCOUNT"));
//				memberVO.setMem_Password(rs.getString("MEM_PASSWORD"));
//				memberVO.setMem_Name(rs.getString("MEM_NAME"));
//				memberVO.setMem_Sex(rs.getInt("MEM_SEX"));
//				memberVO.setMem_Address(rs.getString("MEM_ADDRESS"));
//				memberVO.setMem_Birthday(rs.getDate("MEM_BIRTHDAY"));
//				memberVO.setMem_Phone(rs.getString("MEM_PHONE"));
//				memberVO.setMem_Profile(rs.getString("MEM_PROFILE"));
//				memberVO.setMem_Photo(rs.getBytes("MEM_PHOTO"));
//				memberVO.setMem_State(rs.getInt("MEM_STATE"));
//
//				memberVO.setDelivery_Address_1(rs.getString("DELIVERY_ADDRESS_1"));
//				memberVO.setDelivery_Address_2(rs.getString("DELIVERY_ADDRESS_2"));
//				memberVO.setDelivery_Address_3(rs.getString("DELIVERY_ADDRESS_3"));
//
//				memberVO.setSTORE_ADDR_1(rs.getString("STORE_ADDR_1"));
//				memberVO.setSTORE_ADDR_2(rs.getString("STORE_ADDR_2"));
//				memberVO.setSTORE_ADDR_3(rs.getString("STORE_ADDR_3"));
//
//				memberVO.setSTORE_NAME_1(rs.getString("STORE_NAME_1"));
//				memberVO.setSTORE_NAME_2(rs.getString("STORE_NAME_2"));
//				memberVO.setSTORE_NAME_3(rs.getString("STORE_NAME_3"));
//
//				memberVO.setSTORE_NO_1(rs.getInt("STORE_NO_1"));
//				memberVO.setSTORE_NO_2(rs.getInt("STORE_NO_2"));
//				memberVO.setSTORE_NO_3(rs.getInt("STORE_NO_3"));
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
//		return memberVO;
//	}
//
//	@Override
//	public List<MemberVO> getAll() {
//		List<MemberVO> list = new ArrayList<MemberVO>();
//		MemberVO memberVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ALL_STMT);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				memberVO = new MemberVO();
//				memberVO.setMem_Id(rs.getString("MEM_ID"));
//				memberVO.setMem_Account(rs.getString("MEM_ACCOUNT"));
//				memberVO.setMem_Password(rs.getString("MEM_PASSWORD"));
//				memberVO.setMem_Name(rs.getString("MEM_NAME"));
//				memberVO.setMem_Sex(rs.getInt("MEM_SEX"));
//				memberVO.setMem_Address(rs.getString("MEM_ADDRESS"));
//				memberVO.setMem_Birthday(rs.getDate("MEM_BIRTHDAY"));
//				memberVO.setMem_Phone(rs.getString("MEM_PHONE"));
//				memberVO.setMem_Profile(rs.getString("MEM_PROFILE"));
//				memberVO.setMem_Photo(rs.getBytes("MEM_PHOTO"));
//				memberVO.setMem_State(rs.getInt("MEM_STATE"));
//
//				memberVO.setDelivery_Address_1(rs.getString("DELIVERY_ADDRESS_1"));
//				memberVO.setDelivery_Address_2(rs.getString("DELIVERY_ADDRESS_2"));
//				memberVO.setDelivery_Address_3(rs.getString("DELIVERY_ADDRESS_3"));
//
//				memberVO.setSTORE_ADDR_1(rs.getString("STORE_ADDR_1"));
//				memberVO.setSTORE_ADDR_2(rs.getString("STORE_ADDR_2"));
//				memberVO.setSTORE_ADDR_3(rs.getString("STORE_ADDR_3"));
//
//				memberVO.setSTORE_NAME_1(rs.getString("STORE_NAME_1"));
//				memberVO.setSTORE_NAME_2(rs.getString("STORE_NAME_2"));
//				memberVO.setSTORE_NAME_3(rs.getString("STORE_NAME_3"));
//
//				memberVO.setSTORE_NO_1(rs.getInt("STORE_NO_1"));
//				memberVO.setSTORE_NO_2(rs.getInt("STORE_NO_2"));
//				memberVO.setSTORE_NO_3(rs.getInt("STORE_NO_3"));
//				list.add(memberVO); // Store the row in the list
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
//	@Override
//	public MemberVO login_Member(String mem_Account, String mem_Password) {
//		MemberVO memberVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(login_Member);
//
//			pstmt.setString(1, mem_Account);
//			pstmt.setString(2, mem_Password);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				memberVO = new MemberVO();
//				memberVO.setMem_Account(rs.getString("MEM_ACCOUNT"));
//				memberVO.setMem_Password(rs.getString("MEM_PASSWORD"));
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
//		return memberVO;
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
//	public static void main(String[] args) throws IOException {
//
//		MemberJDBCDAO dao = new MemberJDBCDAO();
//		
//		
//
//		// insert
//		// MemberVO memberVO = new MemberVO();
//		// memberVO.setMem_Account("A123456");
//		// memberVO.setMem_Password("A123456");
//		// memberVO.setMem_Name("AAA");
//		// memberVO.setMem_State(1);
//		// dao.insert(memberVO);
//		
//		
//		
//		// update
//		MemberVO memberVO = new MemberVO();
//
//		memberVO.setMem_Name("KKK");
//		memberVO.setMem_Sex(0);
//		memberVO.setMem_Birthday(java.sql.Date.valueOf("2017-8-26"));
//		memberVO.setMem_Phone("145613789");
//		memberVO.setMem_Profile("123");
//		memberVO.setMem_Photo(getPictureByteArray("C:\\ca102g4_pics\\member\\puppuy.jpg"));
//		memberVO.setMem_Id("M000012");
//
//		dao.update_Member(memberVO);
//		
//		//
//		// update
////		 MemberVO memberVO = new MemberVO();
////		 memberVO.setMem_Account("A789100");
////		 memberVO.setMem_Password("A55555");
////		 memberVO.setMem_Name("bbb");
////		 memberVO.setMem_Sex(0);
////		 memberVO.setMem_Address("ADDDDDDDDDDDDDDDDDDDDDDC");
////		 memberVO.setMem_Birthday(java.sql.Date.valueOf("2017-8-26"));
////		 memberVO.setMem_Phone("145613789");
////		 memberVO.setMem_Profile("123");
////		 memberVO.setMem_Photo(getPictureByteArray("C:\\ca102g4_pics\\member\\puppuy.jpg"));
////		 memberVO.setMem_State(1);
////		 memberVO.setDelivery_Address_1("桃園市-中壢區-中大路111號-1");
////		 memberVO.setDelivery_Address_2("桃園市-中壢區-中大路222號-2");
////		 memberVO.setDelivery_Address_3("桃園市-中壢區-中大路333號-3");
////		 memberVO.setSTORE_ADDR_1("桃園市-中壢區-中大路333號-1");
////		 memberVO.setSTORE_ADDR_2("桃園市-中壢區-中大路666號-2");
////		 memberVO.setSTORE_ADDR_3("桃園市-中壢區-中大路999號-3");
////		 memberVO.setSTORE_NAME_1("桃園店");
////		 memberVO.setSTORE_NAME_2("中壢店");
////		 memberVO.setSTORE_NAME_3("內壢店");
////		 memberVO.setSTORE_NO_1(555);
////		 memberVO.setSTORE_NO_2(666);
////		 memberVO.setSTORE_NO_3(777);
////		 memberVO.setMem_Id("M000010");
////		 dao.update(memberVO);
//
//		// delete()
//		// dao.delete("M000012");
//
//		// findByPrimaryKey()
//		// MemberVO memberVO = dao.findByPrimaryKey("M000011");
//		// System.out.print(memberVO.getMem_Id() + ", ");
//		// System.out.print(memberVO.getMem_Account() + ", ");
//		// System.out.print(memberVO.getMem_Password() +", ");
//		// System.out.print(memberVO.getMem_Name() +", ");
//		// System.out.print(memberVO.getMem_Sex() +", ");
//		// System.out.print(memberVO.getMem_Address() +", ");
//		// System.out.print(memberVO.getMem_Birthday() +", ");
//		// System.out.print(memberVO.getMem_Phone() +", ");
//		// System.out.print(memberVO.getMem_Profile() +", ");
//		// System.out.print(memberVO.getMem_Photo() +", ");
//		// System.out.print(memberVO.getMem_State() +", ");
//		//
//		// System.out.print(memberVO.getDelivery_Address_1() +", ");
//		// System.out.print(memberVO.getDelivery_Address_2() +", ");
//		// System.out.print(memberVO.getDelivery_Address_3() +", ");
//		//
//		// System.out.print(memberVO.getSTORE_ADDR_1() +", ");
//		// System.out.print(memberVO.getSTORE_ADDR_2() +", ");
//		// System.out.print(memberVO.getSTORE_ADDR_3() +", ");
//		//
//		// System.out.print(memberVO.getSTORE_NAME_1() +", ");
//		// System.out.print(memberVO.getSTORE_NAME_2() +", ");
//		// System.out.print(memberVO.getSTORE_NAME_3() +", ");
//		//
//		// System.out.print(memberVO.getSTORE_NO_1() +", ");
//		// System.out.print(memberVO.getSTORE_NO_2() +", ");
//		// System.out.print(memberVO.getSTORE_NO_3() +", ");
//
//		// getAll()
//		// List<MemberVO> list = dao.getAll(); for (MemberVO aMemberVO : list) {
//		// System.out.print(aMemberVO.getMem_Id() + ", ");
//		// System.out.print(aMemberVO.getMem_Account() + ", ");
//		// System.out.print(aMemberVO.getMem_Password() +", ");
//		// System.out.print(aMemberVO.getMem_Name() +", ");
//		// System.out.print(aMemberVO.getMem_Sex() +", ");
//		// System.out.print(aMemberVO.getMem_Address() +", ");
//		// System.out.print(aMemberVO.getMem_Birthday() +", ");
//		// System.out.print(aMemberVO.getMem_Phone() +", ");
//		// System.out.print(aMemberVO.getMem_Profile() +", ");
//		// System.out.print(aMemberVO.getMem_Photo() +", ");
//		// System.out.print(aMemberVO.getMem_State() +", ");
//		//
//		// System.out.print(aMemberVO.getDelivery_Address_1() +", ");
//		// System.out.print(aMemberVO.getDelivery_Address_2() +", ");
//		// System.out.print(aMemberVO.getDelivery_Address_3() +", ");
//		//
//		// System.out.print(aMemberVO.getSTORE_ADDR_1() +", ");
//		// System.out.print(aMemberVO.getSTORE_ADDR_2() +", ");
//		// System.out.print(aMemberVO.getSTORE_ADDR_3() +", ");
//		//
//		// System.out.print(aMemberVO.getSTORE_NAME_1() +", ");
//		// System.out.print(aMemberVO.getSTORE_NAME_2() +", ");
//		// System.out.print(aMemberVO.getSTORE_NAME_3() +", ");
//		//
//		// System.out.print(aMemberVO.getSTORE_NO_1() +", ");
//		// System.out.print(aMemberVO.getSTORE_NO_2() +", ");
//		// System.out.print(aMemberVO.getSTORE_NO_3() +", ");
//		// System.out.println();
//		// }
//
//		// findByPrimaryKey()
//		// MemberVO memberVO = dao.login_Member("TORRES123@GMAIL.COM","123456");
//		// System.out.print(memberVO.getMem_Account() + ", ");
//		// System.out.print(memberVO.getMem_Password() +", ");
//		//
//		// System.out.println("OK");
//
//		
//
//	}
//
//}