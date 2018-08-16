package com.attractions.model;

import java.sql.*;
import java.util.*;

public class AttractionsJDBCDAO implements AttractionsDAO_interface {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	private static final String SQL_INSERT = "insert into ATTRACTIONS "
			+ "(ATT_NO,ATT_NAME,ATT_LAT,ATT_LON,COUNTRY,ADMINISTRATIVE_AREA,ATT_INFORMATION,ATT_PICTURE,ATT_ADDRESS,ATT_STATUS) "
			+ "values ('A'||LPAD(to_char(ATT_SEQ.NEXTVAL), 9, '0'),?,?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE = "update ATTRACTIONS set "
			+ "ATT_NAME = ?,ATT_LAT = ?,ATT_LON = ?,COUNTRY = ?,ADMINISTRATIVE_AREA = ?,ATT_INFORMATION = ?,ATT_PICTURE = ?,"
			+ "ATT_ADDRESS = ?,ATT_STATUS = ? where ATT_NO = ?";
	private static final String SQL_DELETE = "delete from ATTRACTIONS where ATT_NO = ?";
	//無圖片搜尋
	private static final String SQL_QUERY = "select ATT_NO,ATT_NAME,ATT_LAT,ATT_LON,COUNTRY,ADMINISTRATIVE_AREA,ATT_INFORMATION,ATT_ADDRESS,ATT_STATUS from ATTRACTIONS where ATT_NO = ?";
	private static final String SQL_QUERY_ALL = "select ATT_NO,ATT_NAME,ATT_LAT,ATT_LON,COUNTRY,ADMINISTRATIVE_AREA,ATT_INFORMATION,ATT_ADDRESS,ATT_STATUS from ATTRACTIONS";
//	//有圖片搜尋
//	private static final String SQL_QUERY = "select * from ATTRACTIONS where ATT_NO = ?";
//	private static final String SQL_QUERY_ALL = "select * from ATTRACTIONS";
	private static final String SQL_QUERY_PICTURE = "select ATT_PICTURE from ATTRACTIONS where ATT_NO = ?";
	//世銘打的
	private static final String SQL_QUERY_ALL_RANDOM = "SELECT * FROM(SELECT * FROM ATTRACTIONS ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM <=4 AND ATT_STATUS = 1";
	
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		}
	}
	
	@Override
	public int insert(AttractionsVO attVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_INSERT);
			pstmt.setString(1, attVO.getAtt_name());
			pstmt.setDouble(2, attVO.getAtt_lat());
			pstmt.setDouble(3, attVO.getAtt_lon());
			pstmt.setString(4, attVO.getCountry());
			pstmt.setString(5, attVO.getAdministrative_area());
			pstmt.setString(6, attVO.getAtt_information());
			pstmt.setBytes(7, attVO.getAtt_picture());
			pstmt.setString(8, attVO.getAtt_address());
			pstmt.setInt(9, attVO.getAtt_status());

			updateCount = pstmt.executeUpdate();
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public int update(AttractionsVO attVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_UPDATE);
			pstmt.setString(1, attVO.getAtt_name());
			pstmt.setDouble(2, attVO.getAtt_lat());
			pstmt.setDouble(3, attVO.getAtt_lon());
			pstmt.setString(4, attVO.getCountry());
			pstmt.setString(5, attVO.getAdministrative_area());
			pstmt.setString(6, attVO.getAtt_information());
			pstmt.setBytes(7, attVO.getAtt_picture());
			pstmt.setString(8, attVO.getAtt_address());
			pstmt.setInt(9, attVO.getAtt_status());
			pstmt.setString(10, attVO.getAtt_no());

			updateCount = pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public int delete(String att_no) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_DELETE);
			pstmt.setString(1, att_no);

			updateCount = pstmt.executeUpdate();
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public AttractionsVO findByPrimaryKey(String att_no) {
		AttractionsVO attVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY);
			pstmt.setString(1, att_no);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				attVO = new AttractionsVO();
				attVO.setAtt_no(rs.getString("ATT_NO"));
				attVO.setAtt_name(rs.getString("ATT_NAME"));
				attVO.setAtt_lat(rs.getDouble("ATT_LAT"));
				attVO.setAtt_lon(rs.getDouble("ATT_LON"));
				attVO.setCountry(rs.getString("COUNTRY"));
				attVO.setAdministrative_area(rs.getString("ADMINISTRATIVE_AREA"));
				attVO.setAtt_information(rs.getString("ATT_INFORMATION"));
