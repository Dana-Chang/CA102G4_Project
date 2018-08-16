package com.trafficTrip.model;

import java.util.*;
import java.sql.*;

public class TrafficTripJDBCDAO implements TrafficTripDAO_interface {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	private static final String SQL_INSERT = "insert into TRAFFIC_TRIP "
			+ "(TRATRIP_NO,TRIPDAY_NO,TRATRIP_TYPE,TRATRIP_COST,TRATRIP_START,TRATRIP_END,TRIP_ORDER,TRATRIP_NAME,TRATRIP_NOTE) "
			+ "values ('TT'||LPAD(to_char(TRATRIP_SEQ.NEXTVAL), 8, '0'),?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE = "update TRAFFIC_TRIP set "
			+ "TRIPDAY_NO = ?,TRATRIP_TYPE = ?,TRATRIP_COST = ?,TRATRIP_START = ?,TRATRIP_END = ?,TRIP_ORDER = ?,"
			+ "TRATRIP_NAME = ?,TRATRIP_NOTE = ? where TRATRIP_NO = ?";
	private static final String SQL_DELETE = "delete from TRAFFIC_TRIP where TRATRIP_NO = ?";
	private static final String SQL_DELETE2 = "delete from TRAFFIC_TRIP where TRIPDAY_NO = ?";
	private static final String SQL_QUERY = "select * from TRAFFIC_TRIP where TRATRIP_NO = ?";
	private static final String SQL_QUERY_ALL = "select * from TRAFFIC_TRIP";
	private static final String SQL_QUERY_TDNO = "select * from TRAFFIC_TRIP where TRIPDAY_NO = ?";
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		}
	}

	@Override
	public int insert(TrafficTripVO trafficTripVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_INSERT);
			pstmt.setString(1, trafficTripVO.getTripDay_no());
			pstmt.setInt(2, trafficTripVO.getTraTrip_type());
			pstmt.setInt(3, trafficTripVO.getTraTrip_cost());
			pstmt.setInt(4, trafficTripVO.getTraTrip_start());
			pstmt.setInt(5, trafficTripVO.getTraTrip_end());
			pstmt.setInt(6, trafficTripVO.getTrip_order());
			pstmt.setString(7, trafficTripVO.getTraTrip_name());
			pstmt.setString(8, trafficTripVO.getTraTrip_note());

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
	public int update(TrafficTripVO trafficTripVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_UPDATE);
			pstmt.setString(1, trafficTripVO.getTripDay_no());
			pstmt.setInt(2, trafficTripVO.getTraTrip_type());
			pstmt.setInt(3, trafficTripVO.getTraTrip_cost());
			pstmt.setInt(4, trafficTripVO.getTraTrip_start());
			pstmt.setInt(5, trafficTripVO.getTraTrip_end());
			pstmt.setInt(6, trafficTripVO.getTrip_order());
			pstmt.setString(7, trafficTripVO.getTraTrip_name());
			pstmt.setString(8, trafficTripVO.getTraTrip_note());
			pstmt.setString(9, trafficTripVO.getTraTrip_no());

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
	public int delete(String traTrip_no) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_DELETE);
			pstmt.setString(1, traTrip_no);

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
	public TrafficTripVO findByPrimaryKey(String traTrip_no) {
		TrafficTripVO trafficTripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY);
			pstmt.setString(1, traTrip_no);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				trafficTripVO = new TrafficTripVO();
				trafficTripVO.setTraTrip_no(rs.getString("TRATRIP_NO"));
				trafficTripVO.setTripDay_no(rs.getString("TRIPDAY_NO"));
				trafficTripVO.setTraTrip_type(rs.getInt("TRATRIP_TYPE"));
				trafficTripVO.setTraTrip_cost(rs.getInt("TRATRIP_COST"));
				trafficTripVO.setTraTrip_start(rs.getInt("TRATRIP_START"));
				trafficTripVO.setTraTrip_end(rs.getInt("TRATRIP_END"));
				trafficTripVO.setTrip_order(rs.getInt("TRIP_ORDER"));
				trafficTripVO.setTraTrip_name(rs.getString("TRATRIP_NAME"));
				trafficTripVO.setTraTrip_note(rs.getString("TRATRIP_NOTE"));
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
		return trafficTripVO;
	}

	@Override
	public List<TrafficTripVO> getAll() {
		List<TrafficTripVO> list = new ArrayList<TrafficTripVO>();
		TrafficTripVO trafficTripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_ALL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				trafficTripVO = new TrafficTripVO();
				trafficTripVO.setTraTrip_no(rs.getString("TRATRIP_NO"));
				trafficTripVO.setTripDay_no(rs.getString("TRIPDAY_NO"));
				trafficTripVO.setTraTrip_type(rs.getInt("TRATRIP_TYPE"));
				trafficTripVO.setTraTrip_cost(rs.getInt("TRATRIP_COST"));
				trafficTripVO.setTraTrip_start(rs.getInt("TRATRIP_START"));
				trafficTripVO.setTraTrip_end(rs.getInt("TRATRIP_END"));
				trafficTripVO.setTrip_order(rs.getInt("TRIP_ORDER"));
				trafficTripVO.setTraTrip_name(rs.getString("TRATRIP_NAME"));
				trafficTripVO.setTraTrip_note(rs.getString("TRATRIP_NOTE"));

				list.add(trafficTripVO);
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
	public void insert2(TrafficTripVO traTripVO, Connection con) {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(SQL_INSERT);
			pstmt.setString(1, traTripVO.getTripDay_no());
			pstmt.setInt(2, traTripVO.getTraTrip_type());
			pstmt.setInt(3, traTripVO.getTraTrip_cost());
			pstmt.setInt(4, traTripVO.getTraTrip_start());
			pstmt.setInt(5, traTripVO.getTraTrip_end());
			pstmt.setInt(6, traTripVO.getTrip_order());
			pstmt.setString(7, traTripVO.getTraTrip_name());
			pstmt.setString(8, traTripVO.getTraTrip_note());

			pstmt.executeUpdate();
			// Handle any driver errors
		} catch (SQLException se) {
			if(con != null) {
				try {
					System.out.println("由TrafficTrip rollback");
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
	public List<TrafficTripVO> getByTripDays_no(String tripDays_no) {
		List<TrafficTripVO> list = new ArrayList<TrafficTripVO>();
		TrafficTripVO traTripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_TDNO);
			pstmt.setString(1, tripDays_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				traTripVO = new TrafficTripVO();
				traTripVO.setTraTrip_no(rs.getString("TRATRIP_NO"));
				traTripVO.setTripDay_no(rs.getString("TRIPDAY_NO"));
				traTripVO.setTraTrip_type(rs.getInt("TRATRIP_TYPE"));
				traTripVO.setTraTrip_cost(rs.getInt("TRATRIP_COST"));
				traTripVO.setTraTrip_start(rs.getInt("TRATRIP_START"));
				traTripVO.setTraTrip_end(rs.getInt("TRATRIP_END"));
				traTripVO.setTrip_order(rs.getInt("TRIP_ORDER"));
				traTripVO.setTraTrip_name(rs.getString("TRATRIP_NAME"));
				traTripVO.setTraTrip_note(rs.getString("TRATRIP_NOTE"));

				list.add(traTripVO);
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
					System.out.println("由TrafficTrip deleteByTripDay rollback");
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
