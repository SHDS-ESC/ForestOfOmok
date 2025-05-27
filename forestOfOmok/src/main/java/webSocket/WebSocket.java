package webSocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import model.OmokGame;

@ServerEndpoint("/server")
public class WebSocket {
	
	Map<String, OmokGame> omokContainer = new HashMap<String, OmokGame>();
	
	@OnOpen
	public void HandleOpen(Session session) {
		System.out.println("open");
	}
	
	@OnMessage
	public String handleMessage(String message, Session session) throws IOException {
		System.out.println(message);
		return message;
	}
	
	@OnClose
	public void handleClose(Session session) {
		System.out.println("disconnected...");
	}
	
	
	
	
	public OmokGame getOmokGame(String gameId) {
		return omokContainer.get(gameId);
	}

}
