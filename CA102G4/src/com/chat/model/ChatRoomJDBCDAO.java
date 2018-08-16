package com.chat.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomJDBCDAO implements ChatRoomDAO_Interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL="jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER="CA102G4";
	private static final String PASSWORD="12345678";
	
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
	
	//新增聊天對話
	@Override
	public String addChatRoom(ChatRoomVO cr,String[] addPeople,String loginMemId) {
		int count = 0 ;
		Connection con = null;
		PreparedStatement pstmt = null;
		String pk = null;
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
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
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("資料庫無法載入驅動"+ce.getMessage());
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

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(UPDATECHATROOM_STMT);
			pstmt.setString(1, cr.getChatRoom_Name());
			pstmt.setString(2, cr.getChatRec());
			pstmt.setString(3, cr.getChatRoom_ID());
			count=pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("資料庫無法載入驅動程式"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(DELCHATROOM_STMT);
			pstmt.setString(1, chatRoom_id);
			count=pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入資料庫驅動程式"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(FINDALLCHATROOM_STMT);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				cr=new ChatRoomVO();
				cr.setChatRoom_ID(rs.getString("CHATROOM_ID"));
				cr.setChatRoom_Name(rs.getString("CHATROOM_NAME"));
				cr.setChatRec(rs.getString("CHATREC"));
				list.add(cr);
			}
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("無法載入資料庫驅動程式"+ce.getMessage());
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
	@Override //查詢某個聊天對話
	public ChatRoomVO getOne_ByChatRoomID(String chatRoom_id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ChatRoomVO cr = null;
		
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(FINDONECHATROOM_STMT);
			pstmt.setString(1,chatRoom_id);

			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				cr=new ChatRoomVO();
				cr.setChatRoom_ID(rs.getString("CHATROOM_ID"));
				cr.setChatRoom_Name(rs.getString("CHATROOM_NAME"));
				cr.setChatRec(rs.getString("CHATREC"));

			}
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException ("資料庫無法載入驅動程式"+ce.getMessage());
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
	
	
	public static void main(String args[]) {
		ChatRoomJDBCDAO dao = new ChatRoomJDBCDAO();
		
		//新增聊天對話(第一次建立聊天室時)  --- 不能用喔 因為有改
//		ChatRoomVO cr = new ChatRoomVO();
//		cr.setChatRoom_Name("測試");
//		cr.setChatRec("");
//		String count = dao.addChatRoom(cr);
//		System.out.println("新增了"+count+"聊天對話");
		
		//修改聊天對話(名子或聊天紀錄)
		ChatRoomVO cr1 = new ChatRoomVO();
		cr1.setChatRoom_ID("CR000001");
		cr1.setChatRoom_Name("Aile與Eddie的聊天室");
		cr1.setChatRec("[{'MEM_ID':'M000003','MSG':'我愛足球','TIME':'2018-07-23 01:00:00'},{'MEM_ID':'M000010','MSG':'我愛美女','TIME':'2018-07-23 01:00:00'},{'MEM_ID':'M000010','MSG':'YAYAYA','TIME':'2018-07-23 01:00:00'}]");
		int update_Count = dao.updateChatRoom(cr1);
		System.out.println("更新聊天對話的資訊，共"+update_Count+"筆");
		
		//刪除聊天對話(當最後一個人離開聊天對話時)
		int delCount=dao.delChatRoom("CR000004");
		System.out.println("刪除"+delCount+"筆聊天對話");
		
		//所有聊天對話
		List<ChatRoomVO> all=dao.getAllChatRoom();
		for(ChatRoomVO i : all ) {
			System.out.print(i.getChatRoom_ID()+" , ");
			System.out.print(i.getChatRoom_Name()+" , ");
			System.out.println(i.getChatRec());
		}
		
	}

	


	
}
