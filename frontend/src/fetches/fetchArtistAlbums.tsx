export const getArtistAlbum = (artistID: string, profile: JSON) => {
    let myProfile: any
    if (profile != null){
    myProfile = profile
    }
    return fetch(`http://127.0.0.1:9090/artists/${artistID}/albums?country=${myProfile.country}`, {
        method: 'GET',
        credentials: 'include',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    })
    .then(response => response.json())
}
export default getArtistAlbum