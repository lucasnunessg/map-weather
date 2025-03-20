package map_weather.project.service;

import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

@Service
public class RouteProducerService {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public RouteProducerService(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendRoute(String message) {
    kafkaTemplate.send("Route-topic", message);
    System.out.println("Event send: " + message);
  }

  public void sendTwoRoutes(String origem, String destino) {
    kafkaTemplate.send("map_wheater_route_origem", origem );
    kafkaTemplate.send("map_weather_route_destino", destino);
    System.out.println("Event sent to route: " + origem + destino);
  }

}
