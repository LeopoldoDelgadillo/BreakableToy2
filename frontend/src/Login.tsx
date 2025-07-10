import { useState } from 'react'
import encoraLogo from './assets/encoraLogo.svg'
import spotifyLogo from './assets/spotifyLogo.png'
import './Login.css'

function App() {
  const loginTry = (event: React.FormEvent<HTMLFormElement>) => {
    
  }

  return (
    <>
      <div>
        <a>
          <img src={spotifyLogo} className="logo spotify" alt="Spotify logo" />
        </a>
        <a>
          <img src={encoraLogo} className="logo encora" alt="Encora logo" />
        </a>
      </div>
      <h1>Login</h1>
      <div className="LoginBox">
        <form onSubmit={loginTry}>
          <div className="inputBox">
            <input type="text" name="username" required />
            <span>Username</span>
          </div>
          <div className="inputBox">
            <input type="password" name="password" required />
            <span>Password</span>
          </div>
          <button type="submit">Login</button>
        </form>
      </div>
    </>
  )
}

export default App
