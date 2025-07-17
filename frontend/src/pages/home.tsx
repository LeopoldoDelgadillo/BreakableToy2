import { Middle } from '../components/Middle'
import { useMiddleConfig } from '../components/MiddleValue';
import { NowPlaying } from '../components/NowPlaying'
import { TopBar } from '../components/TopBar'
function Home() {

  const { middleValue } = useMiddleConfig();
  
  return (
    <>
    <div>
      <TopBar />
    </div>
    <div key={middleValue}>
      <Middle />
    </div>
    <div>
      <NowPlaying />
    </div>
    </>
  )
}

export default Home;
