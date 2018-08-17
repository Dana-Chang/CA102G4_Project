package com.chat.controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.*;
import javax.websocket.server.*;

import org.json.JSONObject;

import com.chat.model.*;
import com.fri.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import idv.david.websocketchat.model.NoticeMessageVO;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import idv.david.websocketchat.model.NoticeMessageVO;

@ServerEndpoint("/chat/{mem_id}")
public class ChatServlet {
	//存放連線人及對應的那條seesion(ws的)
	private static Map<String,Session> sessionsMap = new ConcurrentHashMap<>();
    //private static final Set<Session> connectedSessions = Collections.synchronizedSet(new HashSet<>());
	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd kk:mm:ss").create();
	//取得JedisPool
	private static JedisPool pool = JedisPoolUtil.getJedisPool();

	
	@OnOpen
	public void onOpen(@PathParam("mem_id") String mem_id , Session userWbSession) {
		
		
		int maxBufferSize = 500 * 1024;
		userWbSession.setMaxTextMessageBufferSize(maxBufferSize);
		userWbSession.setMaxBinaryMessageBufferSize(maxBufferSize);
		
		
		//將我自己加入上線名單中。
		sessionsMap.put(mem_id,userWbSession);
		//取出目前所有上線人員的mem_id
		Set<String> onlineMemId = sessionsMap.keySet();
		//取出目前所有上線人員session
		Collection<Session> sessions = sessionsMap.values();

		
		/*****************避免讓A封鎖B時，讓B看到A是否上線**********************/
		FriendService friSvc = new FriendService();
		
		//剔除上線者將發送者封鎖的人
		for(String friMemId:onlineMemId) {
			if((friSvc.findRelationship(friMemId,mem_id)) != null && 
					(friSvc.findRelationship(friMemId,mem_id)).getFri_Stat() == 3) {
				//並沒有動到原sessionsMap。
				onlineMemId.remove(friMemId);			
			}
		}
		/*****************避免讓A封鎖B時，讓B看到A是否上線**********************/
		
		
		//把要送出去的資料先用MAP裝，再轉成JSONObject
		Map map = new HashMap();
		map.put("TYPE", "open");
		map.put("openMemId",mem_id);
		map.put("online",onlineMemId);
		JSONObject json = new JSONObject(map);

		
		//告訴所有人我上線了
		for(Session session : sessions) {
			if(session != null && session.isOpen()) {
				session.getAsyncRemote().sendText(json.toString());
			}
		}

		String text = String.format("WSSession ID = %s, connected; mem_id = %s", userWbSession.getId(), mem_id);
		System.out.println(text);
	}
	
	
	
