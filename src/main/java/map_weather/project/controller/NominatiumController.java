package map_weather.project.controller;

import map_weather.project.controller.dto.ResponseRoutes;
import org.springframework.http.MediaType;
import map_weather.project.service.NominatiumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rotas")
public class NominatiumController {

  private final NominatiumService nominatiumService;

  public NominatiumController(NominatiumService nominatiumService) {
    this.nominatiumService = nominatiumService;
  }

  @GetMapping("/geo")
  public Mono<String> geoCode(@RequestParam String endereco) {
    return nominatiumService.buscarCoordenadas(endereco);
  }

  @GetMapping("/travel")
  public Mono<String> buscarCity(@RequestParam String origem, @RequestParam String destino){
return nominatiumService.buscar2Coordenadas(origem, destino)
    .onErrorResume(e -> Mono.just("Erro ao buscar coordenadas: " + e.getMessage()));
  }

  @GetMapping(value = "/travels", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ResponseRoutes> buscarCityWithStop(@RequestParam String origem, @RequestParam String parada, @RequestParam String destino){
    return nominatiumService.buscar3Coordenadas(origem, parada, destino);
  }

}
