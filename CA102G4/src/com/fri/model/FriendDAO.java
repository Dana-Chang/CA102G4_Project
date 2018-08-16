package com.fri.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class FriendDAO implements FriendDAO_interface{
	
	private static DataSource ds = null;
	
	static {
		try {
			Context ctx = new javax.naming.InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
			
		}catch(NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	//送出好友邀請時
	private static final String ADDFRI_STMT=
			"INSERT INTO FRIEND(FRI_ID,MEMID_SELF,MEMID_FRI,FRI_STAT) VALUES('FRI'||LPAD(TO_CHAR(FRIEND_SEQ.NEXTVAL),6,0),?,?,?)";
	//成為好友時
	private static final String UPDATE_FRISTAT_STMT=
			"UPDATE FRIEND SET FRI_STAT=2,FRI_TIME=SYSDATE WHERE MEMID_SELF=? AND MEMID_FRI=?";
	//封鎖好友時
	private static final String UPDATE_FRIBLOCK_STMT=
			"UPDATE FRIEND SET FRI_STAT=? WHERE MEMID_SELF=? AND MEMID_FRI=?";
	//解除好友關係時
	private static final String DELFRI_STMT=
			"DELETE FROM FRIEND WHERE MEMID_SELF=? AND MEMID_FRI=?";
	//查詢我所有的好友
	private static final String FINDMYFRI_STMT=
			"SELECT * FROM FRIEND WHERE MEMID_SELF=? AND FRI_STAT=?";
	//查詢最近新增好友
	private static final String FINDMYNEWFRI_STMT=
			"SELECT * FROM FRIEND WHERE MEMID_SELF=? AND FRI_STAT=2 AND FRI_TIME BETWEEN SYSDATE-31 AND SYSDATE";
	//查詢當月壽星好友(可能會拿掉,因為有join)
	private static final String FINDMYBIRFRI_STMT=
			"SELECT * FROM FRIEND,MEMBER WHERE MEMID_FRI=MEM_ID AND MEMID_SELF=? AND FRI_STAT=2 AND to_Char(MEMBER.MEM_BIRTHDAY,'mm')=to_Char(SYSDATE,'mm')";
	//查詢好友之間的關係(一筆)
	private static final String FINDRELATION_STMT=
			"SELECT * FROM FRIEND WHERE MEMID_SELF=? AND MEMID_FRI=?";
	
	
	//新增好友邀請
	@Override
	public int addFri(Friend friend) {
		int count=0;
		Connection con = null;
		PreparedStatement pstmt=null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(ADDFRI_STMT);
			
			pstmt.setString(1,friend.getMemID_Self());
			pstmt.setString(2,friend.getMemID_Fri());
			pstmt.setInt(3,friend.getFri_Stat());
			
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
		return count;
	}
	
	//更新好友狀態(成為好友設定狀態及設定成為好友時間)
	@Override
	public int updateFriStat(Friend friend) {
		int count =0;
		Connection con=null;
		PreparedStatement pstmt=null;
		
		try {
			con=ds.getConnection();
			pstmt=con.prepareStatement(UPDATE_FRISTAT_STMT);
			
			pstmt.setString(1,friend.getMemID_Self());
			pstmt.setString(2,friend.getMemID_Fri());
			
			pstmt.executeUpdate();
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return count;
	}
	
	//更新好友狀態(封鎖或解除封鎖時)
	@Override
	public int updateFriBlock(Friend friend) {
		int count = 0 ;
		Connection con= null;
		PreparedStatement pstmt=null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(UPDATE_FRIBLOCK_STMT);
			
			pstmt.setInt(1,friend.getFri_Stat());
			pstmt.setString(2,friend.getMemID_Self());
			pstmt.setString(3,friend.getMemID_Fri());
			
			pstmt.executeUpdate();
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return count;
	}
	
	//刪除好友(處理交易問題)
	@Override
	public int deleteFri(String memID_self,String memID_Fri) {
		int count=0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con=ds.getConnection();
			
			// 1●設定於 pstm.executeUpdate()之前
			con.setAutoCommit(false);
			
			//先刪A對B
			pstmt=con.prepareStatement(DELFRI_STMT);
			pstmt.setString(1, memID_self);
			pstmt.setString(2, memID_Fri);
			pstmt.executeUpdate();
			
			//再刪B對A
			pstmt=con.prepareStatement(DELFRI_STMT);
			pstmt.setString(1,memID_Fri);
			pstmt.setString(2,memID_self);
			pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true);

		}catch(SQLException se) {
			if(con != null) {
				try {
					con.rollback();
				} catch (SQLException e) {
					throw new RuntimeException("rollback失敗 "+e.getMessage());
				}
			}
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return count;
	}
	
	//查詢好友名單搭配好友狀態(封鎖或為好友關係)
	@Override
	public List<Friend> findMyFri(String memID_self, Integer status) {
		List<Friend> list = new ArrayList<>();
		Friend friend = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDMYFRI_STMT);
			pstmt.setString(1,memID_self);
			pstmt.setInt(2, status);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				friend = new Friend();
				friend.setFri_ID(rs.getString("FRI_ID"));
				friend.setMemID_Self(rs.getString("MEMID_SELF"));
				friend.setMemID_Fri(rs.getString("MEMID_FRI"));
				friend.setFri_Time(rs.getDate("FRI_TIME"));
				friend.setFri_Stat(rs.getInt("FRI_STAT"));
				list.add(friend);
			}
			
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(rs != null) {
				try {
					rs.close();
				}catch(SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	
	//查詢好友名單(最近新增)
	@Override
	public List<Friend> findMyNewFri(String memID_self) {
		List<Friend> list = new ArrayList<>();
		Friend friend = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDMYNEWFRI_STMT);
			pstmt.setString(1,memID_self);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				friend = new Friend();
				friend.setFri_ID(rs.getString("FRI_ID"));
				friend.setMemID_Self(rs.getString("MEMID_SELF"));
				friend.setMemID_Fri(rs.getString("MEMID_FRI"));
				friend.setFri_Time(rs.getDate("FRI_TIME"));
				friend.setFri_Stat(rs.getInt("FRI_STAT"));
				list.add(friend);
			}
			
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(rs != null) {
				try {
					rs.close();
				}catch(SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	
	//(可能會拿掉,因為有join)
	@Override
	public List<Friend> findMyBirFri(String memID_self) {
		List<Friend> list = new ArrayList<>();
		Friend friend = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDMYBIRFRI_STMT);
			pstmt.setString(1,memID_self);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				friend = new Friend();
				friend.setFri_ID(rs.getString("FRI_ID"));
				friend.setMemID_Self(rs.getString("MEMID_SELF"));
				friend.setMemID_Fri(rs.getString("MEMID_FRI"));
				friend.setFri_Time(rs.getDate("FRI_TIME"));
				friend.setFri_Stat(rs.getInt("FRI_STAT"));
				list.add(friend);
			}
			
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(rs != null) {
				try {
					rs.close();
				}catch(SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	//查詢好友之間的關係(一筆)
	@Override
	public Friend findRelation(String memID_self, String memID_Fri) {
		Connection con = null ;
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		Friend friend = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FINDRELATION_STMT);
			pstmt.setString(1, memID_self);
			pstmt.setString(2, memID_Fri);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				friend = new Friend();
				friend.setFri_ID(rs.getString("FRI_ID"));
				friend.setMemID_Self(rs.getString("MEMID_SELF"));
				friend.setMemID_Fri(rs.getString("MEMID_FRI"));
				friend.setFri_Time(rs.getDate("FRI_TIME"));
				friend.setFri_Stat(rs.getInt("FRI_STAT"));
			}
			
			
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			try {
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return friend;
	}

	//確認他人好友邀請
	@Override
	public int becomeFri(String memID_self, String memID_Fri) {
		int count = 0 ;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			
			//處理交易前要先關掉自動提交
			con.setAutoCommit(false);
			
			//先執行接受他人好友--新增好友
			pstmt = con.prepareStatement(ADDFRI_STMT);
			pstmt.setString(1, memID_self);
			pstmt.setString(2, memID_Fri);
			pstmt.setInt(3, 2);
			pstmt.executeUpdate();
			//先執行接受他人好友--新增好友後更新成為好友時間
			pstmt = con.prepareStatement(UPDATE_FRISTAT_STMT);
			pstmt.setString(1, memID_self);
			pstmt.setString(2, memID_Fri);
			pstmt.executeUpdate();
			//再執行更改送出好友邀請的人，更改狀態
			pstmt = con.prepareStatement(UPDATE_FRISTAT_STMT);
			pstmt.setString(1, memID_Fri);
			pstmt.setString(2, memID_self);
			pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true);
			count++;
			
		}catch(Exception e){
			if( con != null) {
				try {
					con.rollback();
				} catch (SQLException sw) {
					throw new RuntimeException("Rollback失敗"+e.getMessage());
				}
			}
			throw new RuntimeException("資料庫發生錯誤"+e.getMessage());
		}finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		
		return count;

	}

	
	//取消他人好友邀請
	@Override
	public int rejectFri(String memID_self, String memID_Fri) {
		int count=0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con=ds.getConnection();


			pstmt=con.prepareStatement(DELFRI_STMT);
			pstmt.setString(1, memID_Fri);
			pstmt.setString(2, memID_self);
			pstmt.executeUpdate();
			
			count++;

		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return count;
	}

	

}
