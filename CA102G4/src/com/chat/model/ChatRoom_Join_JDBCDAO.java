package com.chat.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatRoom_Join_JDBCDAO implements ChatRoom_JoinDAO_Interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";
	
//	private static final String ADDCRJOIN_STMT="INSERT INTO CHATROOM_JOIN (CHATROOM_ID,JOIN_MEMID) VALUES ('CR'||LPAD(TO_CHAR(CHATROOM_SEQ.CURRVAL),6,'0'),?)";
	private static final String ADDCRJOIN_STMT="INSERT INTO CHATROOM_JOIN (CHATROOM_ID,JOIN_MEMID) VALUES (?,?)";
	private static final String DELCRJOIN_STMT="DELETE CHATROOM_JOIN WHERE CHATROOM_ID=? AND JOIN_MEMID=?";
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL,USER,PASSWORD);
			pstmt=con.prepareStatement(ADDCRJOIN_STMT);
			pstmt.setString(1,crj.getChatRoom_ID());
			pstmt.setString(2,crj.getJoin_MemID());
			
			count=pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException ("資料庫無法載入驅動程式"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL,USER,PASSWORD);
			pstmt=con.prepareStatement(DELCRJOIN_STMT);
			pstmt.setString(1,crj.getChatRoom_ID());
			pstmt.setString(2,crj.getJoin_MemID());
			count=pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException ("資料庫無法載入驅動程式"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL,USER,PASSWORD);
			pstmt=con.prepareStatement(FINDCR_BYMEMID_STMT);
			pstmt.setString(1,memID);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				cj=new ChatRoom_JoinVO();
				cj.setChatRoom_ID(rs.getString("CHATROOM_ID"));
				cj.setJoin_MemID(rs.getString("JOIN_MEMID"));
				list.add(cj);
			}
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException ("資料庫無法載入驅動程式"+ce.getMessage());
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
			Class.forName(DRIVER);	
			con=DriverManager.getConnection(URL,USER,PASSWORD);
			pstmt=con.prepareStatement(FINDMEM_BYCRID_STMT);
			pstmt.setString(1,chatRoom_id);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				cj=new ChatRoom_JoinVO();
				cj.setChatRoom_ID(rs.getString("CHATROOM_ID"));
				cj.setJoin_MemID(rs.getString("JOIN_MEMID"));
				list.add(cj);
			}
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException ("資料庫無法載入驅動程式"+ce.getMessage());
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
		
	public static void main(String args[]) {
		ChatRoom_Join_JDBCDAO dao = new ChatRoom_Join_JDBCDAO();
		
		//新增用戶到聊天對話
//		ChatRoom_JoinVO crj=new ChatRoom_JoinVO();
//		crj.setChatRoom_ID("CR000002");
//		crj.setJoin_MemID("M000009");
//		int count = dao.addMemInCR(crj);
//		System.out.println("新增用戶到聊天對話，共"+count+"筆");
		
		//將用戶退出聊天對話
//		ChatRoom_JoinVO crj1=new ChatRoom_JoinVO();
//		crj1.setChatRoom_ID("CR000001");
//		crj1.setJoin_MemID("M000001");
//		int delcount = dao.delMemOutCR(crj1);
//		System.out.println("將用戶從聊天對話踢出，共"+delcount+"筆");
		
		//查詢我所有參與的聊天室
		List<ChatRoom_JoinVO> myCR=dao.findMyChatRoom("M000003");
		for(ChatRoom_JoinVO i : myCR) {
			System.out.print(i.getChatRoom_ID()+" , ");
			System.out.println(i.getJoin_MemID());
		}
		
		
	}
	
}
