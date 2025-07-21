import { useMiddle } from './MiddleContext'
import Profile from '../middleModules/profile'
import Dashboard from '../middleModules/dashboard'
import AlbumSongArtist from '../middleModules/albumOrSong'
export const Middle = () => {

    const { middleValue } = useMiddle()
    return (
        <div className="middle ">
            {middleValue === 0 ? (
                <Dashboard />
                    ) : 
             middleValue === 1 ? (
                <Profile />
            ) : (
                <AlbumSongArtist/>
            )
            }
        </div>
    );
}
export default Middle;