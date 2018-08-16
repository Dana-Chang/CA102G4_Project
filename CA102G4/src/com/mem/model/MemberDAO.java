package com.mem.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MemberDAO implements MemberDAO_interface {
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


//private static final String REGIGST_STMT = 
//	"Insert into MEMBER (MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME) "
//	+ "VALUES ('M'||LPAD(to_char(MEMBER_seq.NEXTVAL), 6, '0'),?,?,?)";

private static final String INSERT_STMT = 
"Insert into MEMBER (MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME,MEM_STATE,MEM_REG_DATE,MEM_PHOTO) "
+ "VALUES ('M'||LPAD(to_char(MEMBER_seq.NEXTVAL), 6, '0'),?,?,?,?,?,?)";
private static final String UPDATE = 
	"UPDATE MEMBER SET MEM_ACCOUNT=?,MEM_PASSWORD= ?, MEM_NAME= ? ,MEM_SEX= ?,MEM_ADDRESS= ?,MEM_BIRTHDAY= ?,MEM_PHONE= ?,MEM_PROFILE= ?,MEM_PHOTO= ?,MEM_STATE= ?,DELIVERY_ADDRESS_1= ?,DELIVERY_ADDRESS_2=?,DELIVERY_ADDRESS_3=?,STORE_ADDR_1=?,STORE_ADDR_2=?,STORE_ADDR_3=?,STORE_NAME_1=?,STORE_NAME_2=?,STORE_NAME_3=?,STORE_NO_1=?,STORE_NO_2=?,STORE_NO_3=? WHERE MEM_ID = ?";
private static final String GET_ALL_STMT = 
	"SELECT * FROM MEMBER ORDER BY MEM_ID";
private static final String GET_ONE_STMT = 
	"SELECT * FROM MEMBER WHERE MEM_ID = ?";

private static final String DELETE_MEMBER = 
	"DELETE FROM MEMBER WHERE MEM_ID = ?";

private static final String login_Member = 
	"SELECT * FROM MEMBER WHERE MEM_ACCOUNT= ? AND MEM_PASSWORD = ?";

private static final String update_Member =
	"UPDATE MEMBER SET MEM_NAME= ? , MEM_PHONE= ? ,MEM_SEX= ?,MEM_BIRTHDAY= ?,MEM_PHOTO= ?,MEM_PROFILE= ? WHERE MEM_ID = ?";

private static final String MEM_Update_PASSWORD = 
	"UPDATE MEMBER SET MEM_PASSWORD=? WHERE MEM_ACCOUNT=?";

//防止重覆註冊
private static final String CHECK_ACCOUNT = 
"SELECT * FROM MEMBER WHERE MEM_ACCOUNT = ?";

//管理員更改會員狀態
private static final String UPDATE_ON_STATE_STMT=
	"UPDATE MEMBER SET MEM_STATE = 1 WHERE MEM_ID=?";
private static final String UPDATE_OFF_STATE_STMT=
	"UPDATE MEMBER SET MEM_STATE = 2 WHERE MEM_ID=?";
//管理員搜尋會員
private static final String getAll_member =
	"SELECT * FROM MEMBER WHERE UPPER(MEM_NAME) LIKE UPPER(?) OR UPPER(MEM_NAME) LIKE UPPER(?) ORDER BY MEM_ID ";

	// 更改 711 地址 第一個
	private static final String MEM_Update_STORE1 = "UPDATE MEMBER SET STORE_NO_1=?,STORE_NAME_1=?,STORE_ADDR_1=? WHERE MEM_ID=?";

	// 更改 711 地址 第二個
	private static final String MEM_Update_STORE2 = "UPDATE MEMBER SET STORE_NO_2=?,STORE_NAME_2=?,STORE_ADDR_2=? WHERE MEM_ID=?";

	// 更改 711 地址 第三個
	private static final String MEM_Update_STORE3 = "UPDATE MEMBER SET STORE_NO_3=?,STORE_NAME_3=?,STORE_ADDR_3=? WHERE MEM_ID=?";

	// 刪除 711 地址 第一個
	private static final String MEM_Delete_STORE1 = "UPDATE MEMBER SET STORE_NO_1=null,STORE_NAME_1=null,STORE_ADDR_1=null WHERE MEM_ID=?";

	// 刪除 711 地址 第二個
	private static final String MEM_Delete_STORE2 = "UPDATE MEMBER SET STORE_NO_2=null,STORE_NAME_2=null,STORE_ADDR_2=null WHERE MEM_ID=?";

	// 刪除 711 地址 第三個
	private static final String MEM_Delete_STORE3 = "UPDATE MEMBER SET STORE_NO_3=null,STORE_NAME_3=null,STORE_ADDR_3=null WHERE MEM_ID=?";

	// 更改宅配地址 第一個
	private static final String MEM_Update_HOME1 = "UPDATE MEMBER SET DELIVERY_ADDRESS_1=? WHERE MEM_ID=?";

	// 更改宅配地址 第二個
	private static final String MEM_Update_HOME2 = "UPDATE MEMBER SET DELIVERY_ADDRESS_2=? WHERE MEM_ID=?";

	// 更改宅配地址 第三個
	private static final String MEM_Update_HOME3 = "UPDATE MEMBER SET DELIVERY_ADDRESS_3=? WHERE MEM_ID=?";
	
	// 刪除宅配地址 第一個
	private static final String MEM_Delete_HOME1 = "UPDATE MEMBER SET DELIVERY_ADDRESS_1=null WHERE MEM_ID=?";
	
	// 刪除宅配地址 第二個
	private static final String MEM_Delete_HOME2 = "UPDATE MEMBER SET DELIVERY_ADDRESS_2=null WHERE MEM_ID=?";
	
	// 刪除宅配地址 第三個
	private static final String MEM_Delete_HOME3 = "UPDATE MEMBER SET DELIVERY_ADDRESS_3=null WHERE MEM_ID=?";
	
	//世銘打的--全站搜尋用--根據會員姓名or會員介紹or旅遊記標題or旅遊記內容or旅遊記標籤類別or旅遊記標籤名稱or揪團目的地or揪團詳情or揪團標題...搜尋
	private static final String SEARCH_ALL = "SELECT M.MEM_ID,M.MEM_NAME,M.MEM_PROFILE,M.MEM_REG_DATE,M.MEM_PHOTO " + 
			"FROM (SELECT DISTINCT M.MEM_ID " + 
			"FROM (MEMBER M LEFT OUTER JOIN GRP G ON M.MEM_ID = G.MEM_ID) LEFT OUTER JOIN ((BLOG B LEFT OUTER JOIN BLOG_TAG BT ON B.BLOG_ID = BT.BLOG_ID) LEFT OUTER JOIN BLOG_TAG_NAME BTN ON BT.BTN_ID = BTN.BTN_ID) ON M.MEM_ID = B.MEM_ID " + 
			"WHERE (M.MEM_STATE = 1 AND UPPER(M.MEM_NAME) LIKE UPPER(?)) " + 
			"OR (M.MEM_STATE = 1 AND UPPER(M.MEM_PROFILE) LIKE UPPER(?)) " + 
			"OR (M.MEM_STATE = 1 AND B.BLOG_STATUS  = 0 AND UPPER(B.BLOG_TITLE) LIKE UPPER(?)) " + 
			"OR (M.MEM_STATE = 1 AND B.BLOG_STATUS  = 0 AND UPPER(B.BLOG_CONTENT) LIKE UPPER(?)) " + 
			"OR (M.MEM_STATE = 1 AND B.BLOG_STATUS  = 0 AND UPPER(BTN.BTN_CLASS) LIKE UPPER(?)) " + 
			"OR (M.MEM_STATE = 1 AND B.BLOG_STATUS  = 0 AND UPPER(BTN.BTN_NAME) LIKE UPPER(?)) " + 
			"OR (M.MEM_STATE = 1 AND UPPER(G.TRIP_LOCALE) LIKE UPPER(?)) " + 
			"OR (M.MEM_STATE = 1 AND UPPER(G.TRIP_DETAILS) LIKE UPPER(?)) " + 
			"OR (M.MEM_STATE = 1 AND UPPER(G.GRP_TITLE) LIKE UPPER(?))) R, MEMBER M " + 
			"WHERE R.MEM_ID = M.MEM_ID";
	@Override
	public void insert(MemberVO memberVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, memberVO.getMem_Account());
			pstmt.setString(2, memberVO.getMem_Password());
			pstmt.setString(3, memberVO.getMem_Name());
			pstmt.setInt(4,3);
			pstmt.setDate(5, memberVO.getMem_Reg_Date());
			pstmt.setBytes(6, memberVO.getMem_Photo());

			pstmt.executeUpdate();
			
			
			// Handle any SQL errors
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
	public void update(MemberVO memberVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, memberVO.getMem_Account());
			pstmt.setString(2, memberVO.getMem_Password());
			pstmt.setString(3, memberVO.getMem_Name());
			pstmt.setInt(4, memberVO.getMem_Sex());
			pstmt.setString(5, memberVO.getMem_Address());
			pstmt.setDate(6, memberVO.getMem_Birthday());
			pstmt.setString(7, memberVO.getMem_Phone());
			pstmt.setString(8, memberVO.getMem_Profile());
			pstmt.setBytes(9, memberVO.getMem_Photo());
			pstmt.setInt(10, memberVO.getMem_State());
			pstmt.setString(11, memberVO.getDelivery_Address_1());
			pstmt.setString(12, memberVO.getDelivery_Address_2());
			pstmt.setString(13, memberVO.getDelivery_Address_3());
			pstmt.setString(14, memberVO.getSTORE_ADDR_1());
			pstmt.setString(15, memberVO.getSTORE_ADDR_2());
			pstmt.setString(16, memberVO.getSTORE_ADDR_3());
			pstmt.setString(17, memberVO.getSTORE_NAME_1());
			pstmt.setString(18, memberVO.getSTORE_NAME_2());
			pstmt.setString(19, memberVO.getSTORE_NAME_3());
			pstmt.setInt(20, memberVO.getSTORE_NO_1());
			pstmt.setInt(21, memberVO.getSTORE_NO_2());
			pstmt.setInt(22, memberVO.getSTORE_NO_3());
			pstmt.setString(23, memberVO.getMem_Id());
			pstmt.executeUpdate();

		} catch (SQLException se) {
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
		



	@Override
	public void delete(String mem_Id) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();
			
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(DELETE_MEMBER);

			pstmt.setString(1, mem_Id);

			pstmt.executeUpdate();

			con.commit();
			con.setAutoCommit(true);
			
			// Handle any SQL errors

		} catch (SQLException se) {
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


	@Override
	public MemberVO findByPrimaryKey(String mem_Id) {
		
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, mem_Id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				memberVO = new MemberVO();
				memberVO.setMem_Id(rs.getString("MEM_ID"));
				memberVO.setMem_Account(rs.getString("MEM_ACCOUNT"));
				memberVO.setMem_Password(rs.getString("MEM_PASSWORD"));
				memberVO.setMem_Name(rs.getString("MEM_NAME"));
				memberVO.setMem_Sex(rs.getInt("MEM_SEX"));
				memberVO.setMem_Address(rs.getString("MEM_ADDRESS"));
				memberVO.setMem_Birthday(rs.getDate("MEM_BIRTHDAY"));
				memberVO.setMem_Phone(rs.getString("MEM_PHONE"));
				memberVO.setMem_Profile(rs.getString("MEM_PROFILE"));
				memberVO.setMem_Photo(rs.getBytes("MEM_PHOTO"));
				memberVO.setMem_State(rs.getInt("MEM_STATE"));
				
				memberVO.setDelivery_Address_1(rs.getString("DELIVERY_ADDRESS_1"));
				memberVO.setDelivery_Address_2(rs.getString("DELIVERY_ADDRESS_2"));
				memberVO.setDelivery_Address_3(rs.getString("DELIVERY_ADDRESS_3"));
				
				memberVO.setSTORE_ADDR_1(rs.getString("STORE_ADDR_1"));
				memberVO.setSTORE_ADDR_2(rs.getString("STORE_ADDR_2"));
				memberVO.setSTORE_ADDR_3(rs.getString("STORE_ADDR_3"));
				
				memberVO.setSTORE_NAME_1(rs.getString("STORE_NAME_1"));
				memberVO.setSTORE_NAME_2(rs.getString("STORE_NAME_2"));
				memberVO.setSTORE_NAME_3(rs.getString("STORE_NAME_3"));

				memberVO.setSTORE_NO_1(rs.getInt("STORE_NO_1"));
				memberVO.setSTORE_NO_2(rs.getInt("STORE_NO_2"));
				memberVO.setSTORE_NO_3(rs.getInt("STORE_NO_3"));
				memberVO.setMem_Reg_Date(rs.getDate("MEM_REG_DATE"));

				String photoEncoded = Base64.getEncoder().encodeToString(rs.getBytes("MEM_PHOTO"));
				memberVO.setEncoded(photoEncoded);
				
//				pstmt.executeUpdate();


			}
			
			// Handle any SQL errors
		} catch (SQLException se) {
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
		return memberVO;
	}
	@Override
	public List<MemberVO> getAll() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				memberVO = new MemberVO();
				memberVO.setMem_Id(rs.getString("MEM_ID"));
				memberVO.setMem_Account(rs.getString("MEM_ACCOUNT"));
				memberVO.setMem_Password(rs.getString("MEM_PASSWORD"));
				memberVO.setMem_Name(rs.getString("MEM_NAME"));
				memberVO.setMem_Sex(rs.getInt("MEM_SEX"));
				memberVO.setMem_Address(rs.getString("MEM_ADDRESS"));
				memberVO.setMem_Birthday(rs.getDate("MEM_BIRTHDAY"));
				memberVO.setMem_Phone(rs.getString("MEM_PHONE"));
				memberVO.setMem_Profile(rs.getString("MEM_PROFILE"));
				memberVO.setMem_Photo(rs.getBytes("MEM_PHOTO"));
				memberVO.setMem_State(rs.getInt("MEM_STATE"));
				
				memberVO.setDelivery_Address_1(rs.getString("DELIVERY_ADDRESS_1"));
				memberVO.setDelivery_Address_2(rs.getString("DELIVERY_ADDRESS_2"));
				memberVO.setDelivery_Address_3(rs.getString("DELIVERY_ADDRESS_3"));
				
				memberVO.setSTORE_ADDR_1(rs.getString("STORE_ADDR_1"));
				memberVO.setSTORE_ADDR_2(rs.getString("STORE_ADDR_2"));
				memberVO.setSTORE_ADDR_3(rs.getString("STORE_ADDR_3"));
				
				memberVO.setSTORE_NAME_1(rs.getString("STORE_NAME_1"));
				memberVO.setSTORE_NAME_2(rs.getString("STORE_NAME_2"));
				memberVO.setSTORE_NAME_3(rs.getString("STORE_NAME_3"));

				memberVO.setSTORE_NO_1(rs.getInt("STORE_NO_1"));
				memberVO.setSTORE_NO_2(rs.getInt("STORE_NO_2"));
				memberVO.setSTORE_NO_3(rs.getInt("STORE_NO_3"));

				memberVO.setMem_Reg_Date(rs.getDate("MEM_REG_DATE"));
				
				list.add(memberVO); // Store the row in the list
			}

			// Handle any SQL errors
		} catch (SQLException se) {
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
	public MemberVO login_Member(String mem_Account, String mem_Password) {
		MemberVO memberVO_login = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(login_Member);
			pstmt.setString(1, mem_Account);
			pstmt.setString(2, mem_Password);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				memberVO_login = new MemberVO();
				memberVO_login.setMem_Id(rs.getString("MEM_ID"));
				memberVO_login.setMem_Account(rs.getString("MEM_ACCOUNT"));
				memberVO_login.setMem_Password(rs.getString("MEM_PASSWORD"));
				memberVO_login.setMem_Name(rs.getString("MEM_NAME"));
				memberVO_login.setMem_Sex(rs.getInt("MEM_SEX"));
				memberVO_login.setMem_Address(rs.getString("MEM_ADDRESS"));
				memberVO_login.setMem_Birthday(rs.getDate("MEM_BIRTHDAY"));
				memberVO_login.setMem_Phone(rs.getString("MEM_PHONE"));
				memberVO_login.setMem_Profile(rs.getString("MEM_PROFILE"));
				memberVO_login.setMem_Photo(rs.getBytes("MEM_PHOTO"));
				memberVO_login.setMem_State(rs.getInt("MEM_STATE"));

				memberVO_login.setDelivery_Address_1(rs.getString("DELIVERY_ADDRESS_1"));
				memberVO_login.setDelivery_Address_2(rs.getString("DELIVERY_ADDRESS_2"));
				memberVO_login.setDelivery_Address_3(rs.getString("DELIVERY_ADDRESS_3"));
				
				memberVO_login.setSTORE_ADDR_1(rs.getString("STORE_ADDR_1"));
				memberVO_login.setSTORE_ADDR_2(rs.getString("STORE_ADDR_2"));
				memberVO_login.setSTORE_ADDR_3(rs.getString("STORE_ADDR_3"));
				
				memberVO_login.setSTORE_NAME_1(rs.getString("STORE_NAME_1"));
				memberVO_login.setSTORE_NAME_2(rs.getString("STORE_NAME_2"));
				memberVO_login.setSTORE_NAME_3(rs.getString("STORE_NAME_3"));

				memberVO_login.setSTORE_NO_1(rs.getInt("STORE_NO_1"));
				memberVO_login.setSTORE_NO_2(rs.getInt("STORE_NO_2"));
				memberVO_login.setSTORE_NO_3(rs.getInt("STORE_NO_3"));
				pstmt.executeUpdate();

			}
			
			// Handle any driver errors
		} 
		catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} 
		finally {
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
		return memberVO_login;
		}

	@Override
	public void update_Member(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(update_Member);
			
			pstmt.setString(1, memberVO.getMem_Name());
			pstmt.setString(2, memberVO.getMem_Phone());
			pstmt.setInt(3, memberVO.getMem_Sex());
			pstmt.setDate(4, memberVO.getMem_Birthday());
			pstmt.setBytes(5, memberVO.getMem_Photo());
			pstmt.setString(6, memberVO.getMem_Profile());
			pstmt.setString(7, memberVO.getMem_Id());
			System.out.println("memberVO.getMem_Id()="+memberVO.getMem_Id()+memberVO.getMem_Phone()+memberVO.getMem_Sex()+
					memberVO.getMem_Birthday()+memberVO.getMem_Photo()+memberVO.getMem_Profile());
			
			pstmt.executeUpdate();
			System.out.println("進來dao/update_member");

		} catch (SQLException se) {
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

	@Override
	public void Mem_Update_Password(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Update_PASSWORD);
			pstmt.setString(1, memberVO.getMem_Password());
			pstmt.setString(2, memberVO.getMem_Account());
			pstmt.executeUpdate();
			
		} catch (SQLException se) {
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
	//管理員更改會員的狀態
	@Override
	public int update_State(String mem_Id, Integer mem_State) {
		Connection con = null ;
		PreparedStatement pstmt= null;
		int count=0;
		if(mem_State == 1) {
			try {
				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_ON_STATE_STMT);
				
				pstmt.setString(1,mem_Id);
				
				count=pstmt.executeUpdate();
				
			}catch(SQLException se) {
				throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
			}finally {
				if(pstmt != null) {
					try {
						pstmt.close();
					}catch(SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				
				if(con != null) {
					try {
					  con.close();
					}catch(Exception e) {
					  e.printStackTrace(System.err);
					}
				}
			}
			
		}else if(mem_State == 2) {		
			try {

				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_OFF_STATE_STMT);
				
				pstmt.setString(1,mem_Id);
				
				count=pstmt.executeUpdate();
				
			}catch(SQLException se) {
				throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
			}finally {
				if(pstmt != null) {
					try {
						pstmt.close();
					}catch(SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				
				if(con != null) {
					try {
					  con.close();
					}catch(Exception e) {
					  e.printStackTrace(System.err);
					}
				}
			}
			
			
		}
		return count;
	}

	@Override
	public List<MemberVO> getAll_member(String mem_Name){
		
		List<MemberVO> list = new ArrayList<MemberVO>();
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(getAll_member);
			pstmt.setString(1, "%" + mem_Name + "%");
			pstmt.setString(2, "%" + mem_Name + "%");

			rs = pstmt.executeQuery();

			while (rs.next()) {
				
			memberVO = new MemberVO();
			memberVO.setMem_Id(rs.getString("MEM_ID"));
			memberVO.setMem_Account(rs.getString("MEM_ACCOUNT"));
			memberVO.setMem_Name(rs.getString("MEM_NAME"));
			memberVO.setMem_Birthday(rs.getDate("MEM_BIRTHDAY"));
			memberVO.setMem_Phone(rs.getString("MEM_PHONE"));
			memberVO.setMem_Profile(rs.getString("MEM_PROFILE"));
			memberVO.setMem_Photo(rs.getBytes("MEM_PHOTO"));
			memberVO.setMem_State(rs.getInt("MEM_STATE"));
			memberVO.setMem_Reg_Date(rs.getDate("MEM_REG_DATE"));

			list.add(memberVO);
			}
			

			// Handle any driver errors
		} catch (SQLException se) {
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
	public MemberVO checkAccount(String mem_Account) {
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(CHECK_ACCOUNT);
			pstmt.setString(1, mem_Account);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				memberVO = new MemberVO();
				memberVO.setMem_Account(rs.getString("MEM_ACCOUNT"));
				
			}
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
		return memberVO;
	}


	// 修改 711地址 第一個
	@Override
	public void Mem_Update_Store1(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Update_STORE1);
			// "UPDATE MEMBER SET STORE_NO_1=?,STORE_NAME_1=?,STORE_ADDR_1=? WHERE
			// MEM_ID=?";
			pstmt.setInt(1, memberVO.getSTORE_NO_1());
			pstmt.setString(2, memberVO.getSTORE_NAME_1());
			pstmt.setString(3, memberVO.getSTORE_ADDR_1());
			pstmt.setString(4, memberVO.getMem_Id());
			System.out.println(memberVO.getSTORE_NO_1());
			System.out.println(memberVO.getSTORE_NAME_1());
			System.out.println(memberVO.getSTORE_ADDR_1());
			System.out.println(memberVO.getMem_Id());
			pstmt.executeUpdate();

		} catch (SQLException se) {
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

	// 修改 711地址 第二個
	@Override
	public void Mem_Update_Store2(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Update_STORE2);
			// "UPDATE MEMBER SET STORE_NO_1=?,STORE_NAME_1=?,STORE_ADDR_1=? WHERE
			// MEM_ID=?";
			pstmt.setInt(1, memberVO.getSTORE_NO_2());
			pstmt.setString(2, memberVO.getSTORE_NAME_2());
			pstmt.setString(3, memberVO.getSTORE_ADDR_2());
			pstmt.setString(4, memberVO.getMem_Id());
			System.out.println("修改 711地址 第二個");
			System.out.println(memberVO.getSTORE_NO_2());
			System.out.println(memberVO.getSTORE_NAME_2());
			System.out.println(memberVO.getSTORE_ADDR_2());
			System.out.println(memberVO.getMem_Id());
			pstmt.executeUpdate();

		} catch (SQLException se) {
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

	// 修改 711地址 第三個
	@Override
	public void Mem_Update_Store3(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Update_STORE3);
			// "UPDATE MEMBER SET STORE_NO_1=?,STORE_NAME_1=?,STORE_ADDR_1=? WHERE
			// MEM_ID=?";
			pstmt.setInt(1, memberVO.getSTORE_NO_3());
			pstmt.setString(2, memberVO.getSTORE_NAME_3());
			pstmt.setString(3, memberVO.getSTORE_ADDR_3());
			pstmt.setString(4, memberVO.getMem_Id());
			System.out.println(memberVO.getSTORE_NO_3());
			System.out.println(memberVO.getSTORE_NAME_3());
			System.out.println(memberVO.getSTORE_ADDR_3());
			System.out.println(memberVO.getMem_Id());
			pstmt.executeUpdate();

		} catch (SQLException se) {
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

	// 刪除 711地址 第一個
	@Override
	public void Mem_Delete_Store1(String memId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Delete_STORE1);
			// "UPDATE MEMBER SET STORE_NO_1=null,STORE_NAME_1=null,STORE_ADDR_1=null WHERE
			// MEM_ID=?";

			pstmt.setString(1, memId);
			System.out.println(memId);
			pstmt.executeUpdate();

		} catch (SQLException se) {
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

	// 刪除 711地址 第二個
	@Override
	public void Mem_Delete_Store2(String memId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Delete_STORE2);
			// "UPDATE MEMBER SET STORE_NO_1=null,STORE_NAME_1=null,STORE_ADDR_1=null WHERE
			// MEM_ID=?";

			pstmt.setString(1, memId);
			System.out.println(memId);
			pstmt.executeUpdate();

		} catch (SQLException se) {
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

	// 刪除 711地址 第三個
	@Override
	public void Mem_Delete_Store3(String memId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Delete_STORE3);
			// "UPDATE MEMBER SET STORE_NO_1=null,STORE_NAME_1=null,STORE_ADDR_1=null WHERE
			// MEM_ID=?";

			pstmt.setString(1, memId);
			System.out.println(memId);
			pstmt.executeUpdate();

		} catch (SQLException se) {
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

	// 修改 宅配地址 第一個
	@Override
	public void Mem_Update_Home1(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Update_HOME1);
			// "UPDATE MEMBER SET DELIVERY_ADDRESS_1=? WHERE MEM_ID=?";
			pstmt.setString(1, memberVO.getDelivery_Address_1());
			pstmt.setString(2, memberVO.getMem_Id());
			System.out.println(memberVO.getDelivery_Address_1());
			System.out.println(memberVO.getMem_Id());
			pstmt.executeUpdate();

		} catch (SQLException se) {
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

	// 修改 宅配地址 第二個
	@Override
	public void Mem_Update_Home2(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Update_HOME2);
			// "UPDATE MEMBER SET DELIVERY_ADDRESS_1=? WHERE MEM_ID=?";
			pstmt.setString(1, memberVO.getDelivery_Address_2());
			pstmt.setString(2, memberVO.getMem_Id());
			System.out.println(memberVO.getDelivery_Address_2());
			System.out.println(memberVO.getMem_Id());
			pstmt.executeUpdate();

		} catch (SQLException se) {
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

	// 修改 宅配地址 第三個
	@Override
	public void Mem_Update_Home3(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(MEM_Update_HOME3);
			// "UPDATE MEMBER SET DELIVERY_ADDRESS_1=? WHERE MEM_ID=?";
			pstmt.setString(1, memberVO.getDelivery_Address_3());
			pstmt.setString(2, memberVO.getMem_Id());
			System.out.println(memberVO.getDelivery_Address_3());
			System.out.println(memberVO.getMem_Id());
			pstmt.executeUpdate();

		} catch (SQLException se) {
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
	
	
	// 刪除宅配地址 第一個
		@Override
		public void Mem_Delete_Home1(String memId) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(MEM_Delete_HOME1);
				// "UPDATE MEMBER SET DELIVERY_ADDRESS_1=null WHERE MEM_ID=?";

				pstmt.setString(1, memId);
				System.out.println(memId);
				pstmt.executeUpdate();

			} catch (SQLException se) {
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
	
		// 刪除宅配地址 第二個
		@Override
		public void Mem_Delete_Home2(String memId) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(MEM_Delete_HOME2);
				// "UPDATE MEMBER SET DELIVERY_ADDRESS_2=null WHERE MEM_ID=?";

				pstmt.setString(1, memId);
				System.out.println(memId);
				pstmt.executeUpdate();

			} catch (SQLException se) {
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
		
		
		// 刪除宅配地址 第二個
		@Override
		public void Mem_Delete_Home3(String memId) {
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(MEM_Delete_HOME3);
				// "UPDATE MEMBER SET DELIVERY_ADDRESS_3=null WHERE MEM_ID=?";

				pstmt.setString(1, memId);
				System.out.println(memId);
				pstmt.executeUpdate();

			} catch (SQLException se) {
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
	
		//世銘打的--全站搜尋用--根據會員姓名or會員介紹or旅遊記標題or旅遊記內容or旅遊記標籤類別or旅遊記標籤名稱or揪團目的地or揪團詳情or揪團標題...搜尋
		@Override
		public List<MemberVO> SearchAll(String keyword) {
			List<MemberVO> list = new ArrayList<MemberVO>();
			MemberVO memberVO = null;
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(SEARCH_ALL);
				pstmt.setString(1, "%" + keyword + "%");
				pstmt.setString(2, "%" + keyword + "%");
				pstmt.setString(3, "%" + keyword + "%");
				pstmt.setString(4, "%" + keyword + "%");
				pstmt.setString(5, "%" + keyword + "%");
				pstmt.setString(6, "%" + keyword + "%");
				pstmt.setString(7, "%" + keyword + "%");
				pstmt.setString(8, "%" + keyword + "%");
				pstmt.setString(9, "%" + keyword + "%");
				rs = pstmt.executeQuery();

				while (rs.next()) {
					memberVO = new MemberVO();
					memberVO.setMem_Id(rs.getString("MEM_ID"));
					memberVO.setMem_Name(rs.getString("MEM_NAME"));
					memberVO.setMem_Profile(rs.getString("MEM_PROFILE"));
					memberVO.setMem_Photo(rs.getBytes("MEM_PHOTO"));
					memberVO.setMem_Reg_Date(rs.getDate("MEM_REG_DATE"));
					
					list.add(memberVO); // Store the row in the list
				}

			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
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
}
//@Override
//public MemberVO login_Member(MemberVO memberVO) {
//	Connection con = null;
//	PreparedStatement pstmt = null;
//	ResultSet rs = null;
//	MemberVO memberVO_login = null;
//
//	try {
//		con = ds.getConnection();
//		pstmt = con.prepareStatement(Login_Member);
//		pstmt.setString(1, memberVO.getMem_Account());
//		pstmt.setString(2, memberVO.getMem_Password());
//
//		rs = pstmt.executeQuery();
//
//		while (rs.next()) {
//			memberVO_login = new MemberVO();
//			memberVO_login.setMem_Account(rs.getString("setMem_Account"));
//			memberVO_login.setMem_Password(rs.getString("setMem_Password"));
//
//		}
//		// Handle any SQL errors
//	} catch (SQLException se) {
//		throw new RuntimeException("A database error occured. " + se.getMessage());
//		// Clean up JDBC resources
//	} finally {
//		if (rs != null) {
//			try {
//				rs.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (pstmt != null) {
//			try {
//				pstmt.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//			}
//		}
//	}
//	return memberVO_login;
//}
	
