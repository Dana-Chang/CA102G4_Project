package com.trafficTrip.model;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.*;

public class TrafficTripDAO implements TrafficTripDAO_interface {

	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

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

	@Override
	public int insert(TrafficTripVO traTripVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_INSERT);
			pstmt.setString(1, traTripVO.getTripDay_no());
			pstmt.setInt(2, traTripVO.getTraTrip_type());
			pstmt.setInt(3, traTripVO.getTraTrip_cost());
			pstmt.setInt(4, traTripVO.getTraTrip_start());
			pstmt.setInt(5, traTripVO.getTraTrip_end());
			pstmt.setInt(6, traTripVO.getTrip_order());
			pstmt.setString(7, traTripVO.getTraTrip_name());
			pstmt.setString(8, traTripVO.getTraTrip_note());

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
	public int update(TrafficTripVO traTripVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE);
			pstmt.setString(1, traTripVO.getTripDay_no());
			pstmt.setInt(2, traTripVO.getTraTrip_type());
			pstmt.setInt(3, traTripVO.getTraTrip_cost());
			pstmt.setInt(4, traTripVO.getTraTrip_start());
			pstmt.setInt(5, traTripVO.getTraTrip_end());
			pstmt.setInt(6, traTripVO.getTrip_order());
			pstmt.setString(7, traTripVO.getTraTrip_name());
			pstmt.setString(8, traTripVO.getTraTrip_note());
			pstmt.setString(9, traTripVO.getTraTrip_no());

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
	public int delete(String traTrip_no) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_DELETE);
			pstmt.setString(1, traTrip_no);

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
	public TrafficTripVO findByPrimaryKey(String traTrip_no) {
		TrafficTripVO traTripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_QUERY);
			pstmt.setString(1, traTrip_no);

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
		return traTripVO;
	}

	@Override
	public List<TrafficTripVO> getAll() {
		List<TrafficTripVO> list = new ArrayList<TrafficTripVO>();
		TrafficTripVO traTripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SQL_QUERY_ALL);
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
					System.out.println("由TrafficTrip insert2 rollback");
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
	}

	@Override
	public List<TrafficTripVO> getByTripDays_no(String tripDays_no) {
		List<TrafficTripVO> list = new ArrayList<TrafficTripVO>();
		TrafficTripVO traTripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
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
			con = ds.getConnection();
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
