package map_weather.project.controller;

import map_weather.project.controller.dto.ResponseRoutes;
import map_weather.project.service.RouteProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import map_weather.project.service.NominatiumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rotas")
public class NominatiumController {

  private final NominatiumService nominatiumService;
  private final RouteProducerService routeProducerService;

  public NominatiumController(NominatiumService nominatiumService, RouteProducerService routeProducerService) {
    this.nominatiumService = nominatiumService;
    this.routeProducerService = routeProducerService;
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


  @PostMapping("/send")
  public ResponseEntity<String> sendRoute(@RequestParam String route) {
    if (route == null || route.isEmpty()) {
      return ResponseEntity.badRequest().body("Parâmetro 'route' é obrigatório.");
    }
    try {
      routeProducerService.sendRoute(route);
      return ResponseEntity.ok("Rota enviada!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erro ao enviar rota: " + e.getMessage());
    }
  }

  @PostMapping("/send-two/routes")
  public ResponseEntity<String> sendTwoRoutes(@RequestParam String origem, @RequestParam String destino) {
    if (origem.isEmpty() || destino.isEmpty()) {
      return ResponseEntity.badRequest().body("Erro: Origem e destino não podem estar vazios.");
    }

    try {
      routeProducerService.sendTwoRoutes(origem, destino);
      String rotasOk = String.format("Rotas enviadas com sucesso! Origem: %s | Destino: %s", origem, destino);
      return ResponseEntity.ok(rotasOk);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erro ao enviar rotas: " + e.getMessage());
    }
  }

  }


