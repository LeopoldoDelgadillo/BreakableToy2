import { Middle } from '../components/Middle'
import { NowPlaying } from '../components/NowPlaying'
import { TopBar } from '../components/TopBar'
function Home() {
  
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
