
import { useEffect, useState } from 'react';
import fetchSearch from '../fetches/search';
import { useMiddle } from './MiddleContext';
import homeIcon from 'D:/SpotifyApp/frontend/src/assets/home.svg';
import { useSongSearch } from './songSearchContext';
import { useProfile } from './ProfileContext';

export const TopBar = () => {
    const {profileContent} = useProfile()
    const [userImage, setUserImage] = useState("")
    let myProfile: any
    useEffect (() => {
        if (profileContent != null){
            console.log("profileContent raw value:", profileContent);
            myProfile = profileContent
            setUserImage(myProfile?.images?.[1]?.url)
        }
    },[profileContent])

    const { middleValue, changeValue } = useMiddle();
    const mainPage = () => {
        changeValue(0)
        console.log("Main page clicked. middleValue = ", middleValue);
    }
    const seeProfile = () => {
        changeValue(1);
        console.log("Profile clicked. middleValue = ", middleValue);
    }
    const {changeSearch} = useSongSearch()
    const [debounceSearch,setDebounceSearch] = useState("")
    useEffect(() => {
        const search = setTimeout (() => {
            if(middleValue != 0) {
                changeValue(0)
            }
            fetchSearch(debounceSearch,changeSearch,profileContent)
            const searchDataString = debounceSearch
            if (searchDataString != null && searchDataString != ""){
                console.log("TopBar search",searchDataString)
            }
        },1000)
        return () => clearTimeout(search)
    },[debounceSearch]) 
    return (
        <div className="topBar w-full grid grid-cols-3 items-center">
            <div>
                <button onClick={() => mainPage()} className="rounded-full ml-[10px] mt-[10px] hover:bg-gray-700">
                    <img src={homeIcon} alt="Home"/>
                </button>
            </div>
            <div>  
                <input type="text" onChange={(e) => setDebounceSearch(e.target.value)} className="bg-gray-800 text-white rounded-full px-4 py-2 w-full focus:outline-none" placeholder="Search..."/>
            </div>
            <div>
                <button onClick={() => seeProfile()} className="absolute rounded-full right-[10px] top-[5px] hover:bg-gray-700">
                    {userImage!=undefined && userImage!="" ? (
                        <img src={userImage} className="rounded-full" alt="UserProfile"/>
                    ) : (<h1>profile</h1>)}
                </button>
            </div>
        </div>
    )
}