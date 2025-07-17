import homeIcon from 'D:/SpotifyApp/frontend/src/assets/home.svg';
export const TopBar = () => {
    return (
        <div className="topBar absolute top-0 w-full bg-black h-[75px] grid grid-cols-3 items-center">
            <div>
                <button className="rounded-full ml-[10px] mt-[10px] hover:bg-gray-700">
                    <img src={homeIcon} alt="Home"/>
                </button>
            </div>
            <div>  
                <input type="text" className="bg-gray-800 text-white rounded-full px-4 py-2 w-full focus:outline-none" placeholder="Search..."/>
            </div>
            <div>
                <button className="absolute rounded-full right-[10px] top-[10px] hover:bg-gray-700">
                    <img src={homeIcon} alt="User"/>
                </button>
            </div>
        </div>
    )
}