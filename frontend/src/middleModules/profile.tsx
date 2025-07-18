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

function displayProfile() {
    const myProfileJSON = sessionStorage.getItem('myProfile')
    let myProfile: Profile | null = null;
    if (myProfileJSON != null){
    myProfile = JSON.parse(myProfileJSON)
    }
    return(
        <div>
        {myProfile ? (
            <div className="grid grid-cols-2 items-center">
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