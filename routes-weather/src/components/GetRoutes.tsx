import { useEffect, useState } from "react";

export function GetRoutes(){
  

  const [message, setMessages] = useState<string[]>([]);

  useEffect(() => {
    const socket = new WebSocket("ws://localhost:8080/ws/routes");

    socket.onmessage = (e) => {
      setMessages((prev) => [...prev, e.data]);
    };

    socket.onerror = (error) => console.error("Erro no socket", error);

    return () => socket.close();
  }, [])
  

  return(
    <div>
      <h2> Rota: </h2>
      <ul>
        {message.map((msg, index) => (
          <li key={index}>{msg}</li>
        ))}
      </ul>

    </div>
  )
}