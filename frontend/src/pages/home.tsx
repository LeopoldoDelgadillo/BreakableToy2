import loginCheck from '../components/loginCheck';
import { Middle } from '../components/Middle'
import { NowPlaying } from '../components/NowPlaying'
import { refreshToken } from '../components/refreshToken';
import { TopBar } from '../components/TopBar'
import fetchProfile from '../homeFetches/fetchProfile';
function Home() {
  loginCheck();
  fetchProfile();
  refreshToken();
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
