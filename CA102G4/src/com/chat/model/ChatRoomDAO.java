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

public class ChatRoomDAO implements ChatRoomDAO_Interface{

	private static DataSource ds=null;
	
	static {
		try {
			Context ctx = new javax.naming.InitialContext();
			ds=(DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		}catch(NamingException e) {
			e.printStackTrace();
		}
	}
	
	//新增聊天對話
	private static final String ADDCHATROOM_STMT="INSERT INTO CHATROOM(CHATROOM_ID,CHATROOM_NAME,CHATREC,CHATROOM_INITCNT) VALUES('CR'||LPAD(TO_CHAR(CHATROOM_SEQ.NEXTVAL),6,'0'),?,?,?)";
	
	//更新某個聊天對話
	private static final String UPDATECHATROOM_STMT="UPDATE CHATROOM SET CHATROOM_NAME=?,CHATREC=?  WHERE CHATROOM_ID=?";
	
	//刪除某個聊天對話
	private static final String DELCHATROOM_STMT="DELETE CHATROOM WHERE CHATROOM_ID=?";
	
	//查詢所有聊天對話
	private static final String FINDALLCHATROOM_STMT="SELECT * FROM CHATROOM";
	
	//查詢某個聊天對話
	private static final String FINDONECHATROOM_STMT="SELECT * FROM CHATROOM WHERE CHATROOM_ID=?";
	
	//新增聊天對話(一對一或群組時，解決交易問題)
	@Override
	public String addChatRoom(ChatRoomVO cr,String[] addPeople,String loginMemId) {
		int count = 0 ;
		Connection con = null;
		PreparedStatement pstmt = null;
		String pk = null;
		try {
			
			con=ds.getConnection();
			//處理交易問題，故要先關閉自動提交
			con.setAutoCommit(false);

			//1----先新增聊天對話，設定自增主建
			String[] col = {"CHATROOM_ID"}; 

			pstmt=con.prepareStatement(ADDCHATROOM_STMT,col);
			pstmt.setString(1, cr.getChatRoom_Name());
			pstmt.setString(2, cr.getChatRec());
			pstmt.setInt(3,cr.getChatRoom_InitCNT());
			count=pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			pk=rs.getString(1);
			System.out.println("有近來DAO2");
			
			//2----拿到自增組件，新增聊天對話名單
			for(int i = 0 ; i < addPeople.length ;i++) {
				pstmt = con.prepareStatement("INSERT INTO CHATROOM_JOIN (CHATROOM_ID,JOIN_MEMID) VALUES (?,?)");
				pstmt.setString(1, pk);
				pstmt.setString(2, addPeople[i]);
				pstmt.executeUpdate();
			}
			//3----把自己也加入進去
			if(loginMemId != null) {
				pstmt = con.prepareStatement("INSERT INTO CHATROOM_JOIN (CHATROOM_ID,JOIN_MEMID) VALUES (?,?)");
				pstmt.setString(1, pk);
				pstmt.setString(2, loginMemId);
				pstmt.executeUpdate();
			}
			System.out.println("有近來DAO3");
			//3--------新增完成----------
			con.commit();
			con.setAutoCommit(true);
			
		}catch(SQLException se) {
			if(con != null) {
				try {
					con.rollback();
				} catch (SQLException e) {
					throw new RuntimeException("rollback失敗"+e.getMessage());
				}
			}
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return pk;
	}

	//修改聊天對話(名子或聊天紀錄)
	@Override
	public int updateChatRoom(ChatRoomVO cr) {
		int count = 0 ;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(UPDATECHATROOM_STMT);
			pstmt.setString(1, cr.getChatRoom_Name());
			pstmt.setString(2, cr.getChatRec());
			pstmt.setString(3, cr.getChatRoom_ID());
			count=pstmt.executeUpdate();
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return count;
	}
	
	//刪除聊天對話(當最後一個人離開聊天對話時)
	@Override
	public int delChatRoom(String chatRoom_id) {
		int count = 0 ;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(DELCHATROOM_STMT);
			pstmt.setString(1, chatRoom_id);
			count=pstmt.executeUpdate();
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return count;
	}

	//所有聊天對話
	@Override
	public List<ChatRoomVO> getAllChatRoom() {
		List<ChatRoomVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ChatRoomVO cr = null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDALLCHATROOM_STMT);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				cr=new ChatRoomVO();
				cr.setChatRoom_ID(rs.getString("CHATROOM_ID"));
				cr.setChatRoom_Name(rs.getString("CHATROOM_NAME"));
				cr.setChatRec(rs.getString("CHATREC"));
				cr.setChatRoom_InitCNT(rs.getInt("CHATROOM_INITCNT"));
				list.add(cr);
			}
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}

	//查詢某個聊天對話
	@Override
	public ChatRoomVO getOne_ByChatRoomID(String chatRoom_id) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ChatRoomVO cr = null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDONECHATROOM_STMT);
			pstmt.setString(1,chatRoom_id);

			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				cr=new ChatRoomVO();
				cr.setChatRoom_ID(rs.getString("CHATROOM_ID"));
				cr.setChatRoom_Name(rs.getString("CHATROOM_NAME"));
				cr.setChatRec(rs.getString("CHATREC"));
				cr.setChatRoom_InitCNT(rs.getInt("CHATROOM_INITCNT"));

			}
			
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return cr;
	}

}