	//收到訊息時會觸發
	@OnMessage
	public void onMessage(Session userSession, String message) {
		
		//*************************華霖的通知****************************//
		if(message.contains("title")) {
			NoticeMessageVO noticeMessage = gson.fromJson(message, NoticeMessageVO.class);
			String sender = noticeMessage.getSender();
			String receiver = noticeMessage.getReceiver();
			
			Session receiverSession = sessionsMap.get(receiver);
			if (receiverSession != null && receiverSession.isOpen()) {
				receiverSession.getAsyncRemote().sendText(message);
			}
			Jedis jedis = pool.getResource();
			jedis.rpush(receiver,message);
			jedis.close();
			System.out.println("這是通知:"+message);
			return;
		}
		//*************************華霖的通知****************************//

		
		//取出傳來的訊息，擷取裡面要發送的聊天對話編號
		JsonObject jsonObject = gson.fromJson(message,JsonObject.class);
		String chatRoom_id = jsonObject.get("TO_CHATROOMID").getAsString();
		String sendMemId = jsonObject.get("MEM_ID").getAsString();  //發送者ID
		String type = jsonObject.get("TYPE").getAsString(); //訊息種類ID
		
		if("chat".equals(type)) {
			//透過Service去查詢，該聊天對話參與人員
			ChatRoom_JoinService chatRoom_JoinSvc = new ChatRoom_JoinService();
			List<ChatRoom_JoinVO> joinList = chatRoom_JoinSvc.getJoinMem_ByChatRoom(chatRoom_id);
			
			//透過Service去查詢，該聊天對話初始參與人數來判斷是否為群組聊天還是一對一
			ChatRoomService crSvc = new ChatRoomService();
			
			System.out.println(message);
			
			/*若發送者發送給聊天對話時，裡面參與人員只有發送者跟另外一位朋友的且是一對一聊天(群組不擋封鎖)
			     要多判斷對方是否是把發送者封鎖的??若封鎖將只會發送訊息給他自己而已*/
			if(joinList.size() == 2 && crSvc.getOne_ByChatRoomID(chatRoom_id).getChatRoom_InitCNT() == 2) {
				for(ChatRoom_JoinVO crjVO : joinList) {
					if(!crjVO.getJoin_MemID().equals(sendMemId)) {
						//當為另外一位參與對話的人員時,判斷他是否把發送者封鎖了
						FriendService friSvc = new FriendService();
						Friend fri = friSvc.findRelationship(crjVO.getJoin_MemID(),sendMemId);
						//發送者被接收者封鎖了，所以只能傳訊息給自己(為了不讓發送者發現被封鎖)
						if(fri.getFri_Stat() == 3) {	
							Session receiverSession = sessionsMap.get(sendMemId); 
							if(receiverSession != null && receiverSession.isOpen()) {
								receiverSession.getAsyncRemote().sendText(message);	
							}
							
							System.out.println("因為我自己被封鎖了，所以我只收得到自己的訊息" + message);
							/*********這邊是不是要存訊息到Redis????再思考一下下**************/
							Jedis jedis = pool.getResource();
							jedis.rpush(chatRoom_id,message);
							jedis.close();
							
							return;	
						}
					}
				}
	
				jsonObject.addProperty("CNT", "2");
				message=jsonObject.toString();
			}
			//判斷發送者是否還在參與名單內
			int count = 0 ;
			for(ChatRoom_JoinVO crjVO : joinList) {
				if(sendMemId.equals(crjVO.getJoin_MemID())){
					count++;
				}
			}
			if(count == 0) {
				Map map = new HashMap();
				map.put("TYPE", "reject");
				map.put("MEM_ID",sendMemId);
				map.put("TO_CHATROOMID",chatRoom_id);
				map.put("MSG","你已被退出群組，即將重新整理頁面");
				JSONObject json = new JSONObject(map);
				
				Session receiverSession = sessionsMap.get(sendMemId);
				if(receiverSession != null && receiverSession.isOpen()) {
					receiverSession.getAsyncRemote().sendText(json.toString());
				}	
				return;
			}
			
			for(ChatRoom_JoinVO crjVO : joinList) {
				//藉由參與名單，去Map裡找該人員使用的對應WSSession
				Session receiverSession = sessionsMap.get(crjVO.getJoin_MemID()); 
				if(receiverSession != null && receiverSession.isOpen()) {
					receiverSession.getAsyncRemote().sendText(message);				
				}
			}
			
			/*********前面發送完畢後，存訊息到Redis**************/
			Jedis jedis = pool.getResource();
			jedis.rpush(chatRoom_id,message);
			jedis.close();
		}
		
		//當請求訊息為歷史訊息時，要到jedis取出
		if("history".equals(type)) {
			Jedis jedis = pool.getResource();
			List<String> historyData = jedis.lrange(chatRoom_id, 0, jedis.llen(chatRoom_id) - 1);
			String historyMsg = gson.toJson(historyData)
													.replace("\\","")
													.replace("[\"{","[{")
													.replace("}\",\"{", "},{")
													.replaceAll("}\"]", "}]");
			System.out.println("要回傳的歷史訊息:"+historyMsg);
			Map map = new LinkedHashMap();
			map.put("TYPE", "history");
			map.put("HISTORYMSG",historyMsg);
			JSONObject json = new JSONObject(map);
			
			Session receiverSession = sessionsMap.get(sendMemId); 
			if(receiverSession != null && receiverSession.isOpen()) {
				receiverSession.getAsyncRemote().sendText(json.toString());				
			}
			jedis.close();
		}

		System.out.println("Message received: " + message);
	}

	
	
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		//String memIdClose = null ;
		Set<String> memId_List = sessionsMap.keySet();
		for(String memid : memId_List) {
			if(sessionsMap.get(memid).equals(userSession)) {
				//memIdClose = memid;
				
				Map map = new LinkedHashMap();
				map.put("TYPE", "close");
				map.put("closeMemId",memid);
				JSONObject json = new JSONObject(map);
				
				Collection<Session> sessions = sessionsMap.values();
				for(Session session : sessions) {
					if(session != null && session.isOpen()) {
						session.getAsyncRemote().sendText(json.toString());
					}
				}
				sessionsMap.remove(memid);//把這個斷線的會員從Map刪除
			}
		}
		
		
		
		//connectedSessions.remove(userSession);
		String text = String.format("webSocket關閉：session ID = %s, disconnected; close code = %d; reason phrase = %s",
				userSession.getId(), reason.getCloseCode().getCode(), reason.getReasonPhrase());
		System.out.println(text);
	}

	@OnError
	public void onError(Session userSession, Throwable e) {
		System.out.println("Error: " + e.toString());
	}


}
