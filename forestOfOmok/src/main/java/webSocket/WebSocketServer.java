package webSocket;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import model.User;

/*
 * @ServerEndPoint : @WebServlet과 달리 url 요청 접근 x
 * 					 클라이언트가 ws 프로토콜 호출로 경로 입력 후 통신 가능
 */
@ServerEndpoint(value="/ws", configurator=EndpointConfigurator.class)
public class WebSocketServer {
	
	// Field
		// 게임 ID별 유저 세션 저장 gameId, {session, session}
	private static final Map<String, Set<Session>> gameSessions = new ConcurrentHashMap<>();
	
	public static int cnt = 0;
	
	// getter
	public Set<Session> getSessionSet(String gameId) {
		return gameSessions.get(gameId);
	}
	
	// Event
		// 로그인 이후 DB에서 userId로 검색해서 유저 객체 (DTO가 맞는지??) 만들고 세션에 저장
	@OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        // HttpSession 꺼내기
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (httpSession != null) {
            String userId = (String) httpSession.getAttribute("userId");
            String gameId = (String) httpSession.getAttribute("gameId");
            System.out.println("[WebSocketServer]웹소켓 연결 유저 ID: " + userId + " gameId: " + gameId);
            
            // user 객체 생성
        	User user = new User();
            user.setUserId(userId);
            user.setGameId(gameId);
        	session.getUserProperties().put("user", user);
        	
            // 1:1 게임방의 세션 Set에 현재 세션 추가
            gameSessions.computeIfAbsent(gameId, k -> ConcurrentHashMap.newKeySet()).add(session);
            
        	printLog("onOpen", session);
            
        } else {
            System.out.println("[WebSocketServer]HttpSession이 없습니다.");
        }
        ++cnt;
        printLog("onOpen", session);
    }
    
	@OnClose
	public void onClose(Session session) {
	    // 1. 세션에서 유저 객체 추출
	    User user = (User) session.getUserProperties().get("user");
	    String gameId = null;
	    
	    if (user != null) {
	        // 2. 유저 객체에서 gameId 추출 (UserDTO에 gameId 필드가 있어야 함)
	        gameId = user.getGameId();
	    }
	    
	    if (gameId != null) {
	        // 3. 해당 gameId의 세션 Set 가져오기
	        Set<Session> sessions = gameSessions.get(gameId);
	        
	        if (sessions != null) {
	            // 4. 현재 세션 제거
	            sessions.remove(session);
	            
	            // 5. 세션 Set이 비었으면 gameId 삭제
	            if (sessions.isEmpty()) {
	                gameSessions.remove(gameId);
	                System.out.println("[게임방 삭제] gameId: " + gameId);
	            }
	        }
	    }
	    --cnt;
	    printLog("onClose", session);
	}


	
//	@OnClose
//	public void onClose(Session session, EndpointConfig config) {
//        // HttpSession 꺼내기
//        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
//        UserDTO user = (UserDTO) session.getUserProperties().get("user");
//	    String gameId = null;
//	    if (user != null) {
////	        gameId = user.getGameId();
//	    	gameId = (String) httpSession.getAttribute("gameId");
//	    }
//	    if (gameId != null) {
//	        Set<Session> set = gameSessions.get(gameId);
//	        if (set != null) {
//	            set.remove(session);
//	            if (set.isEmpty()) {
//	                gameSessions.remove(gameId);
//	            }
//	        }
//	    }
//	    printLog("onClose", session);
//	}
	
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("[WS.onMsg]"+message);
	    // 1. JSON 파싱 (org.json 사용 예시)
	    JSONObject json = new JSONObject(message);
	    String gameId = json.getString("gameId");
	    String msg = json.getString("message");
	    
	    // 2. 해당 gameId의 세션 Set을 가져옴
	    Set<Session> players = gameSessions.get(gameId);

	    if (players != null) {
	        // 3. 각 세션에 메시지 전송
	        for (Session player : players) {
	            if (player.isOpen()) {
	                player.getBasicRemote().sendText(message); // 원본 메시지 그대로 전송
	                // 또는 player.getBasicRemote().sendText(msg); // 메시지 내용만 전송
	            }
	        }
	    }
	    printLog("onMessage", session);
	}

	
	@OnError
	public void onError(Session session, Throwable t) {
	    System.err.println("[ERROR] WebSocket Error: " + t.getMessage());
	    t.printStackTrace();
	}
	
	
	void printLog(String eventStr, Session session) {
        System.out.println("==========================");
    	System.out.println("[" + eventStr + "] : " + session.getId() + " " + session);

    	// 현재 저장된 userContainer 값들 출력
        System.out.println("=== 현재 접속 중인 세션 목록 ===");
        for (Map.Entry<String, Set<Session>> entry : gameSessions.entrySet()) {
            String gameId = entry.getKey();
            Set<Session> sessions = entry.getValue();

            System.out.println("Game ID: " + gameId);
            for (Session sess : sessions) {
                System.out.println(" - Session: " + sess.getId());
            }
        }
	}

	
}
