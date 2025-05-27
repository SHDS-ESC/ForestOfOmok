import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class ChatServerEndpoint {
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
    	System.out.println(session.getId());
        clients.add(session);
    }

    @OnClose
    public void onClose(Session session) {
    	System.out.println(session.getId());
        clients.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
    	System.out.println(session.getId());
    	System.out.println("message : " + message);
        for (Session client : clients) {
            if (!client.equals(session)) {
                client.getBasicRemote().sendText(message);
            }
        }
    }
}