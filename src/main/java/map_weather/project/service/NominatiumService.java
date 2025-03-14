package map_weather.project.service;

import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NominatiumService {

  private final WebClient webClient;

  public NominatiumService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
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

  public Flux<String> buscar3Coordenadas(String origem, String parada, String destino) {
    Mono<String> monoOrigem = buscarCoordenadas(origem);
    Mono<String> monoParada = buscarCoordenadas(parada);
    Mono<String> monoDestino = buscarCoordenadas(destino);

    Mono<String> primeraParada = Mono.zip(monoOrigem, monoParada)
        .map(tuple -> {
          String respostaOrigem = tuple.getT1();
          String respostaParada = tuple.getT2();
          return "data: Origem: " + respostaOrigem + "\nParada: " + respostaParada + "\n\n"; //pra acompanhhar o fluxo streaming
        });

    Mono<String> segundaParada = monoDestino.delayElement(Duration.ofSeconds(5))
        .map(respostaDestino -> "data: Destino: " + respostaDestino + "\n\n");
    return Flux.interval(Duration.ZERO, Duration.ofSeconds(5)) // Emite a cada 5 segundos
        .zipWith(Flux.concat(primeraParada, segundaParada)) //combina as 2 respostas + o intervalo
        .map(tuple -> tuple.getT2()); // traz somente ate a parada
  }


}
