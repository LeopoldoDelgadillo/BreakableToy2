
import { useEffect, useRef } from 'react'
import { Middle } from '../components/Middle'
import { NowPlaying } from '../components/NowPlaying'
import { TopBar } from '../components/TopBar'
import fetchProfile from '../fetches/fetchProfile'
import loginCheck from '../fetches/loginCheck'
import { refreshToken } from '../fetches/refreshToken'


function Home() {
    loginCheck()
    fetchProfile()
    refreshToken()
  return (
    <>
    <div>
      <TopBar />
    </div>
    <div>
      <Middle />
    </div>
    <div>
      <NowPlaying />
    </div>
    </>
  )
}

export default Home;
