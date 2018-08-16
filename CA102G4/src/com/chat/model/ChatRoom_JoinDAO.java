package com.chat.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ChatRoom_JoinDAO implements ChatRoom_JoinDAO_Interface{
	
	private static DataSource ds = null;
	static {
		
		try {
			Context ctx = new javax.naming.InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
//	private static final String ADDCRJOIN_STMT="INSERT INTO CHATROOM_JOIN (CHATROOM_ID,JOIN_MEMID) VALUES ('CR'||LPAD(TO_CHAR(CHATROOM_SEQ.CURRVAL),6,'0'),?)";
	//新增聊天對話參加的人員
	private static final String ADDCRJOIN_STMT="INSERT INTO CHATROOM_JOIN (CHATROOM_ID,JOIN_MEMID) VALUES (?,?)";
	//將某位參與對話的人員，踢出對話
	private static final String DELCRJOIN_STMT="DELETE CHATROOM_JOIN WHERE CHATROOM_ID=? AND JOIN_MEMID=?";
	//找出我參與的對話
	private static final String FINDCR_BYMEMID_STMT="SELECT * FROM CHATROOM_JOIN WHERE JOIN_MEMID=?";
	//查詢某對話聊天，所有參與的人員
	private static final String FINDMEM_BYCRID_STMT="SELECT * FROM CHATROOM_JOIN WHERE CHATROOM_ID=?";
	
	//新增用戶到聊天對話
	@Override
	public int addMemInCR(ChatRoom_JoinVO crj) {
		int count = 0;
		Connection con = null ;
		PreparedStatement pstmt = null;
		try {
			
			con=ds.getConnection();
			pstmt=con.prepareStatement(ADDCRJOIN_STMT);
			pstmt.setString(1,crj.getChatRoom_ID());
			pstmt.setString(2,crj.getJoin_MemID());
			
			count=pstmt.executeUpdate();
			
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

	//將用戶退出聊天對話
	@Override
	public int delMemOutCR(ChatRoom_JoinVO crj) {
		int count = 0;
		Connection con = null ;
		PreparedStatement pstmt = null;
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(DELCRJOIN_STMT);
			pstmt.setString(1,crj.getChatRoom_ID());
			pstmt.setString(2,crj.getJoin_MemID());
			count=pstmt.executeUpdate();
			
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

	//查詢我所有參與的聊天室
	@Override
	public List<ChatRoom_JoinVO> findMyChatRoom(String memID) {
		List<ChatRoom_JoinVO> list = new ArrayList<>();
		ChatRoom_JoinVO cj=null;
		
		Connection con = null ;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDCR_BYMEMID_STMT);
			pstmt.setString(1,memID);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				cj=new ChatRoom_JoinVO();
				cj.setChatRoom_ID(rs.getString("CHATROOM_ID"));
				cj.setJoin_MemID(rs.getString("JOIN_MEMID"));
				list.add(cj);
			}
			
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
		
		
		return list;
	}

	
	//查詢某對話聊天，所有參與的人員
	@Override
	public List<ChatRoom_JoinVO> getJoinMem_ByChatRoom(String chatRoom_id) {
		List<ChatRoom_JoinVO> list = new ArrayList<>();
		ChatRoom_JoinVO cj=null;
		
		Connection con = null ;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDMEM_BYCRID_STMT);
			pstmt.setString(1,chatRoom_id);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				cj=new ChatRoom_JoinVO();
				cj.setChatRoom_ID(rs.getString("CHATROOM_ID"));
				cj.setJoin_MemID(rs.getString("JOIN_MEMID"));
				list.add(cj);
			}
			
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
		
		
		return list;
	}


}
