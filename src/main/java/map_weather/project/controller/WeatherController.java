package map_weather.project.controller;

import map_weather.project.controller.dto.CoordinatesDto;
import map_weather.project.service.WeatherMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/previsao-tempo")
public class WeatherController {

  private final WeatherMap weatherMap;

  public WeatherController(WeatherMap weatherMap) {
    this.weatherMap = weatherMap;
  }

  @GetMapping("/previsao-cidade")
  public Mono<String> previsaoCity(@RequestParam String cityName) throws RuntimeException {
    List<CoordinatesDto> coords = weatherMap.getLanAndLong(cityName);
    CoordinatesDto coord = coords.get(0);
    double lat = Double.parseDouble(coord.latitude());
    double lon = Double.parseDouble(coord.longitude());

    return weatherMap.getWeather(lat, lon);
  }

}
