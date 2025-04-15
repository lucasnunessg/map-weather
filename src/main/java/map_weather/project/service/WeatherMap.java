package map_weather.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherMap {

  private final WebClient webClient;
  private static final String APY_KEY = "eb4ed333617099af55e7ae7161d801ce";

  public WeatherMap();

}
