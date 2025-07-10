import { useState } from 'react'
import './home.css'
import NowPlaying from './nowPlaying.tsx'
import TopBar from './topBar.tsx'
function Home() {

  return (
    <>
    <div>
      <TopBar />
    </div>
    <div>
      <NowPlaying />
    </div>
    </>
  )
}

export default Home
