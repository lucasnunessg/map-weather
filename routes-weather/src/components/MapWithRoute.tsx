import { useEffect, useRef, useState } from "react";
import L from "leaflet";
import api from "./Fetch";
import 'leaflet/dist/leaflet.css';

export default function MapWithRoute() {
  const [origem, setOrigem] = useState("");
  const [destino, setDestino] = useState("");
  const [error, setError] = useState("");
  const [coords, setCoords] = useState<L.LatLngExpression[] | null>(null); 

  const mapRef = useRef<L.Map | null>(null);
  const routeLayerRef = useRef<L.Polyline | null>(null);

  type RouteData = {
    latitude: string;
    longitude: string;

    
  };
  

 useEffect(() => {
  if(!mapRef.current) {
   mapRef.current = L.map('map').setView([51.505, -0.09], 13);
    L.tileLayer('https://title.openstreetmap.org/{z}/{x}/{y}.png',{
      maxZoom: 19,
      attribution:
      '&copy; <a href="http://www.openstreetmap.org/copyright">OenStretMap</a>'
    }).addTo(mapRef.current)
  }
 }, [])



  useEffect(() => {
    if (coords && mapRef.current) {
      if (routeLayerRef.current) {
        mapRef.current.removeLayer(routeLayerRef.current); 
      }

      routeLayerRef.current = L.polyline(coords, { color: "blue" }).addTo(mapRef.current);
      mapRef.current.fitBounds(routeLayerRef.current.getBounds()); 
    }
  }, [coords]);

  const fetchData = async () => {
    if (!origem || !destino) {
      setError("Por favor, preencha ambos os campos.");
      return;
    }

    try {
      const response = await api.get(`/rotas/two-routes`, {
        params: {
          cityNameOrigem: origem,
          cityNameDestino: destino,
        },
      });

      if (response.status === 200) {
        const transformedCoordinates = response.data.map((item: RouteData) => ({
          lat: parseFloat(item.latitude),
          lng: parseFloat(item.longitude),
        }));

        setCoords(transformedCoordinates); 
        console.log(response);
        
        setError(""); 
      }
    } catch (e) {
      console.error(e);
      setError("Não foi possível achar a rota.");
    }
  };

  return (
    <div>
      {error && <p>{error}</p>}
      <input
        type="text"
        placeholder="De onde você vai sair"
        value={origem}
        onChange={(e) => setOrigem(e.target.value)}
      />
      <input
        type="text"
        placeholder="Destino"
        value={destino}
        onChange={(e) => setDestino(e.target.value)}
      />
      <button onClick={fetchData}>Enviar</button>

      <div id="map" style={{ height: "500px", width: "100%" }}></div>
    </div>
  );
}
