package com.attTrip.model;

import java.sql.*;
import java.util.*;

public class AttractionsTripJDBCDAO implements AttractionsTripDAO_interface {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	private static final String SQL_INSERT = "insert into ATTRACTIONS_TRIP "
			+ "(ATTTRIP_NO,ATT_NO,TRIPDAY_NO,ATTTRIP_COST,ATTTRIP_START,ATTTRIP_END,TRIP_ORDER,ATTTRIP_NOTE) "
			+ "values ('AT'||LPAD(to_char(ATTTRIP_SEQ.NEXTVAL), 8, '0'),?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE = "update ATTRACTIONS_TRIP set "
			+ "ATT_NO = ?,TRIPDAY_NO = ?,ATTTRIP_COST = ?,ATTTRIP_START = ?,ATTTRIP_END = ?,TRIP_ORDER = ?,"
			+ "ATTTRIP_NOTE = ? where ATTTRIP_NO = ?";
	private static final String SQL_DELETE = "delete from ATTRACTIONS_TRIP where ATTTRIP_NO = ?";
	private static final String SQL_DELETE2 = "delete from ATTRACTIONS_TRIP where TRIPDAY_NO = ?";
	private static final String SQL_QUERY = "select * from ATTRACTIONS_TRIP where ATTTRIP_NO = ?";
	private static final String SQL_QUERY_ALL = "select * from ATTRACTIONS_TRIP";
	private static final String SQL_QUERY_TDNO = "select * from ATTRACTIONS_TRIP where TRIPDAY_NO = ?";
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		}
	}

	@Override
	public int insert(AttractionsTripVO attractionsTripVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_INSERT);
			pstmt.setString(1, attractionsTripVO.getAtt_no());
			pstmt.setString(2, attractionsTripVO.getTripDay_no());
			pstmt.setInt(3, attractionsTripVO.getAttTrip_cost());
			pstmt.setInt(4, attractionsTripVO.getAttTrip_start());
			pstmt.setInt(5, attractionsTripVO.getAttTrip_end());
			pstmt.setInt(6, attractionsTripVO.getTrip_order());
			pstmt.setString(7, attractionsTripVO.getAttTrip_note());

			updateCount = pstmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
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
	public int update(AttractionsTripVO attractionsTripVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_UPDATE);
			pstmt.setString(1, attractionsTripVO.getAtt_no());
			pstmt.setString(2, attractionsTripVO.getTripDay_no());
			pstmt.setInt(3, attractionsTripVO.getAttTrip_cost());
			pstmt.setInt(4, attractionsTripVO.getAttTrip_start());
			pstmt.setInt(5, attractionsTripVO.getAttTrip_end());
			pstmt.setInt(6, attractionsTripVO.getTrip_order());
			pstmt.setString(7, attractionsTripVO.getAttTrip_note());
			pstmt.setString(8, attractionsTripVO.getAttTrip_no());

			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
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
	public int delete(String attTrip_no) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_DELETE);
			pstmt.setString(1, attTrip_no);

			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
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
	public AttractionsTripVO findByPrimaryKey(String attTrip_no) {
		AttractionsTripVO attractionsTripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY);
			pstmt.setString(1, attTrip_no);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				attractionsTripVO = new AttractionsTripVO();
				attractionsTripVO.setAttTrip_no(rs.getString("ATTTRIP_NO"));
				attractionsTripVO.setAtt_no(rs.getString("ATT_NO"));
				attractionsTripVO.setTripDay_no(rs.getString("TRIPDAY_NO"));
				attractionsTripVO.setAttTrip_cost(rs.getInt("ATTTRIP_COST"));
				attractionsTripVO.setAttTrip_start(rs.getInt("ATTTRIP_START"));
				attractionsTripVO.setAttTrip_end(rs.getInt("ATTTRIP_END"));
				attractionsTripVO.setTrip_order(rs.getInt("TRIP_ORDER"));
				attractionsTripVO.setAttTrip_note(rs.getString("ATTTRIP_NOTE"));
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
		return attractionsTripVO;
	}

	@Override
	public List<AttractionsTripVO> getAll() {
		List<AttractionsTripVO> list = new ArrayList<AttractionsTripVO>();
		AttractionsTripVO attractionsTripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_ALL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				attractionsTripVO = new AttractionsTripVO();
				attractionsTripVO.setAttTrip_no(rs.getString("ATTTRIP_NO"));
				attractionsTripVO.setAtt_no(rs.getString("ATT_NO"));
				attractionsTripVO.setTripDay_no(rs.getString("TRIPDAY_NO"));
				attractionsTripVO.setAttTrip_cost(rs.getInt("ATTTRIP_COST"));
				attractionsTripVO.setAttTrip_start(rs.getInt("ATTTRIP_START"));
				attractionsTripVO.setAttTrip_end(rs.getInt("ATTTRIP_END"));
				attractionsTripVO.setTrip_order(rs.getInt("TRIP_ORDER"));
				attractionsTripVO.setAttTrip_note(rs.getString("ATTTRIP_NOTE"));
				list.add(attractionsTripVO);
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
		return list;
	}
	
	@Override
	public void insert2(AttractionsTripVO attTripVO, Connection con) {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(SQL_INSERT);
			pstmt.setString(1, attTripVO.getAtt_no());
			pstmt.setString(2, attTripVO.getTripDay_no());
			pstmt.setInt(3, attTripVO.getAttTrip_cost());
			pstmt.setInt(4, attTripVO.getAttTrip_start());
			pstmt.setInt(5, attTripVO.getAttTrip_end());
			pstmt.setInt(6, attTripVO.getTrip_order());
			pstmt.setString(7, attTripVO.getAttTrip_note());

			pstmt.executeUpdate();
			// Handle any driver errors
		} catch (SQLException se) {
			if(con != null) {
				try {
					System.out.println("由AttractionsTrip rollback");
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
	}
	@Override
	public List<AttractionsTripVO> getByTripDays_no(String tripDays_no) {
		List<AttractionsTripVO> list = new ArrayList<AttractionsTripVO>();
		AttractionsTripVO attTripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_TDNO);
			pstmt.setString(1, tripDays_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				attTripVO = new AttractionsTripVO();
				attTripVO.setAttTrip_no(rs.getString("ATTTRIP_NO"));
				attTripVO.setAtt_no(rs.getString("ATT_NO"));
				attTripVO.setTripDay_no(rs.getString("TRIPDAY_NO"));
				attTripVO.setAttTrip_cost(rs.getInt("ATTTRIP_COST"));
				attTripVO.setAttTrip_start(rs.getInt("ATTTRIP_START"));
				attTripVO.setAttTrip_end(rs.getInt("ATTTRIP_END"));
				attTripVO.setTrip_order(rs.getInt("TRIP_ORDER"));
				attTripVO.setAttTrip_note(rs.getString("ATTTRIP_NOTE"));
				list.add(attTripVO);
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
	public int deleteByTripDay(String tripDay_no, Connection con) {
		int updateCount = 0;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_DELETE2);
			pstmt.setString(1, tripDay_no);

			updateCount = pstmt.executeUpdate();
			// Handle any driver errors
		} catch (SQLException se) {
			if(con != null) {
				try {
					System.out.println("由AttractionsTrip deleteByTripDay rollback");
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
		}
		return updateCount;
	}

}
