package map_weather.project.controller.dto;

public record ResponseRoutes(String origem, String parada, String destino) {
  @Override
  public String toString() {
    return "Origem: " + origem + "\nParada: " + parada + "\nDestino: " + destino;
   }

}
