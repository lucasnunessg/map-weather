package map_weather.project.service;

import map_weather.project.controller.dto.ResponseRoutes;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RouteProducerService {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final WebClient webClient;

  public RouteProducerService(KafkaTemplate<String, String> kafkaTemplate,
      WebClient.Builder webClientBuilder) {
    this.kafkaTemplate = kafkaTemplate;
    this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
  }

  public void sendRoute(String message) {
    kafkaTemplate.send("routes-topic", message);
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

  public Mono<String> foundTwoRoutes(String origem, String destino) {
    Mono<String> foundOrigem = foundRoute(origem);
    Mono<String> foundDestino = foundRoute(destino);

    return Mono.zip(foundOrigem, foundDestino)
        .map(tuple -> "Origem: " + tuple.getT1() + " | Destino: " + tuple.getT2())
        .flatMap(combinedMessage -> {
          System.out.println("Enviando mensagem ao Kafka: " + combinedMessage);
          return Mono.fromFuture(
              kafkaTemplate.send("routes-topic", combinedMessage).toCompletableFuture());
        })
        .thenReturn("Mensagem enviada ao Kafka");
  }

  public Mono<String> foundThreeRoutes(String origem, String parada, String destino) {
    Mono<String> foundOrigem = foundRoute(origem);
    Mono<String> foundParada = foundRoute(parada);
    Mono<String> foundDestino = foundRoute(destino);

    return Mono.zip(foundOrigem, foundParada, foundDestino)
        .map(tuple -> "Origem: " + tuple.getT1() + "| Parada: " + tuple.getT2() + "| Destino: "
            + tuple.getT3())
        .flatMap(combinedMessage -> {
          System.out.println("enviando as rotas ao kafka" + combinedMessage);
          return Mono.fromFuture(
              kafkaTemplate.send("routes-topic", combinedMessage).toCompletableFuture());
        })
        .thenReturn("Mensagem enviada");
  }

  public Mono<String> foundThreeRoutesWithoutMap(String origem, String parada, String destino) {
    Mono<String> foundOrigem = foundRoute(origem);
    Mono<String> foundParada = foundRoute(parada);
    Mono<String> foundDestino = foundRoute(destino);

    return Mono.zip(foundOrigem, foundParada, foundDestino)
        .flatMap(tuple -> {
          String combinedMessage = "Origem: " + tuple.getT1() + "Parada: " + tuple.getT2() +  "  | Destino: " + tuple.getT3();
          kafkaTemplate.send("routes-topic", combinedMessage);
          return Mono.just("Rota enviada: " + combinedMessage);
        });


  }

}
