import { useEffect, useState } from "react";
import api from "./Fetch";

export default function MapWithRoute(){


  const [origem, setOrigem] = useState("");
  const [destino, setDestino] = useState("");
  const [error, setError] = useState("");
  const [coords, setCoords] = useState<Coord[]>([]);
  
  type Coord = {
    latitude: string;
    longitude: string;
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
       const response = await api.get<Coord[]>(`/rotas/two-routes`,
          {
            params: {
              cityNameOrigem: origem,
              cityNameDestino: destino,
            }
          }
        )
        if(response.status === 200) {
          setCoords(response.data)
        }
      }catch(e) {
        console.error(e)
        setError("Não foi possível achar a rota.")
      }

      fetchData();
    }
  }, [origem, destino])

return(
  <div>
    {error && <p>{error}</p>}
  </div>
)
  
}