package map_weather.project.entity;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import map_weather.project.entity.RouteWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final RouteWebSocketHandler routeWebSocketHandler;

  public WebSocketConfig(RouteWebSocketHandler routeWebSocketHandler) {
    this.routeWebSocketHandler = routeWebSocketHandler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(routeWebSocketHandler, "/ws/routes")
        .setAllowedOrigins("*");
  }
}
