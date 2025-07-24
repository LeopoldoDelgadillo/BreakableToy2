import { useProfile } from "../components/ProfileContext"

function displayProfile() {
    const {profileContent} = useProfile()
    let myProfile: any
    if (profileContent != null){
        myProfile = profileContent
    }
    return(
        <div className="flex flex-col justify-center items-center">
        {myProfile ? (
            <div className="grid grid-cols-3 mt-[20px]">
                <div className="flex items-center justify-center">
                    <img src={`${myProfile.images[0].url}`} className={`rounded-full max-w-${myProfile.images[0].width} max-h-${myProfile.images[0].height}`}></img>
                </div>
                <div className="ml-[20px] flex justify-center flex-col col-span-2">
                    <div className="text-[50px]">
                        {myProfile.display_name}
                    </div>
                    <div className="text-[30px]">
                        Country: {myProfile.country} · {myProfile.followers.total} followers · {myProfile.product} user
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