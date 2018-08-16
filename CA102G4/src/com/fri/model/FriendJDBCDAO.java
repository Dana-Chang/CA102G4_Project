package com.fri.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendJDBCDAO implements FriendDAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL="jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER="CA102G4";
	private static final String PASSWORD="12345678";
	
	//送出好友邀請時
	private static final String ADDFRI_STMT="INSERT INTO FRIEND(FRI_ID,MEMID_SELF,MEMID_FRI,FRI_STAT) VALUES('FRI-'||LPAD(TO_CHAR(FRIEND_SEQ.NEXTVAL),6,0),?,?,?)";
	//成為好友時
	private static final String UPDATE_FRISTAT_STMT="UPDATE FRIEND SET FRI_STAT=2,FRI_TIME=SYSDATE WHERE MEMID_SELF=? AND MEMID_FRI=?";
	//封鎖好友時
	private static final String UPDATE_FRIBLOCK_STMT="UPDATE FRIEND SET FRI_STAT=? WHERE MEMID_SELF=? AND MEMID_FRI=?";
	//解除好友關係時
	private static final String DELFRI_STMT="DELETE FROM FRIEND WHERE MEMID_SELF=? AND MEMID_FRI=?";
	//查詢我所有的好友
	private static final String FINDMYFRI_STMT="SELECT * FROM FRIEND WHERE MEMID_SELF=? AND FRI_STAT=?";
	//查詢最近新增好友
	private static final String FINDMYNEWFRI_STMT="SELECT * FROM FRIEND WHERE MEMID_SELF=? AND FRI_STAT=2 AND FRI_TIME BETWEEN SYSDATE-31 AND SYSDATE";
	//查詢當月壽星好友(可能會拿掉,因為有join)
	private static final String FINDMYBIRFRI_STMT="SELECT * FROM FRIEND,MEMBER WHERE MEMID_FRI=MEM_ID AND MEMID_SELF=? AND FRI_STAT=2 AND to_Char(MEMBER.MEM_BIRTHDAY,'mm')=to_Char(SYSDATE,'mm')";
	
	//查詢好友之間的關係(一筆)
	private static final String FINDRELATION_STMT=
			"SELECT * FROM FRIEND WHERE MEMID_SELF=? AND MEMID_FRI=?";
	
	
	
	@Override
	public int addFri(Friend friend) {
		int count=0;
		Connection con = null;
		PreparedStatement pstmt=null;
		
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(ADDFRI_STMT);
			
			pstmt.setString(1,friend.getMemID_Self());
			pstmt.setString(2,friend.getMemID_Fri());
			pstmt.setInt(3,friend.getFri_Stat());
			
			count=pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入資料庫驅動程式"+ce.getMessage());
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
	
	

	@Override
	public int updateFriStat(Friend friend) {
		int count =0;
		Connection con=null;
		PreparedStatement pstmt=null;
		
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(UPDATE_FRISTAT_STMT);
			
			pstmt.setString(1,friend.getMemID_Self());
			pstmt.setString(2,friend.getMemID_Fri());
			
			pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入資料庫驅動程式"+ce.getMessage());
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
	
	

	@Override
	public int updateFriBlock(Friend friend) {
		int count = 0 ;
		Connection con= null;
		PreparedStatement pstmt=null;
		
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL,USER,PASSWORD);
			pstmt=con.prepareStatement(UPDATE_FRIBLOCK_STMT);
			
			pstmt.setInt(1,friend.getFri_Stat());
			pstmt.setString(2,friend.getMemID_Self());
			pstmt.setString(3,friend.getMemID_Fri());
			
			pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入驅動程式"+ce.getMessage());
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
	
	
	
	@Override
	public int deleteFri(String memID_self,String memID_Fri) {
		int count=0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL,USER,PASSWORD);
			pstmt=con.prepareStatement(DELFRI_STMT);
			
			pstmt.setString(1, memID_self);
			pstmt.setString(2, memID_Fri);
			
			pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入資料庫驅動程式"+ce.getMessage());
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
	

	@Override
	public List<Friend> findMyFri(String memID_self, Integer status) {
		List<Friend> list = new ArrayList<>();
		Friend friend = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
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
			
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入資料庫驅動程式"+ce.getMessage());
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
	

	@Override
	public List<Friend> findMyNewFri(String memID_self) {
		List<Friend> list = new ArrayList<>();
		Friend friend = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
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
			
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入資料庫驅動程式"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
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
			
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入資料庫驅動程式"+ce.getMessage());
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
		Friend friend = null ;
		
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FINDRELATION_STMT);
			pstmt.setString(1, memID_self);
			pstmt.setString(2, memID_Fri);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				friend = new Friend();
				friend.setFri_ID(rs.getString("FRI_ID"));
				friend.setMemID_Self(rs.getString("MEMID_SELF"));
				friend.setMemID_Fri(rs.getString("MEMID_FRI"));
				friend.setFri_Time(rs.getDate("FRI_TIME"));
				friend.setFri_Stat(rs.getInt("FRI_STAT"));
			}
			
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException ("無法載入資料庫驅動程式"+ce.getMessage());
		}catch(SQLException se) {
			throw new RuntimeException ("資料庫發生錯誤"+se.getMessage());
		}finally {
			try {
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pstmt!=null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(con!=null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return friend;
	}


	/**********待補*************/
	@Override
	public int rejectFri(String memID_self, String memID_Fri) {

		int count=0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			
			pstmt=con.prepareStatement(DELFRI_STMT);
			pstmt.setString(1, memID_Fri);
			pstmt.setString(2, memID_self);
			pstmt.executeUpdate();
			
			count++;

		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入資料庫驅動程式"+ce.getMessage());
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



	@Override
	public int becomeFri(String memID_self, String memID_Fri) {
		int count = 0 ;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			
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



	public static void main(String args[]) {
		FriendDAO dao=new FriendDAO();
		
		//新增一筆送出的好友(包含好友狀態)
		Friend insertFri =new Friend();
		insertFri.setMemID_Self("M000006");
		insertFri.setMemID_Fri("M000007");
		insertFri.setFri_Stat(1);
		int addCount=dao.addFri(insertFri);
		System.out.println("已新增了"+addCount+"筆");
		
		//若對方回覆接受好友邀請時，更改好友狀態
		Friend upFriStat =new Friend();
		upFriStat.setMemID_Self("M000006");
		upFriStat.setMemID_Fri("M000007");
		upFriStat.setFri_Stat(1);
		dao.updateFriStat(upFriStat);
		
		Friend insertFri_Acc =new Friend();
		insertFri_Acc.setMemID_Self("M000007");
		insertFri_Acc.setMemID_Fri("M000006");
		insertFri_Acc.setFri_Stat(2);
		dao.addFri(insertFri_Acc);
		
		dao.updateFriStat(insertFri);
		
		//封鎖好友或解除好友
		Friend blockFri =new Friend();
		insertFri.setMemID_Self("M000006");
		insertFri.setMemID_Fri("M000007");
		insertFri.setFri_Stat(3);
		dao.updateFriBlock(blockFri);
		
		//刪除好友
		dao.deleteFri("M000007","M000006");
		dao.deleteFri("M000006", "M000007");
		
		System.out.println("==============查詢我的好友或封鎖名單======================");
		//查詢我的好友及封鎖名單
		List<Friend> myFrilist = dao.findMyFri("M000003", 2);
		for(Friend myfir : myFrilist) {
			System.out.print(myfir.getFri_ID()+",");
			System.out.print(myfir.getMemID_Self()+",");
			System.out.print(myfir.getMemID_Fri()+",");
			System.out.print(myfir.getFri_Time()+",");
			System.out.print(myfir.getFri_Stat());
			System.out.println();
		}
		System.out.println("===============查詢我最近一個月新增的好友=====================");
		
		
		//查詢我最近一個月新增的好友
		List<Friend> myNewFriList = dao.findMyNewFri("M000003");
		for(Friend newFri : myNewFriList) {
			System.out.print(newFri.getFri_ID()+",");
			System.out.print(newFri.getMemID_Self()+",");
			System.out.print(newFri.getMemID_Fri()+",");
			System.out.print(newFri.getFri_Time()+",");
			System.out.print(newFri.getFri_Stat());
			System.out.println();
		}
		System.out.println("=============查詢當月壽星(可能會拿掉,因為有join)=======================");
		
		//查詢當月壽星(可能會拿掉,因為有join)
		List<Friend> myBirFriList = dao.findMyBirFri("M000003");
		for(Friend birFri:myBirFriList) {
			System.out.print(birFri.getFri_ID()+",");
			System.out.print(birFri.getMemID_Self()+",");
			System.out.print(birFri.getMemID_Fri()+",");
			System.out.print(birFri.getFri_Time()+",");
			System.out.print(birFri.getFri_Stat());
			System.out.println();
		}
		
	}





}
