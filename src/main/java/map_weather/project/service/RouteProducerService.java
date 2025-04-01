package map_weather.project.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class RouteProducerService {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final WebClient graphhopperWebClient;

  private static final String GRAPHHOPPER_API_KEY = "a04596a8-04b9-4ca9-91a9-d4e4df34e48d";
  private static final String KAFKA_TOPIC = "routes-topic";

  public RouteProducerService(KafkaTemplate<String, String> kafkaTemplate,
      WebClient.Builder webClientBuilder) {
    this.kafkaTemplate = kafkaTemplate;
    this.graphhopperWebClient = webClientBuilder
        .baseUrl("https://graphhopper.com/api/1")
        .build();
  }

  public void sendRoute(String message) {
    kafkaTemplate.send(KAFKA_TOPIC, message);
    System.out.println("Event send: " + message);
  }

  public Mono<String> getRouteBetweenPoints(double lat1, double lon1, double lat2, double lon2) {
    return graphhopperWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/route")
            .queryParam("point", lat1 + "," + lon1)
            .queryParam("point", lat2 + "," + lon2)
            .queryParam("profile", "car")
            .queryParam("key", GRAPHHOPPER_API_KEY)
            .queryParam("points_encoded", "false")
            .build())
        .retrieve()
        .bodyToMono(String.class)
        .doOnSuccess(response -> System.out.println("Rota obtida com sucesso"))
        .doOnError(e -> System.err.println("Erro ao calcular rota: " + e.getMessage()))
        .onErrorResume(e -> Mono.just("{\"error\":\"Failed to calculate route\"}"));
  }

  public Mono<String> getRouteByCityName(String cityName) {
    return graphhopperWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/geocode")
            .queryParam("q", cityName)  // Aqui você só precisa de uma chave "q" por vez, então pode passar a origem
            .queryParam("locale", "de")  // Defina o local, como no seu exemplo da URL
            .queryParam("key", GRAPHHOPPER_API_KEY)  // Adicione a chave da API
            .build())
        .retrieve()
        .bodyToMono(String.class);  // Recebe o corpo da resposta como String
  }

  public Mono<String> getRouteByCities(String origem, String destino) {
    Mono<String> cityOrigem = getRouteByCityName(origem);
    Mono<String> cityDestino = getRouteByCityName(destino);

    return Mono.zip(cityOrigem, cityDestino, (origemRes, destinoRes) -> {
      System.out.println("Origem: " + origemRes);
      System.out.println("Destino: " + destinoRes);;
      return "Rota gerada com sucesso!" + "Origem: " + origemRes + "Destino: " + destinoRes;

    });
  }





}