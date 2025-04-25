import { useState } from "react";
import api from "./Fetch";

interface Weather {
  dt: number;
  temp: number;
}

export function Weather() {
  const [weather, setWeather] = useState<null|Weather>(null);
  const [cityName, setCityName] = useState("");
  const [error, setError] = useState("");

  const fetchData = async () => {
    try {
      const response = await api.get("previsao-tempo/previsao-cidade", {
        params: { cityName }
      });
      if (response.status === 200) {
        console.log("response here: " + response)
        setWeather(response.data);
        setError(""); 
      }
    } catch (e) {
      console.error(e);
      setError("Não foi possível encontrar a cidade. Tente novamente.");
    }
  };

  return (
    <>
      {error && <p>{error}</p>}

      <input
        type="text"
        placeholder="Digite a cidade"
        value={cityName}
        onChange={(e) => setCityName(e.target.value)}
      />

      <button onClick={fetchData}>Enviar</button>

      {weather && (
  <div>
    <h2>Previsão para {weather.dt}</h2>
    <p>Temperatura: {weather.temp}</p>
  </div>
)}

      <div id="map" style={{ height: "500px", width: "100%" }}></div>
    </>
  );
}
