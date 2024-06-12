import './App.css'
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from './components/Home';
import ImageDispaly from './components/ImageDispaly';

function App() {
  

  
  return (
    <BrowserRouter>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/file/:id" element={<ImageDispaly />} /> # TODO ADD PDF SUPPORT
    </Routes>
  </BrowserRouter>
  )
}

export default App
