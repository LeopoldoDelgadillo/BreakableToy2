import previousSongIcon from 'D:/SpotifyApp/frontend/src/assets/previousSongIcon.svg';
import playIcon from 'D:/SpotifyApp/frontend/src/assets/playIcon.svg';
import pauseIcon from 'D:/SpotifyApp/frontend/src/assets/pauseIcon.svg';
import nextSongIcon from 'D:/SpotifyApp/frontend/src/assets/nextSongIcon.svg';
import shuffleIcon from 'D:/SpotifyApp/frontend/src/assets/shuffleIcon.svg';
import shufflingIcon from 'D:/SpotifyApp/frontend/src/assets/shufflingIcon.svg';
import repeatIcon from 'D:/SpotifyApp/frontend/src/assets/repeatIcon.svg';
import repeatingIcon from 'D:/SpotifyApp/frontend/src/assets/repeatingIcon.svg';
import volumeLowIcon from 'D:/SpotifyApp/frontend/src/assets/volumeLowIcon.svg';
import lyricsIcon from 'D:/SpotifyApp/frontend/src/assets/lyricsIcon.svg';
import volumeMedIcon from 'D:/SpotifyApp/frontend/src/assets/volumeMedIcon.svg';
import volumeHighIcon from 'D:/SpotifyApp/frontend/src/assets/volumeHighIcon.svg';
import volumeMutedIcon from 'D:/SpotifyApp/frontend/src/assets/volumeMutedIcon.svg';
import likedIcon from 'D:/SpotifyApp/frontend/src/assets/likedIcon.svg';
import likeIcon from 'D:/SpotifyApp/frontend/src/assets/likeIcon.svg';
import followArtistIcon from 'D:/SpotifyApp/frontend/src/assets/followArtistIcon.svg';
import followingArtistIcon from 'D:/SpotifyApp/frontend/src/assets/followingArtistIcon.svg';
import { useState } from 'react';
//import followIcon from 'D:/SpotifyApp/frontend/src/assets/followIcon.svg';
export const NowPlaying = () => {

  const [isPlaying, setIsPlaying] = useState(false);
  const playPause = () => {
    setIsPlaying(!isPlaying);
  }

  const [isShuffling, setIsShuffling] = useState(false);
  const shuffleSongs = () => {
    setIsShuffling(!isShuffling);
  }

  const [isRepeating, setIsRepeating] = useState(false);
  const repeatSongs = () => {
    setIsRepeating(!isRepeating);
  }

  const [isLiked, setIsLiked] = useState(false);
  const likeLiked = () => {
    setIsLiked(!isLiked);
  }

  const [isFollowed, setIsFollowed] = useState(false);
  const followUnfollow = () => {
    setIsFollowed(!isFollowed);
  }

  return (
    <div className="nowPlaying flex w-full grid grid-cols-5 items-center">
      <div className="playDiv grid grid-cols-5 flex items-center justify-center">
        <div className="previous flex items-center justify-center">
          <button><img src={previousSongIcon} alt="Previous" className="h-[35px] w-[35px]"/></button>
        </div> 
        <div className="playPause flex items-center justify-center"> 
          <button onClick={() => playPause()}>
            {!isPlaying ? (<img src={playIcon} alt="Play" className="h-[35px] w-[35px]"/>) 
                      : (<img src={pauseIcon} alt="Pause" className="h-[35px] w-[35px]"/>)}</button>
        </div> 
        <div className="next flex items-center justify-center"> 
          <button><img src={nextSongIcon} alt="Next" className="h-[35px] w-[35px]"/></button>
        </div>
      </div>
      <div className="songDiv grid grid-cols-10 col-span-2 flex items-center justify-center">
        <div className="suffle flex items-center justify-center">
          <button onClick={() => shuffleSongs()}>
            {!isShuffling ? (<img src={shuffleIcon} alt="Shuffle" className="h-[35px] w-[35px]"/>) 
                      : (<img src={shufflingIcon} alt="Shuffling" className="h-[35px] w-[35px]"/>)}</button>
        </div> 
        <div className="repeat flex items-center justify-center">         
          <button onClick={() => repeatSongs()}>
            {!isRepeating ? (<img src={repeatIcon} alt="Repeat" className="h-[35px] w-[35px]"/>) 
                      : (<img src={repeatingIcon} alt="Repeating" className="h-[35px] w-[35px]"/>)}</button>
        </div>
        <div className="songBar grid grid-cols-8 col-start-3 col-span-7 flex items-center">
          <div className="actualTime flex items-center justify-center">
              0:00
          </div>
          <div className="songProgress col-start-2 col-span-6 flex items-center justify-center">
            <input type="range" min="0" max="100" defaultValue="0" className="w-full h-2 bg-gray-700 rounded-lg cursor-pointer range range-primary"/>
          </div>   
          <div className="totalTime col-start-8 flex items-center justify-center">
              3:00
          </div>
        </div>
        <div className="volume col-start-10 relative group flex items-center justify-center">
          <div className="bg-black h-[110px] w-[35px] opacity-25 mb-2 absolute bottom-[30px] rounded-md invisible group-hover:visible transistion:opacity group-hover:opacity-100">
            <input type="range" min="0" max="100" defaultValue={100} className="transform rotate-[270deg] absolute bottom-[40px] left-[-32px] w-[100px] h-2 bg-gray-700 rounded-lg cursor-pointer"/>
          </div>
            <button><img src={volumeHighIcon} alt="VolumeHigh" className="h-[35px] w-[35px]"/></button>
        </div>
      </div>
      <div className="artist grid grid-cols-10 col-start-4 col-span-2 flex items-center justify-center">
        <div className="lyrics col-start-3 flex items-center justify-center">
          <button><img src={lyricsIcon} alt="Lyrics" className="h-[35px] w-[35px]"/></button>
        </div>
        <div className="artistImg">
          
        </div>
        <div className="artist/SongName">

        </div>
        <div className="songLiked col-start-9 flex items-center justify-center">
          <button onClick={() => likeLiked()}>
            {!isLiked ? (<img src={likeIcon} alt="Like" className="h-[35px] w-[35px]"/>) 
                      : (<img src={likedIcon} alt="Liked" className="h-[35px] w-[35px]"/>)}</button>
        </div>
        <div className="followArtist col-start-10 flex items-center justify-center">
          <button onClick={() => followUnfollow()}>
            {!isFollowed ? (<img src={followArtistIcon} alt="Follow" className="h-[35px] w-[35px]"/>) 
                      : (<img src={followingArtistIcon} alt="Followed" className="h-[35px] w-[35px]"/>)}</button>
        </div>
      </div>
    </div>
  )
}