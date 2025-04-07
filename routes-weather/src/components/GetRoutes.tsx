import { useState } from 'react';
import api from './Fetch';

export default function GetRoutes() {
  const [origem, setOrigem] = useState('');
  const [destino, setDestino] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");

  const handleSearch = async () => {
    try {
      const response = await api.get('/rotas/two-routes', {
        params: {
          cityNameOrigem: origem,
          cityNameDestino: destino,
        },
      });
      if(response.status === 200) {

        setResult(response.data);
        setError("")
      }
    } catch (error) {
      console.error('Erro ao buscar rotas:', error);
      setError("Não foi possível encontrar")
    }
  };

  if(handleSearch === null) {
    setError("Você precisa preencher os campos")
  }

  return (
    <div className="p-4">
      <input
        type="text"
        placeholder="Cidade de origem"
        value={origem}
        onChange={(e) => setOrigem(e.target.value)}
        className="border p-2 rounded mr-2"
      />
      <input
        type="text"
        placeholder="Cidade de destino"
        value={destino}
        onChange={(e) => setDestino(e.target.value)}
        className="border p-2 rounded mr-2"
      />
      <button onClick={handleSearch} className="bg-blue-500 text-black px-4 py-2 rounded">
        Buscar
      </button>

      {result && (
        <div className="mt-4">
          <pre>{JSON.stringify(result, null, 2)}</pre>
        </div>
      )}

      {error && <p className='text-[red]'>{error}</p>}
    </div>
  );
}
