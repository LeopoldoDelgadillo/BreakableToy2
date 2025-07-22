
import { createRoot } from 'react-dom/client'
import './index.css'
import Home from './pages/home.tsx'
import Login from './pages/login.tsx'
import Redirect from './pages/redirect.tsx'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { MiddleProvider } from './components/MiddleContext.tsx'
import { SongSearchProvider } from './components/SongSearchContext.tsx'
import { ProfileProvider } from './components/ProfileContext.tsx'
import { Top10Provider } from './components/Top10Context.tsx'
import { IdentifierProvider } from './components/IdentifierContext.tsx'
  
createRoot(document.getElementById('root')!).render(
  <IdentifierProvider>
    <Top10Provider>
      <ProfileProvider>
        <SongSearchProvider>
          <MiddleProvider>
            <BrowserRouter>
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/redirect" element={<Redirect />} />
              </Routes>
            </BrowserRouter>
          </MiddleProvider>
        </SongSearchProvider>
      </ProfileProvider>
    </Top10Provider>
  </IdentifierProvider>
)
