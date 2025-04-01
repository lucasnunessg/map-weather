package map_weather.project.entity;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class RouteWebSocketHandler extends TextWebSocketHandler {
  private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    sessions.add(session);
    System.out.println("🟢 Cliente conectado ao WebSocket! Sessões ativas: " + sessions.size());
  }

  public void sendMessageToClients(String message) {
    System.out.println("📤 Enviando mensagem para clientes WebSocket: " + message);

    for (WebSocketSession session : sessions) {
      if (session.isOpen()) {
        try {
          session.sendMessage(new TextMessage(message));
          System.out.println("✅ Mensagem enviada com sucesso para cliente!");
        } catch (IOException e) {
          System.out.println("❌ Erro ao enviar mensagem via WebSocket: " + e.getMessage());
        }
      } else {
        System.out.println("🔴 Sessão WebSocket fechada, mensagem não enviada.");
      }
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessions.remove(session);
    System.out.println("🔴 Cliente desconectado! Sessões ativas: " + sessions.size());
  }
}

