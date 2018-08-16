package com.chat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.chat.model.*;
import com.mem.model.MemberService;



public class ChatRoomServlet extends HttpServlet {


	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);	
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		res.setContentType("text/html;charset=UTF-8");
		
		
		//新增群組聊天
		if("addNewCR".equals(action)) {
			List<String> errorMsgs_Ailee = new LinkedList<>();
			req.setAttribute("errorMsgs_Ailee", errorMsgs_Ailee);
			String requestURL = req.getParameter("requestURL");
			ChatRoom_JoinService crjSvc = new ChatRoom_JoinService();

			try {
			String myMemId = req.getParameter("myMemId");
			String input_newChatName = req.getParameter("input_newChatName");
			String[] select_friList = req.getParameterValues("select_fri");

			/**************第一步接受參數，錯誤處理******************************/
			if(myMemId == null || myMemId.trim().length() == 0) {
				errorMsgs_Ailee.add("錯誤：未取到登入者的會員ID。");
			}
			
			if(input_newChatName == null || input_newChatName.trim().length() == 0 || input_newChatName.trim().length() > 12) {
				errorMsgs_Ailee.add("聊天對話名稱，請勿空白或字數請勿超過12個字");
			}
			
			if(select_friList == null || select_friList.length == 0 || select_friList.length <= 1) {
				errorMsgs_Ailee.add("新的聊天對話要加入的人員，請勿空白,且至少需要選兩位好友唷!");
			}
			
			//假設我只選擇一位朋友，我要判斷我與他的對話已經存在了嗎?存在的話，就不要在新增了?? 可能要刪掉，因為不需要了??
			if(select_friList.length == 1 ) {
				List<ChatRoom_JoinVO> myJoinCR =crjSvc.getMyChatRoom(myMemId);
				for(ChatRoom_JoinVO crjVO : myJoinCR) {
					List<ChatRoom_JoinVO> join_memid =crjSvc.getJoinMem_ByChatRoom(crjVO.getChatRoom_ID());
					if( join_memid.size() == 2) {
						for(ChatRoom_JoinVO join_list : join_memid) {
							if(join_list.getJoin_MemID().equals(select_friList[0])) {
								errorMsgs_Ailee.add("您與該位朋友的對話已經存在了唷");
							}
						}	
					}
				}
			}
			

			if(!errorMsgs_Ailee.isEmpty()) {
				RequestDispatcher failureView  = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
				return;
			}


			//*****************************排除重複提交問題**********************************//
			//session來的token
			long session_token = Long.parseLong(req.getSession().getAttribute("addCR_token")+"");
			//input來的token
			long addCR_token = Long.parseLong(req.getParameter("addCR_token"));
			System.out.println(session_token);
			System.out.println(addCR_token);
			
			if(session_token == addCR_token) {
				//代表第一次送就可以換token值了
				req.getSession().setAttribute("addCR_token",new Date().getTime());
			}else {
				RequestDispatcher failureView  = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
				return;
			}
			
			
			/***************************第二步錯誤處理完畢，準備新增新的聊天對話****************/
			ChatRoomService crSvc = new ChatRoomService();
			//因為欄位多加初始的參加人數，用以判斷是群組聊天還是一對一
			int initCnt = select_friList.length + 1;
			//先新增聊天對話及參與名單，拿到回傳的主鍵
			String pkey = crSvc.addChatRoom(input_newChatName,"",select_friList,myMemId,initCnt);

			System.out.println("新增成功新的聊天對話成功"+pkey+input_newChatName);
			
			/***************************第三步新增完畢，準備提交畫面************************/
			RequestDispatcher successView  = req.getRequestDispatcher(requestURL);
			successView.forward(req, res);
			
			}catch(Exception e) {
				errorMsgs_Ailee.add(e.getMessage());
				RequestDispatcher failureView  = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}

		}
		
		//確認OneByOne聊天對話是否存在?已存在時，回傳聊天編號、聊天對話名稱/不存在時，先新增在回傳
		if("checkOnebyOneCR".equals(action)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter out =res.getWriter();
			
			try {
				String myMemId = req.getParameter("memId");
				String friMemId = req.getParameter("friId");
				String joinCnt = req.getParameter("joinCnt");
				/**************第一步接受參數，錯誤處理******************************/
				if(myMemId == null || myMemId.trim().length() == 0) {
					out.println("錯誤：未取到登入者的會員ID。");
					return;
				}
				
				if(friMemId == null || friMemId.trim().length() == 0) {
					out.println("錯誤：未取到好友的會員ID。");
					return;
				}
				
				if(joinCnt == null || joinCnt.trim().length() == 0) {
					out.println("錯誤：未取到一對一的人數。");
					return;
				}
	
				/**************第二步 確認我與他的聊天對話是否已存在******************************/
				ChatRoom_JoinService crjSvc = new ChatRoom_JoinService();
				ChatRoomService crSvc = new ChatRoomService();
				List<ChatRoom_JoinVO> myJoinCR =crjSvc.getMyChatRoom(myMemId);
				
				if(myJoinCR.size() != 0 ) {
					for(ChatRoom_JoinVO crjVO : myJoinCR) {
						//列出某我參與的聊天對話中，參與人列表
						List<ChatRoom_JoinVO> join_memid = crjSvc.getJoinMem_ByChatRoom(crjVO.getChatRoom_ID());
						//並且找出初始參與人數為二，代表為一對一的對話
						if(join_memid.size() == 2 && crSvc.getOne_ByChatRoomID(crjVO.getChatRoom_ID()).getChatRoom_InitCNT() == 2) {
							for(ChatRoom_JoinVO join_list : join_memid) {
								if(join_list.getJoin_MemID().equals(friMemId)){
									//您與該位朋友的對話已經存在了唷
									out.println(crjVO.getChatRoom_ID()+
												","+
												crSvc.getOne_ByChatRoomID(crjVO.getChatRoom_ID()).getChatRoom_Name());
									return;
								}
							}	
						}
					}
				}
				//我參與的聊天對話為空時，或是我與該朋友沒有聊過天(1-建立聊天室 2-把參與人加入)
				MemberService memSvc = new MemberService();
				
				String RoomName = memSvc.getOneMember(myMemId).getMem_Name()+"與"+memSvc.getOneMember(friMemId).getMem_Name();
				String[] addPeople = {myMemId,friMemId};
				
				String pk = crSvc.addChatRoom(RoomName,"",addPeople,null,2);
				
				out.print(pk+","+RoomName);
				
			}catch(Exception e) {
				out.print("錯誤：發生Exception。");
			}
			
		}
		
		//來自於聊天對話視窗，查詢該聊天對話參與人員  >> 要回傳給Ajax
		if("getCRJ_List".equals(action)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter out =res.getWriter();
				
			try {
				/**************第一步接受參數，錯誤處理******************************/
				String crId = req.getParameter("chatRoomId");
				if(crId == null || crId.trim().length() == 0) {
					out.print("錯誤：未取到聊天。");
					return;
				}
				
				/**************第二步接受參數，處理查詢******************************/
				//先查出該聊天室名單
				ChatRoom_JoinService crjSvc = new ChatRoom_JoinService();
				ChatRoomService crSvc = new ChatRoomService();
				MemberService memSvc = new MemberService();
				List<ChatRoom_JoinVO> list = crjSvc.getJoinMem_ByChatRoom(crId);
				
				if(list.size() != 0) {
				
					String tempStr = "";
					
					tempStr+="[";
					for(ChatRoom_JoinVO joinVO : list) {
						String memId = joinVO.getJoin_MemID();
						String memName = memSvc.getOneMember(memId).getMem_Name();
						Integer joinInitCnt = crSvc.getOne_ByChatRoomID(crId).getChatRoom_InitCNT();
						tempStr+=("{\"ChatRoom_ID\":\""+crId+"\",\"memId\":\""+memId+"\",\"memName\":\""+memName+"\",\"joinInitCnt\":\""+joinInitCnt+"\"},");
					}
					String str = tempStr.substring(0,tempStr.length()-1);
					str+="]";
					System.out.println(str);
					
					/**************第三步回傳查詢結果******************************/
					out.println(str);
					
				}else {
					
					out.println("空");
					
				}
				
				
			}catch(Exception e) {
				out.print("錯誤：發生Exception。");
			}
		}
		
		//針對某聊天對話，踢出人員或自己退出群組 
		if("delOneCRFri".equals(action)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter out =res.getWriter();
			
			try {
				/**************第一步接受參數，錯誤處理******************************/
				String delMemId = req.getParameter("delMemId");
				String chatRoomId = req.getParameter("delfromCR");
				System.out.println(delMemId);
				if(delMemId == null || delMemId.trim().length() == 0) {
					out.print("錯誤：未取到要踢出的人的ID。");
					return;
				}
				
				if(chatRoomId == null || chatRoomId.trim().length() == 0) {
					out.print("錯誤：未取到要從哪間聊天對話ID踢出。");
					return;
				}
				
				/**************第二步錯誤驗證完，處理刪除聊天室該位人員******************************/
				ChatRoom_JoinService crjSvc = new ChatRoom_JoinService();
				ChatRoomService crSvc = new ChatRoomService();
				//當聊天的參與人員只剩1人時，要把群組聊天也刪掉....
				if(crjSvc.getJoinMem_ByChatRoom(chatRoomId).size() == 1) {
					int cnt = crjSvc.delMemOutCR(chatRoomId, delMemId);
					crSvc.delChatRoom(chatRoomId);
					out.print(cnt);
					return;
				}
				
				int count = crjSvc.delMemOutCR(chatRoomId, delMemId);
				
				/**************第三步回傳查詢結果******************************/
				out.print(count);

			}catch(Exception e) {
				out.print("錯誤：發生Exception。");
			}
		}
		
		
		//針對現有聊天對話，新增更多人
		if("AddMoreFri".equals(action)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter out =res.getWriter();
			
			try {
				/**************第一步接受參數，錯誤處理******************************/
				String chatRoom_id = req.getParameter("addMoreFri_CRId");
				String[] addMore_MemId = req.getParameterValues("select_fri_more");
				
				if(chatRoom_id == null || chatRoom_id.trim().length() == 0) {
					out.print("錯誤：未取到要加入對話的聊天室是哪間?");
					return;
				}
				if(addMore_MemId == null || addMore_MemId.length == 0) {
					out.print("錯誤：沒取到要加入哪些新朋友到聊天對話中。");
					return;
				}
				
				/**************第二步錯誤處理完畢，準備新增好友進入聊天對話*************/
				ChatRoom_JoinService crjSvc = new ChatRoom_JoinService();
				
				int insertCount = 0;
				StringBuilder successName = new StringBuilder();
				for(int i = 0; i < addMore_MemId.length ; i++) {
					int count =crjSvc.addMemInCR(chatRoom_id, addMore_MemId[i]);
					if(count == 0) {
						out.print("加入好友到聊天對話："+addMore_MemId[i]+"新增失敗");
						return;
					}
					successName.append(addMore_MemId[i]);
					insertCount++;
				}
				//比對新增比數跟要加入的人員數是否相同
				if(insertCount != addMore_MemId.length) {
					out.print("新增筆數跟要加入的好友數不同!!!");
					return;
				}
				/**************第三步回傳新增結果******************************/
				out.print("新增完成");
				
			}catch(Exception e){
				out.print("錯誤：發生Exception。");
			}
			
			
			
		}
		
		
		
		
	}

}
