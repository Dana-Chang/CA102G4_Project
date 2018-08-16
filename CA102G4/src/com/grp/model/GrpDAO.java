package com.grp.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.grp_mem.model.Grp_memVO;
import com.mem.model.MemberVO;
import com.photo_wall.model.Photo_wallVO;
import com.tools.jdbcUtil_CompositeQuery_Grp;
	
	public class GrpDAO implements GrpDAO_interface {
		
		// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
		private static DataSource ds = null;
		static {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		
		//新增一個揪團
		private static final String INSERT_STMT =
			"Insert into GRP (GRP_ID,MEM_ID,GRP_START,GRP_END,GRP_CNT,GRP_ACPT,TRIP_NO,TRIP_START,TRIP_END,TRIP_LOCALE,TRIP_DETAILS,GRP_PHOTO,GRP_STATUS,CHATROOM_ID,GRP_TITLE,GRP_PRICE) "
			+ "VALUES ('GRP'||LPAD(to_char(GRP_seq.NEXTVAL), 6, '0'),?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		//更新揪團
		private static final String UPDATE =
			"UPDATE GRP SET GRP_END=?,GRP_CNT=?,GRP_ACPT=?,TRIP_NO=?,TRIP_START=?,TRIP_END=?,TRIP_LOCALE=?,TRIP_DETAILS=?,GRP_PHOTO=?,CHATROOM_ID=?,GRP_TITLE=?,GRP_PRICE=? WHERE GRP_ID =? ";
		
		//刪除揪團
		private static final String DELETE_GRP =
			"DELETE FROM GRP WHERE GRP_ID = ?";
		
		//已揪團開始日期篩選狀態是正常的揪團 
		private static final String GET_ALL = 
			"SELECT * FROM GRP WHERE GRP_STATUS = 1 ORDER BY GRP_START DESC";
		
		//查詢揪團內容
		private static final String GET_ONE_STMT = 
			"SELECT * FROM GRP WHERE GRP_ID = ?";
		
		//讀取登入者的揪團
		private static final String GET_ALL_BYMEMID=
			"SELECT * FROM GRP WHERE MEM_ID = ?";
		
		//取得我參加揪團的內容
		private static final String GET_JOIN_GRP = 
			"SELECT GRP_PHOTO,TRIP_START,GRP_TITLE,TRIP_DETAILS,GRP_TITLE,GRP_MEM.GRP_ID FROM GRP JOIN GRP_MEM ON GRP.GRP_ID = GRP_MEM.GRP_ID WHERE GRP_MEM.MEM_ID = ? ORDER BY TRIP_START DESC";		
		
		//成團後更改揪團狀態(=2成團)
		private static final String UPDATE_GRP_STATUS =
			"UPDATE GRP SET GRP_STATUS = ? WHERE GRP_ID = ? ";
		
		// 揪團可報名人數、接受人數 確定參加會減少
		private static final String UPDATE_GRP_MEM_LESS = 
			"UPDATE GRP SET GRP_CNT = ? WHERE GRP_ID = ?";
		
		// 揪團可報名人數、接受人數 取消參加會增加
		private static final String UPDATE_GRP_MEM_PLUS = 
			"UPDATE GRP SET GRP_CNT = ? WHERE GRP_ID = ?";
		
		
		@Override
		public void insert(GrpVO grpVO) {
			
			System.out.println("DAO的INSERT-1");
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				System.out.println("DAO的INSERT-2");

				con = ds.getConnection();

				pstmt = con.prepareStatement(INSERT_STMT);
				System.out.println("DAO的INSERT-3");

				pstmt.setString(1, grpVO.getMem_Id());
				
//				pstmt.setTimestamp(2, grpVO.getGrp_Start());
				pstmt.setTimestamp(2, grpVO.getGrp_End());
				
				System.out.println("1的="+grpVO.getGrp_End());
				
				//可接受報名人數
				
				pstmt.setInt(3, grpVO.getGrp_Cnt());
				
				System.out.println("1-3的="+grpVO.getGrp_Cnt());
				
				//滿團人數
				
				pstmt.setInt(4, grpVO.getGrp_Acpt());				
				
				System.out.println("1-4的="+grpVO.getGrp_Acpt());

				pstmt.setString(5, grpVO.getTrip_No());
				
				System.out.println("1-5的="+grpVO.getTrip_No());

				
				pstmt.setTimestamp(6, grpVO.getTrip_Start());
				
				System.out.println("2的="+grpVO.getTrip_Start());

				pstmt.setTimestamp(7, grpVO.getTrip_End());
				
				System.out.println("3的="+grpVO.getTrip_End());

				pstmt.setString(8, grpVO.getTrip_Locale());
				pstmt.setString(9, grpVO.getTrip_Details());
				pstmt.setBytes(10, grpVO.getGrp_Photo());
				pstmt.setInt(11, grpVO.getGrp_Status());
				pstmt.setString(12, grpVO.getChatroom_Id());
				pstmt.setString(13, grpVO.getGrp_Title());
				pstmt.setString(14, grpVO.getGrp_Price());
				
				System.out.println("DAO的INSERT-4");

				pstmt.executeUpdate();
				System.out.println("DAO的INSERT-5");

			}catch (SQLException se) {
				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}
		
		@Override
		public void update(GrpVO grpVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {

				con = ds.getConnection();

				pstmt = con.prepareStatement(UPDATE);

				pstmt.setTimestamp(1, grpVO.getGrp_End());
						
				pstmt.setInt(2, grpVO.getGrp_Cnt());
				
				pstmt.setInt(3, grpVO.getGrp_Acpt());
								
				pstmt.setString(4, grpVO.getTrip_No());
				
				pstmt.setTimestamp(5, grpVO.getTrip_Start());
				
				pstmt.setTimestamp(6, grpVO.getTrip_End());
				
				pstmt.setString(7, grpVO.getTrip_Locale());
				
				pstmt.setString(8, grpVO.getTrip_Details());
				
				pstmt.setBytes(9, grpVO.getGrp_Photo());				

				pstmt.setString(10, grpVO.getChatroom_Id());				

				pstmt.setString(11, grpVO.getGrp_Title());
				
				pstmt.setString(12, grpVO.getGrp_Price());				

				pstmt.setString(13, grpVO.getGrp_Id());
				pstmt.executeUpdate();


				
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}

		@Override
		public void delete(String grp_Id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				
				con = ds.getConnection();
				
				con.setAutoCommit(false);
				
				pstmt = con.prepareStatement(DELETE_GRP);

				pstmt.setString(1, grp_Id);

				pstmt.executeUpdate();

				con.commit();
				con.setAutoCommit(true);
				
			}catch (SQLException se) {
				if (con != null) {
					try {
						con.rollback();
					} catch (SQLException excep) {
						throw new RuntimeException("rollback error occured. " + excep.getMessage());
					}
				}
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}
		
		//***************為了讀取某位會員的揪團內容***********************
		public List<GrpVO> getAll_ByMemID(String mem_Id){
			List<GrpVO> list = new ArrayList<>();
			
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			
			try {
			con = ds.getConnection();
			pstmt  = con.prepareStatement(GET_ALL_BYMEMID);
			pstmt.setString(1, mem_Id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				GrpVO grpVO = new GrpVO();
				grpVO.setMem_Id(rs.getString("MEM_ID"));
				grpVO.setGrp_Id(rs.getString("GRP_ID"));
				grpVO.setGrp_Start(rs.getTimestamp("GRP_START"));
				grpVO.setGrp_End(rs.getTimestamp("GRP_END"));
				grpVO.setGrp_Cnt(rs.getInt("GRP_CNT"));
				grpVO.setGrp_Acpt(rs.getInt("GRP_ACPT"));
				grpVO.setTrip_No(rs.getString("TRIP_NO"));
				grpVO.setTrip_Start(rs.getTimestamp("TRIP_START"));
				grpVO.setTrip_End(rs.getTimestamp("TRIP_END"));
				grpVO.setTrip_Locale(rs.getString("TRIP_LOCALE"));
				grpVO.setTrip_Details(rs.getString("TRIP_DETAILS"));
				grpVO.setGrp_Photo(rs.getBytes("GRP_PHOTO"));
				grpVO.setGrp_Status(rs.getInt("GRP_STATUS"));
				grpVO.setChatroom_Id(rs.getString("CHATROOM_ID"));
				grpVO.setGrp_Title(rs.getString("GRP_TITLE"));
				grpVO.setGrp_Price(rs.getString("GRP_PRICE"));
				list.add(grpVO);

			}
			
			}catch(SQLException se) {
				throw new RuntimeException ("資料庫發生錯誤"+se.getMessage());
			}finally{
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return list;
		}

		@Override
		public GrpVO findByPrimaryKey(String grp_Id) {
			GrpVO grpVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {

				con = ds.getConnection();

				pstmt = con.prepareStatement(GET_ONE_STMT);

				pstmt.setString(1, grp_Id);

				rs = pstmt.executeQuery();

				while (rs.next()) {

					grpVO = new GrpVO();
					grpVO.setGrp_Id(rs.getString("GRP_ID"));
					grpVO.setGrp_Start(rs.getTimestamp("GRP_START"));
					grpVO.setGrp_End(rs.getTimestamp("GRP_END"));
					grpVO.setGrp_Cnt(rs.getInt("GRP_CNT"));
					grpVO.setGrp_Acpt(rs.getInt("GRP_ACPT"));
					grpVO.setTrip_No(rs.getString("TRIP_NO"));
					grpVO.setTrip_Start(rs.getTimestamp("TRIP_START"));
					grpVO.setTrip_End(rs.getTimestamp("TRIP_END"));
					grpVO.setTrip_Locale(rs.getString("TRIP_LOCALE"));
					grpVO.setTrip_Details(rs.getString("TRIP_DETAILS"));
					grpVO.setGrp_Photo(rs.getBytes("GRP_PHOTO"));
					grpVO.setGrp_Status(rs.getInt("GRP_STATUS"));
					grpVO.setChatroom_Id(rs.getString("CHATROOM_ID"));
					grpVO.setGrp_Title(rs.getString("GRP_TITLE"));
					grpVO.setGrp_Price(rs.getString("GRP_PRICE"));
					grpVO.setMem_Id(rs.getString("MEM_ID"));

				}
				

				// Handle any driver errors
			}catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}

			return grpVO;
		}
		
		//查詢所有揪團
		@Override
		public List<GrpVO> getAll() {

		List<GrpVO> list = new ArrayList<GrpVO>();
		GrpVO grpVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				grpVO = new GrpVO();
				grpVO.setMem_Id(rs.getString("MEM_ID"));
				grpVO.setGrp_Id(rs.getString("GRP_ID"));
				grpVO.setGrp_Start(rs.getTimestamp("GRP_START"));
				grpVO.setGrp_End(rs.getTimestamp("GRP_END"));
				grpVO.setGrp_Cnt(rs.getInt("GRP_CNT"));
				grpVO.setGrp_Acpt(rs.getInt("GRP_ACPT"));
				grpVO.setTrip_No(rs.getString("TRIP_NO"));
				grpVO.setTrip_Start(rs.getTimestamp("TRIP_START"));
				grpVO.setTrip_End(rs.getTimestamp("TRIP_END"));
				grpVO.setTrip_Locale(rs.getString("TRIP_LOCALE"));
				grpVO.setTrip_Details(rs.getString("TRIP_DETAILS"));
				grpVO.setGrp_Photo(rs.getBytes("GRP_PHOTO"));
				grpVO.setGrp_Status(rs.getInt("GRP_STATUS"));
				grpVO.setChatroom_Id(rs.getString("CHATROOM_ID"));
				grpVO.setGrp_Title(rs.getString("GRP_TITLE"));
				grpVO.setGrp_Price(rs.getString("GRP_PRICE"));
				list.add(grpVO);
				
				System.out.println(grpVO.getGrp_Id());
			}
			

			// Handle any driver errors
		}catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
		
		//萬用複合查詢
		@Override
		public List<GrpVO> getAll(Map<String, String[]> map) {
			List<GrpVO> list = new ArrayList<GrpVO>();
			GrpVO grpVO = null;
			System.out.println("萬用複合查詢0");
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
		
			try {
				System.out.println("萬用複合查詢1");

				con = ds.getConnection();
				String finalSQL = "SELECT * FROM GRP "
				      + jdbcUtil_CompositeQuery_Grp.get_WhereCondition(map)
			          + "order by GRP_ID";
				pstmt = con.prepareStatement(finalSQL);
				
				System.out.println("●●finalSQL(by DAO) = "+finalSQL);
				
				rs = pstmt.executeQuery();
				
				//System.out.println(pstmt.executeQuery());
				
				
				
				while (rs.next()) {
					
					System.out.println("萬用複合查詢3");

					grpVO = new GrpVO();
					
					System.out.println("萬用複合查詢4grpVO"+grpVO);

					
					grpVO.setMem_Id(rs.getString("MEM_ID"));
					
					System.out.println("MEM_ID="+rs.getString("MEM_ID"));
					
					grpVO.setGrp_Id(rs.getString("GRP_ID"));
					
					System.out.println("MEM_ID="+rs.getString("GRP_ID"));

					grpVO.setGrp_Start(rs.getTimestamp("GRP_START"));
					grpVO.setGrp_End(rs.getTimestamp("GRP_END"));
					grpVO.setGrp_Cnt(rs.getInt("GRP_CNT"));
					grpVO.setGrp_Acpt(rs.getInt("GRP_ACPT"));
					grpVO.setTrip_No(rs.getString("TRIP_NO"));
					grpVO.setTrip_Start(rs.getTimestamp("TRIP_START"));
					grpVO.setTrip_End(rs.getTimestamp("TRIP_END"));
					grpVO.setTrip_Locale(rs.getString("TRIP_LOCALE"));
					grpVO.setTrip_Details(rs.getString("TRIP_DETAILS"));
					grpVO.setGrp_Photo(rs.getBytes("GRP_PHOTO"));
					grpVO.setGrp_Status(rs.getInt("GRP_STATUS"));
					grpVO.setChatroom_Id(rs.getString("CHATROOM_ID"));
					grpVO.setGrp_Title(rs.getString("GRP_TITLE"));
					grpVO.setGrp_Price(rs.getString("GRP_PRICE"));
					System.out.println("萬用複合查詢3");

					list.add(grpVO);
					System.out.println("萬用複合查詢4");

				}
		
				// Handle any SQL errors
			} catch (SQLException se) {
				
				System.out.println("萬用複合查詢6");

				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				
			} finally {
				
				System.out.println("萬用複合查詢7");

				
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return list;
			 
		}
		
		@Override
		public List<GrpVO> getAll_join_grp(String mem_Id) {
			List<GrpVO> list = new ArrayList<GrpVO>();
			GrpVO grpVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				con = ds.getConnection();
				
				pstmt = con.prepareStatement(GET_JOIN_GRP);
				
				
				pstmt.setString(1, mem_Id);
				
				rs = pstmt.executeQuery();

				while (rs.next()) {
					
					grpVO = new GrpVO();
					grpVO.setGrp_Id(rs.getString("GRP_ID"));
					grpVO.setGrp_Title(rs.getString("GRP_TITLE"));
					grpVO.setTrip_Start(rs.getTimestamp("TRIP_START"));
					grpVO.setTrip_Details(rs.getString("TRIP_DETAILS"));
					grpVO.setGrp_Photo(rs.getBytes("GRP_PHOTO"));
					
					list.add(grpVO);
				}
				

				// Handle any driver errors
			}catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return list;
		}

		@Override
		public void update_status(GrpVO grpVO) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			
		try {
			
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE_GRP_STATUS);
			
			pstmt.setInt(1, grpVO.getGrp_Status());
			pstmt.setString(2, grpVO.getGrp_Id());
			pstmt.executeUpdate();
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

		@Override
			public void update_mem_less(GrpVO grpVO) {
				Connection con = null;
				PreparedStatement pstmt = null;
				
				try {

					con = ds.getConnection();

					pstmt = con.prepareStatement(UPDATE_GRP_MEM_LESS);

							
					pstmt.setInt(1, grpVO.getGrp_Cnt());
					
					pstmt.setString(2, grpVO.getGrp_Id());
					pstmt.executeUpdate();


					
				} catch (SQLException se) {
					throw new RuntimeException("A database error occured. "
							+ se.getMessage());
					// Clean up JDBC resources
				} finally {
					if (pstmt != null) {
						try {
							pstmt.close();
						} catch (SQLException se) {
							se.printStackTrace(System.err);
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (Exception e) {
							e.printStackTrace(System.err);
						}
					}
				}
			}


		@Override
		public void update_mem_plus(GrpVO grpVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				System.out.println("我有增加人數!!");
				con = ds.getConnection();

				pstmt = con.prepareStatement(UPDATE_GRP_MEM_PLUS);

						
				pstmt.setInt(1, grpVO.getGrp_Cnt());
				System.out.println("可接受報名人數為:"+grpVO.getGrp_Cnt());
				pstmt.setString(2, grpVO.getGrp_Id());
				pstmt.executeUpdate();


				
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}
		
		
	}
		


