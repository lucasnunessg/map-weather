package map_weather.project.controller;

import map_weather.project.entity.RouteWebSocketHandler;
import map_weather.project.entity.WebSocketConfig;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/routes")
public class WebSocketController {


  private final RouteWebSocketHandler routeWebSocketHandler;

  public WebSocketController(RouteWebSocketHandler routeWebSocketHandler) {
    this.routeWebSocketHandler = routeWebSocketHandler;
  }

  @PostMapping("/send-message")
  public String sendMessage(@RequestBody String message) {
    routeWebSocketHandler.sendMessageToClients(message);
    return "Message send: " + message;
  }
}
