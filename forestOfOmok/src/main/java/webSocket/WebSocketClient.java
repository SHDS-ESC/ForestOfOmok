package webSocket;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class WebSocketClient {
	
	@OnOpen
	public void onOpen(Session session) {
		System.out.println("[WebSocketClient]Client 연결 : " + session.getId());
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("[WebSocketClient]서버 응답 : " + message);
	}
	
	
}
