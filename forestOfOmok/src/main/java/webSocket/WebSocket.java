package webSocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/*
 * @ServerEndPoint : @WebServlet과 달리 url 요청 접근 x
 * 					 클라이언트가 ws 프로토콜 호출로 경로 입력 후 통신 가능
 */

@ServerEndpoint("/server")
public class WebSocket {
	// 
	private static final Map<String, Session> clients = new ConcurrentHashMap<>();

	void printLog(String eventStr, Session session) {
        System.out.println("==========================");
    	System.out.println("[" + eventStr + "] : " + session.getId() + " " + session);

    	// 현재 저장된 clients 값들 출력
        System.out.println("=== 현재 접속 중인 세션 목록 ===");
        for (Map.Entry<String, Session> entry : clients.entrySet()) {
            System.out.println("세션ID: " + entry.getKey() + ", Session: " + entry.getValue());
        }
	}
	
    @OnOpen
    public void onOpen(Session session) {
    	clients.put(session.getId(), session);
    	printLog("onOpen", session);
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session.getId());
    	printLog("onClose", session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
    	printLog("onMessage", session);
    	System.out.println("message : " + message);
    	
    }
	
	@OnError
	public void onError(Session session, Throwable t) {
		
	}
	
	
//	public OmokGame getOmokGame(String gameId) {
//		return omokContainer.get(gameId);
//	}

}
