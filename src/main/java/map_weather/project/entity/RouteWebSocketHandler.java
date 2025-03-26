package map_weather.project.entity;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class RouteWebSocketHandler extends TextWebSocketHandler {

  private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
    sessions.remove(session);
  }

  public void sendMessageToClients(String message) {
    for (WebSocketSession session : sessions) {
      try {
        session.sendMessage(new TextMessage(message));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
