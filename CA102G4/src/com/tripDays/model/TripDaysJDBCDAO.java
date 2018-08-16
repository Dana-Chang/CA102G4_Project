package com.tripDays.model;

import java.sql.*;
import java.util.*;

import com.attTrip.model.AttractionsTripDAO;
import com.attTrip.model.AttractionsTripVO;
import com.trafficTrip.model.TrafficTripDAO;
import com.trafficTrip.model.TrafficTripVO;

public class TripDaysJDBCDAO implements TripDaysDAO_interface {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	private static final String SQL_INSERT = "insert into TRIP_DAYS "
			+ "(TRIPDAY_NO,TRIP_NO,TRIPDAY_DAYS,TRIPDAY_DATE,TRIPDAY_HOTELNAME,TRIPDAY_HOTELSTART,TRIPDAY_HOTELNOTE,TRIPDAY_HOTELCOST) "
			+ "values ('TD'||LPAD(TO_CHAR(TRIPDAYS_SEQ.NEXTVAL), 8, '0'),?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE = "update TRIP_DAYS set "
			+ "TRIP_NO = ?,TRIPDAY_DAYS = ?,TRIPDAY_DATE = ?,TRIPDAY_HOTELNAME = ?,TRIPDAY_HOTELSTART = ?,TRIPDAY_HOTELNOTE = ?,"
			+ "TRIPDAY_HOTELCOST = ? where TRIPDAY_NO = ?";
	private static final String SQL_UPDATE2 = "update TRIP_DAYS set "
			+ "TRIPDAY_DAYS = ?,TRIPDAY_DATE = ?,TRIPDAY_HOTELNAME = ?,TRIPDAY_HOTELSTART = ?,TRIPDAY_HOTELNOTE = ?,"
			+ "TRIPDAY_HOTELCOST = ? where TRIPDAY_NO = ?";
	private static final String SQL_DELETE = "delete from TRIP_DAYS where TRIPDAY_NO = ?";
	private static final String SQL_QUERY = "select * from TRIP_DAYS where TRIPDAY_NO = ?";
	private static final String SQL_QUERY_ALL = "select * from TRIP_DAYS";
	private static final String SQL_QUERY_TRIP_NO = "select * from TRIP_DAYS where TRIP_NO = ?";
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		}
	}

	@Override
	public int insert(TripDaysVO tripDaysVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_INSERT);
			pstmt.setString(1, tripDaysVO.getTrip_no());
			pstmt.setInt(2, tripDaysVO.getTripDay_days());
			pstmt.setDate(3, tripDaysVO.getTripDay_date());
			pstmt.setString(4, tripDaysVO.getTripDay_hotelName());
			pstmt.setInt(5, tripDaysVO.getTripDay_hotelStart());
			pstmt.setString(6, tripDaysVO.getTripDay_hotelNote());
			pstmt.setInt(7, tripDaysVO.getTripDay_hotelCost());

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
	public int update(TripDaysVO tripDaysVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_UPDATE);
			pstmt.setString(1, tripDaysVO.getTrip_no());
			pstmt.setInt(2, tripDaysVO.getTripDay_days());
			pstmt.setDate(3, tripDaysVO.getTripDay_date());
			pstmt.setString(4, tripDaysVO.getTripDay_hotelName());
			pstmt.setInt(5, tripDaysVO.getTripDay_hotelStart());
			pstmt.setString(6, tripDaysVO.getTripDay_hotelNote());
			pstmt.setInt(7, tripDaysVO.getTripDay_hotelCost());
			pstmt.setString(8, tripDaysVO.getTripDay_no());

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
	public int delete(String tripDay_no) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_DELETE);
			pstmt.setString(1, tripDay_no);

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
	public TripDaysVO findByPrimaryKey(String tripDay_no) {
		TripDaysVO tripDaysVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY);
			pstmt.setString(1, tripDay_no);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				tripDaysVO = new TripDaysVO();
				tripDaysVO.setTripDay_no(rs.getString("TRIPDAY_NO"));
				tripDaysVO.setTrip_no(rs.getString("TRIP_NO"));
				tripDaysVO.setTripDay_days(rs.getInt("TRIPDAY_DAYS"));
				tripDaysVO.setTripDay_date(rs.getDate("TRIPDAY_DATE"));
				tripDaysVO.setTripDay_hotelName(rs.getString("TRIPDAY_HOTELNAME"));
				tripDaysVO.setTripDay_hotelStart(rs.getInt("TRIPDAY_HOTELSTART"));
				tripDaysVO.setTripDay_hotelNote(rs.getString("TRIPDAY_HOTELNOTE"));
				tripDaysVO.setTripDay_hotelCost(rs.getInt("TRIPDAY_HOTELCOST"));
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
		return tripDaysVO;
	}

	@Override
	public List<TripDaysVO> getAll() {
		List<TripDaysVO> list = new ArrayList<TripDaysVO>();
		TripDaysVO tripDaysVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_ALL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tripDaysVO = new TripDaysVO();
				tripDaysVO.setTripDay_no(rs.getString("TRIPDAY_NO"));
				tripDaysVO.setTrip_no(rs.getString("TRIP_NO"));
				tripDaysVO.setTripDay_days(rs.getInt("TRIPDAY_DAYS"));
				tripDaysVO.setTripDay_date(rs.getDate("TRIPDAY_DATE"));
				tripDaysVO.setTripDay_hotelName(rs.getString("TRIPDAY_HOTELNAME"));
				tripDaysVO.setTripDay_hotelStart(rs.getInt("TRIPDAY_HOTELSTART"));
				tripDaysVO.setTripDay_hotelNote(rs.getString("TRIPDAY_HOTELNOTE"));
				tripDaysVO.setTripDay_hotelCost(rs.getInt("TRIPDAY_HOTELCOST"));
				list.add(tripDaysVO);
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
	public void insert2(TripDaysVO tripDaysVO, List<Object> detailList, Connection con) {
		PreparedStatement pstmt = null;
		try {
			String[] cols = {"TRIPDAY_NO"};
			pstmt = con.prepareStatement(SQL_INSERT,cols);
			
			pstmt.setString(1, tripDaysVO.getTrip_no());
			pstmt.setInt(2, tripDaysVO.getTripDay_days());
			pstmt.setDate(3, tripDaysVO.getTripDay_date());
			pstmt.setString(4, tripDaysVO.getTripDay_hotelName());
			pstmt.setInt(5, tripDaysVO.getTripDay_hotelStart());
			pstmt.setString(6, tripDaysVO.getTripDay_hotelNote());
			pstmt.setInt(7, tripDaysVO.getTripDay_hotelCost());

			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			String next_tripDays_no = rs.getString("TRIPDAY_NO");
			
			TrafficTripDAO ttDAO = new TrafficTripDAO();
			AttractionsTripDAO atDAO = new AttractionsTripDAO();
			for(Object detail:detailList) {
				if(detail instanceof TrafficTripVO) {
					TrafficTripVO traTripVO = (TrafficTripVO)detail;
					traTripVO.setTripDay_no(next_tripDays_no);
					ttDAO.insert2(traTripVO, con);
				}else if(detail instanceof AttractionsTripVO) {
					AttractionsTripVO attTripVO = (AttractionsTripVO)detail;
					attTripVO.setTripDay_no(next_tripDays_no);
					atDAO.insert2(attTripVO, con);
				}
			}
			// Handle any driver errors
		} catch (SQLException se) {
			if(con != null) {
				try {
					System.out.println("由TripDays rollback");
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
	public List<TripDaysVO> getByTrip_no(String trip_no) {
		List<TripDaysVO> list = new ArrayList<TripDaysVO>();
		TripDaysVO tripDaysVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_TRIP_NO);
			pstmt.setString(1, trip_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tripDaysVO = new TripDaysVO();
				tripDaysVO.setTripDay_no(rs.getString("TRIPDAY_NO"));
				tripDaysVO.setTrip_no(rs.getString("TRIP_NO"));
				tripDaysVO.setTripDay_days(rs.getInt("TRIPDAY_DAYS"));
				tripDaysVO.setTripDay_date(rs.getDate("TRIPDAY_DATE"));
				tripDaysVO.setTripDay_hotelName(rs.getString("TRIPDAY_HOTELNAME"));
				tripDaysVO.setTripDay_hotelStart(rs.getInt("TRIPDAY_HOTELSTART"));
				tripDaysVO.setTripDay_hotelNote(rs.getString("TRIPDAY_HOTELNOTE"));
				tripDaysVO.setTripDay_hotelCost(rs.getInt("TRIPDAY_HOTELCOST"));
				list.add(tripDaysVO);
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
	public void update2(TripDaysVO tripDaysVO, List<Object> detailList, Connection con) {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(SQL_UPDATE2);
			
			pstmt.setInt(1, tripDaysVO.getTripDay_days());
			pstmt.setDate(2, tripDaysVO.getTripDay_date());
			pstmt.setString(3, tripDaysVO.getTripDay_hotelName());
			if(tripDaysVO.getTripDay_hotelStart()!=null) {
				pstmt.setInt(4, tripDaysVO.getTripDay_hotelStart());
			}else {
				pstmt.setNull(4, Types.INTEGER);
			}
			pstmt.setString(5, tripDaysVO.getTripDay_hotelNote());
			if(tripDaysVO.getTripDay_hotelStart()!=null) {
				pstmt.setInt(6, tripDaysVO.getTripDay_hotelCost());
			}else {
				pstmt.setNull(6, Types.INTEGER);
			}
			pstmt.setString(7, tripDaysVO.getTripDay_no());

			pstmt.executeUpdate();
			
			TrafficTripDAO ttDAO = new TrafficTripDAO();
			AttractionsTripDAO atDAO = new AttractionsTripDAO();
			//先刪除原本行程
			ttDAO.deleteByTripDay(tripDaysVO.getTripDay_no(), con);
			atDAO.deleteByTripDay(tripDaysVO.getTripDay_no(), con);
			//再新增修改後的
			for(Object detail:detailList) {
				if(detail instanceof TrafficTripVO) {
					TrafficTripVO traTripVO = (TrafficTripVO)detail;
					traTripVO.setTripDay_no(tripDaysVO.getTripDay_no());
					ttDAO.insert2(traTripVO, con);
				}else if(detail instanceof AttractionsTripVO) {
					AttractionsTripVO attTripVO = (AttractionsTripVO)detail;
					attTripVO.setTripDay_no(tripDaysVO.getTripDay_no());
					atDAO.insert2(attTripVO, con);
				}
			}
			// Handle any driver errors
		} catch (SQLException se) {
			if(con != null) {
				try {
					System.out.println("由TripDays update2 rollback");
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
	
}
