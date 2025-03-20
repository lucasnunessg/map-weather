package map_weather.project.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RouteProducerService {

  private final KafkaTemplate<String, String> kafkaTemplate;
    private final WebClient webClient;

  public RouteProducerService(KafkaTemplate<String, String> kafkaTemplate, WebClient.Builder webClientBuilder) {
    this.kafkaTemplate = kafkaTemplate;
    this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
  }

  public void sendRoute(String message) {
    kafkaTemplate.send("Route-topic", message);
    System.out.println("Event send: " + message);
  }

  public Mono<String> foundRoute(String route) {

    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/search")
            .queryParam("q", route)
            .queryParam("format", "json")
            .queryParam("limit", 1)
            .build())
        .retrieve()
        .bodyToMono(String.class);

  }

  public String foundTwoRoutes(String route) {

  }



}
