package map_weather.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import map_weather.project.controller.dto.CoordinatesDto;
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
            .queryParam("q", cityName)  // Aqui so pd 1 query por vez por limitacao da api
            .queryParam("locale", "pt")
            .queryParam("key", GRAPHHOPPER_API_KEY)
            .build())
        .retrieve()
        .bodyToMono(String.class);
  }

  public Mono<String> getRouteByCities(String origem, String destino) {
    Mono<String> cityOrigem = getRouteByCityName(origem);
    Mono<String> cityDestino = getRouteByCityName(destino);

    return Mono.zip(cityOrigem, cityDestino, (origemRes, destinoRes) -> {
      System.out.println("Origem: " + origemRes);
      System.out.println("Destino: " + destinoRes);
      return "Rota gerada com sucesso!" + "Origem: " + origemRes + "Destino: " + destinoRes;

    });
  }

  public List<CoordinatesDto> getLatAndLon(String cidade) {
    Mono<String> responseMono = getRouteByCityName(cidade);

    return responseMono.map(response -> {
      List<CoordinatesDto> coordinatesList = new ArrayList<>();
      try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

        JsonNode hits = rootNode.path("hits");
        if (hits.isArray() && hits.size() > 0) {
          JsonNode firstHit = hits.get(0);
          JsonNode point = firstHit.path("point");

          if (!point.isMissingNode()) {
            String lat = String.valueOf(point.path("lat").asDouble());
            String lon = String.valueOf(point.path("lng").asDouble());
            coordinatesList.add(new CoordinatesDto(lat, lon));
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return coordinatesList;
    }).block();
  }

  public List<CoordinatesDto> getRoutesBetweenCities(String origem, String destino) {
    List<CoordinatesDto> getOrigem = getLatAndLon(origem);
    List<CoordinatesDto> getDestino = getLatAndLon(destino);

    if (getOrigem.isEmpty()) {
      throw new RuntimeException("Ponto de origem não encontrado");
    }
    if (getDestino.isEmpty()) {
      throw new RuntimeException("Ponto de destino não encontrado");
    }

    CoordinatesDto origemCoord = getOrigem.get(0);
    CoordinatesDto destinoCoord = getDestino.get(0);

    double lat1 = Double.parseDouble(origemCoord.latitude());
    double lon1 = Double.parseDouble(origemCoord.longitude());
    double lat2 = Double.parseDouble(destinoCoord.latitude());
    double lon2 = Double.parseDouble(destinoCoord.longitude());

    String rotaJson = getRouteBetweenPoints(lat1, lon1, lat2, lon2).block();

    List<CoordinatesDto> routeCoordinates = new ArrayList<>();
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode root = objectMapper.readTree(rotaJson);
      JsonNode points = root.path("paths").get(0).path("points").path("coordinates");

      for (JsonNode coord : points) {
        double longitude = coord.get(0).asDouble();
        double latitude = coord.get(1).asDouble();
        routeCoordinates.add(new CoordinatesDto(String.valueOf(latitude), String.valueOf(longitude)));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return routeCoordinates;
  }



}