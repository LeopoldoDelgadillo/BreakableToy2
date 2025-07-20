export const getArtist = (artistID: string): Promise<JSON> => {
    return fetch(`http://127.0.0.1:9090/artists/${artistID}`, {
        method: 'GET',
        credentials: 'include',
        headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    })
    .then(response => response.json())
}
export default getArtist