export const getAlbumTracks = (albumID: string, profile: JSON, trackCount: number) => {
    let myProfile: any
    if (profile != null){
    myProfile = profile
    }
    return fetch(`http://127.0.0.1:9090/albums/${albumID}/tracks?country=${myProfile.country}&trackCount=${trackCount}`, {
        method: 'GET',
        credentials: 'include',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        },
    })
    .then(response => response.json())
    }
export default getAlbumTracks