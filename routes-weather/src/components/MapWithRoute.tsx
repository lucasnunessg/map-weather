import { useState } from "react";
import api from "./Fetch";

export default function MapWithRoute(){


  const [origem, setOrigem] = useState("");
  const [destino, setDestino] = useState("");
  const [error, setError] = useState("");
  const [coords, setCoords] = useState(null);
  

 
    const fetchData = async () => {
      try {
       const response = await api.get(`/rotas/two-routes`,
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

    }

return(
  <div>
    {error && <p>{error}</p>}
    <input
    type="text"
    placeholder="de onde você vai sair"
    value={origem}
    onChange={(e) => setOrigem(e.target.value)}>

    </input>

    <input
    type="text"
    placeholder="destino"
    value={destino}
    onChange={(e) => setDestino(e.target.value)}>

    </input>

    <button onClick={fetchData}>Enviar</button>

    {coords && (
      <p>{JSON.stringify(coords, null, 2)}</p>
        
    )}
  </div>
)
  
}