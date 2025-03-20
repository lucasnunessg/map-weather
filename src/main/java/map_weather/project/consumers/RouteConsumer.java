package map_weather.project.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RouteConsumer {

  @KafkaListener(topics = {"routes-topic", "weather-topic", "traffic-topic"}, groupId = "my-group")
  public void consumeMultipleTopics(String message) {
    System.out.println("Mensagem recebida: " + message);
  }



}
