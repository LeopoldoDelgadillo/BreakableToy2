
import { useEffect } from 'react'
import { Middle } from '../components/Middle'
import { NowPlaying } from '../components/NowPlaying'
import { useProfile } from '../components/ProfileContext'
import { TopBar } from '../components/TopBar'
import fetchProfile from '../fetches/fetchProfile'
import loginCheck from '../fetches/loginCheck'
import { refreshToken } from '../fetches/refreshToken'
import getTop10Artists from '../fetches/top10artists'
import { useTop10 } from '../components/Top10Context'


function Home() {
  const {setProfile} = useProfile()
  const {top10set} = useTop10()
    loginCheck()
    useEffect (() => {
    fetchProfile(setProfile)
    getTop10Artists(top10set)
    },[])
    
    refreshToken()
  return (
    <div className="flex flex-col h-screen overflow-hidden items-center bg-gray-900">
    <div className="h-[75px] flex flex-none w-screen z-10 bg-black">
      <TopBar />
    </div>
    <div className="flex-1 overflow-auto relative items-center justify-center  h-full w-screen z-0">
      <Middle />
    </div>
    <div className="h-[50px] flex flex-none z-10 bg-black w-screen items-center">
      <NowPlaying />
    </div>
    </div>
  )
}

export default Home;
