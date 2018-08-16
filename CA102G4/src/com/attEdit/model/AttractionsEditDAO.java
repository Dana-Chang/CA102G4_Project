package com.attEdit.model;

import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AttractionsEditDAO implements AttractionsEditDAO_interface {

	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private static final String SQL_INSERT = "Insert into ATTRACTIONS_EDIT "
			+ "(ATTEDIT_NO,MEM_ID,ATTEDIT_DATE,ATT_NO,ATT_NAME,ATT_LAT,ATT_LON,COUNTRY,ADMINISTRATIVE_AREA,ATT_INFORMATION,ATT_PICTURE,ATT_ADDRESS) "
			+ "values ('AE'||LPAD(to_char(ATTEDIT_SEQ.NEXTVAL), 8, '0'),?,SYSTIMESTAMP,?,?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE = "update ATTRACTIONS_EDIT set "
			+ "MEM_ID = ?,ATTEDIT_DATE = SYSTIMESTAMP,ATT_NO = ?,ATT_NAME = ?,ATT_LAT = ?,ATT_LON = ?,COUNTRY = ?,ADMINISTRATIVE_AREA = ?,ATT_INFORMATION = ?,"
			+ "ATT_PICTURE = ?,ATT_ADDRESS = ? where ATTEDIT_NO = ?";
	private static final String SQL_DELETE = "delete from ATTRACTIONS_EDIT where ATTEDIT_NO = ?";
	private static final String SQL_QUERY = "select * from ATTRACTIONS_EDIT where ATTEDIT_NO = ?";
	private static final String SQL_QUERY_ALL = "select * from ATTRACTIONS_EDIT";
	private static final String SQL_QUERY_ALL_ORDER_DATE = "select * from ATTRACTIONS_EDIT order by ATTEDIT_DATE desc";
	private static final String SQL_ATT_UPDATE = "update ATTRACTIONS set "
			+ "ATT_NAME = ?,ATT_LAT = ?,ATT_LON = ?,COUNTRY = ?,ADMINISTRATIVE_AREA = ?,ATT_INFORMATION = ?,ATT_PICTURE = ?,"
			+ "ATT_ADDRESS = ? where ATT_NO = ?";

	@Override
	public int insert(AttractionsEditVO attEditVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_INSERT);

			pstmt.setString(1, attEditVO.getMem_id());
			pstmt.setString(2, attEditVO.getAtt_no());
			pstmt.setString(3, attEditVO.getAtt_name());
			pstmt.setDouble(4, attEditVO.getAtt_lat());
			pstmt.setDouble(5, attEditVO.getAtt_lon());
			pstmt.setString(6, attEditVO.getCountry());
			pstmt.setString(7, attEditVO.getAdministrative_area());
			pstmt.setString(8, attEditVO.getAtt_information());
			pstmt.setBytes(9, attEditVO.getAtt_picture());
			pstmt.setString(10, attEditVO.getAtt_address());

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
	public int update(AttractionsEditVO attEditVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE);

			pstmt.setString(1, attEditVO.getMem_id());
			pstmt.setString(2, attEditVO.getAtt_no());
			pstmt.setString(3, attEditVO.getAtt_name());
			pstmt.setDouble(4, attEditVO.getAtt_lat());
			pstmt.setDouble(5, attEditVO.getAtt_lon());
			pstmt.setString(6, attEditVO.getCountry());
			pstmt.setString(7, attEditVO.getAdministrative_area());
			pstmt.setString(8, attEditVO.getAtt_information());
			pstmt.setBytes(9, attEditVO.getAtt_picture());
			pstmt.setString(10, attEditVO.getAtt_address());
			pstmt.setString(11, attEditVO.getAttEdit_no());

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
	public int delete(String attEdit_no) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_DELETE);

			pstmt.setString(1, attEdit_no);

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
	public AttractionsEditVO findByPrimaryKey(String attEdit_no) {
		AttractionsEditVO attEditVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_QUERY);

			pstmt.setString(1, attEdit_no);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				attEditVO = new AttractionsEditVO();

				attEditVO.setAttEdit_no(rs.getString("ATTEDIT_NO"));
				attEditVO.setMem_id(rs.getString("MEM_ID"));
				attEditVO.setAttEdit_date(rs.getTimestamp("ATTEDIT_DATE"));
				attEditVO.setAtt_no(rs.getString("ATT_NO"));
				attEditVO.setAtt_name(rs.getString("ATT_NAME"));
				attEditVO.setAtt_lat(rs.getDouble("ATT_LAT"));
				attEditVO.setAtt_lon(rs.getDouble("ATT_LON"));
				attEditVO.setCountry(rs.getString("COUNTRY"));
				attEditVO.setAdministrative_area(rs.getString("ADMINISTRATIVE_AREA"));
				attEditVO.setAtt_information(rs.getString("ATT_INFORMATION"));
				attEditVO.setAtt_picture(rs.getBytes("ATT_PICTURE"));
				attEditVO.setAtt_address(rs.getString("ATT_ADDRESS"));
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
		return attEditVO;
	}

	@Override
	public List<AttractionsEditVO> getAll() {
		List<AttractionsEditVO> list = new ArrayList<AttractionsEditVO>();
		AttractionsEditVO attEditVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_QUERY_ALL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				attEditVO = new AttractionsEditVO();

				attEditVO.setAttEdit_no(rs.getString("ATTEDIT_NO"));
				attEditVO.setMem_id(rs.getString("MEM_ID"));
				attEditVO.setAttEdit_date(rs.getTimestamp("ATTEDIT_DATE"));
				attEditVO.setAtt_no(rs.getString("ATT_NO"));
				attEditVO.setAtt_name(rs.getString("ATT_NAME"));
				attEditVO.setAtt_lat(rs.getDouble("ATT_LAT"));
				attEditVO.setAtt_lon(rs.getDouble("ATT_LON"));
				attEditVO.setCountry(rs.getString("COUNTRY"));
				attEditVO.setAdministrative_area(rs.getString("ADMINISTRATIVE_AREA"));
				attEditVO.setAtt_information(rs.getString("ATT_INFORMATION"));
				attEditVO.setAtt_picture(rs.getBytes("ATT_PICTURE"));
				attEditVO.setAtt_address(rs.getString("ATT_ADDRESS"));

				list.add(attEditVO);
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
	public List<AttractionsEditVO> getAllOrderByDate() {
		List<AttractionsEditVO> list = new ArrayList<AttractionsEditVO>();
		AttractionsEditVO attEditVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_QUERY_ALL_ORDER_DATE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				attEditVO = new AttractionsEditVO();

				attEditVO.setAttEdit_no(rs.getString("ATTEDIT_NO"));
				attEditVO.setMem_id(rs.getString("MEM_ID"));
				attEditVO.setAttEdit_date(rs.getTimestamp("ATTEDIT_DATE"));
				attEditVO.setAtt_no(rs.getString("ATT_NO"));
				attEditVO.setAtt_name(rs.getString("ATT_NAME"));
				attEditVO.setAtt_lat(rs.getDouble("ATT_LAT"));
				attEditVO.setAtt_lon(rs.getDouble("ATT_LON"));
				attEditVO.setCountry(rs.getString("COUNTRY"));
				attEditVO.setAdministrative_area(rs.getString("ADMINISTRATIVE_AREA"));
				attEditVO.setAtt_information(rs.getString("ATT_INFORMATION"));
				attEditVO.setAtt_picture(rs.getBytes("ATT_PICTURE"));
				attEditVO.setAtt_address(rs.getString("ATT_ADDRESS"));

				list.add(attEditVO);
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
	public int att_update(AttractionsEditVO attEditVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(SQL_ATT_UPDATE);

			pstmt.setString(1, attEditVO.getAtt_name());
			pstmt.setDouble(2, attEditVO.getAtt_lat());
			pstmt.setDouble(3, attEditVO.getAtt_lon());
			pstmt.setString(4, attEditVO.getCountry());
			pstmt.setString(5, attEditVO.getAdministrative_area());
			pstmt.setString(6, attEditVO.getAtt_information());
			pstmt.setBytes(7, attEditVO.getAtt_picture());
			pstmt.setString(8, attEditVO.getAtt_address());
			pstmt.setString(9, attEditVO.getAtt_no());

			updateCount += pstmt.executeUpdate();
			
			pstmt = con.prepareStatement(SQL_DELETE);

			pstmt.setString(1, attEditVO.getAttEdit_no());

			updateCount += pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true);
			// Handle any driver errors
		} catch (SQLException se) {
			if(con != null) {
				try {
					con.rollback();
				}catch (SQLException excep) {
					throw new RuntimeException("rollback error occured. "
							+ excep.getMessage());
				}
			}
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

}
