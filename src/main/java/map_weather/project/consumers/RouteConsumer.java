package map_weather.project.consumers;

import map_weather.project.entity.RouteWebSocketHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RouteConsumer {

  private final RouteWebSocketHandler routeWebSocketHandler;

      public RouteConsumer(RouteWebSocketHandler routeWebSocketHandler) {
      this.routeWebSocketHandler = routeWebSocketHandler;
      }

  @KafkaListener(topics = {"routes-topic", "weather-topic", "traffic-topic"}, groupId = "my-group")
  public void consumeMultipleTopics(String message) {
        routeWebSocketHandler.sendMessageToClients(message);
    System.out.println("Mensagem recebida: " + message);

  }



}
