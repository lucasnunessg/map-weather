package map_weather.project.service;

import java.time.Duration;
import map_weather.project.controller.dto.ResponseRoutes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NominatiumService {

  private final WebClient webClient;
  private final RouteProducerService routeProducerService;

  public NominatiumService(WebClient.Builder webClientBuilder, RouteProducerService routeProducerService) {
    this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
    this.routeProducerService = routeProducerService;
  }

 public Mono<String> buscarCoordenadas(String endereco) {


    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/search")
            .queryParam("q", endereco)
            .queryParam("format", "json")
            .queryParam("limit", 1)
            .build())
        .retrieve()
        .bodyToMono(String.class);

  }

  public Mono<String> buscar2Coordenadas(String origem, String destino) {
    Mono<String> monoOrigem = buscarCoordenadas(origem);
    Mono<String> monoDestino = buscarCoordenadas(destino);

    return Mono.zip(monoOrigem, monoDestino, (respostaOrigem, respostaDestino) -> {
      return "Origem: " + respostaOrigem + "\nDestino: " + respostaDestino;
    });
  }

  public Flux<ResponseRoutes> buscar3Coordenadas(String origem, String parada, String destino) {
    Mono<String> monoOrigem = buscarCoordenadas(origem);
    Mono<String> monoParada = buscarCoordenadas(parada);
    Mono<String> monoDestino = buscarCoordenadas(destino);

    Mono<ResponseRoutes> primeraParada = Mono.zip(monoOrigem, monoParada)
        .map(tuple -> new ResponseRoutes(tuple.getT1(), tuple.getT2(), null));

    Mono<ResponseRoutes> segundaParada = monoDestino.delayElement(Duration.ofSeconds(5))
        .map(respostaDestino -> new ResponseRoutes(null, null, respostaDestino));
    return Flux.concat(primeraParada, segundaParada);

  }


}