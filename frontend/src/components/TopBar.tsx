
import { useMiddle } from './MiddleContext';
import homeIcon from 'D:/SpotifyApp/frontend/src/assets/home.svg';

interface Profile {
  country: number;
  display_name: string;
  email: string;
  explicit_content: {filter_enabled: boolean, filter_locked: boolean};
  external_urls: {spotify: string};
  followers: {href: null, total: number};
  href: string;
  id: string;
  images: [{height: number, url: string, width: number},{height: number, url: string, width: number}];
  product: string;
  type: string;
  uri: string;
}

export const TopBar = () => {
    let myProfile: Profile | null = null;
    let profileImage: string | undefined
    const myProfileJSON = sessionStorage.getItem('myProfile')
    if (myProfileJSON != null){
    myProfile = JSON.parse(myProfileJSON)
    }
    profileImage = myProfile?.images[1].url
    const { middleValue, changeValue } = useMiddle();
        const mainPage = () => {
            changeValue(0)
            console.log("Main page clicked. middleValue = ", middleValue);
        }
        const seeProfile = () => {
            changeValue(1);
            console.log("Profile clicked. middleValue = ", middleValue);
        }
    return (
        <div className="topBar absolute top-0 w-full bg-black h-[75px] grid grid-cols-3 items-center">
            <div>
                <button onClick={() => mainPage()} className="rounded-full ml-[10px] mt-[10px] hover:bg-gray-700">
                    <img src={homeIcon} alt="Home"/>
                </button>
            </div>
            <div>  
                <input type="text" className="bg-gray-800 text-white rounded-full px-4 py-2 w-full focus:outline-none" placeholder="Search..."/>
            </div>
            <div>
                <button onClick={() => seeProfile()} className="absolute rounded-full right-[10px] top-[5px] hover:bg-gray-700">
                    <img src={profileImage} className="rounded-full" alt="UserProfile"/>
                </button>
            </div>
        </div>
    )
}