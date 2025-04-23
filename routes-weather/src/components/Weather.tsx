import { useState } from "react";
import api from "./Fetch";

export async function Weather(){



const [weather, setWeather] = useState(null);
const [city, setCity] = useState("");


  const fetchData = async () => {
    try {
      
      const response = await api.get("previsao-tempo/previsao-cidade", {
        city
      })

    }catch(e){
      console.error(e)
    }
  }

  return(
    <></>
  )
}