//				attVO.setAtt_picture(rs.getBytes("ATT_PICTURE"));
				attVO.setAtt_address(rs.getString("ATT_ADDRESS"));
				attVO.setAtt_status(rs.getInt("ATT_STATUS"));
			}
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return attVO;
	}

	@Override
	public List<AttractionsVO> getAll() {
		List<AttractionsVO> list = new ArrayList<AttractionsVO>();
		AttractionsVO attVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_ALL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				attVO = new AttractionsVO();
				attVO.setAtt_no(rs.getString("ATT_NO"));
				attVO.setAtt_name(rs.getString("ATT_NAME"));
				attVO.setAtt_lat(rs.getDouble("ATT_LAT"));
				attVO.setAtt_lon(rs.getDouble("ATT_LON"));
				attVO.setCountry(rs.getString("COUNTRY"));
				attVO.setAdministrative_area(rs.getString("ADMINISTRATIVE_AREA"));
				attVO.setAtt_information(rs.getString("ATT_INFORMATION"));
//				attVO.setAtt_picture(rs.getBytes("ATT_PICTURE"));
				attVO.setAtt_address(rs.getString("ATT_ADDRESS"));
				attVO.setAtt_status(rs.getInt("ATT_STATUS"));
				list.add(attVO);
			}
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public List<AttractionsVO> getAll(Map<String, String[]> map) {
		List<AttractionsVO> list = new ArrayList<>();
		AttractionsVO attVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			String finalSQL = "select ATT_NO,ATT_NAME,ATT_LAT,ATT_LON,COUNTRY,ATT_INFORMATION,ATT_ADDRESS,ATT_STATUS from ATTRACTIONS "
					+ CompositeQuery_Att.get_WhereCondition(map)
					+ "order by ATT_NO";
			pstmt = con.prepareStatement(finalSQL);
			System.out.println("●●finalSQL(by DAO) = "+finalSQL);
			rs = pstmt.executeQuery();
			

			while (rs.next()) {
				attVO = new AttractionsVO();
				attVO.setAtt_no(rs.getString("ATT_NO"));
				attVO.setAtt_name(rs.getString("ATT_NAME"));
				attVO.setAtt_lat(rs.getDouble("ATT_LAT"));
				attVO.setAtt_lon(rs.getDouble("ATT_LON"));
				attVO.setCountry(rs.getString("COUNTRY"));
				attVO.setAdministrative_area(rs.getString("ADMINISTRATIVE_AREA"));
				attVO.setAtt_information(rs.getString("ATT_INFORMATION"));
				attVO.setAtt_address(rs.getString("ATT_ADDRESS"));
				attVO.setAtt_status(rs.getInt("ATT_STATUS"));
				list.add(attVO);
			}
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public byte[] getAttPicture(String att_no) {
		byte[] att_picture = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_PICTURE);
			pstmt.setString(1, att_no);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				att_picture = rs.getBytes("ATT_PICTURE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return att_picture;
	}

	@Override
	public List<AttractionsVO> getAllRandom() {
		List<AttractionsVO> list = new ArrayList<AttractionsVO>();
		AttractionsVO attVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_ALL_RANDOM);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				attVO = new AttractionsVO();
				attVO.setAtt_no(rs.getString("ATT_NO"));
				attVO.setAtt_name(rs.getString("ATT_NAME"));
				attVO.setAtt_lat(rs.getDouble("ATT_LAT"));
				attVO.setAtt_lon(rs.getDouble("ATT_LON"));
				attVO.setCountry(rs.getString("COUNTRY"));
				attVO.setAdministrative_area(rs.getString("ADMINISTRATIVE_AREA"));
				attVO.setAtt_information(rs.getString("ATT_INFORMATION"));
//				attVO.setAtt_picture(rs.getBytes("ATT_PICTURE"));
				attVO.setAtt_address(rs.getString("ATT_ADDRESS"));
				attVO.setAtt_status(rs.getInt("ATT_STATUS"));
				list.add(attVO);
			}
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

}
