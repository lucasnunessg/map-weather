package map_weather.project.service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
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

  public Mono<String> buscar3Coordenadas(String origem, String parada, String destino) {
    Mono<String> monoOrigem = buscarCoordenadas(origem);
    Mono<String> monoParada = buscarCoordenadas(parada);
    Mono<String> monoDestino = buscarCoordenadas(destino);

    return Mono.zip(monoOrigem, monoParada, monoDestino)
        .map(tupla -> {
          String respostaOrigem = tupla.getT1();
          String respostaParada = tupla.getT2();
          String respostaDestino = tupla.getT3();
          return "Origem: " + respostaOrigem + "\nParada: " + respostaParada + "\nDestino: " + respostaDestino;
        });

  }


}
