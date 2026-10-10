import React, { useState } from 'react';

function App() {
  const [prompt, setPrompt] = useState('');
  const [response, setResponse] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const safePrompt = encodeURIComponent(prompt);
      const res = await fetch(`http://localhost:8080/api/ollama/${safePrompt}`, {
        method: 'GET',
      });
      const textData = await res.text();
      setResponse(textData);
    } catch (error) {
      setResponse('Error connecting to localhost Ollama API.');
    } finally {
      setLoading(false);
    }
  };

  // Clear function to reset everything
  const handleClear = () => {
    setPrompt('');
    setResponse('');
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'sans-serif', maxWidth: '500px' }}>
      <h2>React API Prompt Client</h2>
      <form onSubmit={handleSubmit}>
        <textarea
          style={{ width: '100%', height: '80px', marginBottom: '10px' }}
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
          placeholder="Enter your prompt here..."
          required
        />
        <div>
          <button type="submit" disabled={loading}>
            {loading ? 'Sending...' : 'Submit Prompt'}
          </button>
          
          {/* New Clear Button */}
          <button 
            type="button" 
            onClick={handleClear} 
            style={{ marginLeft: '10px', background: '#ccc', border: '1px solid #999', cursor: 'pointer' }}
          >
            Clear Window
          </button>
        </div>
      </form>

      <div style={{ marginTop: '20px', padding: '15px', background: '#f0f0f0', borderRadius: '4px' }}>
        <h3>Response Window:</h3>
        <p style={{ whiteSpace: 'pre-wrap' }}>{response || 'No response yet.'}</p>
      </div>
    </div>
  );
}

export default App;
