package map_weather.project.service;

import map_weather.project.controller.dto.CoordinatesDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class WeatherMap {

  private final WebClient webClient;
  private final RouteProducerService routeProducerService;
  private static final String API_KEY = "eb4ed333617099af55e7ae7161d801ce";

  public WeatherMap(WebClient.Builder webClientBuilder, RouteProducerService routeProducerService) {
    this.webClient = webClientBuilder
        .baseUrl("https://api.openweathermap.org/data/3.0/onecall")
        .build();
    this.routeProducerService = routeProducerService;
  }

  public Mono<String> getWeather(double lat, double lon) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .queryParam("lat", lat)
            .queryParam("lon", lon)
            .queryParam("appid", API_KEY)
            .build())
        .retrieve()
        .bodyToMono(String.class);
  }

  public List<CoordinatesDto> getLanAndLong(String cidade) {
    return routeProducerService.getLatAndLon(cidade);
  }


}
