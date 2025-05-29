package webSocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class EndpointConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request,HandshakeResponse response) {
        System.out.println("EndPointConfigurator");
        
        // HttpSession을 UserProperties에 저장
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        if (httpSession != null) {
            System.out.println("httpSession not null");
            config.getUserProperties().put(HttpSession.class.getName(), httpSession);
        }
        else {
            System.out.println("httpSession null");
        }
        
    }
}
