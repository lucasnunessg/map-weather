import { useState } from "react";
import api from "./Fetch";

interface Weather {
  dt: number;
  temp: number;
}

interface WeatherDaily {
  dt: number;
  temp: {
    day: number;
  };
}


export function Weather() {
  const [weather, setWeather] = useState<null|Weather>(null);
  const [dailyy, setDaily] = useState<WeatherDaily []>([]);
  const [cityName, setCityName] = useState("");
  const [error, setError] = useState("");

  const kelvinToCelsius = (kelvin: number) => {
    return +(kelvin - 273.15).toFixed(1);
  }

  const formatTimestamp = (timestamp: number): string => {
    const date = new Date(timestamp * 1000); 
    return date.toLocaleString("pt-BR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    });
  };
  

  const fetchData = async () => {
    try {
      const response = await api.get("previsao-tempo/previsao-cidade", {
        params: { cityName }
      });
      if (response.status === 200) {
        console.log("response here: " + JSON.stringify(response.data, null, 3));
        setDaily(response.data.daily)
        setWeather({
          dt: response.data.current.dt,
          temp: response.data.current.temp
        });
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
<h2>Horário e data: {formatTimestamp(weather.dt)}</h2>
<p>Temperatura: {kelvinToCelsius(weather.temp)} º C</p>

<h2>Previsão por dia: </h2>
{dailyy.map((dayy, index) => (
  <p key={index}>
    {`Data: ${formatTimestamp(dayy.dt)} Temperatura: ${kelvinToCelsius(dayy.temp.day)} ºC`}
  </p>
))}


  </div>
)}

      <div id="map" style={{ height: "500px", width: "100%" }}></div>
    </>
  );
}
