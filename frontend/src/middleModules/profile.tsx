import { useProfile } from "../components/ProfileContext"

function displayProfile() {
    const {profileContent} = useProfile()
    let myProfile: any
    if (profileContent != null){
        myProfile = profileContent
    }
    return(
        <div>
        {myProfile ? (
            <div className="border-[2px] grid grid-cols-2 flex items-center">
                <div >
                    <img src={`${myProfile.images[0].url}`} className={`rounded-full max-w-${myProfile.images[0].width} max-h-${myProfile.images[0].height}`}></img>
                </div>
                <div className="text-[100px] ml-[20px] grid-rows-3">
                    <div className="row-start-1">
                        {myProfile.display_name}
                    </div>
                    <div className="row-start-2 text-[50px]">
                        {myProfile.followers.total} followers
                    </div>
                    <div className="row-start-3 text-[35px]">
                        Country: {myProfile.country}, User: {myProfile.product}
                    </div>
                </div>
            </div>
        ) : (
            <h1>Nothing to see on profile</h1>
        )
        }
        </div>
    )
}

export default displayProfile;