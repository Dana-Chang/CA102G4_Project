package com.trip.model;

import java.sql.*;
import java.util.*;

import com.tripDays.model.TripDaysDAO;
import com.tripDays.model.TripDaysVO;

public class TripJDBCDAO implements TripDAO_interface {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	private static final String SQL_INSERT = "insert into TRIP "
			+ "(TRIP_NO,MEM_ID,TRIP_NAME,TRIP_STARTDAY,TRIP_DAYS,TRIP_VIEWS,TRIP_STATUS) "
			+ "values ('T'||LPAD(TO_CHAR(TRIP_SEQ.NEXTVAL), 9, '0'),?,?,?,?,?,?)";
	private static final String SQL_UPDATE = "update TRIP set "
			+ "MEM_ID = ?,TRIP_NAME = ?,TRIP_STARTDAY = ?,TRIP_DAYS = ?,TRIP_VIEWS = ?,TRIP_STATUS = ?"
			+ " where TRIP_NO = ?";
	
	private static final String SQL_UPDATE2 = "update TRIP set TRIP_NAME = ?,TRIP_STARTDAY = ? where TRIP_NO = ?";
	private static final String SQL_DELETE = "delete from TRIP where TRIP_NO = ?";
	private static final String SQL_QUERY = "select * from TRIP where TRIP_NO = ?";
	private static final String SQL_QUERY_ALL = "select * from TRIP";
	private static final String SQL_QUERY_PUB = "select * from TRIP where TRIP_STATUS = 2";
	private static final String SQL_QUERY_ORDER_VIEWS = "select * from TRIP where TRIP_STATUS = 2 order by TRIP_VIEWS DESC";
	private static final String SQL_QUERY_MEM = "select * from TRIP where MEM_ID = ?";
	private static final String SQL_DELETE_ONLINE = "update TRIP set TRIP_STATUS = 0 where TRIP_NO = ?";
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		}
	}

	@Override
	public int insert(TripVO tripVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_INSERT);
			pstmt.setString(1, tripVO.getMem_id());
			pstmt.setString(2, tripVO.getTrip_name());
			pstmt.setDate(3, tripVO.getTrip_startDay());
			pstmt.setInt(4, tripVO.getTrip_days());
			pstmt.setInt(5, tripVO.getTrip_views());
			pstmt.setInt(6, tripVO.getTrip_status());

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
	public int update(TripVO tripVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_UPDATE);
			pstmt.setString(1, tripVO.getMem_id());
			pstmt.setString(2, tripVO.getTrip_name());
			pstmt.setDate(3, tripVO.getTrip_startDay());
			pstmt.setInt(4, tripVO.getTrip_days());
			pstmt.setInt(5, tripVO.getTrip_views());
			pstmt.setInt(6, tripVO.getTrip_status());
			pstmt.setString(7, tripVO.getTrip_no());
			;

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
	public int delete(String trip_no) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_DELETE);
			pstmt.setString(1, trip_no);

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
	public TripVO findByPrimaryKey(String trip_no) {
		TripVO tripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY);
			pstmt.setString(1, trip_no);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				tripVO = new TripVO();
				tripVO.setTrip_no(rs.getString("TRIP_NO"));
				tripVO.setMem_id(rs.getString("MEM_ID"));
				tripVO.setTrip_name(rs.getString("TRIP_NAME"));
				tripVO.setTrip_startDay(rs.getDate("TRIP_STARTDAY"));
				tripVO.setTrip_days(rs.getInt("TRIP_DAYS"));
				tripVO.setTrip_views(rs.getInt("TRIP_VIEWS"));
				tripVO.setTrip_status(rs.getInt("TRIP_STATUS"));
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
		return tripVO;
	}

	@Override
	public List<TripVO> getAll() {
		List<TripVO> list = new ArrayList<TripVO>();
		TripVO tripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_ALL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tripVO = new TripVO();
				tripVO.setTrip_no(rs.getString("TRIP_NO"));
				tripVO.setMem_id(rs.getString("MEM_ID"));
				tripVO.setTrip_name(rs.getString("TRIP_NAME"));
				tripVO.setTrip_startDay(rs.getDate("TRIP_STARTDAY"));
				tripVO.setTrip_days(rs.getInt("TRIP_DAYS"));
				tripVO.setTrip_views(rs.getInt("TRIP_VIEWS"));
				tripVO.setTrip_status(rs.getInt("TRIP_STATUS"));

				list.add(tripVO);
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
	public void addTrip(TripVO tripVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String SQL_TDAYS_ADD = "insert into TRIP_DAYS "
				+ "(TRIPDAY_NO,TRIP_NO,TRIPDAY_DAYS) "
				+ "values ('TD'||LPAD(to_char(ATT_SEQ.NEXTVAL), 8, '0'),?,?)";
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			String[] cols = {"TRIP_NO"};
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(SQL_INSERT,cols);
			pstmt.setString(1, tripVO.getMem_id());
			pstmt.setString(2, tripVO.getTrip_name());
			pstmt.setDate(3, tripVO.getTrip_startDay());
			pstmt.setInt(4, tripVO.getTrip_days());
			pstmt.setInt(5, tripVO.getTrip_views());
			pstmt.setInt(6, tripVO.getTrip_status());

			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String generatedKeys=null;
			if (rs.next()) {
				do {
					for (int i = 1; i <= columnCount; i++) {
						generatedKeys = rs.getString(i);
					}
				} while (rs.next());
			} else {
				System.out.println("NO KEYS WERE GENERATED.");
			}
			int trip_days = tripVO.getTrip_days();
			System.out.println(generatedKeys);
			System.out.println(trip_days);
			for(int i = 1; i<= trip_days;i++) {
				pstmt = con.prepareStatement(SQL_TDAYS_ADD);
				
				pstmt.setString(1, generatedKeys);
				pstmt.setInt(2, i);
				
				pstmt.executeUpdate();
			}
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
	}
	
	@Override
	public void insertOneTrip(TripVO tripVO, List<TripDaysVO> tdList, Map<Integer, List<Object>> tripDayMap) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			con.setAutoCommit(false);
			
			String[] cols = {"TRIP_NO"};
			pstmt = con.prepareStatement(SQL_INSERT,cols);
			pstmt.setString(1, tripVO.getMem_id());
			pstmt.setString(2, tripVO.getTrip_name());
			pstmt.setDate(3, tripVO.getTrip_startDay());
			pstmt.setInt(4, tripVO.getTrip_days());
			pstmt.setInt(5, tripVO.getTrip_views());
			pstmt.setInt(6, tripVO.getTrip_status());

			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			String next_trip_no = rs.getString("TRIP_NO");
			
			TripDaysDAO tripDaysDAO = new TripDaysDAO();
			for(TripDaysVO tripDaysVO: tdList) {
				tripDaysVO.setTrip_no(next_trip_no);
				List<Object> detailList = tripDayMap.get(tripDaysVO.getTripDay_days());
				tripDaysDAO.insert2(tripDaysVO, detailList, con);
			}
			con.commit();
			con.setAutoCommit(true);
			// Handle any driver errors
		} catch (SQLException se) {
			if(con != null) {
				try {
					System.out.println("由Trip rollback");
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
	public List<TripVO> getByMem_id(String mem_id) {
		List<TripVO> list = new ArrayList<TripVO>();
		TripVO tripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_MEM);
			
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tripVO = new TripVO();
				tripVO.setTrip_no(rs.getString("TRIP_NO"));
				tripVO.setMem_id(rs.getString("MEM_ID"));
				tripVO.setTrip_name(rs.getString("TRIP_NAME"));
				tripVO.setTrip_startDay(rs.getDate("TRIP_STARTDAY"));
				tripVO.setTrip_days(rs.getInt("TRIP_DAYS"));
				tripVO.setTrip_views(rs.getInt("TRIP_VIEWS"));
				tripVO.setTrip_status(rs.getInt("TRIP_STATUS"));

				list.add(tripVO);
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
	public List<TripVO> getPublish() {
		List<TripVO> list = new ArrayList<TripVO>();
		TripVO tripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_PUB);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tripVO = new TripVO();
				tripVO.setTrip_no(rs.getString("TRIP_NO"));
				tripVO.setMem_id(rs.getString("MEM_ID"));
				tripVO.setTrip_name(rs.getString("TRIP_NAME"));
				tripVO.setTrip_startDay(rs.getDate("TRIP_STARTDAY"));
				tripVO.setTrip_days(rs.getInt("TRIP_DAYS"));
				tripVO.setTrip_views(rs.getInt("TRIP_VIEWS"));
				tripVO.setTrip_status(rs.getInt("TRIP_STATUS"));

				list.add(tripVO);
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
	public int deleteOnline(String trip_no) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_DELETE_ONLINE);
			pstmt.setString(1, trip_no);
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
	public List<TripVO> getPublishOrderViews() {
		List<TripVO> list = new ArrayList<TripVO>();
		TripVO tripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_ORDER_VIEWS);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tripVO = new TripVO();
				tripVO.setTrip_no(rs.getString("TRIP_NO"));
				tripVO.setMem_id(rs.getString("MEM_ID"));
				tripVO.setTrip_name(rs.getString("TRIP_NAME"));
				tripVO.setTrip_startDay(rs.getDate("TRIP_STARTDAY"));
				tripVO.setTrip_days(rs.getInt("TRIP_DAYS"));
				tripVO.setTrip_views(rs.getInt("TRIP_VIEWS"));
				tripVO.setTrip_status(rs.getInt("TRIP_STATUS"));

				list.add(tripVO);
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
	public void update2(TripVO tripVO, List<TripDaysVO> tdList, Map<Integer, List<Object>> tripDayMap) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(SQL_UPDATE2);
			pstmt.setString(1, tripVO.getTrip_name());
			pstmt.setDate(2, tripVO.getTrip_startDay());
			pstmt.setString(3, tripVO.getTrip_no());
			
			pstmt.executeUpdate();
			
			TripDaysDAO tripDaysDAO = new TripDaysDAO();
			for(TripDaysVO tripDaysVO: tdList) {
				List<Object> detailList = tripDayMap.get(tripDaysVO.getTripDay_days());
				tripDaysDAO.update2(tripDaysVO, detailList, con);
			}
			con.commit();
			con.setAutoCommit(true);
			// Handle any driver errors
		} catch (SQLException se) {
			if(con != null) {
				try {
					System.out.println("由Trip update2 rollback");
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
	public List<TripVO> getAll(Map<String, String[]> map) {
		List<TripVO> list = new ArrayList<>();
		TripVO tripVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			String finalSQL = "select * from TRIP "
					+ jdbcUtil_CompositeQuery_Trip.get_WhereCondition(map)
					+ "order by TRIP_VIEWS";
			pstmt = con.prepareStatement(finalSQL);
			System.out.println("●●finalSQL(by DAO) = "+finalSQL);
			rs = pstmt.executeQuery();
			

			while (rs.next()) {
				tripVO = new TripVO();
				tripVO.setTrip_no(rs.getString("TRIP_NO"));
				tripVO.setMem_id(rs.getString("MEM_ID"));
				tripVO.setTrip_name(rs.getString("TRIP_NAME"));
				tripVO.setTrip_startDay(rs.getDate("TRIP_STARTDAY"));
				tripVO.setTrip_days(rs.getInt("TRIP_DAYS"));
				tripVO.setTrip_views(rs.getInt("TRIP_VIEWS"));
				tripVO.setTrip_status(rs.getInt("TRIP_STATUS"));
				
				list.add(tripVO);
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
