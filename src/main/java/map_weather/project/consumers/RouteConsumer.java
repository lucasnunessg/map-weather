package map_weather.project.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RouteConsumer {

  @KafkaListener(topics = "routes-topic", groupId = "my-group")
  public void consumeRoute(String message) {
    System.out.println("Recebendo a rota: " + message);
  }

}
